/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.SoCondicionesAmbientalesP;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSoCondicionesAmbientalesPInterface;
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
public class ControlSoCondicionesAmbientalesP implements Serializable {

   private static Logger log = Logger.getLogger(ControlSoCondicionesAmbientalesP.class);

    @EJB
    AdministrarSoCondicionesAmbientalesPInterface administrarSoCondicionesAmbientalesP;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP;
    private List<SoCondicionesAmbientalesP> filtrarSoCondicionesAmbientalesP;
    private List<SoCondicionesAmbientalesP> crearSoCondicionesAmbientalesP;
    private List<SoCondicionesAmbientalesP> modificarSoCondicionesAmbientalesP;
    private List<SoCondicionesAmbientalesP> borrarSoCondicionesAmbientalesP;
    private SoCondicionesAmbientalesP nuevaSoCondicionAmbientalP;
    private SoCondicionesAmbientalesP duplicarSoCondicionAmbientalP;
    private SoCondicionesAmbientalesP editarSoCondicionAmbientalP;
    private SoCondicionesAmbientalesP soCondicionAmbientalPSeleccionado;
    private BigInteger verificarBorradoAccidentes;
    private int tamano;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;

    private String backUpCodigo;
    private String backUpDescripcion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlSoCondicionesAmbientalesP() {
        listSoCondicionesAmbientalesP = null;
        crearSoCondicionesAmbientalesP = new ArrayList<SoCondicionesAmbientalesP>();
        modificarSoCondicionesAmbientalesP = new ArrayList<SoCondicionesAmbientalesP>();
        borrarSoCondicionesAmbientalesP = new ArrayList<SoCondicionesAmbientalesP>();
        permitirIndex = true;
        editarSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
        nuevaSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
        duplicarSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
        guardado = true;
        tamano = 270;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }
   
   @PostConstruct
    public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSoCondicionesAmbientalesP.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
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
        /*if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
         
        } else {
            */
String pagActual = "condicionambientalpeligrosa";
            
            
            


            
            
            
            
            
            
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

    public void eventoFiltrar() {
        try {
            log.info("\n EVENTO FILTRAR \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarSoCondicionesAmbientalesP.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            log.warn("Error EVENTO FILTRAR ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        log.error("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listSoCondicionesAmbientalesP.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarSoCondicionesAmbientalesP.get(index).getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listSoCondicionesAmbientalesP.get(index).getDescripcion();
                } else {
                    backUpDescripcion = filtrarSoCondicionesAmbientalesP.get(index).getDescripcion();
                }
            }

            secRegistro = listSoCondicionesAmbientalesP.get(index).getSecuencia();

        }
        log.info("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            log.info("\n ENTRE CONTROLSOCONDICIONESAMBIENTALESP  AsignarIndex \n");
            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                log.info("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            log.warn("Error CONTROLSOCONDICIONESAMBIENTALESP asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }
    private String infoRegistro;

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            bandera = 0;
            filtrarSoCondicionesAmbientalesP = null;
            tipoLista = 0;
        }

        borrarSoCondicionesAmbientalesP.clear();
        crearSoCondicionesAmbientalesP.clear();
        modificarSoCondicionesAmbientalesP.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSoCondicionesAmbientalesP = null;
        guardado = true;
        permitirIndex = true;
        getListSoCondicionesAmbientalesP();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSoCondicionesAmbientalesP == null || listSoCondicionesAmbientalesP.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSoCondicionesAmbientalesP.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
    }

    public void salir() {  limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            bandera = 0;
            filtrarSoCondicionesAmbientalesP = null;
            tipoLista = 0;
        }

        borrarSoCondicionesAmbientalesP.clear();
        crearSoCondicionesAmbientalesP.clear();
        modificarSoCondicionesAmbientalesP.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listSoCondicionesAmbientalesP = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            bandera = 0;
            filtrarSoCondicionesAmbientalesP = null;
            tipoLista = 0;
        }
    }

    public void modificandoSoCondicionAmbientalP(int indice, String confirmarCambio, String valorConfirmar) {
        log.error("MODIFICAR  SO CONDICIONES AMBIENTALES P");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        Short a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        log.error("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            log.error("MODIFICANDO CLASE ACCIDENTE CONFIRMAR CAMBIO = N");
            if (tipoLista == 0) {
                if (!crearSoCondicionesAmbientalesP.contains(listSoCondicionesAmbientalesP.get(indice))) {
                    if (listSoCondicionesAmbientalesP.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                    } else if (listSoCondicionesAmbientalesP.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else if (listSoCondicionesAmbientalesP.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listSoCondicionesAmbientalesP.size(); j++) {
                            if (j != indice) {
                                if (listSoCondicionesAmbientalesP.get(indice).getCodigo().equals(listSoCondicionesAmbientalesP.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listSoCondicionesAmbientalesP.get(indice).getDescripcion().isEmpty()) {
                        listSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (listSoCondicionesAmbientalesP.get(indice).getDescripcion().equals(" ")) {
                        listSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarSoCondicionesAmbientalesP.isEmpty()) {
                            modificarSoCondicionesAmbientalesP.add(listSoCondicionesAmbientalesP.get(indice));
                        } else if (!modificarSoCondicionesAmbientalesP.contains(listSoCondicionesAmbientalesP.get(indice))) {
                            modificarSoCondicionesAmbientalesP.add(listSoCondicionesAmbientalesP.get(indice));
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
                    if (listSoCondicionesAmbientalesP.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                    } else if (listSoCondicionesAmbientalesP.get(indice).getCodigo().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else if (listSoCondicionesAmbientalesP.get(indice).getCodigo().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        for (int j = 0; j < listSoCondicionesAmbientalesP.size(); j++) {
                            if (j != indice) {
                                if (listSoCondicionesAmbientalesP.get(indice).getCodigo().equals(listSoCondicionesAmbientalesP.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            listSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listSoCondicionesAmbientalesP.get(indice).getDescripcion().isEmpty()) {
                        listSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (listSoCondicionesAmbientalesP.get(indice).getDescripcion().equals(" ")) {
                        listSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
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
            } else if (!crearSoCondicionesAmbientalesP.contains(filtrarSoCondicionesAmbientalesP.get(indice))) {
                if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                }
                if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {
                    for (int j = 0; j < listSoCondicionesAmbientalesP.size(); j++) {
                        log.error("indice lista  indice : " + listSoCondicionesAmbientalesP.get(j).getCodigo());
                        if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().equals(listSoCondicionesAmbientalesP.get(j).getCodigo())) {
                            contador++;
                        }
                    }

                    for (int j = 0; j < filtrarSoCondicionesAmbientalesP.size(); j++) {
                        log.error("indice filtrar indice : " + filtrarSoCondicionesAmbientalesP.get(j).getCodigo());
                        if (j == indice) {
                            if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().equals(filtrarSoCondicionesAmbientalesP.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        filtrarSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarSoCondicionesAmbientalesP.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                    banderita = false;
                }
                if (filtrarSoCondicionesAmbientalesP.get(indice).getDescripcion().equals(" ")) {
                    filtrarSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                }

                if (banderita == true) {
                    if (modificarSoCondicionesAmbientalesP.isEmpty()) {
                        modificarSoCondicionesAmbientalesP.add(filtrarSoCondicionesAmbientalesP.get(indice));
                    } else if (!modificarSoCondicionesAmbientalesP.contains(filtrarSoCondicionesAmbientalesP.get(indice))) {
                        modificarSoCondicionesAmbientalesP.add(filtrarSoCondicionesAmbientalesP.get(indice));
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
                if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                }
                if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {
                    for (int j = 0; j < listSoCondicionesAmbientalesP.size(); j++) {
                        log.error("indice lista  indice : " + listSoCondicionesAmbientalesP.get(j).getCodigo());
                        if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().equals(listSoCondicionesAmbientalesP.get(j).getCodigo())) {
                            contador++;
                        }
                    }

                    for (int j = 0; j < filtrarSoCondicionesAmbientalesP.size(); j++) {
                        log.error("indice filtrar indice : " + filtrarSoCondicionesAmbientalesP.get(j).getCodigo());
                        if (j == indice) {
                            if (filtrarSoCondicionesAmbientalesP.get(indice).getCodigo().equals(filtrarSoCondicionesAmbientalesP.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        filtrarSoCondicionesAmbientalesP.get(indice).setCodigo(backUpCodigo);
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarSoCondicionesAmbientalesP.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                    banderita = false;
                }
                if (filtrarSoCondicionesAmbientalesP.get(indice).getDescripcion().equals(" ")) {
                    filtrarSoCondicionesAmbientalesP.get(indice).setDescripcion(backUpDescripcion);
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
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
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoSoCondicionesAmbientalesP() {

        RequestContext context = RequestContext.getCurrentInstance();

        if (index >= 0) {

            if (tipoLista == 0) {
                log.info("borrandoSoCondicionesAmbientalesP");
                if (!modificarSoCondicionesAmbientalesP.isEmpty() && modificarSoCondicionesAmbientalesP.contains(listSoCondicionesAmbientalesP.get(index))) {
                    int modIndex = modificarSoCondicionesAmbientalesP.indexOf(listSoCondicionesAmbientalesP.get(index));
                    modificarSoCondicionesAmbientalesP.remove(modIndex);
                    borrarSoCondicionesAmbientalesP.add(listSoCondicionesAmbientalesP.get(index));
                } else if (!crearSoCondicionesAmbientalesP.isEmpty() && crearSoCondicionesAmbientalesP.contains(listSoCondicionesAmbientalesP.get(index))) {
                    int crearIndex = crearSoCondicionesAmbientalesP.indexOf(listSoCondicionesAmbientalesP.get(index));
                    crearSoCondicionesAmbientalesP.remove(crearIndex);
                } else {
                    borrarSoCondicionesAmbientalesP.add(listSoCondicionesAmbientalesP.get(index));
                }
                listSoCondicionesAmbientalesP.remove(index);
            }
            if (tipoLista == 1) {
                log.info("borrandoSoCondicionesAmbientalesP");
                if (!modificarSoCondicionesAmbientalesP.isEmpty() && modificarSoCondicionesAmbientalesP.contains(filtrarSoCondicionesAmbientalesP.get(index))) {
                    int modIndex = modificarSoCondicionesAmbientalesP.indexOf(filtrarSoCondicionesAmbientalesP.get(index));
                    modificarSoCondicionesAmbientalesP.remove(modIndex);
                    borrarSoCondicionesAmbientalesP.add(filtrarSoCondicionesAmbientalesP.get(index));
                } else if (!crearSoCondicionesAmbientalesP.isEmpty() && crearSoCondicionesAmbientalesP.contains(filtrarSoCondicionesAmbientalesP.get(index))) {
                    int crearIndex = crearSoCondicionesAmbientalesP.indexOf(filtrarSoCondicionesAmbientalesP.get(index));
                    crearSoCondicionesAmbientalesP.remove(crearIndex);
                } else {
                    borrarSoCondicionesAmbientalesP.add(filtrarSoCondicionesAmbientalesP.get(index));
                }
                int VCIndex = listSoCondicionesAmbientalesP.indexOf(filtrarSoCondicionesAmbientalesP.get(index));
                listSoCondicionesAmbientalesP.remove(VCIndex);
                filtrarSoCondicionesAmbientalesP.remove(index);

            }
            infoRegistro = "Cantidad de registros: " + listSoCondicionesAmbientalesP.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        log.info("verificarBorrado");
        try {
            if (tipoLista == 0) {
                verificarBorradoAccidentes = administrarSoCondicionesAmbientalesP.verificarSoAccidentesMedicos(listSoCondicionesAmbientalesP.get(index).getSecuencia());
            } else {
                verificarBorradoAccidentes = administrarSoCondicionesAmbientalesP.verificarSoAccidentesMedicos(filtrarSoCondicionesAmbientalesP.get(index).getSecuencia());
            }
            if (verificarBorradoAccidentes.equals(new BigInteger("0"))) {
                log.info("Borrado==0");
                borrandoSoCondicionesAmbientalesP();
            } else {
                log.info("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                verificarBorradoAccidentes = new BigInteger("-1");
            }
        } catch (Exception e) {
            log.error("ERROR CLASES ACCIDENTES verificarBorrado ERROR  ", e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarSoCondicionesAmbientalesP.isEmpty() || !crearSoCondicionesAmbientalesP.isEmpty() || !modificarSoCondicionesAmbientalesP.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardandoSoCondicionesAmbientalesP() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("REALIZANDO SO CONDICIONES AMBIENTALES P");
            if (!borrarSoCondicionesAmbientalesP.isEmpty()) {
                administrarSoCondicionesAmbientalesP.borrarSoCondicionesAmbientalesP(borrarSoCondicionesAmbientalesP);
                //mostrarBorrados
                registrosBorrados = borrarSoCondicionesAmbientalesP.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarSoCondicionesAmbientalesP.clear();
            }
            if (!crearSoCondicionesAmbientalesP.isEmpty()) {
                administrarSoCondicionesAmbientalesP.crearSoCondicionesAmbientalesP(crearSoCondicionesAmbientalesP);
                crearSoCondicionesAmbientalesP.clear();
            }
            if (!modificarSoCondicionesAmbientalesP.isEmpty()) {
                administrarSoCondicionesAmbientalesP.modificarSoCondicionesAmbientalesP(modificarSoCondicionesAmbientalesP);
                modificarSoCondicionesAmbientalesP.clear();
            }
            log.info("Se guardaron los datos con exito");
            listSoCondicionesAmbientalesP = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosClasesAusentismos");
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            k = 0;
            if (guardado == false) {
                guardado = true;
            }
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarSoCondicionAmbientalP = listSoCondicionesAmbientalesP.get(index);
            }
            if (tipoLista == 1) {
                editarSoCondicionAmbientalP = filtrarSoCondicionesAmbientalesP.get(index);
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
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevaSoCondicionAmbiental() {
        log.info("agregarNuevaSoCondicionAmbiental");
        int contador = 0;
        int duplicados = 0;

        Short a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevaSoCondicionAmbientalP.getCodigo() == null) {
            mensajeValidacion = " *Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            log.info("codigo en Motivo Cambio Cargo: " + nuevaSoCondicionAmbientalP.getCodigo());

            for (int x = 0; x < listSoCondicionesAmbientalesP.size(); x++) {
                if (listSoCondicionesAmbientalesP.get(x).getCodigo().equals(nuevaSoCondicionAmbientalP.getCodigo())) {
                    duplicados++;
                }
            }
            log.info("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                log.info("Mensaje validacion : " + mensajeValidacion);
            } else {
                log.info("bandera");
                contador++;
            }
        }
        if (nuevaSoCondicionAmbientalP.getDescripcion() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("bandera");
            contador++;

        }

        log.info("contador " + contador);

        if (contador == 2) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                log.info("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
                bandera = 0;
                filtrarSoCondicionesAmbientalesP = null;
                tipoLista = 0;
            }
            log.info("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevaSoCondicionAmbientalP.setSecuencia(l);

            crearSoCondicionesAmbientalesP.add(nuevaSoCondicionAmbientalP);

            listSoCondicionesAmbientalesP.add(nuevaSoCondicionAmbientalP);
            nuevaSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            infoRegistro = "Cantidad de registros: " + listSoCondicionesAmbientalesP.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroSoCondicionesAmbientalesPeligrosaP').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoSoCondicionesAmbientalesP() {
        log.info("limpiarSoCondicionesAmbientalesP");
        nuevaSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoSoCondicionAmbientalP() {
        log.info("duplicandoSoCondicionAmbientalP");
        if (index >= 0) {
            duplicarSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarSoCondicionAmbientalP.setSecuencia(l);
                duplicarSoCondicionAmbientalP.setCodigo(listSoCondicionesAmbientalesP.get(index).getCodigo());
                duplicarSoCondicionAmbientalP.setDescripcion(listSoCondicionesAmbientalesP.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarSoCondicionAmbientalP.setSecuencia(l);
                duplicarSoCondicionAmbientalP.setCodigo(filtrarSoCondicionesAmbientalesP.get(index).getCodigo());
                duplicarSoCondicionAmbientalP.setDescripcion(filtrarSoCondicionesAmbientalesP.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRCAP");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSoCondicionesAmbientalesP').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        log.error("CONFIRMAR DUPLICAR SO CONDICIONES AMBIENTALES P");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Short a = 0;
        a = null;
        log.error("ConfirmarDuplicar codigo " + duplicarSoCondicionAmbientalP.getCodigo());
        log.error("ConfirmarDuplicar Descripcion " + duplicarSoCondicionAmbientalP.getDescripcion());

        if (duplicarSoCondicionAmbientalP.getCodigo() == null || duplicarSoCondicionAmbientalP.getCodigo().equals(" ") || duplicarSoCondicionAmbientalP.getCodigo().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            for (int x = 0; x < listSoCondicionesAmbientalesP.size(); x++) {
                if (listSoCondicionesAmbientalesP.get(x).getCodigo().equals(duplicarSoCondicionAmbientalP.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                log.info("Mensaje validacion : " + mensajeValidacion);
            } else {
                log.info("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarSoCondicionAmbientalP.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripción \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            log.info("Datos Duplicando: " + duplicarSoCondicionAmbientalP.getSecuencia() + "  " + duplicarSoCondicionAmbientalP.getCodigo());
            if (crearSoCondicionesAmbientalesP.contains(duplicarSoCondicionAmbientalP)) {
                log.info("Ya lo contengo.");
            }
            listSoCondicionesAmbientalesP.add(duplicarSoCondicionAmbientalP);
            crearSoCondicionesAmbientalesP.add(duplicarSoCondicionAmbientalP);
            RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
            infoRegistro = "Cantidad de registros: " + listSoCondicionesAmbientalesP.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosSoCondicionesAmbientalesP:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosSoCondicionesAmbientalesP");
                bandera = 0;
                filtrarSoCondicionesAmbientalesP = null;
                tipoLista = 0;
            }
            duplicarSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroSoCondicionesAmbientalesP').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarSoCondicionesAmbientalesP() {
        duplicarSoCondicionAmbientalP = new SoCondicionesAmbientalesP();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSoCondicionesAmbientalesPExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "CONDICIONESAMBIENTALES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSoCondicionesAmbientalesPExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "CONDICIONESAMBIENTALES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("lol");
        if (!listSoCondicionesAmbientalesP.isEmpty()) {
            if (secRegistro != null) {
                log.info("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "SOCONDICIONESAMBIENTALESP"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("SOCONDICIONESAMBIENTALESP")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
    public List<SoCondicionesAmbientalesP> getListSoCondicionesAmbientalesP() {
        if (listSoCondicionesAmbientalesP == null) {
            listSoCondicionesAmbientalesP = administrarSoCondicionesAmbientalesP.consultarSoCondicionesAmbientalesP();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listSoCondicionesAmbientalesP == null || listSoCondicionesAmbientalesP.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listSoCondicionesAmbientalesP.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listSoCondicionesAmbientalesP;
    }

    public void setListSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> listSoCondicionesAmbientalesP) {
        this.listSoCondicionesAmbientalesP = listSoCondicionesAmbientalesP;
    }

    public List<SoCondicionesAmbientalesP> getFiltrarSoCondicionesAmbientalesP() {
        return filtrarSoCondicionesAmbientalesP;
    }

    public void setFiltrarSoCondicionesAmbientalesP(List<SoCondicionesAmbientalesP> filtrarSoCondicionesAmbientalesP) {
        this.filtrarSoCondicionesAmbientalesP = filtrarSoCondicionesAmbientalesP;
    }

    public SoCondicionesAmbientalesP getNuevaSoCondicionAmbientalP() {
        return nuevaSoCondicionAmbientalP;
    }

    public void setNuevaSoCondicionAmbientalP(SoCondicionesAmbientalesP nuevaSoCondicionAmbientalP) {
        this.nuevaSoCondicionAmbientalP = nuevaSoCondicionAmbientalP;
    }

    public SoCondicionesAmbientalesP getDuplicarSoCondicionAmbientalP() {
        return duplicarSoCondicionAmbientalP;
    }

    public void setDuplicarSoCondicionAmbientalP(SoCondicionesAmbientalesP duplicarSoCondicionAmbientalP) {
        this.duplicarSoCondicionAmbientalP = duplicarSoCondicionAmbientalP;
    }

    public SoCondicionesAmbientalesP getEditarSoCondicionAmbientalP() {
        return editarSoCondicionAmbientalP;
    }

    public void setEditarSoCondicionAmbientalP(SoCondicionesAmbientalesP editarSoCondicionAmbientalP) {
        this.editarSoCondicionAmbientalP = editarSoCondicionAmbientalP;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
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

    public SoCondicionesAmbientalesP getSoCondicionAmbientalPSeleccionado() {
        return soCondicionAmbientalPSeleccionado;
    }

    public void setSoCondicionAmbientalPSeleccionado(SoCondicionesAmbientalesP soCondicionAmbientalPSeleccionado) {
        this.soCondicionAmbientalPSeleccionado = soCondicionAmbientalPSeleccionado;
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
