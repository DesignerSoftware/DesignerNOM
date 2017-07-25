/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.MotivosReemplazos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarMotivosReemplazosInterface;
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
public class ControlMotivosReemplazos implements Serializable {

   private static Logger log = Logger.getLogger(ControlMotivosReemplazos.class);

    @EJB
    AdministrarMotivosReemplazosInterface administrarMotivosReemplazos;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<MotivosReemplazos> listMotivosReemplazos;
    private List<MotivosReemplazos> filtrarMotivosReemplazos;
    private List<MotivosReemplazos> crearMotivosReemplazos;
    private List<MotivosReemplazos> modificarMotivosReemplazos;
    private List<MotivosReemplazos> borrarMotivosReemplazos;
    private MotivosReemplazos nuevoMotivosReemplazos;
    private MotivosReemplazos duplicarMotivosReemplazos;
    private MotivosReemplazos editarMotivosReemplazos;
    private MotivosReemplazos motivosReemplazosSeleccionado;
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
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlMotivosReemplazos() {
        listMotivosReemplazos = null;
        crearMotivosReemplazos = new ArrayList<MotivosReemplazos>();
        modificarMotivosReemplazos = new ArrayList<MotivosReemplazos>();
        borrarMotivosReemplazos = new ArrayList<MotivosReemplazos>();
        permitirIndex = true;
        editarMotivosReemplazos = new MotivosReemplazos();
        nuevoMotivosReemplazos = new MotivosReemplazos();
        duplicarMotivosReemplazos = new MotivosReemplazos();
        guardado = true;
        tamano = 270;
        log.info("controlMotivosReemplazos Constructor");
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
        String pagActual = "motivosreemplazos";
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
            log.info("ControlMotivosReemplazos PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarMotivosReemplazos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaAnterior(String pagina) {
        paginaAnterior = pagina;
        log.info("ControlMotivosReemplazos recibirPaginaAnterior paginaAnterior : " + paginaAnterior);
    }

    public String redirigirPaginaAnterior() {
        log.info("ControlMotivosReemplazos redirigirPaginaAnterior paginaAnterior : " + paginaAnterior);
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        try {
            log.info("\n ENTRE A ControlMotivosReemplazos.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarMotivosReemplazos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            log.warn("Error ControlMotivosReemplazos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        log.error("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listMotivosReemplazos.get(index).getCodigo();
                    log.info(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listMotivosReemplazos.get(index).getNombre();
                    log.info(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listMotivosReemplazos.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarMotivosReemplazos.get(index).getCodigo();
                    log.info(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarMotivosReemplazos.get(index).getNombre();
                    log.info(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarMotivosReemplazos.get(index).getSecuencia();
            }

        }
        log.info("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            log.info("\n ENTRE A ControlMotivosReemplazos.asignarIndex \n");
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
            log.warn("Error ControlMotivosReemplazos.asignarIndex ERROR======" + e.getMessage());
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
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            bandera = 0;
            filtrarMotivosReemplazos = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarMotivosReemplazos.clear();
        crearMotivosReemplazos.clear();
        modificarMotivosReemplazos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosReemplazos = null;
        guardado = true;
        permitirIndex = true;
        getListMotivosReemplazos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMotivosReemplazos == null || listMotivosReemplazos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosReemplazos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            bandera = 0;
            filtrarMotivosReemplazos = null;
            tipoLista = 0;
            tamano = 270;
        }
        borrarMotivosReemplazos.clear();
        crearMotivosReemplazos.clear();
        modificarMotivosReemplazos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosReemplazos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            log.info("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            bandera = 0;
            filtrarMotivosReemplazos = null;
            tipoLista = 0;
        }
    }

    public void modificarMotivosReemplazos(int indice, String confirmarCambio, String valorConfirmar) {
        log.error("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        log.error("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            log.error("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearMotivosReemplazos.contains(listMotivosReemplazos.get(indice))) {
                    if (listMotivosReemplazos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosReemplazos.size(); j++) {
                            if (j != indice) {
                                if (listMotivosReemplazos.get(indice).getCodigo() == listMotivosReemplazos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listMotivosReemplazos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listMotivosReemplazos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarMotivosReemplazos.isEmpty()) {
                            modificarMotivosReemplazos.add(listMotivosReemplazos.get(indice));
                        } else if (!modificarMotivosReemplazos.contains(listMotivosReemplazos.get(indice))) {
                            modificarMotivosReemplazos.add(listMotivosReemplazos.get(indice));
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
                    if (listMotivosReemplazos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosReemplazos.size(); j++) {
                            if (j != indice) {
                                if (listMotivosReemplazos.get(indice).getCodigo() == listMotivosReemplazos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listMotivosReemplazos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listMotivosReemplazos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
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
            } else if (!crearMotivosReemplazos.contains(filtrarMotivosReemplazos.get(indice))) {
                if (filtrarMotivosReemplazos.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < listMotivosReemplazos.size(); j++) {
                        if (j != indice) {
                            if (filtrarMotivosReemplazos.get(indice).getCodigo() == listMotivosReemplazos.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarMotivosReemplazos.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
                }
                if (filtrarMotivosReemplazos.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
                }

                if (banderita == true) {
                    if (modificarMotivosReemplazos.isEmpty()) {
                        modificarMotivosReemplazos.add(filtrarMotivosReemplazos.get(indice));
                    } else if (!modificarMotivosReemplazos.contains(filtrarMotivosReemplazos.get(indice))) {
                        modificarMotivosReemplazos.add(filtrarMotivosReemplazos.get(indice));
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
                if (filtrarMotivosReemplazos.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < listMotivosReemplazos.size(); j++) {
                        if (j != indice) {
                            if (filtrarMotivosReemplazos.get(indice).getCodigo() == listMotivosReemplazos.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarMotivosReemplazos.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarMotivosReemplazos.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
                }
                if (filtrarMotivosReemplazos.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarMotivosReemplazos.get(indice).setNombre(backUpDescripcion);
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
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoMotivosReemplazos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                log.info("Entro a borrandoMotivosReemplazos");
                if (!modificarMotivosReemplazos.isEmpty() && modificarMotivosReemplazos.contains(listMotivosReemplazos.get(index))) {
                    int modIndex = modificarMotivosReemplazos.indexOf(listMotivosReemplazos.get(index));
                    modificarMotivosReemplazos.remove(modIndex);
                    borrarMotivosReemplazos.add(listMotivosReemplazos.get(index));
                } else if (!crearMotivosReemplazos.isEmpty() && crearMotivosReemplazos.contains(listMotivosReemplazos.get(index))) {
                    int crearIndex = crearMotivosReemplazos.indexOf(listMotivosReemplazos.get(index));
                    crearMotivosReemplazos.remove(crearIndex);
                } else {
                    borrarMotivosReemplazos.add(listMotivosReemplazos.get(index));
                }
                listMotivosReemplazos.remove(index);
            }
            if (tipoLista == 1) {
                log.info("borrandoMotivosReemplazos ");
                if (!modificarMotivosReemplazos.isEmpty() && modificarMotivosReemplazos.contains(filtrarMotivosReemplazos.get(index))) {
                    int modIndex = modificarMotivosReemplazos.indexOf(filtrarMotivosReemplazos.get(index));
                    modificarMotivosReemplazos.remove(modIndex);
                    borrarMotivosReemplazos.add(filtrarMotivosReemplazos.get(index));
                } else if (!crearMotivosReemplazos.isEmpty() && crearMotivosReemplazos.contains(filtrarMotivosReemplazos.get(index))) {
                    int crearIndex = crearMotivosReemplazos.indexOf(filtrarMotivosReemplazos.get(index));
                    crearMotivosReemplazos.remove(crearIndex);
                } else {
                    borrarMotivosReemplazos.add(filtrarMotivosReemplazos.get(index));
                }
                int VCIndex = listMotivosReemplazos.indexOf(filtrarMotivosReemplazos.get(index));
                listMotivosReemplazos.remove(VCIndex);
                filtrarMotivosReemplazos.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            infoRegistro = "Cantidad de registros: " + listMotivosReemplazos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        log.info("Estoy en verificarBorrado");
        BigInteger contarEncargaturasMotivoReemplazo;

        try {
            log.error("Control Secuencia de ControlMotivosReemplazos ");
            if (tipoLista == 0) {
                contarEncargaturasMotivoReemplazo = administrarMotivosReemplazos.contarEncargaturasMotivoReemplazo(listMotivosReemplazos.get(index).getSecuencia());
            } else {
                contarEncargaturasMotivoReemplazo = administrarMotivosReemplazos.contarEncargaturasMotivoReemplazo(filtrarMotivosReemplazos.get(index).getSecuencia());
            }
            if (contarEncargaturasMotivoReemplazo.equals(new BigInteger("0"))) {
                log.info("Borrado==0");
                borrandoMotivosReemplazos();
            } else {
                log.info("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                contarEncargaturasMotivoReemplazo = new BigInteger("-1");

            }
        } catch (Exception e) {
            log.error("ERROR ControlMotivosReemplazos verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarMotivosReemplazos.isEmpty() || !crearMotivosReemplazos.isEmpty() || !modificarMotivosReemplazos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarMotivosReemplazos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("Realizando guardarMotivosReemplazos");
            if (!borrarMotivosReemplazos.isEmpty()) {
                administrarMotivosReemplazos.borrarMotivosReemplazos(borrarMotivosReemplazos);
                //mostrarBorrados
                registrosBorrados = borrarMotivosReemplazos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarMotivosReemplazos.clear();
            }
            if (!modificarMotivosReemplazos.isEmpty()) {
                administrarMotivosReemplazos.modificarMotivosReemplazos(modificarMotivosReemplazos);
                modificarMotivosReemplazos.clear();
            }
            if (!crearMotivosReemplazos.isEmpty()) {
                administrarMotivosReemplazos.crearMotivosReemplazos(crearMotivosReemplazos);
                crearMotivosReemplazos.clear();
            }
            log.info("Se guardaron los datos con exito");
            listMotivosReemplazos = null;
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarMotivosReemplazos = listMotivosReemplazos.get(index);
            }
            if (tipoLista == 1) {
                editarMotivosReemplazos = filtrarMotivosReemplazos.get(index);
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

    public void agregarNuevoMotivosReemplazos() {
        log.info("agregarNuevoMotivosReemplazos");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoMotivosReemplazos.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            log.info("codigo en Motivo Cambio Cargo: " + nuevoMotivosReemplazos.getCodigo());

            for (int x = 0; x < listMotivosReemplazos.size(); x++) {
                if (listMotivosReemplazos.get(x).getCodigo() == nuevoMotivosReemplazos.getCodigo()) {
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
        if (nuevoMotivosReemplazos.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Motivo Reemplazo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoMotivosReemplazos.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Motivo Reemplazo \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
                bandera = 0;
                filtrarMotivosReemplazos = null;
                tipoLista = 0;
            }
            log.info("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivosReemplazos.setSecuencia(l);

            crearMotivosReemplazos.add(nuevoMotivosReemplazos);

            listMotivosReemplazos.add(nuevoMotivosReemplazos);
            nuevoMotivosReemplazos = new MotivosReemplazos();
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            infoRegistro = "Cantidad de registros: " + listMotivosReemplazos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMotivosReemplazos').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivosReemplazos() {
        log.info("limpiarNuevoMotivosReemplazos");
        nuevoMotivosReemplazos = new MotivosReemplazos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoMotivosReemplazos() {
        log.info("duplicandoMotivosReemplazos");
        if (index >= 0) {
            duplicarMotivosReemplazos = new MotivosReemplazos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivosReemplazos.setSecuencia(l);
                duplicarMotivosReemplazos.setCodigo(listMotivosReemplazos.get(index).getCodigo());
                duplicarMotivosReemplazos.setNombre(listMotivosReemplazos.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarMotivosReemplazos.setSecuencia(l);
                duplicarMotivosReemplazos.setCodigo(filtrarMotivosReemplazos.get(index).getCodigo());
                duplicarMotivosReemplazos.setNombre(filtrarMotivosReemplazos.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosReemplazos').show()");
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
        Integer a = 0;
        a = null;
        log.error("ConfirmarDuplicar codigo " + duplicarMotivosReemplazos.getCodigo());
        log.error("ConfirmarDuplicar Descripcion " + duplicarMotivosReemplazos.getNombre());

        if (duplicarMotivosReemplazos.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosReemplazos.size(); x++) {
                if (listMotivosReemplazos.get(x).getCodigo() == duplicarMotivosReemplazos.getCodigo()) {
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
        if (duplicarMotivosReemplazos.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Motivo Reemplazo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarMotivosReemplazos.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Motivo Reemplazo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("bandera");
            contador++;

        }

        if (contador == 2) {

            log.info("Datos Duplicando: " + duplicarMotivosReemplazos.getSecuencia() + "  " + duplicarMotivosReemplazos.getCodigo());
            if (crearMotivosReemplazos.contains(duplicarMotivosReemplazos)) {
                log.info("Ya lo contengo.");
            }
            listMotivosReemplazos.add(duplicarMotivosReemplazos);
            crearMotivosReemplazos.add(duplicarMotivosReemplazos);
            RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listMotivosReemplazos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivosReemplazos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivosReemplazos");
                bandera = 0;
                filtrarMotivosReemplazos = null;
                tipoLista = 0;
            }
            duplicarMotivosReemplazos = new MotivosReemplazos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosReemplazos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarMotivosReemplazos() {
        duplicarMotivosReemplazos = new MotivosReemplazos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosReemplazosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MOTIVOSREEMPLEAZOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivosReemplazosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MOTIVOSREEMPLEAZOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("lol");
        if (!listMotivosReemplazos.isEmpty()) {
            if (secRegistro != null) {
                log.info("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "MOTIVOSREEMPLEAZOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSREEMPLEAZOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<MotivosReemplazos> getListMotivosReemplazos() {
        if (listMotivosReemplazos == null) {
            log.info("ControlMotivosReemplazos getListMotivosReemplazos");
            listMotivosReemplazos = administrarMotivosReemplazos.MotivosReemplazos();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listMotivosReemplazos == null || listMotivosReemplazos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listMotivosReemplazos.size();
        }
        return listMotivosReemplazos;
    }

    public void setListMotivosReemplazos(List<MotivosReemplazos> listMotivosReemplazos) {
        this.listMotivosReemplazos = listMotivosReemplazos;
    }

    public List<MotivosReemplazos> getFiltrarMotivosReemplazos() {
        return filtrarMotivosReemplazos;
    }

    public void setFiltrarMotivosReemplazos(List<MotivosReemplazos> filtrarMotivosReemplazos) {
        this.filtrarMotivosReemplazos = filtrarMotivosReemplazos;
    }

    public MotivosReemplazos getNuevoMotivosReemplazos() {
        return nuevoMotivosReemplazos;
    }

    public void setNuevoMotivosReemplazos(MotivosReemplazos nuevoMotivosReemplazos) {
        this.nuevoMotivosReemplazos = nuevoMotivosReemplazos;
    }

    public MotivosReemplazos getDuplicarMotivosReemplazos() {
        return duplicarMotivosReemplazos;
    }

    public void setDuplicarMotivosReemplazos(MotivosReemplazos duplicarMotivosReemplazos) {
        this.duplicarMotivosReemplazos = duplicarMotivosReemplazos;
    }

    public MotivosReemplazos getEditarMotivosReemplazos() {
        return editarMotivosReemplazos;
    }

    public void setEditarMotivosReemplazos(MotivosReemplazos editarMotivosReemplazos) {
        this.editarMotivosReemplazos = editarMotivosReemplazos;
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

    public MotivosReemplazos getMotivosReemplazosSeleccionado() {
        return motivosReemplazosSeleccionado;
    }

    public void setMotivosReemplazosSeleccionado(MotivosReemplazos clasesPensionesSeleccionado) {
        this.motivosReemplazosSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
