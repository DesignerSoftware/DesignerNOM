/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposAccidentes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposAccidentesInterface;
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
public class ControlTiposAccidentes implements Serializable {

    @EJB
    AdministrarTiposAccidentesInterface administrarTiposAccidentes;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposAccidentes> listTiposAccidentes;
    private List<TiposAccidentes> filtrarTiposAccidentes;
    private List<TiposAccidentes> crearTiposAccidentes;
    private List<TiposAccidentes> modificarTiposAccidentes;
    private List<TiposAccidentes> borrarTiposAccidentes;
    private TiposAccidentes nuevoTiposAccidentes;
    private TiposAccidentes duplicarTiposAccidentes;
    private TiposAccidentes editarTiposAccidentes;
    private TiposAccidentes tiposAccidentesSeleccionado;
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
    private String backUpCodigo;
    private String backUpDescripcion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTiposAccidentes() {
        listTiposAccidentes = null;
        crearTiposAccidentes = new ArrayList<TiposAccidentes>();
        modificarTiposAccidentes = new ArrayList<TiposAccidentes>();
        borrarTiposAccidentes = new ArrayList<TiposAccidentes>();
        permitirIndex = true;
        editarTiposAccidentes = new TiposAccidentes();
        nuevoTiposAccidentes = new TiposAccidentes();
        duplicarTiposAccidentes = new TiposAccidentes();
        guardado = true;
        tamano = 270;
        mapParametros.put("paginaAnterior", paginaAnterior);
        System.out.println("controlTiposAccidentes Constructor");
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlTiposAccidentes PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposAccidentes.obtenerConexion(ses.getId());
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
            String pagActual = "tipoaccidente";
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

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlTiposAccidentes.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarTiposAccidentes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposAccidentes eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listTiposAccidentes.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listTiposAccidentes.get(index).getNombre();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listTiposAccidentes.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarTiposAccidentes.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarTiposAccidentes.get(index).getNombre();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarTiposAccidentes.get(index).getSecuencia();
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposAccidentes.asignarIndex \n");
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
            System.out.println("ERROR ControlTiposAccidentes.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAccidentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAccidentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            bandera = 0;
            filtrarTiposAccidentes = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposAccidentes.clear();
        crearTiposAccidentes.clear();
        modificarTiposAccidentes.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposAccidentes = null;
        guardado = true;
        permitirIndex = true;
        getListTiposAccidentes();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposAccidentes == null || listTiposAccidentes.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposAccidentes.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAccidentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAccidentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            bandera = 0;
            filtrarTiposAccidentes = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposAccidentes.clear();
        crearTiposAccidentes.clear();
        modificarTiposAccidentes.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposAccidentes = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAccidentes:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAccidentes:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAccidentes:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAccidentes:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            bandera = 0;
            filtrarTiposAccidentes = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposAccidentes(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearTiposAccidentes.contains(listTiposAccidentes.get(indice))) {
                    if (listTiposAccidentes.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposAccidentes.size(); j++) {
                            if (j != indice) {
                                if (listTiposAccidentes.get(indice).getCodigo().equals(listTiposAccidentes.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposAccidentes.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAccidentes.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listTiposAccidentes.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAccidentes.get(indice).setNombre(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposAccidentes.isEmpty()) {
                            modificarTiposAccidentes.add(listTiposAccidentes.get(indice));
                        } else if (!modificarTiposAccidentes.contains(listTiposAccidentes.get(indice))) {
                            modificarTiposAccidentes.add(listTiposAccidentes.get(indice));
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
                    if (listTiposAccidentes.get(indice).getCodigo() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposAccidentes.size(); j++) {
                            if (j != indice) {
                                if (listTiposAccidentes.get(indice).getCodigo().equals(listTiposAccidentes.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposAccidentes.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAccidentes.get(indice).setNombre(backUpDescripcion);
                    }
                    if (listTiposAccidentes.get(indice).getNombre().equals(" ")) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposAccidentes.get(indice).setNombre(backUpDescripcion);
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
            } else if (!crearTiposAccidentes.contains(filtrarTiposAccidentes.get(indice))) {
                if (filtrarTiposAccidentes.get(indice).getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < filtrarTiposAccidentes.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposAccidentes.get(indice).getCodigo().equals(filtrarTiposAccidentes.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarTiposAccidentes.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposAccidentes.get(indice).setNombre(backUpDescripcion);
                }
                if (filtrarTiposAccidentes.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposAccidentes.get(indice).setNombre(backUpDescripcion);
                }

                if (banderita == true) {
                    if (modificarTiposAccidentes.isEmpty()) {
                        modificarTiposAccidentes.add(filtrarTiposAccidentes.get(indice));
                    } else if (!modificarTiposAccidentes.contains(filtrarTiposAccidentes.get(indice))) {
                        modificarTiposAccidentes.add(filtrarTiposAccidentes.get(indice));
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
                if (filtrarTiposAccidentes.get(indice).getCodigo() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                    banderita = false;
                } else {

                    for (int j = 0; j < filtrarTiposAccidentes.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposAccidentes.get(indice).getCodigo().equals(filtrarTiposAccidentes.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarTiposAccidentes.get(indice).setCodigo(backUpCodigo);
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarTiposAccidentes.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposAccidentes.get(indice).setNombre(backUpDescripcion);
                }
                if (filtrarTiposAccidentes.get(indice).getNombre().equals(" ")) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposAccidentes.get(indice).setNombre(backUpDescripcion);
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
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoTiposAccidentes() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposAccidentes");
                if (!modificarTiposAccidentes.isEmpty() && modificarTiposAccidentes.contains(listTiposAccidentes.get(index))) {
                    int modIndex = modificarTiposAccidentes.indexOf(listTiposAccidentes.get(index));
                    modificarTiposAccidentes.remove(modIndex);
                    borrarTiposAccidentes.add(listTiposAccidentes.get(index));
                } else if (!crearTiposAccidentes.isEmpty() && crearTiposAccidentes.contains(listTiposAccidentes.get(index))) {
                    int crearIndex = crearTiposAccidentes.indexOf(listTiposAccidentes.get(index));
                    crearTiposAccidentes.remove(crearIndex);
                } else {
                    borrarTiposAccidentes.add(listTiposAccidentes.get(index));
                }
                listTiposAccidentes.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposAccidentes ");
                if (!modificarTiposAccidentes.isEmpty() && modificarTiposAccidentes.contains(filtrarTiposAccidentes.get(index))) {
                    int modIndex = modificarTiposAccidentes.indexOf(filtrarTiposAccidentes.get(index));
                    modificarTiposAccidentes.remove(modIndex);
                    borrarTiposAccidentes.add(filtrarTiposAccidentes.get(index));
                } else if (!crearTiposAccidentes.isEmpty() && crearTiposAccidentes.contains(filtrarTiposAccidentes.get(index))) {
                    int crearIndex = crearTiposAccidentes.indexOf(filtrarTiposAccidentes.get(index));
                    crearTiposAccidentes.remove(crearIndex);
                } else {
                    borrarTiposAccidentes.add(filtrarTiposAccidentes.get(index));
                }
                int VCIndex = listTiposAccidentes.indexOf(filtrarTiposAccidentes.get(index));
                listTiposAccidentes.remove(VCIndex);
                filtrarTiposAccidentes.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            infoRegistro = "Cantidad de registros: " + listTiposAccidentes.size();
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
        System.out.println("Estoy en verificarBorrado");
        BigInteger contarAccidentesTipoAccidente;
        BigInteger contarSoAccidentesMedicosTipoAccidente;

        try {
            System.err.println("Control Secuencia de ControlTiposAccidentes ");
            if (tipoLista == 0) {
                contarAccidentesTipoAccidente = administrarTiposAccidentes.contarAccidentesTipoAccidente(listTiposAccidentes.get(index).getSecuencia());
                contarSoAccidentesMedicosTipoAccidente = administrarTiposAccidentes.contarSoAccidentesMedicosTipoAccidente(listTiposAccidentes.get(index).getSecuencia());
            } else {
                contarAccidentesTipoAccidente = administrarTiposAccidentes.contarAccidentesTipoAccidente(filtrarTiposAccidentes.get(index).getSecuencia());
                contarSoAccidentesMedicosTipoAccidente = administrarTiposAccidentes.contarSoAccidentesMedicosTipoAccidente(filtrarTiposAccidentes.get(index).getSecuencia());
            }
            if (contarAccidentesTipoAccidente.equals(new BigInteger("0"))
                    && contarSoAccidentesMedicosTipoAccidente.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposAccidentes();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                contarAccidentesTipoAccidente = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposAccidentes verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposAccidentes.isEmpty() || !crearTiposAccidentes.isEmpty() || !modificarTiposAccidentes.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTiposAccidentes() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarTiposAccidentes");
            if (!borrarTiposAccidentes.isEmpty()) {
                administrarTiposAccidentes.borrarTiposAccidentes(borrarTiposAccidentes);
                //mostrarBorrados
                registrosBorrados = borrarTiposAccidentes.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarTiposAccidentes.clear();
            }
            if (!modificarTiposAccidentes.isEmpty()) {
                administrarTiposAccidentes.modificarTiposAccidentes(modificarTiposAccidentes);
                modificarTiposAccidentes.clear();
            }
            if (!crearTiposAccidentes.isEmpty()) {
                administrarTiposAccidentes.crearTiposAccidentes(crearTiposAccidentes);
                crearTiposAccidentes.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposAccidentes = null;
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
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
                editarTiposAccidentes = listTiposAccidentes.get(index);
            }
            if (tipoLista == 1) {
                editarTiposAccidentes = filtrarTiposAccidentes.get(index);
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

    public void agregarNuevoTiposAccidentes() {
        System.out.println("agregarNuevoTiposAccidentes");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposAccidentes.getCodigo() == null) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTiposAccidentes.getCodigo());

            for (int x = 0; x < listTiposAccidentes.size(); x++) {
                if (listTiposAccidentes.get(x).getCodigo().equals(nuevoTiposAccidentes.getCodigo())) {
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
        if (nuevoTiposAccidentes.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (nuevoTiposAccidentes.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        System.out.println("contador " + contador);

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAccidentes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAccidentes:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
                bandera = 0;
                filtrarTiposAccidentes = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposAccidentes.setSecuencia(l);

            crearTiposAccidentes.add(nuevoTiposAccidentes);

            listTiposAccidentes.add(nuevoTiposAccidentes);
            nuevoTiposAccidentes = new TiposAccidentes();
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            infoRegistro = "Cantidad de registros: " + listTiposAccidentes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposAccidentes').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposAccidentes() {
        System.out.println("limpiarNuevoTiposAccidentes");
        nuevoTiposAccidentes = new TiposAccidentes();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposAccidentes() {
        System.out.println("duplicandoTiposAccidentes");
        if (index >= 0) {
            duplicarTiposAccidentes = new TiposAccidentes();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposAccidentes.setSecuencia(l);
                duplicarTiposAccidentes.setCodigo(listTiposAccidentes.get(index).getCodigo());
                duplicarTiposAccidentes.setNombre(listTiposAccidentes.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarTiposAccidentes.setSecuencia(l);
                duplicarTiposAccidentes.setCodigo(filtrarTiposAccidentes.get(index).getCodigo());
                duplicarTiposAccidentes.setNombre(filtrarTiposAccidentes.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposAccidentes').show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposAccidentes.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposAccidentes.getNombre());

        if (duplicarTiposAccidentes.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposAccidentes.size(); x++) {
                if (listTiposAccidentes.get(x).getCodigo().equals(duplicarTiposAccidentes.getCodigo())) {
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
        if (duplicarTiposAccidentes.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else if (duplicarTiposAccidentes.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;

        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarTiposAccidentes.getSecuencia() + "  " + duplicarTiposAccidentes.getCodigo());
            if (crearTiposAccidentes.contains(duplicarTiposAccidentes)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposAccidentes.add(duplicarTiposAccidentes);
            crearTiposAccidentes.add(duplicarTiposAccidentes);
            RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listTiposAccidentes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposAccidentes:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposAccidentes:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposAccidentes");
                bandera = 0;
                filtrarTiposAccidentes = null;
                tipoLista = 0;
            }
            duplicarTiposAccidentes = new TiposAccidentes();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposAccidentes').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposAccidentes() {
        duplicarTiposAccidentes = new TiposAccidentes();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposAccidentesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSACCIDENTES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposAccidentesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSACCIDENTES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposAccidentes.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSACCIDENTES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSACCIDENTES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposAccidentes> getListTiposAccidentes() {
        if (listTiposAccidentes == null) {
            System.out.println("ControlTiposAccidentes getListTiposAccidentes");
            listTiposAccidentes = administrarTiposAccidentes.consultarTiposAccidentes();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposAccidentes == null || listTiposAccidentes.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposAccidentes.size();
        }
        return listTiposAccidentes;
    }

    public void setListTiposAccidentes(List<TiposAccidentes> listTiposAccidentes) {
        this.listTiposAccidentes = listTiposAccidentes;
    }

    public List<TiposAccidentes> getFiltrarTiposAccidentes() {
        return filtrarTiposAccidentes;
    }

    public void setFiltrarTiposAccidentes(List<TiposAccidentes> filtrarTiposAccidentes) {
        this.filtrarTiposAccidentes = filtrarTiposAccidentes;
    }

    public TiposAccidentes getNuevoTiposAccidentes() {
        return nuevoTiposAccidentes;
    }

    public void setNuevoTiposAccidentes(TiposAccidentes nuevoTiposAccidentes) {
        this.nuevoTiposAccidentes = nuevoTiposAccidentes;
    }

    public TiposAccidentes getDuplicarTiposAccidentes() {
        return duplicarTiposAccidentes;
    }

    public void setDuplicarTiposAccidentes(TiposAccidentes duplicarTiposAccidentes) {
        this.duplicarTiposAccidentes = duplicarTiposAccidentes;
    }

    public TiposAccidentes getEditarTiposAccidentes() {
        return editarTiposAccidentes;
    }

    public void setEditarTiposAccidentes(TiposAccidentes editarTiposAccidentes) {
        this.editarTiposAccidentes = editarTiposAccidentes;
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

    public TiposAccidentes getTiposAccidentesSeleccionado() {
        return tiposAccidentesSeleccionado;
    }

    public void setTiposAccidentesSeleccionado(TiposAccidentes clasesPensionesSeleccionado) {
        this.tiposAccidentesSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
