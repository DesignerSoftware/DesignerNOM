/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.LugaresOcurrencias;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarLugaresOcurrenciasInterface;
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
public class ControlLugaresOcurrencias implements Serializable {

   private static Logger log = Logger.getLogger(ControlLugaresOcurrencias.class);

    @EJB
    AdministrarLugaresOcurrenciasInterface administrarLugaresOcurrencias;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<LugaresOcurrencias> listLugaresOcurrencias;
    private List<LugaresOcurrencias> filtrarLugaresOcurrencias;
    private List<LugaresOcurrencias> crearLugaresOcurrencias;
    private List<LugaresOcurrencias> modificarLugaresOcurrencias;
    private List<LugaresOcurrencias> borrarLugaresOcurrencias;
    private LugaresOcurrencias nuevoLugaresOcurrencias;
    private LugaresOcurrencias duplicarLugaresOcurrencias;
    private LugaresOcurrencias editarLugaresOcurrencias;
    private LugaresOcurrencias lugarOcurrenciaSeleccionada;
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
    //filtrado table
    private int tamano;
    private String backUpDescripcion;
    private String infoRegistro;
    private Integer backUpCodigo;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlLugaresOcurrencias() {
        listLugaresOcurrencias = null;
        crearLugaresOcurrencias = new ArrayList<LugaresOcurrencias>();
        modificarLugaresOcurrencias = new ArrayList<LugaresOcurrencias>();
        borrarLugaresOcurrencias = new ArrayList<LugaresOcurrencias>();
        permitirIndex = true;
        editarLugaresOcurrencias = new LugaresOcurrencias();
        nuevoLugaresOcurrencias = new LugaresOcurrencias();
        duplicarLugaresOcurrencias = new LugaresOcurrencias();
        guardado = true;
        tamano = 270;
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
        String pagActual = "sitioocurrencia";
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
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }
   
   @PostConstruct
    public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarLugaresOcurrencias.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            log.info("\n ENTRE A ControlLugaresOcurrencias.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarLugaresOcurrencias.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            log.warn("Error ControlLugaresOcurrencias eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        log.error("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listLugaresOcurrencias.get(index).getSecuencia();
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listLugaresOcurrencias.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarLugaresOcurrencias.get(index).getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listLugaresOcurrencias.get(index).getDescripcion();
                } else {
                    backUpDescripcion = filtrarLugaresOcurrencias.get(index).getDescripcion();
                }
            }

        }
        log.info("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            log.info("\n ENTRE A ControlLugaresOcurrencias.asignarIndex \n");
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
            log.warn("Error ControlLugaresOcurrencias.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
            bandera = 0;
            filtrarLugaresOcurrencias = null;
            tipoLista = 0;
        }

        borrarLugaresOcurrencias.clear();
        crearLugaresOcurrencias.clear();
        modificarLugaresOcurrencias.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listLugaresOcurrencias = null;
        guardado = true;
        permitirIndex = true;
        getListLugaresOcurrencias();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listLugaresOcurrencias == null || listLugaresOcurrencias.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listLugaresOcurrencias.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
            bandera = 0;
            filtrarLugaresOcurrencias = null;
            tipoLista = 0;
        }

        borrarLugaresOcurrencias.clear();
        crearLugaresOcurrencias.clear();
        modificarLugaresOcurrencias.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listLugaresOcurrencias = null;
        guardado = true;
        permitirIndex = true;
        getListLugaresOcurrencias();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listLugaresOcurrencias == null || listLugaresOcurrencias.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listLugaresOcurrencias.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            log.info("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
            bandera = 0;
            filtrarLugaresOcurrencias = null;
            tipoLista = 0;
        }
    }

    public void modificarLugaresOcurrencias(int indice, String confirmarCambio, String valorConfirmar) {
        log.error("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0, pass = 0;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        log.error("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            log.error("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearLugaresOcurrencias.contains(listLugaresOcurrencias.get(indice))) {
                    if (listLugaresOcurrencias.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listLugaresOcurrencias.size(); j++) {
                            if (j != indice) {
                                if (listLugaresOcurrencias.get(indice).getCodigo() == listLugaresOcurrencias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listLugaresOcurrencias.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);
                    } else if (listLugaresOcurrencias.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);

                    } else {
                        pass++;
                    }
                    if (pass == 2) {
                        if (modificarLugaresOcurrencias.isEmpty()) {
                            modificarLugaresOcurrencias.add(listLugaresOcurrencias.get(indice));
                        } else if (!modificarLugaresOcurrencias.contains(listLugaresOcurrencias.get(indice))) {
                            modificarLugaresOcurrencias.add(listLugaresOcurrencias.get(indice));
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
                    if (listLugaresOcurrencias.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listLugaresOcurrencias.size(); j++) {
                            if (j != indice) {
                                if (listLugaresOcurrencias.get(indice).getCodigo() == listLugaresOcurrencias.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listLugaresOcurrencias.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);
                    } else if (listLugaresOcurrencias.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);

                    } else {
                        pass++;
                    }
                    if (pass == 2) {

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
            } else if (!crearLugaresOcurrencias.contains(filtrarLugaresOcurrencias.get(indice))) {
                if (filtrarLugaresOcurrencias.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < filtrarLugaresOcurrencias.size(); j++) {
                        if (j != indice) {
                            if (filtrarLugaresOcurrencias.get(indice).getCodigo() == filtrarLugaresOcurrencias.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    for (int j = 0; j < listLugaresOcurrencias.size(); j++) {
                        if (j != indice) {
                            if (filtrarLugaresOcurrencias.get(indice).getCodigo() == listLugaresOcurrencias.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                    } else {
                        pass++;
                    }

                }

                if (filtrarLugaresOcurrencias.get(indice).getDescripcion() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);
                } else if (filtrarLugaresOcurrencias.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);
                } else {
                    pass++;
                }

                if (pass == 2) {
                    if (modificarLugaresOcurrencias.isEmpty()) {
                        modificarLugaresOcurrencias.add(filtrarLugaresOcurrencias.get(indice));
                    } else if (!modificarLugaresOcurrencias.contains(filtrarLugaresOcurrencias.get(indice))) {
                        modificarLugaresOcurrencias.add(filtrarLugaresOcurrencias.get(indice));
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

                if (filtrarLugaresOcurrencias.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < filtrarLugaresOcurrencias.size(); j++) {
                        if (j != indice) {
                            if (filtrarLugaresOcurrencias.get(indice).getCodigo() == filtrarLugaresOcurrencias.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    for (int j = 0; j < listLugaresOcurrencias.size(); j++) {
                        if (j != indice) {
                            if (filtrarLugaresOcurrencias.get(indice).getCodigo() == listLugaresOcurrencias.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarLugaresOcurrencias.get(indice).setCodigo(backUpCodigo);
                    } else {
                        pass++;
                    }

                }

                if (filtrarLugaresOcurrencias.get(indice).getDescripcion() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);
                } else if (filtrarLugaresOcurrencias.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarLugaresOcurrencias.get(indice).setDescripcion(backUpDescripcion);
                } else {
                    pass++;
                }

                if (pass == 2) {

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
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoLugaresOcurrencias() {

        if (index >= 0) {
            if (tipoLista == 0) {
                log.info("Entro a borrandoLugaresOcurrencias");
                if (!modificarLugaresOcurrencias.isEmpty() && modificarLugaresOcurrencias.contains(listLugaresOcurrencias.get(index))) {
                    int modIndex = modificarLugaresOcurrencias.indexOf(listLugaresOcurrencias.get(index));
                    modificarLugaresOcurrencias.remove(modIndex);
                    borrarLugaresOcurrencias.add(listLugaresOcurrencias.get(index));
                } else if (!crearLugaresOcurrencias.isEmpty() && crearLugaresOcurrencias.contains(listLugaresOcurrencias.get(index))) {
                    int crearIndex = crearLugaresOcurrencias.indexOf(listLugaresOcurrencias.get(index));
                    crearLugaresOcurrencias.remove(crearIndex);
                } else {
                    borrarLugaresOcurrencias.add(listLugaresOcurrencias.get(index));
                }
                listLugaresOcurrencias.remove(index);
            }
            if (tipoLista == 1) {
                log.info("borrandoLugaresOcurrencias ");
                if (!modificarLugaresOcurrencias.isEmpty() && modificarLugaresOcurrencias.contains(filtrarLugaresOcurrencias.get(index))) {
                    int modIndex = modificarLugaresOcurrencias.indexOf(filtrarLugaresOcurrencias.get(index));
                    modificarLugaresOcurrencias.remove(modIndex);
                    borrarLugaresOcurrencias.add(filtrarLugaresOcurrencias.get(index));
                } else if (!crearLugaresOcurrencias.isEmpty() && crearLugaresOcurrencias.contains(filtrarLugaresOcurrencias.get(index))) {
                    int crearIndex = crearLugaresOcurrencias.indexOf(filtrarLugaresOcurrencias.get(index));
                    crearLugaresOcurrencias.remove(crearIndex);
                } else {
                    borrarLugaresOcurrencias.add(filtrarLugaresOcurrencias.get(index));
                }
                int VCIndex = listLugaresOcurrencias.indexOf(filtrarLugaresOcurrencias.get(index));
                listLugaresOcurrencias.remove(VCIndex);
                filtrarLugaresOcurrencias.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listLugaresOcurrencias == null || listLugaresOcurrencias.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listLugaresOcurrencias.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void revisarDialogoGuardar() {

        if (!borrarLugaresOcurrencias.isEmpty() || !crearLugaresOcurrencias.isEmpty() || !modificarLugaresOcurrencias.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void verificarBorrado() {
        log.info("verificarBorrado");
        BigInteger soAccidentes;

        try {
            if (tipoLista == 0) {
                soAccidentes = administrarLugaresOcurrencias.verificarSoAccidentesLugarOcurrencia(listLugaresOcurrencias.get(index).getSecuencia());
            } else {
                soAccidentes = administrarLugaresOcurrencias.verificarSoAccidentesLugarOcurrencia(filtrarLugaresOcurrencias.get(index).getSecuencia());
            }
            if (soAccidentes.equals(new BigInteger("0"))) {
                log.info("Borrado==0");
                borrandoLugaresOcurrencias();
            } else {
                log.info("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                soAccidentes = new BigInteger("-1");
            }
        } catch (Exception e) {
            log.error("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
        }
    }

    public void guardarLugaresOcurrencias() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("Realizando guardarLugaresOcurrencias");
            if (!borrarLugaresOcurrencias.isEmpty()) {
                administrarLugaresOcurrencias.borrarLugarOcurrencia(borrarLugaresOcurrencias);
                //mostrarBorrados
                registrosBorrados = borrarLugaresOcurrencias.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarLugaresOcurrencias.clear();
            }
            if (!modificarLugaresOcurrencias.isEmpty()) {
                administrarLugaresOcurrencias.modificarLugarOcurrencia(modificarLugaresOcurrencias);
                modificarLugaresOcurrencias.clear();
            }
            if (!crearLugaresOcurrencias.isEmpty()) {
                administrarLugaresOcurrencias.crearLugarOcurrencia(crearLugaresOcurrencias);
                crearLugaresOcurrencias.clear();
            }
            log.info("Se guardaron los datos con exito");
            listLugaresOcurrencias = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarLugaresOcurrencias = listLugaresOcurrencias.get(index);
            }
            if (tipoLista == 1) {
                editarLugaresOcurrencias = filtrarLugaresOcurrencias.get(index);
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

    public void agregarNuevoLugaresOcurrencias() {
        log.info("agregarNuevoLugaresOcurrencias");
        int contador = 0;
        int duplicados = 0;
        Integer a;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoLugaresOcurrencias.getCodigo() == a) {
            mensajeValidacion = " *Código \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            log.info("codigo en Motivo Cambio Cargo: " + nuevoLugaresOcurrencias.getCodigo());

            for (int x = 0; x < listLugaresOcurrencias.size(); x++) {
                if (listLugaresOcurrencias.get(x).getCodigo() == nuevoLugaresOcurrencias.getCodigo()) {
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
        if (nuevoLugaresOcurrencias.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("bandera");
            contador++;

        }

        log.info("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                log.info("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
                bandera = 0;
                filtrarLugaresOcurrencias = null;
                tipoLista = 0;
            }
            log.info("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoLugaresOcurrencias.setSecuencia(l);

            crearLugaresOcurrencias.add(nuevoLugaresOcurrencias);

            listLugaresOcurrencias.add(nuevoLugaresOcurrencias);
            nuevoLugaresOcurrencias = new LugaresOcurrencias();
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");

            infoRegistro = "Cantidad de registros: " + listLugaresOcurrencias.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroLugaresOcurrencias').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
        }
    }

    public void limpiarNuevoLugaresOcurrencias() {
        log.info("limpiarNuevoLugaresOcurrencias");
        nuevoLugaresOcurrencias = new LugaresOcurrencias();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoLugaresOcurrencias() {
        log.info("duplicandoLugaresOcurrencias");
        if (index >= 0) {
            duplicarLugaresOcurrencias = new LugaresOcurrencias();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarLugaresOcurrencias.setSecuencia(l);
                duplicarLugaresOcurrencias.setCodigo(listLugaresOcurrencias.get(index).getCodigo());
                duplicarLugaresOcurrencias.setDescripcion(listLugaresOcurrencias.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarLugaresOcurrencias.setSecuencia(l);
                duplicarLugaresOcurrencias.setCodigo(filtrarLugaresOcurrencias.get(index).getCodigo());
                duplicarLugaresOcurrencias.setDescripcion(filtrarLugaresOcurrencias.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroLugaresOcurrencias').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        log.error("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a;
        a = null;
        log.error("ConfirmarDuplicar codigo " + duplicarLugaresOcurrencias.getCodigo());
        log.error("ConfirmarDuplicar Descripcion " + duplicarLugaresOcurrencias.getDescripcion());

        if (duplicarLugaresOcurrencias.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listLugaresOcurrencias.size(); x++) {
                if (listLugaresOcurrencias.get(x).getCodigo() == duplicarLugaresOcurrencias.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                log.info("Mensaje validacion : " + mensajeValidacion);
            } else {
                log.info("bandera");
                contador++;
            }
        }
        if (duplicarLugaresOcurrencias.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            log.info("Datos Duplicando: " + duplicarLugaresOcurrencias.getSecuencia() + "  " + duplicarLugaresOcurrencias.getCodigo());
            if (crearLugaresOcurrencias.contains(duplicarLugaresOcurrencias)) {
                log.info("Ya lo contengo.");
            }
            listLugaresOcurrencias.add(duplicarLugaresOcurrencias);
            crearLugaresOcurrencias.add(duplicarLugaresOcurrencias);
            RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            infoRegistro = "Cantidad de registros: " + listLugaresOcurrencias.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosLugaresOcurrencias:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosLugaresOcurrencias");
                bandera = 0;
                filtrarLugaresOcurrencias = null;
                tipoLista = 0;
            }
            duplicarLugaresOcurrencias = new LugaresOcurrencias();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroLugaresOcurrencias').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarLugaresOcurrencias() {
        duplicarLugaresOcurrencias = new LugaresOcurrencias();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLugaresOcurrenciasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "LUGARESOCURRENCIAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLugaresOcurrenciasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "LUGARESOCURRENCIAS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("lol");
        if (!listLugaresOcurrencias.isEmpty()) {
            if (secRegistro != null) {
                log.info("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "LUGARESOCURRENCIAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("LUGARESOCURRENCIAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<LugaresOcurrencias> getListLugaresOcurrencias() {
        if (listLugaresOcurrencias == null) {
            listLugaresOcurrencias = administrarLugaresOcurrencias.consultarLugaresOcurrencias();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listLugaresOcurrencias == null || listLugaresOcurrencias.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listLugaresOcurrencias.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listLugaresOcurrencias;
    }

    public void setListLugaresOcurrencias(List<LugaresOcurrencias> listLugaresOcurrencias) {
        this.listLugaresOcurrencias = listLugaresOcurrencias;
    }

    public List<LugaresOcurrencias> getFiltrarLugaresOcurrencias() {
        return filtrarLugaresOcurrencias;
    }

    public void setFiltrarLugaresOcurrencias(List<LugaresOcurrencias> filtrarLugaresOcurrencias) {
        this.filtrarLugaresOcurrencias = filtrarLugaresOcurrencias;
    }

    public LugaresOcurrencias getNuevoLugaresOcurrencias() {
        return nuevoLugaresOcurrencias;
    }

    public void setNuevoLugaresOcurrencias(LugaresOcurrencias nuevoLugaresOcurrencias) {
        this.nuevoLugaresOcurrencias = nuevoLugaresOcurrencias;
    }

    public LugaresOcurrencias getDuplicarLugaresOcurrencias() {
        return duplicarLugaresOcurrencias;
    }

    public void setDuplicarLugaresOcurrencias(LugaresOcurrencias duplicarLugaresOcurrencias) {
        this.duplicarLugaresOcurrencias = duplicarLugaresOcurrencias;
    }

    public LugaresOcurrencias getEditarLugaresOcurrencias() {
        return editarLugaresOcurrencias;
    }

    public void setEditarLugaresOcurrencias(LugaresOcurrencias editarLugaresOcurrencias) {
        this.editarLugaresOcurrencias = editarLugaresOcurrencias;
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

    public LugaresOcurrencias getLugarOcurrenciaSeleccionada() {
        return lugarOcurrenciaSeleccionada;
    }

    public void setLugarOcurrenciaSeleccionada(LugaresOcurrencias lugarOcurrenciaSeleccionada) {
        this.lugarOcurrenciaSeleccionada = lugarOcurrenciaSeleccionada;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
