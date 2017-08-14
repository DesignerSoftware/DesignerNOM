/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.PartesCuerpo;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarPartesCuerpoInterface;
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
public class ControlPartesCuerpo implements Serializable {

   private static Logger log = Logger.getLogger(ControlPartesCuerpo.class);

    @EJB
    AdministrarPartesCuerpoInterface administrarPartesCuerpo;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<PartesCuerpo> listPartesCuerpo;
    private List<PartesCuerpo> filtrarPartesCuerpo;
    private List<PartesCuerpo> crearPartesCuerpo;
    private List<PartesCuerpo> modificarPartesCuerpo;
    private List<PartesCuerpo> borrarPartesCuerpo;
    private PartesCuerpo nuevoPartesCuerpo;
    private PartesCuerpo duplicarPartesCuerpo;
    private PartesCuerpo editarPartesCuerpo;
    private PartesCuerpo parteCuerpoSeleccionado;
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

    public ControlPartesCuerpo() {
        listPartesCuerpo = null;
        crearPartesCuerpo = new ArrayList<PartesCuerpo>();
        modificarPartesCuerpo = new ArrayList<PartesCuerpo>();
        borrarPartesCuerpo = new ArrayList<PartesCuerpo>();
        permitirIndex = true;
        editarPartesCuerpo = new PartesCuerpo();
        nuevoPartesCuerpo = new PartesCuerpo();
        duplicarPartesCuerpo = new PartesCuerpo();
        guardado = true;
        tamano = 270;
        mapParametros.put("paginaAnterior", paginaAnterior);
        log.info("controlPartesCuerpo Constructor");
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
            log.info("ControlPartesCuerpo PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarPartesCuerpo.obtenerConexion(ses.getId());
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
        String pagActual = "partecuerpo";
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
            log.info("\n ENTRE A ControlPartesCuerpo.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarPartesCuerpo.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            log.warn("Error ControlPartesCuerpo eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        log.error("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listPartesCuerpo.get(index).getCodigo();
                    log.info(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listPartesCuerpo.get(index).getDescripcion();
                    log.info(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listPartesCuerpo.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarPartesCuerpo.get(index).getCodigo();
                    log.info(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarPartesCuerpo.get(index).getDescripcion();
                    log.info(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarPartesCuerpo.get(index).getSecuencia();
            }

        }
        log.info("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            log.info("\n ENTRE A ControlPartesCuerpo.asignarIndex \n");
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
            log.warn("Error ControlPartesCuerpo.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
            bandera = 0;
            filtrarPartesCuerpo = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarPartesCuerpo.clear();
        crearPartesCuerpo.clear();
        modificarPartesCuerpo.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listPartesCuerpo = null;
        guardado = true;
        permitirIndex = true;
        getListPartesCuerpo();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listPartesCuerpo == null || listPartesCuerpo.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listPartesCuerpo.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
            bandera = 0;
            filtrarPartesCuerpo = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarPartesCuerpo.clear();
        crearPartesCuerpo.clear();
        modificarPartesCuerpo.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listPartesCuerpo = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            log.info("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
            bandera = 0;
            filtrarPartesCuerpo = null;
            tipoLista = 0;
        }
    }

    public void modificarPartesCuerpo(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearPartesCuerpo.contains(listPartesCuerpo.get(indice))) {
                    if (listPartesCuerpo.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listPartesCuerpo.size(); j++) {
                            if (j != indice) {
                                if (listPartesCuerpo.get(indice).getCodigo().equals(listPartesCuerpo.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listPartesCuerpo.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listPartesCuerpo.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarPartesCuerpo.isEmpty()) {
                            modificarPartesCuerpo.add(listPartesCuerpo.get(indice));
                        } else if (!modificarPartesCuerpo.contains(listPartesCuerpo.get(indice))) {
                            modificarPartesCuerpo.add(listPartesCuerpo.get(indice));
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
                    if (listPartesCuerpo.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listPartesCuerpo.size(); j++) {
                            if (j != indice) {
                                if (listPartesCuerpo.get(indice).getCodigo().equals(listPartesCuerpo.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listPartesCuerpo.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listPartesCuerpo.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
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
            } else if (!crearPartesCuerpo.contains(filtrarPartesCuerpo.get(indice))) {
                if (filtrarPartesCuerpo.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < filtrarPartesCuerpo.size(); j++) {
                        if (j != indice) {
                            if (filtrarPartesCuerpo.get(indice).getCodigo().equals(filtrarPartesCuerpo.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarPartesCuerpo.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
                }
                if (filtrarPartesCuerpo.get(indice).getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
                }

                if (banderita == true) {
                    if (modificarPartesCuerpo.isEmpty()) {
                        modificarPartesCuerpo.add(filtrarPartesCuerpo.get(indice));
                    } else if (!modificarPartesCuerpo.contains(filtrarPartesCuerpo.get(indice))) {
                        modificarPartesCuerpo.add(filtrarPartesCuerpo.get(indice));
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
                if (filtrarPartesCuerpo.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < filtrarPartesCuerpo.size(); j++) {
                        if (j != indice) {
                            if (filtrarPartesCuerpo.get(indice).getCodigo().equals(filtrarPartesCuerpo.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarPartesCuerpo.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarPartesCuerpo.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
                }
                if (filtrarPartesCuerpo.get(indice).getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarPartesCuerpo.get(indice).setDescripcion(backUpDescripcion);
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
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoPartesCuerpo() {

        if (index >= 0) {
            if (tipoLista == 0) {
                log.info("Entro a borrandoPartesCuerpo");
                if (!modificarPartesCuerpo.isEmpty() && modificarPartesCuerpo.contains(listPartesCuerpo.get(index))) {
                    int modIndex = modificarPartesCuerpo.indexOf(listPartesCuerpo.get(index));
                    modificarPartesCuerpo.remove(modIndex);
                    borrarPartesCuerpo.add(listPartesCuerpo.get(index));
                } else if (!crearPartesCuerpo.isEmpty() && crearPartesCuerpo.contains(listPartesCuerpo.get(index))) {
                    int crearIndex = crearPartesCuerpo.indexOf(listPartesCuerpo.get(index));
                    crearPartesCuerpo.remove(crearIndex);
                } else {
                    borrarPartesCuerpo.add(listPartesCuerpo.get(index));
                }
                listPartesCuerpo.remove(index);
            }
            if (tipoLista == 1) {
                log.info("borrandoPartesCuerpo ");
                if (!modificarPartesCuerpo.isEmpty() && modificarPartesCuerpo.contains(filtrarPartesCuerpo.get(index))) {
                    int modIndex = modificarPartesCuerpo.indexOf(filtrarPartesCuerpo.get(index));
                    modificarPartesCuerpo.remove(modIndex);
                    borrarPartesCuerpo.add(filtrarPartesCuerpo.get(index));
                } else if (!crearPartesCuerpo.isEmpty() && crearPartesCuerpo.contains(filtrarPartesCuerpo.get(index))) {
                    int crearIndex = crearPartesCuerpo.indexOf(filtrarPartesCuerpo.get(index));
                    crearPartesCuerpo.remove(crearIndex);
                } else {
                    borrarPartesCuerpo.add(filtrarPartesCuerpo.get(index));
                }
                int VCIndex = listPartesCuerpo.indexOf(filtrarPartesCuerpo.get(index));
                listPartesCuerpo.remove(VCIndex);
                filtrarPartesCuerpo.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
            infoRegistro = "Cantidad de registros: " + listPartesCuerpo.size();
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
        BigInteger contarDetallesExamenesParteCuerpo;
        BigInteger contarSoAccidentesMedicosParteCuerpo;
        BigInteger contarSoDetallesRevisionesParteCuerpo;

        try {
            log.error("Control Secuencia de ControlPartesCuerpo ");
            if (tipoLista == 0) {
                contarDetallesExamenesParteCuerpo = administrarPartesCuerpo.contarDetallesExamenesParteCuerpo(listPartesCuerpo.get(index).getSecuencia());
                contarSoAccidentesMedicosParteCuerpo = administrarPartesCuerpo.contarSoAccidentesMedicosParteCuerpo(listPartesCuerpo.get(index).getSecuencia());
                contarSoDetallesRevisionesParteCuerpo = administrarPartesCuerpo.contarSoDetallesRevisionesParteCuerpo(listPartesCuerpo.get(index).getSecuencia());
            } else {
                contarDetallesExamenesParteCuerpo = administrarPartesCuerpo.contarDetallesExamenesParteCuerpo(filtrarPartesCuerpo.get(index).getSecuencia());
                contarSoAccidentesMedicosParteCuerpo = administrarPartesCuerpo.contarSoAccidentesMedicosParteCuerpo(filtrarPartesCuerpo.get(index).getSecuencia());
                contarSoDetallesRevisionesParteCuerpo = administrarPartesCuerpo.contarSoDetallesRevisionesParteCuerpo(filtrarPartesCuerpo.get(index).getSecuencia());
            }
            if (contarDetallesExamenesParteCuerpo.equals(new BigInteger("0"))
                    && contarSoAccidentesMedicosParteCuerpo.equals(new BigInteger("0"))
                    && contarSoDetallesRevisionesParteCuerpo.equals(new BigInteger("0"))) {
                log.info("Borrado==0");
                borrandoPartesCuerpo();
            } else {
                log.info("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                contarDetallesExamenesParteCuerpo = new BigInteger("-1");
                contarSoAccidentesMedicosParteCuerpo = new BigInteger("-1");
                contarSoDetallesRevisionesParteCuerpo = new BigInteger("-1");

            }
        } catch (Exception e) {
            log.error("ERROR ControlPartesCuerpo verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarPartesCuerpo.isEmpty() || !crearPartesCuerpo.isEmpty() || !modificarPartesCuerpo.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarPartesCuerpo() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("Realizando guardarPartesCuerpo");
            if (!borrarPartesCuerpo.isEmpty()) {
                administrarPartesCuerpo.borrarPartesCuerpo(borrarPartesCuerpo);
                //mostrarBorrados
                registrosBorrados = borrarPartesCuerpo.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarPartesCuerpo.clear();
            }
            if (!modificarPartesCuerpo.isEmpty()) {
                administrarPartesCuerpo.modificarPartesCuerpo(modificarPartesCuerpo);
                modificarPartesCuerpo.clear();
            }
            if (!crearPartesCuerpo.isEmpty()) {
                administrarPartesCuerpo.crearPartesCuerpo(crearPartesCuerpo);
                crearPartesCuerpo.clear();
            }
            log.info("Se guardaron los datos con exito");
            listPartesCuerpo = null;
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
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
                editarPartesCuerpo = listPartesCuerpo.get(index);
            }
            if (tipoLista == 1) {
                editarPartesCuerpo = filtrarPartesCuerpo.get(index);
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

    public void agregarNuevoPartesCuerpo() {
        log.info("agregarNuevoPartesCuerpo");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoPartesCuerpo.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            log.info("codigo en Motivo Cambio Cargo: " + nuevoPartesCuerpo.getCodigo());

            for (int x = 0; x < listPartesCuerpo.size(); x++) {
                if (listPartesCuerpo.get(x).getCodigo().equals(nuevoPartesCuerpo.getCodigo())) {
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
        if (nuevoPartesCuerpo.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoPartesCuerpo.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
                bandera = 0;
                filtrarPartesCuerpo = null;
                tipoLista = 0;
            }
            log.info("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoPartesCuerpo.setSecuencia(l);

            crearPartesCuerpo.add(nuevoPartesCuerpo);

            listPartesCuerpo.add(nuevoPartesCuerpo);
            nuevoPartesCuerpo = new PartesCuerpo();
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
            infoRegistro = "Cantidad de registros: " + listPartesCuerpo.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroPartesCuerpo').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoPartesCuerpo() {
        log.info("limpiarNuevoPartesCuerpo");
        nuevoPartesCuerpo = new PartesCuerpo();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoPartesCuerpo() {
        log.info("duplicandoPartesCuerpo");
        if (index >= 0) {
            duplicarPartesCuerpo = new PartesCuerpo();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarPartesCuerpo.setSecuencia(l);
                duplicarPartesCuerpo.setCodigo(listPartesCuerpo.get(index).getCodigo());
                duplicarPartesCuerpo.setDescripcion(listPartesCuerpo.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarPartesCuerpo.setSecuencia(l);
                duplicarPartesCuerpo.setCodigo(filtrarPartesCuerpo.get(index).getCodigo());
                duplicarPartesCuerpo.setDescripcion(filtrarPartesCuerpo.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPartesCuerpo').show()");
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
        log.error("ConfirmarDuplicar codigo " + duplicarPartesCuerpo.getCodigo());
        log.error("ConfirmarDuplicar Descripcion " + duplicarPartesCuerpo.getDescripcion());

        if (duplicarPartesCuerpo.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listPartesCuerpo.size(); x++) {
                if (listPartesCuerpo.get(x).getCodigo().equals(duplicarPartesCuerpo.getCodigo())) {
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
        if (duplicarPartesCuerpo.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarPartesCuerpo.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("bandera");
            contador++;

        }

        if (contador == 2) {

            log.info("Datos Duplicando: " + duplicarPartesCuerpo.getSecuencia() + "  " + duplicarPartesCuerpo.getCodigo());
            if (crearPartesCuerpo.contains(duplicarPartesCuerpo)) {
                log.info("Ya lo contengo.");
            }
            listPartesCuerpo.add(duplicarPartesCuerpo);
            crearPartesCuerpo.add(duplicarPartesCuerpo);
            RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listPartesCuerpo.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosPartesCuerpo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosPartesCuerpo");
                bandera = 0;
                filtrarPartesCuerpo = null;
                tipoLista = 0;
            }
            duplicarPartesCuerpo = new PartesCuerpo();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroPartesCuerpo').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarPartesCuerpo() {
        duplicarPartesCuerpo = new PartesCuerpo();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPartesCuerpoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "PARTESCUERPO", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosPartesCuerpoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "PARTESCUERPO", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("lol");
        if (!listPartesCuerpo.isEmpty()) {
            if (secRegistro != null) {
                log.info("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "PARTESCUERPO"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("PARTESCUERPO")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<PartesCuerpo> getListPartesCuerpo() {
        if (listPartesCuerpo == null) {
            log.info("ControlPartesCuerpo getListPartesCuerpo");
            listPartesCuerpo = administrarPartesCuerpo.consultarPartesCuerpo();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listPartesCuerpo == null || listPartesCuerpo.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listPartesCuerpo.size();
        }
        return listPartesCuerpo;
    }

    public void setListPartesCuerpo(List<PartesCuerpo> listPartesCuerpo) {
        this.listPartesCuerpo = listPartesCuerpo;
    }

    public List<PartesCuerpo> getFiltrarPartesCuerpo() {
        return filtrarPartesCuerpo;
    }

    public void setFiltrarPartesCuerpo(List<PartesCuerpo> filtrarPartesCuerpo) {
        this.filtrarPartesCuerpo = filtrarPartesCuerpo;
    }

    public PartesCuerpo getNuevoPartesCuerpo() {
        return nuevoPartesCuerpo;
    }

    public void setNuevoPartesCuerpo(PartesCuerpo nuevoPartesCuerpo) {
        this.nuevoPartesCuerpo = nuevoPartesCuerpo;
    }

    public PartesCuerpo getDuplicarPartesCuerpo() {
        return duplicarPartesCuerpo;
    }

    public void setDuplicarPartesCuerpo(PartesCuerpo duplicarPartesCuerpo) {
        this.duplicarPartesCuerpo = duplicarPartesCuerpo;
    }

    public PartesCuerpo getEditarPartesCuerpo() {
        return editarPartesCuerpo;
    }

    public void setEditarPartesCuerpo(PartesCuerpo editarPartesCuerpo) {
        this.editarPartesCuerpo = editarPartesCuerpo;
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

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public PartesCuerpo getParteCuerpoSeleccionado() {
        return parteCuerpoSeleccionado;
    }

    public void setParteCuerpoSeleccionado(PartesCuerpo parteCuerpoSeleccionado) {
        this.parteCuerpoSeleccionado = parteCuerpoSeleccionado;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
