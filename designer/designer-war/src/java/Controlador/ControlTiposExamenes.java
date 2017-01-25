/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposExamenes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposExamenesInterface;
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
 * @author John Pineda
 */
@ManagedBean
@SessionScoped
public class ControlTiposExamenes implements Serializable {

    @EJB
    AdministrarTiposExamenesInterface administrarTiposExamenes;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<TiposExamenes> listTiposExamenes;
    private List<TiposExamenes> filtrarTiposExamenes;
    private List<TiposExamenes> crearTiposExamenes;
    private List<TiposExamenes> modificarTiposExamenes;
    private List<TiposExamenes> borrarTiposExamenes;
    private TiposExamenes nuevoTipoExamen;
    private TiposExamenes duplicarTipoExamen;
    private TiposExamenes editarTipoExamen;
    private TiposExamenes tiposExamenesSeleccionado;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, descripcion, minimoNormal, maximoNormal, diasRecurrencia;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger tiposExamenesCargos;
    private BigInteger vigenciasExamenesMedicos;

    private int tamano;
    private Integer backUpCodigo;
    private String infoRegistro;
    private String backUpDescripcion;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTiposExamenes() {
        listTiposExamenes = null;
        crearTiposExamenes = new ArrayList<TiposExamenes>();
        modificarTiposExamenes = new ArrayList<TiposExamenes>();
        borrarTiposExamenes = new ArrayList<TiposExamenes>();
        permitirIndex = true;
        guardado = true;
        editarTipoExamen = new TiposExamenes();
        nuevoTipoExamen = new TiposExamenes();
        duplicarTipoExamen = new TiposExamenes();
        tamano = 270;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposExamenes.obtenerConexion(ses.getId());
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
            String pagActual = "tipoexamen";
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
            System.out.println("\n ENTRE A ControlTiposExamenes.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarTiposExamenes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR ControlTiposExamenes eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listTiposExamenes.get(index).getSecuencia();
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listTiposExamenes.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarTiposExamenes.get(index).getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listTiposExamenes.get(index).getNombre();
                } else {
                    backUpDescripcion = filtrarTiposExamenes.get(index).getNombre();
                }
            }
        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A ControlTiposExamenes.asignarIndex \n");
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
            System.out.println("ERROR ControlTiposExamenes.asignarIndex ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            minimoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:minimoNormal");
            minimoNormal.setFilterStyle("display: none; visibility: hidden;");
            maximoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:maximoNormal");
            maximoNormal.setFilterStyle("display: none; visibility: hidden;");
            diasRecurrencia = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:diasRecurrencia");
            diasRecurrencia.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            bandera = 0;
            filtrarTiposExamenes = null;
            tipoLista = 0;
        }

        borrarTiposExamenes.clear();
        crearTiposExamenes.clear();
        modificarTiposExamenes.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposExamenes = null;
        guardado = true;
        permitirIndex = true;
        getListTiposExamenes();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposExamenes == null || listTiposExamenes.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposExamenes.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosTipoExamen");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            minimoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:minimoNormal");
            minimoNormal.setFilterStyle("display: none; visibility: hidden;");
            maximoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:maximoNormal");
            maximoNormal.setFilterStyle("display: none; visibility: hidden;");
            diasRecurrencia = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:diasRecurrencia");
            diasRecurrencia.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            bandera = 0;
            filtrarTiposExamenes = null;
            tipoLista = 0;
        }

        borrarTiposExamenes.clear();
        crearTiposExamenes.clear();
        modificarTiposExamenes.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposExamenes = null;
        guardado = true;
        permitirIndex = true;
        getListTiposExamenes();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposExamenes == null || listTiposExamenes.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposExamenes.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosTipoExamen");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            minimoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:minimoNormal");
            minimoNormal.setFilterStyle("width: 85% !important;");
            maximoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:maximoNormal");
            maximoNormal.setFilterStyle("width: 85% !important;");
            diasRecurrencia = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:diasRecurrencia");
            diasRecurrencia.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            minimoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:minimoNormal");
            minimoNormal.setFilterStyle("display: none; visibility: hidden;");
            maximoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:maximoNormal");
            maximoNormal.setFilterStyle("display: none; visibility: hidden;");
            diasRecurrencia = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:diasRecurrencia");
            diasRecurrencia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            bandera = 0;
            filtrarTiposExamenes = null;
            tipoLista = 0;
        }
    }

    public void modificarTipoExamen(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR TIPO EXAMEN");
        index = indice;

        int contador = 0, pass = 0;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR EXAMEN, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearTiposExamenes.contains(listTiposExamenes.get(indice))) {
                    if (listTiposExamenes.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listTiposExamenes.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposExamenes.size(); j++) {
                            if (j != indice) {
                                if (listTiposExamenes.get(indice).getCodigo() == listTiposExamenes.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposExamenes.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listTiposExamenes.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listTiposExamenes.get(indice).setNombre(backUpDescripcion);
                    } else if (listTiposExamenes.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listTiposExamenes.get(indice).setNombre(backUpDescripcion);
                    } else {
                        pass++;
                    }

                    if (pass == 2) {
                        if (modificarTiposExamenes.isEmpty()) {
                            modificarTiposExamenes.add(listTiposExamenes.get(indice));
                        } else if (!modificarTiposExamenes.contains(listTiposExamenes.get(indice))) {
                            modificarTiposExamenes.add(listTiposExamenes.get(indice));
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
                    if (listTiposExamenes.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listTiposExamenes.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposExamenes.size(); j++) {
                            if (j != indice) {
                                if (listTiposExamenes.get(indice).getCodigo() == listTiposExamenes.get(j).getCodigo()) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposExamenes.get(indice).setCodigo(backUpCodigo);
                        } else {
                            pass++;
                        }

                    }
                    if (listTiposExamenes.get(indice).getNombre() == null) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listTiposExamenes.get(indice).setNombre(backUpDescripcion);
                    } else if (listTiposExamenes.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listTiposExamenes.get(indice).setNombre(backUpDescripcion);
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
            } else if (!crearTiposExamenes.contains(filtrarTiposExamenes.get(indice))) {
                if (filtrarTiposExamenes.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposExamenes.get(indice).setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < listTiposExamenes.size(); j++) {
                        if (j == indice) {
                            if (listTiposExamenes.get(indice).getCodigo() == listTiposExamenes.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    for (int j = 0; j < filtrarTiposExamenes.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposExamenes.get(indice).getCodigo().equals(filtrarTiposExamenes.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarTiposExamenes.get(indice).setCodigo(backUpCodigo);
                    } else {
                        pass++;
                    }

                }

                if (filtrarTiposExamenes.get(indice).getNombre() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposExamenes.get(indice).setNombre(backUpDescripcion);
                } else if (filtrarTiposExamenes.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposExamenes.get(indice).setNombre(backUpDescripcion);
                } else {
                    pass++;
                }

                if (pass == 2) {
                    if (modificarTiposExamenes.isEmpty()) {
                        modificarTiposExamenes.add(filtrarTiposExamenes.get(indice));
                    } else if (!modificarTiposExamenes.contains(filtrarTiposExamenes.get(indice))) {
                        modificarTiposExamenes.add(filtrarTiposExamenes.get(indice));
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
                if (filtrarTiposExamenes.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposExamenes.get(indice).setCodigo(backUpCodigo);
                } else {
                    for (int j = 0; j < listTiposExamenes.size(); j++) {
                        if (j == indice) {
                            if (listTiposExamenes.get(indice).getCodigo() == listTiposExamenes.get(j).getCodigo()) {
                                contador++;
                            }
                        }
                    }
                    for (int j = 0; j < filtrarTiposExamenes.size(); j++) {
                        if (j != indice) {
                            if (filtrarTiposExamenes.get(indice).getCodigo().equals(filtrarTiposExamenes.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        filtrarTiposExamenes.get(indice).setCodigo(backUpCodigo);
                    } else {
                        pass++;
                    }

                }

                if (filtrarTiposExamenes.get(indice).getNombre() == null) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposExamenes.get(indice).setNombre(backUpDescripcion);
                } else if (filtrarTiposExamenes.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    filtrarTiposExamenes.get(indice).setNombre(backUpDescripcion);
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
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoTiposExamenes() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoTiposExamenes");
                if (!modificarTiposExamenes.isEmpty() && modificarTiposExamenes.contains(listTiposExamenes.get(index))) {
                    int modIndex = modificarTiposExamenes.indexOf(listTiposExamenes.get(index));
                    modificarTiposExamenes.remove(modIndex);
                    borrarTiposExamenes.add(listTiposExamenes.get(index));
                } else if (!crearTiposExamenes.isEmpty() && crearTiposExamenes.contains(listTiposExamenes.get(index))) {
                    int crearIndex = crearTiposExamenes.indexOf(listTiposExamenes.get(index));
                    crearTiposExamenes.remove(crearIndex);
                } else {
                    borrarTiposExamenes.add(listTiposExamenes.get(index));
                }
                listTiposExamenes.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrandoTiposExamenes");
                if (!modificarTiposExamenes.isEmpty() && modificarTiposExamenes.contains(filtrarTiposExamenes.get(index))) {
                    int modIndex = modificarTiposExamenes.indexOf(filtrarTiposExamenes.get(index));
                    modificarTiposExamenes.remove(modIndex);
                    borrarTiposExamenes.add(filtrarTiposExamenes.get(index));
                } else if (!crearTiposExamenes.isEmpty() && crearTiposExamenes.contains(filtrarTiposExamenes.get(index))) {
                    int crearIndex = crearTiposExamenes.indexOf(filtrarTiposExamenes.get(index));
                    crearTiposExamenes.remove(crearIndex);
                } else {
                    borrarTiposExamenes.add(filtrarTiposExamenes.get(index));
                }
                int VCIndex = listTiposExamenes.indexOf(filtrarTiposExamenes.get(index));
                listTiposExamenes.remove(VCIndex);
                filtrarTiposExamenes.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listTiposExamenes == null || listTiposExamenes.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listTiposExamenes.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
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
        try {
            System.err.println("Control Secuencia de ControlTiposExamenes ");
            if (tipoLista == 0) {
                tiposExamenesCargos = administrarTiposExamenes.contarTiposExamenesCargosTipoExamen(listTiposExamenes.get(index).getSecuencia());
                vigenciasExamenesMedicos = administrarTiposExamenes.contarVigenciasExamenesMedicosTipoExamen(listTiposExamenes.get(index).getSecuencia());
            } else {
                tiposExamenesCargos = administrarTiposExamenes.contarTiposExamenesCargosTipoExamen(filtrarTiposExamenes.get(index).getSecuencia());
                vigenciasExamenesMedicos = administrarTiposExamenes.contarVigenciasExamenesMedicosTipoExamen(filtrarTiposExamenes.get(index).getSecuencia());
            }
            if (tiposExamenesCargos.equals(new BigInteger("0")) && vigenciasExamenesMedicos.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposExamenes();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;

                tiposExamenesCargos = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposExamenes verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposExamenes.isEmpty() || !crearTiposExamenes.isEmpty() || !modificarTiposExamenes.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTiposExamenes() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando ControlTiposExamenes");
            if (!borrarTiposExamenes.isEmpty()) {
                administrarTiposExamenes.borrarTiposExamenes(borrarTiposExamenes);

                //mostrarBorrados
                registrosBorrados = borrarTiposExamenes.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarTiposExamenes.clear();
            }
            if (!crearTiposExamenes.isEmpty()) {
                administrarTiposExamenes.crearTiposExamenes(crearTiposExamenes);
                crearTiposExamenes.clear();
            }
            if (!modificarTiposExamenes.isEmpty()) {
                administrarTiposExamenes.modificarTiposExamenes(modificarTiposExamenes);
                modificarTiposExamenes.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposExamenes = null;
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            k = 0;
            guardado = true;
        }
        index = -1;
        FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update("form:growl");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTipoExamen = listTiposExamenes.get(index);
            }
            if (tipoLista == 1) {
                editarTipoExamen = filtrarTiposExamenes.get(index);
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
                RequestContext.getCurrentInstance().update("formularioDialogos:editMinimoNormal");
                RequestContext.getCurrentInstance().execute("PF('editMinimoNormal').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editMaximoNormal");
                RequestContext.getCurrentInstance().execute("PF('editMaximoNormal').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDiasRecurrencia");
                RequestContext.getCurrentInstance().execute("PF('editDiasRecurrencia').show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoTiposExamenes() {
        System.out.println("agregarNuevoTiposExamenes");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTipoExamen.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTipoExamen.getCodigo());

            for (int x = 0; x < listTiposExamenes.size(); x++) {
                if (listTiposExamenes.get(x).getCodigo() == nuevoTipoExamen.getCodigo()) {
                    duplicados++;
                }
            }
            System.out.println("Antes del if Duplicados eses igual  : " + duplicados);

            if (duplicados > 0) {
                mensajeValidacion = " *Que NO hayan codigos repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoTipoExamen.getNombre() == (null)) {
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                minimoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:minimoNormal");
                minimoNormal.setFilterStyle("display: none; visibility: hidden;");
                maximoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:maximoNormal");
                maximoNormal.setFilterStyle("display: none; visibility: hidden;");
                diasRecurrencia = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:diasRecurrencia");
                diasRecurrencia.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoExamen");
                bandera = 0;
                filtrarTiposExamenes = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoExamen.setSecuencia(l);

            crearTiposExamenes.add(nuevoTipoExamen);

            listTiposExamenes.add(nuevoTipoExamen);
            nuevoTipoExamen = new TiposExamenes();

            infoRegistro = "Cantidad de registros: " + listTiposExamenes.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposExamenes').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposExamenes() {
        System.out.println("limpiarNuevoTiposExamenes");
        nuevoTipoExamen = new TiposExamenes();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposExamenes() {
        System.out.println("duplicandoTiposExamenes");
        if (index >= 0) {
            duplicarTipoExamen = new TiposExamenes();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTipoExamen.setSecuencia(l);
                duplicarTipoExamen.setCodigo(listTiposExamenes.get(index).getCodigo());
                duplicarTipoExamen.setNombre(listTiposExamenes.get(index).getNombre());
            }
            if (tipoLista == 1) {
                duplicarTipoExamen.setSecuencia(l);
                duplicarTipoExamen.setCodigo(filtrarTiposExamenes.get(index).getCodigo());
                duplicarTipoExamen.setNombre(filtrarTiposExamenes.get(index).getNombre());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposExamenes').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR TIPOS EXAMENES");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarTipoExamen.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTipoExamen.getNombre());

        if (duplicarTipoExamen.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposExamenes.size(); x++) {
                if (listTiposExamenes.get(x).getCodigo() == duplicarTipoExamen.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *que NO existan codigo repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarTipoExamen.getNombre().isEmpty()) {
            mensajeValidacion = mensajeValidacion + "   *Descripcion \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            System.out.println("Datos Duplicando: " + duplicarTipoExamen.getSecuencia() + "  " + duplicarTipoExamen.getCodigo());
            if (crearTiposExamenes.contains(duplicarTipoExamen)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposExamenes.add(duplicarTipoExamen);
            crearTiposExamenes.add(duplicarTipoExamen);
            RequestContext.getCurrentInstance().update("form:datosTipoExamen");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
            }
            infoRegistro = "Cantidad de registros: " + listTiposExamenes.size();

            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                minimoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:minimoNormal");
                minimoNormal.setFilterStyle("display: none; visibility: hidden;");
                maximoNormal = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:maximoNormal");
                maximoNormal.setFilterStyle("display: none; visibility: hidden;");
                diasRecurrencia = (Column) c.getViewRoot().findComponent("form:datosTipoExamen:diasRecurrencia");
                diasRecurrencia.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoExamen");
                bandera = 0;
                filtrarTiposExamenes = null;
                tipoLista = 0;
            }
            duplicarTipoExamen = new TiposExamenes();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposExamenes').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposExamenes() {
        duplicarTipoExamen = new TiposExamenes();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoExamenExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSEXAMENES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoExamenExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSEXAMENES", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposExamenes.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSEXAMENES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSEXAMENES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<TiposExamenes> getListTiposExamenes() {
        if (listTiposExamenes == null) {
            listTiposExamenes = administrarTiposExamenes.consultarTiposExamenes();
        }
        RequestContext context = RequestContext.getCurrentInstance();

        if (listTiposExamenes == null || listTiposExamenes.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposExamenes.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listTiposExamenes;
    }

    public void setListTiposExamenes(List<TiposExamenes> listTiposExamenes) {
        this.listTiposExamenes = listTiposExamenes;
    }

    public TiposExamenes getNuevoTipoExamen() {
        return nuevoTipoExamen;
    }

    public void setNuevoTipoExamen(TiposExamenes nuevoTipoExamen) {
        this.nuevoTipoExamen = nuevoTipoExamen;
    }

    public TiposExamenes getDuplicarTipoExamen() {
        return duplicarTipoExamen;
    }

    public void setDuplicarTipoExamen(TiposExamenes duplicarTipoExamen) {
        this.duplicarTipoExamen = duplicarTipoExamen;
    }

    public TiposExamenes getEditarTipoExamen() {
        return editarTipoExamen;
    }

    public void setEditarTipoExamen(TiposExamenes editarTipoExamen) {
        this.editarTipoExamen = editarTipoExamen;
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

    public List<TiposExamenes> getFiltrarTiposExamenes() {
        return filtrarTiposExamenes;
    }

    public void setFiltrarTiposExamenes(List<TiposExamenes> filtrarTiposExamenes) {
        this.filtrarTiposExamenes = filtrarTiposExamenes;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public TiposExamenes getTiposExamenesSeleccionado() {
        return tiposExamenesSeleccionado;
    }

    public void setTiposExamenesSeleccionado(TiposExamenes tiposExamenesSeleccionado) {
        this.tiposExamenesSeleccionado = tiposExamenesSeleccionado;
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
