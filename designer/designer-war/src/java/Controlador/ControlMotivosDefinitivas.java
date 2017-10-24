/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.MotivosDefinitivas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosDefinitivasInterface;
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
public class ControlMotivosDefinitivas implements Serializable {

    private static Logger log = Logger.getLogger(ControlMotivosDefinitivas.class);

    @EJB
    AdministrarMotivosDefinitivasInterface administrarMotivosDefinitivas;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<MotivosDefinitivas> listMotivosDefinitivas;
    private List<MotivosDefinitivas> filtrarMotivosDefinitivas;
    private List<MotivosDefinitivas> crearMotivosDefinitivas;
    private List<MotivosDefinitivas> modificarMotivosDefinitivas;
    private List<MotivosDefinitivas> borrarMotivosDefinitivas;
    private MotivosDefinitivas nuevoMotivoDefinitiva;
    private MotivosDefinitivas duplicarMotivoDefinitiva;
    private MotivosDefinitivas editarMotivoDefinitiva;
    private MotivosDefinitivas motivoDefinitivaSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion, retiro, cambioRegimen;
    private String altoTabla;
    //borrado
    private int registrosBorrados;
    private String infoRegistro;
    private String mensajeValidacion;
    private String paginaAnterior = "nominaf";
    private BigInteger novedadesSistema;
    private BigInteger verificarParametrosCambiosMasivos;
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String msgError;

    public ControlMotivosDefinitivas() {
        listMotivosDefinitivas = null;
        crearMotivosDefinitivas = new ArrayList<MotivosDefinitivas>();
        modificarMotivosDefinitivas = new ArrayList<MotivosDefinitivas>();
        borrarMotivosDefinitivas = new ArrayList<MotivosDefinitivas>();
        editarMotivoDefinitiva = new MotivosDefinitivas();
        nuevoMotivoDefinitiva = new MotivosDefinitivas();
        duplicarMotivoDefinitiva = new MotivosDefinitivas();
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

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "motivodefinitiva";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
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

    public void limpiarListasValor() {

    }

    @PreDestroy
    public void destruyendose() {
        log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMotivosDefinitivas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listMotivosDefinitivas = null;
            getListMotivosDefinitivas();
            if (listMotivosDefinitivas != null) {
                motivoDefinitivaSeleccionado = listMotivosDefinitivas.get(0);
            }

        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(MotivosDefinitivas motivo, int celda) {

        motivoDefinitivaSeleccionado = motivo;
        cualCelda = celda;
        motivoDefinitivaSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            motivoDefinitivaSeleccionado.getCodigo();
        } else if (cualCelda == 1) {
            motivoDefinitivaSeleccionado.getNombre();

        } else if (cualCelda == 2) {
            motivoDefinitivaSeleccionado.getRetiro();
//        } else if (cualCelda == 3) {
//            motivoDefinitivaSeleccionado.getCatedraticosemestral();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            retiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:retiro");
            retiro.setFilterStyle("display: none; visibility: hidden;");
            cambioRegimen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:cambioRegimen");
            cambioRegimen.setFilterStyle("display: none; visibility: hidden;");
//            catedratico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:catedratico");
//            catedratico.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");
            bandera = 0;
            filtrarMotivosDefinitivas = null;
            tipoLista = 0;
            altoTabla = "335";
        }

        borrarMotivosDefinitivas.clear();
        crearMotivosDefinitivas.clear();
        modificarMotivosDefinitivas.clear();
        motivoDefinitivaSeleccionado = null;
        k = 0;
        listMotivosDefinitivas = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
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
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            retiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:retiro");
            retiro.setFilterStyle("width: 85% !important;");
            cambioRegimen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:cambioRegimen");
            cambioRegimen.setFilterStyle("width: 85% !important;");
//            catedratico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:catedratico");
//            catedratico.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "335";
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            retiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:retiro");
            retiro.setFilterStyle("display: none; visibility: hidden;");
            cambioRegimen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:cambioRegimen");
            cambioRegimen.setFilterStyle("display: none; visibility: hidden;");
//            catedratico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:catedratico");
//            catedratico.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");
            bandera = 0;
            filtrarMotivosDefinitivas = null;
            tipoLista = 0;
        }
    }

    public void mostrarInfo(MotivosDefinitivas motivo) {
        motivoDefinitivaSeleccionado = motivo;
        if (motivoDefinitivaSeleccionado.getVariableRetiro().equals("SI")) {
            motivoDefinitivaSeleccionado.setRetiro("S");
        } else if (motivoDefinitivaSeleccionado.getVariableRetiro().equals("NO")) {
            motivoDefinitivaSeleccionado.setRetiro("N");
        }
        if (motivoDefinitivaSeleccionado.getVariableCambioRegimen().equals("SI")) {
            motivoDefinitivaSeleccionado.setCambioregimen("S");
        } else if (motivoDefinitivaSeleccionado.getVariableCambioRegimen().equals("NO")) {
            motivoDefinitivaSeleccionado.setCambioregimen("N");
        } else if (motivoDefinitivaSeleccionado.getVariableCambioRegimen().equals(" ")) {
            motivoDefinitivaSeleccionado.setCambioregimen(null);
        }
//        if (motivoDefinitivaSeleccionado.getVariableCatedratico().equals("SI")) {
//            motivoDefinitivaSeleccionado.setCatedraticosemestral("S");
//        } else if (motivoDefinitivaSeleccionado.getVariableCatedratico().equals("NO")) {
//            motivoDefinitivaSeleccionado.setCatedraticosemestral("N");
//        }

        modificandoMotivoDefinitivas(motivoDefinitivaSeleccionado);
    }

    public void modificandoMotivoDefinitivas(MotivosDefinitivas motivo) {
        motivoDefinitivaSeleccionado = motivo;
        if (!crearMotivosDefinitivas.contains(motivoDefinitivaSeleccionado)) {
            if (modificarMotivosDefinitivas.isEmpty()) {
                modificarMotivosDefinitivas.add(motivoDefinitivaSeleccionado);
            } else if (!modificarMotivosDefinitivas.contains(motivoDefinitivaSeleccionado)) {
                modificarMotivosDefinitivas.add(motivoDefinitivaSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");
    }

    public void verificarBorrado() {
        try {
            novedadesSistema = administrarMotivosDefinitivas.contarNovedadesSistemasMotivoDefinitiva(motivoDefinitivaSeleccionado.getSecuencia());
            verificarParametrosCambiosMasivos = administrarMotivosDefinitivas.contarParametrosCambiosMasivosMotivoDefinitiva(motivoDefinitivaSeleccionado.getSecuencia());
            if (!novedadesSistema.equals(new BigInteger("0")) || !verificarParametrosCambiosMasivos.equals(new BigInteger("0"))) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                novedadesSistema = new BigInteger("-1");
                verificarParametrosCambiosMasivos = new BigInteger("-1");
            } else {
                borrandoMotivosDefinitivas();
            }
        } catch (Exception e) {
            log.error("ERROR ControlTiposCertificados verificarBorrado ERROR  ", e);
        }
    }

    public void borrandoMotivosDefinitivas() {

        if (motivoDefinitivaSeleccionado != null) {
            if (!modificarMotivosDefinitivas.isEmpty() && modificarMotivosDefinitivas.contains(motivoDefinitivaSeleccionado)) {
                int modIndex = modificarMotivosDefinitivas.indexOf(motivoDefinitivaSeleccionado);
                modificarMotivosDefinitivas.remove(modIndex);
                borrarMotivosDefinitivas.add(motivoDefinitivaSeleccionado);
            } else if (!crearMotivosDefinitivas.isEmpty() && crearMotivosDefinitivas.contains(motivoDefinitivaSeleccionado)) {
                int crearIndex = crearMotivosDefinitivas.indexOf(motivoDefinitivaSeleccionado);
                crearMotivosDefinitivas.remove(crearIndex);
            } else {
                borrarMotivosDefinitivas.add(motivoDefinitivaSeleccionado);
            }
            listMotivosDefinitivas.remove(motivoDefinitivaSeleccionado);
            if (tipoLista == 1) {
                filtrarMotivosDefinitivas.remove(motivoDefinitivaSeleccionado);
            }
            motivoDefinitivaSeleccionado = null;
            RequestContext context = RequestContext.getCurrentInstance();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");

        }

    }

    public void revisarDialogoGuardar() {
        if (!borrarMotivosDefinitivas.isEmpty() || !crearMotivosDefinitivas.isEmpty() || !modificarMotivosDefinitivas.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarMotivoDefinitivas() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarMotivosDefinitivas.isEmpty()) {
                    for (int i = 0; i < borrarMotivosDefinitivas.size(); i++) {
                        msgError = administrarMotivosDefinitivas.borrarMotivosDefinitivas(borrarMotivosDefinitivas.get(i));
                    }
                    registrosBorrados = borrarMotivosDefinitivas.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarMotivosDefinitivas.clear();
                }
                if (!crearMotivosDefinitivas.isEmpty()) {
                    for (int i = 0; i < crearMotivosDefinitivas.size(); i++) {
                        msgError = administrarMotivosDefinitivas.crearMotivosDefinitivas(crearMotivosDefinitivas.get(i));
                    }
                    crearMotivosDefinitivas.clear();
                }
                if (!modificarMotivosDefinitivas.isEmpty()) {
                    for (int i = 0; i < modificarMotivosDefinitivas.size(); i++) {
                        msgError = administrarMotivosDefinitivas.modificarMotivosDefinitivas(crearMotivosDefinitivas.get(i));
                    }
                }
                modificarMotivosDefinitivas.clear();

                if (msgError.equals("EXITO")) {
                    listMotivosDefinitivas = null;
                    getListMotivosDefinitivas();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    motivoDefinitivaSeleccionado = null;
                    RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {
                    RequestContext.getCurrentInstance().update("formularioDialogos:errorGuardado");
                    RequestContext.getCurrentInstance().execute("PF('errorGuardado').show()");
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
        if (motivoDefinitivaSeleccionado != null) {
            editarMotivoDefinitiva = motivoDefinitivaSeleccionado;
            RequestContext context = RequestContext.getCurrentInstance();
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

    public void agregarNuevoMotivosDefinitivas() {
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        if (nuevoMotivoDefinitiva.getCodigo() == a) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios\n";
        } else {
            for (int x = 0; x < listMotivosDefinitivas.size(); x++) {
                if (listMotivosDefinitivas.get(x).getCodigo() == nuevoMotivoDefinitiva.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevoMotivoDefinitiva.getNombre() == (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios\n";
        } else {
            contador++;
        }
        if (nuevoMotivoDefinitiva.getRetiro() == null) {
            nuevoMotivoDefinitiva.setRetiro("S");
        }
        if (nuevoMotivoDefinitiva.getCambioregimen() == null || nuevoMotivoDefinitiva.getCambioregimen().equals(" ")) {
            nuevoMotivoDefinitiva.setCambioregimen(null);
        }
//        if (nuevoMotivoDefinitiva.getCatedraticosemestral() == null) {
//            nuevoMotivoDefinitiva.setCatedraticosemestral("N");
//        }
//        log.info("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                retiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:retiro");
                retiro.setFilterStyle("display: none; visibility: hidden;");
                cambioRegimen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:cambioRegimen");
                cambioRegimen.setFilterStyle("display: none; visibility: hidden;");
//                catedratico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:catedratico");
//                catedratico.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");
                bandera = 0;
                filtrarMotivosDefinitivas = null;
                tipoLista = 0;
                altoTabla = "335";
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoDefinitiva.setSecuencia(l);
            crearMotivosDefinitivas.add(nuevoMotivoDefinitiva);
            listMotivosDefinitivas.add(0, nuevoMotivoDefinitiva);
            motivoDefinitivaSeleccionado = nuevoMotivoDefinitiva;
            RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMotivoDefinitiva').hide()");
            nuevoMotivoDefinitiva = new MotivosDefinitivas();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivosDefinitivas() {
        nuevoMotivoDefinitiva = new MotivosDefinitivas();
    }

    public void duplicandoMotivosDefinitivas() {
        if (motivoDefinitivaSeleccionado != null) {
            duplicarMotivoDefinitiva = new MotivosDefinitivas();
            k++;
            l = BigInteger.valueOf(k);
            duplicarMotivoDefinitiva.setSecuencia(l);
            duplicarMotivoDefinitiva.setCodigo(motivoDefinitivaSeleccionado.getCodigo());
            duplicarMotivoDefinitiva.setNombre(motivoDefinitivaSeleccionado.getNombre());
            duplicarMotivoDefinitiva.setRetiro(motivoDefinitivaSeleccionado.getRetiro());
            duplicarMotivoDefinitiva.setCambioregimen(motivoDefinitivaSeleccionado.getCambioregimen());
//            duplicarMotivoDefinitiva.setCatedraticosemestral(motivoDefinitivaSeleccionado.getCatedraticosemestral());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTR");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosDefinitivas').show()");
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

        if (duplicarMotivoDefinitiva.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listMotivosDefinitivas.size(); x++) {
                if (listMotivosDefinitivas.get(x).getCodigo() == duplicarMotivoDefinitiva.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                log.info("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarMotivoDefinitiva.getNombre() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (duplicarMotivoDefinitiva.getCambioregimen() == null || duplicarMotivoDefinitiva.getCambioregimen().equals("") || duplicarMotivoDefinitiva.getCambioregimen().equals(" ")) {
            duplicarMotivoDefinitiva.setCambioregimen(null);
        }
        if (contador == 2) {
            listMotivosDefinitivas.add(0, duplicarMotivoDefinitiva);
            crearMotivosDefinitivas.add(duplicarMotivoDefinitiva);
            motivoDefinitivaSeleccionado = duplicarMotivoDefinitiva;
            RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                retiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:retiro");
                retiro.setFilterStyle("display: none; visibility: hidden;");
                cambioRegimen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:cambioRegimen");
                cambioRegimen.setFilterStyle("display: none; visibility: hidden;");
//                catedratico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:catedratico");
//                catedratico.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");
                bandera = 0;
                filtrarMotivosDefinitivas = null;
                tipoLista = 0;
                altoTabla = "270";
            }
            duplicarMotivoDefinitiva = new MotivosDefinitivas();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosDefinitivas').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarMotivosDefinitivas() {
        duplicarMotivoDefinitiva = new MotivosDefinitivas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosDefinitivaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosDefinitivas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosDefinitivaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosDefinitivas", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        if (motivoDefinitivaSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(motivoDefinitivaSeleccionado.getSecuencia(), "MOTIVOSDEFINITIVAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSDEFINITIVAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            retiro = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:retiro");
            retiro.setFilterStyle("display: none; visibility: hidden;");
            cambioRegimen = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:cambioRegimen");
            cambioRegimen.setFilterStyle("display: none; visibility: hidden;");
//            catedratico = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosMotivosDefinitiva:catedratico");
//            catedratico.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");
            bandera = 0;
            filtrarMotivosDefinitivas = null;
            tipoLista = 0;
            altoTabla = "335";
        }
        borrarMotivosDefinitivas.clear();
        crearMotivosDefinitivas.clear();
        modificarMotivosDefinitivas.clear();
        motivoDefinitivaSeleccionado = null;
        k = 0;
        listMotivosDefinitivas = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosMotivosDefinitiva");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
    public List<MotivosDefinitivas> getListMotivosDefinitivas() {
        if (listMotivosDefinitivas == null) {
            listMotivosDefinitivas = administrarMotivosDefinitivas.mostrarMotivosDefinitivas();
        }
        return listMotivosDefinitivas;
    }

    public void setListMotivosDefinitivas(List<MotivosDefinitivas> listMotivosDefinitivas) {
        this.listMotivosDefinitivas = listMotivosDefinitivas;
    }

    public List<MotivosDefinitivas> getFiltrarMotivosDefinitivas() {
        return filtrarMotivosDefinitivas;
    }

    public void setFiltrarMotivosDefinitivas(List<MotivosDefinitivas> filtrarMotivosDefinitivas) {
        this.filtrarMotivosDefinitivas = filtrarMotivosDefinitivas;
    }

    public MotivosDefinitivas getNuevoMotivoDefinitiva() {
        return nuevoMotivoDefinitiva;
    }

    public void setNuevoMotivoDefinitiva(MotivosDefinitivas nuevoMotivoDefinitiva) {
        this.nuevoMotivoDefinitiva = nuevoMotivoDefinitiva;
    }

    public MotivosDefinitivas getDuplicarMotivoDefinitiva() {
        return duplicarMotivoDefinitiva;
    }

    public void setDuplicarMotivoDefinitiva(MotivosDefinitivas duplicarMotivoDefinitiva) {
        this.duplicarMotivoDefinitiva = duplicarMotivoDefinitiva;
    }

    public MotivosDefinitivas getEditarMotivoDefinitiva() {
        return editarMotivoDefinitiva;
    }

    public void setEditarMotivoDefinitiva(MotivosDefinitivas editarMotivoDefinitiva) {
        this.editarMotivoDefinitiva = editarMotivoDefinitiva;
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

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMotivosDefinitiva");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public MotivosDefinitivas getMotivoDefinitivaSeleccionado() {
        return motivoDefinitivaSeleccionado;
    }

    public void setMotivoDefinitivaSeleccionado(MotivosDefinitivas motivoDefinitivaSeleccionado) {
        this.motivoDefinitivaSeleccionado = motivoDefinitivaSeleccionado;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

}
