/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.UbicacionesGeograficas;
import Entidades.Empresas;
import Entidades.Ciudades;
import Entidades.SucursalesPila;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarUbicacionesGeograficasInterface;
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
public class ControlUbicacionesGeograficas implements Serializable {

    @EJB
    AdministrarUbicacionesGeograficasInterface administrarUbicacionesGeograficas;
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
//EMPRESA
    private List<Empresas> listaEmpresas;
    private List<Empresas> filtradoListaEmpresas;

    private Empresas empresaSeleccionada;
    private int banderaModificacionEmpresa;
    private int indiceEmpresaMostrada;
//LISTA CENTRO COSTO
    private List<UbicacionesGeograficas> listUbicacionesGeograficasPorEmpresa;
    private List<UbicacionesGeograficas> filtrarUbicacionesGeograficas;
    private List<UbicacionesGeograficas> crearUbicacionesGeograficas;
    private List<UbicacionesGeograficas> modificarUbicacionesGeograficas;
    private List<UbicacionesGeograficas> borrarUbicacionesGeograficas;
    private UbicacionesGeograficas nuevaUbicacionGeografica;
    private UbicacionesGeograficas duplicarUbicacionGeografica;
    private UbicacionesGeograficas editarCentroCosto;
    private UbicacionesGeograficas ubicacionGeograficaSeleccionada;

    private Column codigoCC,
            nombreCentroCosto,
            tipoCentroCosto,
            codigoCTT,
            telefono,
            fax,
            observacion,
            manoDeObra,
            actividadEconomica,
            sucursalPila,
            codigoAT;

    //AUTOCOMPLETAR
    private String ciudadesAutocompletar;
    private List<Ciudades> listaCiudades;
    private List<Ciudades> filtradoCiudades;
    private Ciudades ciudadSeleccionada;
    private String sucursalPilaAutocompletar;
    private List<SucursalesPila> listaSucursalesPilas;
    private List<SucursalesPila> filtradoSucursalesPilas;
    private SucursalesPila sucursalesPilasSeleccionada;
    private List<UbicacionesGeograficas> filterUbicacionesGeograficasPorEmpresa;
    private String nuevoTipoCCAutoCompletar;
    private Empresas backUpEmpresaActual;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private UbicacionesGeograficas UbicacionesGeograficasPorEmpresaSeleccionado;
    private boolean banderaSeleccionUbicacionesGeograficasPorEmpresa;
    private int tamano;
    private String infoRegistro;

    public ControlUbicacionesGeograficas() {
        permitirIndex = true;
        listaEmpresas = null;
        empresaSeleccionada = null;
        indiceEmpresaMostrada = 0;
        listUbicacionesGeograficasPorEmpresa = null;
        crearUbicacionesGeograficas = new ArrayList<UbicacionesGeograficas>();
        modificarUbicacionesGeograficas = new ArrayList<UbicacionesGeograficas>();
        borrarUbicacionesGeograficas = new ArrayList<UbicacionesGeograficas>();
        editarCentroCosto = new UbicacionesGeograficas();
        nuevaUbicacionGeografica = new UbicacionesGeograficas();
        nuevaUbicacionGeografica.setCiudad(new Ciudades());
        nuevaUbicacionGeografica.setSucursalPila(new SucursalesPila());
        duplicarUbicacionGeografica = new UbicacionesGeograficas();
        listaCiudades = null;
        aceptar = true;
        filtradoListaEmpresas = null;
        guardado = true;
        banderaSeleccionUbicacionesGeograficasPorEmpresa = false;
        listaSucursalesPilas = null;
        tamano = 270;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarUbicacionesGeograficas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
    private String paginaAnterior;

    public void recibirPagina(String pagina) {
        paginaAnterior = pagina;
    }

    public String redirigirPaginaAnterior() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarUbicacionesGeograficas.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS eventoFiltrar ERROR===" + e);
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("BETA CENTRO COSTO TIPO LISTA = " + tipoLista);
        System.err.println("PERMITIR INDEX = " + permitirIndex);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            System.err.println("CAMBIAR INDICE CUALCELDA = " + cualCelda);
            secRegistro = listUbicacionesGeograficasPorEmpresa.get(index).getSecuencia();
            System.err.println("Sec Registro = " + secRegistro);
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listUbicacionesGeograficasPorEmpresa.get(index).getCodigo();
                    System.err.println("backUpCodigo  = " + backUpCodigo);

                } else {
                    backUpCodigo = filtrarUbicacionesGeograficas.get(index).getCodigo();
                    System.err.println("backUpCodigo  = " + backUpCodigo);
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listUbicacionesGeograficasPorEmpresa.get(index).getDescripcion();
                    System.err.println("backUpDescripcion = " + backUpDescripcion);

                } else {
                    backUpDescripcion = filtrarUbicacionesGeograficas.get(index).getDescripcion();
                    System.err.println("backUpDescripcion = " + backUpDescripcion);
                }
            }
            if (cualCelda == 2) {
                if (tipoLista == 0) {
                    ciudadesAutocompletar = listUbicacionesGeograficasPorEmpresa.get(index).getCiudad().getNombre();
                    System.err.println("ciudadesAutocompletar = " + ciudadesAutocompletar);

                } else {
                    ciudadesAutocompletar = filtrarUbicacionesGeograficas.get(index).getCiudad().getNombre();
                }
            }
            if (cualCelda == 9) {
                if (tipoLista == 0) {
                    sucursalPilaAutocompletar = listUbicacionesGeograficasPorEmpresa.get(index).getSucursalPila().getDescripcion();
                } else {
                    sucursalPilaAutocompletar = filtrarUbicacionesGeograficas.get(index).getSucursalPila().getDescripcion();
                }
            }
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void modificandoUbicacionGeografica(int indice, String confirmarCambio, String valorConfirmar) {

        System.err.println("ENTRE A MODIFICAR CENTROCOSTO");
        index = indice;
        banderaModificacionEmpresa = 1;
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
                if (!crearUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(indice))) {
                    if (listUbicacionesGeograficasPorEmpresa.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listUbicacionesGeograficasPorEmpresa.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listUbicacionesGeograficasPorEmpresa.size(); j++) {
                            if (j != indice) {
                                if (listUbicacionesGeograficasPorEmpresa.get(indice).getCodigo().equals(listUbicacionesGeograficasPorEmpresa.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listUbicacionesGeograficasPorEmpresa.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (listUbicacionesGeograficasPorEmpresa.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listUbicacionesGeograficasPorEmpresa.get(indice).setDescripcion(backUpDescripcion);
                        banderita1 = false;
                    } else if (listUbicacionesGeograficasPorEmpresa.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listUbicacionesGeograficasPorEmpresa.get(indice).setDescripcion(backUpDescripcion);
                        banderita1 = false;
                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarUbicacionesGeograficas.isEmpty()) {
                            modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(indice));
                        } else if (!modificarUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(indice))) {
                            modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;

                        }
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listUbicacionesGeograficasPorEmpresa.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listUbicacionesGeograficasPorEmpresa.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listUbicacionesGeograficasPorEmpresa.size(); j++) {
                            if (j != indice) {
                                if (listUbicacionesGeograficasPorEmpresa.get(indice).getCodigo().equals(listUbicacionesGeograficasPorEmpresa.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listUbicacionesGeograficasPorEmpresa.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (listUbicacionesGeograficasPorEmpresa.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listUbicacionesGeograficasPorEmpresa.get(indice).setDescripcion(backUpDescripcion);
                        banderita1 = false;
                    } else if (listUbicacionesGeograficasPorEmpresa.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listUbicacionesGeograficasPorEmpresa.get(indice).setDescripcion(backUpDescripcion);
                        banderita1 = false;
                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {

                        if (guardado == true) {
                            guardado = false;

                        }
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else {

                if (!crearUbicacionesGeograficas.contains(filtrarUbicacionesGeograficas.get(indice))) {
                    if (filtrarUbicacionesGeograficas.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarUbicacionesGeograficas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listUbicacionesGeograficasPorEmpresa.size(); j++) {
                            if (j != indice) {
                                if (filtrarUbicacionesGeograficas.get(indice).getCodigo().equals(listUbicacionesGeograficasPorEmpresa.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < filtrarUbicacionesGeograficas.size(); j++) {
                            if (j != indice) {
                                if (filtrarUbicacionesGeograficas.get(indice).getCodigo().equals(filtrarUbicacionesGeograficas.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarUbicacionesGeograficas.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }
                    }

                    if (filtrarUbicacionesGeograficas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarUbicacionesGeograficas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarUbicacionesGeograficas.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarUbicacionesGeograficas.get(indice).setDescripcion(backUpDescripcion);
                        banderita1 = false;
                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarUbicacionesGeograficas.isEmpty()) {
                            modificarUbicacionesGeograficas.add(filtrarUbicacionesGeograficas.get(indice));
                        } else if (!modificarUbicacionesGeograficas.contains(filtrarUbicacionesGeograficas.get(indice))) {
                            modificarUbicacionesGeograficas.add(filtrarUbicacionesGeograficas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (filtrarUbicacionesGeograficas.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarUbicacionesGeograficas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listUbicacionesGeograficasPorEmpresa.size(); j++) {
                            if (j != indice) {
                                if (filtrarUbicacionesGeograficas.get(indice).getCodigo().equals(listUbicacionesGeograficasPorEmpresa.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < filtrarUbicacionesGeograficas.size(); j++) {
                            if (j != indice) {
                                if (filtrarUbicacionesGeograficas.get(indice).getCodigo().equals(filtrarUbicacionesGeograficas.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            filtrarUbicacionesGeograficas.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }
                    }

                    if (filtrarUbicacionesGeograficas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarUbicacionesGeograficas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (filtrarUbicacionesGeograficas.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        filtrarUbicacionesGeograficas.get(indice).setDescripcion(backUpDescripcion);
                        banderita1 = false;
                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }

            }
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
        } else if (confirmarCambio.equalsIgnoreCase("CIUDADES")) {
            System.err.println("ENTRE A MODIFICAR TIPO CENTRO COSTO, CONFIRMAR CAMBIO ES TIPOCENTROCOSTO");
            System.err.println("ciudadesAutocompletar " + ciudadesAutocompletar);
            System.err.println("listUbicacionesGeograficasPorEmpresa.get(indice).getCiudad().getDescripcion() " + listUbicacionesGeograficasPorEmpresa.get(indice).getCiudad().getNombre());
            if (!listUbicacionesGeograficasPorEmpresa.get(indice).getCiudad().getNombre().equals("")) {
                if (tipoLista == 0) {
                    System.err.println("COMPLETAR   ciudadesAutocompletar " + ciudadesAutocompletar);
                    listUbicacionesGeograficasPorEmpresa.get(indice).getCiudad().setNombre(ciudadesAutocompletar);
                } else {

                    filtrarUbicacionesGeograficas.get(indice).getCiudad().setNombre(ciudadesAutocompletar);
                }
                getListaCiudades();
                for (int i = 0; i < listaCiudades.size(); i++) {
                    if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listUbicacionesGeograficasPorEmpresa.get(indice).setCiudad(listaCiudades.get(indiceUnicoElemento));
                        if (modificarUbicacionesGeograficas.isEmpty()) {
                            modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(indice));
                        } else if (!modificarUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(indice))) {
                            modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    } else {
                        filtrarUbicacionesGeograficas.get(indice).setCiudad(listaCiudades.get(indiceUnicoElemento));
                        if (modificarUbicacionesGeograficas.isEmpty()) {
                            modificarUbicacionesGeograficas.add(filtrarUbicacionesGeograficas.get(indice));
                        } else if (!modificarUbicacionesGeograficas.contains(filtrarUbicacionesGeograficas.get(indice))) {
                            modificarUbicacionesGeograficas.add(filtrarUbicacionesGeograficas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    listaCiudades.clear();
                    listaCiudades = null;
                    getListaCiudades();
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (tipoLista == 0) {
                    listUbicacionesGeograficasPorEmpresa.get(indice).setCiudad(new Ciudades());
                    if (modificarUbicacionesGeograficas.isEmpty()) {
                        modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(indice));
                    } else if (!modificarUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(indice))) {
                        modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                } else {
                    filtrarUbicacionesGeograficas.get(indice).setCiudad(new Ciudades());
                    if (modificarUbicacionesGeograficas.isEmpty()) {
                        modificarUbicacionesGeograficas.add(filtrarUbicacionesGeograficas.get(indice));
                    } else if (!modificarUbicacionesGeograficas.contains(filtrarUbicacionesGeograficas.get(indice))) {
                        modificarUbicacionesGeograficas.add(filtrarUbicacionesGeograficas.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }

        } else if (confirmarCambio.equalsIgnoreCase("SUCURSALESPILA")) {
            System.err.println("ENTRO A MODIFICANDOUBICACIONESGEOGGRAFICAS VALOR A CONFIRMAR SUCURSALESPILA");
            System.err.println("sucursalPilaAutocompletar " + sucursalPilaAutocompletar);
            System.err.println("listUbicacionesGeograficasPorEmpresa.get(indice).getCiudad().getDescripcion() " + listUbicacionesGeograficasPorEmpresa.get(indice).getSucursalPila().getDescripcion());
            if (!listUbicacionesGeograficasPorEmpresa.get(indice).getSucursalPila().getDescripcion().equals("")) {
                if (tipoLista == 0) {
                    System.err.println("COMPLETAR   ciudadesAutocompletar " + sucursalPilaAutocompletar);
                    listUbicacionesGeograficasPorEmpresa.get(indice).getSucursalPila().setDescripcion(sucursalPilaAutocompletar);
                } else {

                    filtrarUbicacionesGeograficas.get(indice).getSucursalPila().setDescripcion(sucursalPilaAutocompletar);
                }
                getListaSucursalesPilas();
                for (int i = 0; i < listaSucursalesPilas.size(); i++) {
                    if (listaSucursalesPilas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listUbicacionesGeograficasPorEmpresa.get(indice).setSucursalPila(listaSucursalesPilas.get(indiceUnicoElemento));
                        if (modificarUbicacionesGeograficas.isEmpty()) {
                            modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(indice));
                        } else if (!modificarUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(indice))) {
                            modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    } else {
                        filtrarUbicacionesGeograficas.get(indice).setSucursalPila(listaSucursalesPilas.get(indiceUnicoElemento));
                        if (modificarUbicacionesGeograficas.isEmpty()) {
                            modificarUbicacionesGeograficas.add(filtrarUbicacionesGeograficas.get(indice));
                        } else if (!modificarUbicacionesGeograficas.contains(filtrarUbicacionesGeograficas.get(indice))) {
                            modificarUbicacionesGeograficas.add(filtrarUbicacionesGeograficas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    listaCiudades.clear();
                    listaCiudades = null;
                    getListaCiudades();
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (tipoLista == 0) {
                    listUbicacionesGeograficasPorEmpresa.get(indice).setSucursalPila(new SucursalesPila());
                    if (modificarUbicacionesGeograficas.isEmpty()) {
                        modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(indice));
                    } else if (!modificarUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(indice))) {
                        modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                } else {
                    filtrarUbicacionesGeograficas.get(indice).setSucursalPila(new SucursalesPila());
                    if (modificarUbicacionesGeograficas.isEmpty()) {
                        modificarUbicacionesGeograficas.add(filtrarUbicacionesGeograficas.get(indice));
                    } else if (!modificarUbicacionesGeograficas.contains(filtrarUbicacionesGeograficas.get(indice))) {
                        modificarUbicacionesGeograficas.add(filtrarUbicacionesGeograficas.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
            }

        }
        RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void cancelarModificacion() {
        try {
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("entre a CONTROLUBICACIONESGEOGRAFICAS.cancelarModificacion");
            if (bandera == 1) {
                //CERRAR FILTRADO
                //0
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                nombreCentroCosto = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:nombreCentroCosto");
                nombreCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                tipoCentroCosto = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:tipoCentroCosto");
                tipoCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                codigoCTT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoCTT");
                codigoCTT.setFilterStyle("display: none; visibility: hidden;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:telefono");
                telefono.setFilterStyle("display: none; visibility: hidden;");
                fax = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:fax");
                fax.setFilterStyle("display: none; visibility: hidden;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:observacion");
                observacion.setFilterStyle("display: none; visibility: hidden;");
                manoDeObra = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:manoDeObra");
                manoDeObra.setFilterStyle("display: none; visibility: hidden;");
                actividadEconomica = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:actividadEconomica");
                actividadEconomica.setFilterStyle("display: none; visibility: hidden;");
                sucursalPila = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:sucursalPila");
                sucursalPila.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarUbicacionesGeograficas = null;
                tipoLista = 0;
            }

            borrarUbicacionesGeograficas.clear();
            crearUbicacionesGeograficas.clear();
            modificarUbicacionesGeograficas.clear();
            index = -1;
            k = 0;
            listUbicacionesGeograficasPorEmpresa = null;
            guardado = true;
            permitirIndex = true;
            RequestContext context = RequestContext.getCurrentInstance();
            banderaModificacionEmpresa = 0;
            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
            }
            getListUbicacionesGeograficasPorEmpresa();
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
            if (listUbicacionesGeograficasPorEmpresa == null || listUbicacionesGeograficasPorEmpresa.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listUbicacionesGeograficasPorEmpresa.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception E) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void salir() {
        try {
            FacesContext c = FacesContext.getCurrentInstance();
            System.out.println("entre a CONTROLUBICACIONESGEOGRAFICAS.cancelarModificacion");
            if (bandera == 1) {
                //CERRAR FILTRADO
                //0
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                nombreCentroCosto = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:nombreCentroCosto");
                nombreCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                tipoCentroCosto = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:tipoCentroCosto");
                tipoCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                codigoCTT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoCTT");
                codigoCTT.setFilterStyle("display: none; visibility: hidden;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:telefono");
                telefono.setFilterStyle("display: none; visibility: hidden;");
                fax = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:fax");
                fax.setFilterStyle("display: none; visibility: hidden;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:observacion");
                observacion.setFilterStyle("display: none; visibility: hidden;");
                manoDeObra = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:manoDeObra");
                manoDeObra.setFilterStyle("display: none; visibility: hidden;");
                actividadEconomica = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:actividadEconomica");
                actividadEconomica.setFilterStyle("display: none; visibility: hidden;");
                sucursalPila = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:sucursalPila");
                sucursalPila.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarUbicacionesGeograficas = null;
                tipoLista = 0;
            }

            borrarUbicacionesGeograficas.clear();
            crearUbicacionesGeograficas.clear();
            modificarUbicacionesGeograficas.clear();
            index = -1;
            k = 0;
            listUbicacionesGeograficasPorEmpresa = null;
            guardado = true;
            permitirIndex = true;
            RequestContext context = RequestContext.getCurrentInstance();
            banderaModificacionEmpresa = 0;
            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
            }
            getListUbicacionesGeograficasPorEmpresa();
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
            if (listUbicacionesGeograficasPorEmpresa == null || listUbicacionesGeograficasPorEmpresa.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listUbicacionesGeograficasPorEmpresa.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception E) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.ModificarModificacion ERROR====================" + E.getMessage());
        }
    }

    public void mostrarInfo(int indice, int celda) {
        if (permitirIndex == true) {
            RequestContext context = RequestContext.getCurrentInstance();

            banderaModificacionEmpresa = 1;
            index = indice;
            cualCelda = celda;
            secRegistro = listUbicacionesGeograficasPorEmpresa.get(index).getSecuencia();
            System.out.println("Modificar Zona : " + listUbicacionesGeograficasPorEmpresa.get(indice).getZona());
            if (!crearUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(indice))) {
                if (modificarUbicacionesGeograficas.isEmpty()) {
                    modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(indice));
                } else if (!modificarUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(indice))) {
                    modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(indice));
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);

    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.asignarIndex \n");
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
                RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
                dig = -1;
            }
            if (dig == 9) {
                System.out.println("Secuencia Empresa Seleccionada = " + empresaSeleccionada.getSecuencia());
                listaSucursalesPilas = null;
                getListaSucursalesPilas();
                RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
                RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
                dig = -1;
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS ASIGNARINDEX ERROR :" + e);
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarCiudad() {
        System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS ACTUALIZARCIUDAD \n");
        banderaModificacionEmpresa = 1;
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.ACTUALIZARCIUDAD TIPOACTUALIZACION : " + tipoActualizacion);
        if (tipoActualizacion == 0) {
            listUbicacionesGeograficasPorEmpresa.get(index).setCiudad(ciudadSeleccionada);
            if (!crearUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(index))) {
                if (modificarUbicacionesGeograficas.isEmpty()) {
                    modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(index));
                } else if (!modificarUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(index))) {
                    modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(index));
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }

            }
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
        } else if (tipoActualizacion == 1) {
            // context.reset("formularioDialogos:nuevaTipoCentroCostos");
            System.out.println("Entro actualizar centro costo nuevo rgistro");
            nuevaUbicacionGeografica.setCiudad(ciudadSeleccionada);
            System.out.println("Centro Costo Seleccionado: " + nuevaUbicacionGeografica.getCiudad().getNombre());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoCentroCostos");
        } else if (tipoActualizacion == 2) {
            duplicarUbicacionGeografica.setCiudad(ciudadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoCentroCostos");

        }
        filtradoCiudades = null;
        ciudadSeleccionada = null;
        aceptar = true;
        index = -1;
        tipoActualizacion = -1;
        permitirIndex = true;

        context.reset("formularioDialogos:lovTipoUbicacionesGeograficas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoUbicacionesGeograficas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').hide()");

    }

    public void cancelarCambioCiudad() {
        filtradoCiudades = null;
        ciudadSeleccionada = null;
        aceptar = true;
        index = -1;
        tipoActualizacion = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovTipoUbicacionesGeograficas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoUbicacionesGeograficas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').hide()");

    }

    public void actualizarSucursalPila() {
        System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS ACTUALIZARSUCURSALPILA \n");
        banderaModificacionEmpresa = 1;
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.ACTUALIZARSUCURSALPILA TIPOACTUALIZACION : " + tipoActualizacion);
        if (tipoActualizacion == 0) {
            listUbicacionesGeograficasPorEmpresa.get(index).setSucursalPila(sucursalesPilasSeleccionada);
            if (!crearUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(index))) {
                if (modificarUbicacionesGeograficas.isEmpty()) {
                    modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(index));
                } else if (!modificarUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(index))) {
                    modificarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(index));
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }

            }
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
        } else if (tipoActualizacion == 1) {
            // context.reset("formularioDialogos:nuevaTipoCentroCostos");
            System.out.println("Entro actualizar centro costo nuevo rgistro");
            nuevaUbicacionGeografica.setSucursalPila(sucursalesPilasSeleccionada);
            System.out.println("Centro Costo Seleccionado: " + nuevaUbicacionGeografica.getSucursalPila().getDescripcion());
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSucursal");
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");

        } else if (tipoActualizacion == 2) {
            duplicarUbicacionGeografica.setSucursalPila(sucursalesPilasSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoCentroCostos");
        }
        filtradoCiudades = null;
        ciudadSeleccionada = null;
        aceptar = true;
        index = -1;
        tipoActualizacion = -1;
        permitirIndex = true;

        context.reset("formularioDialogos:lovSucursalesPila:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSucursalesPila').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').hide()");

    }

    public void cancelarCambioSucursalPila() {
        filtradoSucursalesPilas = null;
        sucursalesPilasSeleccionada = null;
        aceptar = true;
        index = -1;
        tipoActualizacion = -1;
        listaSucursalesPilas = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovSucursalesPila:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSucursalesPila').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').hide()");
    }

    public void seleccionUbicacionesGeograficasPorEmpresa() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();

            if (guardado == true) {
                listUbicacionesGeograficasPorEmpresa.clear();
                System.err.println("seleccionUbicacionesGeograficasPorEmpresa " + UbicacionesGeograficasPorEmpresaSeleccionado.getDescripcion());
                listUbicacionesGeograficasPorEmpresa.add(UbicacionesGeograficasPorEmpresaSeleccionado);
                System.err.println("listUbicacionesGeograficasPorEmpresa tamaño " + listUbicacionesGeograficasPorEmpresa.size());
                System.err.println("listUbicacionesGeograficasPorEmpresa nombre " + listUbicacionesGeograficasPorEmpresa.get(0).getDescripcion());
                UbicacionesGeograficasPorEmpresaSeleccionado = null;
                filterUbicacionesGeograficasPorEmpresa = null;
                aceptar = true;
                banderaModificacionEmpresa = 1;
                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
                RequestContext.getCurrentInstance().execute("PF('buscarUbicacionesGeograficasDialogo').hide()");
                context.reset("formularioDialogos:lovUbicacionesGeograficas:globalFilter");
            } /*else {
             System.err.println("listUbicacionesGeograficasPorEmpresa tamaño " + listUbicacionesGeograficasPorEmpresa.size());
             System.err.println("listUbicacionesGeograficasPorEmpresa nombre " + listUbicacionesGeograficasPorEmpresa.get(0).getDescripcion());
             banderaSeleccionUbicacionesGeograficasPorEmpresa = true;
             RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
             UbicacionesGeograficasPorEmpresaSeleccionado = null;
             listUbicacionesGeograficasPorEmpresa.clear();
             System.err.println("seleccionUbicacionesGeograficasPorEmpresa " + UbicacionesGeograficasPorEmpresaSeleccionado.getDescripcion());
             listUbicacionesGeograficasPorEmpresa.add(UbicacionesGeograficasPorEmpresaSeleccionado);
             filterUbicacionesGeograficasPorEmpresa = null;
             aceptar = true;
             banderaModificacionEmpresa = 0;
             RequestContext.getCurrentInstance().execute("PF('buscarUbicacionesGeograficasDialogo').hide()");
             context.reset("formularioDialogos:lovUbicacionesGeograficas:globalFilter");
             }*/


        } catch (Exception e) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.seleccionaVigencia ERROR====" + e);
        }
    }

    public void cancelarSeleccionCentroCostoPorEmpresa() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            UbicacionesGeograficasPorEmpresaSeleccionado = null;
            filterUbicacionesGeograficasPorEmpresa = null;
            aceptar = true;
            index = -1;
            tipoActualizacion = -1;
            RequestContext.getCurrentInstance().update("form:aceptarNCC");

        } catch (Exception e) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.cancelarSeleccionVigencia ERROR====" + e);
        }
    }
    private String nuevoSucursalPilaAutocompletar;

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        System.out.println("1...");
        if (Campo.equals("CIUDADES")) {
            if (tipoNuevo == 1) {
                nuevoTipoCCAutoCompletar = nuevaUbicacionGeografica.getCiudad().getNombre();
            } else if (tipoNuevo == 2) {
                nuevoTipoCCAutoCompletar = duplicarUbicacionGeografica.getCiudad().getNombre();
            }

        }
        if (Campo.equals("SUCURSALESPILA")) {
            if (tipoNuevo == 1) {
                nuevoSucursalPilaAutocompletar = nuevaUbicacionGeografica.getSucursalPila().getDescripcion();
            } else if (tipoNuevo == 2) {
                nuevoSucursalPilaAutocompletar = duplicarUbicacionGeografica.getSucursalPila().getDescripcion();
            }

        }
    }

    public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CIUDADES")) {
            System.out.println(" nuevoTipoCentroCosto    Entro al if 'Centro costo'");
            System.out.println("NOMBRE CENTRO COSTO: " + nuevaUbicacionGeografica.getCiudad().getNombre());

            System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("valorConfirmar: " + valorConfirmar);
            System.out.println("nuevoTipoCCAutoCompletar: " + nuevoTipoCCAutoCompletar);
            nuevaUbicacionGeografica.getCiudad().setNombre(nuevoTipoCCAutoCompletar);
            getListaCiudades();
            for (int i = 0; i < listaCiudades.size(); i++) {
                if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
                nuevaUbicacionGeografica.setCiudad(listaCiudades.get(indiceUnicoElemento));
                listaCiudades = null;
                getListaCiudades();
            } else {
                RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
                tipoActualizacion = tipoNuevo;
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoCentroCostos");
        }
        if (confirmarCambio.equalsIgnoreCase("SUCURSALESPILA")) {
            System.out.println(" NUEVO CAMPO SUCURSALPILA ");
            System.out.println("NOMBRE SUCURSAL : " + nuevaUbicacionGeografica.getSucursalPila().getDescripcion());

            System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("valorConfirmar: " + valorConfirmar);
            System.out.println("nuevoSucursalPilaAutocompletar: " + nuevoSucursalPilaAutocompletar);
            nuevaUbicacionGeografica.getSucursalPila().setDescripcion(nuevoSucursalPilaAutocompletar);
            getListaSucursalesPilas();
            for (int i = 0; i < listaSucursalesPilas.size(); i++) {
                if (listaSucursalesPilas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
                nuevaUbicacionGeografica.setSucursalPila(listaSucursalesPilas.get(indiceUnicoElemento));
                listaSucursalesPilas = null;
                getListaCiudades();
            } else {
                RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
                RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
                tipoActualizacion = tipoNuevo;
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSucursal");
        }

    }

    public void asignarVariableSucursalesPila(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
        RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
    }

    public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        System.out.println("entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CIUDADES")) {
            if (!duplicarUbicacionGeografica.getCiudad().getNombre().equals("")) {
                System.out.println("Entro al if 'Centro costo'");
                System.out.println("NOMBRE CENTRO COSTO: " + duplicarUbicacionGeografica.getCiudad().getNombre());

                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoTipoCCAutoCompletar: " + nuevoTipoCCAutoCompletar);
                duplicarUbicacionGeografica.getCiudad().setNombre(nuevoTipoCCAutoCompletar);
                for (int i = 0; i < listaCiudades.size(); i++) {
                    if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarUbicacionGeografica.setCiudad(listaCiudades.get(indiceUnicoElemento));
                    listaCiudades = null;
                    getListaCiudades();
                } else {
                    RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoLista == 0) {
                    listUbicacionesGeograficasPorEmpresa.get(index).getCiudad().setNombre(nuevoTipoCCAutoCompletar);
                } else {
                    filtrarUbicacionesGeograficas.get(index).getCiudad().setNombre(nuevoTipoCCAutoCompletar);
                }
                duplicarUbicacionGeografica.setCiudad(new Ciudades());
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoCentroCostos");
        }
        if (confirmarCambio.equalsIgnoreCase("SUCURSALESPILA")) {
            if (!duplicarUbicacionGeografica.getSucursalPila().getDescripcion().equals("")) {
                System.out.println("Entro al if 'SUCURSALPILA'");
                System.out.println("SUCURSAL PILA : " + duplicarUbicacionGeografica.getSucursalPila().getDescripcion());

                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoSucursalPilaAutocompletar: " + nuevoSucursalPilaAutocompletar);
                duplicarUbicacionGeografica.getSucursalPila().setDescripcion(nuevoSucursalPilaAutocompletar);
                for (int i = 0; i < listaSucursalesPilas.size(); i++) {
                    if (listaSucursalesPilas.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarUbicacionGeografica.setSucursalPila(listaSucursalesPilas.get(indiceUnicoElemento));
                    listaSucursalesPilas = null;
                    getListaCiudades();
                } else {
                    RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoLista == 0) {
                    listUbicacionesGeograficasPorEmpresa.get(index).getSucursalPila().setDescripcion(nuevoSucursalPilaAutocompletar);
                } else {
                    filtrarUbicacionesGeograficas.get(index).getSucursalPila().setDescripcion(nuevoSucursalPilaAutocompletar);
                }
                duplicarUbicacionGeografica.setSucursalPila(new SucursalesPila());
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSucursalPila");
            index = -1;
        }

    }

    public void asignarVariableTiposCC(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
        RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
    }

    public void limpiarNuevaUbicacionGeografica() {
        System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.limpiarNuevaUbicacionGeograficas \n");
        try {
            nuevaUbicacionGeografica = new UbicacionesGeograficas();
            index = -1;
        } catch (Exception e) {
            System.out.println("Error CONTROLUBICACIONESGEOGRAFICAS.LimpiarNuevaUbicacionGeograficas ERROR=============================" + e);
        }
    }

    public void agregarNuevaUbicacionGeografica() {
        System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.agregarNuevaUbicacionGeograficas \n");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        banderaModificacionEmpresa = 1;
        if (nuevaUbicacionGeografica.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + "   * Un codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevaUbicacionGeografica.getCodigo());

            for (int x = 0; x < listUbicacionesGeograficasPorEmpresa.size(); x++) {
                if (listUbicacionesGeograficasPorEmpresa.get(x).getCodigo().equals(nuevaUbicacionGeografica.getCodigo())) {
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
        if (nuevaUbicacionGeografica.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *una Descripción \n";

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (nuevaUbicacionGeografica.getCiudad().getNombre() == null) {
            nuevaUbicacionGeografica.setCiudad(new Ciudades());
        }
        if (nuevaUbicacionGeografica.getSucursalPila().getDescripcion() == null) {
            nuevaUbicacionGeografica.setSucursalPila(new SucursalesPila());
        }
        if (contador == 2) {
            k++;
            l = BigInteger.valueOf(k);
            nuevaUbicacionGeografica.setSecuencia(l);
            nuevaUbicacionGeografica.setEmpresa(empresaSeleccionada);
            if (crearUbicacionesGeograficas.contains(nuevaUbicacionGeografica)) {
                System.out.println("Ya lo contengo.");
            } else {
                crearUbicacionesGeograficas.add(nuevaUbicacionGeografica);

            }
            System.out.println("CONTROLUBICACIONESGEOGRAFICAS secuencia : " + nuevaUbicacionGeografica.getSecuencia());
            System.out.println("CONTROLUBICACIONESGEOGRAFICAS empresa : " + nuevaUbicacionGeografica.getEmpresa());
            System.out.println("CONTROLUBICACIONESGEOGRAFICAS codigo : " + nuevaUbicacionGeografica.getCodigo());
            System.out.println("CONTROLUBICACIONESGEOGRAFICAS descripcion : " + nuevaUbicacionGeografica.getDescripcion());
            //System.out.println("CONTROLUBICACIONESGEOGRAFICAS ciudad : " + nuevaUbicacionGeografica.getCiudad().getNombre());
            System.out.println("CONTROLUBICACIONESGEOGRAFICAS direccion : " + nuevaUbicacionGeografica.getDireccion());
            System.out.println("CONTROLUBICACIONESGEOGRAFICAS telefono : " + nuevaUbicacionGeografica.getTelefono());
            System.out.println("CONTROLUBICACIONESGEOGRAFICAS fax : " + nuevaUbicacionGeografica.getFax());
            System.out.println("CONTROLUBICACIONESGEOGRAFICAS observacion : " + nuevaUbicacionGeografica.getObservacion());
            System.out.println("CONTROLUBICACIONESGEOGRAFICAS zona : " + nuevaUbicacionGeografica.getZona());
            System.out.println("CONTROLUBICACIONESGEOGRAFICAS Actividad Economica : " + nuevaUbicacionGeografica.getActividadeconomica());
            //System.out.println("CONTROLUBICACIONESGEOGRAFICAS sucursal : " + nuevaUbicacionGeografica.getSucursalPila().getDescripcion());
            System.out.println("CONTROLUBICACIONESGEOGRAFICAS codigo alternativo : " + nuevaUbicacionGeografica.getCodigoalternativo());

            listUbicacionesGeograficasPorEmpresa.add(nuevaUbicacionGeografica);
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
            infoRegistro = "Cantidad de registros: " + listUbicacionesGeograficasPorEmpresa.size();

            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            nuevaUbicacionGeografica = new UbicacionesGeograficas();
            // index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                nombreCentroCosto = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:nombreCentroCosto");
                nombreCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                tipoCentroCosto = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:tipoCentroCosto");
                tipoCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                codigoCTT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoCTT");
                codigoCTT.setFilterStyle("display: none; visibility: hidden;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:telefono");
                telefono.setFilterStyle("display: none; visibility: hidden;");
                fax = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:fax");
                fax.setFilterStyle("display: none; visibility: hidden;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:observacion");
                observacion.setFilterStyle("display: none; visibility: hidden;");
                manoDeObra = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:manoDeObra");
                manoDeObra.setFilterStyle("display: none; visibility: hidden;");
                actividadEconomica = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:actividadEconomica");
                actividadEconomica.setFilterStyle("display: none; visibility: hidden;");
                sucursalPila = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:sucursalPila");
                sucursalPila.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");

                bandera = 0;
                filtrarUbicacionesGeograficas = null;
                tipoLista = 0;
            }
            mensajeValidacion = " ";
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCentroCostos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void cargarCiudadesNuevoRegistro(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
        } else if (tipoNuevo == 1) {
            tipoActualizacion = 2;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:tiposCentrosCostosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
        }
    }

    public void cargarSucursalesPilaNuevoRegistro(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
        } else if (tipoNuevo == 1) {
            tipoActualizacion = 2;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:sucursalesPilaDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
        }
    }

    public void mostrarDialogoNuevoCiudades() {
        RequestContext context = RequestContext.getCurrentInstance();
        nuevaUbicacionGeografica = new UbicacionesGeograficas();
        nuevaUbicacionGeografica.setCiudad(new Ciudades());
        index = -1;
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUbicacionGeograficas");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCentroCostos').show()");
    }

    public void mostrarDialogoListaEmpresas() {
        RequestContext context = RequestContext.getCurrentInstance();
        index = -1;
        RequestContext.getCurrentInstance().execute("PF('buscarUbicacionesGeograficasDialogo').show()");
    }

    public void duplicandoCentroCostos() {
        //try {
        banderaModificacionEmpresa = 1;
        System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.duplicarUbicacionGeograficas INDEX===" + index);

        if (index >= 0) {
            System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.duplicarUbicacionGeograficas TIPOLISTA===" + tipoLista);

            duplicarUbicacionGeografica = new UbicacionesGeograficas();
            duplicarUbicacionGeografica.setSucursalPila(new SucursalesPila());
            duplicarUbicacionGeografica.setCiudad(new Ciudades());
            k++;
            l = BigInteger.valueOf(k);
            if (tipoLista == 0) {
                duplicarUbicacionGeografica.setSecuencia(l);
                duplicarUbicacionGeografica.setEmpresa(listUbicacionesGeograficasPorEmpresa.get(index).getEmpresa());
                duplicarUbicacionGeografica.setCodigo(listUbicacionesGeograficasPorEmpresa.get(index).getCodigo());
                duplicarUbicacionGeografica.setDescripcion(listUbicacionesGeograficasPorEmpresa.get(index).getDescripcion());
                duplicarUbicacionGeografica.getCiudad().setNombre(listUbicacionesGeograficasPorEmpresa.get(index).getCiudad().getNombre());
                duplicarUbicacionGeografica.setDireccion(listUbicacionesGeograficasPorEmpresa.get(index).getDireccion());
                duplicarUbicacionGeografica.setTelefono(listUbicacionesGeograficasPorEmpresa.get(index).getTelefono());
                duplicarUbicacionGeografica.setFax(listUbicacionesGeograficasPorEmpresa.get(index).getFax());
                duplicarUbicacionGeografica.setObservacion(listUbicacionesGeograficasPorEmpresa.get(index).getObservacion());
                duplicarUbicacionGeografica.setZona(listUbicacionesGeograficasPorEmpresa.get(index).getZona());
                duplicarUbicacionGeografica.setActividadeconomica(listUbicacionesGeograficasPorEmpresa.get(index).getActividadeconomica());
                duplicarUbicacionGeografica.getSucursalPila().setDescripcion(listUbicacionesGeograficasPorEmpresa.get(index).getSucursalPila().getDescripcion());
                duplicarUbicacionGeografica.setCodigoalternativo(listUbicacionesGeograficasPorEmpresa.get(index).getCodigoalternativo());

            }
            if (tipoLista == 1) {

                duplicarUbicacionGeografica.setSecuencia(l);
                duplicarUbicacionGeografica.setEmpresa(filtrarUbicacionesGeograficas.get(index).getEmpresa());
                duplicarUbicacionGeografica.setCodigo(filtrarUbicacionesGeograficas.get(index).getCodigo());
                duplicarUbicacionGeografica.setDescripcion(filtrarUbicacionesGeograficas.get(index).getDescripcion());
                duplicarUbicacionGeografica.getCiudad().setNombre(filtrarUbicacionesGeograficas.get(index).getCiudad().getNombre());
                duplicarUbicacionGeografica.setDireccion(filtrarUbicacionesGeograficas.get(index).getDireccion());
                duplicarUbicacionGeografica.setTelefono(filtrarUbicacionesGeograficas.get(index).getTelefono());
                duplicarUbicacionGeografica.setFax(filtrarUbicacionesGeograficas.get(index).getFax());
                duplicarUbicacionGeografica.setObservacion(filtrarUbicacionesGeograficas.get(index).getObservacion());
                duplicarUbicacionGeografica.setZona(filtrarUbicacionesGeograficas.get(index).getZona());
                duplicarUbicacionGeografica.setActividadeconomica(filtrarUbicacionesGeograficas.get(index).getActividadeconomica());
                duplicarUbicacionGeografica.getSucursalPila().setDescripcion(filtrarUbicacionesGeograficas.get(index).getSucursalPila().getDescripcion());
                duplicarUbicacionGeografica.setCodigoalternativo(filtrarUbicacionesGeograficas.get(index).getCodigoalternativo());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUbicacionGeograficas");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCentroCostos').show()");
            //index = -1;
        }
        /* } catch (Exception e) {
         System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.DuplicarCentroCostos ERROR===============" + e);
         }*/
    }

    public void limpiarDuplicarCentroCostos() {
        System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.limpiarDuplicarCentroCostos \n");
        try {
            duplicarUbicacionGeografica = new UbicacionesGeograficas();
        } catch (Exception e) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.limpiarDuplicarCentroCostos ERROR========" + e);
        }

    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR CONTROLTIPOSCENTROSCOSTOS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;

        if (duplicarUbicacionGeografica.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + "   * Un codigo \n";

        } else {
            System.out.println("codigo en Ubicacion Geografica: " + duplicarUbicacionGeografica.getCodigo());

            for (int x = 0; x < listUbicacionesGeograficasPorEmpresa.size(); x++) {
                if (listUbicacionesGeograficasPorEmpresa.get(x).getCodigo().equals(duplicarUbicacionGeografica.getCodigo())) {
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
        if (duplicarUbicacionGeografica.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   * Un nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarUbicacionGeografica.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   * Un nombre \n";

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarUbicacionGeografica.getCiudad().getSecuencia() == null) {
            duplicarUbicacionGeografica.setCiudad(new Ciudades());

        }

        if (contador == 2) {
            if (crearUbicacionesGeograficas.contains(duplicarUbicacionGeografica)) {
                System.out.println("Ya lo contengo.");
            } else {
                listUbicacionesGeograficasPorEmpresa.add(duplicarUbicacionGeografica);
            }
            crearUbicacionesGeograficas.add(duplicarUbicacionGeografica);
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
            infoRegistro = "Cantidad de registros: " + listUbicacionesGeograficasPorEmpresa.size();

            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                nombreCentroCosto = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:nombreCentroCosto");
                nombreCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                tipoCentroCosto = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:tipoCentroCosto");
                tipoCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                codigoCTT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoCTT");
                codigoCTT.setFilterStyle("display: none; visibility: hidden;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:telefono");
                telefono.setFilterStyle("display: none; visibility: hidden;");
                fax = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:fax");
                fax.setFilterStyle("display: none; visibility: hidden;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:observacion");
                observacion.setFilterStyle("display: none; visibility: hidden;");
                manoDeObra = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:manoDeObra");
                manoDeObra.setFilterStyle("display: none; visibility: hidden;");
                actividadEconomica = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:actividadEconomica");
                actividadEconomica.setFilterStyle("display: none; visibility: hidden;");
                sucursalPila = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:sucursalPila");
                sucursalPila.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");

                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
                bandera = 0;
                filtrarUbicacionesGeograficas = null;
                tipoLista = 0;
            }
            duplicarUbicacionGeografica = new UbicacionesGeograficas();
            duplicarUbicacionGeografica.setCiudad(new Ciudades());
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCentroCostos').hide()");
            mensajeValidacion = " ";
            banderaModificacionEmpresa = 1;

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    private BigInteger contarAfiliacionesEntidadesUbicacionGeografica;
    private BigInteger contarInspeccionesUbicacionGeografica;
    private BigInteger contarParametrosInformesUbicacionGeografica;
    private BigInteger contarRevisionesUbicacionGeografica;
    private BigInteger contarVigenciasUbicacionesUbicacionGeografica;

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        System.out.println("TIPOLISTA = " + tipoLista);
        try {
            if (tipoLista == 0) {
                contarAfiliacionesEntidadesUbicacionGeografica = administrarUbicacionesGeograficas.contarAfiliacionesEntidadesUbicacionGeografica(listUbicacionesGeograficasPorEmpresa.get(index).getSecuencia());
                contarInspeccionesUbicacionGeografica = administrarUbicacionesGeograficas.contarInspeccionesUbicacionGeografica(listUbicacionesGeograficasPorEmpresa.get(index).getSecuencia());;
                contarParametrosInformesUbicacionGeografica = administrarUbicacionesGeograficas.contarParametrosInformesUbicacionGeografica(listUbicacionesGeograficasPorEmpresa.get(index).getSecuencia());
                contarRevisionesUbicacionGeografica = administrarUbicacionesGeograficas.contarRevisionesUbicacionGeografica(listUbicacionesGeograficasPorEmpresa.get(index).getSecuencia());
                contarVigenciasUbicacionesUbicacionGeografica = administrarUbicacionesGeograficas.contarVigenciasUbicacionesUbicacionGeografica(listUbicacionesGeograficasPorEmpresa.get(index).getSecuencia());
            } else {
                contarAfiliacionesEntidadesUbicacionGeografica = administrarUbicacionesGeograficas.contarAfiliacionesEntidadesUbicacionGeografica(filtrarUbicacionesGeograficas.get(index).getSecuencia());
                contarInspeccionesUbicacionGeografica = administrarUbicacionesGeograficas.contarInspeccionesUbicacionGeografica(filtrarUbicacionesGeograficas.get(index).getSecuencia());;
                contarParametrosInformesUbicacionGeografica = administrarUbicacionesGeograficas.contarParametrosInformesUbicacionGeografica(filtrarUbicacionesGeograficas.get(index).getSecuencia());
                contarRevisionesUbicacionGeografica = administrarUbicacionesGeograficas.contarRevisionesUbicacionGeografica(filtrarUbicacionesGeograficas.get(index).getSecuencia());
                contarVigenciasUbicacionesUbicacionGeografica = administrarUbicacionesGeograficas.contarVigenciasUbicacionesUbicacionGeografica(filtrarUbicacionesGeograficas.get(index).getSecuencia());
            }
            System.out.println("contarAfiliacionesEntidadesUbicacionGeografica : " + contarAfiliacionesEntidadesUbicacionGeografica);
            System.out.println("contarInspeccionesUbicacionGeografica : " + contarInspeccionesUbicacionGeografica);
            System.out.println("contarParametrosInformesUbicacionGeografica : " + contarParametrosInformesUbicacionGeografica);
            System.out.println("contarRevisionesUbicacionGeografica : " + contarRevisionesUbicacionGeografica);
            System.out.println("contarVigenciasUbicacionesUbicacionGeografica : " + contarVigenciasUbicacionesUbicacionGeografica);
            if (contarAfiliacionesEntidadesUbicacionGeografica.equals(new BigInteger("0"))
                    && contarInspeccionesUbicacionGeografica.equals(new BigInteger("0"))
                    && contarParametrosInformesUbicacionGeografica.equals(new BigInteger("0"))
                    && contarRevisionesUbicacionGeografica.equals(new BigInteger("0"))
                    && contarVigenciasUbicacionesUbicacionGeografica.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoUbicacionesGeograficas();
            } else {

                System.out.println("Borrado>0");
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                contarAfiliacionesEntidadesUbicacionGeografica = new BigInteger("-1");
                contarInspeccionesUbicacionGeografica = new BigInteger("-1");
                contarParametrosInformesUbicacionGeografica = new BigInteger("-1");
                contarRevisionesUbicacionGeografica = new BigInteger("-1");
                contarVigenciasUbicacionesUbicacionGeografica = new BigInteger("-1");
            }
        } catch (Exception e) {
            System.err.println("ERROR CONTROLUBICACIONESGEOGRAFICAS VERIFICARBORRADO ERROR " + e);
        }
    }

    public void borrandoUbicacionesGeograficas() {
        try {
            banderaModificacionEmpresa = 1;
            if (index >= 0) {
                if (tipoLista == 0) {
                    if (!modificarUbicacionesGeograficas.isEmpty() && modificarUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(index))) {
                        int modIndex = modificarUbicacionesGeograficas.indexOf(listUbicacionesGeograficasPorEmpresa.get(index));
                        modificarUbicacionesGeograficas.remove(modIndex);
                        borrarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(index));
                    } else if (!crearUbicacionesGeograficas.isEmpty() && crearUbicacionesGeograficas.contains(listUbicacionesGeograficasPorEmpresa.get(index))) {
                        int crearIndex = crearUbicacionesGeograficas.indexOf(listUbicacionesGeograficasPorEmpresa.get(index));
                        crearUbicacionesGeograficas.remove(crearIndex);
                    } else {

                        borrarUbicacionesGeograficas.add(listUbicacionesGeograficasPorEmpresa.get(index));
                    }
                    listUbicacionesGeograficasPorEmpresa.remove(index);
                }
                if (tipoLista == 1) {
                    if (!modificarUbicacionesGeograficas.isEmpty() && modificarUbicacionesGeograficas.contains(filtrarUbicacionesGeograficas.get(index))) {
                        System.out.println("\n 6 ENTRE A CONTROLUBICACIONESGEOGRAFICAS.borrarCentroCosto tipolista==1 try if if if filtrarUbicacionesGeograficas.get(index).getCodigo()" + filtrarUbicacionesGeograficas.get(index).getCodigo());

                        int modIndex = modificarUbicacionesGeograficas.indexOf(filtrarUbicacionesGeograficas.get(index));
                        modificarUbicacionesGeograficas.remove(modIndex);
                        borrarUbicacionesGeograficas.add(filtrarUbicacionesGeograficas.get(index));
                    } else if (!crearUbicacionesGeograficas.isEmpty() && crearUbicacionesGeograficas.contains(filtrarUbicacionesGeograficas.get(index))) {
                        System.out.println("\n 7 ENTRE A CONTROLUBICACIONESGEOGRAFICAS.borrarCentroCosto tipolista==1 try if if if filtrarUbicacionesGeograficas.get(index).getCodigo()" + filtrarUbicacionesGeograficas.get(index).getCodigo());
                        int crearIndex = crearUbicacionesGeograficas.indexOf(filtrarUbicacionesGeograficas.get(index));
                        crearUbicacionesGeograficas.remove(crearIndex);
                    } else {
                        System.out.println("\n 8 ENTRE A CONTROLUBICACIONESGEOGRAFICAS.borrarCentroCosto tipolista==1 try if if if filtrarUbicacionesGeograficas.get(index).getCodigo()" + filtrarUbicacionesGeograficas.get(index).getCodigo());
                        borrarUbicacionesGeograficas.add(filtrarUbicacionesGeograficas.get(index));
                    }
                    int VCIndex = listUbicacionesGeograficasPorEmpresa.indexOf(filtrarUbicacionesGeograficas.get(index));
                    listUbicacionesGeograficasPorEmpresa.remove(VCIndex);
                    filtrarUbicacionesGeograficas.remove(index);
                }

                RequestContext context = RequestContext.getCurrentInstance();
                index = -1;
                System.err.println("verificar Borrado " + guardado);
                if (guardado == true) {
                    guardado = false;
                }
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.BorrarCentroCosto ERROR=====================" + e);
        }
    }

    public void guardarCambiosUbicacionGeografica() {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            if (guardado == false) {
                System.out.println("Realizando Operaciones Ubicaciones Geograficas");
                if (!borrarUbicacionesGeograficas.isEmpty()) {
                    administrarUbicacionesGeograficas.borrarUbicacionesGeograficas(borrarUbicacionesGeograficas);
                    //mostrarBorrados
                    registrosBorrados = borrarUbicacionesGeograficas.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarUbicacionesGeograficas.clear();
                }
                if (!modificarUbicacionesGeograficas.isEmpty()) {
                    administrarUbicacionesGeograficas.modificarUbicacionesGeograficas(modificarUbicacionesGeograficas);
                    modificarUbicacionesGeograficas.clear();
                }
                if (!crearUbicacionesGeograficas.isEmpty()) {
                    administrarUbicacionesGeograficas.crearUbicacionesGeograficas(crearUbicacionesGeograficas);
                    crearUbicacionesGeograficas.clear();
                }
                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
                FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }

            if (banderaModificacionEmpresa == 0) {
                cambiarEmpresa();
                banderaModificacionEmpresa = 1;

            }
            k = 0;
            guardado = true;
            index = -1;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            banderaModificacionEmpresa = 0;
            System.out.println("Se guardaron los datos con exito");
        } catch (Exception e) {
            System.err.println("CONTROLUBICACIONESGEOGRAFICAS GUARDARCAMBIOS: " + e);
        }
        listUbicacionesGeograficasPorEmpresa = null;
    }

    public void cancelarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (banderaModificacionEmpresa == 0) {
            empresaSeleccionada = backUpEmpresaActual;
            RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
            banderaModificacionEmpresa = 1;
        }

    }

    public void activarCtrlF11() {
        System.out.println("\n ENTRE A CONTROLUBICACIONESGEOGRAFICAS.activarCtrlF11 \n");
        FacesContext c = FacesContext.getCurrentInstance();
        try {

            if (bandera == 0) {
                System.out.println("Activar");
                tamano = 250;
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoCC");
                codigoCC.setFilterStyle("width: 85% !important;");
                nombreCentroCosto = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:nombreCentroCosto");
                nombreCentroCosto.setFilterStyle("width: 85% !important;");
                tipoCentroCosto = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:tipoCentroCosto");
                tipoCentroCosto.setFilterStyle("width: 85% !important;");
                codigoCTT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoCTT");
                codigoCTT.setFilterStyle("width: 85% !important;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:telefono");
                telefono.setFilterStyle("width: 85% !important;");
                fax = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:fax");
                fax.setFilterStyle("width: 85% !important;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:observacion");
                observacion.setFilterStyle("width: 85% !important;");
                manoDeObra = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:manoDeObra");
                manoDeObra.setFilterStyle("width: 85% !important;");
                actividadEconomica = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:actividadEconomica");
                actividadEconomica.setFilterStyle("width: 85% !important;");
                sucursalPila = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:sucursalPila");
                sucursalPila.setFilterStyle("width: 85% !important;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoAT");
                codigoAT.setFilterStyle("width: 85% !important;");
                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
                bandera = 1;
            } else if (bandera == 1) {
                System.out.println("Desactivar");
                //0
                codigoCC = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoCC");
                codigoCC.setFilterStyle("display: none; visibility: hidden;");
                nombreCentroCosto = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:nombreCentroCosto");
                nombreCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                tipoCentroCosto = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:tipoCentroCosto");
                tipoCentroCosto.setFilterStyle("display: none; visibility: hidden;");
                codigoCTT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoCTT");
                codigoCTT.setFilterStyle("display: none; visibility: hidden;");
                telefono = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:telefono");
                telefono.setFilterStyle("display: none; visibility: hidden;");
                fax = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:fax");
                fax.setFilterStyle("display: none; visibility: hidden;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:observacion");
                observacion.setFilterStyle("display: none; visibility: hidden;");
                manoDeObra = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:manoDeObra");
                manoDeObra.setFilterStyle("display: none; visibility: hidden;");
                actividadEconomica = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:actividadEconomica");
                actividadEconomica.setFilterStyle("display: none; visibility: hidden;");
                sucursalPila = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:sucursalPila");
                sucursalPila.setFilterStyle("display: none; visibility: hidden;");
                codigoAT = (Column) c.getViewRoot().findComponent("form:datosUbicacionesGeograficas:codigoAT");
                codigoAT.setFilterStyle("display: none; visibility: hidden;");
                tamano = 270;

                RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");
                bandera = 0;
                filtrarUbicacionesGeograficas = null;
                tipoLista = 0;
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.activarCtrlF11 ERROR " + e);
        }
    }

    public void editarCelda() {
        try {
            System.out.println("\n ENTRE A editarCelda INDEX  " + index);
            if (index >= 0) {
                System.out.println("\n ENTRE AeditarCelda TIPOLISTA " + tipoLista);
                if (tipoLista == 0) {
                    editarCentroCosto = listUbicacionesGeograficasPorEmpresa.get(index);
                }
                if (tipoLista == 1) {
                    editarCentroCosto = filtrarUbicacionesGeograficas.get(index);
                }
                RequestContext context = RequestContext.getCurrentInstance();
                System.out.println("CONTROLUBICACIONESGEOGRAFICAS: Entro a editar... valor celda: " + cualCelda);
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigo");
                    RequestContext.getCurrentInstance().execute("PF('editarCodigo').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcion");
                    RequestContext.getCurrentInstance().execute("PF('editarDescripcion').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudad");
                    RequestContext.getCurrentInstance().execute("PF('editarCiudad').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarDireccion");
                    RequestContext.getCurrentInstance().execute("PF('editarDireccion').show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarTelefono");
                    RequestContext.getCurrentInstance().execute("PF('editarTelefono').show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFax");
                    RequestContext.getCurrentInstance().execute("PF('editarFax').show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarObservacion");
                    RequestContext.getCurrentInstance().execute("PF('editarObservacion').show()");
                    cualCelda = -1;
                } else if (cualCelda == 8) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarActividadEconomica");
                    RequestContext.getCurrentInstance().execute("PF('editarActividadEconomica').show()");
                    cualCelda = -1;
                } else if (cualCelda == 9) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarSucursalPilla");
                    RequestContext.getCurrentInstance().execute("PF('editarSucursalPilla').show()");
                    cualCelda = -1;
                } else if (cualCelda == 10) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoAlt");
                    RequestContext.getCurrentInstance().execute("PF('editarCodigoAlt').show()");
                    cualCelda = -1;
                }
            }
            index = -1;
        } catch (Exception E) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.editarCelDa ERROR " + E);
        }
    }

    public void listaValoresBoton() {

        try {
            if (index >= 0) {
                RequestContext context = RequestContext.getCurrentInstance();
                if (cualCelda == 2) {
                    System.out.println("\n ListaValoresBoton \n");
                    RequestContext.getCurrentInstance().update("formularioDialogos:tiposCentrosCostosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('tiposCentrosCostosDialogo').show()");
                    tipoActualizacion = 0;
                }
                if (cualCelda == 9) {
                    System.out.println("\n ListaValoresBoton \n");
                    RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesPilaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('sucursalesPilaDialogo').show()");
                    tipoActualizacion = 0;
                }
            }
        } catch (Exception e) {
            System.out.println("\n ERROR CONTROLUBICACIONESGEOGRAFICAS.listaValoresBoton ERROR====================" + e);

        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUbicacionesGeograficasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "UBICACIONESGEOGRAFICAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    /**
     *
     * @throws IOException
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosUbicacionesGeograficasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "UBICACIONESGEOGRAFICAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listUbicacionesGeograficasPorEmpresa.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "UBICACIONESGEOGRAFICAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
                System.out.println("resultado: " + resultado);
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
                } else if (resultado == 3) {
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                } else if (resultado == 5) {
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("UBICACIONESGEOGRAFICAS")) { // igual acá
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    public void lovEmpresas() {
        index = -1;
        secRegistro = null;
        cualCelda = -1;
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').show()");
    }

    public void cambiarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("Cambiar empresa  GUARDADO = " + guardado);
        System.err.println("Cambiar empresa  GUARDADO = " + empresaSeleccionada.getNombre());
        if (guardado == true) {
            RequestContext.getCurrentInstance().update("form:nombreEmpresa");
            RequestContext.getCurrentInstance().update("form:nitEmpresa");
            listUbicacionesGeograficasPorEmpresa = null;
            listaSucursalesPilas = null;
            getListUbicacionesGeograficasPorEmpresa();
            getListaSucursalesPilas();
            filtradoListaEmpresas = null;

            aceptar = true;
            backUpEmpresaActual = empresaSeleccionada;
            banderaModificacionEmpresa = 0;
            RequestContext.getCurrentInstance().update("form:datosUbicacionesGeograficas");

        } else {
            banderaModificacionEmpresa = 0;
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");

            //RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
            backUpEmpresaActual = empresaSeleccionada;
        }
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
    }

    public void cancelarCambioEmpresa() {
        filtradoListaEmpresas = null;
        banderaModificacionEmpresa = 0;
        index = -1;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
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
    private String infoRegistroEmpresas;

    public List<Empresas> getListaEmpresas() {
        try {
            if (listaEmpresas == null) {
                listaEmpresas = administrarUbicacionesGeograficas.consultarEmpresas();
                if (!listaEmpresas.isEmpty()) {
                    empresaSeleccionada = listaEmpresas.get(0);
                    backUpEmpresaActual = empresaSeleccionada;
                }
            }
            if (listaEmpresas == null || listaEmpresas.isEmpty()) {
                infoRegistroEmpresas = "Cantidad de registros: 0 ";
            } else {
                infoRegistroEmpresas = "Cantidad de registros: " + listaEmpresas.size();
            }
            return listaEmpresas;
        } catch (Exception e) {
            System.out.println("ERROR LISTA EMPRESAS " + e);
            return null;
        }
    }

    public void setListaEmpresas(List<Empresas> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public List<Empresas> getFiltradoListaEmpresas() {
        return filtradoListaEmpresas;
    }

    public void setFiltradoListaEmpresas(List<Empresas> filtradoListaEmpresas) {
        this.filtradoListaEmpresas = filtradoListaEmpresas;
    }

    public Empresas getEmpresaSeleccionada() {
        try {
            if (empresaSeleccionada == null) {
                getListaEmpresas();
                return empresaSeleccionada;
            }
        } catch (Exception e) {
            System.out.println("ERROR CONTROLUBICACIONESGEOGRAFICAS.getEmpresaSeleccionada ERROR " + e);
        } finally {
            return empresaSeleccionada;
        }
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public List<UbicacionesGeograficas> getListUbicacionesGeograficasPorEmpresa() {
        //try {
        if (empresaSeleccionada == null) {
            getEmpresaSeleccionada();
            if (listUbicacionesGeograficasPorEmpresa == null) {
                listUbicacionesGeograficasPorEmpresa = administrarUbicacionesGeograficas.consultarUbicacionesGeograficasPorEmpresa(empresaSeleccionada.getSecuencia());
            } else {
                System.out.println(".-.");
            }
        } else if (listUbicacionesGeograficasPorEmpresa == null) {
            System.out.println("CONTROLUBICACIONESGEOGRAFICAS LA EMPRESA NO ES NULA");
            listUbicacionesGeograficasPorEmpresa = administrarUbicacionesGeograficas.consultarUbicacionesGeograficasPorEmpresa(empresaSeleccionada.getSecuencia());
            getListaSucursalesPilas();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listUbicacionesGeograficasPorEmpresa == null || listUbicacionesGeograficasPorEmpresa.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listUbicacionesGeograficasPorEmpresa.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listUbicacionesGeograficasPorEmpresa;
        // } catch (Exception e) {
        ///.out.println(" BETA  BETA ControlCentrosCosto: Error al recibir los UbicacionesGeograficas de la empresa seleccionada /n" + e);
        // return null;
        //}
    }

    public void setListUbicacionesGeograficasPorEmpresa(List<UbicacionesGeograficas> listUbicacionesGeograficasPorEmpresa) {
        this.listUbicacionesGeograficasPorEmpresa = listUbicacionesGeograficasPorEmpresa;
    }

    public List<UbicacionesGeograficas> getFiltrarUbicacionesGeograficas() {
        return filtrarUbicacionesGeograficas;
    }

    public void setFiltrarUbicacionesGeograficas(List<UbicacionesGeograficas> filtrarUbicacionesGeograficas) {
        this.filtrarUbicacionesGeograficas = filtrarUbicacionesGeograficas;
    }

    public UbicacionesGeograficas getNuevaUbicacionGeografica() {
        if (nuevaUbicacionGeografica == null) {
            nuevaUbicacionGeografica = new UbicacionesGeograficas();
        }
        return nuevaUbicacionGeografica;
    }

    public void setNuevaUbicacionGeografica(UbicacionesGeograficas nuevaUbicacionGeografica) {
        this.nuevaUbicacionGeografica = nuevaUbicacionGeografica;
    }

    public UbicacionesGeograficas getDuplicarUbicacionGeografica() {
        return duplicarUbicacionGeografica;
    }

    public void setDuplicarUbicacionGeografica(UbicacionesGeograficas duplicarUbicacionGeografica) {
        this.duplicarUbicacionGeografica = duplicarUbicacionGeografica;
    }
    private String infoRegistroCiudades;

    public List<Ciudades> getListaCiudades() {
        if (listaCiudades == null) {
            listaCiudades = administrarUbicacionesGeograficas.lovCiudades();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaCiudades == null || listaCiudades.isEmpty()) {
            infoRegistroCiudades = "Cantidad de registros: 0 ";
        } else {
            infoRegistroCiudades = "Cantidad de registros: " + listaCiudades.size();
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroCiudades");
        return listaCiudades;
    }

    public void setListaCiudades(List<Ciudades> listaCiudades) {
        this.listaCiudades = listaCiudades;
    }

    public List<Ciudades> getFiltradoCiudades() {
        return filtradoCiudades;
    }

    public void setFiltradoCiudades(List<Ciudades> filtradoCiudades) {
        this.filtradoCiudades = filtradoCiudades;
    }

    public Ciudades getTipoCentroCostoSeleccionado() {
        return ciudadSeleccionada;
    }

    public void setTipoCentroCostoSeleccionado(Ciudades ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<UbicacionesGeograficas> getFilterUbicacionesGeograficasPorEmpresa() {
        return filterUbicacionesGeograficasPorEmpresa;
    }

    public void setFilterUbicacionesGeograficasPorEmpresa(List<UbicacionesGeograficas> filterUbicacionesGeograficasPorEmpresa) {
        this.filterUbicacionesGeograficasPorEmpresa = filterUbicacionesGeograficasPorEmpresa;
    }

    public UbicacionesGeograficas getUbicacionesGeograficasPorEmpresaSeleccionado() {
        return UbicacionesGeograficasPorEmpresaSeleccionado;
    }

    public void setUbicacionesGeograficasPorEmpresaSeleccionado(UbicacionesGeograficas UbicacionesGeograficasPorEmpresaSeleccionado) {
        this.UbicacionesGeograficasPorEmpresaSeleccionado = UbicacionesGeograficasPorEmpresaSeleccionado;
    }

    public UbicacionesGeograficas getEditarCentroCosto() {
        return editarCentroCosto;
    }

    public void setEditarCentroCosto(UbicacionesGeograficas editarCentroCosto) {
        this.editarCentroCosto = editarCentroCosto;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }
    private String infoRegistroSucursalesPila;

    public List<SucursalesPila> getListaSucursalesPilas() {
        if (listaSucursalesPilas == null) {
            listaSucursalesPilas = administrarUbicacionesGeograficas.lovSucursalesPilaPorEmpresa(empresaSeleccionada.getSecuencia());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaSucursalesPilas == null || listaSucursalesPilas.isEmpty()) {
            infoRegistroSucursalesPila = "Cantidad de registros: 0 ";
        } else {
            infoRegistroSucursalesPila = "Cantidad de registros: " + listaSucursalesPilas.size();
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroSucursalesPila");
        return listaSucursalesPilas;
    }

    public void setListaSucursalesPilas(List<SucursalesPila> listaSucursalesPilas) {
        this.listaSucursalesPilas = listaSucursalesPilas;
    }

    public List<SucursalesPila> getFiltradoSucursalesPilas() {
        return filtradoSucursalesPilas;
    }

    public void setFiltradoSucursalesPilas(List<SucursalesPila> filtradoSucursalesPilas) {
        this.filtradoSucursalesPilas = filtradoSucursalesPilas;
    }

    public SucursalesPila getSucursalesPilasSeleccionada() {
        return sucursalesPilasSeleccionada;
    }

    public void setSucursalesPilasSeleccionada(SucursalesPila sucursalesPilasSeleccionada) {
        this.sucursalesPilasSeleccionada = sucursalesPilasSeleccionada;
    }

    public Ciudades getCiudadSeleccionada() {
        return ciudadSeleccionada;
    }

    public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
        this.ciudadSeleccionada = ciudadSeleccionada;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public UbicacionesGeograficas getUbicacionGeograficaSeleccionada() {
        return ubicacionGeograficaSeleccionada;
    }

    public void setUbicacionGeograficaSeleccionada(UbicacionesGeograficas ubicacionGeograficaSeleccionada) {
        this.ubicacionGeograficaSeleccionada = ubicacionGeograficaSeleccionada;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroCiudades() {
        return infoRegistroCiudades;
    }

    public void setInfoRegistroCiudades(String infoRegistroCiudades) {
        this.infoRegistroCiudades = infoRegistroCiudades;
    }

    public String getInfoRegistroSucursalesPila() {
        return infoRegistroSucursalesPila;
    }

    public void setInfoRegistroSucursalesPila(String infoRegistroSucursalesPila) {
        this.infoRegistroSucursalesPila = infoRegistroSucursalesPila;
    }

    public String getInfoRegistroEmpresas() {
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
    }

}
