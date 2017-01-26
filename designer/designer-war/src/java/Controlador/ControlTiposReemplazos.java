/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposReemplazos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposReemplazosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class ControlTiposReemplazos implements Serializable {

    @EJB
    AdministrarTiposReemplazosInterface administrarTiposReemplazos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<TiposReemplazos> listTiposReemplazos;
    private List<TiposReemplazos> filtrarTiposReemplazos;
    private List<TiposReemplazos> crearTiposReemplazos;
    private List<TiposReemplazos> modificarTiposReemplazos;
    private List<TiposReemplazos> borrarTiposReemplazos;
    private TiposReemplazos nuevoTipoReemplazo;
    private TiposReemplazos duplicarTipoReemplazo;
    private TiposReemplazos editarTipoReemplazo;
    private TiposReemplazos tiposReemplazosSeleccionado;
    //otros
    private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private BigInteger secRegistro;
    private Column codigo, descripcion, factorReemplazado;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    BigDecimal verificarBorrandoEncargaturas;
    BigDecimal verificarBorradoProgramacionesTiempos;
    BigDecimal verificarBorradoReemplazos;
    //Redireccionamiento de pantallas
    private int tamano;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTiposReemplazos() {
        listTiposReemplazos = null;
        crearTiposReemplazos = new ArrayList<TiposReemplazos>();
        modificarTiposReemplazos = new ArrayList<TiposReemplazos>();
        borrarTiposReemplazos = new ArrayList<TiposReemplazos>();
        permitirIndex = true;
        editarTipoReemplazo = new TiposReemplazos();
        nuevoTipoReemplazo = new TiposReemplazos();
        duplicarTipoReemplazo = new TiposReemplazos();
        guardado = true;
        tamano = 270;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposReemplazos.obtenerConexion(ses.getId());
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
            String pagActual = "tiporeemplazo";
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

    public String redirigir() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A CONTROLTIPOSREEMPLAZOS.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            infoRegistro = "Cantidad de registros: " + filtrarTiposReemplazos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
        } catch (Exception e) {
            System.out.println("ERROR CONTROLTIPOSREEMPLAZOS eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    private String infoRegistro;
    private Integer backUpCodigo;
    private String backUpDescripcion;

    public void cambiarIndice(int indice, int celda) {
        System.err.println("TIPO LISTA = " + tipoLista);

        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            secRegistro = listTiposReemplazos.get(index).getSecuencia();
            if (cualCelda == 0) {
                if (tipoLista == 0) {
                    backUpCodigo = listTiposReemplazos.get(index).getCodigo();
                } else {
                    backUpCodigo = filtrarTiposReemplazos.get(index).getCodigo();
                }
            }
            if (cualCelda == 1) {
                if (tipoLista == 0) {
                    backUpDescripcion = listTiposReemplazos.get(index).getNombre();
                } else {
                    backUpDescripcion = filtrarTiposReemplazos.get(index).getNombre();
                }
            }

        }
        System.out.println("Indice: " + index + " Celda: " + cualCelda);
    }

    public void asignarIndex(Integer indice, int LND, int dig) {
        try {
            System.out.println("\n ENTRE A CONTROLTIPOSREEMPLAZOS ASIGNAR INDEX \n");
            index = indice;
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
                System.out.println("TIPO ACTUALIZACION : " + tipoActualizacion);
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

        } catch (Exception e) {
            System.out.println("ERROR CONTROLTIPOSREEMPLAZOS ASIGNAR INDEX ERROR======" + e.getMessage());
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            factorReemplazado = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:factorReemplazado");
            factorReemplazado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarTiposReemplazos = null;
            tipoLista = 0;
        }

        borrarTiposReemplazos.clear();
        crearTiposReemplazos.clear();
        modificarTiposReemplazos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposReemplazos = null;
        guardado = true;
        permitirIndex = true;
        getListTiposReemplazos();
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposReemplazos == null || listTiposReemplazos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposReemplazos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            factorReemplazado = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:factorReemplazado");
            factorReemplazado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarTiposReemplazos = null;
            tipoLista = 0;
        }

        borrarTiposReemplazos.clear();
        crearTiposReemplazos.clear();
        modificarTiposReemplazos.clear();
        index = -1;
        secRegistro = null;
        k = 0;
        listTiposReemplazos = null;
        guardado = true;
        permitirIndex = true;
        getListTiposReemplazos();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosClasesPensiones");
        if (listTiposReemplazos == null || listTiposReemplazos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposReemplazos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            factorReemplazado = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:factorReemplazado");
            factorReemplazado.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;

            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            factorReemplazado = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:factorReemplazado");
            factorReemplazado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            bandera = 0;
            filtrarTiposReemplazos = null;
            tipoLista = 0;
        }
    }

    public void modificandoTipoReemplazo(int indice, String confirmarCambio, String valorConfirmar) {
        System.err.println("ENTRE A MODIFICAR TIPO REEMPLAZO");
        index = indice;

        int contador = 0;
        int contadorGuardar = 0;
        boolean banderita = false;
        Integer a;
        a = null;
        RequestContext context = RequestContext.getCurrentInstance();
        System.err.println("TIPO LISTA = " + tipoLista);
        if (confirmarCambio.equalsIgnoreCase("N")) {
            System.err.println("ENTRE A MODIFICAR TIPO REEMPLAZO, CONFIRMAR CAMBIO ES N");
            if (tipoLista == 0) {
                if (!crearTiposReemplazos.contains(listTiposReemplazos.get(indice))) {
                    if (listTiposReemplazos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listTiposReemplazos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposReemplazos.size(); j++) {
                            if (j != indice) {
                                if (listTiposReemplazos.get(indice).getCodigo().equals(listTiposReemplazos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposReemplazos.get(indice).setCodigo(backUpCodigo);
                        } else {
                            contadorGuardar++;
                        }

                    }
                    if (listTiposReemplazos.get(indice).getNombre() == null || listTiposReemplazos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposReemplazos.get(indice).setNombre(backUpDescripcion);
                    } else {
                        contadorGuardar++;
                    }

                    if (contadorGuardar == 2) {
                        if (modificarTiposReemplazos.isEmpty()) {
                            modificarTiposReemplazos.add(listTiposReemplazos.get(indice));
                        } else if (!modificarTiposReemplazos.contains(listTiposReemplazos.get(indice))) {
                            modificarTiposReemplazos.add(listTiposReemplazos.get(indice));
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
                    if (listTiposReemplazos.get(indice).getCodigo() == a) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        listTiposReemplazos.get(indice).setCodigo(backUpCodigo);
                    } else {
                        for (int j = 0; j < listTiposReemplazos.size(); j++) {
                            if (j != indice) {
                                if (listTiposReemplazos.get(indice).getCodigo().equals(listTiposReemplazos.get(j).getCodigo())) {
                                    contador++;
                                }
                            }
                        }
                        if (contador > 0) {
                            mensajeValidacion = "CODIGOS REPETIDOS";
                            listTiposReemplazos.get(indice).setCodigo(backUpCodigo);
                        } else {
                            contadorGuardar++;
                        }

                    }
                    if (listTiposReemplazos.get(indice).getNombre() == null || listTiposReemplazos.get(indice).getNombre().isEmpty()) {
                        mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                        banderita = false;
                        listTiposReemplazos.get(indice).setNombre(backUpDescripcion);
                    } else {
                        contadorGuardar++;
                    }

                    if (contadorGuardar == 2) {

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
            } else if (!crearTiposReemplazos.contains(filtrarTiposReemplazos.get(indice))) {
                if (filtrarTiposReemplazos.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                } else {
                    for (int j = 0; j < filtrarTiposReemplazos.size(); j++) {
                        System.err.println("indice filtrar indice : " + filtrarTiposReemplazos.get(j).getCodigo());
                        if (j != indice) {
                            if (filtrarTiposReemplazos.get(indice).getCodigo().equals(filtrarTiposReemplazos.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                    } else {
                        contadorGuardar++;
                    }

                }

                if (filtrarTiposReemplazos.get(indice).getNombre() == null || filtrarTiposReemplazos.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                } else {
                    contadorGuardar++;
                }

                if (contadorGuardar == 2) {
                    if (modificarTiposReemplazos.isEmpty()) {
                        modificarTiposReemplazos.add(filtrarTiposReemplazos.get(indice));
                    } else if (!modificarTiposReemplazos.contains(filtrarTiposReemplazos.get(indice))) {
                        modificarTiposReemplazos.add(filtrarTiposReemplazos.get(indice));
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
                if (filtrarTiposReemplazos.get(indice).getCodigo() == a) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                } else {
                    for (int j = 0; j < filtrarTiposReemplazos.size(); j++) {
                        System.err.println("indice filtrar indice : " + filtrarTiposReemplazos.get(j).getCodigo());
                        if (j != indice) {
                            if (filtrarTiposReemplazos.get(indice).getCodigo().equals(filtrarTiposReemplazos.get(j).getCodigo())) {
                                contador++;
                            }
                        }
                    }
                    if (contador > 0) {
                        mensajeValidacion = "CODIGOS REPETIDOS";
                        banderita = false;
                    } else {
                        contadorGuardar++;
                    }

                }

                if (filtrarTiposReemplazos.get(indice).getNombre() == null || filtrarTiposReemplazos.get(indice).getNombre().isEmpty()) {
                    mensajeValidacion = "NO PUEDEN HABER CAMPOS VACIOS";
                    banderita = false;
                } else {
                    contadorGuardar++;
                }

                if (contadorGuardar == 2) {

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
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void borrandoTiposReemplazos() {

        if (index >= 0) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrarandoTiposReemplazos");
                if (!modificarTiposReemplazos.isEmpty() && modificarTiposReemplazos.contains(listTiposReemplazos.get(index))) {
                    int modIndex = modificarTiposReemplazos.indexOf(listTiposReemplazos.get(index));
                    modificarTiposReemplazos.remove(modIndex);
                    borrarTiposReemplazos.add(listTiposReemplazos.get(index));
                } else if (!crearTiposReemplazos.isEmpty() && crearTiposReemplazos.contains(listTiposReemplazos.get(index))) {
                    int crearIndex = crearTiposReemplazos.indexOf(listTiposReemplazos.get(index));
                    crearTiposReemplazos.remove(crearIndex);
                } else {
                    borrarTiposReemplazos.add(listTiposReemplazos.get(index));
                }
                listTiposReemplazos.remove(index);
            }
            if (tipoLista == 1) {
                System.out.println("borrarandoTiposReemplazos");
                if (!modificarTiposReemplazos.isEmpty() && modificarTiposReemplazos.contains(filtrarTiposReemplazos.get(index))) {
                    int modIndex = modificarTiposReemplazos.indexOf(filtrarTiposReemplazos.get(index));
                    modificarTiposReemplazos.remove(modIndex);
                    borrarTiposReemplazos.add(filtrarTiposReemplazos.get(index));
                } else if (!crearTiposReemplazos.isEmpty() && crearTiposReemplazos.contains(filtrarTiposReemplazos.get(index))) {
                    int crearIndex = crearTiposReemplazos.indexOf(filtrarTiposReemplazos.get(index));
                    crearTiposReemplazos.remove(crearIndex);
                } else {
                    borrarTiposReemplazos.add(filtrarTiposReemplazos.get(index));
                }
                int VCIndex = listTiposReemplazos.indexOf(filtrarTiposReemplazos.get(index));
                listTiposReemplazos.remove(VCIndex);
                filtrarTiposReemplazos.remove(index);

            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (listTiposReemplazos == null || listTiposReemplazos.isEmpty()) {
                infoRegistro = "Cantidad de registros: 0 ";
            } else {
                infoRegistro = "Cantidad de registros: " + listTiposReemplazos.size();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            index = -1;
            secRegistro = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }

    }

    public void verificarBorrado() {
        System.out.println("ESTOY EN VERIFICAR BORRADO");
        BigInteger verificarBorrandoEncargaturas;
        BigInteger verificarBorradoProgramacionesTiempos;
        BigInteger verificarBorradoReemplazos;
        try {
            if (tipoLista == 0) {
                verificarBorrandoEncargaturas = administrarTiposReemplazos.contarEncargaturasTipoReemplazo(listTiposReemplazos.get(index).getSecuencia());
                verificarBorradoProgramacionesTiempos = administrarTiposReemplazos.contarProgramacionesTiemposTipoReemplazo(listTiposReemplazos.get(index).getSecuencia());
                verificarBorradoReemplazos = administrarTiposReemplazos.contarReemplazosTipoReemplazo(listTiposReemplazos.get(index).getSecuencia());
            } else {
                verificarBorrandoEncargaturas = administrarTiposReemplazos.contarEncargaturasTipoReemplazo(filtrarTiposReemplazos.get(index).getSecuencia());
                verificarBorradoProgramacionesTiempos = administrarTiposReemplazos.contarProgramacionesTiemposTipoReemplazo(filtrarTiposReemplazos.get(index).getSecuencia());
                verificarBorradoReemplazos = administrarTiposReemplazos.contarReemplazosTipoReemplazo(filtrarTiposReemplazos.get(index).getSecuencia());

            }

            if (verificarBorrandoEncargaturas.equals(new BigInteger("0")) && verificarBorradoProgramacionesTiempos.equals(new BigInteger("0")) && verificarBorradoReemplazos.equals(new BigInteger("0"))) {
                System.out.println("Borrado==0");
                borrandoTiposReemplazos();
            } else {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                index = -1;

                verificarBorrandoEncargaturas = new BigInteger("-1");
                verificarBorradoProgramacionesTiempos = new BigInteger("-1");
                verificarBorradoReemplazos = new BigInteger("-1");
            }
        } catch (Exception e) {
            System.err.println("ERROR ControlTiposCertificados verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarTiposReemplazos.isEmpty() || !crearTiposReemplazos.isEmpty() || !modificarTiposReemplazos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }

    }

    public void guardarTipoReemplazo() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando TipoReemplazo");
            if (!borrarTiposReemplazos.isEmpty()) {
                administrarTiposReemplazos.borrarTiposReemplazos(borrarTiposReemplazos);
                //mostrarBorrados
                registrosBorrados = borrarTiposReemplazos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarTiposReemplazos.clear();
            }
            if (!crearTiposReemplazos.isEmpty()) {
                administrarTiposReemplazos.crearTiposReemplazos(crearTiposReemplazos);
                crearTiposReemplazos.clear();
            }
            if (!modificarTiposReemplazos.isEmpty()) {
                administrarTiposReemplazos.modificarTiposReemplazos(modificarTiposReemplazos);
                modificarTiposReemplazos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposReemplazos = null;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");

            k = 0;
        }
        index = -1;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarTipoReemplazo = listTiposReemplazos.get(index);
            }
            if (tipoLista == 1) {
                editarTipoReemplazo = filtrarTiposReemplazos.get(index);
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
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFactorRiesgo");
                RequestContext.getCurrentInstance().execute("PF('editarFactorRiesgo').show()");
                cualCelda = -1;
            }

        }
        index = -1;
        secRegistro = null;
    }

    public void agregarNuevoTiposReemplazos() {
        System.out.println("agregarNuevoTiposReemplazos");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoTipoReemplazo.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("codigo en Motivo Cambio Cargo: " + nuevoTipoReemplazo.getCodigo());

            for (int x = 0; x < listTiposReemplazos.size(); x++) {
                if (listTiposReemplazos.get(x).getCodigo() == nuevoTipoReemplazo.getCodigo()) {
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
        if (nuevoTipoReemplazo.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
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
                codigo = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                factorReemplazado = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:factorReemplazado");
                factorReemplazado.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
                bandera = 0;
                filtrarTiposReemplazos = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTipoReemplazo.setSecuencia(l);

            crearTiposReemplazos.add(nuevoTipoReemplazo);

            listTiposReemplazos.add(nuevoTipoReemplazo);
            nuevoTipoReemplazo = new TiposReemplazos();
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");

            infoRegistro = "Cantidad de registros: " + listTiposReemplazos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposReemplazos').hide()");
            index = -1;
            secRegistro = null;

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposReemplazos() {
        System.out.println("limpiarNuevoTiposReemplazos");
        nuevoTipoReemplazo = new TiposReemplazos();
        secRegistro = null;
        index = -1;

    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposReemplazos() {
        System.out.println("duplicandoTiposReemplazos");
        if (index >= 0) {
            duplicarTipoReemplazo = new TiposReemplazos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarTipoReemplazo.setSecuencia(l);
                duplicarTipoReemplazo.setCodigo(listTiposReemplazos.get(index).getCodigo());
                duplicarTipoReemplazo.setNombre(listTiposReemplazos.get(index).getNombre());
                duplicarTipoReemplazo.setFactorreemplazado(listTiposReemplazos.get(index).getFactorreemplazado());
            }
            if (tipoLista == 1) {
                duplicarTipoReemplazo.setSecuencia(l);
                duplicarTipoReemplazo.setCodigo(filtrarTiposReemplazos.get(index).getCodigo());
                duplicarTipoReemplazo.setNombre(filtrarTiposReemplazos.get(index).getNombre());
                duplicarTipoReemplazo.setFactorreemplazado(filtrarTiposReemplazos.get(index).getFactorreemplazado());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTR");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').show()");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR TIPOSREEMPLAZOS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;

        if (duplicarTipoReemplazo.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposReemplazos.size(); x++) {
                if (listTiposReemplazos.get(x).getCodigo() == duplicarTipoReemplazo.getCodigo()) {
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
        if (duplicarTipoReemplazo.getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarTipoReemplazo.setSecuencia(l);
            System.out.println("Datos Duplicando: " + duplicarTipoReemplazo.getSecuencia() + "  " + duplicarTipoReemplazo.getCodigo());
            if (crearTiposReemplazos.contains(duplicarTipoReemplazo)) {
                System.out.println("Ya lo contengo.");
            }
            listTiposReemplazos.add(duplicarTipoReemplazo);
            crearTiposReemplazos.add(duplicarTipoReemplazo);
            RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
            infoRegistro = "Cantidad de registros: " + listTiposReemplazos.size();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                factorReemplazado = (Column) c.getViewRoot().findComponent("form:datosTipoReemplazo:factorReemplazado");
                factorReemplazado.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTipoReemplazo");
                bandera = 0;
                filtrarTiposReemplazos = null;
                tipoLista = 0;
            }
            duplicarTipoReemplazo = new TiposReemplazos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposReemplazos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarTiposReemplazos() {
        duplicarTipoReemplazo = new TiposReemplazos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSREEMPLAZOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTipoReemplazoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSREEMPLAZOS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (!listTiposReemplazos.isEmpty()) {
            if (secRegistro != null) {
                System.out.println("lol 2");
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TIPOSREEMPLAZOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSREEMPLAZOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    //--------///////////////////////---------------------*****//*/*/*/*/*/-****----
    public List<TiposReemplazos> getListTiposReemplazos() {
        if (listTiposReemplazos == null) {
            listTiposReemplazos = administrarTiposReemplazos.consultarTiposReemplazos();
        }
        RequestContext context = RequestContext.getCurrentInstance();
        if (listTiposReemplazos == null || listTiposReemplazos.isEmpty()) {
            infoRegistro = "Cantidad de registros: 0 ";
        } else {
            infoRegistro = "Cantidad de registros: " + listTiposReemplazos.size();
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        return listTiposReemplazos;
    }

    public void setListTiposReemplazos(List<TiposReemplazos> listTiposReemplazos) {
        this.listTiposReemplazos = listTiposReemplazos;
    }

    public List<TiposReemplazos> getFiltrarTiposReemplazos() {
        return filtrarTiposReemplazos;
    }

    public void setFiltrarTiposReemplazos(List<TiposReemplazos> filtrarTiposReemplazos) {
        this.filtrarTiposReemplazos = filtrarTiposReemplazos;
    }

    public TiposReemplazos getNuevoTipoReemplazo() {
        return nuevoTipoReemplazo;
    }

    public void setNuevoTipoReemplazo(TiposReemplazos nuevoTipoReemplazo) {
        this.nuevoTipoReemplazo = nuevoTipoReemplazo;
    }

    public TiposReemplazos getDuplicarTipoReemplazo() {
        return duplicarTipoReemplazo;
    }

    public void setDuplicarTipoReemplazo(TiposReemplazos duplicarTipoReemplazo) {
        this.duplicarTipoReemplazo = duplicarTipoReemplazo;
    }

    public TiposReemplazos getEditarTipoReemplazo() {
        return editarTipoReemplazo;
    }

    public void setEditarTipoReemplazo(TiposReemplazos editarTipoReemplazo) {
        this.editarTipoReemplazo = editarTipoReemplazo;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
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

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public TiposReemplazos getTiposReemplazosSeleccionado() {
        return tiposReemplazosSeleccionado;
    }

    public void setTiposReemplazosSeleccionado(TiposReemplazos tiposReemplazosSeleccionado) {
        this.tiposReemplazosSeleccionado = tiposReemplazosSeleccionado;
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
