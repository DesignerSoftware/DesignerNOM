/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.EstadosCiviles;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEstadosCivilesInterface;
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
public class ControlEstadosCiviles implements Serializable {

    @EJB
    AdministrarEstadosCivilesInterface administrarEstadosCiviles;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<EstadosCiviles> listEstadosCiviles;
    private List<EstadosCiviles> filtrarEstadosCiviles;
    private List<EstadosCiviles> crearEstadosCiviles;
    private List<EstadosCiviles> modificarEstadosCiviles;
    private List<EstadosCiviles> borrarEstadosCiviles;
    private EstadosCiviles nuevoEstadoCivil;
    private EstadosCiviles duplicarEstadoCivil;
    private EstadosCiviles editarEstadoCivil;
    private EstadosCiviles estadoCivilSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger vigenciasEstadosAficilaciones;
    private Integer a;
    private int tamano;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String paginaanterior;
    private DataTable tablaC;
    private boolean activarLov;

    public ControlEstadosCiviles() {
        listEstadosCiviles = null;
        crearEstadosCiviles = new ArrayList<EstadosCiviles>();
        modificarEstadosCiviles = new ArrayList<EstadosCiviles>();
        borrarEstadosCiviles = new ArrayList<EstadosCiviles>();
        permitirIndex = true;
        editarEstadoCivil = new EstadosCiviles();
        nuevoEstadoCivil = new EstadosCiviles();
        duplicarEstadoCivil = new EstadosCiviles();
        a = null;
        guardado = true;
        tamano = 270;
        activarLov = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEstadosCiviles.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPag(String pagina) {
        paginaanterior = pagina;
        listEstadosCiviles = null;
        getListEstadosCiviles();
        contarRegistros();
        deshabilitarBotonLov();
        if (!listEstadosCiviles.isEmpty()) {
            estadoCivilSeleccionado = listEstadosCiviles.get(0);
        }
    }

    public String retornarPagina() {
        return paginaanterior;
    }

    public void cambiarIndice(EstadosCiviles estadoCivil, int celda) {
        if (permitirIndex == true) {
            estadoCivilSeleccionado = estadoCivil;
            cualCelda = celda;
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = estadoCivilSeleccionado.getCodigo();
                } else {
                    backUpCodigo = estadoCivilSeleccionado.getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = estadoCivilSeleccionado.getDescripcion();
                } else {
                    backUpDescripcion = estadoCivilSeleccionado.getDescripcion();
                }
            }
            estadoCivilSeleccionado.getSecuencia();

        }

    }

    public void asignarIndex(EstadosCiviles estadoCivil, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlEstadosCiviles.asignarIndex \n");
            estadoCivilSeleccionado = estadoCivil;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlEstadosCiviles.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }
    private String infoRegistro;

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            bandera = 0;
            filtrarEstadosCiviles = null;
            tipoLista = 0;
        }

        borrarEstadosCiviles.clear();
        crearEstadosCiviles.clear();
        modificarEstadosCiviles.clear();
        k = 0;
        listEstadosCiviles = null;
        estadoCivilSeleccionado = null;
        getListEstadosCiviles();
        contarRegistros();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        modificarInfoRegistro(listEstadosCiviles.size());
        RequestContext.getCurrentInstance().update("form:infoRegistro");
        RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            bandera = 0;
            filtrarEstadosCiviles = null;
            tipoLista = 0;
        }

        borrarEstadosCiviles.clear();
        crearEstadosCiviles.clear();
        modificarEstadosCiviles.clear();
        estadoCivilSeleccionado = null;
        k = 0;
        listEstadosCiviles = null;
        guardado = true;
        permitirIndex = true;
        getListEstadosCiviles();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:infoRegistro");
        RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:codigo");
            codigo.setFilterStyle("width: 85%");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:descripcion");
            descripcion.setFilterStyle("width: 85%");
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            bandera = 0;
            filtrarEstadosCiviles = null;
            tipoLista = 0;
        }
    }

    public void modificarEstadoCivil(EstadosCiviles estadoCivil, String confirmarCambio, String valorConfirmar) {
        estadoCivilSeleccionado = estadoCivil;
        int contador = 0;
        boolean banderita = false;

        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR Enfermedades, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearEstadosCiviles.contains(estadoCivilSeleccionado)) {
                    if (estadoCivilSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        estadoCivilSeleccionado.setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listEstadosCiviles.size(); j++) {
                            if (estadoCivilSeleccionado.getCodigo() == listEstadosCiviles.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                        if (contador > 0) {
                            banderita = false;
                            estadoCivilSeleccionado.setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (estadoCivilSeleccionado.getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        estadoCivilSeleccionado.setDescripcion(backUpDescripcion);
                    }
                    if (estadoCivilSeleccionado.getDescripcion().equals(" ")) {
                        banderita = false;
                        estadoCivilSeleccionado.setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarEstadosCiviles.isEmpty()) {
                            modificarEstadosCiviles.add(estadoCivilSeleccionado);
                        } else if (!modificarEstadosCiviles.contains(estadoCivilSeleccionado)) {
                            modificarEstadosCiviles.add(estadoCivilSeleccionado);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                    //estadoCivilSeleccionado = null;
                } else {
                    if (estadoCivilSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        estadoCivilSeleccionado.setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listEstadosCiviles.size(); j++) {
                            if (estadoCivilSeleccionado.getCodigo() == listEstadosCiviles.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                        if (contador > 0) {
                            banderita = false;
                            estadoCivilSeleccionado.setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (estadoCivilSeleccionado.getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        estadoCivilSeleccionado.setDescripcion(backUpDescripcion);
                    }
                    if (estadoCivilSeleccionado.getDescripcion().equals(" ")) {
                        banderita = false;
                        estadoCivilSeleccionado.setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
//                    estadoCivilSeleccionado = null;
                }
            } else {

                if (!crearEstadosCiviles.contains(estadoCivilSeleccionado)) {
                    if (estadoCivilSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        estadoCivilSeleccionado.setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listEstadosCiviles.size(); j++) {
                            if (estadoCivilSeleccionado.getCodigo() == listEstadosCiviles.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            estadoCivilSeleccionado.setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (estadoCivilSeleccionado.getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        estadoCivilSeleccionado.setDescripcion(backUpDescripcion);
                    }
                    if (estadoCivilSeleccionado.getDescripcion().equals(" ")) {
                        estadoCivilSeleccionado.setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarEstadosCiviles.isEmpty()) {
                            modificarEstadosCiviles.add(estadoCivilSeleccionado);
                        } else if (!modificarEstadosCiviles.contains(estadoCivilSeleccionado)) {
                            modificarEstadosCiviles.add(estadoCivilSeleccionado);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
                   // estadoCivilSeleccionado = null;
                } else {
                    if (estadoCivilSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        estadoCivilSeleccionado.setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listEstadosCiviles.size(); j++) {
                            if (estadoCivilSeleccionado.getCodigo() == listEstadosCiviles.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            estadoCivilSeleccionado.setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }

                    if (estadoCivilSeleccionado.getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        estadoCivilSeleccionado.setDescripcion(backUpDescripcion);
                    }
                    if (estadoCivilSeleccionado.getDescripcion().equals(" ")) {
                        estadoCivilSeleccionado.setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
                    }
               //     estadoCivilSeleccionado = null;
                }

            }
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoEstadoCivil() {

        if (estadoCivilSeleccionado != null) {
            if (!modificarEstadosCiviles.isEmpty() && modificarEstadosCiviles.contains(estadoCivilSeleccionado)) {
                modificarEstadosCiviles.remove(modificarEstadosCiviles.indexOf(estadoCivilSeleccionado));
                borrarEstadosCiviles.add(estadoCivilSeleccionado);
            } else if (!crearEstadosCiviles.isEmpty() && crearEstadosCiviles.contains(estadoCivilSeleccionado)) {
                crearEstadosCiviles.remove(crearEstadosCiviles.indexOf(estadoCivilSeleccionado));
            } else {
                borrarEstadosCiviles.add(estadoCivilSeleccionado);
            }
            listEstadosCiviles.remove(estadoCivilSeleccionado);
            if (tipoLista == 1) {
                filtrarEstadosCiviles.remove(estadoCivilSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            modificarInfoRegistro(listEstadosCiviles.size());
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }else{
            PrimefacesContextUI.ejecutar("PF('formularioDialogos:seleccionarRegistro').show()");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        try {
            System.err.println("Control Secuencia de EstadosCiviles a borrar");
            vigenciasEstadosAficilaciones = administrarEstadosCiviles.verificarVigenciasEstadosCiviles(estadoCivilSeleccionado.getSecuencia());

            if (!vigenciasEstadosAficilaciones.equals(new BigInteger("0"))) {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                estadoCivilSeleccionado = null;

                vigenciasEstadosAficilaciones = new BigInteger("-1");

            } else {
                System.out.println("Borrado==0");
                borrandoEstadoCivil();
            }
        } catch (Exception e) {
            System.err.println("ERROR ControlEstadosCiviles verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarEstadosCiviles.isEmpty() || !crearEstadosCiviles.isEmpty() || !modificarEstadosCiviles.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }
    }

    public void guardarEstadoCivil() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando EstadosCiviles");
            if (!borrarEstadosCiviles.isEmpty()) {
                administrarEstadosCiviles.borrarEstadosCiviles(borrarEstadosCiviles);
                //mostrarBorrados
                registrosBorrados = borrarEstadosCiviles.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarEstadosCiviles.clear();
            }
            if (!crearEstadosCiviles.isEmpty()) {
                administrarEstadosCiviles.crearEstadosCiviles(crearEstadosCiviles);
                crearEstadosCiviles.clear();
            }
            if (!modificarEstadosCiviles.isEmpty()) {
                administrarEstadosCiviles.modificarEstadosCiviles(modificarEstadosCiviles);
                modificarEstadosCiviles.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listEstadosCiviles = null;
            getListEstadosCiviles();
            contarRegistros();
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            k = 0;
            guardado = true;
        }
        estadoCivilSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (estadoCivilSeleccionado != null) {
            if (tipoLista == 0) {
                editarEstadoCivil = estadoCivilSeleccionado;
            }
            if (tipoLista == 1) {
                editarEstadoCivil = estadoCivilSeleccionado;
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
        } else{
            PrimefacesContextUI.ejecutar("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoEstadoCivil() {
        System.out.println("Agregar EstadosCiviles");
        int contador = 0;
        int duplicados = 0;

        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoEstadoCivil.getCodigo() == a) {
            mensajeValidacion = " Campo código vacío \n";
            System.out.println( mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoEstadoCivil.getCodigo());

            for (int x = 0; x < listEstadosCiviles.size(); x++) {
                if (listEstadosCiviles.get(x).getCodigo().equals(nuevoEstadoCivil.getCodigo())) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "El Código ingresado está relacionado con un registro anterior \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoEstadoCivil.getDescripcion() == (null)) {
            mensajeValidacion = mensajeValidacion + " Campo Descripción vacío \n";
            System.out.println( mensajeValidacion);

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
                codigo = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
                bandera = 0;
                filtrarEstadosCiviles = null;
                tipoLista = 0;
                tamano = 270;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoEstadoCivil.setSecuencia(l);
            crearEstadosCiviles.add(nuevoEstadoCivil);
            listEstadosCiviles.add(nuevoEstadoCivil);
            nuevoEstadoCivil = new EstadosCiviles();
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            modificarInfoRegistro(listEstadosCiviles.size());
            estadoCivilSeleccionado = nuevoEstadoCivil;
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            PrimefacesContextUI.ejecutar("PF('nuevoRegistroEstadoCivil').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoEstadoCivil");
            PrimefacesContextUI.ejecutar("PF('validacionNuevoEstadoCivil').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoEstadoCivil() {
        System.out.println("limpiarNuevoEstadosCiviles");
        nuevoEstadoCivil = new EstadosCiviles();

    }

    //------------------------------------------------------------------------------
    public void duplicarEstadosCiviles() {
        System.out.println("duplicarEstadosCiviles");
        if (estadoCivilSeleccionado != null) {
            duplicarEstadoCivil = new EstadosCiviles();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarEstadoCivil.setSecuencia(l);
                duplicarEstadoCivil.setCodigo(estadoCivilSeleccionado.getCodigo());
                duplicarEstadoCivil.setDescripcion(estadoCivilSeleccionado.getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarEstadoCivil.setSecuencia(l);
                duplicarEstadoCivil.setCodigo(estadoCivilSeleccionado.getCodigo());
                duplicarEstadoCivil.setDescripcion(estadoCivilSeleccionado.getDescripcion());
                tamano = 270;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEC");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroEstadoCivil').show()");
            
        } else{
            PrimefacesContextUI.ejecutar("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR EstadosCiviles");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        System.err.println("ConfirmarDuplicar codigo " + duplicarEstadoCivil.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarEstadoCivil.getDescripcion());

        if (duplicarEstadoCivil.getCodigo() == a) {
            mensajeValidacion = "Campo Código vacío \n";
            System.out.println( mensajeValidacion);
        } else {
            for (int x = 0; x < listEstadosCiviles.size(); x++) {
                if (listEstadosCiviles.get(x).getCodigo().equals(duplicarEstadoCivil.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " El Código ingresado está relacionado con un registro anterior \n";
                System.out.println( mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarEstadoCivil.getDescripcion() == null || duplicarEstadoCivil.getDescripcion().isEmpty()) {
            mensajeValidacion = " Campo Descripción vacío \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarEstadoCivil.getSecuencia() + "  " + duplicarEstadoCivil.getCodigo());
            if (crearEstadosCiviles.contains(duplicarEstadoCivil)) {
            }
            listEstadosCiviles.add(duplicarEstadoCivil);
            crearEstadosCiviles.add(duplicarEstadoCivil);
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            modificarInfoRegistro(listEstadosCiviles.size());
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            estadoCivilSeleccionado = duplicarEstadoCivil;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
                bandera = 0;
                filtrarEstadosCiviles = null;
                tipoLista = 0;
            }
            duplicarEstadoCivil = new EstadosCiviles();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroEstadoCivil').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarEstadosCiviles() {
        duplicarEstadoCivil = new EstadosCiviles();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEstadosCivilesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ESTADOSCIVILES", false, false, "UTF-8", null, null);
        context.responseComplete();
        estadoCivilSeleccionado = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEstadosCivilesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ESTADOSCIVILES", false, false, "UTF-8", null, null);
        context.responseComplete();
        estadoCivilSeleccionado = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
            if (estadoCivilSeleccionado != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(estadoCivilSeleccionado.getSecuencia(), "ESTADOSCIVILES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            } 
         else {
            if (administrarRastros.verificarHistoricosTabla("ESTADOSCIVILES")) { // igual acá
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }
        }
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            modificarInfoRegistro(filtrarEstadosCiviles.size());
            RequestContext.getCurrentInstance().update("form:infoRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlEstadosCiviles eventoFiltrar ERROR== " + e.getMessage());
        }
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void contarRegistros() {
        if (listEstadosCiviles != null) {
            modificarInfoRegistro(listEstadosCiviles.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void recordarSeleccion() {
        if (estadoCivilSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosEstadosCiviles");
            tablaC.setSelection(estadoCivilSeleccionado);
        }
    }
    //////////* SETS Y GETS*/////////////////////////////////

    public List<EstadosCiviles> getListEstadosCiviles() {
        if (listEstadosCiviles == null) {
            listEstadosCiviles = administrarEstadosCiviles.consultarEstadosCiviles();
        }
        return listEstadosCiviles;
    }

    public void setListEstadosCiviles(List<EstadosCiviles> listEstadosCiviles) {
        this.listEstadosCiviles = listEstadosCiviles;
    }

    public List<EstadosCiviles> getFiltrarEstadosCiviles() {
        return filtrarEstadosCiviles;
    }

    public void setFiltrarEstadosCiviles(List<EstadosCiviles> filtrarEstadosCiviles) {
        this.filtrarEstadosCiviles = filtrarEstadosCiviles;
    }

    public List<EstadosCiviles> getModificarEstadosCiviles() {
        return modificarEstadosCiviles;
    }

    public void setModificarEstadosCiviles(List<EstadosCiviles> modificarEstadosCiviles) {
        this.modificarEstadosCiviles = modificarEstadosCiviles;
    }

    public EstadosCiviles getNuevoEstadoCivil() {
        return nuevoEstadoCivil;
    }

    public void setNuevoEstadoCivil(EstadosCiviles nuevoEstadoCivil) {
        this.nuevoEstadoCivil = nuevoEstadoCivil;
    }

    public EstadosCiviles getDuplicarEstadoCivil() {
        return duplicarEstadoCivil;
    }

    public void setDuplicarEstadoCivil(EstadosCiviles duplicarEstadoCivil) {
        this.duplicarEstadoCivil = duplicarEstadoCivil;
    }

    public EstadosCiviles getEditarEstadoCivil() {
        return editarEstadoCivil;
    }

    public void setEditarEstadoCivil(EstadosCiviles editarEstadoCivil) {
        this.editarEstadoCivil = editarEstadoCivil;
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

    public EstadosCiviles getEstadoCivilSeleccionado() {
        return estadoCivilSeleccionado;
    }

    public void setEstadoCivilSeleccionado(EstadosCiviles estadoCivilSeleccionado) {
        this.estadoCivilSeleccionado = estadoCivilSeleccionado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getPaginaanterior() {
        return paginaanterior;
    }

    public void setPaginaanterior(String paginaanterior) {
        this.paginaanterior = paginaanterior;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
