/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Enfermedades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEnfermedadesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
@ManagedBean
@SessionScoped
public class ControlEnfermedades implements Serializable {

    private static Logger log = Logger.getLogger(ControlEnfermedades.class);

    @EJB
    AdministrarEnfermedadesInterface administrarEnfermedades;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Enfermedades> listEnfermedades;
    private List<Enfermedades> filtrarEnfermedades;
    private List<Enfermedades> crearEnfermedades;
    private List<Enfermedades> modificarEnfermedades;
    private List<Enfermedades> borrarEnfermedades;
    private Enfermedades nuevaEnfermedad;
    private Enfermedades duplicarEnfermedad;
    private Enfermedades editarEnfermedad;
    private Enfermedades enfermedadSeleccionada;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion;
    private int registrosBorrados;
    private String mensajeValidacion;
    private Integer a;
    private int tamano;
    private String infoRegistro;
    private String msgError;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private BigInteger contadorAusentimos;
    private BigInteger contadorDetallesLicencias;
    private BigInteger contadorEnfermedadesPadecidas;
    private BigInteger contadorSoausentismos;
    private BigInteger contadorSorevisionessSistemas;

    public ControlEnfermedades() {
        listEnfermedades = null;
        crearEnfermedades = new ArrayList<Enfermedades>();
        modificarEnfermedades = new ArrayList<Enfermedades>();
        borrarEnfermedades = new ArrayList<Enfermedades>();
        editarEnfermedad = new Enfermedades();
        nuevaEnfermedad = new Enfermedades();
        duplicarEnfermedad = new Enfermedades();
        guardado = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        tamano = 335;
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

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "enfermedad";
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
        log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEnfermedades.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listEnfermedades = null;
            getListEnfermedades();
            if (listEnfermedades != null) {
                enfermedadSeleccionada = listEnfermedades.get(0);
            }
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void cambiarIndice(Enfermedades enfermedad, int celda) {

        enfermedadSeleccionada = enfermedad;
        cualCelda = celda;
        enfermedadSeleccionada.getSecuencia();
        if (cualCelda == 0) {
            enfermedadSeleccionada.getCodigo();
        } else if (cualCelda == 1) {
            enfermedadSeleccionada.getDescripcion();
        }

    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEnfermedades");
            bandera = 0;
            filtrarEnfermedades = null;
            tipoLista = 0;
            tamano = 335;
        }

        borrarEnfermedades.clear();
        crearEnfermedades.clear();
        modificarEnfermedades.clear();
        enfermedadSeleccionada = null;
        k = 0;
        listEnfermedades = null;
        getListEnfermedades();
        contarRegistros();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEnfermedades");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEnfermedades");
            bandera = 0;
            filtrarEnfermedades = null;
            tipoLista = 0;
            tamano = 335;
        }

        borrarEnfermedades.clear();
        crearEnfermedades.clear();
        modificarEnfermedades.clear();
        enfermedadSeleccionada = null;
        k = 0;
        listEnfermedades = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEnfermedades");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            tamano = 315;
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosEnfermedades");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 335;
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEnfermedades");
            bandera = 0;
            filtrarEnfermedades = null;
            tipoLista = 0;
        }
    }

    public void modificarEnfermedad(Enfermedades enfermedad) {
        enfermedadSeleccionada = enfermedad;
        if (!crearEnfermedades.contains(enfermedadSeleccionada)) {
            if (modificarEnfermedades.isEmpty()) {
                modificarEnfermedades.add(enfermedadSeleccionada);
            } else if (!modificarEnfermedades.contains(enfermedadSeleccionada)) {
                modificarEnfermedades.add(enfermedadSeleccionada);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:datosEnfermedades");
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void borrarEnfermedad() {
        if (enfermedadSeleccionada != null) {
            log.info("Entro a borrarEnfermedades");
            if (!modificarEnfermedades.isEmpty() && modificarEnfermedades.contains(enfermedadSeleccionada)) {
                int modIndex = modificarEnfermedades.indexOf(enfermedadSeleccionada);
                modificarEnfermedades.remove(modIndex);
                borrarEnfermedades.add(enfermedadSeleccionada);
            } else if (!crearEnfermedades.isEmpty() && crearEnfermedades.contains(enfermedadSeleccionada)) {
                int crearIndex = crearEnfermedades.indexOf(enfermedadSeleccionada);
                crearEnfermedades.remove(crearIndex);
            } else {
                borrarEnfermedades.add(enfermedadSeleccionada);
            }
            listEnfermedades.remove(enfermedadSeleccionada);
            if (tipoLista == 1) {
                filtrarEnfermedades.remove(enfermedadSeleccionada);

            }
            enfermedadSeleccionada = null;
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:datosEnfermedades");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void verificarBorrado() {
        try {
            contadorAusentimos = administrarEnfermedades.verificarAusentimos(enfermedadSeleccionada.getSecuencia());
            contadorDetallesLicencias = administrarEnfermedades.verificarDetallesLicencias(enfermedadSeleccionada.getSecuencia());
            contadorEnfermedadesPadecidas = administrarEnfermedades.verificarEnfermedadesPadecidas(enfermedadSeleccionada.getSecuencia());
            contadorSoausentismos = administrarEnfermedades.verificarSoAusentismos(enfermedadSeleccionada.getSecuencia());
            contadorSorevisionessSistemas = administrarEnfermedades.verificarSoRevisionesSistemas(enfermedadSeleccionada.getSecuencia());
            if (!contadorAusentimos.equals(new BigInteger("0")) || !contadorDetallesLicencias.equals(new BigInteger("0")) || !contadorEnfermedadesPadecidas.equals(new BigInteger("0")) || !contadorSoausentismos.equals(new BigInteger("0")) || !contadorSorevisionessSistemas.equals(new BigInteger("0"))) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                contadorAusentimos = new BigInteger("-1");
                contadorDetallesLicencias = new BigInteger("-1");
                contadorEnfermedadesPadecidas = new BigInteger("-1");
                contadorSoausentismos = new BigInteger("-1");
                contadorSorevisionessSistemas = new BigInteger("-1");
            } else {
                borrarEnfermedad();
            }
        } catch (Exception e) {
            log.error("ERROR CONTROLENFERMEDADES verificarBorrado ERROR  ", e);
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarEnfermedades.isEmpty() || !crearEnfermedades.isEmpty() || !modificarEnfermedades.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarEnfermedad() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarEnfermedades.isEmpty()) {
                    for (int i = 0; i < borrarEnfermedades.size(); i++) {
                        msgError = administrarEnfermedades.borrarEnfermedades(borrarEnfermedades.get(i));
                    }
                    registrosBorrados = borrarEnfermedades.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarEnfermedades.clear();
                }
                if (!crearEnfermedades.isEmpty()) {
                    for (int i = 0; i < crearEnfermedades.size(); i++) {
                        msgError = administrarEnfermedades.crearEnfermedades(crearEnfermedades.get(i));
                    }
                    crearEnfermedades.clear();
                }
                if (!modificarEnfermedades.isEmpty()) {
                    for (int i = 0; i < modificarEnfermedades.size(); i++) {
                        msgError = administrarEnfermedades.modificarEnfermedades(modificarEnfermedades.get(i));
                    }
                    modificarEnfermedades.clear();
                }
                if (msgError.equals("EXITO")) {

                    listEnfermedades = null;
                    getListEnfermedades();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    enfermedadSeleccionada = null;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    RequestContext.getCurrentInstance().update("form:datosEnfermedades");
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardadoBD");
                    RequestContext.getCurrentInstance().execute("PF('errorGuardadoBD').show()");
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
        if (enfermedadSeleccionada != null) {
            editarEnfermedad = enfermedadSeleccionada;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoEnfermedades() {
        int contador = 0;
        int duplicados = 0;
        a = null;
        mensajeValidacion = " ";
        if (nuevaEnfermedad.getCodigo() == a) {
            mensajeValidacion = " *Debe Tener Un Codigo \n";
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            for (int x = 0; x < listEnfermedades.size(); x++) {
                if (listEnfermedades.get(x).getCodigo() == nuevaEnfermedad.getCodigo()) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevaEnfermedad.getDescripcion() == (null) || nuevaEnfermedad.getDescripcion().isEmpty()) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                log.info("Desactivar");
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEnfermedades");
                bandera = 0;
                filtrarEnfermedades = null;
                tipoLista = 0;
                tamano = 335;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaEnfermedad.setSecuencia(l);
            crearEnfermedades.add(0, nuevaEnfermedad);
            listEnfermedades.add(nuevaEnfermedad);
            enfermedadSeleccionada = nuevaEnfermedad;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEnfermedades').hide()");
            nuevaEnfermedad = new Enfermedades();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoEnfermedades() {
        nuevaEnfermedad = new Enfermedades();
    }

    //------------------------------------------------------------------------------
    public void duplicarEnfermedades() {
        if (enfermedadSeleccionada != null) {
            duplicarEnfermedad = new Enfermedades();
            k++;
            l = BigInteger.valueOf(k);
            duplicarEnfermedad.setSecuencia(l);
            duplicarEnfermedad.setCodigo(enfermedadSeleccionada.getCodigo());
            duplicarEnfermedad.setDescripcion(enfermedadSeleccionada.getDescripcion());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEnfer");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEnfermedad').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        a = null;
        if (duplicarEnfermedad.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listEnfermedades.size(); x++) {
                if (listEnfermedades.get(x).getCodigo() == duplicarEnfermedad.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarEnfermedad.getDescripcion() == null || duplicarEnfermedad.getDescripcion().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (contador == 2) {
            listEnfermedades.add(duplicarEnfermedad);
            crearEnfermedades.add(duplicarEnfermedad);
            RequestContext.getCurrentInstance().update("form:datosEnfermedades");
            enfermedadSeleccionada = duplicarEnfermedad;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                //CERRAR FILTRADO
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosEnfermedades:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEnfermedades");
                bandera = 0;
                filtrarEnfermedades = null;
                tipoLista = 0;
                tamano = 335;
            }
            duplicarEnfermedad = new Enfermedades();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEnfermedad').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarEnfermedades() {
        duplicarEnfermedad = new Enfermedades();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEnfermedadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ENFERMEDADES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEnfermedadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ENFERMEDADES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (enfermedadSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(enfermedadSeleccionada.getSecuencia(), "ENFERMEDADES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("ENFERMEDADES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    ////////////GETS Y SETS//////////////////
    public List<Enfermedades> getListEnfermedades() {
        if (listEnfermedades == null) {
            listEnfermedades = administrarEnfermedades.consultarEnfermedades();
        }
        return listEnfermedades;
    }

    public void setListEnfermedades(List<Enfermedades> listEnfermedades) {
        this.listEnfermedades = listEnfermedades;
    }

    public List<Enfermedades> getFiltrarEnfermedades() {
        return filtrarEnfermedades;
    }

    public void setFiltrarEnfermedades(List<Enfermedades> filtrarEnfermedades) {
        this.filtrarEnfermedades = filtrarEnfermedades;
    }

    public Enfermedades getNuevaEnfermedad() {
        return nuevaEnfermedad;
    }

    public void setNuevaEnfermedad(Enfermedades nuevaEnfermedad) {
        this.nuevaEnfermedad = nuevaEnfermedad;
    }

    public Enfermedades getDuplicarEnfermedad() {
        return duplicarEnfermedad;
    }

    public void setDuplicarEnfermedad(Enfermedades duplicarEnfermedad) {
        this.duplicarEnfermedad = duplicarEnfermedad;
    }

    public Enfermedades getEditarEnfermedad() {
        return editarEnfermedad;
    }

    public void setEditarEnfermedad(Enfermedades editarEnfermedad) {
        this.editarEnfermedad = editarEnfermedad;
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

    public Enfermedades getEnfermedadSeleccionada() {
        return enfermedadSeleccionada;
    }

    public void setEnfermedadSeleccionada(Enfermedades enfermedadSeleccionada) {
        this.enfermedadSeleccionada = enfermedadSeleccionada;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEnfermedades");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

}
