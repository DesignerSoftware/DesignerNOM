/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvReferencias;
import Entidades.Personas;
import Entidades.TiposFamiliares;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarHvReferenciasInterface;
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
public class ControlHvReferencias1 implements Serializable {

    @EJB
    AdministrarHvReferenciasInterface administrarHvReferencias1;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<HvReferencias> listHvReferencias1;
    private List<HvReferencias> filtrarHvReferencias1;
    private List<HvReferencias> crearHvReferencias1;
    private List<HvReferencias> modificarHvReferencias1;
    private List<HvReferencias> borrarHvReferencias1;
    private HvReferencias nuevoHvReferencia1;
    private HvReferencias duplicarHvReferencia1;
    private HvReferencias editarHvReferencia1;
    private HvReferencias hvReferencia1Seleccionada;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column cargo, nombre, numTelefono, numCelular, parentesco;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger secuenciaPersona;
//Empleado
    private Personas personaSeleccionada;
//otros
    private HVHojasDeVida hvHojasDeVida;
    private List<HVHojasDeVida> listHvHojasDeVida;
    //autocompletar
    private String tiposFamiliares;
    private List<TiposFamiliares> listaTiposFamiliares;
    private List<TiposFamiliares> filtradoTiposFamiliares;
    private TiposFamiliares tipoFamiliarSeleccionado;
    private String nuevoParentesco;
    private String infoRegistro;
    private String infoRegistroParentesco;
    private int tamano;
    private String backUpNombre;
    private Long backUpTelefono;
    private Empleados empleado;
    private DataTable tablaC;
    private boolean activarLov;

    public ControlHvReferencias1() {
        listHvReferencias1 = null;
        crearHvReferencias1 = new ArrayList<HvReferencias>();
        modificarHvReferencias1 = new ArrayList<HvReferencias>();
        borrarHvReferencias1 = new ArrayList<HvReferencias>();
        permitirIndex = true;
        guardado = true;
        editarHvReferencia1 = new HvReferencias();
        nuevoHvReferencia1 = new HvReferencias();
        duplicarHvReferencia1 = new HvReferencias();
        personaSeleccionada = null;
        secuenciaPersona = null;
        listHvHojasDeVida = new ArrayList<HVHojasDeVida>();
        listaTiposFamiliares = null;
        filtradoTiposFamiliares = null;
        tipoLista = 0;
        tamano = 270;
        aceptar = true;
        empleado = new Empleados();
        activarLov = true;

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarHvReferencias1.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger sec) {
       secuenciaPersona = sec;
        listHvReferencias1 = null;
        empleado = administrarHvReferencias1.empleadoActual(sec);
        getListHvReferencias1();
        contarRegistros();
        deshabilitarBotonLov();
        if (!listHvReferencias1.isEmpty()) {
            hvReferencia1Seleccionada = listHvReferencias1.get(0);
        }
    }

    public void mostrarNuevo() {
        System.err.println("nuevo Tipo Entrevista " + nuevoHvReferencia1.getTipo());
    }

    /* public void mostrarInfo(int indice, int celda) {
     if (permitirIndex == true) {
     index = indice;
     cualCelda = celda;
     secRegistro = listHvReferencias.get(index).getSecuencia();
     System.err.println("MODIFICAR FECHA \n Indice" + indice + "Fecha " + listHvReferencias.get(indice).getNombrepersona());
     System.out.println("Tipo Entrevista " + listHvReferencias.get(indice).getTipo());
     if (!crearHvReferencias.contains(listHvReferencias.get(indice))) {

     if (modificarHvReferencias.isEmpty()) {
     modificarHvReferencias.add(listHvReferencias.get(indice));
     } else if (!modificarHvReferencias.contains(listHvReferencias.get(indice))) {
     modificarHvReferencias.add(listHvReferencias.get(indice));
     }
     if (guardado == true) {
     guardado = false;
     //PrimefacesContextUI.actualizar("form:aceptar");
     }
     }
     }
     System.out.println("Indice: " + index + " Celda: " + cualCelda);

     }
     */
    public void cambiarIndice(HvReferencias hvReferencia, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            hvReferencia1Seleccionada = hvReferencia;
            cualCelda = celda;
            hvReferencia1Seleccionada.getSecuencia();

            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpNombre = hvReferencia1Seleccionada.getNombrepersona();
                } else {
                    backUpNombre = hvReferencia1Seleccionada.getNombrepersona();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpTelefono = hvReferencia1Seleccionada.getTelefono();
                } else {
                    backUpTelefono = hvReferencia1Seleccionada.getTelefono();
                }
            }

            if (cualCelda == 4) {
                if (tipoLista == 0) {
                    tiposFamiliares = hvReferencia1Seleccionada.getParentesco().getTipo();
                } else {
                    tiposFamiliares = hvReferencia1Seleccionada.getParentesco().getTipo();
                }
                System.out.println("Cambiar Indice Tipo de Familia : " + tiposFamiliares);
            }

        }
    }

    public void asignarIndex(HvReferencias hvReferencia, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlHvReferencias.asignarIndex \n");
            RequestContext context = RequestContext.getCurrentInstance();

            hvReferencia1Seleccionada = hvReferencia;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }
            if (dig == 4) {
                PrimefacesContextUI.actualizar("form:sucursalesDialogo");
                PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                dig = -1;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlHvReferencias.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        if (hvReferencia1Seleccionada != null) {
            RequestContext context = RequestContext.getCurrentInstance();

            if (cualCelda == 4) {
                PrimefacesContextUI.actualizar("form:sucursalesDialogo");
                PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                tipoActualizacion = 0;
            }

        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
            numTelefono.setFilterStyle("display: none; visibility: hidden;");
            numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
            numCelular.setFilterStyle("display: none; visibility: hidden;");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:parentesco");
            parentesco.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosHvReferencia");
            bandera = 0;
            filtrarHvReferencias1 = null;
            tipoLista = 0;
        }

        borrarHvReferencias1.clear();
        crearHvReferencias1.clear();
        modificarHvReferencias1.clear();
        hvReferencia1Seleccionada = null;
        hvReferencia1Seleccionada = null;
        k = 0;
        listHvReferencias1 = null;
        guardado = true;
        permitirIndex = true;
        getListHvReferencias1();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        PrimefacesContextUI.actualizar("form:datosHvReferencia");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
            numTelefono.setFilterStyle("display: none; visibility: hidden;");
            numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
            numCelular.setFilterStyle("display: none; visibility: hidden;");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:parentesco");
            parentesco.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosHvReferencia");
            bandera = 0;
            filtrarHvReferencias1 = null;
            tipoLista = 0;
        }

        borrarHvReferencias1.clear();
        crearHvReferencias1.clear();
        modificarHvReferencias1.clear();
        hvReferencia1Seleccionada = null;
        hvReferencia1Seleccionada = null;
        k = 0;
        listHvReferencias1 = null;
        guardado = true;
        permitirIndex = true;
        getListHvReferencias1();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        PrimefacesContextUI.actualizar("form:datosHvReferencia");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
            nombre.setFilterStyle("width: 85%");
            cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
            cargo.setFilterStyle("width: 85%");
            numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
            numTelefono.setFilterStyle("width: 85%");
            numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
            numCelular.setFilterStyle("width: 85%");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:parentesco");
            parentesco.setFilterStyle("width: 85%");
            PrimefacesContextUI.actualizar("form:datosHvReferencia");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
            numTelefono.setFilterStyle("display: none; visibility: hidden;");
            numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
            numCelular.setFilterStyle("display: none; visibility: hidden;");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:parentesco");
            parentesco.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosHvReferencia");
            bandera = 0;
            filtrarHvReferencias1 = null;
            tipoLista = 0;
        }
    }

    /*   public void modificandoHvReferencia(int indice, String confirmarCambio, String valorConfirmar) {
     System.err.println("ENTRE A MODIFICAR HV Referencia");
     index = indice;

     int contador = 0;
     boolean banderita = false;
     Short a;
     a = null;
     RequestContext context = RequestContext.getCurrentInstance();
     System.err.println("TIPO LISTA = " + tipoLista);
     if (confirmarCambio.equalsIgnoreCase("N")) {
     System.err.println("ENTRE A MODIFICAR HvReferencia, CONFIRMAR CAMBIO ES N");
     if (tipoLista == 0) {
     if (!crearHvReferenciasFamiliares.contains(hvReferencia1Seleccionada )) {

     if (hvReferencia1Seleccionada .getNombrepersona().isEmpty()) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     } else if (hvReferencia1Seleccionada .getNombrepersona().equals(" ")) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     } else {
     banderita = true;
     }

     if (banderita == true) {
     if (modificarHvReferenciasFamiliares.isEmpty()) {
     modificarHvReferenciasFamiliares.add(hvReferencia1Seleccionada );
     } else if (!modificarHvReferenciasFamiliares.contains(hvReferencia1Seleccionada )) {
     modificarHvReferenciasFamiliares.add(hvReferencia1Seleccionada );
     }
     if (guardado == true) {
     guardado = false;
     }

     } else {
     PrimefacesContextUI.actualizar("form:validacionModificar");
     PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
     cancelarModificacion();
     }
     hvReferencia1Seleccionada = null;
     hvReferencia1Seleccionada = null;
     }
     } else {

     if (!crearHvReferenciasFamiliares.contains(hvReferencia1Seleccionada )) {
     if (hvReferencia1Seleccionada .getNombrepersona().isEmpty()) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     }
     if (hvReferencia1Seleccionada .getNombrepersona().equals(" ")) {
     mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
     banderita = false;
     }

     if (banderita == true) {
     if (modificarHvReferenciasFamiliares.isEmpty()) {
     modificarHvReferenciasFamiliares.add(hvReferencia1Seleccionada );
     } else if (!modificarHvReferenciasFamiliares.contains(hvReferencia1Seleccionada )) {
     modificarHvReferenciasFamiliares.add(hvReferencia1Seleccionada );
     }
     if (guardado == true) {
     guardado = false;
     }

     } else {
     PrimefacesContextUI.actualizar("form:validacionModificar");
     PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
     cancelarModificacion();
     }
     hvReferencia1Seleccionada = null;
     hvReferencia1Seleccionada = null;
     }

     }
     PrimefacesContextUI.actualizar("form:datosHvReferencia");
     }

     }
     */
    /**
     *
     * @param indice donde se encuentra posicionado
     * @param confirmarCambio nombre de la columna
     * @param valorConfirmar valor ingresado
     */
    public void modificarHvrReferencia1(HvReferencias hvReferencia, String confirmarCambio, String valorConfirmar) {
        hvReferencia1Seleccionada = hvReferencia;
        int coincidencias = 0;
        int indiceUnicoElemento = 0, pass = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR HvReferencia, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearHvReferencias1.contains(hvReferencia1Seleccionada)) {

                    if (hvReferencia1Seleccionada.getNombrepersona() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        hvReferencia1Seleccionada.setNombrepersona(backUpNombre);
                    } else if (hvReferencia1Seleccionada.getNombrepersona().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        hvReferencia1Seleccionada.setNombrepersona(backUpNombre);
                    } else {
                        pass++;
                    }
                    if (hvReferencia1Seleccionada.getTelefono() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        hvReferencia1Seleccionada.setTelefono(backUpTelefono);
                    } else {
                        pass++;
                    }
                    if (pass == 2) {
                        if (modificarHvReferencias1.isEmpty()) {
                            modificarHvReferencias1.add(hvReferencia1Seleccionada);
                        } else if (!modificarHvReferencias1.contains(hvReferencia1Seleccionada)) {
                            modificarHvReferencias1.add(hvReferencia1Seleccionada);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        PrimefacesContextUI.actualizar("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    hvReferencia1Seleccionada = null;
                    hvReferencia1Seleccionada = null;
                } else {
                    if (hvReferencia1Seleccionada.getNombrepersona() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        hvReferencia1Seleccionada.setNombrepersona(backUpNombre);
                    } else if (hvReferencia1Seleccionada.getNombrepersona().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        hvReferencia1Seleccionada.setNombrepersona(backUpNombre);
                    } else {
                        pass++;
                    }
                    if (hvReferencia1Seleccionada.getTelefono() == null) {
                        hvReferencia1Seleccionada.setTelefono(backUpTelefono);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    } else {
                        pass++;
                    }
                    if (pass == 2) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        PrimefacesContextUI.actualizar("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                }
            } else {

                if (!crearHvReferencias1.contains(hvReferencia1Seleccionada)) {
                    if (hvReferencia1Seleccionada.getNombrepersona() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        hvReferencia1Seleccionada.setNombrepersona(backUpNombre);
                    } else if (hvReferencia1Seleccionada.getNombrepersona().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        hvReferencia1Seleccionada.setNombrepersona(backUpNombre);
                    } else {
                        pass++;
                    }
                    if (hvReferencia1Seleccionada.getTelefono() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        hvReferencia1Seleccionada.setTelefono(backUpTelefono);
                    } else {
                        pass++;
                    }
                    if (pass == 2) {
                        if (modificarHvReferencias1.isEmpty()) {
                            modificarHvReferencias1.add(hvReferencia1Seleccionada);
                        } else if (!modificarHvReferencias1.contains(hvReferencia1Seleccionada)) {
                            modificarHvReferencias1.add(hvReferencia1Seleccionada);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        PrimefacesContextUI.actualizar("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");

                    }
                } else {
                    if (hvReferencia1Seleccionada.getNombrepersona() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        hvReferencia1Seleccionada.setNombrepersona(backUpNombre);
                    } else if (hvReferencia1Seleccionada.getNombrepersona().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        hvReferencia1Seleccionada.setNombrepersona(backUpNombre);
                    } else {
                        pass++;
                    }
                    if (hvReferencia1Seleccionada.getTelefono() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        hvReferencia1Seleccionada.setTelefono(backUpTelefono);
                    } else {
                        pass++;
                    }
                    if (pass == 2) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        PrimefacesContextUI.actualizar("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");

                    }
                }
            }
            PrimefacesContextUI.actualizar("form:datosHvReferencia");
        } else if (confirmarCambio.equalsIgnoreCase("TIPOSFAMILIARES")) {
            if (!hvReferencia1Seleccionada.getParentesco().getTipo().equals("")) {
                if (tipoLista == 0) {
                    hvReferencia1Seleccionada.getParentesco().setTipo(tiposFamiliares);

                } else {
                    hvReferencia1Seleccionada.getParentesco().setTipo(tiposFamiliares);
                }

                for (int i = 0; i < listaTiposFamiliares.size(); i++) {
                    if (listaTiposFamiliares.get(i).getTipo().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }

                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        hvReferencia1Seleccionada.setParentesco(listaTiposFamiliares.get(indiceUnicoElemento));
                    } else {
                        hvReferencia1Seleccionada.setParentesco(listaTiposFamiliares.get(indiceUnicoElemento));
                    }
                    listaTiposFamiliares.clear();
                    listaTiposFamiliares = null;
                    getListaTiposFamiliares();

                } else {
                    permitirIndex = false;
                    PrimefacesContextUI.actualizar("form:sucursalesDialogo");
                    PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                System.out.println("PUSE UN VACIO");
                hvReferencia1Seleccionada.getParentesco().setTipo(tiposFamiliares);
                hvReferencia1Seleccionada.setParentesco(new TiposFamiliares());
                coincidencias = 1;
            }

            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    if (!crearHvReferencias1.contains(hvReferencia1Seleccionada)) {

                        if (modificarHvReferencias1.isEmpty()) {
                            modificarHvReferencias1.add(hvReferencia1Seleccionada);
                        } else if (!modificarHvReferencias1.contains(hvReferencia1Seleccionada)) {
                            modificarHvReferencias1.add(hvReferencia1Seleccionada);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                        PrimefacesContextUI.actualizar("form:datosHvReferencia");
                    }
                } else {
                    if (!crearHvReferencias1.contains(hvReferencia1Seleccionada)) {

                        if (modificarHvReferencias1.isEmpty()) {
                            modificarHvReferencias1.add(hvReferencia1Seleccionada);
                        } else if (!modificarHvReferencias1.contains(hvReferencia1Seleccionada)) {
                            modificarHvReferencias1.add(hvReferencia1Seleccionada);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                    }
                }
            }

            PrimefacesContextUI.actualizar("form:datosHvReferencia");

        }
        PrimefacesContextUI.actualizar("form:datosHvReferencia");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void actualizarTipoFamiliar() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                hvReferencia1Seleccionada.setParentesco(tipoFamiliarSeleccionado);

                if (!crearHvReferencias1.contains(hvReferencia1Seleccionada)) {
                    if (modificarHvReferencias1.isEmpty()) {
                        modificarHvReferencias1.add(hvReferencia1Seleccionada);
                    } else if (!modificarHvReferencias1.contains(hvReferencia1Seleccionada)) {
                        modificarHvReferencias1.add(hvReferencia1Seleccionada);
                    }
                }
            } else {
                hvReferencia1Seleccionada.setParentesco(tipoFamiliarSeleccionado);

                if (!crearHvReferencias1.contains(hvReferencia1Seleccionada)) {
                    if (modificarHvReferencias1.isEmpty()) {
                        modificarHvReferencias1.add(hvReferencia1Seleccionada);
                    } else if (!modificarHvReferencias1.contains(hvReferencia1Seleccionada)) {
                        modificarHvReferencias1.add(hvReferencia1Seleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            permitirIndex = true;
            // PrimefacesContextUI.actualizar("form:datosHvReferencia");
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        } else if (tipoActualizacion == 1) {
            nuevoHvReferencia1.setParentesco(tipoFamiliarSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevooHvReferenciaLab");
        } else if (tipoActualizacion == 2) {
            duplicarHvReferencia1.setParentesco(tipoFamiliarSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarRRL");
        }
        filtradoTiposFamiliares = null;
        tipoFamiliarSeleccionado = null;
        aceptar = true;
        hvReferencia1Seleccionada = null;
        hvReferencia1Seleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("form:lovTiposFamiliares:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTiposFamiliares').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').hide()");
        //PrimefacesContextUI.actualizar("form:lovTiposFamiliares");
        PrimefacesContextUI.actualizar("form:datosHvReferencia");
    }

    public void cancelarCambioTiposFamiliares() {
        hvReferencia1Seleccionada.setParentesco(tipoFamiliarSeleccionado);
        filtradoTiposFamiliares = null;
        tipoFamiliarSeleccionado = null;
        aceptar = true;
        hvReferencia1Seleccionada = null;
        hvReferencia1Seleccionada = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTiposFamiliares:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTiposFamiliares').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').hide()");
    }

    public void borrandoHvReferencias() {

        if (hvReferencia1Seleccionada != null) {
            System.out.println("Entro a borrandoHvReferencias");
            if (!modificarHvReferencias1.isEmpty() && modificarHvReferencias1.contains(hvReferencia1Seleccionada)) {
                modificarHvReferencias1.remove(modificarHvReferencias1.indexOf(hvReferencia1Seleccionada));
                borrarHvReferencias1.add(hvReferencia1Seleccionada);
            } else if (!crearHvReferencias1.isEmpty() && crearHvReferencias1.contains(hvReferencia1Seleccionada)) {
                crearHvReferencias1.remove(crearHvReferencias1.indexOf(hvReferencia1Seleccionada));
            } else {
                borrarHvReferencias1.add(hvReferencia1Seleccionada);
            }
            listHvReferencias1.remove(hvReferencia1Seleccionada);

            if (tipoLista == 1) {
                filtrarHvReferencias1.remove(hvReferencia1Seleccionada);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            modificarInfoRegistro(listHvReferencias1.size());
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            PrimefacesContextUI.actualizar("form:datosHvReferencia");
            hvReferencia1Seleccionada = null;
            if (guardado == true) {
                guardado = false;
            }
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        } else {
            PrimefacesContextUI.ejecutar("PF('formularioDialogos:seleccionarRegistro').show()");
        }

    }

    /* public void verificarBorrado() {
     System.out.println("Estoy en verificarBorrado");
     try {
     System.err.println("Control Secuencia de ControlHvReferencias ");
     competenciasCargos = administrarHvEntrevistas.verificarBorradoCompetenciasCargos(listHvEntrevistas.get(index).getSecuencia());

     if (competenciasCargos.intValueExact() == 0) {
     System.out.println("Borrado==0");
     borrandoHvEntrevistas();
     } else {
     System.out.println("Borrado>0");

     RequestContext context = RequestContext.getCurrentInstance();
     PrimefacesContextUI.actualizar("form:validacionBorrar");
     PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
     hvReferencia1Seleccionada = null;

     competenciasCargos = new BigDecimal(-1);

     }
     } catch (Exception e) {
     System.err.println("ERROR ControlHvReferencias verificarBorrado ERROR " + e);
     }
     }*/
    public void revisarDialogoGuardar() {

        if (!borrarHvReferencias1.isEmpty() || !crearHvReferencias1.isEmpty() || !modificarHvReferencias1.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }

    }

    public void guardarHvReferencia() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarHvReferencia");
            if (!borrarHvReferencias1.isEmpty()) {
                for (int i = 0; i < borrarHvReferencias1.size(); i++) {
                    System.out.println("Borrando...");
                    if (borrarHvReferencias1.get(i).getParentesco().getSecuencia() == null) {
                        borrarHvReferencias1.get(i).setParentesco(null);
                    }
                }
                administrarHvReferencias1.borrarHvReferencias(borrarHvReferencias1);
                //mostrarBorrados
                registrosBorrados = borrarHvReferencias1.size();
                PrimefacesContextUI.actualizar("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarHvReferencias1.clear();
            }
            if (!crearHvReferencias1.isEmpty()) {
                for (int i = 0; i < crearHvReferencias1.size(); i++) {

                    if (crearHvReferencias1.get(i).getParentesco().getSecuencia() == null) {
                        crearHvReferencias1.get(i).setParentesco(null);
                    }
                }
                System.out.println("Creando...");
                administrarHvReferencias1.crearHvReferencias(crearHvReferencias1);
                crearHvReferencias1.clear();
            }
            if (!modificarHvReferencias1.isEmpty()) {
                for (int i = 0; i < modificarHvReferencias1.size(); i++) {
                    if (modificarHvReferencias1.get(i).getParentesco().getSecuencia() == null) {
                        modificarHvReferencias1.get(i).setParentesco(null);
                    }
                }
                administrarHvReferencias1.modificarHvReferencias(modificarHvReferencias1);
                modificarHvReferencias1.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listHvReferencias1 = null;
            getListHvReferencias1();
            contarRegistros();
            PrimefacesContextUI.actualizar("form:datosHvReferencia");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");

            k = 0;
        }
        hvReferencia1Seleccionada = null;
        guardado = true;
        PrimefacesContextUI.actualizar("form:ACEPTAR");

    }

    public void editarCelda() {
        if (hvReferencia1Seleccionada != null) {
            if (tipoLista == 0) {
                editarHvReferencia1 = hvReferencia1Seleccionada;
            }
            if (tipoLista == 1) {
                editarHvReferencia1 = hvReferencia1Seleccionada;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editNombre");
                PrimefacesContextUI.ejecutar("PF('editNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editCargo");
                PrimefacesContextUI.ejecutar("PF('editCargo').show()");
                cualCelda = -1;

            } else if (cualCelda == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editTelefono");
                PrimefacesContextUI.ejecutar("PF('editTelefono').show()");
                cualCelda = -1;

            } else if (cualCelda == 3) {
                PrimefacesContextUI.actualizar("formularioDialogos:editCelular");
                PrimefacesContextUI.ejecutar("PF('editCelular').show()");
                cualCelda = -1;

            } else if (cualCelda == 4) {
                PrimefacesContextUI.actualizar("formularioDialogos:editParentesco");
                PrimefacesContextUI.ejecutar("PF('editParentesco').show()");
                cualCelda = -1;
            }

        } else {
            PrimefacesContextUI.ejecutar("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {

        if (Campo.equalsIgnoreCase("TIPOSFAMILIARES")) {
            if (tipoNuevo == 1) {
                nuevoParentesco = nuevoHvReferencia1.getParentesco().getTipo();
            } else if (tipoNuevo == 2) {
                nuevoParentesco = duplicarHvReferencia1.getParentesco().getTipo();
            }
        }
        System.err.println("NUEVO PARENTESCO " + nuevoParentesco);
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOSFAMILIARES")) {
            if (!nuevoHvReferencia1.getParentesco().getTipo().equals("")) {
                if (tipoNuevo == 1) {
                    nuevoHvReferencia1.getParentesco().setTipo(nuevoParentesco);
                }
                for (int i = 0; i < listaTiposFamiliares.size(); i++) {
                    if (listaTiposFamiliares.get(i).getTipo().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
            } else {

                if (tipoNuevo == 1) {
                    nuevoHvReferencia1.setParentesco(new TiposFamiliares());
                    coincidencias = 1;
                }
                coincidencias = 1;
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoHvReferencia1.setParentesco(listaTiposFamiliares.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoNombreSucursal");
                }
                listaTiposFamiliares.clear();
                listaTiposFamiliares = null;
                getListaTiposFamiliares();
            } else {
                PrimefacesContextUI.actualizar("form:sucursalesDialogo");
                PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoNombreSucursal");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarNombreSucursal");
                }
            }
        }
    }

    public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOSFAMILIARES")) {
            if (!duplicarHvReferencia1.getParentesco().getTipo().equals("")) {
                if (tipoNuevo == 2) {
                    duplicarHvReferencia1.getParentesco().setTipo(nuevoParentesco);
                }
                for (int i = 0; i < listaTiposFamiliares.size(); i++) {
                    if (listaTiposFamiliares.get(i).getTipo().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 2) {
                        duplicarHvReferencia1.setParentesco(listaTiposFamiliares.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarNombreSucursal");

                    }
                    listaTiposFamiliares.clear();
                    listaTiposFamiliares = null;
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarNombreSucursal");
                    getListaTiposFamiliares();
                } else {
                    PrimefacesContextUI.actualizar("form:sucursalesDialogo");
                    PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 2) {
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicarNombreSucursal");
                    }
                }
            } else {
                if (tipoNuevo == 2) {
                    duplicarHvReferencia1.setParentesco(new TiposFamiliares());
                    System.out.println("NUEVO PARENTESCO " + nuevoParentesco);
                    if (tipoLista == 0) {
                        if (hvReferencia1Seleccionada != null) {
                            hvReferencia1Seleccionada.getParentesco().setTipo(nuevoParentesco);
                            System.err.println("tipo lista" + tipoLista);
                            System.err.println("Secuencia Parentesco " + hvReferencia1Seleccionada.getParentesco().getSecuencia());
                        }
                    } else if (tipoLista == 1) {
                        hvReferencia1Seleccionada.getParentesco().setTipo(nuevoParentesco);
                    }

                }
            }

            PrimefacesContextUI.actualizar("formularioDialogos:duplicarNombreSucursal");

        }
    }

    public void asignarVariableTiposFamiliaresNuevo(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:sucursalesDialogo");
        PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
    }

    public void agregarNuevoHvRefencias() {
        System.out.println("agregarNuevoHvRefencias");
        int contador = 0;
        nuevoHvReferencia1.setHojadevida(new HVHojasDeVida());
        Short a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoHvReferencia1.getNombrepersona() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("bandera");
            contador++;
        }
        if (nuevoHvReferencia1.getTelefono() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Telefono \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("bandera");
            contador++;
        }

        listHvHojasDeVida = administrarHvReferencias1.consultarHvHojasDeVida(secuenciaPersona);
        if (listHvHojasDeVida == null) {
            System.err.println("ERROR NULO HVHOJASDEVIDA");
        } else {
            System.err.println("tamaño listHojasDeVida " + listHvHojasDeVida.size());
            hvHojasDeVida = listHvHojasDeVida.get(0);
            System.err.println("Agregar nuevo HvHojasDeVida " + hvHojasDeVida.getSecuencia());
            nuevoHvReferencia1.setHojadevida(hvHojasDeVida);
        }

        nuevoHvReferencia1.setTipo("FAMILIARES");
        System.err.println("agregar tipo entrevista " + nuevoHvReferencia1.getTipo());
        System.out.println("contador " + contador);

        if (contador == 2) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
                cargo.setFilterStyle("display: none; visibility: hidden;");
                numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
                numTelefono.setFilterStyle("display: none; visibility: hidden;");
                numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
                numCelular.setFilterStyle("display: none; visibility: hidden;");
                parentesco = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:parentesco");
                parentesco.setFilterStyle("display: none; visibility: hidden;");

                PrimefacesContextUI.actualizar("form:datosHvReferencia");
                bandera = 0;
                filtrarHvReferencias1 = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoHvReferencia1.setSecuencia(l);
            System.err.println("---------------AGREGAR REGISTRO----------------");
            System.err.println("nombre " + nuevoHvReferencia1.getNombrepersona());
            System.err.println("cargo " + nuevoHvReferencia1.getCargo());
            System.err.println("numero de telefono " + nuevoHvReferencia1.getTelefono());
            System.err.println("numero de celular" + nuevoHvReferencia1.getCelular());
            System.err.println("-----------------------------------------------");

            crearHvReferencias1.add(nuevoHvReferencia1);
            listHvReferencias1.add(nuevoHvReferencia1);
            hvReferencia1Seleccionada = nuevoHvReferencia1;
            nuevoHvReferencia1 = new HvReferencias();
            nuevoHvReferencia1.setParentesco(new TiposFamiliares());
            PrimefacesContextUI.actualizar("form:datosHvReferencia");
            modificarInfoRegistro(listHvReferencias1.size());
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroHvReferencias').hide()");

        } else {
            PrimefacesContextUI.actualizar("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoHvReferencias() {
        System.out.println("limpiarNuevoHvEntrevistas");
        nuevoHvReferencia1 = new HvReferencias();
    }

    //------------------------------------------------------------------------------
    public void duplicandoHvEntrevistas() {
        System.out.println("duplicandoHvEntrevistas");
        if (hvReferencia1Seleccionada != null) {
            duplicarHvReferencia1 = new HvReferencias();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarHvReferencia1.setSecuencia(l);
                duplicarHvReferencia1.setNombrepersona(hvReferencia1Seleccionada.getNombrepersona());
                duplicarHvReferencia1.setCargo(hvReferencia1Seleccionada.getCargo());
                duplicarHvReferencia1.setTelefono(hvReferencia1Seleccionada.getTelefono());
                duplicarHvReferencia1.setCelular(hvReferencia1Seleccionada.getCelular());
                duplicarHvReferencia1.setHojadevida(hvReferencia1Seleccionada.getHojadevida());
                duplicarHvReferencia1.setTipo(hvReferencia1Seleccionada.getTipo());
                TiposFamiliares t = new TiposFamiliares();
                t = hvReferencia1Seleccionada.getParentesco();
                duplicarHvReferencia1.setParentesco(t);
            }
            if (tipoLista == 1) {
                duplicarHvReferencia1.setSecuencia(l);
                duplicarHvReferencia1.setNombrepersona(hvReferencia1Seleccionada.getNombrepersona());
                duplicarHvReferencia1.setCargo(hvReferencia1Seleccionada.getCargo());
                duplicarHvReferencia1.setTelefono(hvReferencia1Seleccionada.getTelefono());
                duplicarHvReferencia1.setCelular(hvReferencia1Seleccionada.getCelular());
                duplicarHvReferencia1.setHojadevida(hvReferencia1Seleccionada.getHojadevida());
                duplicarHvReferencia1.setTipo(hvReferencia1Seleccionada.getTipo());
                duplicarHvReferencia1.setParentesco(hvReferencia1Seleccionada.getParentesco());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarRRL");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroHvReferencias').show()");
            hvReferencia1Seleccionada = null;
        } else {
            PrimefacesContextUI.ejecutar("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR HVENTREVISTAS");
        int contador = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarHvReferencia1.getNombrepersona() == null) {
            mensajeValidacion = mensajeValidacion + "   *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarHvReferencia1.getTelefono() == null) {
            mensajeValidacion = mensajeValidacion + "*Telefono \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (contador == 2) {

            if (crearHvReferencias1.contains(duplicarHvReferencia1)) {
                System.out.println("Ya lo contengo.");
            } else {
                crearHvReferencias1.add(duplicarHvReferencia1);
            }
            listHvReferencias1.add(duplicarHvReferencia1);
            hvReferencia1Seleccionada = duplicarHvReferencia1;
            PrimefacesContextUI.actualizar("form:datosHvReferencia");

            System.err.println("---------------DUPLICAR REGISTRO----------------");
            System.err.println("nombre  " + duplicarHvReferencia1.getNombrepersona());
            System.err.println("cargo  " + duplicarHvReferencia1.getCargo());
            System.err.println("numero de telefono  " + duplicarHvReferencia1.getTelefono());
            System.err.println("numero de celular  " + duplicarHvReferencia1.getCelular());
            System.err.println("numero de Parentesco  " + duplicarHvReferencia1.getParentesco().getTipo());
            System.err.println("-----------------------------------------------");
            if (guardado == true) {
                guardado = false;
            }
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            modificarInfoRegistro(listHvReferencias1.size());
            PrimefacesContextUI.actualizar("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
                nombre.setFilterStyle("display: none; visibility: hidden;");
                cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
                cargo.setFilterStyle("display: none; visibility: hidden;");
                numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
                numTelefono.setFilterStyle("display: none; visibility: hidden;");
                numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
                numCelular.setFilterStyle("display: none; visibility: hidden;");
                parentesco = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:parentesco");
                parentesco.setFilterStyle("display: none; visibility: hidden;");

                PrimefacesContextUI.actualizar("form:datosHvReferencia");
                bandera = 0;
                filtrarHvReferencias1 = null;
                tipoLista = 0;
            }
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroHvReferencias').hide()");
            duplicarHvReferencia1 = new HvReferencias();

        } else {
            contador = 0;
            PrimefacesContextUI.actualizar("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarHvReferencias() {
        duplicarHvReferencia1 = new HvReferencias();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosHvReferenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "REFERENCIASFAMILIARES", false, false, "UTF-8", null, null);
        context.responseComplete();
        hvReferencia1Seleccionada = null;
        hvReferencia1Seleccionada = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosHvReferenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "REFERENCIASFAMILIARES", false, false, "UTF-8", null, null);
        context.responseComplete();
        hvReferencia1Seleccionada = null;
        hvReferencia1Seleccionada = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (hvReferencia1Seleccionada != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(hvReferencia1Seleccionada.getSecuencia(), "HVREFERENCIAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("HVREFERENCIAS")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        hvReferencia1Seleccionada = null;
    }

    public void recordarSeleccion() {
        if (hvReferencia1Seleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosHvReferencia");
            tablaC.setSelection(hvReferencia1Seleccionada);
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlHvReferencias.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            modificarInfoRegistro(filtrarHvReferencias1.size());
            PrimefacesContextUI.actualizar("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlHvReferencias eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void eventoFiltrarParentesco(){
        modificarInfoRegistroParentesco(filtradoTiposFamiliares.size());
        PrimefacesContextUI.actualizar("form:infoRegistroParentesco");
    }
    
    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void modificarInfoRegistroParentesco(int valor){
        infoRegistroParentesco=String.valueOf(valor);
    }
    
    public void contarRegistros() {
        if (listHvReferencias1 != null) {
            modificarInfoRegistro(listHvReferencias1.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    public void habilitarBotonLov() {
        activarLov = false;
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<HvReferencias> getListHvReferencias1() {
        if(listHvReferencias1 == null){
           listHvReferencias1=administrarHvReferencias1.consultarHvReferenciasPersonalesPorPersona(empleado.getPersona().getSecuencia());
       }
        return listHvReferencias1;
    }

    public void setListHvReferencias1(List<HvReferencias> listHvReferencias1) {
        this.listHvReferencias1 = listHvReferencias1;
    }

    public List<HvReferencias> getFiltrarHvReferencias1() {
        return filtrarHvReferencias1;
    }

    public void setFiltrarHvReferencias1(List<HvReferencias> filtrarHvReferencias1) {
        this.filtrarHvReferencias1 = filtrarHvReferencias1;
    }

    public List<HvReferencias> getCrearHvReferencias1() {
        return crearHvReferencias1;
    }

    public void setCrearHvReferencias1(List<HvReferencias> crearHvReferencias1) {
        this.crearHvReferencias1 = crearHvReferencias1;
    }

    public HvReferencias getNuevoHvReferencia1() {
        return nuevoHvReferencia1;
    }

    public void setNuevoHvReferencia1(HvReferencias nuevoHvReferencia1) {
        this.nuevoHvReferencia1 = nuevoHvReferencia1;
    }

    public HvReferencias getDuplicarHvReferencia1() {
        return duplicarHvReferencia1;
    }

    public void setDuplicarHvReferencia1(HvReferencias duplicarHvReferencia1) {
        this.duplicarHvReferencia1 = duplicarHvReferencia1;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }

    public BigInteger getSecuenciaPersona() {
        return secuenciaPersona;
    }

    public void setSecuenciaPersona(BigInteger secuenciaPersona) {
        this.secuenciaPersona = secuenciaPersona;
    }

    public List<TiposFamiliares> getListaTiposFamiliares() {
        if (listaTiposFamiliares == null) {
            listaTiposFamiliares = administrarHvReferencias1.consultarLOVTiposFamiliares();
        }
        return listaTiposFamiliares;
    }

    public void setListaTiposFamiliares(List<TiposFamiliares> listaTiposFamiliares) {
        this.listaTiposFamiliares = listaTiposFamiliares;
    }

    public List<TiposFamiliares> getFiltradoTiposFamiliares() {
        return filtradoTiposFamiliares;
    }

    public void setFiltradoTiposFamiliares(List<TiposFamiliares> filtradoTiposFamiliares) {
        this.filtradoTiposFamiliares = filtradoTiposFamiliares;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public HvReferencias getEditarHvReferencia1() {
        return editarHvReferencia1;
    }

    public void setEditarHvReferencia1(HvReferencias editarHvReferencia1) {
        this.editarHvReferencia1 = editarHvReferencia1;
    }

    public TiposFamiliares getTipoFamiliarSeleccionado() {
        return tipoFamiliarSeleccionado;
    }

    public void setTipoFamiliarSeleccionado(TiposFamiliares tipoFamiliarSeleccionado) {
        this.tipoFamiliarSeleccionado = tipoFamiliarSeleccionado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Personas getPersonaSeleccionada() {
        if (personaSeleccionada == null) {
            personaSeleccionada = administrarHvReferencias1.consultarPersonas(secuenciaPersona);
        }
        return personaSeleccionada;
    }

    public void setPersonaSeleccionada(Personas personaSeleccionada) {
        this.personaSeleccionada = personaSeleccionada;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public HvReferencias getHvReferencia1Seleccionada() {
        return hvReferencia1Seleccionada;
    }

    public void setHvReferencia1Seleccionada(HvReferencias hvReferencia1Seleccionada) {
        this.hvReferencia1Seleccionada = hvReferencia1Seleccionada;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroParentesco() {
        return infoRegistroParentesco;
    }

    public void setInfoRegistroParentesco(String infoRegistroParentesco) {
        this.infoRegistroParentesco = infoRegistroParentesco;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
