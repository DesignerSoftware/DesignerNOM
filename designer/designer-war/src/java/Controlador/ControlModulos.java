/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Modulos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarModulosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

/**
 *
 * @author user
 */
@Named(value = "controlModulos")
@SessionScoped
public class ControlModulos implements Serializable {

    private static Logger log = Logger.getLogger(ControlModulos.class);

    @EJB
    AdministrarModulosInterface administrarModulos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Modulos> listModulos;
    private List<Modulos> filtrarModulos;
    private List<Modulos> listaModulosCrear;
    private List<Modulos> listaModulosModificar;
    private List<Modulos> listaModulosBorrar;
    private Modulos nuevoModulo;
    private Modulos duplicarModulo;
    private Modulos editarModulo;
    private Modulos moduloSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion;
    private String altoTabla;
    //borrado
    private String infoRegistro;
    private String mensajeValidacion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String msgError;

    public ControlModulos() {
        listModulos = null;
        listaModulosCrear = new ArrayList<Modulos>();
        listaModulosModificar = new ArrayList<Modulos>();
        listaModulosBorrar = new ArrayList<Modulos>();
        editarModulo = new Modulos();
        nuevoModulo = new Modulos();
        duplicarModulo = new Modulos();
        guardado = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        altoTabla = "335";
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "modulo";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
        }
        limpiarListasValor();
    }

    public void limpiarListasValor() {

    }

    @PreDestroy
    public void destruyendose() {
        log.info(this.getClass().getName() + ".destruyendose() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarModulos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listModulos = null;
            getListModulos();
            if (listModulos != null) {
                moduloSeleccionado = listModulos.get(0);
            }

        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(Modulos modulo, int celda) {

        moduloSeleccionado = modulo;
        cualCelda = celda;
        moduloSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            moduloSeleccionado.getNombre();
        } else if (cualCelda == 1) {
            moduloSeleccionado.getNombrecorto();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosModulos:nombre");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosModulos:nombrecorto");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosModulos");
            bandera = 0;
            filtrarModulos = null;
            tipoLista = 0;
            altoTabla = "335";
        }

        listaModulosBorrar.clear();
        listaModulosCrear.clear();
        listaModulosModificar.clear();
        k = 0;
        listModulos = null;
        guardado = true;
        moduloSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosModulos");
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            altoTabla = "315";
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosModulos:nombre");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosModulos:nombrecorto");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosModulos");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "335";
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosModulos:nombre");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosModulos:nombrecorto");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosModulos");
            bandera = 0;
            filtrarModulos = null;
            tipoLista = 0;
        }
    }

    public void modificandoModulo(Modulos modulo) {
        moduloSeleccionado = modulo;
        if (!listaModulosCrear.contains(moduloSeleccionado)) {
            if (listaModulosModificar.isEmpty()) {
                listaModulosModificar.add(moduloSeleccionado);
            } else if (!listaModulosModificar.contains(moduloSeleccionado)) {
                listaModulosModificar.add(moduloSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosModulos");
    }

    public void borrandoModulo() {
        if (moduloSeleccionado != null) {
            if (!listaModulosModificar.isEmpty() && listaModulosModificar.contains(moduloSeleccionado)) {
                int modIndex = listaModulosModificar.indexOf(moduloSeleccionado);
                listaModulosModificar.remove(modIndex);
                listaModulosBorrar.add(moduloSeleccionado);
            } else if (!listaModulosCrear.isEmpty() && listaModulosCrear.contains(moduloSeleccionado)) {
                int crearIndex = listaModulosCrear.indexOf(moduloSeleccionado);
                listaModulosCrear.remove(crearIndex);
            } else {
                listaModulosBorrar.add(moduloSeleccionado);
            }
            listModulos.remove(moduloSeleccionado);
            if (tipoLista == 1) {
                filtrarModulos.remove(moduloSeleccionado);
            }
            moduloSeleccionado = null;
            RequestContext context = RequestContext.getCurrentInstance();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosModulos");
        }

    }

    public void revisarDialogoGuardar() {
        if (!listaModulosBorrar.isEmpty() || !listaModulosCrear.isEmpty() || !listaModulosModificar.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarModulos() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!listaModulosBorrar.isEmpty()) {
                    for (int i = 0; i < listaModulosBorrar.size(); i++) {
                        msgError = administrarModulos.borrarModulos(listaModulosBorrar.get(i));
                    }
                    listaModulosBorrar.clear();
                }
                if (!listaModulosCrear.isEmpty()) {
                    for (int i = 0; i < listaModulosCrear.size(); i++) {
                        msgError = administrarModulos.crearModulos(listaModulosCrear.get(i));
                    }
                    listaModulosCrear.clear();
                }
                if (!listaModulosModificar.isEmpty()) {
                    for (int i = 0; i < listaModulosModificar.size(); i++) {
                        msgError = administrarModulos.modificarModulos(listaModulosModificar.get(i));
                    }
                }
                listaModulosModificar.clear();

                if (msgError.equals("EXITO")) {
                    listModulos = null;
                    getListModulos();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    moduloSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:datosModulos");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardadoDB");
                    RequestContext.getCurrentInstance().execute("PF('errorGuardadoDB').show()");
                }

            }
        } catch (Exception e) {
            log.warn("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void editarCelda() {
        if (moduloSeleccionado != null) {
            editarModulo = moduloSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
                RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNombreCorto");
                RequestContext.getCurrentInstance().execute("PF('editNombreCorto').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoModulo() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";

        if (nuevoModulo.getNombre() == (null) || nuevoModulo.getNombre().isEmpty()) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            for (int i = 0; i < listModulos.size(); i++) {
                if (listModulos.get(i).getNombre().equals(nuevoModulo.getNombre()) || listModulos.get(i).getNombrecorto().equals(nuevoModulo.getNombrecorto())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El Nombre ó el Nombre Corto ingresado ya está registrado. Por favor ingrese un valor válido";
            } else {
                contador++;
            }
        }
        if (nuevoModulo.getNombrecorto() == (null) || nuevoModulo.getNombrecorto().isEmpty()) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios\n";
        } else {
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosModulos:nombre");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosModulos:nombrecorto");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosModulos");
                bandera = 0;
                filtrarModulos = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoModulo.setSecuencia(l);
            listaModulosCrear.add(nuevoModulo);
            listModulos.add(0, nuevoModulo);
            moduloSeleccionado = nuevoModulo;
            RequestContext.getCurrentInstance().update("form:datosModulos");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroModulo').hide()");
            nuevoModulo = new Modulos();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoModulo() {
        nuevoModulo = new Modulos();
    }

    public void duplicandoModulo() {
        if (moduloSeleccionado != null) {
            duplicarModulo = new Modulos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarModulo.setSecuencia(l);
            duplicarModulo.setNombre(moduloSeleccionado.getNombre());
            duplicarModulo.setNombrecorto(moduloSeleccionado.getNombrecorto());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTR");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroModulos').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;

        if (duplicarModulo.getNombre() == (null) || duplicarModulo.getNombre().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int i = 0; i < listModulos.size(); i++) {
                if (listModulos.get(i).getNombre().equals(duplicarModulo.getNombre()) || listModulos.get(i).getNombrecorto().equals(duplicarModulo.getNombrecorto())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El Nombre ó el Nombre Corto ingresado ya está registrado. Por favor ingrese un valor válido";
            } else {
                contador++;
            }
        }
        if (duplicarModulo.getNombrecorto() == (null) || duplicarModulo.getNombrecorto().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 2) {
            listModulos.add(0, duplicarModulo);
            listaModulosCrear.add(duplicarModulo);
            moduloSeleccionado = duplicarModulo;
            RequestContext.getCurrentInstance().update("form:datosModulos");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosModulos:nombre");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosModulos:nombrecorto");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosModulos");
                bandera = 0;
                filtrarModulos = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            duplicarModulo = new Modulos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroModulos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarModulo() {
        duplicarModulo = new Modulos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosModulosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "Modulos", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosModulosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "Modulos", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (moduloSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(moduloSeleccionado.getSecuencia(), "MODULOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MODULOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosModulos:nombre");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosModulos:nombrecorto");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosModulos");
            bandera = 0;
            filtrarModulos = null;
            tipoLista = 0;
            altoTabla = "335";
        }
        listaModulosBorrar.clear();
        listaModulosCrear.clear();
        listaModulosModificar.clear();
        moduloSeleccionado = null;
        k = 0;
        listModulos = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosModulos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    ///////////GETS Y SETS////////////
    public List<Modulos> getListModulos() {
        if (listModulos == null) {
            listModulos = administrarModulos.consultarModulos();
        }
        return listModulos;
    }

    public void setListModulos(List<Modulos> listModulos) {
        this.listModulos = listModulos;
    }

    public List<Modulos> getFiltrarModulos() {
        return filtrarModulos;
    }

    public void setFiltrarModulos(List<Modulos> filtrarModulos) {
        this.filtrarModulos = filtrarModulos;
    }

    public Modulos getNuevoModulo() {
        return nuevoModulo;
    }

    public void setNuevoModulo(Modulos nuevoModulo) {
        this.nuevoModulo = nuevoModulo;
    }

    public Modulos getDuplicarModulo() {
        return duplicarModulo;
    }

    public void setDuplicarModulo(Modulos duplicarModulo) {
        this.duplicarModulo = duplicarModulo;
    }

    public Modulos getEditarModulo() {
        return editarModulo;
    }

    public void setEditarModulo(Modulos editarModulo) {
        this.editarModulo = editarModulo;
    }

    public Modulos getModuloSeleccionado() {
        return moduloSeleccionado;
    }

    public void setModuloSeleccionado(Modulos moduloSeleccionado) {
        this.moduloSeleccionado = moduloSeleccionado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosModulos");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

}
