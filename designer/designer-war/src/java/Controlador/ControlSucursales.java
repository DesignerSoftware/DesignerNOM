/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.Ciudades;
import Entidades.Sucursales;
import Entidades.Bancos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarSucursalesInterface;
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
public class ControlSucursales implements Serializable {

    @EJB
    AdministrarSucursalesInterface administrarSucursales;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Sucursales> listSucursales;
    private List<Sucursales> filtrarSucursales;
    private List<Sucursales> crearSucursales;
    private List<Sucursales> modificarSucursales;
    private List<Sucursales> borrarSucursales;
    private Sucursales nuevoSucursales;
    private Sucursales duplicarSucursales;
    private Sucursales editarSucursales;
    private Sucursales sucursalSeleccionada;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, descripcion, personafir, cargo;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private Integer backupCodigo;
    private String backupDescripcion;
    private String backupPais;
    //---------------------------------
    private String backupCiudad;
    private List<Bancos> listaBancos;
    private List<Bancos> filtradoBancos;
    private Bancos bancoSeleccionado;
    private String nuevoYduplicarCompletarPersona;
    //--------------------------------------
    private String backupBanco;
    private List<Ciudades> listaCiudades;
    private List<Ciudades> filtradoCiudades;
    private Ciudades cargoSeleccionado;
    private String nuevoYduplicarCompletarCargo;

    public ControlSucursales() {
        listSucursales = null;
        crearSucursales = new ArrayList<Sucursales>();
        modificarSucursales = new ArrayList<Sucursales>();
        borrarSucursales = new ArrayList<Sucursales>();
        permitirIndex = true;
        editarSucursales = new Sucursales();
        nuevoSucursales = new Sucursales();
        nuevoSucursales.setBanco(new Bancos());
        nuevoSucursales.setCiudad(new Ciudades());
        duplicarSucursales = new Sucursales();
        duplicarSucursales.setBanco(new Bancos());
        duplicarSucursales.setCiudad(new Ciudades());
        listaBancos = null;
        filtradoBancos = null;
        listaCiudades = null;
        filtradoCiudades = null;
        guardado = true;
        tamano = 270;
        aceptar = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSucursales.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlSucursales.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarSucursales.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlSucursales eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listSucursales.get(index).getSecuencia();
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backupCodigo = listSucursales.get(index).getCodigo();
                }
                if (cualCelda == 1) {
                    backupDescripcion = listSucursales.get(index).getNombre();
                    System.out.println("DESCRIPCION : " + backupDescripcion);
                }
                if (cualCelda == 2) {
                    backupBanco = listSucursales.get(index).getBanco().getNombre();
                    System.out.println("BANCO : " + backupBanco);

                }
                if (cualCelda == 3) {
                    backupCiudad = listSucursales.get(index).getCiudad().getNombre();
                    System.out.println("CIUDAD : " + backupCiudad);

                }

            } else if (tipoLista == 1) {
                if (cualCelda == 0) {
                    backupCodigo = filtrarSucursales.get(index).getCodigo();
                }
                if (cualCelda == 1) {
                    backupDescripcion = filtrarSucursales.get(index).getNombre();
                    System.out.println("DESCRIPCION : " + backupDescripcion);
                }
                if (cualCelda == 2) {
                    backupBanco = filtrarSucursales.get(index).getBanco().getNombre();
                    System.out.println("BANCO : " + backupBanco);

                }
                if (cualCelda == 3) {
                    backupCiudad = filtrarSucursales.get(index).getCiudad().getNombre();
                    System.out.println("CIUDAD : " + backupCiudad);

                }
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("\n ENTRE A ControlSucursales.asignarIndex \n");
            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dig == 2) {
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                dig = -1;
            }
            if (dig == 3) {
                RequestContext.getCurrentInstance().update("form:cargosDialogo");
                PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
                dig = -1;
            }
        } catch (Exception e) {
            System.out.println("ERROR ControlSucursales.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        if (index >= 0) {

            if (cualCelda == 2) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }
            if (cualCelda == 3) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:cargosDialogo");
                PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
            }
        }
    }
    private String infoRegistro;

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosSucursales:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSucursales:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosSucursales:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosSucursales:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            bandera = 0;
            filtrarSucursales = null;
            tipoLista = 0;
        }

        borrarSucursales.clear();
        crearSucursales.clear();
        modificarSucursales.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSucursales = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSucursales == null || listSucursales.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSucursales.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosSucursales");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosSucursales:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSucursales:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosSucursales:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosSucursales:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            bandera = 0;
            filtrarSucursales = null;
            tipoLista = 0;
        }

        borrarSucursales.clear();
        crearSucursales.clear();
        modificarSucursales.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSucursales = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSucursales == null || listSucursales.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSucursales.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosSucursales");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSucursales:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSucursales:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosSucursales:personafir");
            personafir.setFilterStyle("width: 85% !important;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosSucursales:cargo");
            cargo.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSucursales:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSucursales:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            personafir = (Column) c.getViewRoot().findComponent("form:datosSucursales:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosSucursales:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosSucursales");
            bandera = 0;
            filtrarSucursales = null;
            tipoLista = 0;
        }
    }

    public void actualizarBancos() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("banco seleccionado : " + bancoSeleccionado.getNombre());
        System.out.println("tipo Actualizacion : " + tipoActualizacion);
        System.out.println("tipo Lista : " + tipoLista);

        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listSucursales.get(index).setBanco(bancoSeleccionado);

                if (!crearSucursales.contains(listSucursales.get(index))) {
                    if (modificarSucursales.isEmpty()) {
                        modificarSucursales.add(listSucursales.get(index));
                    } else if (!modificarSucursales.contains(listSucursales.get(index))) {
                        modificarSucursales.add(listSucursales.get(index));
                    }
                }
            } else {
                filtrarSucursales.get(index).setBanco(bancoSeleccionado);

                if (!crearSucursales.contains(filtrarSucursales.get(index))) {
                    if (modificarSucursales.isEmpty()) {
                        modificarSucursales.add(filtrarSucursales.get(index));
                    } else if (!modificarSucursales.contains(filtrarSucursales.get(index))) {
                        modificarSucursales.add(filtrarSucursales.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            System.out.println("ACTUALIZAR PAIS PAIS SELECCIONADO : " + bancoSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            System.out.println("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + bancoSeleccionado.getNombre());
            nuevoSucursales.setBanco(bancoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
        } else if (tipoActualizacion == 2) {
            System.out.println("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + bancoSeleccionado.getNombre());
            duplicarSucursales.setBanco(bancoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
        }
        filtradoBancos = null;
        bancoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("form:lovBancos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovBancos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').hide()");
        //RequestContext.getCurrentInstance().update("form:lovBancos");
        //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
    }

    public void actualizarCiudades() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("ciudad seleccionado : " + cargoSeleccionado.getNombre());
        System.out.println("tipo Actualizacion : " + tipoActualizacion);
        System.out.println("tipo Lista : " + tipoLista);

        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listSucursales.get(index).setCiudad(cargoSeleccionado);

                if (!crearSucursales.contains(listSucursales.get(index))) {
                    if (modificarSucursales.isEmpty()) {
                        modificarSucursales.add(listSucursales.get(index));
                    } else if (!modificarSucursales.contains(listSucursales.get(index))) {
                        modificarSucursales.add(listSucursales.get(index));
                    }
                }
            } else {
                filtrarSucursales.get(index).setCiudad(cargoSeleccionado);

                if (!crearSucursales.contains(filtrarSucursales.get(index))) {
                    if (modificarSucursales.isEmpty()) {
                        modificarSucursales.add(filtrarSucursales.get(index));
                    } else if (!modificarSucursales.contains(filtrarSucursales.get(index))) {
                        modificarSucursales.add(filtrarSucursales.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            System.out.println("ACTUALIZAR PAIS CARGO SELECCIONADO : " + cargoSeleccionado.getNombre());
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            System.out.println("ACTUALIZAR PAIS NUEVO DEPARTAMENTO: " + cargoSeleccionado.getNombre());
            nuevoSucursales.setCiudad(cargoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
        } else if (tipoActualizacion == 2) {
            System.out.println("ACTUALIZAR PAIS DUPLICAR DEPARTAMENO: " + cargoSeleccionado.getNombre());
            duplicarSucursales.setCiudad(cargoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
        }
        filtradoBancos = null;
        bancoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("form:lovCiudades:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCiudades').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('cargosDialogo').hide()");
        //RequestContext.getCurrentInstance().update("form:lovCiudades");
        //RequestContext.getCurrentInstance().update("form:datosHvEntrevista");
    }

    public void cancelarCambioBancos() {
        listSucursales.get(index).getBanco().setNombre(backupBanco);
        filtradoBancos = null;
        bancoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovBancos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovBancos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').hide()");
    }

    public void cancelarCambioCiudades() {
        filtradoCiudades = null;
        cargoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovCiudades:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovCiudades').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('cargosDialogo').hide()");
    }

    public void modificarSucursales(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;
        int coincidencias = 0;
        int contador = 0;
        boolean banderita = false;
        boolean banderita1 = false;
        boolean banderita2 = false;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearSucursales.contains(listSucursales.get(indice))) {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listSucursales.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listSucursales.get(indice).setCodigo(backupCodigo);

                    } else {
                        for (int j = 0; j < listSucursales.size(); j++) {
                            if (j != indice) {
                                if (listSucursales.get(indice).getCodigo() == listSucursales.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listSucursales.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listSucursales.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listSucursales.get(indice).setNombre(backupDescripcion);
                    } else if (listSucursales.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listSucursales.get(indice).setNombre(backupDescripcion);

                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarSucursales.isEmpty()) {
                            modificarSucursales.add(listSucursales.get(indice));
                        } else if (!modificarSucursales.contains(listSucursales.get(indice))) {
                            modificarSucursales.add(listSucursales.get(indice));
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
                    RequestContext.getCurrentInstance().update("form:datosSucursales");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {

                    System.out.println("backupCodigo : " + backupCodigo);
                    System.out.println("backupDescripcion : " + backupDescripcion);

                    if (listSucursales.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listSucursales.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < listSucursales.size(); j++) {
                            if (j != indice) {
                                if (listSucursales.get(indice).getCodigo() == listSucursales.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listSucursales.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listSucursales.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listSucursales.get(indice).setNombre(backupDescripcion);
                    } else if (listSucursales.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listSucursales.get(indice).setNombre(backupDescripcion);

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
                    RequestContext.getCurrentInstance().update("form:datosSucursales");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            } else {

                if (!crearSucursales.contains(filtrarSucursales.get(indice))) {
                    if (filtrarSucursales.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarSucursales.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarSucursales.size(); j++) {
                            if (j != indice) {
                                if (filtrarSucursales.get(indice).getCodigo() == listSucursales.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listSucursales.size(); j++) {
                            if (j != indice) {
                                if (filtrarSucursales.get(indice).getCodigo() == listSucursales.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarSucursales.get(indice).setCodigo(backupCodigo);

                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarSucursales.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarSucursales.get(indice).setNombre(backupDescripcion);
                    }
                    if (filtrarSucursales.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarSucursales.get(indice).setNombre(backupDescripcion);
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarSucursales.isEmpty()) {
                            modificarSucursales.add(filtrarSucursales.get(indice));
                        } else if (!modificarSucursales.contains(filtrarSucursales.get(indice))) {
                            modificarSucursales.add(filtrarSucursales.get(indice));
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
                    if (filtrarSucursales.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        filtrarSucursales.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < filtrarSucursales.size(); j++) {
                            if (j != indice) {
                                if (filtrarSucursales.get(indice).getCodigo() == listSucursales.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        for (int j = 0; j < listSucursales.size(); j++) {
                            if (j != indice) {
                                if (filtrarSucursales.get(indice).getCodigo() == listSucursales.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            filtrarSucursales.get(indice).setCodigo(backupCodigo);

                        } else {
                            banderita = true;
                        }

                    }

                    if (filtrarSucursales.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarSucursales.get(indice).setNombre(backupDescripcion);
                    }
                    if (filtrarSucursales.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        filtrarSucursales.get(indice).setNombre(backupDescripcion);
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
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else if (confirmarCambio.equalsIgnoreCase("PERSONAS")) {
            System.out.println("MODIFICANDO NORMA LABORAL : " + listSucursales.get(indice).getBanco().getNombre());
            if (!listSucursales.get(indice).getBanco().getNombre().equals("")) {
                if (tipoLista == 0) {
                    listSucursales.get(indice).getBanco().setNombre(backupBanco);
                } else {
                    listSucursales.get(indice).getBanco().setNombre(backupBanco);
                }

                for (int i = 0; i < listaBancos.size(); i++) {
                    if (listaBancos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listSucursales.get(indice).setBanco(listaBancos.get(indiceUnicoElemento));
                    } else {
                        filtrarSucursales.get(indice).setBanco(listaBancos.get(indiceUnicoElemento));
                    }
                    listaBancos.clear();
                    listaBancos = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (backupBanco != null) {
                    if (tipoLista == 0) {
                        listSucursales.get(index).getBanco().setNombre(backupBanco);
                    } else {
                        filtrarSucursales.get(index).getBanco().setNombre(backupBanco);
                    }
                }
                tipoActualizacion = 0;
                System.out.println("PAIS ANTES DE MOSTRAR DIALOGO PERSONA : " + backupBanco);
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearSucursales.contains(listSucursales.get(indice))) {

                        if (modificarSucursales.isEmpty()) {
                            modificarSucursales.add(listSucursales.get(indice));
                        } else if (!modificarSucursales.contains(listSucursales.get(indice))) {
                            modificarSucursales.add(listSucursales.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearSucursales.contains(filtrarSucursales.get(indice))) {

                        if (modificarSucursales.isEmpty()) {
                            modificarSucursales.add(filtrarSucursales.get(indice));
                        } else if (!modificarSucursales.contains(filtrarSucursales.get(indice))) {
                            modificarSucursales.add(filtrarSucursales.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosSucursales");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        } else if (confirmarCambio.equalsIgnoreCase("CARGOS")) {
            System.out.println("MODIFICANDO CARGO: " + listSucursales.get(indice).getCiudad().getNombre());
            if (!listSucursales.get(indice).getBanco().getNombre().equals("")) {
                if (tipoLista == 0) {
                    listSucursales.get(indice).getCiudad().setNombre(backupCiudad);
                } else {
                    listSucursales.get(indice).getCiudad().setNombre(backupCiudad);
                }

                for (int i = 0; i < listaCiudades.size(); i++) {
                    if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listSucursales.get(indice).setCiudad(listaCiudades.get(indiceUnicoElemento));
                    } else {
                        filtrarSucursales.get(indice).setCiudad(listaCiudades.get(indiceUnicoElemento));
                    }
                    listaCiudades.clear();
                    listaCiudades = null;
                    //getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:cargosDialogo");
                    PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                if (backupCiudad != null) {
                    if (tipoLista == 0) {
                        listSucursales.get(index).getCiudad().setNombre(backupCiudad);
                    } else {
                        filtrarSucursales.get(index).getCiudad().setNombre(backupCiudad);
                    }
                }
                tipoActualizacion = 0;
                System.out.println("PAIS ANTES DE MOSTRAR DIALOGO CARGOS : " + backupCiudad);
                RequestContext.getCurrentInstance().update("form:personasDialogo");
                PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearSucursales.contains(listSucursales.get(indice))) {

                        if (modificarSucursales.isEmpty()) {
                            modificarSucursales.add(listSucursales.get(indice));
                        } else if (!modificarSucursales.contains(listSucursales.get(indice))) {
                            modificarSucursales.add(listSucursales.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (!crearSucursales.contains(filtrarSucursales.get(indice))) {

                        if (modificarSucursales.isEmpty()) {
                            modificarSucursales.add(filtrarSucursales.get(indice));
                        } else if (!modificarSucursales.contains(filtrarSucursales.get(indice))) {
                            modificarSucursales.add(filtrarSucursales.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                    index = -1;
                    secRegistro = null;
                }
            }

            RequestContext.getCurrentInstance().update("form:datosSucursales");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        }

    }

    public void verificarBorrado() {
        BigInteger vigenciasFormasPagosSucursal;

        System.out.println("Estoy en verificarBorrado");
        try {
            System.err.println("Control Secuencia de ControlTiposFamiliares ");
            if (tipoLista == 0) {
                vigenciasFormasPagosSucursal = administrarSucursales.contarVigenciasFormasPagosSucursal(listSucursales.get(index).getSecuencia());
            } else {
                vigenciasFormasPagosSucursal = administrarSucursales.contarVigenciasFormasPagosSucursal(filtrarSucursales.get(index).getSecuencia());
            }
            if (vigenciasFormasPagosSucursal.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoSucursales();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                index = -1;

                vigenciasFormasPagosSucursal = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposFamiliares verificarBorrado ERROR " + e);
        }
    }

    public void borrandoSucursales() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoSucursales");
                if (!modificarSucursales.isEmpty() && modificarSucursales.contains(listSucursales.get(index))) {
                    int modIndex = modificarSucursales.indexOf(listSucursales.get(index));
                    modificarSucursales.remove(modIndex);
                    borrarSucursales.add(listSucursales.get(index));
                } else if (!crearSucursales.isEmpty() && crearSucursales.contains(listSucursales.get(index))) {
                    int crearIndex = crearSucursales.indexOf(listSucursales.get(index));
                    crearSucursales.remove(crearIndex);
                } else {
                    borrarSucursales.add(listSucursales.get(index));
                }
                listSucursales.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoSucursales ");
                if (!modificarSucursales.isEmpty() && modificarSucursales.contains(filtrarSucursales.get(index))) {
                    int modIndex = modificarSucursales.indexOf(filtrarSucursales.get(index));
                    modificarSucursales.remove(modIndex);
                    borrarSucursales.add(filtrarSucursales.get(index));
                } else if (!crearSucursales.isEmpty() && crearSucursales.contains(filtrarSucursales.get(index))) {
                    int crearIndex = crearSucursales.indexOf(filtrarSucursales.get(index));
                    crearSucursales.remove(crearIndex);
                } else {
                    borrarSucursales.add(filtrarSucursales.get(index));
                }
                int VCIndex = listSucursales.indexOf(filtrarSucursales.get(index));
                listSucursales.remove(VCIndex);

                filtrarSucursales.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listSucursales == null || listSucursales.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listSucursales.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void valoresBackupAutocompletar(int tipoNuevo, String valorCambio) {
        System.out.println("1...");
        if (valorCambio.equals("PERSONA")) {
            if (tipoNuevo == 1) {
                nuevoYduplicarCompletarPersona = nuevoSucursales.getBanco().getNombre();
            } else if (tipoNuevo == 2) {
                nuevoYduplicarCompletarPersona = duplicarSucursales.getBanco().getNombre();
            }

            System.out.println("PERSONA : " + nuevoYduplicarCompletarPersona);
        } else if (valorCambio.equals("CARGO")) {
            if (tipoNuevo == 1) {
                nuevoYduplicarCompletarCargo = nuevoSucursales.getCiudad().getNombre();
            } else if (tipoNuevo == 2) {
                nuevoYduplicarCompletarCargo = duplicarSucursales.getCiudad().getNombre();
            }
            System.out.println("CARGO : " + nuevoYduplicarCompletarCargo);
        }

    }

    public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
            System.out.println(" nueva Ciudad    Entro al if 'Centro costo'");
            System.out.println("NUEVO PERSONA :-------> " + nuevoSucursales.getBanco().getNombre());

            if (!nuevoSucursales.getBanco().getNombre().equals("")) {
                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarPersona);
                nuevoSucursales.getBanco().setNombre(nuevoYduplicarCompletarPersona);
                for (int i = 0; i < listaBancos.size(); i++) {
                    if (listaBancos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    nuevoSucursales.setBanco(listaBancos.get(indiceUnicoElemento));
                    listaBancos = null;
                    System.err.println("PERSONA GUARDADA :-----> " + nuevoSucursales.getBanco().getNombre());
                } else {
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                nuevoSucursales.getBanco().setNombre(nuevoYduplicarCompletarPersona);
                System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
                nuevoSucursales.setBanco(new Bancos());
                nuevoSucursales.getBanco().setNombre(" ");
                System.out.println("NUEVA NORMA LABORAL" + nuevoSucursales.getBanco().getNombre());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
        } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
            System.out.println(" nueva Ciudad    Entro al if 'Centro costo'");
            System.out.println("NUEVO PERSONA :-------> " + nuevoSucursales.getCiudad().getNombre());

            if (!nuevoSucursales.getCiudad().getNombre().equals("")) {
                System.out.println("ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("valorConfirmar: " + valorConfirmar);
                System.out.println("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarCargo);
                nuevoSucursales.getCiudad().setNombre(nuevoYduplicarCompletarCargo);
                for (int i = 0; i < listaCiudades.size(); i++) {
                    if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    nuevoSucursales.setCiudad(listaCiudades.get(indiceUnicoElemento));
                    listaCiudades = null;
                    System.err.println("CARGO GUARDADA :-----> " + nuevoSucursales.getCiudad().getNombre());
                } else {
                    RequestContext.getCurrentInstance().update("form:cargosDialogo");
                    PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                nuevoSucursales.getCiudad().setNombre(nuevoYduplicarCompletarCargo);
                System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
                nuevoSucursales.setCiudad(new Ciudades());
                nuevoSucursales.getCiudad().setNombre(" ");
                System.out.println("NUEVO CARGO " + nuevoSucursales.getCiudad().getNombre());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
        }

    }

    public void asignarVariableBancos(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:personasDialogo");
        PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
    }

    public void asignarVariableCiudades(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:cargosDialogo");
        PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
    }

    public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        System.out.println("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
            System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
            System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarPersona);

            if (!duplicarSucursales.getBanco().getNombre().equals("")) {
                System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
                System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarPersona);
                duplicarSucursales.getBanco().setNombre(nuevoYduplicarCompletarPersona);
                for (int i = 0; i < listaBancos.size(); i++) {
                    if (listaBancos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarSucursales.setBanco(listaBancos.get(indiceUnicoElemento));
                    listaBancos = null;
                } else {
                    RequestContext.getCurrentInstance().update("form:personasDialogo");
                    PrimefacesContextUI.ejecutar("PF('personasDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoNuevo == 2) {
                    //duplicarSucursales.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
                    System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
                    System.out.println("DUPLICAR INDEX : " + index);
                    duplicarSucursales.setBanco(new Bancos());
                    duplicarSucursales.getBanco().setNombre(" ");

                    System.out.println("DUPLICAR PERSONA  : " + duplicarSucursales.getBanco().getNombre());
                    System.out.println("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarPersona);
                    if (tipoLista == 0) {
                        listSucursales.get(index).getBanco().setNombre(nuevoYduplicarCompletarPersona);
                        System.err.println("tipo lista" + tipoLista);
                        System.err.println("Secuencia Parentesco " + listSucursales.get(index).getBanco().getSecuencia());
                    } else if (tipoLista == 1) {
                        filtrarSucursales.get(index).getBanco().setNombre(nuevoYduplicarCompletarPersona);
                    }

                }

            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
        } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
            System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
            System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarCargo);

            if (!duplicarSucursales.getCiudad().getNombre().equals("")) {
                System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
                System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
                System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarCargo);
                duplicarSucursales.getCiudad().setNombre(nuevoYduplicarCompletarCargo);
                for (int i = 0; i < listaCiudades.size(); i++) {
                    if (listaCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                System.out.println("Coincidencias: " + coincidencias);
                if (coincidencias == 1) {
                    duplicarSucursales.setCiudad(listaCiudades.get(indiceUnicoElemento));
                    listaCiudades = null;
                    getListaCiudades();
                } else {
                    RequestContext.getCurrentInstance().update("form:cargosDialogo");
                    PrimefacesContextUI.ejecutar("PF('cargosDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                }
            } else {
                if (tipoNuevo == 2) {
                    //duplicarSucursales.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
                    System.out.println("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
                    System.out.println("DUPLICAR INDEX : " + index);
                    duplicarSucursales.setCiudad(new Ciudades());
                    duplicarSucursales.getCiudad().setNombre(" ");

                    System.out.println("DUPLICAR CARGO  : " + duplicarSucursales.getCiudad().getNombre());
                    System.out.println("nuevoYduplicarCompletarCARGO : " + nuevoYduplicarCompletarCargo);
                    if (tipoLista == 0) {
                        listSucursales.get(index).getCiudad().setNombre(nuevoYduplicarCompletarCargo);
                        System.err.println("tipo lista" + tipoLista);
                        System.err.println("Secuencia Parentesco " + listSucursales.get(index).getCiudad().getSecuencia());
                    } else if (tipoLista == 1) {
                        filtrarSucursales.get(index).getCiudad().setNombre(nuevoYduplicarCompletarCargo);
                    }

                }

            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
        }
    }

    /*public void verificarBorrado() {
     System.out.println("Estoy en verificarBorrado");
     BigInteger contarBienProgramacionesDepartamento;
     BigInteger contarCapModulosDepartamento;
     BigInteger contarCiudadesDepartamento;
     BigInteger contarSoAccidentesMedicosDepartamento;

     try {
     System.err.println("Control Secuencia de ControlSucursales ");
     if (tipoLista == 0) {
     contarBienProgramacionesDepartamento = administrarSucursales.contarBienProgramacionesDepartamento(listSucursales.get(index).getSecuencia());
     contarCapModulosDepartamento = administrarSucursales.contarCapModulosDepartamento(listSucursales.get(index).getSecuencia());
     contarCiudadesDepartamento = administrarSucursales.contarCiudadesDepartamento(listSucursales.get(index).getSecuencia());
     contarSoAccidentesMedicosDepartamento = administrarSucursales.contarSoAccidentesMedicosDepartamento(listSucursales.get(index).getSecuencia());
     } else {
     contarBienProgramacionesDepartamento = administrarSucursales.contarBienProgramacionesDepartamento(filtrarSucursales.get(index).getSecuencia());
     contarCapModulosDepartamento = administrarSucursales.contarCapModulosDepartamento(filtrarSucursales.get(index).getSecuencia());
     contarCiudadesDepartamento = administrarSucursales.contarCiudadesDepartamento(filtrarSucursales.get(index).getSecuencia());
     contarSoAccidentesMedicosDepartamento = administrarSucursales.contarSoAccidentesMedicosDepartamento(filtrarSucursales.get(index).getSecuencia());
     }
     if (contarBienProgramacionesDepartamento.equals(new BigInteger("0"))
     && contarCapModulosDepartamento.equals(new BigInteger("0"))
     && contarCiudadesDepartamento.equals(new BigInteger("0"))
     && contarSoAccidentesMedicosDepartamento.equals(new BigInteger("0"))) {
     System.out.println("Borrado==0");
     borrandoSucursales();
     } else {
     System.out.println("Borrado>0");

     RequestContext context = RequestContext.getCurrentInstance();
     RequestContext.getCurrentInstance().update("form:validacionBorrar");
     PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
     index = -1;
     contarBienProgramacionesDepartamento = new BigInteger("-1");
     contarCapModulosDepartamento = new BigInteger("-1");
     contarCiudadesDepartamento = new BigInteger("-1");
     contarSoAccidentesMedicosDepartamento = new BigInteger("-1");

     }
     } catch (Exception e) {
     System.err.println("ERROR ControlSucursales verificarBorrado ERROR " + e);
     }
     }
     */
    public void revisarDialogoGuardar() {

        if (!borrarSucursales.isEmpty() || !crearSucursales.isEmpty() || !modificarSucursales.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarSucursales() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarSucursales");
            if (!borrarSucursales.isEmpty()) {
                administrarSucursales.borrarSucursales(borrarSucursales);
                //mostrarBorrados
                registrosBorrados = borrarSucursales.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarSucursales.clear();
            }
            if (!modificarSucursales.isEmpty()) {
                administrarSucursales.modificarSucursales(modificarSucursales);
                modificarSucursales.clear();
            }
            if (!crearSucursales.isEmpty()) {
                administrarSucursales.crearSucursales(crearSucursales);
                crearSucursales.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listSucursales = null;
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
                editarSucursales = listSucursales.get(index);
            }
            if (tipoLista == 1) {
                editarSucursales = filtrarSucursales.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
                PrimefacesContextUI.ejecutar("PF('editPais').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editSubtituloFirma");
                PrimefacesContextUI.ejecutar("PF('editSubtituloFirma').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editBancos");
                PrimefacesContextUI.ejecutar("PF('editBancos').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCiudades");
                PrimefacesContextUI.ejecutar("PF('editCiudades').show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoSucursales() {
        System.out.println("agregarNuevoSucursales");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoSucursales.getCodigo() == null) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoSucursales.getCodigo());

            for (int x = 0; x < listSucursales.size(); x++) {
                if (listSucursales.get(x).getCodigo().equals(nuevoSucursales.getCodigo())) {
                    duplicados++;
                }
            }
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;//1
            }
        }
        if (nuevoSucursales.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;//2

        }
        if (nuevoSucursales.getBanco().getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Banco \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;//3

        }

        if (nuevoSucursales.getCiudad().getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Ciudad \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;//4

        }

        System.out.println("contador " + contador);

        if (contador == 4) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosSucursales:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSucursales:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                personafir = (Column) c.getViewRoot().findComponent("form:datosSucursales:personafir");
                personafir.setFilterStyle("display: none; visibility: hidden;");
                cargo = (Column) c.getViewRoot().findComponent("form:datosSucursales:cargo");
                cargo.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarSucursales = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoSucursales.setSecuencia(l);

            crearSucursales.add(nuevoSucursales);

            listSucursales.add(nuevoSucursales);
            nuevoSucursales = new Sucursales();
            nuevoSucursales.setCiudad(new Ciudades());
            nuevoSucursales.setBanco(new Bancos());
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            infoRegistro = "Cantidad de registros: " + listSucursales.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroSucursales').hide()");
            RequestContext.getCurrentInstance().update("nuevoRegistroSucursales').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoSucursales() {
        System.out.println("limpiarNuevoSucursales");
        nuevoSucursales = new Sucursales();
        nuevoSucursales.setBanco(new Bancos());
        nuevoSucursales.setCiudad(new Ciudades());
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void cargarNuevoSucursales() {
        System.out.println("cargarNuevoSucursales");

        duplicarSucursales = new Sucursales();
        duplicarSucursales.setBanco(new Bancos());
        duplicarSucursales.setCiudad(new Ciudades());
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.ejecutar("PF('nuevoRegistroSucursales').show()");
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoempresa");

    }

    public void duplicandoSucursales() {
        System.out.println("duplicandoSucursales");
        if (index >= 0) {
            duplicarSucursales = new Sucursales();
            duplicarSucursales.setBanco(new Bancos());
            duplicarSucursales.setCiudad(new Ciudades());
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarSucursales.setSecuencia(l);
                duplicarSucursales.setCodigo(listSucursales.get(index).getCodigo());
                duplicarSucursales.setNombre(listSucursales.get(index).getNombre());
                duplicarSucursales.setBanco(listSucursales.get(index).getBanco());
                duplicarSucursales.setCiudad(listSucursales.get(index).getCiudad());
            }
            if (tipoLista == 1) {
                duplicarSucursales.setSecuencia(l);
                duplicarSucursales.setCodigo(filtrarSucursales.get(index).getCodigo());
                duplicarSucursales.setNombre(filtrarSucursales.get(index).getNombre());
                duplicarSucursales.setBanco(filtrarSucursales.get(index).getBanco());
                duplicarSucursales.setCiudad(filtrarSucursales.get(index).getCiudad());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroSucursales').show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarSucursales.getCodigo());

        if (duplicarSucursales.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listSucursales.size(); x++) {
                if (listSucursales.get(x).getCodigo().equals(duplicarSucursales.getCodigo())) {
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

        if (duplicarSucursales.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarSucursales.getBanco().getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   *Banco \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarSucursales.getCiudad().getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   *Ciudad \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 4) {

            System.out.println("Datos Duplicando: " + duplicarSucursales.getSecuencia() + "  " + duplicarSucursales.getCodigo());
            if (crearSucursales.contains(duplicarSucursales)) {
                System.out.println("Ya lo contengo.");
            }
            listSucursales.add(duplicarSucursales);
            crearSucursales.add(duplicarSucursales);
            RequestContext.getCurrentInstance().update("form:datosSucursales");
            index = -1;
            System.out.println("--------------DUPLICAR------------------------");
            System.out.println("CODIGO : " + duplicarSucursales.getCodigo());
            System.out.println("EMPRESA: " + duplicarSucursales.getNombre());
            System.out.println("PERSONA : " + duplicarSucursales.getBanco().getNombre());
            System.out.println("CARGO : " + duplicarSucursales.getCiudad().getNombre());
            System.out.println("--------------DUPLICAR------------------------");

            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listSucursales.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosSucursales:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSucursales:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                personafir = (Column) c.getViewRoot().findComponent("form:datosSucursales:personafir");
                personafir.setFilterStyle("display: none; visibility: hidden;");
                cargo = (Column) c.getViewRoot().findComponent("form:datosSucursales:cargo");
                cargo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSucursales");
                bandera = 0;
                filtrarSucursales = null;
                tipoLista = 0;
            }
            duplicarSucursales = new Sucursales();
            duplicarSucursales.setCiudad(new Ciudades());
            duplicarSucursales.setBanco(new Bancos());

            PrimefacesContextUI.ejecutar("PF('duplicarRegistroSucursales').hide()");
            RequestContext.getCurrentInstance().update("duplicarRegistroSucursales').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarSucursales() {
        duplicarSucursales = new Sucursales();
        duplicarSucursales.setBanco(new Bancos());
        duplicarSucursales.setCiudad(new Ciudades());
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSucursalesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SUCURSALES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSucursalesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SUCURSALES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listSucursales.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "SUCURSALES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("SUCURSALES")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Sucursales> getListSucursales() {
        if (listSucursales == null) {
            listSucursales = administrarSucursales.consultarSucursales();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSucursales == null || listSucursales.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSucursales.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listSucursales;
    }

    public void setListSucursales(List<Sucursales> listSucursales) {
        this.listSucursales = listSucursales;
    }

    public List<Sucursales> getFiltrarSucursales() {
        return filtrarSucursales;
    }

    public void setFiltrarSucursales(List<Sucursales> filtrarSucursales) {
        this.filtrarSucursales = filtrarSucursales;
    }

    public Sucursales getNuevoSucursales() {
        return nuevoSucursales;
    }

    public void setNuevoSucursales(Sucursales nuevoSucursales) {
        this.nuevoSucursales = nuevoSucursales;
    }

    public Sucursales getDuplicarSucursales() {
        return duplicarSucursales;
    }

    public void setDuplicarSucursales(Sucursales duplicarSucursales) {
        this.duplicarSucursales = duplicarSucursales;
    }

    public Sucursales getEditarSucursales() {
        return editarSucursales;
    }

    public void setEditarSucursales(Sucursales editarSucursales) {
        this.editarSucursales = editarSucursales;
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
    private String infoRegistroBancos;

    public List<Bancos> getListaBancos() {
        if (listaBancos == null) {
            listaBancos = administrarSucursales.consultarLOVBancos();
        }

        RequestContext context = RequestContext.getCurrentInstance();
        if (listaBancos == null || listaBancos.isEmpty()) {
            infoRegistroBancos = "Cantidad de registros: 0 ";
        } else {
            infoRegistroBancos = "Cantidad de registros: " + listaBancos.size();
        }
        RequestContext.getCurrentInstance().update("form:infoRegistroBancos");
        return listaBancos;
    }

    public void setListaBancos(List<Bancos> listaBancos) {
        this.listaBancos = listaBancos;
    }

    public List<Bancos> getFiltradoBancos() {
        return filtradoBancos;
    }

    public void setFiltradoBancos(List<Bancos> filtradoBancos) {
        this.filtradoBancos = filtradoBancos;
    }

    public Bancos getBancoSeleccionado() {
        return bancoSeleccionado;
    }

    public void setBancoSeleccionado(Bancos bancoSeleccionado) {
        this.bancoSeleccionado = bancoSeleccionado;
    }
    private String infoRegistroCiudades;

    public List<Ciudades> getListaCiudades() {
        if (listaCiudades == null) {
            listaCiudades = administrarSucursales.consultarLOVCiudades();
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

    public Ciudades getCiudadSeleccionado() {
        return cargoSeleccionado;
    }

    public void setCiudadSeleccionado(Ciudades cargoSeleccionado) {
        this.cargoSeleccionado = cargoSeleccionado;
    }

    public Sucursales getSucursalSeleccionada() {
        return sucursalSeleccionada;
    }

    public void setSucursalSeleccionada(Sucursales sucursalSeleccionada) {
        this.sucursalSeleccionada = sucursalSeleccionada;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroBancos() {
        return infoRegistroBancos;
    }

    public void setInfoRegistroBancos(String infoRegistroBancos) {
        this.infoRegistroBancos = infoRegistroBancos;
    }

    public String getInfoRegistroCiudades() {
        return infoRegistroCiudades;
    }

    public void setInfoRegistroCiudades(String infoRegistroCiudades) {
        this.infoRegistroCiudades = infoRegistroCiudades;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

}
