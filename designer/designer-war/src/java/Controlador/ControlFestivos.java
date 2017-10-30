/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Paises;
import Entidades.Festivos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarFestivosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
public class ControlFestivos implements Serializable {

    private static Logger log = Logger.getLogger(ControlFestivos.class);

    @EJB
    AdministrarFestivosInterface administrarFestivos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Festivos> listFestivosPorPais;
    private List<Festivos> filtrarFestivosPorPais;
    private List<Festivos> crearFestivosPorPais;
    private List<Festivos> modificarFestivosPorPais;
    private List<Festivos> borrarFestivosPorPais;
    private Festivos nuevoFestivos;
    private Festivos duplicarFestivos;
    private Festivos editarFestivos;
    private Festivos festivoSeleccionado;
    private int cualCelda, cualCeldaPais, tipoLista, tipoActualizacion, k, bandera, cualTabla;
    private BigInteger l;
    private boolean aceptar, guardado;
    private Column fecha, codigo, nombre;
    private int registrosBorrados;
    private String mensajeValidacion;
    private List<Paises> listPaises;
    private List<Paises> listPaisesFiltrar;
    private Paises editarPais;
    private Paises paisSeleccionado;
    private int tamano, altoTablaPaises;
    private String infoRegistro, infoRegistroPaises, msgError;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlFestivos() {
        listFestivosPorPais = null;
        crearFestivosPorPais = new ArrayList<Festivos>();
        modificarFestivosPorPais = new ArrayList<Festivos>();
        borrarFestivosPorPais = new ArrayList<Festivos>();
        editarFestivos = new Festivos();
        nuevoFestivos = new Festivos();
        nuevoFestivos.setPais(new Paises());
        duplicarFestivos = new Festivos();
        guardado = true;
        tamano = 180;
        altoTablaPaises = 80;
        mapParametros.put("paginaAnterior", paginaAnterior);
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
        String pagActual = "festivo";

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
            administrarFestivos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listPaises = null;
            getListPaises();
            if (listPaises != null) {
                paisSeleccionado = listPaises.get(0);
            }
            listFestivosPorPais = null;
            getListFestivosPorPais();
            if (listFestivosPorPais != null) {
                if (!listFestivosPorPais.isEmpty()) {
                    festivoSeleccionado = listFestivosPorPais.get(0);
                }
            }
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPais() {

        if (paisSeleccionado != null) {
            listFestivosPorPais = null;
            listFestivosPorPais = administrarFestivos.consultarFestivosPais(paisSeleccionado.getSecuencia());
        }
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosFestivos");
    }

    public void cambiarIndice(Festivos festivo, int celda) {
        cualTabla = 1;
        festivoSeleccionado = festivo;
        cualCelda = celda;
        festivoSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            festivoSeleccionado.getDia();
        }
    }

    public void cambiarIndicePaises(Paises pais, int celda) {
        cualTabla = 2;
        paisSeleccionado = pais;
        cualCeldaPais = celda;
        paisSeleccionado.getSecuencia();
        if (cualCeldaPais == 0) {
            paisSeleccionado.getCodigo();
        } else if (cualCeldaPais == 1) {
            paisSeleccionado.getNombre();
        }
//        recibirPais();
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void anularCambios() {
        modificarFestivosPorPais = null;
        crearFestivosPorPais = null;
        borrarFestivosPorPais = null;
        listFestivosPorPais = null;
        recibirPais();
    }

    public void modificandoFestivos(Festivos festivo) {
        festivoSeleccionado = festivo;
        if (!crearFestivosPorPais.contains(festivoSeleccionado)) {
            if (modificarFestivosPorPais.isEmpty()) {
                modificarFestivosPorPais.add(festivoSeleccionado);
            } else if (!modificarFestivosPorPais.contains(festivoSeleccionado)) {
                modificarFestivosPorPais.add(festivoSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosFestivos");

        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            fecha = (Column) c.getViewRoot().findComponent("form:datosFestivos:dia");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            codigo = (Column) c.getViewRoot().findComponent("form:datosPaises:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosPaises:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarFestivosPorPais = null;
            tipoLista = 0;
            altoTablaPaises = 80;
            tamano = 80;
            RequestContext.getCurrentInstance().update("form:datosFestivos");
            RequestContext.getCurrentInstance().update("form:datosPaises");
        }

        borrarFestivosPorPais.clear();
        crearFestivosPorPais.clear();
        modificarFestivosPorPais.clear();
        paisSeleccionado = listPaises.get(0);
        festivoSeleccionado = null;
        k = 0;
        listFestivosPorPais = null;
        guardado = true;
        getListFestivosPorPais();
        contarRegistros();
        contarRegistrosPaises();
        RequestContext.getCurrentInstance().update("form:datosFestivos");
        RequestContext.getCurrentInstance().update("form:datosPaises");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        cancelarModificacion();
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 160;
            altoTablaPaises = 60;
            fecha = (Column) c.getViewRoot().findComponent("form:datosFestivos:dia");
            fecha.setFilterStyle("width: 85% !important;");
            codigo = (Column) c.getViewRoot().findComponent("form:datosPaises:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosPaises:nombre");
            nombre.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosFestivos");
            RequestContext.getCurrentInstance().update("form:datosPaises");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 180;
            altoTablaPaises = 80;
            log.info("Desactivar");
            fecha = (Column) c.getViewRoot().findComponent("form:datosFestivos:dia");
            fecha.setFilterStyle("display: none; visibility: hidden;");
            codigo = (Column) c.getViewRoot().findComponent("form:datosPaises:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosPaises:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosFestivos");
            RequestContext.getCurrentInstance().update("form:datosPaises");
            bandera = 0;
            filtrarFestivosPorPais = null;
            tipoLista = 0;
        }
    }

    public void limpiarNuevaNormaLaboral() {
        nuevoFestivos = new Festivos();
        nuevoFestivos.setPais(new Paises());
    }

    public void borrandoFestivos() {
        if (festivoSeleccionado != null) {
            if (!modificarFestivosPorPais.isEmpty() && modificarFestivosPorPais.contains(festivoSeleccionado)) {
                int modIndex = modificarFestivosPorPais.indexOf(festivoSeleccionado);
                modificarFestivosPorPais.remove(modIndex);
                borrarFestivosPorPais.add(festivoSeleccionado);
            } else if (!crearFestivosPorPais.isEmpty() && crearFestivosPorPais.contains(festivoSeleccionado)) {
                int crearIndex = crearFestivosPorPais.indexOf(festivoSeleccionado);
                crearFestivosPorPais.remove(crearIndex);
            } else {
                borrarFestivosPorPais.add(festivoSeleccionado);
            }
            listFestivosPorPais.remove(festivoSeleccionado);

            if (tipoLista == 1) {
                filtrarFestivosPorPais.remove(festivoSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            guardado = false;
            festivoSeleccionado = null;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosFestivos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void revisarDialogoGuardar() {

        if (!borrarFestivosPorPais.isEmpty() || !crearFestivosPorPais.isEmpty() || !modificarFestivosPorPais.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarFestivos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == false) {
            msgError = "";
            if (!borrarFestivosPorPais.isEmpty()) {
                for (int i = 0; i < borrarFestivosPorPais.size(); i++) {
                    msgError = administrarFestivos.borrarFestivos(borrarFestivosPorPais.get(i));
                }
                registrosBorrados = borrarFestivosPorPais.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarFestivosPorPais.clear();
            }
            if (!crearFestivosPorPais.isEmpty()) {
                for (int i = 0; i < crearFestivosPorPais.size(); i++) {
                    msgError = administrarFestivos.crearFestivos(crearFestivosPorPais.get(i));
                }
                crearFestivosPorPais.clear();
            }
            if (!modificarFestivosPorPais.isEmpty()) {
                for (int i = 0; i < borrarFestivosPorPais.size(); i++) {
                    msgError = administrarFestivos.modificarFestivos(modificarFestivosPorPais.get(i));
                }
                modificarFestivosPorPais.clear();
            }
            listFestivosPorPais = null;
            getListFestivosPorPais();
            festivoSeleccionado = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosFestivos");
            k = 0;
            guardado = true;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void editarCelda() {
        if (cualTabla == 1) {

            if (festivoSeleccionado != null) {
                editarFestivos = festivoSeleccionado;
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
                    RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
                    cualCelda = -1;
                }
            }
        } else if (cualTabla == 2) {
            if (paisSeleccionado != null) {
                editarPais = paisSeleccionado;
                if (cualCeldaPais == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigo");
                    RequestContext.getCurrentInstance().execute("PF('editarCodigo').show()");
                    cualCeldaPais = -1;
                }
                if (cualCeldaPais == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarNombre");
                    RequestContext.getCurrentInstance().execute("PF('editarNombre').show()");
                    cualCeldaPais = -1;
                }
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoFestivos() {
        int contador = 0;
        Short a = 0;
        a = null;
        int fechas = 0;
        mensajeValidacion = " ";
        nuevoFestivos.setPais(paisSeleccionado);
        if (nuevoFestivos.getDia() == null || nuevoFestivos.getDia().equals("")) {
            mensajeValidacion = "La fecha no pude estar nula o ser vacía";
        } else {
            for (int i = 0; i < listFestivosPorPais.size(); i++) {
                if (nuevoFestivos.getDia().equals(listFestivosPorPais.get(i).getDia())) {
                    fechas++;
                }
            }
            if (fechas > 0) {
                mensajeValidacion = "Existe un registro con la fecha ingresada. Por favor ingrese una nueva fecha";
            } else {
                contador++;
            }
        }

        if (contador == 1) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                log.info("Desactivar");
                fecha = (Column) c.getViewRoot().findComponent("form:datosFestivos:dia");
                fecha.setFilterStyle("display: none; visibility: hidden;");
                tamano = 180;
                RequestContext.getCurrentInstance().update("form:datosFestivos");
                bandera = 0;
                filtrarFestivosPorPais = null;
                tipoLista = 0;
            }

            k++;
            l = BigInteger.valueOf(k);
            nuevoFestivos.setSecuencia(l);
            crearFestivosPorPais.add(nuevoFestivos);
            listFestivosPorPais.add(0, nuevoFestivos);
            festivoSeleccionado = nuevoFestivos;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosFestivos");
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            nuevoFestivos = new Festivos();
            nuevoFestivos.setPais(new Paises());
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEvalEmpresas').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoFestivos() {
        nuevoFestivos = new Festivos();
        nuevoFestivos.setPais(new Paises());
    }

    public void duplicandoFestivos() {
        if (festivoSeleccionado != null) {
            duplicarFestivos = new Festivos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarFestivos.setSecuencia(l);
            duplicarFestivos.setPais(festivoSeleccionado.getPais());
            duplicarFestivos.setDia(festivoSeleccionado.getDia());

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEvC");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalCompetencias').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        int fechas = 0;
        a = null;
        if (duplicarFestivos.getDia() == null) {
            mensajeValidacion = "La fecha no pude estar nula o ser vacía";
        } else {
            for (int j = 0; j < listFestivosPorPais.size(); j++) {
                if (duplicarFestivos.getDia().equals(listFestivosPorPais.get(j).getDia())) {
                    fechas++;
                }
            }
            if (fechas > 0) {
                mensajeValidacion = "Existe un registro con la fecha ingresada. Por favor ingrese una nueva fecha";
            } else {
                contador++;
            }
        }

        if (contador == 1) {
            listFestivosPorPais.add(0, duplicarFestivos);
            crearFestivosPorPais.add(duplicarFestivos);
            festivoSeleccionado = duplicarFestivos;
            RequestContext.getCurrentInstance().update("form:datosFestivos");
            contarRegistros();;
            guardado = false;
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                fecha = (Column) c.getViewRoot().findComponent("form:datosFestivos:dia");
                fecha.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosFestivos");
                bandera = 0;
                filtrarFestivosPorPais = null;
                tamano = 180;
                tipoLista = 0;
            }
            duplicarFestivos = new Festivos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalCompetencias').hide()");

        } else {
            contador = 0;
            fechas = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void limpiarDuplicarFestivos() {
        duplicarFestivos = new Festivos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFestivosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "FESTIVOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFestivosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "FESTIVOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listFestivosPorPais.isEmpty()) {
            int resultado = administrarRastros.obtenerTabla(festivoSeleccionado.getSecuencia(), "FESTIVOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("FESTIVOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void contarRegistrosPaises() {
        RequestContext.getCurrentInstance().update("form:informacionRegistroPaises");
    }

    public void posicionGenerado() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        paisSeleccionado = listPaises.get(indice);
        cualCeldaPais = columna;
        cambiarIndicePaises(paisSeleccionado, cualCeldaPais);
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Festivos> getListFestivosPorPais() {
        try {
            if (listFestivosPorPais == null) {
                listFestivosPorPais = administrarFestivos.consultarFestivosPais(paisSeleccionado.getSecuencia());
            }
            return listFestivosPorPais;
        } catch (ArrayIndexOutOfBoundsException AiEX) {
            log.error("ListaFestivosPorPais vacía:", AiEX);
            return null;
        }
    }

    public void setListFestivosPorPais(List<Festivos> listFestivosPorPais) {
        this.listFestivosPorPais = listFestivosPorPais;
    }

    public List<Festivos> getFiltrarFestivosPorPais() {
        return filtrarFestivosPorPais;
    }

    public void setFiltrarFestivosPorPais(List<Festivos> filtrarFestivosPorPais) {
        this.filtrarFestivosPorPais = filtrarFestivosPorPais;
    }

    public Festivos getNuevoFestivos() {
        return nuevoFestivos;
    }

    public void setNuevoFestivos(Festivos nuevoFestivos) {
        this.nuevoFestivos = nuevoFestivos;
    }

    public Festivos getDuplicarFestivos() {
        return duplicarFestivos;
    }

    public void setDuplicarFestivos(Festivos duplicarFestivos) {
        this.duplicarFestivos = duplicarFestivos;
    }

    public Festivos getEditarFestivos() {
        return editarFestivos;
    }

    public void setEditarFestivos(Festivos editarFestivos) {
        this.editarFestivos = editarFestivos;
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

    public List<Paises> getListPaises() {
        if (listPaises == null) {
            listPaises = administrarFestivos.consultarLOVPaises();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        }
        return listPaises;
    }

    public void setListPaises(List<Paises> listPaises) {
        this.listPaises = listPaises;
    }

    public Paises getPaisSeleccionado() {
        return paisSeleccionado;
    }

    public void setPaisSeleccionado(Paises paisSeleccionado) {
        this.paisSeleccionado = paisSeleccionado;
    }

    public List<Paises> getListPaisesFiltrar() {
        return listPaisesFiltrar;
    }

    public void setListPaisesFiltrar(List<Paises> listPaisesFiltrar) {
        this.listPaisesFiltrar = listPaisesFiltrar;
    }

    public Festivos getFestivoSeleccionado() {
        return festivoSeleccionado;
    }

    public void setFestivoSeleccionado(Festivos festivoSeleccionado) {
        this.festivoSeleccionado = festivoSeleccionado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosFestivos");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroPaises() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosPaises");
        infoRegistroPaises = String.valueOf(tabla.getRowCount());
        return infoRegistroPaises;
    }

    public void setInfoRegistroPaises(String infoRegistroPaises) {
        this.infoRegistroPaises = infoRegistroPaises;
    }

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }

    public int getAltoTablaPaises() {
        return altoTablaPaises;
    }

    public void setAltoTablaPaises(int altoTablaPaises) {
        this.altoTablaPaises = altoTablaPaises;
    }

    public Paises getEditarPais() {
        return editarPais;
    }

    public void setEditarPais(Paises editarPais) {
        this.editarPais = editarPais;
    }

}
