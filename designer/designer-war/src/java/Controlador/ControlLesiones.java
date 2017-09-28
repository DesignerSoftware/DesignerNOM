/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Lesiones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarLesionesInterface;
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
public class ControlLesiones implements Serializable {

   private static Logger log = Logger.getLogger(ControlLesiones.class);

    @EJB
    AdministrarLesionesInterface administrarLesiones;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Lesiones> listLesiones;
    private List<Lesiones> filtrarLesiones;
    private List<Lesiones> crearLesiones;
    private List<Lesiones> modificarLesiones;
    private List<Lesiones> borrarLesiones;
    private Lesiones nuevoLesiones;
    private Lesiones duplicarLesiones;
    private Lesiones editarLesiones;
    private Lesiones lesionSeleccionada;
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
    private Integer backupCodigo;
    private String backupDescripcion;
    private String infoRegistro;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlLesiones() {
        listLesiones = null;
        crearLesiones = new ArrayList<Lesiones>();
        modificarLesiones = new ArrayList<Lesiones>();
        borrarLesiones = new ArrayList<Lesiones>();
        permitirIndex = true;
        editarLesiones = new Lesiones();
        nuevoLesiones = new Lesiones();
        duplicarLesiones = new Lesiones();
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
        String pagActual = "lesion";
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
            administrarLesiones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        try {
            log.info("\n ENTRE A ControlLesiones.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarLesiones.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            log.warn("Error ControlLesiones eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        log.error("TIPO LISTA = " + tipoLista);
        log.error("permitirIndex  " + permitirIndex);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listLesiones.get(index).getSecuencia();
            if (tipoLista == 0) {
                backupCodigo = listLesiones.get(index).getCodigo();
                backupDescripcion = listLesiones.get(index).getDescripcion();
            } else if (tipoLista == 1) {
                backupCodigo = filtrarLesiones.get(index).getCodigo();
                backupDescripcion = filtrarLesiones.get(index).getDescripcion();
            }
        }
        log.info("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            log.info("\n ENTRE A ControlLesiones.asignarIndex \n");
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
            log.warn("Error ControlLesiones.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosLesiones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosLesiones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLesiones");
            bandera = 0;
            filtrarLesiones = null;
            tipoLista = 0;
        }

        borrarLesiones.clear();
        crearLesiones.clear();
        modificarLesiones.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listLesiones = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();

        if (listLesiones == null || listLesiones.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listLesiones.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosLesiones");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosLesiones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosLesiones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLesiones");
            bandera = 0;
            filtrarLesiones = null;
            tipoLista = 0;
        }

        borrarLesiones.clear();
        crearLesiones.clear();
        modificarLesiones.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listLesiones = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();

        if (listLesiones == null || listLesiones.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listLesiones.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosLesiones");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosLesiones:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosLesiones:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosLesiones");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            log.info("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosLesiones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosLesiones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosLesiones");
            bandera = 0;
            filtrarLesiones = null;
            tipoLista = 0;
        }
    }

    public void modificarLesiones(int indice, String confirmarCambio, String valorConfirmar) {
        log.error("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        boolean banderita1 = false;

        RequestContext context = RequestContext.getCurrentInstance();
        log.error("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            log.error("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearLesiones.contains(listLesiones.get(indice))) {

                    log.info("backupCodigo : " + backupCodigo);
                    log.info("backupDescripcion : " + backupDescripcion);

                    if (listLesiones.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listLesiones.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < listLesiones.size(); j++) {
                            if (j != indice) {
                                if (listLesiones.get(indice).getCodigo() == listLesiones.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listLesiones.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listLesiones.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listLesiones.get(indice).setDescripcion(backupDescripcion);
                    } else if (listLesiones.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listLesiones.get(indice).setDescripcion(backupDescripcion);

                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (modificarLesiones.isEmpty()) {
                            modificarLesiones.add(listLesiones.get(indice));
                        } else if (!modificarLesiones.contains(listLesiones.get(indice))) {
                            modificarLesiones.add(listLesiones.get(indice));
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
                    RequestContext.getCurrentInstance().update("form:datosLesiones");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                } else {

                    log.info("backupCodigo : " + backupCodigo);
                    log.info("backupDescripcion : " + backupDescripcion);

                    if (listLesiones.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listLesiones.get(indice).setCodigo(backupCodigo);
                    } else {
                        for (int j = 0; j < listLesiones.size(); j++) {
                            if (j != indice) {
                                if (listLesiones.get(indice).getCodigo() == listLesiones.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }

                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listLesiones.get(indice).setCodigo(backupCodigo);
                        } else {
                            banderita = true;
                        }

                    }
                    if (listLesiones.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listLesiones.get(indice).setDescripcion(backupDescripcion);
                    } else if (listLesiones.get(indice).getDescripcion().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita1 = false;
                        listLesiones.get(indice).setDescripcion(backupDescripcion);

                    } else {
                        banderita1 = true;
                    }

                    if (banderita == true && banderita1 == true) {
                        if (guardado == true) {
                            guardado = false;
                        }
                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");

                    }
                    index = -1;
                    secRegistro = null;
                    RequestContext.getCurrentInstance().update("form:datosLesiones");
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                }
            } else if (!crearLesiones.contains(filtrarLesiones.get(indice))) {
                if (filtrarLesiones.get(indice).getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarLesiones.get(indice).setCodigo(backupCodigo);
                } else {
                    for (int j = 0; j < filtrarLesiones.size(); j++) {
                        if (j != indice) {
                            if (filtrarLesiones.get(indice).getCodigo() == listLesiones.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    for (int j = 0; j < listLesiones.size(); j++) {
                        if (j != indice) {
                            if (filtrarLesiones.get(indice).getCodigo() == listLesiones.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                        filtrarLesiones.get(indice).setCodigo(backupCodigo);

                    } else {
                        banderita = true;
                    }

                }

                if (filtrarLesiones.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita1 = false;
                    filtrarLesiones.get(indice).setDescripcion(backupDescripcion);
                }
                if (filtrarLesiones.get(indice).getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita1 = false;
                    filtrarLesiones.get(indice).setDescripcion(backupDescripcion);
                }

                if (banderita == true && banderita1 == true) {
                    if (modificarLesiones.isEmpty()) {
                        modificarLesiones.add(filtrarLesiones.get(indice));
                    } else if (!modificarLesiones.contains(filtrarLesiones.get(indice))) {
                        modificarLesiones.add(filtrarLesiones.get(indice));
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
                if (filtrarLesiones.get(indice).getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarLesiones.get(indice).setCodigo(backupCodigo);
                } else {
                    for (int j = 0; j < filtrarLesiones.size(); j++) {
                        if (j != indice) {
                            if (filtrarLesiones.get(indice).getCodigo() == listLesiones.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    for (int j = 0; j < listLesiones.size(); j++) {
                        if (j != indice) {
                            if (filtrarLesiones.get(indice).getCodigo() == listLesiones.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                        filtrarLesiones.get(indice).setCodigo(backupCodigo);

                    } else {
                        banderita = true;
                    }

                }

                if (filtrarLesiones.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita1 = false;
                    filtrarLesiones.get(indice).setDescripcion(backupDescripcion);
                }
                if (filtrarLesiones.get(indice).getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita1 = false;
                    filtrarLesiones.get(indice).setDescripcion(backupDescripcion);
                }

                if (banderita == true && banderita1 == true) {
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
            RequestContext.getCurrentInstance().update("form:datosLesiones");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoLesiones() {

        if (index >= 0) {
            if (tipoLista == 0) {
                log.info("Entro a borrandoLesiones");
                if (!modificarLesiones.isEmpty() && modificarLesiones.contains(listLesiones.get(index))) {
                    int modIndex = modificarLesiones.indexOf(listLesiones.get(index));
                    modificarLesiones.remove(modIndex);
                    borrarLesiones.add(listLesiones.get(index));
                } else if (!crearLesiones.isEmpty() && crearLesiones.contains(listLesiones.get(index))) {
                    int crearIndex = crearLesiones.indexOf(listLesiones.get(index));
                    crearLesiones.remove(crearIndex);
                } else {
                    borrarLesiones.add(listLesiones.get(index));
                }
                listLesiones.remove(index);
            }
            if (tipoLista == 1) {
                log.info("borrandoLesiones ");
                if (!modificarLesiones.isEmpty() && modificarLesiones.contains(filtrarLesiones.get(index))) {
                    int modIndex = modificarLesiones.indexOf(filtrarLesiones.get(index));
                    modificarLesiones.remove(modIndex);
                    borrarLesiones.add(filtrarLesiones.get(index));
                } else if (!crearLesiones.isEmpty() && crearLesiones.contains(filtrarLesiones.get(index))) {
                    int crearIndex = crearLesiones.indexOf(filtrarLesiones.get(index));
                    crearLesiones.remove(crearIndex);
                } else {
                    borrarLesiones.add(filtrarLesiones.get(index));
                }
                int VCIndex = listLesiones.indexOf(filtrarLesiones.get(index));
                listLesiones.remove(VCIndex);
                filtrarLesiones.remove(index);

            }
            infoRegistro = "Cantidad de registros: " + listLesiones.size();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosLesiones");
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
        BigInteger contarDetallesLicensiasLesion;
        BigInteger contarSoAccidentesDomesticosLesion;

        try {
            log.error("Control Secuencia de ControlLesiones ");
            if (tipoLista == 0) {
                contarDetallesLicensiasLesion = administrarLesiones.contarDetallesLicensiasLesion(listLesiones.get(index).getSecuencia());
                contarSoAccidentesDomesticosLesion = administrarLesiones.contarSoAccidentesDomesticosLesion(listLesiones.get(index).getSecuencia());
            } else {
                contarDetallesLicensiasLesion = administrarLesiones.contarDetallesLicensiasLesion(filtrarLesiones.get(index).getSecuencia());
                contarSoAccidentesDomesticosLesion = administrarLesiones.contarSoAccidentesDomesticosLesion(filtrarLesiones.get(index).getSecuencia());
            }
            if (contarDetallesLicensiasLesion.equals(new BigInteger("0")) && contarSoAccidentesDomesticosLesion.equals(new BigInteger("0"))) {
                log.info("Borrado==0");
                borrandoLesiones();
            } else {
                log.info("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                contarDetallesLicensiasLesion = new BigInteger("-1");
                contarSoAccidentesDomesticosLesion = new BigInteger("-1");

            }
        } catch (Exception e) {
            log.error("ERROR ControlLesiones verificarBorrado ERROR  ", e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarLesiones.isEmpty() || !crearLesiones.isEmpty() || !modificarLesiones.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarLesiones() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            log.info("Realizando guardarLesiones");
            if (!borrarLesiones.isEmpty()) {
                administrarLesiones.borrarLesiones(borrarLesiones);
                //mostrarBorrados
                registrosBorrados = borrarLesiones.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarLesiones.clear();
            }
            if (!modificarLesiones.isEmpty()) {
                administrarLesiones.modificarLesiones(modificarLesiones);
                modificarLesiones.clear();
            }
            if (!crearLesiones.isEmpty()) {
                administrarLesiones.crearLesiones(crearLesiones);
                crearLesiones.clear();
            }
            log.info("Se guardaron los datos con exito");
            listLesiones = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosLesiones");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarLesiones = listLesiones.get(index);
            }
            if (tipoLista == 1) {
                editarLesiones = filtrarLesiones.get(index);
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

    public void agregarNuevoLesiones() {
        log.info("agregarNuevoLesiones");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoLesiones.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            log.info("codigo en Motivo Cambio Cargo: " + nuevoLesiones.getCodigo());

            for (int x = 0; x < listLesiones.size(); x++) {
                if (listLesiones.get(x).getCodigo() == nuevoLesiones.getCodigo()) {
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
        if (nuevoLesiones.getDescripcion() == null || nuevoLesiones.getDescripcion().isEmpty()) {
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosLesiones:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosLesiones:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosLesiones");
                bandera = 0;
                filtrarLesiones = null;
                tipoLista = 0;
            }
            log.info("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoLesiones.setSecuencia(l);

            crearLesiones.add(nuevoLesiones);

            listLesiones.add(nuevoLesiones);
            infoRegistro = "Cantidad de registros: " + listLesiones.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            nuevoLesiones = new Lesiones();
            RequestContext.getCurrentInstance().update("form:datosLesiones");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroLesiones').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoLesiones() {
        log.info("limpiarNuevoLesiones");
        nuevoLesiones = new Lesiones();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoLesiones() {
        log.info("duplicandoLesiones");
        if (index >= 0) {
            duplicarLesiones = new Lesiones();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarLesiones.setSecuencia(l);
                duplicarLesiones.setCodigo(listLesiones.get(index).getCodigo());
                duplicarLesiones.setDescripcion(listLesiones.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarLesiones.setSecuencia(l);
                duplicarLesiones.setCodigo(filtrarLesiones.get(index).getCodigo());
                duplicarLesiones.setDescripcion(filtrarLesiones.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroLesiones').show()");
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
        log.error("ConfirmarDuplicar codigo " + duplicarLesiones.getCodigo());
        log.error("ConfirmarDuplicar Descripcion " + duplicarLesiones.getDescripcion());

        if (duplicarLesiones.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listLesiones.size(); x++) {
                if (listLesiones.get(x).getCodigo() == duplicarLesiones.getCodigo()) {
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
        if (duplicarLesiones.getDescripcion() == null || duplicarLesiones.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Descripción \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            log.info("Datos Duplicando: " + duplicarLesiones.getSecuencia() + "  " + duplicarLesiones.getCodigo());
            if (crearLesiones.contains(duplicarLesiones)) {
                log.info("Ya lo contengo.");
            }
            listLesiones.add(duplicarLesiones);
            crearLesiones.add(duplicarLesiones);
            RequestContext.getCurrentInstance().update("form:datosLesiones");
            index = -1;
            infoRegistro = "Cantidad de registros: " + listLesiones.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosLesiones:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosLesiones:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosLesiones");
                bandera = 0;
                filtrarLesiones = null;
                tipoLista = 0;
            }
            duplicarLesiones = new Lesiones();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroLesiones').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarLesiones() {
        duplicarLesiones = new Lesiones();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLesionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "LESIONES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosLesionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "LESIONES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        log.info("lol");
        if (!listLesiones.isEmpty()) {
            if (secRegistro != null) {
                log.info("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "LESIONES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("LESIONES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Lesiones> getListLesiones() {
        if (listLesiones == null) {
            listLesiones = administrarLesiones.consultarLesiones();
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listLesiones == null || listLesiones.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listLesiones.size();
        }
        RequestContext.getCurrentInstance().update("form:infoRegistro");
        return listLesiones;
    }

    public void setListLesiones(List<Lesiones> listLesiones) {
        this.listLesiones = listLesiones;
    }

    public List<Lesiones> getFiltrarLesiones() {
        return filtrarLesiones;
    }

    public void setFiltrarLesiones(List<Lesiones> filtrarLesiones) {
        this.filtrarLesiones = filtrarLesiones;
    }

    public Lesiones getNuevoLesiones() {
        return nuevoLesiones;
    }

    public void setNuevoLesiones(Lesiones nuevoLesiones) {
        this.nuevoLesiones = nuevoLesiones;
    }

    public Lesiones getDuplicarLesiones() {
        return duplicarLesiones;
    }

    public void setDuplicarLesiones(Lesiones duplicarLesiones) {
        this.duplicarLesiones = duplicarLesiones;
    }

    public Lesiones getEditarLesiones() {
        return editarLesiones;
    }

    public void setEditarLesiones(Lesiones editarLesiones) {
        this.editarLesiones = editarLesiones;
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

    public Lesiones getLesionSeleccionada() {
        return lesionSeleccionada;
    }

    public void setLesionSeleccionada(Lesiones lesionSeleccionada) {
        this.lesionSeleccionada = lesionSeleccionada;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
