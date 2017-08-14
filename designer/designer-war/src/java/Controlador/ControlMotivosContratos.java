/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import InterfaceAdministrar.AdministrarMotivosContratosInterface;
import Entidades.MotivosContratos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
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
public class ControlMotivosContratos implements Serializable {

   private static Logger log = Logger.getLogger(ControlMotivosContratos.class);

    @EJB
    AdministrarMotivosContratosInterface administrarMotivosContratos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<MotivosContratos> listMotivosContratos;
    private List<MotivosContratos> filtrarMotivosContratos;
    private List<MotivosContratos> crearMotivoContratos;
    private List<MotivosContratos> modificarMotivoContrato;
    private List<MotivosContratos> borrarMotivoContrato;
    private MotivosContratos nuevoMotivoContrato;
    private MotivosContratos duplicarMotivoContrato;
    private MotivosContratos editarMotivoContrato;
    private MotivosContratos motivoContratoSeleccionado;
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
    private BigInteger borradoVC;
    private int tamano;

    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String infoRegistro;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlMotivosContratos() {

        listMotivosContratos = null;
        crearMotivoContratos = new ArrayList<MotivosContratos>();
        modificarMotivoContrato = new ArrayList<MotivosContratos>();
        borrarMotivoContrato = new ArrayList<MotivosContratos>();
        permitirIndex = true;
        editarMotivoContrato = new MotivosContratos();
        nuevoMotivoContrato = new MotivosContratos();
        duplicarMotivoContrato = new MotivosContratos();
        guardado = true;
        tamano = 320;
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
        String pagActual = "motivocontrato";
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
            administrarMotivosContratos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
            log.error("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        motivoContratoSeleccionado = null;
        contarRegistros();
    }

    public void cambiarIndice(int indice, int celda) {
        log.error("TIPO LISTA = " + tipoLista);
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listMotivosContratos.get(index).getCodigo();
                    log.info(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listMotivosContratos.get(index).getNombre();
                    log.info(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listMotivosContratos.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarMotivosContratos.get(index).getCodigo();
                    log.info(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarMotivosContratos.get(index).getNombre();
                    log.info(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = filtrarMotivosContratos.get(index).getSecuencia();
            }
        }
        log.info("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            log.info("\n ENTRE A ControlMotiviosCambiosCargos.asignarIndex \n");
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
            log.warn("Error ControlMotiviosCambiosCargos.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        log.info("ControlMotivosContratos cancelarMOdificacion");
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();

            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            bandera = 0;
            filtrarMotivosContratos = null;
            tipoLista = 0;
            tamano = 320;
        }

        borrarMotivoContrato.clear();
        crearMotivoContratos.clear();
        modificarMotivoContrato.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosContratos = null;
        guardado = true;
        permitirIndex = true;
        getListMotivosContratos();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            bandera = 0;
            filtrarMotivosContratos = null;
            tipoLista = 0;
            tamano = 320;
        }
        borrarMotivoContrato.clear();
        crearMotivoContratos.clear();
        modificarMotivoContrato.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listMotivosContratos = null;
        guardado = true;
        permitirIndex = true;
        getListMotivosContratos();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        cancelarModificacion();
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 300;
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            log.info("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 320;
            log.info("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            bandera = 0;
            filtrarMotivosContratos = null;
            tipoLista = 0;
        }
        RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
    }

    public void modificarMotivosContrato(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        log.error("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!crearMotivoContratos.contains(listMotivosContratos.get(indice))) {
                    if (listMotivosContratos.get(indice).getCodigo() == a) {
                        banderita = false;
                        listMotivosContratos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosContratos.size(); j++) {
                            if (j != indice) {
                                if (listMotivosContratos.get(indice).getCodigo() == listMotivosContratos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listMotivosContratos.get(indice).setCodigo(backUpCodigo);
                        } else {
                            banderita = true;
                        }
                    }
                    if (listMotivosContratos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosContratos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listMotivosContratos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosContratos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (banderita == true) {
                        if (modificarMotivoContrato.isEmpty()) {
                            modificarMotivoContrato.add(listMotivosContratos.get(indice));
                        } else if (!modificarMotivoContrato.contains(listMotivosContratos.get(indice))) {
                            modificarMotivoContrato.add(listMotivosContratos.get(indice));
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
                    if (listMotivosContratos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosContratos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listMotivosContratos.size(); j++) {
                            if (j != indice) {
                                if (listMotivosContratos.get(indice).getCodigo() == listMotivosContratos.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                            listMotivosContratos.get(indice).setCodigo(backUpCodigo);
                        } else {
                            banderita = true;
                        }
                    }
                    if (listMotivosContratos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosContratos.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listMotivosContratos.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listMotivosContratos.get(indice).setNombre(backUpDescripcion);
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
            } else if (!crearMotivoContratos.contains(filtrarMotivosContratos.get(indice))) {
                if (filtrarMotivosContratos.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarMotivosContratos.get(indice).setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < listMotivosContratos.size(); j++) {
                        if (j != indice) {
                            if (filtrarMotivosContratos.get(indice).getCodigo() == listMotivosContratos.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                        filtrarMotivosContratos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        banderita = true;
                    }
                }
                if (filtrarMotivosContratos.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarMotivosContratos.get(indice).setNombre(backUpDescripcion);
                }
                if (filtrarMotivosContratos.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarMotivosContratos.get(indice).setNombre(backUpDescripcion);
                }
                if (banderita == true) {
                    if (modificarMotivoContrato.isEmpty()) {
                        modificarMotivoContrato.add(filtrarMotivosContratos.get(indice));
                    } else if (!modificarMotivoContrato.contains(filtrarMotivosContratos.get(indice))) {
                        modificarMotivoContrato.add(filtrarMotivosContratos.get(indice));
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
                if (filtrarMotivosContratos.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarMotivosContratos.get(indice).setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < listMotivosContratos.size(); j++) {
                        if (j != indice) {
                            if (filtrarMotivosContratos.get(indice).getCodigo() == listMotivosContratos.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                        filtrarMotivosContratos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        banderita = true;
                    }
                }
                if (filtrarMotivosContratos.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarMotivosContratos.get(indice).setNombre(backUpDescripcion);
                }
                if (filtrarMotivosContratos.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarMotivosContratos.get(indice).setNombre(backUpDescripcion);
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
            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrarMotivosContratos() {
        if (index >= 0) {
            if (tipoLista == 0) {
                log.info("Entro a borrarMotivosContratos");
                if (!modificarMotivoContrato.isEmpty() && modificarMotivoContrato.contains(listMotivosContratos.get(index))) {
                    int modIndex = modificarMotivoContrato.indexOf(listMotivosContratos.get(index));
                    modificarMotivoContrato.remove(modIndex);
                    borrarMotivoContrato.add(listMotivosContratos.get(index));
                } else if (!crearMotivoContratos.isEmpty() && crearMotivoContratos.contains(listMotivosContratos.get(index))) {
                    int crearIndex = crearMotivoContratos.indexOf(listMotivosContratos.get(index));
                    crearMotivoContratos.remove(crearIndex);
                } else {
                    borrarMotivoContrato.add(listMotivosContratos.get(index));
                }
                listMotivosContratos.remove(index);
            }
            if (tipoLista == 1) {
                log.info("borrarMotivosContratos ");
                if (!modificarMotivoContrato.isEmpty() && modificarMotivoContrato.contains(filtrarMotivosContratos.get(index))) {
                    int modIndex = modificarMotivoContrato.indexOf(filtrarMotivosContratos.get(index));
                    modificarMotivoContrato.remove(modIndex);
                    borrarMotivoContrato.add(filtrarMotivosContratos.get(index));
                } else if (!crearMotivoContratos.isEmpty() && crearMotivoContratos.contains(filtrarMotivosContratos.get(index))) {
                    int crearIndex = crearMotivoContratos.indexOf(filtrarMotivosContratos.get(index));
                    crearMotivoContratos.remove(crearIndex);
                } else {
                    borrarMotivoContrato.add(filtrarMotivosContratos.get(index));
                }
                int VCIndex = listMotivosContratos.indexOf(filtrarMotivosContratos.get(index));
                listMotivosContratos.remove(VCIndex);
                filtrarMotivosContratos.remove(index);
            }
            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            contarRegistros();
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
        try {
            if (tipoLista == 0) {
                borradoVC = administrarMotivosContratos.contarVigenciasTiposContratosMotivoContrato(listMotivosContratos.get(index).getSecuencia());
            } else {
                borradoVC = administrarMotivosContratos.contarVigenciasTiposContratosMotivoContrato(filtrarMotivosContratos.get(index).getSecuencia());
            }
            if (borradoVC.equals(new BigInteger("0"))) {
                log.info("Borrado==0");
                borrarMotivosContratos();
            } else {
                log.info("Borrado>0");
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                borradoVC = new BigInteger("-1");
            }
        } catch (Exception e) {
            log.error("ERROR ControlMotivosContratos verificarBorrado ERROR " + e);
        }
    }

    public void guardarMotivosContratos() {
        if (guardado == false) {
            log.info("Realizando Motivos Contratos");
            if (!borrarMotivoContrato.isEmpty()) {
                log.info("Borrando...");
                administrarMotivosContratos.borrarMotivosContratos(borrarMotivoContrato);
                registrosBorrados = borrarMotivoContrato.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarMotivoContrato.clear();
            }
            if (!crearMotivoContratos.isEmpty()) {
                administrarMotivosContratos.crearMotivosContratos(crearMotivoContratos);
            }
            crearMotivoContratos.clear();
            if (!modificarMotivoContrato.isEmpty()) {
                administrarMotivosContratos.modificarMotivosContratos(modificarMotivoContrato);
                modificarMotivoContrato.clear();
            }
            log.info("Se guardaron los datos con exito");
            listMotivosContratos = null;
            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            k = 0;
            guardado = true;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarMotivoContrato = listMotivosContratos.get(index);
            }
            if (tipoLista == 1) {
                editarMotivoContrato = filtrarMotivosContratos.get(index);
            }
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

    public void agregarNuevoMotivoContrato() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        if (nuevoMotivoContrato.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
        } else {
            for (int x = 0; x < listMotivosContratos.size(); x++) {
                if (listMotivosContratos.get(x).getCodigo() == nuevoMotivoContrato.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
            } else {
                contador++;
            }
        }
        if (nuevoMotivoContrato.getNombre() == (null)) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
        } else if (nuevoMotivoContrato.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
                bandera = 0;
                filtrarMotivosContratos = null;
                tipoLista = 0;
                tamano = 320;
            }
            log.info("Despues de la bandera");

            //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
            k++;
            l = BigInteger.valueOf(k);
            nuevoMotivoContrato.setSecuencia(l);

            crearMotivoContratos.add(nuevoMotivoContrato);
            listMotivosContratos.add(nuevoMotivoContrato);
            nuevoMotivoContrato = new MotivosContratos();

            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            contarRegistros();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroMotivoContratos').hide()");
            index = -1;
            secRegistro = null;
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoMotivoContratos() {
        log.info("limpiarnuevoMotivoContrato");
        nuevoMotivoContrato = new MotivosContratos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicarMotivosContratos() {
        log.info("duplicarMotivosCambiosCargos");
        if (index >= 0) {
            duplicarMotivoContrato = new MotivosContratos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarMotivoContrato.setSecuencia(l);
                duplicarMotivoContrato.setCodigo(listMotivosContratos.get(index).getCodigo());
                duplicarMotivoContrato.setNombre(listMotivosContratos.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarMotivoContrato.setSecuencia(l);
                duplicarMotivoContrato.setCodigo(filtrarMotivosContratos.get(index).getCodigo());
                duplicarMotivoContrato.setNombre(filtrarMotivosContratos.get(index).getNombre());
            }

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivosCambiosCargos");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosContratos').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        log.error("ESTOY EN CONFIRMAR DUPLICAR CONTROLTIPOSCENTROSCOSTOS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        Integer a = 0;
        a = null;
        log.error("ConfirmarDuplicar codigo " + duplicarMotivoContrato.getCodigo());
        log.error("ConfirmarDuplicar nombre " + duplicarMotivoContrato.getNombre());

        if (duplicarMotivoContrato.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            log.info("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listMotivosContratos.size(); x++) {
                if (listMotivosContratos.get(x).getCodigo() == duplicarMotivoContrato.getCodigo()) {
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
        if (duplicarMotivoContrato.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   *Nombre \n";
            log.info("Mensaje validacion : " + mensajeValidacion);

        } else {
            log.info("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            log.info("Datos Duplicando: " + duplicarMotivoContrato.getSecuencia() + "  " + duplicarMotivoContrato.getCodigo());
            if (crearMotivoContratos.contains(duplicarMotivoContrato)) {
                log.info("Ya lo contengo.");
            }
            listMotivosContratos.add(duplicarMotivoContrato);
            crearMotivoContratos.add(duplicarMotivoContrato);
            RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
            contarRegistros();
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                tamano = 320;
                codigo = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosMotivoContrato:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosMotivoContrato");
                bandera = 0;
                filtrarMotivosContratos = null;
                tipoLista = 0;
            }
            duplicarMotivoContrato = new MotivosContratos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroMotivosContratos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarduplicarMotivosContratos() {
        duplicarMotivoContrato = new MotivosContratos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoContratoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "MotivosContratosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosMotivoContratoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "MotivosContratosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        if (!listMotivosContratos.isEmpty()) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "MOTIVOSCONTRATOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("MOTIVOSCONTRATOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

//-----------------------//---------------//----------------------//------------
    public List<MotivosContratos> getListMotivosContratos() {
        if (listMotivosContratos == null) {
            listMotivosContratos = administrarMotivosContratos.consultarMotivosContratos();
        }
        return listMotivosContratos;
    }

    public void setListMotivosContratos(List<MotivosContratos> listMotivosContratos) {
        this.listMotivosContratos = listMotivosContratos;
    }

    public List<MotivosContratos> getFiltrarMotivosContratos() {
        return filtrarMotivosContratos;
    }

    public void setFiltrarMotivosContratos(List<MotivosContratos> filtrarMotivosContratos) {
        this.filtrarMotivosContratos = filtrarMotivosContratos;
    }

    public MotivosContratos getNuevoMotivoContrato() {
        return nuevoMotivoContrato;
    }

    public void setNuevoMotivoContrato(MotivosContratos nuevoMotivoContrato) {
        this.nuevoMotivoContrato = nuevoMotivoContrato;
    }

    public MotivosContratos getEditarMotivoContrato() {
        return editarMotivoContrato;
    }

    public void setEditarMotivoContrato(MotivosContratos editarMotivoContrato) {
        this.editarMotivoContrato = editarMotivoContrato;
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

    public MotivosContratos getDuplicarMotivoContrato() {
        return duplicarMotivoContrato;
    }

    public void setDuplicarMotivoContrato(MotivosContratos duplicarMotivoContrato) {
        this.duplicarMotivoContrato = duplicarMotivoContrato;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public MotivosContratos getMotivoContratoSeleccionado() {
        return motivoContratoSeleccionado;
    }

    public void setMotivoContratoSeleccionado(MotivosContratos motivoContratoSeleccionado) {
        this.motivoContratoSeleccionado = motivoContratoSeleccionado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosMotivoContrato");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
