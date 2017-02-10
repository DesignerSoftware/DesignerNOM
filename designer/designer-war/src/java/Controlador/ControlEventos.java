/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Eventos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEventosInterface;
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
public class ControlEventos implements Serializable {

    @EJB
    AdministrarEventosInterface administrarEventos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Eventos> listEventos;
    private List<Eventos> filtrarEventos;
    private List<Eventos> crearEventos;
    private List<Eventos> modificarEventos;
    private List<Eventos> borrarEventos;
    private Eventos nuevoEvento;
    private Eventos duplicarEvento;
    private Eventos editarEvento;
    private Eventos eventoSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado, activarLov;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion, organizador, objetivo;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger vigenciasEventos;
    private Integer a;
    private int tamano;
    private DataTable tablaC;
    private String infoRegistro;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String backUpOrganizador;
    private String backUpObjetivo;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlEventos() {
        listEventos = null;
        crearEventos = new ArrayList<Eventos>();
        modificarEventos = new ArrayList<Eventos>();
        borrarEventos = new ArrayList<Eventos>();
        permitirIndex = true;
        editarEvento = new Eventos();
        nuevoEvento = new Eventos();
        duplicarEvento = new Eventos();
        a = null;
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
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina();
        } else {
            String pagActual = "evento";
            //Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParametros.put("paginaAnterior", pagActual);
            //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
            controlListaNavegacion.adicionarPagina(pagActual);
        }
        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEventos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPag(String pagina) {
        paginaAnterior = pagina;
        listEventos = null;
        getListEventos();
        deshabilitarBotonLov();
        if (!listEventos.isEmpty()) {
            eventoSeleccionado = listEventos.get(0);
        }
    }

    public String retornarPagina() {
        return paginaAnterior;
    }

    public void cambiarIndice(Eventos evento, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            eventoSeleccionado = evento;
            cualCelda = celda;
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = eventoSeleccionado.getCodigo();
                } else {
                    backUpCodigo = eventoSeleccionado.getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = eventoSeleccionado.getDescripcion();
                } else {
                    backUpDescripcion = eventoSeleccionado.getDescripcion();
                }
            }
            if (cualCelda == 2) {
                if (tipoLista == 0) {
                    backUpOrganizador = eventoSeleccionado.getOrganizador();
                } else {
                    backUpOrganizador = eventoSeleccionado.getOrganizador();
                }
            }
            if (cualCelda == 3) {
                if (tipoLista == 0) {
                    backUpObjetivo = eventoSeleccionado.getObjetivo();
                } else {
                    backUpObjetivo = eventoSeleccionado.getObjetivo();
                }
            }
            eventoSeleccionado.getSecuencia();

        }
    }

    public void asignarIndex(Eventos evento, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlEventos.asignarIndex \n");
            eventoSeleccionado = evento;
            deshabilitarBotonLov();
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlEventos.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
        deshabilitarBotonLov();
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvento:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvento:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            organizador = (Column) c.getViewRoot().findComponent("form:datosEvento:organizador");
            organizador.setFilterStyle("display: none; visibility: hidden;");
            objetivo = (Column) c.getViewRoot().findComponent("form:datosEvento:objetivo");
            objetivo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEvento");
            bandera = 0;
            filtrarEventos = null;
            tipoLista = 0;
        }

        borrarEventos.clear();
        crearEventos.clear();
        modificarEventos.clear();
        eventoSeleccionado = null;
        k = 0;
        listEventos = null;
        guardado = true;
        permitirIndex = true;
        getListEventos();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosEvento");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvento:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvento:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            organizador = (Column) c.getViewRoot().findComponent("form:datosEvento:organizador");
            organizador.setFilterStyle("display: none; visibility: hidden;");
            objetivo = (Column) c.getViewRoot().findComponent("form:datosEvento:objetivo");
            objetivo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEvento");
            bandera = 0;
            filtrarEventos = null;
            tipoLista = 0;
        }

        borrarEventos.clear();
        crearEventos.clear();
        modificarEventos.clear();
        eventoSeleccionado = null;
        k = 0;
        listEventos = null;
        guardado = true;
        permitirIndex = true;
        getListEventos();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        context.update("form:informacionRegistro");
        context.update("form:datosEvento");
        context.update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvento:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvento:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            organizador = (Column) c.getViewRoot().findComponent("form:datosEvento:organizador");
            organizador.setFilterStyle("width: 85% !important");
            objetivo = (Column) c.getViewRoot().findComponent("form:datosEvento:objetivo");
            objetivo.setFilterStyle("width: 85% !important");

            RequestContext.getCurrentInstance().update("form:datosEvento");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvento:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvento:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            organizador = (Column) c.getViewRoot().findComponent("form:datosEvento:organizador");
            organizador.setFilterStyle("display: none; visibility: hidden;");
            objetivo = (Column) c.getViewRoot().findComponent("form:datosEvento:objetivo");
            objetivo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEvento");
            bandera = 0;
            filtrarEventos = null;
            tipoLista = 0;
        }
    }

    public void modificarEventos(Eventos evento, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR Eventos");
        eventoSeleccionado = evento;

        int contador = 0;
        boolean banderita = false;

        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR Eventos, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearEventos.contains(eventoSeleccionado)) {
                    if (eventoSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        eventoSeleccionado.setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listEventos.size(); j++) {
                            if (eventoSeleccionado.getCodigo() == listEventos.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                        if (contador > 0) {
                            eventoSeleccionado.setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (eventoSeleccionado.getDescripcion().isEmpty()) {
                        eventoSeleccionado.setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (eventoSeleccionado.getDescripcion().equals(" ")) {
                        eventoSeleccionado.setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (eventoSeleccionado.getOrganizador().isEmpty()) {
                        eventoSeleccionado.setOrganizador(backUpOrganizador);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (eventoSeleccionado.getOrganizador().equals(" ")) {
                        eventoSeleccionado.setOrganizador(backUpOrganizador);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (eventoSeleccionado.getObjetivo().isEmpty()) {
                        eventoSeleccionado.setObjetivo(backUpObjetivo);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (eventoSeleccionado.getObjetivo().equals(" ")) {
                        eventoSeleccionado.setObjetivo(backUpObjetivo);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {
                        if (modificarEventos.isEmpty()) {
                            modificarEventos.add(eventoSeleccionado);
                        } else if (!modificarEventos.contains(eventoSeleccionado)) {
                            modificarEventos.add(eventoSeleccionado);
                        }
                        if (guardado == true) {
                            guardado = false;
                        }
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                } else {
                    if (eventoSeleccionado.getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        eventoSeleccionado.setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listEventos.size(); j++) {
                            if (eventoSeleccionado.getCodigo() == listEventos.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                        if (contador > 0) {
                            eventoSeleccionado.setCodigo(backUpCodigo);
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (eventoSeleccionado.getDescripcion().isEmpty()) {
                        eventoSeleccionado.setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (eventoSeleccionado.getDescripcion().equals(" ")) {
                        eventoSeleccionado.setDescripcion(backUpDescripcion);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (eventoSeleccionado.getOrganizador().isEmpty()) {
                        eventoSeleccionado.setOrganizador(backUpOrganizador);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (eventoSeleccionado.getOrganizador().equals(" ")) {
                        eventoSeleccionado.setOrganizador(backUpOrganizador);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (eventoSeleccionado.getObjetivo().isEmpty()) {
                        eventoSeleccionado.setObjetivo(backUpObjetivo);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }
                    if (eventoSeleccionado.getObjetivo().equals(" ")) {
                        eventoSeleccionado.setObjetivo(backUpObjetivo);
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                    }

                    if (banderita == true) {

                        if (guardado == true) {
                            guardado = false;
                        }
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }

                }
            } else if (!crearEventos.contains(eventoSeleccionado)) {
                if (eventoSeleccionado.getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    eventoSeleccionado.setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < listEventos.size(); j++) {
                        if (eventoSeleccionado.getCodigo() == listEventos.get(j).getCodigo()) {
                            contador++;
                        }
                    }
                    for (int j = 0; j < filtrarEventos.size(); j++) {
                        if (eventoSeleccionado.getCodigo() == filtrarEventos.get(j).getCodigo()) {
                            contador++;
                        }
                    }
                    if (contador > 0) {
                        eventoSeleccionado.setCodigo(backUpCodigo);
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (eventoSeleccionado.getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    eventoSeleccionado.setDescripcion(backUpDescripcion);
                }
                if (eventoSeleccionado.getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    eventoSeleccionado.setDescripcion(backUpDescripcion);
                    banderita = false;
                }
                if (eventoSeleccionado.getObjetivo().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    eventoSeleccionado.setObjetivo(backUpObjetivo);
                }
                if (eventoSeleccionado.getObjetivo().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    eventoSeleccionado.setObjetivo(backUpObjetivo);
                    banderita = false;
                }
                if (eventoSeleccionado.getOrganizador().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    eventoSeleccionado.setOrganizador(backUpOrganizador);
                }
                if (eventoSeleccionado.getOrganizador().equals(" ")) {
                    eventoSeleccionado.setOrganizador(backUpOrganizador);
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                }

                if (banderita == true) {
                    if (modificarEventos.isEmpty()) {
                        modificarEventos.add(eventoSeleccionado);
                    } else if (!modificarEventos.contains(eventoSeleccionado)) {
                        modificarEventos.add(eventoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    contador = 0;
                }
            } else {
                if (eventoSeleccionado.getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    eventoSeleccionado.setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < listEventos.size(); j++) {
                        if (eventoSeleccionado.getCodigo() == listEventos.get(j).getCodigo()) {
                            contador++;
                        }
                    }
                    for (int j = 0; j < filtrarEventos.size(); j++) {
                        if (eventoSeleccionado.getCodigo() == filtrarEventos.get(j).getCodigo()) {
                            contador++;
                        }
                    }
                    if (contador > 0) {
                        eventoSeleccionado.setCodigo(backUpCodigo);
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (eventoSeleccionado.getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    eventoSeleccionado.setDescripcion(backUpDescripcion);
                }
                if (eventoSeleccionado.getDescripcion().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    eventoSeleccionado.setDescripcion(backUpDescripcion);
                    banderita = false;
                }
                if (eventoSeleccionado.getObjetivo().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    eventoSeleccionado.setObjetivo(backUpObjetivo);
                }
                if (eventoSeleccionado.getObjetivo().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    eventoSeleccionado.setObjetivo(backUpObjetivo);
                    banderita = false;
                }
                if (eventoSeleccionado.getOrganizador().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    eventoSeleccionado.setOrganizador(backUpOrganizador);
                }
                if (eventoSeleccionado.getOrganizador().equals(" ")) {
                    eventoSeleccionado.setOrganizador(backUpOrganizador);
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                }

                if (banderita == true) {
                    if (modificarEventos.isEmpty()) {
                        modificarEventos.add(eventoSeleccionado);
                    } else if (!modificarEventos.contains(eventoSeleccionado)) {
                        modificarEventos.add(eventoSeleccionado);
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    contador = 0;
                }
            }
            RequestContext.getCurrentInstance().update("form:datosEvento");
        }

    }

    public void borrarEventos() {

        if (eventoSeleccionado != null) {
            System.out.println("Entro a borrarEventos");
            if (!modificarEventos.isEmpty() && modificarEventos.contains(eventoSeleccionado)) {
                int modIndex = modificarEventos.indexOf(eventoSeleccionado);
                modificarEventos.remove(modIndex);
                borrarEventos.add(eventoSeleccionado);
            } else if (!crearEventos.isEmpty() && crearEventos.contains(eventoSeleccionado)) {
                int crearIndex = crearEventos.indexOf(eventoSeleccionado);
                crearEventos.remove(crearIndex);
            } else {
                borrarEventos.add(eventoSeleccionado);
            }
            listEventos.remove(eventoSeleccionado);
            if (tipoLista == 1) {
                filtrarEventos.remove(eventoSeleccionado);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEvento");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            eventoSeleccionado = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");

        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        try {
            System.err.println("Control Secuencia de verificarBorrado  a borrar");
            vigenciasEventos = administrarEventos.verificarVigenciasEventos(eventoSeleccionado.getSecuencia());

            if (vigenciasEventos.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrarEventos();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                eventoSeleccionado = null;

                vigenciasEventos = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlEstadosCiviles verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarEventos.isEmpty() || !crearEventos.isEmpty() || !modificarEventos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarEventos() {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext c = FacesContext.getCurrentInstance();

        if (guardado == false) {
            if (bandera == 1) {
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosEvento:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosEvento:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                organizador = (Column) c.getViewRoot().findComponent("form:datosEvento:organizador");
                organizador.setFilterStyle("display: none; visibility: hidden;");
                objetivo = (Column) c.getViewRoot().findComponent("form:datosEvento:objetivo");
                objetivo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEvento");
            }
            System.out.println("Realizando GruposInfAdicionales");
            if (!borrarEventos.isEmpty()) {
                administrarEventos.borrarEventos(borrarEventos);
                //mostrarBorrados
                registrosBorrados = borrarEventos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarEventos.clear();
            }
            if (!crearEventos.isEmpty()) {
                administrarEventos.crearEventos(crearEventos);
                crearEventos.clear();
            }
            if (!modificarEventos.isEmpty()) {
                administrarEventos.modificarEventos(modificarEventos);
                modificarEventos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listEventos = null;
            getListEventos();
            contarRegistros();
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosEvento");
            k = 0;
        }
        eventoSeleccionado = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (eventoSeleccionado != null) {
            if (tipoLista == 0) {
                deshabilitarBotonLov();
                editarEvento = eventoSeleccionado;
            }
            if (tipoLista == 1) {
                deshabilitarBotonLov();
                editarEvento = eventoSeleccionado;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editOrganizador");
                RequestContext.getCurrentInstance().execute("PF('editOrganizador').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editObjetivo");
                RequestContext.getCurrentInstance().execute("PF('editObjetivo').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoEventos() {
        System.out.println("Agregar GruposInfAdicionales");
        int contador = 0;
        int duplicados = 0;

        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoEvento.getCodigo() == a) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoEvento.getCodigo());

            for (int x = 0; x < listEventos.size(); x++) {
                if (listEventos.get(x).getCodigo() == nuevoEvento.getCodigo()) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está en uso. Por favor ingrese un código válido";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoEvento.getDescripcion() == (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            System.out.println("bandera");
            contador++;

        }
        if (nuevoEvento.getOrganizador() == (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            System.out.println("bandera");
            contador++;
        }

        if (nuevoEvento.getObjetivo() == (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 4) {
            FacesContext c = FacesContext.getCurrentInstance();

            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosEvento:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosEvento:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                organizador = (Column) c.getViewRoot().findComponent("form:datosEvento:organizador");
                organizador.setFilterStyle("display: none; visibility: hidden;");
                objetivo = (Column) c.getViewRoot().findComponent("form:datosEvento:objetivo");
                objetivo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEvento");
                bandera = 0;
                filtrarEventos = null;
                tipoLista = 0;
                tamano = 270;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoEvento.setSecuencia(l);
            crearEventos.add(nuevoEvento);
            listEventos.add(nuevoEvento);
            eventoSeleccionado = nuevoEvento;

            nuevoEvento = new Eventos();

            RequestContext.getCurrentInstance().update("form:datosEvento");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEvento').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoEvento");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoEvento').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoEventos() {
        System.out.println("limpiarNuevoEstadosCiviles");
        nuevoEvento = new Eventos();

    }

    //------------------------------------------------------------------------------
    public void duplicarEventos() {
        System.out.println("duplicarEstadosCiviles");
        if (eventoSeleccionado != null) {
            duplicarEvento = new Eventos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarEvento.setSecuencia(l);
                duplicarEvento.setCodigo(eventoSeleccionado.getCodigo());
                duplicarEvento.setDescripcion(eventoSeleccionado.getDescripcion());
                duplicarEvento.setOrganizador(eventoSeleccionado.getOrganizador());
                duplicarEvento.setObjetivo(eventoSeleccionado.getObjetivo());
            }
            if (tipoLista == 1) {
                duplicarEvento.setSecuencia(l);
                duplicarEvento.setCodigo(eventoSeleccionado.getCodigo());
                duplicarEvento.setDescripcion(eventoSeleccionado.getDescripcion());
                duplicarEvento.setOrganizador(eventoSeleccionado.getOrganizador());
                duplicarEvento.setObjetivo(eventoSeleccionado.getObjetivo());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvento').show()");

        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarEvento.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listEventos.size(); x++) {
                if (listEventos.get(x).getCodigo() == duplicarEvento.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya está en uso.Por favor ingrese un código válido";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarEvento.getDescripcion() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarEvento.getOrganizador() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            System.out.println("Bandera : ");
            contador++;
        }
        if (duplicarEvento.getObjetivo() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 4) {

            System.out.println("Datos Duplicando: " + duplicarEvento.getSecuencia() + "  " + duplicarEvento.getCodigo());
            if (crearEventos.contains(duplicarEvento)) {
                System.out.println("Ya lo contengo.");
            }
            listEventos.add(duplicarEvento);
            crearEventos.add(duplicarEvento);
            RequestContext.getCurrentInstance().update("form:datosEvento");
            eventoSeleccionado = duplicarEvento;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();

                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosEvento:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosEvento:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                organizador = (Column) c.getViewRoot().findComponent("form:datosEvento:organizador");
                organizador.setFilterStyle("display: none; visibility: hidden;");
                objetivo = (Column) c.getViewRoot().findComponent("form:datosEvento:objetivo");
                objetivo.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEvento");
                bandera = 0;
                filtrarEventos = null;
                tipoLista = 0;
                tamano = 270;
            }
            duplicarEvento = new Eventos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvento').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarEvento");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarEvento').show()");
        }
    }

    public void limpiarDuplicarEventos() {
        duplicarEvento = new Eventos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEventoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "EVENTOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        eventoSeleccionado = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEventoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "EVENTOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        eventoSeleccionado = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (eventoSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(eventoSeleccionado.getSecuencia(), "EVENTOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            System.out.println("resultado: " + resultado);
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
        } else if (administrarRastros.verificarHistoricosTabla("EVENTOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR ControlEventos eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void recordarSeleccion() {
        if (eventoSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosEvento");
            tablaC.setSelection(eventoSeleccionado);
        }
    }

    public void habilitarBotonLov() {
        activarLov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    /////////////////////////////GETS Y SETS///////////////////////////////
    public List<Eventos> getListEventos() {
        if (listEventos == null) {
            listEventos = administrarEventos.consultarEventos();
        }
        return listEventos;
    }

    public void setListEventos(List<Eventos> listEventos) {
        this.listEventos = listEventos;
    }

    public List<Eventos> getFiltrarEventos() {
        return filtrarEventos;
    }

    public void setFiltrarEventos(List<Eventos> filtrarEventos) {
        this.filtrarEventos = filtrarEventos;
    }

    public Eventos getNuevoEvento() {
        return nuevoEvento;
    }

    public void setNuevoEvento(Eventos nuevoEvento) {
        this.nuevoEvento = nuevoEvento;
    }

    public Eventos getDuplicarEvento() {
        return duplicarEvento;
    }

    public void setDuplicarEvento(Eventos duplicarEvento) {
        this.duplicarEvento = duplicarEvento;
    }

    public Eventos getEditarEvento() {
        return editarEvento;
    }

    public void setEditarEvento(Eventos editarEvento) {
        this.editarEvento = editarEvento;
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

    public Eventos getEventoSeleccionado() {
        return eventoSeleccionado;
    }

    public void setEventoSeleccionado(Eventos eventoSeleccionado) {
        this.eventoSeleccionado = eventoSeleccionado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEvento");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
