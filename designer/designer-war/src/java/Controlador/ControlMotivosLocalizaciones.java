/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import InterfaceAdministrar.AdministrarMotivosLocalizacionesInterface;
import Entidades.MotivosLocalizaciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
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
public class ControlMotivosLocalizaciones implements Serializable {

    @EJB
    AdministrarMotivosLocalizacionesInterface administrarMotivosLocalizaciones;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<MotivosLocalizaciones> listMotivosLocalizaciones;
    private List<MotivosLocalizaciones> filtrarMotivosLocalizaciones;
    private List<MotivosLocalizaciones> crearMotivosLocalizaciones;
    private List<MotivosLocalizaciones> modificarMotivoContrato;
    private List<MotivosLocalizaciones> borrarMotivoContrato;
    private MotivosLocalizaciones nuevoMotivoContrato;
    private MotivosLocalizaciones duplicarMotivoContrato;
    private MotivosLocalizaciones editarMotivoContrato;
    private MotivosLocalizaciones motivoLocalizacionSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera, resultado;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger contarVigenciasLocalizacionesMotivoLocalizacion;
    private int tamano;

    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String infoRegistro, paginaanterior;
    private DataTable tablaC;
    private boolean activarLov;

    public ControlMotivosLocalizaciones() {

        listMotivosLocalizaciones = null;
        crearMotivosLocalizaciones = new ArrayList<MotivosLocalizaciones>();
        modificarMotivoContrato = new ArrayList<MotivosLocalizaciones>();
        borrarMotivoContrato = new ArrayList<MotivosLocalizaciones>();
        permitirIndex = true;
        editarMotivoContrato = new MotivosLocalizaciones();
        nuevoMotivoContrato = new MotivosLocalizaciones();
        duplicarMotivoContrato = new MotivosLocalizaciones();
        guardado = true;
        tamano = 270;
        paginaanterior = "";
        activarLov = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMotivosLocalizaciones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPag(String pagina) {
        paginaanterior = pagina;
        listMotivosLocalizaciones = null;
        getListMotivosLocalizaciones();
        contarRegistros();
        if (listMotivosLocalizaciones == null || listMotivosLocalizaciones.isEmpty()) {
            motivoLocalizacionSeleccionado = null;
        } else {
            motivoLocalizacionSeleccionado = listMotivosLocalizaciones.get(0);
        }
    }

    public String retornarPagina() {
        return paginaanterior;
    }

    public void cambiarIndice(MotivosLocalizaciones motivoLocalizacion, int celda) {
        motivoLocalizacionSeleccionado = motivoLocalizacion;
        if (permitirIndex == true) {
            cualCelda = celda;
            motivoLocalizacionSeleccionado.getSecuencia();
            if (cualCelda == 0) {
                backUpCodigo = motivoLocalizacionSeleccionado.getCodigo();
            }
            if (cualCelda == 1) {
                backUpDescripcion = motivoLocalizacionSeleccionado.getDescripcion();
            }
        }
    }

    public void asignarIndex(MotivosLocalizaciones motivoLocalizacion, int LND, int dig) {
        try {
            motivoLocalizacionSeleccionado = motivoLocalizacion;
            RequestContext context = RequestContext.getCurrentInstance();
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlMotiviosCambiosCargos.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            bandera = 0;
            filtrarMotivosLocalizaciones = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarMotivoContrato.clear();
        crearMotivosLocalizaciones.clear();
        modificarMotivoContrato.clear();
        k = 0;
        listMotivosLocalizaciones = null;
        motivoLocalizacionSeleccionado = null;
        getListMotivosLocalizaciones();
        contarRegistros();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosMotivoContrato");
        PrimefacesContextUI.actualizar("form:infoRegistro");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            bandera = 0;
            filtrarMotivosLocalizaciones = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarMotivoContrato.clear();
        crearMotivosLocalizaciones.clear();
        modificarMotivoContrato.clear();
        motivoLocalizacionSeleccionado = null;
        k = 0;
        listMotivosLocalizaciones = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        PrimefacesContextUI.actualizar("form:datosMotivoContrato");
        PrimefacesContextUI.actualizar("form:infoRegistro");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("width: 85%");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("width: 85%");
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            bandera = 0;
            filtrarMotivosLocalizaciones = null;
            tipoLista = 0;
        }
        PrimefacesContextUI.actualizar("form:datosMotivoContrato");
    }

    public void modificarMotivosContrato(MotivosLocalizaciones motivoLocalizacion, String confirmarCambio, String valorConfirmar) {
        motivoLocalizacionSeleccionado = motivoLocalizacion;
        int contador = 0;
        Short codigoVacio = new Short("0");
        boolean coincidencias = false;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {

            if (motivoLocalizacionSeleccionado.getCodigo() == null || motivoLocalizacionSeleccionado.getCodigo().equals(codigoVacio)) {
                mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                coincidencias = false;
                motivoLocalizacionSeleccionado.setCodigo(backUpCodigo);
            } else {

                for (int j = 0; j < listMotivosLocalizaciones.size(); j++) {
                    if (motivoLocalizacionSeleccionado.getSecuencia() != listMotivosLocalizaciones.get(j).getSecuencia()) {
                        if (motivoLocalizacionSeleccionado.getCodigo().equals(listMotivosLocalizaciones.get(j).getCodigo())) {
                            contador++;
                        }
                    }
                }
                if (contador > 0) {
                    mensajeValidacion = "CODIGOS REPETIDOS";
                    coincidencias = false;
                    motivoLocalizacionSeleccionado.setCodigo(backUpCodigo);
                } else {
                    coincidencias = true;
                }
            }
        }

        if (confirmarCambio.equalsIgnoreCase("M")) {

            if (motivoLocalizacionSeleccionado.getDescripcion() == null || motivoLocalizacionSeleccionado.getDescripcion().isEmpty()) {
                mensajeValidacion = "NO PUEDE HABER  NINGUN CAMPO VACIO";
                motivoLocalizacionSeleccionado.setDescripcion(backUpDescripcion);
                coincidencias = false;
            }
            for (int j = 0; j < listMotivosLocalizaciones.size(); j++) {
                if (motivoLocalizacionSeleccionado.getSecuencia() != listMotivosLocalizaciones.get(j).getSecuencia()) {
                    if (motivoLocalizacionSeleccionado.getDescripcion().equals(listMotivosLocalizaciones.get(j).getDescripcion())) {
                        contador++;
                    }
                }
            }
            if (contador > 0) {
                mensajeValidacion = "MOTIVO REPETIDOS";
                coincidencias = false;
                motivoLocalizacionSeleccionado.setCodigo(backUpCodigo);
            } else {
                coincidencias = true;
            }
        }

        if (coincidencias == true) {
            if (!crearMotivosLocalizaciones.contains(motivoLocalizacionSeleccionado)) {
                if (!modificarMotivoContrato.contains(motivoLocalizacionSeleccionado)) {
                    modificarMotivoContrato.add(motivoLocalizacionSeleccionado);
                }
            }

            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
        } else {
            PrimefacesContextUI.actualizar("form:validacionModificar");
            PrimefacesContextUI.ejecutar("PF('validacionModificar').show()");
        }

        PrimefacesContextUI.actualizar("form:datosMotivoCambioCargo");

    }

    public void borrarMotivosLocalizaciones() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (motivoLocalizacionSeleccionado != null) {
            if (!modificarMotivoContrato.isEmpty() && modificarMotivoContrato.contains(motivoLocalizacionSeleccionado)) {
                modificarMotivoContrato.remove(modificarMotivoContrato.indexOf(motivoLocalizacionSeleccionado));
                borrarMotivoContrato.add(motivoLocalizacionSeleccionado);
            } else if (!crearMotivosLocalizaciones.isEmpty() && crearMotivosLocalizaciones.contains(motivoLocalizacionSeleccionado)) {
                crearMotivosLocalizaciones.remove(crearMotivosLocalizaciones.indexOf(motivoLocalizacionSeleccionado));
            } else {
                borrarMotivoContrato.add(motivoLocalizacionSeleccionado);
            }
            listMotivosLocalizaciones.remove(motivoLocalizacionSeleccionado);

            if (tipoLista == 1) {
                filtrarMotivosLocalizaciones.remove(motivoLocalizacionSeleccionado);
                listMotivosLocalizaciones.remove(motivoLocalizacionSeleccionado);
            }
            modificarInfoRegistro(listMotivosLocalizaciones.size());
            PrimefacesContextUI.actualizar("form:infoRegistro");
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            motivoLocalizacionSeleccionado = null;

            if (guardado == true) {
                guardado = false;
            }
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        try {
            if (tipoLista == 0) {
                contarVigenciasLocalizacionesMotivoLocalizacion = administrarMotivosLocalizaciones.contarVigenciasLocalizacionesMotivoLocalizacion(motivoLocalizacionSeleccionado.getSecuencia());
            } else {
                contarVigenciasLocalizacionesMotivoLocalizacion = administrarMotivosLocalizaciones.contarVigenciasLocalizacionesMotivoLocalizacion(motivoLocalizacionSeleccionado.getSecuencia());
            }
            if (contarVigenciasLocalizacionesMotivoLocalizacion.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrarMotivosLocalizaciones();
            } else {
                System.out.println("Borrado>0");
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:validacionBorrar");
                PrimefacesContextUI.ejecutar("PF('validacionBorrar').show()");
                contarVigenciasLocalizacionesMotivoLocalizacion = new BigInteger("-1");
            }

        } catch (Exception e) {
            System.err.println("ERROR ControlMotivosLocalizaciones verificarBorrado ERROR " + e);
        }
    }

    public void guardarMotivosLocalizaciones() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            if (!borrarMotivoContrato.isEmpty()) {
                administrarMotivosLocalizaciones.borrarMotivosLocalizaciones(borrarMotivoContrato);
                registrosBorrados = borrarMotivoContrato.size();
                PrimefacesContextUI.actualizar("form:mostrarBorrados");
                PrimefacesContextUI.ejecutar("PF('mostrarBorrados').show()");
                borrarMotivoContrato.clear();
            }
            if (!crearMotivosLocalizaciones.isEmpty()) {
                administrarMotivosLocalizaciones.crearMotivosLocalizaciones(crearMotivosLocalizaciones);
            }
            crearMotivosLocalizaciones.clear();

            if (!modificarMotivoContrato.isEmpty()) {
                administrarMotivosLocalizaciones.modificarMotivosLocalizaciones(modificarMotivoContrato);
                modificarMotivoContrato.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listMotivosLocalizaciones = null;
            contarRegistros();
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
            k = 0;
            guardado = true;
        }
        RequestContext.getCurrentInstance()
                .update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (motivoLocalizacionSeleccionado != null) {
            if (tipoLista == 0) {
                editarMotivoContrato = motivoLocalizacionSeleccionado;
            }
            if (tipoLista == 1) {
                editarMotivoContrato = motivoLocalizacionSeleccionado;
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

        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoMotivoContrato() {
        System.out.println("Agregar Motivo Contrato");
        int contador = 0;
        int duplicados = 0;
        Integer a = null;
        //a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoMotivoContrato.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoMotivoContrato.getCodigo());

            for (int x = 0; x < listMotivosLocalizaciones.size(); x++) {
                if (listMotivosLocalizaciones.get(x).getCodigo() == nuevoMotivoContrato.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoMotivoContrato.getDescripcion() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoMotivoContrato.getDescripcion().isEmpty()) {
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosMotivoContrato");
                bandera = 0;
                filtrarMotivosLocalizaciones = null;
                tipoLista = 0;
                tamano = 270;
            }
            System.out.println("Despues de la bandera");

            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoContrato.setSecuencia(l);
            crearMotivosLocalizaciones.add(nuevoMotivoContrato);
            listMotivosLocalizaciones.add(nuevoMotivoContrato);
            motivoLocalizacionSeleccionado = nuevoMotivoContrato;
            nuevoMotivoContrato = new MotivosLocalizaciones();
            modificarInfoRegistro(listMotivosLocalizaciones.size());
            PrimefacesContextUI.actualizar("form:infoRegistro");
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            PrimefacesContextUI.ejecutar("PF('nuevoRegistroMotivosLocalizaciones').hide()");
        } else {
            PrimefacesContextUI.actualizar("form:validacionNuevaCentroCosto");
            PrimefacesContextUI.ejecutar("PF('validacionNuevaCentroCosto').show()");

            contador = 0;
        }
    }

    public void limpiarNuevoMotivosLocalizaciones() {
        System.out.println("limpiarnuevoMotivoContrato");
        nuevoMotivoContrato = new MotivosLocalizaciones();
        motivoLocalizacionSeleccionado = null;

    }

    //------------------------------------------------------------------------------
    public void duplicarMotivosLocalizaciones() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (motivoLocalizacionSeleccionado != null) {
            duplicarMotivoContrato = new MotivosLocalizaciones();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivoContrato.setSecuencia(l);
                duplicarMotivoContrato.setCodigo(motivoLocalizacionSeleccionado.getCodigo());
                duplicarMotivoContrato.setDescripcion(motivoLocalizacionSeleccionado.getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarMotivoContrato.setSecuencia(l);
                duplicarMotivoContrato.setCodigo(motivoLocalizacionSeleccionado.getCodigo());
                duplicarMotivoContrato.setDescripcion(motivoLocalizacionSeleccionado.getDescripcion());
            }
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarMotivosCambiosCargos");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroMotivosLocalizaciones').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarMotivoContrato.getCodigo());
        System.err.println("ConfirmarDuplicar nombre " + duplicarMotivoContrato.getDescripcion());

        if (duplicarMotivoContrato.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "Existen campos vacíos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosLocalizaciones.size(); x++) {
                if (listMotivosLocalizaciones.get(x).getCodigo() == duplicarMotivoContrato.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " No puede haber códigos repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarMotivoContrato.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " Existen campos vacíos \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarMotivoContrato.getSecuencia() + "  " + duplicarMotivoContrato.getCodigo());
            if (crearMotivosLocalizaciones.contains(duplicarMotivoContrato)) {
            }
            listMotivosLocalizaciones.add(duplicarMotivoContrato);
            crearMotivosLocalizaciones.add(duplicarMotivoContrato);
            motivoLocalizacionSeleccionado = duplicarMotivoContrato;
            duplicarMotivoContrato = new MotivosLocalizaciones();
            modificarInfoRegistro(listMotivosLocalizaciones.size());
            PrimefacesContextUI.actualizar("form:infoRegistro");
            PrimefacesContextUI.actualizar("form:datosMotivoContrato");
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                tamano = 270;
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                PrimefacesContextUI.actualizar("form:datosMotivoContrato");
                bandera = 0;
                filtrarMotivosLocalizaciones = null;
                tipoLista = 0;
            }
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroMotivosLocalizaciones').hide()");

        } else {
            contador = 0;
            PrimefacesContextUI.actualizar("form:validacionDuplicarMotivoLocalizacion");
            PrimefacesContextUI.ejecutar("PF('validacionDuplicarMotivoLocalizacion').show()");
        }
    }

    public void recordarSeleccionMotivoLocalizacion() {
        if (motivoLocalizacionSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosMotivoContrato");
            tablaC.setSelection(motivoLocalizacionSeleccionado);
        }
    }

    //////GET'S Y SET'S
    public void limpiarduplicarMotivosLocalizaciones() {
        duplicarMotivoContrato = new MotivosLocalizaciones();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoContratoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosLocalizacionesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        motivoLocalizacionSeleccionado = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoContratoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosLocalizacionesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        motivoLocalizacionSeleccionado = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (motivoLocalizacionSeleccionado != null) {
            resultado = administrarRastros.obtenerTabla(motivoLocalizacionSeleccionado.getSecuencia(), "MOTIVOSLOCALIZACIONES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            if (administrarRastros.verificarHistoricosTabla("MOTIVOSLOCALIZACIONES")) { // igual acá
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
            modificarInfoRegistro(filtrarMotivosLocalizaciones.size());
            PrimefacesContextUI.actualizar("form:infoRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlMotiviosCambiosCargos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    public void contarRegistros() {
        if (listMotivosLocalizaciones != null) {
            modificarInfoRegistro(listMotivosLocalizaciones.size());
        } else {
            modificarInfoRegistro(0);
        }
    }
//-----------------------//---------------//----------------------//------------

    public List<MotivosLocalizaciones> getListMotivosLocalizaciones() {
        if (listMotivosLocalizaciones == null) {
            listMotivosLocalizaciones = administrarMotivosLocalizaciones.mostrarMotivosCambiosCargos();
        }
        return listMotivosLocalizaciones;
    }

    public void setListMotivosLocalizaciones(List<MotivosLocalizaciones> listMotivosLocalizaciones) {
        this.listMotivosLocalizaciones = listMotivosLocalizaciones;
    }

    public List<MotivosLocalizaciones> getFiltrarMotivosLocalizaciones() {
        return filtrarMotivosLocalizaciones;
    }

    public void setFiltrarMotivosLocalizaciones(List<MotivosLocalizaciones> filtrarMotivosLocalizaciones) {
        this.filtrarMotivosLocalizaciones = filtrarMotivosLocalizaciones;
    }

    public MotivosLocalizaciones getNuevoMotivoContrato() {
        return nuevoMotivoContrato;
    }

    public void setNuevoMotivoContrato(MotivosLocalizaciones nuevoMotivoContrato) {
        this.nuevoMotivoContrato = nuevoMotivoContrato;
    }

    public MotivosLocalizaciones getEditarMotivoContrato() {
        return editarMotivoContrato;
    }

    public void setEditarMotivoContrato(MotivosLocalizaciones editarMotivoContrato) {
        this.editarMotivoContrato = editarMotivoContrato;
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

    public MotivosLocalizaciones getDuplicarMotivoContrato() {
        return duplicarMotivoContrato;
    }

    public void setDuplicarMotivoContrato(MotivosLocalizaciones duplicarMotivoContrato) {
        this.duplicarMotivoContrato = duplicarMotivoContrato;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public MotivosLocalizaciones getMotivoLocalizacionSeleccionado() {
        return motivoLocalizacionSeleccionado;
    }

    public void setMotivoLocalizacionSeleccionado(MotivosLocalizaciones motivoLocalizacionSeleccionado) {
        this.motivoLocalizacionSeleccionado = motivoLocalizacionSeleccionado;
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

    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
