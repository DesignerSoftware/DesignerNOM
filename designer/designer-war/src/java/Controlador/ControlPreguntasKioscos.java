/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.PreguntasKioskos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPreguntasKioscosInterface;
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
@Named(value = "controlPreguntasKioscos")
@SessionScoped
public class ControlPreguntasKioscos implements Serializable {

   private static Logger log = Logger.getLogger(ControlPreguntasKioscos.class);

    @EJB
    AdministrarPreguntasKioscosInterface administrarPreguntas;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<PreguntasKioskos> listPreguntas;
    private List<PreguntasKioskos> listPreguntasFiltrar;
    private List<PreguntasKioskos> listPreguntasCrear;
    private List<PreguntasKioskos> listPreguntasModificar;
    private List<PreguntasKioskos> listPreguntasBorrar;
    private PreguntasKioskos nuevaPreguntaK;
    private PreguntasKioskos duplicarPreguntaK;
    private PreguntasKioskos editarPreguntaK;
    private PreguntasKioskos preguntaKSeleccionada;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column codigo, descripcion;
    private int registrosBorrados;
    private String mensajeValidacion;
    private int tamano;
    private boolean activarLov;
    private String infoRegistro;
    private DataTable tablaC;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlPreguntasKioscos() {
        listPreguntas = null;
        listPreguntasCrear = new ArrayList<PreguntasKioskos>();
        listPreguntasModificar = new ArrayList<PreguntasKioskos>();
        listPreguntasBorrar = new ArrayList<PreguntasKioskos>();
        editarPreguntaK = new PreguntasKioskos();
        nuevaPreguntaK = new PreguntasKioskos();
        duplicarPreguntaK = new PreguntasKioskos();
        guardado = true;
        tamano = 330;
        cualCelda = -1;
        preguntaKSeleccionada = null;
        activarLov = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPreguntas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listPreguntas = null;
        getListPreguntas();
        if (listPreguntas != null) {
            preguntaKSeleccionada = listPreguntas.get(0);
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listPreguntas = null;
        getListPreguntas();
        if (listPreguntas != null) {
            preguntaKSeleccionada = listPreguntas.get(0);
        }
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "kiopreguntas";
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

    public String redirigir() {
        return paginaAnterior;
    }

    public void cambiarIndice(PreguntasKioskos pregunta, int celda) {
        preguntaKSeleccionada = pregunta;
        cualCelda = celda;
        preguntaKSeleccionada.getSecuencia();
        if (cualCelda == 0) {
            preguntaKSeleccionada.getCodigo();
        } else if (cualCelda == 1) {
            preguntaKSeleccionada.getPregunta();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosPreguntas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPreguntas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listPreguntasFiltrar = null;
            tamano = 330;
            RequestContext.getCurrentInstance().update("form:datosPreguntas");
            tipoLista = 0;
        }

        listPreguntasBorrar.clear();
        listPreguntasCrear.clear();
        listPreguntasModificar.clear();
        preguntaKSeleccionada = null;
        contarRegistros();
        k = 0;
        listPreguntas = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosPreguntas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosPreguntas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPreguntas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPreguntas");
            bandera = 0;
            listPreguntasFiltrar = null;
            tipoLista = 0;
            tamano = 330;
        }
        listPreguntasBorrar.clear();
        listPreguntasCrear.clear();
        listPreguntasModificar.clear();
        preguntaKSeleccionada = null;
        k = 0;
        listPreguntas = null;
        guardado = true;
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 310;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPreguntas:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPreguntas:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosPreguntas");
            bandera = 1;
        } else if (bandera == 1) {
            log.info("Desactivar");
            tamano = 330;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPreguntas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPreguntas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPreguntas");
            bandera = 0;
            listPreguntasFiltrar = null;
            tipoLista = 0;
        }
    }
    
    public void modificarPreguntaK(PreguntasKioskos pregunta) {
        preguntaKSeleccionada = pregunta;
        if (!listPreguntasCrear.contains(preguntaKSeleccionada)) {
            if (listPreguntasModificar.isEmpty()) {
                listPreguntasModificar.add(preguntaKSeleccionada);
            } else if (!listPreguntasModificar.contains(preguntaKSeleccionada)) {
                listPreguntasModificar.add(preguntaKSeleccionada);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosPreguntas");
    }

    public void borrarPreguntaKiosco() {
        if (preguntaKSeleccionada != null) {
            if (!listPreguntasModificar.isEmpty() && listPreguntasModificar.contains(preguntaKSeleccionada)) {
                listPreguntasModificar.remove(listPreguntasModificar.indexOf(preguntaKSeleccionada));
                listPreguntasBorrar.add(preguntaKSeleccionada);
            } else if (!listPreguntasCrear.isEmpty() && listPreguntasCrear.contains(preguntaKSeleccionada)) {
                listPreguntasCrear.remove(listPreguntasCrear.indexOf(preguntaKSeleccionada));
            } else {
                listPreguntasBorrar.add(preguntaKSeleccionada);
            }
            listPreguntas.remove(preguntaKSeleccionada);
            if (tipoLista == 1) {
                listPreguntasFiltrar.remove(preguntaKSeleccionada);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosPreguntas");
            contarRegistros();
            preguntaKSeleccionada = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!listPreguntasBorrar.isEmpty() || !listPreguntasCrear.isEmpty() || !listPreguntasModificar.isEmpty()) {
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listPreguntasBorrar.isEmpty()) {
                    administrarPreguntas.borrarPreguntasKioskos(listPreguntasBorrar);
                    registrosBorrados = listPreguntasBorrar.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    listPreguntasBorrar.clear();
                }
                if (!listPreguntasModificar.isEmpty()) {
                    administrarPreguntas.modificarPreguntasKioskos(listPreguntasModificar);
                    listPreguntasModificar.clear();
                }
                if (!listPreguntasCrear.isEmpty()) {
                    administrarPreguntas.crearPreguntasKioskos(listPreguntasCrear);
                    listPreguntasCrear.clear();
                }
                listPreguntas = null;
                getListPreguntas();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                preguntaKSeleccionada = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosPreguntas");
        } catch (Exception e) {
            log.warn("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void editarCelda() {
        if (preguntaKSeleccionada != null) {
            editarPreguntaK = preguntaKSeleccionada;

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

    public void agregarNuevaPreguntaK() {
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (nuevaPreguntaK.getPregunta() == null || nuevaPreguntaK.getPregunta().equals(" ") || nuevaPreguntaK.getPregunta().equals("")) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }
        if (nuevaPreguntaK.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
            contador++;
        }

        for (int i = 0; i < listPreguntas.size(); i++) {
            if (listPreguntas.get(i).getCodigo() == nuevaPreguntaK.getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                duplicados++;
            }
        }

        if (contador != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaPregunta");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaPregunta').show()");
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                log.info("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosPreguntas:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosPreguntas:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listPreguntasFiltrar = null;
                tipoLista = 0;
                tamano = 330;
                RequestContext.getCurrentInstance().update("form:datosPreguntas");
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaPreguntaK.setSecuencia(l);
            listPreguntasCrear.add(nuevaPreguntaK);
            listPreguntas.add(nuevaPreguntaK);
            contarRegistros();
            preguntaKSeleccionada = nuevaPreguntaK;
            nuevaPreguntaK = new PreguntasKioskos();
            RequestContext.getCurrentInstance().update("form:datosPreguntas");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPreguntaK').hide()");
        }
    }

    public void limpiarNuevaPreguntaK() {
        nuevaPreguntaK = new PreguntasKioskos();
    }

    public void duplicandoPreguntaK() {
        if (preguntaKSeleccionada != null) {
            duplicarPreguntaK = new PreguntasKioskos();
            k++;
            l = BigInteger.valueOf(k);

            duplicarPreguntaK.setSecuencia(l);
            duplicarPreguntaK.setCodigo(preguntaKSeleccionada.getCodigo());
            duplicarPreguntaK.setPregunta(preguntaKSeleccionada.getPregunta());

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPreguntaK').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        RequestContext context = RequestContext.getCurrentInstance();
        int contador = 0;

        for (int i = 0; i < listPreguntas.size(); i++) {
            if (duplicarPreguntaK.getCodigo() == listPreguntas.get(i).getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                contador++;
            }
        }

        if (contador == 0) {
            listPreguntas.add(duplicarPreguntaK);
            listPreguntasCrear.add(duplicarPreguntaK);
            preguntaKSeleccionada = duplicarPreguntaK;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosPreguntas");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosPreguntas:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosPreguntas:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                listPreguntasFiltrar = null;
                RequestContext.getCurrentInstance().update("form:datosPreguntas");
                tipoLista = 0;
            }
            duplicarPreguntaK = new PreguntasKioskos();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroPreguntaK");
        RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPreguntaK').hide()");
    }

    public void limpiarDuplicarPreguntaK() {
        duplicarPreguntaK = new PreguntasKioskos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPreguntasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "PreguntasKioscos", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPreguntasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "PreguntasKioscos", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (preguntaKSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(preguntaKSeleccionada.getSecuencia(), "PREGUNTASKIOSKOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("PREGUNTASKIOSKOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    /////////SETS Y GETS////////////
    public List<PreguntasKioskos> getListPreguntas() {
        if (listPreguntas == null) {
            listPreguntas = administrarPreguntas.consultarPreguntasKioskos();
        }
        return listPreguntas;
    }

    public void setListPreguntas(List<PreguntasKioskos> listPreguntas) {
        this.listPreguntas = listPreguntas;
    }

    public List<PreguntasKioskos> getListPreguntasFiltrar() {
        return listPreguntasFiltrar;
    }

    public void setListPreguntasFiltrar(List<PreguntasKioskos> listPreguntasFiltrar) {
        this.listPreguntasFiltrar = listPreguntasFiltrar;
    }

    public PreguntasKioskos getNuevaPreguntaK() {
        return nuevaPreguntaK;
    }

    public void setNuevaPreguntaK(PreguntasKioskos nuevaPreguntaK) {
        this.nuevaPreguntaK = nuevaPreguntaK;
    }

    public PreguntasKioskos getDuplicarPreguntaK() {
        return duplicarPreguntaK;
    }

    public void setDuplicarPreguntaK(PreguntasKioskos duplicarPreguntaK) {
        this.duplicarPreguntaK = duplicarPreguntaK;
    }

    public PreguntasKioskos getEditarPreguntaK() {
        return editarPreguntaK;
    }

    public void setEditarPreguntaK(PreguntasKioskos editarPreguntaK) {
        this.editarPreguntaK = editarPreguntaK;
    }

    public PreguntasKioskos getPreguntaKSeleccionada() {
        return preguntaKSeleccionada;
    }

    public void setPreguntaKSeleccionada(PreguntasKioskos preguntaKSeleccionada) {
        this.preguntaKSeleccionada = preguntaKSeleccionada;
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

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosPreguntas");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
    }
    
}
