/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.TiposJornadas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.log4j.Logger;
import InterfaceAdministrar.AdministrarTiposJornadasInterface;
import java.io.IOException;
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
public class ControlTiposJornadas implements Serializable {

    private static Logger log = Logger.getLogger(ControlTiposJornadas.class);
    @EJB
    AdministrarTiposJornadasInterface administrarTiposJornadas;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposJornadas> listTiposJornadas;
    private List<TiposJornadas> filtrarTiposJornadas;
    private List<TiposJornadas> crearTiposJornadas;
    private List<TiposJornadas> modificarTiposJornadas;
    private List<TiposJornadas> borrarTiposJornadas;
    private TiposJornadas nuevaTipoJornada;
    private TiposJornadas duplicarTipoJornada;
    private TiposJornadas editarTipoJornada;
    private TiposJornadas tipoJornadaSeleccionada;
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private Integer backUpCodigo, backUpMinicial, backUpHfinal, backUpMfinal, backUpHinicial;
    private String backUpDescripcion;
    private boolean aceptar, guardado;
    private Column codigo, descripcion, minutoinicial, horafinal, minutofinal, horainicial;
    private int registrosBorrados;
    private String mensajeValidacion;
    private Integer a;
    private BigInteger secRegistro;
    private int tamano;
    private String infoRegistro;
    private String msgError;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<>();
    private BigInteger contadorTipoJornada;

    public ControlTiposJornadas() {
        listTiposJornadas = null;
        crearTiposJornadas = new ArrayList<TiposJornadas>();
        modificarTiposJornadas = new ArrayList<TiposJornadas>();
        borrarTiposJornadas = new ArrayList<TiposJornadas>();
        editarTipoJornada = new TiposJornadas();
        nuevaTipoJornada = new TiposJornadas();
        duplicarTipoJornada = new TiposJornadas();
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
        String pagActual = "tiposanexos";
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
            administrarTiposJornadas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
            listTiposJornadas = null;
            getListTiposJornadas();
            if (listTiposJornadas != null) {
                tipoJornadaSeleccionada = listTiposJornadas.get(0);
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

    public void cambiarIndice(TiposJornadas tiposAnexos, int celda) {

        tipoJornadaSeleccionada = tiposAnexos;
        cualCelda = celda;
        tipoJornadaSeleccionada.getSecuencia();
        switch (cualCelda) {
            case 0:
                tipoJornadaSeleccionada.getCodigo();
                break;
            case 1:
                tipoJornadaSeleccionada.getDescripcion();
                break;
            case 2:
                tipoJornadaSeleccionada.getHorainicial();
                break;
            case 3:
                tipoJornadaSeleccionada.getMinutoinicial();
                break;
            case 4:
                tipoJornadaSeleccionada.getHorafinal();
                break;
            case 5:
                tipoJornadaSeleccionada.getMinutofinal();
                break;
            default:
                break;
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cerrarFiltrar() {
        tamano = 335;
        codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposJornadas:codigo");
        codigo.setFilterStyle("display: none; visibility: hidden;");
        descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposJornadas:descripcion");
        descripcion.setFilterStyle("display: none; visibility: hidden;");
        horainicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposJornadas:horaInicial");
        horainicial.setFilterStyle("display: none; visibility: hidden;");
        minutoinicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposJornadas:minutoInicial");
        minutoinicial.setFilterStyle("display: none; visibility: hidden;");
        horafinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposJornadas:horaFinal");
        horafinal.setFilterStyle("display: none; visibility: hidden;");
        minutofinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposJornadas:minutoFinal");
        minutofinal.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:datosTiposJornadas");
        bandera = 0;
        filtrarTiposJornadas = null;
        tipoLista = 0;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            cerrarFiltrar();
        }
        borrarTiposJornadas.clear();
        crearTiposJornadas.clear();
        modificarTiposJornadas.clear();
        tipoJornadaSeleccionada = null;
        k = 0;
        listTiposJornadas = null;
        getListTiposJornadas();
        contarRegistros();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposJornadas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            cerrarFiltrar();
        }

        borrarTiposJornadas.clear();
        crearTiposJornadas.clear();
        modificarTiposJornadas.clear();
        tipoJornadaSeleccionada = null;
        k = 0;
        listTiposJornadas = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposJornadas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            tamano = 315;
            codigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposJornadas:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposJornadas:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            horainicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposJornadas:horaInicial");
            horainicial.setFilterStyle("width: 85% !important");
            minutoinicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposJornadas:minutoInicial");
            minutoinicial.setFilterStyle("width: 85% !important");
            horafinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposJornadas:horaFinal");
            horafinal.setFilterStyle("width: 85% !important");
            minutofinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosTiposJornadas:minutoFinal");
            minutofinal.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosTiposJornadas");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrar();
        }
    }

    public void modificarTipoJornada(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int contador = 0;
        a = null;
        boolean banderita = false;
        RequestContext context = RequestContext.getCurrentInstance();
        backUpCodigo = listTiposJornadas.get(indice).getCodigo();
        backUpDescripcion = listTiposJornadas.get(indice).getDescripcion();
        backUpHinicial = listTiposJornadas.get(indice).getHorainicial();
        backUpMinicial = listTiposJornadas.get(indice).getMinutoinicial();
        backUpHfinal = listTiposJornadas.get(indice).getHorafinal();
        backUpMfinal = listTiposJornadas.get(indice).getMinutofinal();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!crearTiposJornadas.contains(listTiposJornadas.get(indice))) {
                    if (listTiposJornadas.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposJornadas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposJornadas.size(); j++) {
                            if (j != indice) {
                                if (listTiposJornadas.get(indice).getCodigo().equals(listTiposJornadas.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposJornadas.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposJornadas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposJornadas.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposJornadas.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposJornadas.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (listTiposJornadas.get(indice).getHorainicial() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposJornadas.get(indice).setHorainicial(backUpHinicial);
                    } else if (listTiposJornadas.get(indice).getHorainicial() > 24) {
                        mensajeValidacion = "LA HORA INICIAL DEBE ESTAR EN EL RANGO DE 0 - 24";
                        banderita = false;
                        listTiposJornadas.get(indice).setHorainicial(backUpHinicial);
                    }

                    if (listTiposJornadas.get(indice).getMinutoinicial() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposJornadas.get(indice).setMinutoinicial(backUpMinicial);
                    } else if (listTiposJornadas.get(indice).getMinutoinicial() > 59) {
                        mensajeValidacion = "EL MINUTO INICIAL DEBE ESTAR EN EL RANGO DE 0 - 59";
                        banderita = false;
                        listTiposJornadas.get(indice).setMinutoinicial(backUpMinicial);
                    }
                    if (listTiposJornadas.get(indice).getHorafinal() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposJornadas.get(indice).setHorafinal(backUpHfinal);
                    } else if (listTiposJornadas.get(indice).getHorafinal() > 24) {
                        mensajeValidacion = "LA HORA FINAL DEBE ESTAR EN EL RANGO DE 0 - 24";
                        banderita = false;
                        listTiposJornadas.get(indice).setHorafinal(backUpHfinal);
                    }

                    if (listTiposJornadas.get(indice).getMinutofinal() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposJornadas.get(indice).setMinutofinal(backUpMfinal);
                    } else if (listTiposJornadas.get(indice).getMinutofinal() > 59) {
                        mensajeValidacion = "EL MINUTO FINAL DEBE ESTAR EN EL RANGO DE 0 - 59";
                        banderita = false;
                        listTiposJornadas.get(indice).setMinutofinal(backUpMfinal);
                    }

                    if (banderita == true) {
                        if (modificarTiposJornadas.isEmpty()) {
                            modificarTiposJornadas.add(listTiposJornadas.get(indice));
                        } else if (!modificarTiposJornadas.contains(listTiposJornadas.get(indice))) {
                            modificarTiposJornadas.add(listTiposJornadas.get(indice));
                        }
                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listTiposJornadas.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposJornadas.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposJornadas.size(); j++) {
                            if (j != indice) {
                                if (listTiposJornadas.get(indice).getCodigo().equals(listTiposJornadas.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposJornadas.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposJornadas.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposJornadas.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (listTiposJornadas.get(indice).getMinutoinicial() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposJornadas.get(indice).setMinutoinicial(backUpMinicial);
                    } else if (listTiposJornadas.get(indice).getMinutoinicial() > 59) {
                        mensajeValidacion = "EL MINUTO INICIAL DEBE ESTAR EN EL RANGO DE 0 - 59";
                        banderita = false;
                        listTiposJornadas.get(indice).setMinutoinicial(backUpMinicial);
                    }
                    if (listTiposJornadas.get(indice).getHorafinal() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposJornadas.get(indice).setHorafinal(backUpHfinal);
                    } else if (listTiposJornadas.get(indice).getHorafinal() > 24) {
                        mensajeValidacion = "LA HORA FINAL DEBE ESTAR EN EL RANGO DE 0 - 24";
                        banderita = false;
                        listTiposJornadas.get(indice).setHorafinal(backUpHfinal);
                    }

                    if (listTiposJornadas.get(indice).getMinutofinal() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposJornadas.get(indice).setMinutofinal(backUpMfinal);
                    } else if (listTiposJornadas.get(indice).getMinutofinal() > 59) {
                        mensajeValidacion = "EL MINUTO FINAL DEBE ESTAR EN EL RANGO DE 0 - 59";
                        banderita = false;
                        listTiposJornadas.get(indice).setMinutofinal(backUpMfinal);
                    }
                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else if (!crearTiposJornadas.contains(filtrarTiposJornadas.get(indice))) {
                if (filtrarTiposJornadas.get(indice).getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposJornadas.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < filtrarTiposJornadas.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposJornadas.get(indice).getCodigo().equals(filtrarTiposJornadas.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarTiposJornadas.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarTiposJornadas.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setDescripcion(backUpDescripcion);
                }

                if (filtrarTiposJornadas.get(indice).getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setDescripcion(backUpDescripcion);
                }

                if (filtrarTiposJornadas.get(indice).getHorainicial() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setHorainicial(backUpHinicial);
                } else if (filtrarTiposJornadas.get(indice).getHorainicial() > 24) {
                    mensajeValidacion = "LA HORA INICIAL DEBE ESTAR EN EL RANGO DE 0 - 24";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setHorainicial(backUpHinicial);
                }

                if (filtrarTiposJornadas.get(indice).getMinutoinicial() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setMinutoinicial(backUpMinicial);
                } else if (filtrarTiposJornadas.get(indice).getMinutoinicial() > 59) {
                    mensajeValidacion = "EL MINUTO INICIAL DEBE ESTAR EN EL RANGO DE 0 - 59";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setMinutoinicial(backUpMinicial);
                }
                if (filtrarTiposJornadas.get(indice).getHorafinal() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setHorafinal(backUpHfinal);
                } else if (filtrarTiposJornadas.get(indice).getHorafinal() > 24) {
                    mensajeValidacion = "LA HORA FINAL DEBE ESTAR EN EL RANGO DE 0 - 24";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setHorafinal(backUpHfinal);
                }

                if (filtrarTiposJornadas.get(indice).getMinutofinal() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setMinutofinal(backUpMfinal);
                } else if (filtrarTiposJornadas.get(indice).getMinutofinal() > 59) {
                    mensajeValidacion = "EL MINUTO FINAL DEBE ESTAR EN EL RANGO DE 0 - 59";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setMinutofinal(backUpMfinal);
                }

                if (banderita == true) {
                    if (modificarTiposJornadas.isEmpty()) {
                        modificarTiposJornadas.add(filtrarTiposJornadas.get(indice));
                    } else if (!modificarTiposJornadas.contains(filtrarTiposJornadas.get(indice))) {
                        modificarTiposJornadas.add(filtrarTiposJornadas.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
                index = -1;
                secRegistro = null;
            } else {
                if (filtrarTiposJornadas.get(indice).getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposJornadas.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < filtrarTiposJornadas.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposJornadas.get(indice).getCodigo().equals(filtrarTiposJornadas.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarTiposJornadas.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarTiposJornadas.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setDescripcion(backUpDescripcion);
                }
                if (filtrarTiposJornadas.get(indice).getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setDescripcion(backUpDescripcion);
                }
                if (filtrarTiposJornadas.get(indice).getMinutoinicial() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setMinutoinicial(backUpMinicial);
                } else if (filtrarTiposJornadas.get(indice).getMinutoinicial() > 59) {
                    mensajeValidacion = "EL MINUTO INICIAL DEBE ESTAR EN EL RANGO DE 0 - 59";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setMinutoinicial(backUpMinicial);
                }
                if (filtrarTiposJornadas.get(indice).getHorafinal() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setHorafinal(backUpHfinal);
                } else if (filtrarTiposJornadas.get(indice).getHorafinal() > 24) {
                    mensajeValidacion = "LA HORA FINAL DEBE ESTAR EN EL RANGO DE 0 - 24";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setHorafinal(backUpHfinal);
                }

                if (filtrarTiposJornadas.get(indice).getMinutofinal() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setMinutofinal(backUpMfinal);
                } else if (filtrarTiposJornadas.get(indice).getMinutofinal() > 59) {
                    mensajeValidacion = "EL MINUTO FINAL DEBE ESTAR EN EL RANGO DE 0 - 59";
                    banderita = false;
                    filtrarTiposJornadas.get(indice).setMinutofinal(backUpMfinal);
                }
                if (banderita == true) {
                    if (guardado == true) {
                        guardado = false;
                    }
                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosTiposJornadas");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrarTiposJornadas() {
        if (tipoJornadaSeleccionada != null) {
            if (!modificarTiposJornadas.isEmpty() && modificarTiposJornadas.contains(tipoJornadaSeleccionada)) {
                int modIndex = modificarTiposJornadas.indexOf(tipoJornadaSeleccionada);
                modificarTiposJornadas.remove(modIndex);
                borrarTiposJornadas.add(tipoJornadaSeleccionada);
            } else if (!crearTiposJornadas.isEmpty() && crearTiposJornadas.contains(tipoJornadaSeleccionada)) {
                int crearIndex = crearTiposJornadas.indexOf(tipoJornadaSeleccionada);
                crearTiposJornadas.remove(crearIndex);
            } else {
                borrarTiposJornadas.add(tipoJornadaSeleccionada);
            }
            listTiposJornadas.remove(tipoJornadaSeleccionada);
            if (tipoLista == 1) {
                filtrarTiposJornadas.remove(tipoJornadaSeleccionada);

            }
            tipoJornadaSeleccionada = null;
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:datosTiposJornadas");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarTiposJornadas.isEmpty() || !crearTiposJornadas.isEmpty() || !modificarTiposJornadas.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarTiposJornadas() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                msgError = "";
                if (!borrarTiposJornadas.isEmpty()) {
                    for (int i = 0; i < borrarTiposJornadas.size(); i++) {
                        msgError = administrarTiposJornadas.borrarTipoJornada(borrarTiposJornadas.get(i));
                    }
                    registrosBorrados = borrarTiposJornadas.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    borrarTiposJornadas.clear();
                }
                if (!crearTiposJornadas.isEmpty()) {
                    for (int i = 0; i < crearTiposJornadas.size(); i++) {
                        msgError = administrarTiposJornadas.crearTipoJornada(crearTiposJornadas.get(i));
                    }
                    crearTiposJornadas.clear();
                }
                if (!modificarTiposJornadas.isEmpty()) {
                    for (int i = 0; i < modificarTiposJornadas.size(); i++) {
                        msgError = administrarTiposJornadas.modificarTipoJornada(modificarTiposJornadas.get(i));
                    }
                    modificarTiposJornadas.clear();
                }
                if (msgError.equals("EXITO")) {

                    listTiposJornadas = null;
                    getListTiposJornadas();
                    k = 0;
                    FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                    FacesContext.getCurrentInstance().addMessage(null, msg);
                    RequestContext.getCurrentInstance().update("form:growl");
                    contarRegistros();
                    guardado = true;
                    tipoJornadaSeleccionada = null;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    RequestContext.getCurrentInstance().update("form:datosTiposJornadas");
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
        if (tipoJornadaSeleccionada != null) {
            editarTipoJornada = tipoJornadaSeleccionada;
            switch (cualCelda) {
                case 0:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                    RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                    cualCelda = -1;
                    break;
                case 1:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                    RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                    cualCelda = -1;
                    break;
                case 2:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editHoraI");
                    RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                    cualCelda = -1;
                    break;
                case 3:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editMinutoI");
                    RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                    cualCelda = -1;
                    break;
                case 4:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editHoraF");
                    RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                    cualCelda = -1;
                    break;
                case 5:
                    RequestContext.getCurrentInstance().update("formularioDialogos:editMinutoF");
                    RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                    cualCelda = -1;
                    break;
                default:
                    break;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoTiposJornadas() {
        log.info("nuevoRegistroTiposJornadas");
        int contador = 0;
        int duplicados = 0;
        int rango = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaTipoJornada.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios.";
        } else {
            for (int x = 0; x < listTiposJornadas.size(); x++) {
                if (listTiposJornadas.get(x).getCodigo().equals(nuevaTipoJornada.getCodigo())) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
            }
        }
        if (nuevaTipoJornada.getDescripcion() == null || nuevaTipoJornada.getDescripcion().isEmpty() || nuevaTipoJornada.getDescripcion().equals(" ")) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else {
            contador++;
        }
        if (nuevaTipoJornada.getHorainicial() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios.";
        } else if (nuevaTipoJornada.getHorainicial() > 24) {
            mensajeValidacion = "La hora inicial destar en el rango de 0 - 24";
        } else {
            contador++;
        }
        if (nuevaTipoJornada.getMinutoinicial() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else if (nuevaTipoJornada.getMinutoinicial() > 59) {
            mensajeValidacion = "El minuto Inicial debe estar en el rango 0 a 59";
        } else {
            contador++;
        }
        if (nuevaTipoJornada.getHorafinal() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else if (nuevaTipoJornada.getHorafinal() > 24) {
            mensajeValidacion = "La hora final debe estar en el rango 0 a 24";
        } else {
            contador++;
        }
        if (nuevaTipoJornada.getMinutofinal() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else if (nuevaTipoJornada.getMinutofinal() > 59) {
            mensajeValidacion = "El minuto final debe estar en el rango 0 a 59";
        } else {
            contador++;
        }

        if (contador == 6) {
            if (bandera == 1) {
                //CERRAR FILTRADO
                cerrarFiltrar();
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevaTipoJornada.setSecuencia(l);
            crearTiposJornadas.add(nuevaTipoJornada);
            listTiposJornadas.add(0, nuevaTipoJornada);
            tipoJornadaSeleccionada = nuevaTipoJornada;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposJornadas').hide()");
            RequestContext.getCurrentInstance().update("form:datosTiposJornadas");
            nuevaTipoJornada = new TiposJornadas();

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposJornadas() {
        log.info("limpiarNuevoTiposJornadas()");
        nuevaTipoJornada = new TiposJornadas();
    }

    public void duplicarTiposJornadas() {
        if (tipoJornadaSeleccionada != null) {
            duplicarTipoJornada = new TiposJornadas();
            k++;
            l = BigInteger.valueOf(k);
            duplicarTipoJornada.setSecuencia(l);
            duplicarTipoJornada.setCodigo(tipoJornadaSeleccionada.getCodigo());
            duplicarTipoJornada.setDescripcion(tipoJornadaSeleccionada.getDescripcion());
            duplicarTipoJornada.setHorainicial(tipoJornadaSeleccionada.getHorainicial());
            duplicarTipoJornada.setMinutoinicial(tipoJornadaSeleccionada.getMinutoinicial());
            duplicarTipoJornada.setHorafinal(tipoJornadaSeleccionada.getHorafinal());
            duplicarTipoJornada.setMinutofinal(tipoJornadaSeleccionada.getMinutofinal());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoJornada");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTipoJornada').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        if (duplicarTipoJornada.getCodigo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listTiposJornadas.size(); x++) {
                if (listTiposJornadas.get(x).getCodigo() == duplicarTipoJornada.getCodigo()) {
                    duplicados++;
                }
            }
            System.out.println("duplicados: " + duplicados);
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está registrado. Por favor ingrese un código válido";
            } else {
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarTipoJornada.getDescripcion() == null || duplicarTipoJornada.getDescripcion().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }
        if (duplicarTipoJornada.getHorainicial() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios.";
        } else if (duplicarTipoJornada.getHorainicial() > 24) {
            mensajeValidacion = "La hora inicial destar en el rango de 0 - 24";
        } else {
            contador++;
        }
        if (duplicarTipoJornada.getMinutoinicial() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else if (duplicarTipoJornada.getMinutoinicial() > 59) {
            mensajeValidacion = "El minuto Inicial debe estar en el rango 0 a 59";
        } else {
            contador++;
        }
        if (duplicarTipoJornada.getHorafinal() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else if (duplicarTipoJornada.getHorafinal() > 24) {
            mensajeValidacion = "La hora final debe estar en el rango 0 a 24";
        } else {
            contador++;
        }
        if (duplicarTipoJornada.getMinutofinal() == null) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios ";
        } else if (duplicarTipoJornada.getMinutofinal() > 59) {
            mensajeValidacion = "El minuto final debe estar en el rango 0 a 59";
        } else {
            contador++;
        }
        if (contador == 6) {
            listTiposJornadas.add(duplicarTipoJornada);
            crearTiposJornadas.add(duplicarTipoJornada);
            RequestContext.getCurrentInstance().update("form:datosTiposJornadas");
            tipoJornadaSeleccionada = duplicarTipoJornada;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                cerrarFiltrar();
            }
            duplicarTipoJornada = new TiposJornadas();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTipoJornada').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposJornadas() {
        duplicarTipoJornada = new TiposJornadas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposJornadasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSJORNADAS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposJornadasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSJORNADAS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoJornadaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(tipoJornadaSeleccionada.getSecuencia(), "TIPOSANEXOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            switch (resultado) {
                case 1:
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                    break;
                case 2:
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
                    break;
                case 3:
                    RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
                    break;
                case 4:
                    RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
                    break;
                case 5:
                    RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
                    break;
                default:
                    break;
            }
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSJORNADAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    /**
     * ******************************************************************************************************
     * @return
     */
    public List<TiposJornadas> getListTiposJornadas() {
        if (listTiposJornadas == null) {
            listTiposJornadas = administrarTiposJornadas.consultarTipoJornada();
        }
        return listTiposJornadas;
    }

    public void setListTiposJornadas(List<TiposJornadas> listTiposJornadas) {
        this.listTiposJornadas = listTiposJornadas;
    }

    public List<TiposJornadas> getFiltrarTiposJornadas() {
        return filtrarTiposJornadas;
    }

    public void setFiltrarTiposJornadas(List<TiposJornadas> filtrarTiposJornadas) {
        this.filtrarTiposJornadas = filtrarTiposJornadas;
    }

    public TiposJornadas getNuevoTipoJornada() {
        return nuevaTipoJornada;
    }

    public void setNuevoTipoJornada(TiposJornadas nuevoTipoJornada) {
        this.nuevaTipoJornada = nuevoTipoJornada;
    }

    public TiposJornadas getDuplicarTipoJornada() {
        return duplicarTipoJornada;
    }

    public void setDuplicarTipoJornada(TiposJornadas duplicarTipoJornada) {
        this.duplicarTipoJornada = duplicarTipoJornada;
    }

    public TiposJornadas getEditarTipoJornada() {
        return editarTipoJornada;
    }

    public void setEditarTipoJornada(TiposJornadas editarTipoJornada) {
        this.editarTipoJornada = editarTipoJornada;
    }

    public TiposJornadas getTipoJornadasSeleccionado() {
        return tipoJornadaSeleccionada;
    }

    public void setTipoJornadasSeleccionado(TiposJornadas TipoJornadasSeleccionado) {
        this.tipoJornadaSeleccionada = TipoJornadasSeleccionado;
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

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposJornadas");
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
}
