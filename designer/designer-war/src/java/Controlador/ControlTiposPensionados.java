/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposPensionados;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposPensionadosInterface;
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

@ManagedBean
@SessionScoped
public class ControlTiposPensionados implements Serializable {

    @EJB
    AdministrarTiposPensionadosInterface administrarTiposPensionados;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposPensionados> listTiposPensionados;
    private List<TiposPensionados> filtrarTiposPensionados;
    private List<TiposPensionados> crearTiposPensionados;
    private List<TiposPensionados> modificarTiposPensionados;
    private List<TiposPensionados> borrarTiposPensionados;
    private TiposPensionados nuevoTiposPensionados;
    private TiposPensionados duplicarTiposPensionados;
    private TiposPensionados editarTiposPensionados;
    private TiposPensionados tipoPensionadoSeleccionado;
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
    private String infoRegistro;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTiposPensionados() {
        listTiposPensionados = null;
        crearTiposPensionados = new ArrayList<TiposPensionados>();
        modificarTiposPensionados = new ArrayList<TiposPensionados>();
        borrarTiposPensionados = new ArrayList<TiposPensionados>();
        permitirIndex = true;
        editarTiposPensionados = new TiposPensionados();
        nuevoTiposPensionados = new TiposPensionados();
        duplicarTiposPensionados = new TiposPensionados();
        guardado = true;
        tamano = 270;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposPensionados.obtenerConexion(ses.getId());
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
            String pagActual = "tipopensionado";
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
        limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlTiposPensionados.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarTiposPensionados.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposPensionados eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = listTiposPensionados.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);
                } else if (cualCelda == 1) {
                    backUpDescripcion = listTiposPensionados.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);
                }
                secRegistro = listTiposPensionados.get(index).getSecuencia();
            } else {
                if (cualCelda == 0) {
                    backUpCodigo = filtrarTiposPensionados.get(index).getCodigo();
                    System.out.println(" backUpCodigo : " + backUpCodigo);

                } else if (cualCelda == 1) {
                    backUpDescripcion = filtrarTiposPensionados.get(index).getDescripcion();
                    System.out.println(" backUpDescripcion : " + backUpDescripcion);

                }
                secRegistro = filtrarTiposPensionados.get(index).getSecuencia();
            }
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposPensionados.asignarIndex \n");
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
            System.out.println("ERROR ControlTiposPensionados.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void listaValoresBoton() {
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPensionados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPensionados:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
            bandera = 0;
            filtrarTiposPensionados = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposPensionados.clear();
        crearTiposPensionados.clear();
        modificarTiposPensionados.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposPensionados = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposPensionados == null || listTiposPensionados.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposPensionados.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {  limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPensionados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPensionados:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
            bandera = 0;
            filtrarTiposPensionados = null;
            tipoLista = 0;
            tamano = 270;
        }

        borrarTiposPensionados.clear();
        crearTiposPensionados.clear();
        modificarTiposPensionados.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposPensionados = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposPensionados == null || listTiposPensionados.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposPensionados.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPensionados:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPensionados:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPensionados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPensionados:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
            bandera = 0;
            filtrarTiposPensionados = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposPensionados(int indice, String confirmarCambio, String valorConfirmar) {
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
                if (!crearTiposPensionados.contains(listTiposPensionados.get(indice))) {
                    if (listTiposPensionados.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposPensionados.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposPensionados.size(); j++) {
                            if (j != indice) {
                                if (listTiposPensionados.get(indice).getCodigo().equals(listTiposPensionados.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposPensionados.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposPensionados.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposPensionados.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposPensionados.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposPensionados.get(indice).setDescripcion(backUpDescripcion);
                    }

                    if (banderita == true) {
                        if (modificarTiposPensionados.isEmpty()) {
                            modificarTiposPensionados.add(listTiposPensionados.get(indice));
                        } else if (!modificarTiposPensionados.contains(listTiposPensionados.get(indice))) {
                            modificarTiposPensionados.add(listTiposPensionados.get(indice));
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
                    if (listTiposPensionados.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposPensionados.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposPensionados.size(); j++) {
                            if (j != indice) {
                                if (listTiposPensionados.get(indice).getCodigo().equals(listTiposPensionados.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposPensionados.get(indice).setCodigo(backUpCodigo);
                            banderita = false;
                        } else {
                            banderita = true;
                        }

                    }
                    if (listTiposPensionados.get(indice).getDescripcion().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposPensionados.get(indice).setDescripcion(backUpDescripcion);
                    }
                    if (listTiposPensionados.get(indice).getDescripcion() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposPensionados.get(indice).setDescripcion(backUpDescripcion);
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
            } else if (!crearTiposPensionados.contains(filtrarTiposPensionados.get(indice))) {
                if (filtrarTiposPensionados.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposPensionados.get(indice).setCodigo(backUpCodigo);

                } else {
                    for (int j = 0; j < filtrarTiposPensionados.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposPensionados.get(indice).getCodigo().equals(filtrarTiposPensionados.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }

                    if (contador > 0) {
                        filtrarTiposPensionados.get(indice).setCodigo(backUpCodigo);
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarTiposPensionados.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposPensionados.get(indice).setDescripcion(backUpDescripcion);
                }
                if (filtrarTiposPensionados.get(indice).getDescripcion() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposPensionados.get(indice).setDescripcion(backUpDescripcion);
                }

                if (banderita == true) {
                    if (modificarTiposPensionados.isEmpty()) {
                        modificarTiposPensionados.add(filtrarTiposPensionados.get(indice));
                    } else if (!modificarTiposPensionados.contains(filtrarTiposPensionados.get(indice))) {
                        modificarTiposPensionados.add(filtrarTiposPensionados.get(indice));
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
                if (filtrarTiposPensionados.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposPensionados.get(indice).setCodigo(backUpCodigo);

                } else {
                    for (int j = 0; j < filtrarTiposPensionados.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposPensionados.get(indice).getCodigo().equals(filtrarTiposPensionados.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }

                    if (contador > 0) {
                        filtrarTiposPensionados.get(indice).setCodigo(backUpCodigo);
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                    } else {
                        banderita = true;
                    }

                }

                if (filtrarTiposPensionados.get(indice).getDescripcion().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposPensionados.get(indice).setDescripcion(backUpDescripcion);
                }
                if (filtrarTiposPensionados.get(indice).getDescripcion() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                    filtrarTiposPensionados.get(indice).setDescripcion(backUpDescripcion);
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
            RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoTiposPensionados() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposPensionados");
                if (!modificarTiposPensionados.isEmpty() && modificarTiposPensionados.contains(listTiposPensionados.get(index))) {
                    int modIndex = modificarTiposPensionados.indexOf(listTiposPensionados.get(index));
                    modificarTiposPensionados.remove(modIndex);
                    borrarTiposPensionados.add(listTiposPensionados.get(index));
                } else if (!crearTiposPensionados.isEmpty() && crearTiposPensionados.contains(listTiposPensionados.get(index))) {
                    int crearIndex = crearTiposPensionados.indexOf(listTiposPensionados.get(index));
                    crearTiposPensionados.remove(crearIndex);
                } else {
                    borrarTiposPensionados.add(listTiposPensionados.get(index));
                }
                listTiposPensionados.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposPensionados ");
                if (!modificarTiposPensionados.isEmpty() && modificarTiposPensionados.contains(filtrarTiposPensionados.get(index))) {
                    int modIndex = modificarTiposPensionados.indexOf(filtrarTiposPensionados.get(index));
                    modificarTiposPensionados.remove(modIndex);
                    borrarTiposPensionados.add(filtrarTiposPensionados.get(index));
                } else if (!crearTiposPensionados.isEmpty() && crearTiposPensionados.contains(filtrarTiposPensionados.get(index))) {
                    int crearIndex = crearTiposPensionados.indexOf(filtrarTiposPensionados.get(index));
                    crearTiposPensionados.remove(crearIndex);
                } else {
                    borrarTiposPensionados.add(filtrarTiposPensionados.get(index));
                }
                int VCIndex = listTiposPensionados.indexOf(filtrarTiposPensionados.get(index));
                listTiposPensionados.remove(VCIndex);
                filtrarTiposPensionados.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listTiposPensionados == null || listTiposPensionados.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listTiposPensionados.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
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
        BigInteger contarRetiradosTipoPensionado;

        try {
            System.err.println("Control Secuencia de ControlTiposPensionados ");
            if (tipoLista == 0) {
                contarRetiradosTipoPensionado = administrarTiposPensionados.contarRetiradosTipoPensionado(listTiposPensionados.get(index).getSecuencia());
            } else {
                contarRetiradosTipoPensionado = administrarTiposPensionados.contarRetiradosTipoPensionado(filtrarTiposPensionados.get(index).getSecuencia());
            }
            if (contarRetiradosTipoPensionado.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposPensionados();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;
                contarRetiradosTipoPensionado = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposPensionados verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposPensionados.isEmpty() || !crearTiposPensionados.isEmpty() || !modificarTiposPensionados.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTiposPensionados() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarTiposPensionados");
            if (!borrarTiposPensionados.isEmpty()) {
                administrarTiposPensionados.borrarTiposPensionados(borrarTiposPensionados);
                //mostrarBorrados
                registrosBorrados = borrarTiposPensionados.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarTiposPensionados.clear();
            }
            if (!modificarTiposPensionados.isEmpty()) {
                administrarTiposPensionados.modificarTiposPensionados(modificarTiposPensionados);
                modificarTiposPensionados.clear();
            }
            if (!crearTiposPensionados.isEmpty()) {
                administrarTiposPensionados.crearTiposPensionados(crearTiposPensionados);
                crearTiposPensionados.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposPensionados = null;
            RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
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
                editarTiposPensionados = listTiposPensionados.get(index);
            }
            if (tipoLista == 1) {
                editarTiposPensionados = filtrarTiposPensionados.get(index);
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

    public void agregarNuevoTiposPensionados() {
        System.out.println("agregarNuevoTiposPensionados");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTiposPensionados.getCodigo() == a) {
            mensajeValidacion = " *Codigo";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTiposPensionados.getCodigo());

            for (int x = 0; x < listTiposPensionados.size(); x++) {
                if (listTiposPensionados.get(x).getCodigo().equals(nuevoTiposPensionados.getCodigo())) {
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
        if (nuevoTiposPensionados.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Descripción \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPensionados:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPensionados:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
                bandera = 0;
                filtrarTiposPensionados = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposPensionados.setSecuencia(l);

            crearTiposPensionados.add(nuevoTiposPensionados);

            listTiposPensionados.add(nuevoTiposPensionados);
            nuevoTiposPensionados = new TiposPensionados();
            RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
            infoRegistro = "Cantidad de registros: " + listTiposPensionados.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposPensionados').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposPensionados() {
        System.out.println("limpiarNuevoTiposPensionados");
        nuevoTiposPensionados = new TiposPensionados();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposPensionados() {
        System.out.println("duplicandoTiposPensionados");
        if (index >= 0) {
            duplicarTiposPensionados = new TiposPensionados();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTiposPensionados.setSecuencia(l);
                duplicarTiposPensionados.setCodigo(listTiposPensionados.get(index).getCodigo());
                duplicarTiposPensionados.setDescripcion(listTiposPensionados.get(index).getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarTiposPensionados.setSecuencia(l);
                duplicarTiposPensionados.setCodigo(filtrarTiposPensionados.get(index).getCodigo());
                duplicarTiposPensionados.setDescripcion(filtrarTiposPensionados.get(index).getDescripcion());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposPensionados').show()");
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
        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposPensionados.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposPensionados.getDescripcion());

        if (duplicarTiposPensionados.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposPensionados.size(); x++) {
                if (listTiposPensionados.get(x).getCodigo().equals(duplicarTiposPensionados.getCodigo())) {
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
        if (duplicarTiposPensionados.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + "   *Descripción \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarTiposPensionados.getSecuencia() + "  " + duplicarTiposPensionados.getCodigo());
            if (crearTiposPensionados.contains(duplicarTiposPensionados)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposPensionados.add(duplicarTiposPensionados);
            crearTiposPensionados.add(duplicarTiposPensionados);
            RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            infoRegistro = "Cantidad de registros: " + listTiposPensionados.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (bandera == 1) {
                //CERRAR FILTRADO
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposPensionados:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposPensionados:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposPensionados");
                bandera = 0;
                filtrarTiposPensionados = null;
                tipoLista = 0;
            }
            duplicarTiposPensionados = new TiposPensionados();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposPensionados').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposPensionados() {
        duplicarTiposPensionados = new TiposPensionados();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposPensionadosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSPENSIONADOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposPensionadosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSPENSIONADOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposPensionados.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSPENSIONADOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSPENSIONADOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposPensionados> getListTiposPensionados() {
        if (listTiposPensionados == null) {
            listTiposPensionados = administrarTiposPensionados.consultarTiposPensionados();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposPensionados == null || listTiposPensionados.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposPensionados.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listTiposPensionados;
    }

    public void setListTiposPensionados(List<TiposPensionados> listTiposPensionados) {
        this.listTiposPensionados = listTiposPensionados;
    }

    public List<TiposPensionados> getFiltrarTiposPensionados() {
        return filtrarTiposPensionados;
    }

    public void setFiltrarTiposPensionados(List<TiposPensionados> filtrarTiposPensionados) {
        this.filtrarTiposPensionados = filtrarTiposPensionados;
    }

    public TiposPensionados getNuevoTiposPensionados() {
        return nuevoTiposPensionados;
    }

    public void setNuevoTiposPensionados(TiposPensionados nuevoTiposPensionados) {
        this.nuevoTiposPensionados = nuevoTiposPensionados;
    }

    public TiposPensionados getDuplicarTiposPensionados() {
        return duplicarTiposPensionados;
    }

    public void setDuplicarTiposPensionados(TiposPensionados duplicarTiposPensionados) {
        this.duplicarTiposPensionados = duplicarTiposPensionados;
    }

    public TiposPensionados getEditarTiposPensionados() {
        return editarTiposPensionados;
    }

    public void setEditarTiposPensionados(TiposPensionados editarTiposPensionados) {
        this.editarTiposPensionados = editarTiposPensionados;
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

    public TiposPensionados getTipoPensionadoSeleccionado() {
        return tipoPensionadoSeleccionado;
    }

    public void setTipoPensionadoSeleccionado(TiposPensionados tipoPensionadoSeleccionado) {
        this.tipoPensionadoSeleccionado = tipoPensionadoSeleccionado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
