/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.PryPlataformas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPryPlataformasInterface;
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
public class ControlPryPlataformas implements Serializable {

    @EJB
    AdministrarPryPlataformasInterface administrarPryPlataformas;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<PryPlataformas> listPryPlataformas;
    private List<PryPlataformas> filtrarPryPlataformas;
    private List<PryPlataformas> crearPryPlataformas;
    private List<PryPlataformas> modificarPryPlataformas;
    private List<PryPlataformas> borrarPryPlataformas;
    private PryPlataformas nuevoPryPlataforma;
    private PryPlataformas duplicarPryPlataforma;
    private PryPlataformas editarPryPlataforma;
    private PryPlataformas pryPlataformaSeleccionada;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion, observacion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;

    private int tamano;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String infoRegistro;
    private DataTable tablaC;
    private boolean activarLOV;

    public ControlPryPlataformas() {
        listPryPlataformas = null;
        crearPryPlataformas = new ArrayList<PryPlataformas>();
        modificarPryPlataformas = new ArrayList<PryPlataformas>();
        borrarPryPlataformas = new ArrayList<PryPlataformas>();
        permitirIndex = true;
        editarPryPlataforma = new PryPlataformas();
        nuevoPryPlataforma = new PryPlataformas();
        duplicarPryPlataforma = new PryPlataformas();
        guardado = true;
        tamano = 270;
        activarLOV=true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPryPlataformas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    private String paginaAnterior;

    public void recibirPagina(String pagina) {
        paginaAnterior = pagina;
        listPryPlataformas = null;
        getListPryPlataformas();
        contarRegistros();
        if(listPryPlataformas == null || listPryPlataformas.isEmpty()){
        pryPlataformaSeleccionada = null;
        } else {
        pryPlataformaSeleccionada = listPryPlataformas.get(0);
        }
    }

    public String redirigirPaginaAnterior() {
        return paginaAnterior;
    }

    public void cambiarIndice(PryPlataformas pryPlataforma, int celda) {
        if (permitirIndex == true) {
            pryPlataformaSeleccionada = pryPlataforma;
            cualCelda = celda;
            pryPlataformaSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    pryPlataformaSeleccionada.getCodigo();
                } else {
                    pryPlataformaSeleccionada.getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = pryPlataformaSeleccionada.getDescripcion();
                } else {
                    backUpDescripcion = pryPlataformaSeleccionada.getDescripcion();
                }
            }

        }
        System.out.println("Indice: " + pryPlataformaSeleccionada + " Celda: " + cualCelda);
    }

    public void asignarIndex(PryPlataformas pryPlataforma, int LND, int dig) {
        try {
            pryPlataformaSeleccionada = pryPlataforma;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlPryPlataformas.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            observacion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:observacion");
            observacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
            bandera = 0;
            filtrarPryPlataformas = null;
            tipoLista = 0;
        }

        borrarPryPlataformas.clear();
        crearPryPlataformas.clear();
        modificarPryPlataformas.clear();
        k = 0;
        listPryPlataformas = null;
        pryPlataformaSeleccionada = null;
        guardado = true;
        permitirIndex = true;
        getListPryPlataformas();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        context.update("form:infoRegistro");
        context.update("form:datosPrtPlataforma");
        context.update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            observacion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:observacion");
            observacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
            bandera = 0;
            filtrarPryPlataformas = null;
            tipoLista = 0;
        }

        borrarPryPlataformas.clear();
        crearPryPlataformas.clear();
        modificarPryPlataformas.clear();
        pryPlataformaSeleccionada = null;
        k = 0;
        infoRegistro = null;
        guardado = true;
        permitirIndex = true;
        getListPryPlataformas();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        context.update("form:infoRegistro");
        context.update("form:datosPrtPlataforma");
        context.update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:codigo");
            codigo.setFilterStyle("width: 85%");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:descripcion");
            descripcion.setFilterStyle("width: 85%");
            observacion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:observacion");
            observacion.setFilterStyle("width: 85%");
            RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            observacion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:observacion");
            observacion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
            bandera = 0;
            filtrarPryPlataformas = null;
            tipoLista = 0;
        }
    }

    public void modificarEvalPlataforma(PryPlataformas pryPlataforma, String confirmarCambio, String valorConfirmar) {
        pryPlataformaSeleccionada = pryPlataforma;
        int contador = 0;
        int codigoVacio = 0;
        boolean coincidencias = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {

            if (pryPlataformaSeleccionada.getCodigo() == null || pryPlataformaSeleccionada.getCodigo().equals(codigoVacio)) {
                mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                coincidencias = false;
                pryPlataformaSeleccionada.setCodigo(backUpCodigo);
            } else {

                for (int j = 0; j < listPryPlataformas.size(); j++) {
                    if (pryPlataformaSeleccionada.getSecuencia() != listPryPlataformas.get(j).getSecuencia()) {
                        if (pryPlataformaSeleccionada.getCodigo().equals(listPryPlataformas.get(j).getCodigo())) {
                            contador++;
                        }
                    }
                }
                if (contador > 0) {
                    mensajeValidacion = "CODIGOS REPETIDOS";
                    coincidencias = false;
                    pryPlataformaSeleccionada.setCodigo(backUpCodigo);
                } else {
                    coincidencias = true;
                }
            }
        }

        if (confirmarCambio.equalsIgnoreCase("M")) {
            if (pryPlataformaSeleccionada.getDescripcion() == null || pryPlataformaSeleccionada.getDescripcion().isEmpty()) {
                mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                coincidencias = false;
                pryPlataformaSeleccionada.setDescripcion(backUpDescripcion);
            } else {

                for (int j = 0; j < listPryPlataformas.size(); j++) {
                    if (pryPlataformaSeleccionada.getSecuencia() != listPryPlataformas.get(j).getSecuencia()) {
                        if (pryPlataformaSeleccionada.getDescripcion().equals(listPryPlataformas.get(j).getDescripcion())) {
                            contador++;
                        }
                    }
                }
                if (contador > 0) {
                    mensajeValidacion = "DESCRIPCION REPETIDA";
                    coincidencias = false;
                    pryPlataformaSeleccionada.setCodigo(backUpCodigo);
                } else {
                    coincidencias = true;
                }
            }

        }

        if (confirmarCambio.equalsIgnoreCase("O")) {

            if (pryPlataformaSeleccionada.getObservacion() == null || pryPlataformaSeleccionada.getObservacion().isEmpty()) {
                mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                coincidencias = false;
            }
            for (int j = 0; j < listPryPlataformas.size(); j++) {
                if (pryPlataformaSeleccionada.getSecuencia() != listPryPlataformas.get(j).getSecuencia()) {
                    if (pryPlataformaSeleccionada.getObservacion().equals(listPryPlataformas.get(j).getObservacion())) {
                        contador++;
                    }
                }
            }
            if (contador > 0) {
                mensajeValidacion = "OBSERVACION REPETIDA";
                coincidencias = false;
            } else {
                coincidencias = true;
            }
        }

        if (coincidencias == true) {
            if (!crearPryPlataformas.contains(pryPlataformaSeleccionada)) {
                if (!modificarPryPlataformas.contains(pryPlataformaSeleccionada)) {
                    modificarPryPlataformas.add(pryPlataformaSeleccionada);
                }
            }

            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        } else {
            context.update("form:validacionModificar");
            context.execute("validacionModificar.show()");
        }

        context.update("form:datosPrtPlataforma");
        context.update("form:ACEPTAR");
    }

    public void borrandoPryPlataformas() {

        if (pryPlataformaSeleccionada != null) {
                System.out.println("Entro a borrandoPryPlataformas");
                if (!modificarPryPlataformas.isEmpty() && modificarPryPlataformas.contains(pryPlataformaSeleccionada)) {
                    int modIndex = modificarPryPlataformas.indexOf(pryPlataformaSeleccionada);
                    modificarPryPlataformas.remove(modIndex);
                    borrarPryPlataformas.add(pryPlataformaSeleccionada);
                } else if (!crearPryPlataformas.isEmpty() && crearPryPlataformas.contains(pryPlataformaSeleccionada)) {
                    int crearIndex = crearPryPlataformas.indexOf(pryPlataformaSeleccionada);
                    crearPryPlataformas.remove(crearIndex);
                } else {
                    borrarPryPlataformas.add(pryPlataformaSeleccionada);
                }
                listPryPlataformas.remove(pryPlataformaSeleccionada);
            if (tipoLista == 1) {
                filtrarPryPlataformas.remove(pryPlataformaSeleccionada);
                listPryPlataformas.remove(pryPlataformaSeleccionada);
            }
            modificarInfoRegistro(listPryPlataformas.size());
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:datosPryCliente");
            context.update("form:datosPrtPlataforma");
            pryPlataformaSeleccionada = null;

            if (guardado == true) {
                guardado = false;
            }
            context.update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("seleccionarRegistro.show()");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");

        BigInteger proyectos;
        try {
            System.err.println("Control Secuencia de ControlEvalCompetencias ");
            if (tipoLista == 0) {
                proyectos = administrarPryPlataformas.contarProyectosPryPlataformas(pryPlataformaSeleccionada.getSecuencia());
            } else {
                proyectos = administrarPryPlataformas.contarProyectosPryPlataformas(pryPlataformaSeleccionada.getSecuencia());
            }
            if (proyectos.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoPryPlataformas();
            } else {
                System.out.println("Borrado>0");
                RequestContext context = RequestContext.getCurrentInstance();
                context.update("form:validacionBorrar");
                context.execute("validacionBorrar.show()");
                proyectos = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlPryPlataformas verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarPryPlataformas.isEmpty() || !crearPryPlataformas.isEmpty() || !modificarPryPlataformas.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:confirmarGuardar");
            context.execute("confirmarGuardar.show()");
        }

    }

    public void guardarPryPlataformas() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarPryPlataformas");
            if (!borrarPryPlataformas.isEmpty()) {
                administrarPryPlataformas.borrarPryPlataformas(borrarPryPlataformas);

                //mostrarBorrados
                registrosBorrados = borrarPryPlataformas.size();
                context.update("form:mostrarBorrados");
                context.execute("mostrarBorrados.show()");
                borrarPryPlataformas.clear();
            }
            if (!crearPryPlataformas.isEmpty()) {
                administrarPryPlataformas.crearPryPlataformas(crearPryPlataformas);
                crearPryPlataformas.clear();
            }
            if (!modificarPryPlataformas.isEmpty()) {
                administrarPryPlataformas.modificarPryPlataformas(modificarPryPlataformas);
                modificarPryPlataformas.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listPryPlataformas = null;
            context.update("form:datosPrtPlataforma");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            contarRegistros();
            context.update("form:growl");
            context.update("form:datosPryCliente");
            k = 0;
            guardado = true;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (pryPlataformaSeleccionada != null) {
            if (tipoLista == 0) {
                editarPryPlataforma = pryPlataformaSeleccionada;
            }
            if (tipoLista == 1) {
                editarPryPlataforma = pryPlataformaSeleccionada;
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
            } else if (cualCelda == 2) {
                context.update("formularioDialogos:editObservacion");
                context.execute("editObservacion.show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("seleccionarRegistro.show()");
        }
    }

    public void agregarNuevoPryPlataformas() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoPryPlataforma.getCodigo() == a) {
            mensajeValidacion = " Existen campos vacíos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {

            for (int x = 0; x < listPryPlataformas.size(); x++) {
                if (listPryPlataformas.get(x).getCodigo() == nuevoPryPlataforma.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *No pueden haber códigos repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoPryPlataforma.getDescripcion() == (null)) {
            mensajeValidacion = mensajeValidacion + " Existen campos vacíos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }


        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:observacion");
                observacion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
                bandera = 0;
                filtrarPryPlataformas = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoPryPlataforma.setSecuencia(l);
            crearPryPlataformas.add(nuevoPryPlataforma);
            listPryPlataformas.add(nuevoPryPlataforma);
            pryPlataformaSeleccionada = nuevoPryPlataforma;
            nuevoPryPlataforma = new PryPlataformas();
            modificarInfoRegistro(listPryPlataformas.size());
            context.update("form:datosPrtPlataforma");
            context.update("form:infoRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            context.execute("nuevoRegistroPryPlataforma.hide()");

        } else {
            context.update("form:validacionNuevoPrPlat");
            context.execute("validacionNuevoPrPlat.show()");
            contador = 0;
        }
    }

    public void limpiarNuevoPryPlataformas() {
        System.out.println("limpiarNuevoPryPlataformas");
        nuevoPryPlataforma = new PryPlataformas();
        pryPlataformaSeleccionada = null;

    }

    //------------------------------------------------------------------------------
    public void duplicandoPryPlataformas() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("duplicandoPryPlataformas");
        if (pryPlataformaSeleccionada != null) {
            duplicarPryPlataforma = new PryPlataformas();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarPryPlataforma.setSecuencia(l);
                duplicarPryPlataforma.setCodigo(pryPlataformaSeleccionada.getCodigo());
                duplicarPryPlataforma.setDescripcion(pryPlataformaSeleccionada.getDescripcion());
                duplicarPryPlataforma.setObservacion(pryPlataformaSeleccionada.getObservacion());
            }
            if (tipoLista == 1) {
                duplicarPryPlataforma.setSecuencia(l);
                duplicarPryPlataforma.setCodigo(pryPlataformaSeleccionada.getCodigo());
                duplicarPryPlataforma.setDescripcion(pryPlataformaSeleccionada.getDescripcion());
                duplicarPryPlataforma.setObservacion(pryPlataformaSeleccionada.getObservacion());
            }

            context.update("formularioDialogos:duplicarPPL");
            context.execute("duplicarRegistroPryPlataforma.show()");
        } else {
            context.execute("seleccionarRegistro.show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarPryPlataforma.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarPryPlataforma.getDescripcion());

        if (duplicarPryPlataforma.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "Existen campos vacíos\n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listPryPlataformas.size(); x++) {
                if (listPryPlataformas.get(x).getCodigo() == duplicarPryPlataforma.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " Código repetido \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarPryPlataforma.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " Existen campos vacíos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarPryPlataforma.getSecuencia() + "  " + duplicarPryPlataforma.getCodigo());
            if (crearPryPlataformas.contains(duplicarPryPlataforma)) {
                System.out.println("Ya lo contengo.");
            }
            listPryPlataformas.add(duplicarPryPlataforma);
            crearPryPlataformas.add(duplicarPryPlataforma);
            modificarInfoRegistro(listPryPlataformas.size());
            context.update("form:datosPrtPlataforma");
            pryPlataformaSeleccionada = duplicarPryPlataforma;
            if (guardado == true) {
                guardado = false;
            }
            context.update("form:ACEPTAR");
            context.update("form:infoRegistro");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                observacion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:observacion");
                observacion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
                bandera = 0;
                filtrarPryPlataformas = null;
                tipoLista = 0;
            }
            duplicarPryPlataforma = new PryPlataformas();
            RequestContext.getCurrentInstance().execute("duplicarRegistroPryPlataforma.hide()");

        } else {
            contador = 0;
            context.update("form:validacionDuplicarPrPlat");
            context.execute("validacionDuplicarPrPlat.show()");
        }
    }

    public void limpiarDuplicarPryPlataformas() {
        duplicarPryPlataforma = new PryPlataformas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPrtPlataformaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "PRY_PLATAFORMAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        pryPlataformaSeleccionada = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPrtPlataformaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "PRY_PLATAFORMAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        pryPlataformaSeleccionada = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (pryPlataformaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(pryPlataformaSeleccionada.getSecuencia(), "PRY_PLATAFORMAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("PRY_PLATAFORMAS")) { // igual acá
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }
        }
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlPryPlataformas.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            modificarInfoRegistro(filtrarPryPlataformas.size());
            context.update("form:infoRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlPryPlataformas eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void contarRegistros() {
        if (listPryPlataformas != null) {
            modificarInfoRegistro(listPryPlataformas.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void recordarSeleccionPryPlataforma() {
        if (pryPlataformaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosPrtPlataforma");
            tablaC.setSelection(pryPlataformaSeleccionada);
        }
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<PryPlataformas> getListPryPlataformas() {
        if (listPryPlataformas == null) {
            listPryPlataformas = administrarPryPlataformas.mostrarPryPlataformas();
        }
        return listPryPlataformas;
    }

    public void setListPryPlataformas(List<PryPlataformas> listPryPlataformas) {
        this.listPryPlataformas = listPryPlataformas;
    }

    public List<PryPlataformas> getFiltrarPryPlataformas() {
        return filtrarPryPlataformas;
    }

    public void setFiltrarPryPlataformas(List<PryPlataformas> filtrarPryPlataformas) {
        this.filtrarPryPlataformas = filtrarPryPlataformas;
    }

    public PryPlataformas getNuevoPryPlataforma() {
        return nuevoPryPlataforma;
    }

    public void setNuevoPryPlataforma(PryPlataformas nuevoPryPlataforma) {
        this.nuevoPryPlataforma = nuevoPryPlataforma;
    }

    public PryPlataformas getDuplicarPryPlataforma() {
        return duplicarPryPlataforma;
    }

    public void setDuplicarPryPlataforma(PryPlataformas duplicarPryPlataforma) {
        this.duplicarPryPlataforma = duplicarPryPlataforma;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }

    public PryPlataformas getEditarPryPlataforma() {
        return editarPryPlataforma;
    }

    public void setEditarPryPlataforma(PryPlataformas editarPryPlataforma) {
        this.editarPryPlataforma = editarPryPlataforma;
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

    public PryPlataformas getPryPlataformaSeleccionada() {
        return pryPlataformaSeleccionada;
    }

    public void setPryPlataformaSeleccionada(PryPlataformas pryPlataformaSeleccionada) {
        this.pryPlataformaSeleccionada = pryPlataformaSeleccionada;
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

}
