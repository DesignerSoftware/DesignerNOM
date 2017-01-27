/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Tiposviajeros;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposViajerosInterface;
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
public class ControlTiposViajeros implements Serializable {

    @EJB
    AdministrarTiposViajerosInterface administrarTiposViajeros;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Tiposviajeros> listTiposViajeros;
    private List<Tiposviajeros> filtrarTiposViajeros;
    private List<Tiposviajeros> crearTiposViajeros;
    private List<Tiposviajeros> modificarTiposViajeros;
    private List<Tiposviajeros> borrarTiposViajeros;
    private Tiposviajeros nuevoTiposViajeros;
    private Tiposviajeros duplicarTiposViajeros;
    private Tiposviajeros editarTiposViajeros;
    private Tiposviajeros tiposViajeroSeleccionado;
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
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String paginaAnterior = "nominaf";

    public ControlTiposViajeros() {
        listTiposViajeros = null;
        crearTiposViajeros = new ArrayList<Tiposviajeros>();
        modificarTiposViajeros = new ArrayList<Tiposviajeros>();
        borrarTiposViajeros = new ArrayList<Tiposviajeros>();
        permitirIndex = true;
        editarTiposViajeros = new Tiposviajeros();
        nuevoTiposViajeros = new Tiposviajeros();
        duplicarTiposViajeros = new Tiposviajeros();
        guardado = true;
        tamano = 320;
        mapParametros.put("paginaAnterior", paginaAnterior);
        System.out.println("controlTiposViajeros Constructor");
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlTiposViajeros PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposViajeros.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
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
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina();
        } else {
            String pagActual = "tipoviajero";
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

    public void inicializarLista() {
        getListTiposViajeros();
        if (listTiposViajeros != null) {
            tiposViajeroSeleccionado = listTiposViajeros.get(0);
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        tiposViajeroSeleccionado = null;
        contarRegistros();
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listTiposViajeros.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listTiposViajeros.get(index).getNombre();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listTiposViajeros.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarTiposViajeros.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarTiposViajeros.get(index).getNombre();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarTiposViajeros.get(index).getSecuencia();
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposViajeros.asignarIndex \n");
            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("Tipo Actualizacion: " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR ControlTiposViajeros.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }
    private String infoRegistro;

    public void cancelarModificacion() {
        index = -1;
        secRegistro = null;
        cerrarFiltrado();
        borrarTiposViajeros.clear();
        crearTiposViajeros.clear();
        modificarTiposViajeros.clear();
        listTiposViajeros = null;
        k = 0;
        guardado = true;
        permitirIndex = true;
        getListTiposViajeros();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        cerrarFiltrado();
        borrarTiposViajeros.clear();
        crearTiposViajeros.clear();
        modificarTiposViajeros.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposViajeros = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("salir");
    }

    public void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        codigo = (Column) c.getViewRoot().findComponent("form:datosTiposViajeros:codigo");
        codigo.setFilterStyle("display: none; visibility: hidden;");
        descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposViajeros:descripcion");
        descripcion.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
        bandera = 0;
        filtrarTiposViajeros = null;
        tipoLista = 0;
        tamano = 320;
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 300;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposViajeros:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposViajeros:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
        }
    }

    public void modificarTiposViajeros(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
        index = indice;

        int contador = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EMPRESAS, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearTiposViajeros.contains(listTiposViajeros.get(indice))) {
                    if (listTiposViajeros.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposViajeros.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposViajeros.size(); j++) {
                            if (j != indice) {
                                if (listTiposViajeros.get(indice).getCodigo().equals(listTiposViajeros.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposViajeros.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposViajeros.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposViajeros.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listTiposViajeros.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposViajeros.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposViajeros.isEmpty()) {
                            modificarTiposViajeros.add(listTiposViajeros.get(indice));
                        } else if (!modificarTiposViajeros.contains(listTiposViajeros.get(indice))) {
                            modificarTiposViajeros.add(listTiposViajeros.get(indice));
                        }
                        if (guardado) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                } else {
                    if (listTiposViajeros.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposViajeros.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposViajeros.size(); j++) {
                            if (j != indice) {
                                if (listTiposViajeros.get(indice).getCodigo().equals(listTiposViajeros.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposViajeros.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposViajeros.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposViajeros.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listTiposViajeros.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposViajeros.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {

                        if (guardado) {
                            guardado = false;
                        }

                    } else {
                        RequestContext.getCurrentInstance().update("form:validacionModificar");
                        RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                    }
                    index = -1;
                    secRegistro = null;
                }
            } else if (!crearTiposViajeros.contains(filtrarTiposViajeros.get(indice))) {
                if (filtrarTiposViajeros.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposViajeros.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < filtrarTiposViajeros.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposViajeros.get(indice).getCodigo().equals(filtrarTiposViajeros.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarTiposViajeros.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarTiposViajeros.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposViajeros.get(indice).setNombre(backUpDescripcion);
                }
                if (filtrarTiposViajeros.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposViajeros.get(indice).setNombre(backUpDescripcion);
                }

                if (banderita == true) {
                    if (modificarTiposViajeros.isEmpty()) {
                        modificarTiposViajeros.add(filtrarTiposViajeros.get(indice));
                    } else if (!modificarTiposViajeros.contains(filtrarTiposViajeros.get(indice))) {
                        modificarTiposViajeros.add(filtrarTiposViajeros.get(indice));
                    }
                    if (guardado) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
                index = -1;
                secRegistro = null;
            } else {
                if (filtrarTiposViajeros.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposViajeros.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {
                    for (int j = 0; j < filtrarTiposViajeros.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposViajeros.get(indice).getCodigo().equals(filtrarTiposViajeros.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarTiposViajeros.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarTiposViajeros.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposViajeros.get(indice).setNombre(backUpDescripcion);
                }
                if (filtrarTiposViajeros.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposViajeros.get(indice).setNombre(backUpDescripcion);
                }

                if (banderita == true) {

                    if (guardado) {
                        guardado = false;
                    }

                } else {
                    RequestContext.getCurrentInstance().update("form:validacionModificar");
                    RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoTiposViajeros() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposViajeros");
                if (!modificarTiposViajeros.isEmpty() && modificarTiposViajeros.contains(listTiposViajeros.get(index))) {
                    int modIndex = modificarTiposViajeros.indexOf(listTiposViajeros.get(index));
                    modificarTiposViajeros.remove(modIndex);
                    borrarTiposViajeros.add(listTiposViajeros.get(index));
                } else if (!crearTiposViajeros.isEmpty() && crearTiposViajeros.contains(listTiposViajeros.get(index))) {
                    int crearIndex = crearTiposViajeros.indexOf(listTiposViajeros.get(index));
                    crearTiposViajeros.remove(crearIndex);
                } else {
                    borrarTiposViajeros.add(listTiposViajeros.get(index));
                }
                listTiposViajeros.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposViajeros ");
                if (!modificarTiposViajeros.isEmpty() && modificarTiposViajeros.contains(filtrarTiposViajeros.get(index))) {
                    int modIndex = modificarTiposViajeros.indexOf(filtrarTiposViajeros.get(index));
                    modificarTiposViajeros.remove(modIndex);
                    borrarTiposViajeros.add(filtrarTiposViajeros.get(index));
                } else if (!crearTiposViajeros.isEmpty() && crearTiposViajeros.contains(filtrarTiposViajeros.get(index))) {
                    int crearIndex = crearTiposViajeros.indexOf(filtrarTiposViajeros.get(index));
                    crearTiposViajeros.remove(crearIndex);
                } else {
                    borrarTiposViajeros.add(filtrarTiposViajeros.get(index));
                }
                int VCIndex = listTiposViajeros.indexOf(filtrarTiposViajeros.get(index));
                listTiposViajeros.remove(VCIndex);
                filtrarTiposViajeros.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
            contarRegistros();
            index = -1;
            secRegistro = null;

            if (guardado) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        BigInteger contarTiposLegalizaciones;
        BigInteger contarVigenciasViajeros;

        try {
            System.err.println("Control Secuencia de ControlTiposViajeros ");
            if (tipoLista == 0) {
                contarTiposLegalizaciones = administrarTiposViajeros.contarTiposLegalizaciones(listTiposViajeros.get(index).getSecuencia());
                contarVigenciasViajeros = administrarTiposViajeros.contarVigenciasViajeros(listTiposViajeros.get(index).getSecuencia());
            } else {
                contarTiposLegalizaciones = administrarTiposViajeros.contarTiposLegalizaciones(filtrarTiposViajeros.get(index).getSecuencia());
                contarVigenciasViajeros = administrarTiposViajeros.contarVigenciasViajeros(filtrarTiposViajeros.get(index).getSecuencia());
            }
            if (contarTiposLegalizaciones.equals(new BigInteger("0"))
                    && contarVigenciasViajeros.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposViajeros();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                contarTiposLegalizaciones = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposViajeros verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposViajeros.isEmpty() || !crearTiposViajeros.isEmpty() || !modificarTiposViajeros.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTiposViajeros() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarTiposViajeros");
            if (!borrarTiposViajeros.isEmpty()) {
                administrarTiposViajeros.borrarTiposViajeros(borrarTiposViajeros);
                //mostrarBorrados
                registrosBorrados = borrarTiposViajeros.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarTiposViajeros.clear();
            }
            if (!modificarTiposViajeros.isEmpty()) {
                administrarTiposViajeros.modificarTiposViajeros(modificarTiposViajeros);
                modificarTiposViajeros.clear();
            }
            if (!crearTiposViajeros.isEmpty()) {
                administrarTiposViajeros.crearTiposViajeros(crearTiposViajeros);
                crearTiposViajeros.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposViajeros = null;
            RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
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
                editarTiposViajeros = listTiposViajeros.get(index);
            }
            if (tipoLista == 1) {
                editarTiposViajeros = filtrarTiposViajeros.get(index);
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
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoTiposViajeros() {
        System.out.println("agregarNuevoTiposViajeros");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposViajeros.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTiposViajeros.getCodigo());

            for (int x = 0; x < listTiposViajeros.size(); x++) {
                if (listTiposViajeros.get(x).getCodigo().equals(nuevoTiposViajeros.getCodigo())) {
                    duplicados++;
                }
            }
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoTiposViajeros.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoTiposViajeros.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                cerrarFiltrado();
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposViajeros.setSecuencia(l);

            crearTiposViajeros.add(nuevoTiposViajeros);
            listTiposViajeros.add(nuevoTiposViajeros);
            tiposViajeroSeleccionado = listTiposViajeros.get(listTiposViajeros.indexOf(nuevoTiposViajeros));
            nuevoTiposViajeros = new Tiposviajeros();
            RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
            contarRegistros();

            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposViajeros').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposViajeros() {
        System.out.println("limpiarNuevoTiposViajeros");
        nuevoTiposViajeros = new Tiposviajeros();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposViajeros() {
        System.out.println("duplicandoTiposViajeros");
        if (index >= 0) {
            duplicarTiposViajeros = new Tiposviajeros();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposViajeros.setSecuencia(l);
                duplicarTiposViajeros.setCodigo(listTiposViajeros.get(index).getCodigo());
                duplicarTiposViajeros.setNombre(listTiposViajeros.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarTiposViajeros.setSecuencia(l);
                duplicarTiposViajeros.setCodigo(filtrarTiposViajeros.get(index).getCodigo());
                duplicarTiposViajeros.setNombre(filtrarTiposViajeros.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposViajeros').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposViajeros.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposViajeros.getNombre());

        if (duplicarTiposViajeros.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposViajeros.size(); x++) {
                if (listTiposViajeros.get(x).getCodigo().equals(duplicarTiposViajeros.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarTiposViajeros.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarTiposViajeros.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarTiposViajeros.getSecuencia() + "  " + duplicarTiposViajeros.getCodigo());
            if (crearTiposViajeros.contains(duplicarTiposViajeros)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposViajeros.add(duplicarTiposViajeros);
            crearTiposViajeros.add(duplicarTiposViajeros);
            tiposViajeroSeleccionado = listTiposViajeros.get(listTiposViajeros.indexOf(duplicarTiposViajeros));
            RequestContext.getCurrentInstance().update("form:datosTiposViajeros");
            index = -1;
            secRegistro = null;
            if (guardado) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();

            if (bandera == 1) {
                cerrarFiltrado();
            }
            duplicarTiposViajeros = new Tiposviajeros();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposViajeros').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposViajeros() {
        duplicarTiposViajeros = new Tiposviajeros();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposViajerosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSVIAJEROS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposViajerosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSVIAJEROS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (secRegistro != null) {
            int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSVIAJEROS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSVIAJEROS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Tiposviajeros> getListTiposViajeros() {
        if (listTiposViajeros == null) {
            listTiposViajeros = administrarTiposViajeros.consultarTiposViajeros();
        }
        return listTiposViajeros;
    }

    public void setListTiposViajeros(List<Tiposviajeros> listTiposViajeros) {
        this.listTiposViajeros = listTiposViajeros;
    }

    public List<Tiposviajeros> getFiltrarTiposViajeros() {
        return filtrarTiposViajeros;
    }

    public void setFiltrarTiposViajeros(List<Tiposviajeros> filtrarTiposViajeros) {
        this.filtrarTiposViajeros = filtrarTiposViajeros;
    }

    public Tiposviajeros getNuevoTiposViajeros() {
        return nuevoTiposViajeros;
    }

    public void setNuevoTiposViajeros(Tiposviajeros nuevoTiposViajeros) {
        this.nuevoTiposViajeros = nuevoTiposViajeros;
    }

    public Tiposviajeros getDuplicarTiposViajeros() {
        return duplicarTiposViajeros;
    }

    public void setDuplicarTiposViajeros(Tiposviajeros duplicarTiposViajeros) {
        this.duplicarTiposViajeros = duplicarTiposViajeros;
    }

    public Tiposviajeros getEditarTiposViajeros() {
        return editarTiposViajeros;
    }

    public void setEditarTiposViajeros(Tiposviajeros editarTiposViajeros) {
        this.editarTiposViajeros = editarTiposViajeros;
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

    public Tiposviajeros getTiposViajerosSeleccionado() {
        return tiposViajeroSeleccionado;
    }

    public void setTiposViajerosSeleccionado(Tiposviajeros tiposViajeroSeleccionado) {
        this.tiposViajeroSeleccionado = tiposViajeroSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposViajeros");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }
}
