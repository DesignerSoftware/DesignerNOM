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
public class ControlPryPlataformas implements Serializable {

   private static Logger log = Logger.getLogger(ControlPryPlataformas.class);

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
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

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
        activarLOV = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listPryPlataformas = null;
        getListPryPlataformas();
        if (listPryPlataformas != null) {
            if (!listPryPlataformas.isEmpty()) {
                pryPlataformaSeleccionada = listPryPlataformas.get(0);
            }
        }
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listPryPlataformas = null;
        getListPryPlataformas();
        if (listPryPlataformas != null) {
            if (!listPryPlataformas.isEmpty()) {
                pryPlataformaSeleccionada = listPryPlataformas.get(0);
            }
        }
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "pry_plataforma";
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

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPryPlataformas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(PryPlataformas pryPlataforma, int celda) {
        if (permitirIndex == true) {
            pryPlataformaSeleccionada = pryPlataforma;
            cualCelda = celda;
            pryPlataformaSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                pryPlataformaSeleccionada.getCodigo();
            }
            if (cualCelda == 1) {
                backUpDescripcion = pryPlataformaSeleccionada.getDescripcion();
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
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
        RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
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
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            observacion = (Column) c.getViewRoot().findComponent("form:datosPrtPlataforma:observacion");
            observacion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            log.info("Desactivar");
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

    public void modificarEvalPlataforma(PryPlataformas pryPlataforma) {
        pryPlataformaSeleccionada = pryPlataforma;

        if (!crearPryPlataformas.contains(pryPlataformaSeleccionada)) {
            if (modificarPryPlataformas.isEmpty()) {
                modificarPryPlataformas.add(pryPlataformaSeleccionada);
            } else if (!modificarPryPlataformas.contains(pryPlataformaSeleccionada)) {
                modificarPryPlataformas.add(pryPlataformaSeleccionada);
            }
        }

        if (guardado == true) {
            guardado = false;
        }

        RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoPryPlataformas() {

        if (pryPlataformaSeleccionada != null) {
            log.info("Entro a borrandoPryPlataformas");
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
            }
            contarRegistros();
            pryPlataformaSeleccionada = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void verificarBorrado() {
        log.info("Estoy en verificarBorrado");

        BigInteger proyectos;
        try {
            log.error("Control Secuencia de ControlEvalCompetencias ");
            if (tipoLista == 0) {
                proyectos = administrarPryPlataformas.contarProyectosPryPlataformas(pryPlataformaSeleccionada.getSecuencia());
            } else {
                proyectos = administrarPryPlataformas.contarProyectosPryPlataformas(pryPlataformaSeleccionada.getSecuencia());
            }
            if (proyectos.equals(new BigInteger("0"))) {
                log.info("Borrado==0");
                borrandoPryPlataformas();
            } else {
                log.info("Borrado>0");
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                proyectos = new BigInteger("-1");

            }
        } catch (Exception e) {
            log.error("ERROR ControlPryPlataformas verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarPryPlataformas.isEmpty() || !crearPryPlataformas.isEmpty() || !modificarPryPlataformas.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarYSalir() {
        guardarPryPlataformas();
        salir();
    }

    public void guardarPryPlataformas() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("Realizando guardarPryPlataformas");
            if (!borrarPryPlataformas.isEmpty()) {
                administrarPryPlataformas.borrarPryPlataformas(borrarPryPlataformas);

                //mostrarBorrados
                registrosBorrados = borrarPryPlataformas.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
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
            log.info("Se guardaron los datos con exito");
            listPryPlataformas = null;
            RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosPryCliente");
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
            log.info("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editObservacion");
                RequestContext.getCurrentInstance().execute("PF('editObservacion').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
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
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listPryPlataformas.size(); x++) {
                if (listPryPlataformas.get(x).getCodigo() == nuevoPryPlataforma.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está en uso. Por favor ingrese un código válido";
            } else {
                log.info("bandera");
                contador++;
            }
        }
        if (nuevoPryPlataforma.getDescripcion() == (null)) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            log.info("bandera");
            contador++;

        }

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                log.info("Desactivar");
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
            log.info("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoPryPlataforma.setSecuencia(l);
            crearPryPlataformas.add(nuevoPryPlataforma);
            listPryPlataformas.add(nuevoPryPlataforma);
            pryPlataformaSeleccionada = nuevoPryPlataforma;
            nuevoPryPlataforma = new PryPlataformas();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPryPlataforma').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoPrPlat");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoPrPlat').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoPryPlataformas() {
        log.info("limpiarNuevoPryPlataformas");
        nuevoPryPlataforma = new PryPlataformas();
        pryPlataformaSeleccionada = null;

    }

    //------------------------------------------------------------------------------
    public void duplicandoPryPlataformas() {
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("duplicandoPryPlataformas");
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

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPPL");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPryPlataforma').show()");
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
        if (duplicarPryPlataforma.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listPryPlataformas.size(); x++) {
                if (listPryPlataformas.get(x).getCodigo() == duplicarPryPlataforma.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está en uso. Por favor ingrese un código válido";
            } else {
                log.info("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarPryPlataforma.getDescripcion().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";

        } else {
            log.info("Bandera : ");
            contador++;
        }
        if (contador == 2) {

            log.info("Datos Duplicando: " + duplicarPryPlataforma.getSecuencia() + "  " + duplicarPryPlataforma.getCodigo());
            if (crearPryPlataformas.contains(duplicarPryPlataforma)) {
                log.info("Ya lo contengo.");
            }
            listPryPlataformas.add(duplicarPryPlataforma);
            crearPryPlataformas.add(duplicarPryPlataforma);
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosPrtPlataforma");
            pryPlataformaSeleccionada = duplicarPryPlataforma;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
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
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPryPlataforma').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarPrPlat");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarPrPlat').show()");
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
            log.info("resultado: " + resultado);
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
        } else if (administrarRastros.verificarHistoricosTabla("PRY_PLATAFORMAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        try {
            log.info("\n ENTRE A ControlPryPlataformas.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            log.warn("Error ControlPryPlataformas eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
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
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosPrtPlataforma");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
