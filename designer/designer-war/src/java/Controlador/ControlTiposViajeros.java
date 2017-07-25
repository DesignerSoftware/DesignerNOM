/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Tiposviajeros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposViajerosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
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
public class ControlTiposViajeros implements Serializable {

   private static Logger log = Logger.getLogger(ControlTiposViajeros.class);

    @EJB
    AdministrarTiposViajerosInterface administrarTiposViajeros;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Tiposviajeros> listTiposViajeros;
    private List<Tiposviajeros> filtrarTiposViajeros;
    private List<Tiposviajeros> crearTiposViajeros;
    private List<Tiposviajeros> modificarTiposViajeros;
    private List<Tiposviajeros> borrarTiposViajeros;
    private Tiposviajeros nuevoTiposViajeros;
    private Tiposviajeros duplicarTiposViajeros;
    private Tiposviajeros editarTiposViajeros;
    private Tiposviajeros tiposViajeroSeleccionado;
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
    //filtrado table
    private int tamano;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String paginaAnterior = "nominaf";
    private String infoRegistro;

    public ControlTiposViajeros() {
        listTiposViajeros = null;
        crearTiposViajeros = new ArrayList<Tiposviajeros>();
        modificarTiposViajeros = new ArrayList<Tiposviajeros>();
        borrarTiposViajeros = new ArrayList<Tiposviajeros>();
        permitirIndex = true;
        editarTiposViajeros = new Tiposviajeros();
        nuevoTiposViajeros = new Tiposviajeros();
        duplicarTiposViajeros = new Tiposviajeros();
        guardado = true;
        tamano = 320;
        mapParametros.put("paginaAnterior", paginaAnterior);
        log.info("controlTiposViajeros Constructor");
    }

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            log.info("ControlTiposViajeros PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposViajeros.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
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
        String pagActual = "tipoviajero";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
            //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParaEnviar.put("paginaAnterior", pagActual);
            //mas Parametros
            //         if (pag.equals("rastrotabla")) {
            //           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
        }
        limpiarListasValor();
    }

    public void inicializarLista() {
        getListTiposViajeros();
        if (listTiposViajeros != null) {
            tiposViajeroSeleccionado = listTiposViajeros.get(0);
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        tiposViajeroSeleccionado = null;
        contarRegistros();
    }

    public void cambiarIndice(Tiposviajeros tipov, int celda) {
        if (permitirIndex == true) {
            tiposViajeroSeleccionado = tipov;
            cualCelda = celda;
            tiposViajeroSeleccionado.getSecuencia();
            if (cualCelda == 0) {
                backUpCodigo = tiposViajeroSeleccionado.getCodigo();
                log.info(" backUpCodigo : " + backUpCodigo);
            } else if (cualCelda == 1) {
                backUpDescripcion = tiposViajeroSeleccionado.getNombre();
                log.info(" backUpDescripcion : " + backUpDescripcion);
            }

        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        tiposViajeroSeleccionado = null;
        cerrarFiltrado();
        borrarTiposViajeros.clear();
        crearTiposViajeros.clear();
        modificarTiposViajeros.clear();
        listTiposViajeros = null;
        k = 0;
        guardado = true;
        permitirIndex = true;
        getListTiposViajeros();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        cerrarFiltrado();
        borrarTiposViajeros.clear();
        crearTiposViajeros.clear();
        modificarTiposViajeros.clear();
        tiposViajeroSeleccionado = null;
        k = 0;
        listTiposViajeros = null;
        guardado = true;
        permitirIndex = true;
        navegar("atras");
    }

    public void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        codigo = (Column) c.getViewRoot().findComponent("form:datosTiposViajeros:codigo");
        codigo.setFilterStyle("display: none; visibility: hidden;");
        descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposViajeros:descripcion");
        descripcion.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
        bandera = 0;
        filtrarTiposViajeros = null;
        tipoLista = 0;
        tamano = 320;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 300;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposViajeros:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposViajeros:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
        }
    }

    public void modificarTiposViajeros(Tiposviajeros tipov) {
        tiposViajeroSeleccionado = tipov;
        if (!crearTiposViajeros.contains(tiposViajeroSeleccionado)) {
            if (modificarTiposViajeros.isEmpty()) {
                modificarTiposViajeros.add(tiposViajeroSeleccionado);
            } else if (!modificarTiposViajeros.contains(tiposViajeroSeleccionado)) {
                modificarTiposViajeros.add(tiposViajeroSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
        }
    }

    public void borrandoTiposViajeros() {
        if (tiposViajeroSeleccionado != null) {
            if (tipoLista == 0) {
                if (!modificarTiposViajeros.isEmpty() && modificarTiposViajeros.contains(tiposViajeroSeleccionado)) {
                    int modIndex = modificarTiposViajeros.indexOf(tiposViajeroSeleccionado);
                    modificarTiposViajeros.remove(modIndex);
                    borrarTiposViajeros.add(tiposViajeroSeleccionado);
                } else if (!crearTiposViajeros.isEmpty() && crearTiposViajeros.contains(tiposViajeroSeleccionado)) {
                    int crearIndex = crearTiposViajeros.indexOf(tiposViajeroSeleccionado);
                    crearTiposViajeros.remove(crearIndex);
                } else {
                    borrarTiposViajeros.add(tiposViajeroSeleccionado);
                }
                listTiposViajeros.remove(tiposViajeroSeleccionado);
            }
            if (tipoLista == 1) {
                filtrarTiposViajeros.remove(tiposViajeroSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
            contarRegistros();
            tiposViajeroSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void verificarBorrado() {
        BigInteger contarTiposLegalizaciones;
        BigInteger contarVigenciasViajeros;

        try {
            contarTiposLegalizaciones = administrarTiposViajeros.contarTiposLegalizaciones(tiposViajeroSeleccionado.getSecuencia());
            contarVigenciasViajeros = administrarTiposViajeros.contarVigenciasViajeros(tiposViajeroSeleccionado.getSecuencia());
            if (contarTiposLegalizaciones.equals(new BigInteger("0"))
                    && contarVigenciasViajeros.equals(new BigInteger("0"))) {
                borrandoTiposViajeros();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                contarTiposLegalizaciones = new BigInteger("-1");
            }
        } catch (Exception e) {
            log.error("ERROR ControlTiposViajeros verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarTiposViajeros.isEmpty() || !crearTiposViajeros.isEmpty() || !modificarTiposViajeros.isEmpty()) {
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarYSalir() {
        guardarTiposViajeros();
        salir();
    }

    public void guardarTiposViajeros() {
        if (guardado == false) {
            log.info("Realizando guardarTiposViajeros");
            if (!borrarTiposViajeros.isEmpty()) {
                administrarTiposViajeros.borrarTiposViajeros(borrarTiposViajeros);
                registrosBorrados = borrarTiposViajeros.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarTiposViajeros.clear();
            }
            if (!modificarTiposViajeros.isEmpty()) {
                administrarTiposViajeros.modificarTiposViajeros(modificarTiposViajeros);
                modificarTiposViajeros.clear();
            }
            if (!crearTiposViajeros.isEmpty()) {
                administrarTiposViajeros.crearTiposViajeros(crearTiposViajeros);
                crearTiposViajeros.clear();
            }
            listTiposViajeros = null;
            RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (tiposViajeroSeleccionado != null) {
            editarTiposViajeros = tiposViajeroSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            }
        }
    }

    public void agregarNuevoTiposViajeros() {
        log.info("agregarNuevoTiposViajeros");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposViajeros.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposViajeros.size(); x++) {
                if (listTiposViajeros.get(x).getCodigo().equals(nuevoTiposViajeros.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " Existe un registro con el código ingresado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoTiposViajeros.getNombre() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";

        } else if (nuevoTiposViajeros.getNombre().isEmpty()) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                cerrarFiltrado();
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposViajeros.setSecuencia(l);
            crearTiposViajeros.add(nuevoTiposViajeros);
            listTiposViajeros.add(nuevoTiposViajeros);
            tiposViajeroSeleccionado = nuevoTiposViajeros;
            tiposViajeroSeleccionado = listTiposViajeros.get(listTiposViajeros.indexOf(nuevoTiposViajeros));
            nuevoTiposViajeros = new Tiposviajeros();
            RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposViajeros').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposViajeros() {
        nuevoTiposViajeros = new Tiposviajeros();
    }

    public void duplicandoTiposViajeros() {
        if (tiposViajeroSeleccionado != null) {
            duplicarTiposViajeros = new Tiposviajeros();
            k++;
            l = BigInteger.valueOf(k);

            duplicarTiposViajeros.setSecuencia(l);
            duplicarTiposViajeros.setCodigo(tiposViajeroSeleccionado.getCodigo());
            duplicarTiposViajeros.setNombre(tiposViajeroSeleccionado.getNombre());

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposViajeros').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;

        if (duplicarTiposViajeros.getCodigo() == a) {
            mensajeValidacion = "  Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposViajeros.size(); x++) {
                if (listTiposViajeros.get(x).getCodigo().equals(duplicarTiposViajeros.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " Existe un registro con el código ingresado. Por favor ingrese un código válido";
            } else {
                log.info("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarTiposViajeros.getNombre() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";

        } else if (duplicarTiposViajeros.getNombre().isEmpty()) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            log.info("bandera");
            contador++;
        }
        if (contador == 2) {
            if (crearTiposViajeros.contains(duplicarTiposViajeros)) {
                log.info("Ya lo contengo.");
            }
            listTiposViajeros.add(duplicarTiposViajeros);
            crearTiposViajeros.add(duplicarTiposViajeros);
            tiposViajeroSeleccionado = duplicarTiposViajeros;
            RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();
            if (bandera == 1) {
                cerrarFiltrado();
            }
            duplicarTiposViajeros = new Tiposviajeros();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposViajeros').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposViajeros() {
        duplicarTiposViajeros = new Tiposviajeros();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposViajerosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSVIAJEROS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposViajerosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSVIAJEROS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (tiposViajeroSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(tiposViajeroSeleccionado.getSecuencia(), "TIPOSVIAJEROS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSVIAJEROS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Tiposviajeros> getListTiposViajeros() {
        if (listTiposViajeros == null) {
            listTiposViajeros = administrarTiposViajeros.consultarTiposViajeros();
        }
        return listTiposViajeros;
    }

    public void setListTiposViajeros(List<Tiposviajeros> listTiposViajeros) {
        this.listTiposViajeros = listTiposViajeros;
    }

    public List<Tiposviajeros> getFiltrarTiposViajeros() {
        return filtrarTiposViajeros;
    }

    public void setFiltrarTiposViajeros(List<Tiposviajeros> filtrarTiposViajeros) {
        this.filtrarTiposViajeros = filtrarTiposViajeros;
    }

    public Tiposviajeros getNuevoTiposViajeros() {
        return nuevoTiposViajeros;
    }

    public void setNuevoTiposViajeros(Tiposviajeros nuevoTiposViajeros) {
        this.nuevoTiposViajeros = nuevoTiposViajeros;
    }

    public Tiposviajeros getDuplicarTiposViajeros() {
        return duplicarTiposViajeros;
    }

    public void setDuplicarTiposViajeros(Tiposviajeros duplicarTiposViajeros) {
        this.duplicarTiposViajeros = duplicarTiposViajeros;
    }

    public Tiposviajeros getEditarTiposViajeros() {
        return editarTiposViajeros;
    }

    public void setEditarTiposViajeros(Tiposviajeros editarTiposViajeros) {
        this.editarTiposViajeros = editarTiposViajeros;
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

    public Tiposviajeros getTiposViajerosSeleccionado() {
        return tiposViajeroSeleccionado;
    }

    public void setTiposViajerosSeleccionado(Tiposviajeros tiposViajeroSeleccionado) {
        this.tiposViajeroSeleccionado = tiposViajeroSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposViajeros");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }
}
