/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Conceptos;
import Entidades.GruposConceptos;
import Entidades.VigenciasGruposConceptos;
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarGruposConceptosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;
import utilidadesUI.PrimefacesContextUI;

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlGrupoConcepto implements Serializable {

    @EJB
    AdministrarGruposConceptosInterface administrarGruposConceptos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Lista Grupos Conceptos(Arriba)
    private List<GruposConceptos> listaGruposConceptos;
    private List<GruposConceptos> filtradoListaGruposConceptos;
    private GruposConceptos grupoConceptoSeleccionado;
    //Lista Vigencias Grupos Conceptos
    private List<VigenciasGruposConceptos> listaVigenciasGruposConceptos;
    private List<VigenciasGruposConceptos> filtradoListaVigenciasGruposConceptos;
    private VigenciasGruposConceptos vigenciasGruposConceptosSeleccionado;
    //REGISTRO ACTUAL
    private int registroActual, index, tablaActual, indexD;
    //OTROS
    private boolean aceptar, mostrarTodos;
    private String altoScrollGruposConceptos, altoScrollVigenciasGruposConceptos;
    //Crear Vigencias VigenciasGruposConceptos (Arriba)
    private List<GruposConceptos> listaGruposConceptosCrear;
    public GruposConceptos nuevoGruposConceptos;
    public GruposConceptos duplicarGruposConceptos;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Vigencias VigenciasGruposConceptos
    private List<GruposConceptos> listaGruposConceptosModificar;
    //Borrar Vigencias VigenciasGruposConceptos
    private List<GruposConceptos> listaGruposConceptosBorrar;
    //Crear VigenciasGruposConceptos (Abajo)
    private List<VigenciasGruposConceptos> listaVigenciasGruposConceptosCrear;
    public VigenciasGruposConceptos duplicarVigenciaGruposConceptos;
    //Modificar VigenciasGruposConceptos
    private List<VigenciasGruposConceptos> listaVigenciasGruposConceptosModificar;
    //Borrar VigenciasGruposConceptos
    private List<VigenciasGruposConceptos> listaVigenciasGruposConceptosBorrar;
    public VigenciasGruposConceptos nuevoVigenciasGruposConceptos;
    //OTROS
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //editar celda
    private GruposConceptos editarGruposConceptos;
    private VigenciasGruposConceptos editarVigenciasGruposConceptos;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    private int cualCeldaD;
    private int tipoListaD;
    //Autocompletar
    private String codigo, empresa;
    //RASTROS
    private BigInteger secRegistro;
    private BigInteger secRegistroD;
    private boolean guardado, guardarOk;
    private boolean cambiosPagina;
    //FILTRADO
    private Column gcCodigo, gcDescripcion, gcFundamental;
    private Column vCodigo, vDescripcion, vNaturaleza, vInicial, vFinal, vEmpresa;
    //Tabla a Imprimir
    private String tablaImprimir, nombreArchivo;
    //Sec Abajo Duplicar
    private int m;
    private BigInteger n;
    //SECUENCIA DE VIGENCIA
    private BigInteger secuenciaGrupoConcepto;
    private Date fechaParametro;
    private Date fechaInicial;
    private Date fechaFinal;
    private Integer cualTabla;
    //LOVLista Conceptos(Abajo)
    private List<Conceptos> lovlistaConceptos;
    private List<Conceptos> lovfiltradoListaConceptos;
    private Conceptos conceptoSeleccionado;
    //LOV GRUPOS CONCEPTOS
    private List<GruposConceptos> lovlistaGruposConceptos;
    private List<GruposConceptos> lovfiltradoListaGruposConceptos;
    private GruposConceptos gruposSeleccionado;
    private String paginaAnterior;

    public ControlGrupoConcepto() {
        paginaAnterior = "";
        permitirIndex = true;
        cualTabla = 0;
        bandera = 0;
        registroActual = 0;
        mostrarTodos = true;
        altoScrollGruposConceptos = "90";
        altoScrollVigenciasGruposConceptos = "90";
        listaGruposConceptosBorrar = new ArrayList<GruposConceptos>();
        listaGruposConceptosCrear = new ArrayList<GruposConceptos>();
        listaGruposConceptosModificar = new ArrayList<GruposConceptos>();
        listaVigenciasGruposConceptosBorrar = new ArrayList<VigenciasGruposConceptos>();
        listaVigenciasGruposConceptosCrear = new ArrayList<VigenciasGruposConceptos>();
        listaVigenciasGruposConceptosModificar = new ArrayList<VigenciasGruposConceptos>();
        tablaImprimir = ":formExportar:datosGruposConceptosExportar";
        nombreArchivo = "GruposConceptosXML";
        //Crear GruposConceptos
        nuevoGruposConceptos = new GruposConceptos();
        nuevoGruposConceptos.setFundamental("N");
        duplicarGruposConceptos = new GruposConceptos();
        //Crear VigenciasGruposConceptos
        nuevoVigenciasGruposConceptos = new VigenciasGruposConceptos();
        duplicarVigenciaGruposConceptos = new VigenciasGruposConceptos();
        m = 0;
        cambiosPagina = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarGruposConceptos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void seleccionarTipoNuevoFundamental(String estadoTipo, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (estadoTipo.equals("SI ES PERSONALIZABLE")) {
                nuevoGruposConceptos.setFundamental("S");
            } else if (estadoTipo.equals("NO ES PERSONALIZABLE")) {
                nuevoGruposConceptos.setFundamental("N");
            }
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoFundamental");
        } else {
            if (estadoTipo.equals("SI ES PERSONALIZABLE")) {
                duplicarGruposConceptos.setFundamental("S");
            } else if (estadoTipo.equals("NO ES PERSONALIZABLE")) {
                duplicarGruposConceptos.setFundamental("N");
            }
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarFundamental");
        }
    }

    public void actualizarConceptos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listaVigenciasGruposConceptos.get(index).setConcepto(conceptoSeleccionado);
                if (!listaVigenciasGruposConceptosCrear.contains(listaVigenciasGruposConceptos.get(index))) {
                    if (listaVigenciasGruposConceptosModificar.isEmpty()) {
                        listaVigenciasGruposConceptosModificar.add(listaVigenciasGruposConceptos.get(index));
                    } else if (!listaVigenciasGruposConceptosModificar.contains(listaVigenciasGruposConceptos.get(index))) {
                        listaVigenciasGruposConceptosModificar.add(listaVigenciasGruposConceptos.get(index));
                    }
                }
            } else {
                filtradoListaVigenciasGruposConceptos.get(index).setConcepto(conceptoSeleccionado);
                if (!listaVigenciasGruposConceptosCrear.contains(filtradoListaVigenciasGruposConceptos.get(index))) {
                    if (listaVigenciasGruposConceptosModificar.isEmpty()) {
                        listaVigenciasGruposConceptosModificar.add(filtradoListaVigenciasGruposConceptos.get(index));
                    } else if (!listaVigenciasGruposConceptosModificar.contains(filtradoListaVigenciasGruposConceptos.get(index))) {
                        listaVigenciasGruposConceptosModificar.add(filtradoListaVigenciasGruposConceptos.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
            }
            cambiosPagina = false;
            permitirIndex = true;
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
        } else if (tipoActualizacion == 1) {
            nuevoVigenciasGruposConceptos.setConcepto(conceptoSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoCodigoV");
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoDescripcionV");
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoNaturaleza");
            PrimefacesContextUI.actualizar("formularioDialogos:nuevoEmpresa");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaGruposConceptos.setConcepto(conceptoSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarCodigoV");
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarDescripcionV");
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarNaturaleza");
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarEmpresa");

        }

        lovfiltradoListaConceptos = null;
        conceptoSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("formularioDialogos:LOVConceptos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVConceptos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('conceptosDialogo').hide()");
        //PrimefacesContextUI.actualizar("formularioDialogos:LOVConceptos");
    }

    public void cancelarCambioConceptos() {
        lovfiltradoListaConceptos = null;
        conceptoSeleccionado = null;
        aceptar = true;
        indexD = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVConceptos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVConceptos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('conceptosDialogo').hide()");
    }

    //CREAR Grupo Concepto
    public void agregarNuevoGrupoConcepto() {
        int pasa = 0;
        int pasar = 0;

        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoGruposConceptos.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + " * Codigo\n";
            pasa++;
        }
        if (nuevoGruposConceptos.getDescripcion().equals(" ")) {
            mensajeValidacion = mensajeValidacion + " * Descripcion\n";
            pasa++;
        }

        for (int i = 0; i < listaGruposConceptos.size(); i++) {
            if (nuevoGruposConceptos.getCodigo() == listaGruposConceptos.get(i).getCodigo()) {
                PrimefacesContextUI.actualizar("formularioDialogos:codigos");
                PrimefacesContextUI.ejecutar("PF('codigos').show()");
                pasar++;
            }
        }

        if (pasa != 0) {
            PrimefacesContextUI.actualizar("formularioDialogos:validacionNuevo");
            PrimefacesContextUI.ejecutar("PF('validacionNuevo').show()");
        }

        if (pasa == 0 && pasar == 0) {
            if (bandera == 1) {
                gcCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcCodigo");
                gcCodigo.setFilterStyle("display: none; visibility: hidden;");
                gcDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcDescripcion");
                gcDescripcion.setFilterStyle("display: none; visibility: hidden;");
                gcFundamental = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcFundamental");
                gcFundamental.setFilterStyle("display: none; visibility: hidden;");
                altoScrollGruposConceptos = "90";
                PrimefacesContextUI.actualizar("form:datosGruposConceptos");
                bandera = 0;
                filtradoListaGruposConceptos = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoGruposConceptos.setSecuencia(l);
            cambiosPagina = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            listaGruposConceptosCrear.add(nuevoGruposConceptos);
            listaGruposConceptos.add(nuevoGruposConceptos);
            PrimefacesContextUI.actualizar("form:datosGruposConceptos");
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:aceptar");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroGruposConceptos').hide()");
            nuevoGruposConceptos = new GruposConceptos();
            PrimefacesContextUI.actualizar("formularioDialogos:NuevoRegistroGruposConceptos");
            index = -1;
            secRegistro = null;
        }
    }

    //CREAR Grupo Concepto
    public void agregarNuevoVigenciaGrupoConcepto() {
        int pasa = 0;
        int pasar = 0;

        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoVigenciasGruposConceptos.getConcepto() == null) {
            mensajeValidacion = mensajeValidacion + " * Codigo\n";
            pasa++;
        }

        if (pasa != 0) {
            PrimefacesContextUI.actualizar("formularioDialogos:validacionNuevo");
            PrimefacesContextUI.ejecutar("PF('validacionNuevo').show()");
        }

        if (pasa == 0 && pasar == 0) {
            if (bandera == 1) {
                vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vCodigo");
                vCodigo.setFilterStyle("width: 85%;");
                vDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vDescripcion");
                vDescripcion.setFilterStyle("width: 85%;");
                vNaturaleza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vNaturaleza");
                vNaturaleza.setFilterStyle("width: 85%;");
                vInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vInicial");
                vInicial.setFilterStyle("");
                vFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vFinal");
                vFinal.setFilterStyle("");
                vEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vEmpresa");
                vEmpresa.setFilterStyle("");
                altoScrollVigenciasGruposConceptos = "66";
                PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
                bandera = 1;
                tipoListaD = 1;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoVigenciasGruposConceptos.setSecuencia(l);
            nuevoVigenciasGruposConceptos.setGrupoconcepto(grupoConceptoSeleccionado);
            cambiosPagina = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            listaVigenciasGruposConceptos.add(nuevoVigenciasGruposConceptos);
            listaVigenciasGruposConceptos.add(nuevoVigenciasGruposConceptos);
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:aceptar");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroVigenciasGruposConceptos').hide()");
            nuevoGruposConceptos = new GruposConceptos();
            PrimefacesContextUI.actualizar("formularioDialogos:NuevoRegistroVigenciasGruposConceptos");
            index = -1;
            secRegistro = null;
        }
    }

    public void seleccionarFundamental(String estadoFundamental, int indice, int celda) {
        if (tipoListaD == 0) {
            if (estadoFundamental.equals("SI ES PERSONALIZABLE")) {
                listaGruposConceptos.get(indice).setFundamental("S");
            } else if (estadoFundamental.equals("NO ES PERSONALIZABLE")) {
                listaGruposConceptos.get(indice).setFundamental("N");
            }

            if (!listaGruposConceptosCrear.contains(listaGruposConceptos.get(indice))) {
                if (listaGruposConceptosModificar.isEmpty()) {
                    listaGruposConceptosModificar.add(listaGruposConceptos.get(indice));
                } else if (!listaGruposConceptosModificar.contains(listaGruposConceptos.get(indice))) {
                    listaGruposConceptosModificar.add(listaGruposConceptos.get(indice));
                }
            }
        } else {
            if (estadoFundamental.equals("SI ES PERSONALIZABLE")) {
                filtradoListaGruposConceptos.get(indice).setFundamental("S");
            } else if (estadoFundamental.equals("NO ES PERSONALIZABLE")) {
                filtradoListaGruposConceptos.get(indice).setFundamental("N");
            }

            if (!listaGruposConceptosCrear.contains(filtradoListaGruposConceptos.get(indice))) {
                if (listaGruposConceptosModificar.isEmpty()) {
                    listaGruposConceptosModificar.add(filtradoListaGruposConceptos.get(indice));
                } else if (!listaGruposConceptosModificar.contains(filtradoListaGruposConceptos.get(indice))) {
                    listaGruposConceptosModificar.add(filtradoListaGruposConceptos.get(indice));
                }
            }
        }
        if (guardado == true) {
            guardado = false;
        }
        PrimefacesContextUI.actualizar("form:datosGruposConceptos");
    }

    //EVENTO FILTRAR
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
    }

    //EVENTO FILTRAR
    public void eventoFiltrarD() {
        if (tipoListaD == 0) {
            tipoListaD = 1;
        }
    }

    //Ubicacion Celda Arriba 
    public void cambiarVigencia() {
        //Si ninguna de las 3 listas (crear,modificar,borrar) tiene algo, hace esto
        //{
        if (listaGruposConceptosCrear.isEmpty() && listaGruposConceptosBorrar.isEmpty() && listaGruposConceptosModificar.isEmpty()) {
            secuenciaGrupoConcepto = grupoConceptoSeleccionado.getSecuencia();
            listaVigenciasGruposConceptos = null;
            getListaVigenciasGruposConceptos();
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
            //}
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:cambiar");
            PrimefacesContextUI.ejecutar("PF('cambiar').show()");

        }
    }

    public void limpiarListas() {
        listaGruposConceptosCrear.clear();
        listaGruposConceptosBorrar.clear();
        listaGruposConceptosModificar.clear();
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosGruposConceptos");
    }

    //BORRAR 
    public void borrar() {

        if (index >= 0 && cualTabla == 0) {
            if (listaVigenciasGruposConceptos.isEmpty()) {
                if (tipoLista == 0) {
                    if (!listaGruposConceptosModificar.isEmpty() && listaGruposConceptosModificar.contains(listaGruposConceptos.get(index))) {
                        int modIndex = listaGruposConceptosModificar.indexOf(listaGruposConceptos.get(index));
                        listaGruposConceptosModificar.remove(modIndex);
                        listaGruposConceptosBorrar.add(listaGruposConceptos.get(index));
                    } else if (!listaGruposConceptosCrear.isEmpty() && listaGruposConceptosCrear.contains(listaGruposConceptos.get(index))) {
                        int crearIndex = listaGruposConceptosCrear.indexOf(listaGruposConceptos.get(index));
                        listaGruposConceptosCrear.remove(crearIndex);
                    } else {
                        listaGruposConceptosBorrar.add(listaGruposConceptos.get(index));
                    }
                    listaGruposConceptos.remove(index);
                }

                if (tipoLista == 1) {
                    if (!listaGruposConceptosModificar.isEmpty() && listaGruposConceptosModificar.contains(filtradoListaGruposConceptos.get(index))) {
                        int modIndex = listaGruposConceptosModificar.indexOf(filtradoListaGruposConceptos.get(index));
                        listaGruposConceptosModificar.remove(modIndex);
                        listaGruposConceptosBorrar.add(filtradoListaGruposConceptos.get(index));
                    } else if (!listaGruposConceptosCrear.isEmpty() && listaGruposConceptosCrear.contains(filtradoListaGruposConceptos.get(index))) {
                        int crearIndex = listaGruposConceptosCrear.indexOf(filtradoListaGruposConceptos.get(index));
                        listaGruposConceptosCrear.remove(crearIndex);
                    } else {
                        listaGruposConceptosBorrar.add(filtradoListaGruposConceptos.get(index));
                    }
                    int CIndex = listaGruposConceptos.indexOf(filtradoListaGruposConceptos.get(index));
                    listaGruposConceptos.remove(CIndex);
                    filtradoListaGruposConceptos.remove(index);
                    System.out.println("Realizado");
                }

                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:datosGruposConceptos");
                cambiosPagina = false;
                index = -1;
                secRegistro = null;

                if (guardado == true) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:aceptar");
                }
            } else {
                System.out.println("No se puede borrar porque tiene registros en la tabla de abajo");
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("formularioDialogos:registro");
                PrimefacesContextUI.ejecutar("PF('registro').show()");
            }
        } else if (indexD >= 0 && cualTabla == 1) {

            if (tipoListaD == 0) {
                if (!listaVigenciasGruposConceptosModificar.isEmpty() && listaVigenciasGruposConceptosModificar.contains(listaVigenciasGruposConceptos.get(indexD))) {
                    int modIndex = listaVigenciasGruposConceptosModificar.indexOf(listaVigenciasGruposConceptos.get(indexD));
                    listaVigenciasGruposConceptosModificar.remove(modIndex);
                    listaVigenciasGruposConceptosBorrar.add(listaVigenciasGruposConceptos.get(indexD));
                } else if (!listaVigenciasGruposConceptosCrear.isEmpty() && listaVigenciasGruposConceptosCrear.contains(listaVigenciasGruposConceptos.get(indexD))) {
                    int crearIndex = listaVigenciasGruposConceptosCrear.indexOf(listaVigenciasGruposConceptos.get(indexD));
                    listaVigenciasGruposConceptosCrear.remove(crearIndex);
                } else {
                    listaVigenciasGruposConceptosBorrar.add(listaVigenciasGruposConceptos.get(indexD));
                }
                listaVigenciasGruposConceptos.remove(indexD);
            }

            if (tipoListaD == 1) {
                if (!listaVigenciasGruposConceptosModificar.isEmpty() && listaVigenciasGruposConceptosModificar.contains(filtradoListaVigenciasGruposConceptos.get(indexD))) {
                    int modIndex = listaVigenciasGruposConceptosModificar.indexOf(filtradoListaVigenciasGruposConceptos.get(indexD));
                    listaVigenciasGruposConceptosModificar.remove(modIndex);
                    listaVigenciasGruposConceptosBorrar.add(filtradoListaVigenciasGruposConceptos.get(indexD));
                } else if (!listaVigenciasGruposConceptosCrear.isEmpty() && listaVigenciasGruposConceptosCrear.contains(filtradoListaVigenciasGruposConceptos.get(indexD))) {
                    int crearIndex = listaVigenciasGruposConceptosCrear.indexOf(filtradoListaVigenciasGruposConceptos.get(indexD));
                    listaVigenciasGruposConceptosCrear.remove(crearIndex);
                } else {
                    listaVigenciasGruposConceptosBorrar.add(filtradoListaVigenciasGruposConceptos.get(indexD));
                }
                int CIndex = listaVigenciasGruposConceptos.indexOf(filtradoListaVigenciasGruposConceptos.get(indexD));
                listaVigenciasGruposConceptos.remove(CIndex);
                filtradoListaVigenciasGruposConceptos.remove(indexD);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
            indexD = -1;
            secRegistro = null;
            cambiosPagina = false;

            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:aceptar");
            }
        }
    }

    public void guardarTodo() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones Grupo Concepto");
            if (!listaGruposConceptosBorrar.isEmpty()) {
                for (int i = 0; i < listaGruposConceptosBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    if (listaGruposConceptosBorrar.get(i).getFundamental() == null) {
                        listaGruposConceptosBorrar.get(i).setFundamental(null);
                    }

                    administrarGruposConceptos.borrarGruposConceptos(listaGruposConceptosBorrar.get(i));
                    System.out.println("Entra");
                    listaGruposConceptosBorrar.clear();
                }
            }
            if (!listaGruposConceptosCrear.isEmpty()) {
                for (int i = 0; i < listaGruposConceptosCrear.size(); i++) {
                    System.out.println("Creando...");
                    System.out.println(listaGruposConceptosCrear.size());
                    if (listaGruposConceptosCrear.get(i).getFundamental() == null) {
                        listaGruposConceptosCrear.get(i).setFundamental(null);
                    }
                    administrarGruposConceptos.crearGruposConceptos(listaGruposConceptosCrear.get(i));
                }

                System.out.println("LimpiaLista");
                listaGruposConceptosCrear.clear();
            }
            if (!listaGruposConceptosModificar.isEmpty()) {
                administrarGruposConceptos.modificarGruposConceptos(listaGruposConceptosModificar);
                listaGruposConceptosModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaGruposConceptos = null;
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosGruposConceptos");
            guardado = true;
            permitirIndex = true;
            cambiosPagina = true;
            PrimefacesContextUI.actualizar("form:aceptar");
            //  k = 0;
        }
        System.out.println("Valor k: " + k);
        index = -1;
        secRegistro = null;

        if (guardado == false) {
            System.out.println("Realizando Operaciones Vigencias");
            if (!listaVigenciasGruposConceptosBorrar.isEmpty()) {
                for (int i = 0; i < listaVigenciasGruposConceptosBorrar.size(); i++) {
                    System.out.println("Borrando...");

                    administrarGruposConceptos.borrarVigenciaGruposConceptos(listaVigenciasGruposConceptosBorrar.get(i));
                }

                System.out.println("Entra");
                listaVigenciasGruposConceptosBorrar.clear();
            }
        }
        if (!listaVigenciasGruposConceptosCrear.isEmpty()) {
            for (int i = 0; i < listaVigenciasGruposConceptosCrear.size(); i++) {
                System.out.println("Creando...");
                System.out.println(listaVigenciasGruposConceptosCrear.size());

                administrarGruposConceptos.crearVigenciaGruposConceptos(listaVigenciasGruposConceptosCrear.get(i));

            }

            System.out.println("LimpiaLista");
            listaVigenciasGruposConceptosCrear.clear();
        }
        if (!listaVigenciasGruposConceptosModificar.isEmpty()) {
            administrarGruposConceptos.modificarVigenciaGruposConceptos(listaVigenciasGruposConceptosModificar);

            listaVigenciasGruposConceptosModificar.clear();
        }

        System.out.println("Se guardaron los datos con exito");
        listaVigenciasGruposConceptos = null;
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage("Información", "Se han guardado los datos exitosamente.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
        PrimefacesContextUI.actualizar("form:growl");
        guardado = true;
        permitirIndex = true;
        PrimefacesContextUI.actualizar("form:aceptar");
        //  k = 0;

        System.out.println("Valor k: " + k);
        indexD = -1;
        cambiosPagina = true;
        secRegistro = null;

    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        System.out.println("cualTabla= " + cualTabla);
        if (bandera == 0 && cualTabla == 0) {
            System.out.println("Activa 1");
            //Tabla Vigencias VigenciasGruposConceptos
            gcCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcCodigo");
            gcCodigo.setFilterStyle("width: 85%");
            gcDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcDescripcion");
            gcDescripcion.setFilterStyle("width: 85%");
            gcFundamental = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcFundamental");
            gcFundamental.setFilterStyle("width: 85%");
            altoScrollGruposConceptos = "70";
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosGruposConceptos");
            bandera = 1;

        } else if (bandera == 1 && cualTabla == 0) {
            System.out.println("Desactiva 1");
            gcCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcCodigo");
            gcCodigo.setFilterStyle("display: none; visibility: hidden;");
            gcDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcDescripcion");
            gcDescripcion.setFilterStyle("display: none; visibility: hidden;");
            gcFundamental = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcFundamental");
            gcFundamental.setFilterStyle("display: none; visibility: hidden;");
            altoScrollGruposConceptos = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosGruposConceptos");
            bandera = 0;
            filtradoListaGruposConceptos = null;

        } else if (bandera == 0 && cualTabla == 1) {
            System.out.println("Activa 2");
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vCodigo");
            vCodigo.setFilterStyle("width: 85%;");
            vDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vDescripcion");
            vDescripcion.setFilterStyle("width: 85%;");
            vNaturaleza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vNaturaleza");
            vNaturaleza.setFilterStyle("width: 85%;");
            vInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vInicial");
            vInicial.setFilterStyle("");
            vFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vFinal");
            vFinal.setFilterStyle("");
            vEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vEmpresa");
            vEmpresa.setFilterStyle("");
            altoScrollVigenciasGruposConceptos = "66";
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
            bandera = 1;
            tipoListaD = 1;

        } else if (bandera == 1 && cualTabla == 1) {
            //SOLUCIONES NODOS EMPLEADO
            System.out.println("Desactiva 2");
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vDescripcion");
            vDescripcion.setFilterStyle("display: none; visibility: hidden;");
            vNaturaleza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vNaturaleza");
            vNaturaleza.setFilterStyle("display: none; visibility: hidden;");
            vInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vInicial");
            vInicial.setFilterStyle("display: none; visibility: hidden;");
            vFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vFinal");
            vFinal.setFilterStyle("display: none; visibility: hidden;");
            vEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vEmpresa");
            vEmpresa.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasGruposConceptos = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
            bandera = 0;
            tipoListaD = 0;
            filtradoListaVigenciasGruposConceptos = null;
        }
    }

    public void salir() {
        if (bandera == 1) {
            System.out.println("Desactiva 1");
            gcCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcCodigo");
            gcCodigo.setFilterStyle("display: none; visibility: hidden;");
            gcDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcDescripcion");
            gcDescripcion.setFilterStyle("display: none; visibility: hidden;");
            gcFundamental = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcFundamental");
            gcFundamental.setFilterStyle("display: none; visibility: hidden;");
            altoScrollGruposConceptos = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosGruposConceptos");
            bandera = 0;
            filtradoListaGruposConceptos = null;
            altoScrollGruposConceptos = "90";

            bandera = 0;
            filtradoListaGruposConceptos = null;
            tipoLista = 0;
        }

        if (bandera == 1) {
            //SOLUCIONES NODOS EMPLEADO
            System.out.println("Desactiva 2");
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vDescripcion");
            vDescripcion.setFilterStyle("display: none; visibility: hidden;");
            vNaturaleza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vNaturaleza");
            vNaturaleza.setFilterStyle("display: none; visibility: hidden;");
            vInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vInicial");
            vInicial.setFilterStyle("display: none; visibility: hidden;");
            vFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vFinal");
            vFinal.setFilterStyle("display: none; visibility: hidden;");
            vEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vEmpresa");
            vEmpresa.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasGruposConceptos = "90";
            bandera = 0;
            tipoListaD = 0;
            filtradoListaVigenciasGruposConceptos = null;

            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
            bandera = 0;
            filtradoListaVigenciasGruposConceptos = null;
            tipoListaD = 0;
        }
        listaGruposConceptosBorrar.clear();
        listaGruposConceptosCrear.clear();
        listaGruposConceptosModificar.clear();
        index = -1;
        secRegistro = null;

        listaGruposConceptos = null;

        listaVigenciasGruposConceptosBorrar.clear();
        listaVigenciasGruposConceptosCrear.clear();
        listaVigenciasGruposConceptosModificar.clear();
        indexD = -1;

        listaVigenciasGruposConceptos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosGruposConceptos");
        PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");

    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            System.out.println("Desactiva 1");
            gcCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcCodigo");
            gcCodigo.setFilterStyle("display: none; visibility: hidden;");
            gcDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcDescripcion");
            gcDescripcion.setFilterStyle("display: none; visibility: hidden;");
            gcFundamental = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcFundamental");
            gcFundamental.setFilterStyle("display: none; visibility: hidden;");
            altoScrollGruposConceptos = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosGruposConceptos");
            bandera = 0;
            filtradoListaGruposConceptos = null;
            altoScrollGruposConceptos = "90";

            bandera = 0;
            filtradoListaGruposConceptos = null;
            tipoLista = 0;
        }

        if (bandera == 1) {
            //SOLUCIONES NODOS EMPLEADO
            System.out.println("Desactiva 2");
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vDescripcion");
            vDescripcion.setFilterStyle("display: none; visibility: hidden;");
            vNaturaleza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vNaturaleza");
            vNaturaleza.setFilterStyle("display: none; visibility: hidden;");
            vInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vInicial");
            vInicial.setFilterStyle("display: none; visibility: hidden;");
            vFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vFinal");
            vFinal.setFilterStyle("display: none; visibility: hidden;");
            vEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vEmpresa");
            vEmpresa.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasGruposConceptos = "90";
            PrimefacesContextUI.actualizar("form:datosGruposConceptosDetalles");
            bandera = 0;
            filtradoListaVigenciasGruposConceptos = null;
            tipoListaD = 0;
        }
        listaGruposConceptosBorrar.clear();
        listaGruposConceptosCrear.clear();
        listaGruposConceptosModificar.clear();
        index = -1;
        secRegistro = null;

        listaGruposConceptos = null;

        listaVigenciasGruposConceptosBorrar.clear();
        listaVigenciasGruposConceptosCrear.clear();
        listaVigenciasGruposConceptosModificar.clear();
        indexD = -1;

        listaVigenciasGruposConceptos = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosGruposConceptos");
        PrimefacesContextUI.actualizar("form:datosGruposConceptosDetalles");
    }

//CREAR Vigencia
    public void agregarNuevaVigencia() {
        int pasa = 0;
        int pasar = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoVigenciasGruposConceptos.getConcepto().getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + " * Codigo \n";
            pasa++;
        }

        if (nuevoVigenciasGruposConceptos.getFechainicial() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial";
        }

        if (nuevoVigenciasGruposConceptos.getFechafinal() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Final";
        }

        if (pasa != 0) {
            PrimefacesContextUI.actualizar("formularioDialogos:validacionNuevo");
            PrimefacesContextUI.ejecutar("PF('validacionNuevo').show()");
        }

        if (pasa == 0 && pasar == 0) {
            if (bandera == 1) {
                //SOLUCIONES NODOS EMPLEADO
                System.out.println("Desactiva 2");
                vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vCodigo");
                vCodigo.setFilterStyle("display: none; visibility: hidden;");
                vDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vDescripcion");
                vDescripcion.setFilterStyle("display: none; visibility: hidden;");
                vNaturaleza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vNaturaleza");
                vNaturaleza.setFilterStyle("display: none; visibility: hidden;");
                vInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vInicial");
                vInicial.setFilterStyle("display: none; visibility: hidden;");
                vFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vFinal");
                vFinal.setFilterStyle("display: none; visibility: hidden;");
                vEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vEmpresa");
                vEmpresa.setFilterStyle("display: none; visibility: hidden;");
                altoScrollVigenciasGruposConceptos = "90";
                PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
                bandera = 0;
                filtradoListaVigenciasGruposConceptos = null;
                tipoListaD = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoVigenciasGruposConceptos.setSecuencia(l);
            System.out.println("grupoConceptoSeleccionado" + grupoConceptoSeleccionado.getCodigo());
            nuevoVigenciasGruposConceptos.setGrupoconcepto(grupoConceptoSeleccionado);
            cambiosPagina = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            listaVigenciasGruposConceptosCrear.add(nuevoVigenciasGruposConceptos);
            listaVigenciasGruposConceptos.add(nuevoVigenciasGruposConceptos);

            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:aceptar");
            }
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroVigenciasGruposConceptos').hide()");
            nuevoVigenciasGruposConceptos = new VigenciasGruposConceptos();
            PrimefacesContextUI.actualizar("formularioDialogos:NuevoRegistroVigenciasGruposConceptos");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (pasa == 0) {
            listaGruposConceptos.add(duplicarGruposConceptos);
            listaGruposConceptosCrear.add(duplicarGruposConceptos);
            PrimefacesContextUI.actualizar("form:datosGruposConceptos");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:aceptar");
            }
            if (bandera == 1) {
                gcCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcCodigo");
                gcCodigo.setFilterStyle("display: none; visibility: hidden;");
                gcDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcDescripcion");
                gcDescripcion.setFilterStyle("display: none; visibility: hidden;");
                gcFundamental = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosGruposConceptos:gcFundamental");
                gcFundamental.setFilterStyle("display: none; visibility: hidden;");
                altoScrollGruposConceptos = "90";
                PrimefacesContextUI.actualizar("form:datosGruposConceptos");
                bandera = 0;
                filtradoListaGruposConceptos = null;
                tipoLista = 0;
            }
        }
        duplicarGruposConceptos = new GruposConceptos();
    }

    public void confirmarDuplicarD() {

        listaVigenciasGruposConceptos.add(duplicarVigenciaGruposConceptos);
        listaVigenciasGruposConceptosCrear.add(duplicarVigenciaGruposConceptos);
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
        index = -1;
        secRegistro = null;
        if (guardado == true) {
            guardado = false;
            PrimefacesContextUI.actualizar("form:aceptar");
        }
        if (bandera == 1) {
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vDescripcion = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vDescripcion");
            vDescripcion.setFilterStyle("display: none; visibility: hidden;");
            vNaturaleza = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vNaturaleza");
            vNaturaleza.setFilterStyle("display: none; visibility: hidden;");
            vInicial = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vInicial");
            vInicial.setFilterStyle("display: none; visibility: hidden;");
            vFinal = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vFinal");
            vFinal.setFilterStyle("display: none; visibility: hidden;");
            vEmpresa = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasGruposConceptos:vEmpresa");
            vEmpresa.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasGruposConceptos = "90";
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
            bandera = 0;
            filtradoListaVigenciasGruposConceptos = null;
            tipoListaD = 0;
        }

        duplicarVigenciaGruposConceptos = new VigenciasGruposConceptos();
    }

    //DUPLICAR Grupos/Vigencias
    public void duplicarE() {
        if (index >= 0 && cualTabla == 0) {
            duplicarGruposConceptos = new GruposConceptos();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarGruposConceptos.setSecuencia(l);
                duplicarGruposConceptos.setCodigo(listaGruposConceptos.get(index).getCodigo());
                duplicarGruposConceptos.setDescripcion(listaGruposConceptos.get(index).getDescripcion());
                duplicarGruposConceptos.setFundamental(listaGruposConceptos.get(index).getFundamental());
            }
            if (tipoLista == 1) {
                duplicarGruposConceptos.setSecuencia(l);
                duplicarGruposConceptos.setCodigo(filtradoListaGruposConceptos.get(index).getCodigo());
                duplicarGruposConceptos.setDescripcion(filtradoListaGruposConceptos.get(index).getDescripcion());
                duplicarGruposConceptos.setFundamental(filtradoListaGruposConceptos.get(index).getFundamental());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarGrupoConcepto");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroGruposConceptos').show()");
            index = -1;
            secRegistro = null;
        } else if (indexD >= 0 && cualTabla == 1) {
            System.out.println("Entra Duplicar Vigencia Grupo Conceptos");

            duplicarVigenciaGruposConceptos = new VigenciasGruposConceptos();
            m++;
            n = BigInteger.valueOf(m);

            if (tipoListaD == 0) {
                duplicarVigenciaGruposConceptos.setSecuencia(n);
                duplicarVigenciaGruposConceptos.setGrupoconcepto(listaVigenciasGruposConceptos.get(indexD).getGrupoconcepto());
                duplicarVigenciaGruposConceptos.setConcepto(listaVigenciasGruposConceptos.get(indexD).getConcepto());
                duplicarVigenciaGruposConceptos.setFechainicial(listaVigenciasGruposConceptos.get(indexD).getFechainicial());
                duplicarVigenciaGruposConceptos.setFechafinal(listaVigenciasGruposConceptos.get(indexD).getFechafinal());
            }
            if (tipoListaD == 1) {
                duplicarVigenciaGruposConceptos.setSecuencia(n);
                duplicarVigenciaGruposConceptos.setGrupoconcepto(filtradoListaVigenciasGruposConceptos.get(indexD).getGrupoconcepto());
                duplicarVigenciaGruposConceptos.setConcepto(filtradoListaVigenciasGruposConceptos.get(indexD).getConcepto());
                duplicarVigenciaGruposConceptos.setFechainicial(filtradoListaVigenciasGruposConceptos.get(indexD).getFechainicial());
                duplicarVigenciaGruposConceptos.setFechafinal(filtradoListaVigenciasGruposConceptos.get(indexD).getFechafinal());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarVigenciasGruposConceptos");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroVigenciasGruposConceptos').show()");
            indexD = -1;
            secRegistro = null;

        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0 && cualTabla == 0) {
            if (tipoLista == 0) {
                editarGruposConceptos = listaGruposConceptos.get(index);
            }
            if (tipoLista == 1) {
                editarGruposConceptos = filtradoListaGruposConceptos.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCodigoGC");
                PrimefacesContextUI.ejecutar("PF('editarCodigoGC').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarDescripcionGC");
                PrimefacesContextUI.ejecutar("PF('editarDescripcionGC').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFundamental");
                PrimefacesContextUI.ejecutar("PF('editarFundamentalGC').show()");
                cualCelda = -1;
            }
            index = -1;
        } else if (indexD >= 0 && cualTabla == 1) {
            if (tipoListaD == 0) {
                editarVigenciasGruposConceptos = listaVigenciasGruposConceptos.get(indexD);
            }
            if (tipoListaD == 1) {
                editarVigenciasGruposConceptos = filtradoListaVigenciasGruposConceptos.get(indexD);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCeldaD);
            System.out.println("Cual Tabla: " + cualTabla);
            if (cualCeldaD == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCodigoV");
                PrimefacesContextUI.ejecutar("PF('editarCodigoV').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarDescripcionV");
                PrimefacesContextUI.ejecutar("PF('editarDescripcionV').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarNaturalezaV");
                PrimefacesContextUI.ejecutar("PF('editarNaturalezaV').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 3) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFechaInicialV");
                PrimefacesContextUI.ejecutar("PF('editarFechaInicialV').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 4) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFechaFinalV");
                PrimefacesContextUI.ejecutar("PF('editarFechaFinalV').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 5) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarEmpresaV");
                PrimefacesContextUI.ejecutar("PF('editarEmpresaV').show()");
                cualCeldaD = -1;
            }
            indexD = -1;
        }
        secRegistro = null;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    //LISTA DE VALORES DINAMICA
    public void listaValoresBoton() {
        if (index >= 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0 || cualCelda == 5) {
                PrimefacesContextUI.actualizar("form:conceptosDialogo");
                PrimefacesContextUI.ejecutar("PF('conceptosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("CONCEPTO")) {
            if (tipoNuevo == 1) {
                codigo = nuevoVigenciasGruposConceptos.getConcepto().getCodigoSTR();
            } else if (tipoNuevo == 2) {
                codigo = duplicarVigenciaGruposConceptos.getConcepto().getCodigoSTR();
            }
        } else if (Campo.equals("EMPRESA")) {
            if (tipoNuevo == 1) {
                empresa = nuevoVigenciasGruposConceptos.getConcepto().getEmpresa().getNombre();
            } else if (tipoNuevo == 2) {
                empresa = duplicarVigenciaGruposConceptos.getConcepto().getEmpresa().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) {
            if (tipoNuevo == 1) {
                nuevoVigenciasGruposConceptos.getConcepto().setCodigoSTR(codigo);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaGruposConceptos.getConcepto().setCodigoSTR(codigo);
            }
            for (int i = 0; i < lovlistaConceptos.size(); i++) {
                if (lovlistaConceptos.get(i).getCodigoSTR().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoVigenciasGruposConceptos.setConcepto(lovlistaConceptos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoVigenciaGrupoConcepto");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaGruposConceptos.setConcepto(lovlistaConceptos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarVigenciaGrupoConcepto");
                }
                lovlistaConceptos.clear();
                getLovlistaConceptos();
            } else {
                PrimefacesContextUI.actualizar("form:conceptosDialogo");
                PrimefacesContextUI.ejecutar("PF('conceptosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevoVigenciaGrupoConcepto");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarVigenciaGrupoConcepto");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
            if (tipoNuevo == 1) {
                nuevoVigenciasGruposConceptos.getConcepto().getEmpresa().setNombre(empresa);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaGruposConceptos.getConcepto().getEmpresa().setNombre(empresa);
            }

            for (int i = 0; i < lovlistaConceptos.size(); i++) {
                if (lovlistaConceptos.get(i).getEmpresa().getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevoVigenciasGruposConceptos.setConcepto(lovlistaConceptos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTercero");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaGruposConceptos.setConcepto(lovlistaConceptos.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTercero");
                }
                lovlistaConceptos.clear();
                getLovlistaConceptos();
            } else {
                PrimefacesContextUI.actualizar("form:conceptosDialogo");
                PrimefacesContextUI.ejecutar("PF('conceptosDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTercero");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTercero");
                }
            }
        }
    }

    public void mostrarTodos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaGruposConceptos.isEmpty()) {
            listaGruposConceptos.clear();
            listaGruposConceptos = administrarGruposConceptos.buscarGruposConceptos();
            grupoConceptoSeleccionado = listaGruposConceptos.get(0);
            listaVigenciasGruposConceptos = null;
            getListaVigenciasGruposConceptos();
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
        } else {
            listaGruposConceptos = administrarGruposConceptos.buscarGruposConceptos();
            grupoConceptoSeleccionado = listaGruposConceptos.get(0);
            listaVigenciasGruposConceptos = null;
            getListaVigenciasGruposConceptos();
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
        }
        if (!listaGruposConceptos.isEmpty()) {
            grupoConceptoSeleccionado = listaGruposConceptos.get(0);
            listaVigenciasGruposConceptos = null;
            getListaVigenciasGruposConceptos();
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
        }
        PrimefacesContextUI.actualizar("form:datosGruposConceptos");
        PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
        filtradoListaGruposConceptos = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void chiste() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (!listaGruposConceptos.isEmpty() && listaVigenciasGruposConceptos.isEmpty()) {
            PrimefacesContextUI.actualizar("formularioDialogos:elegirTabla");
            PrimefacesContextUI.ejecutar("PF('elegirTabla').show()");
        }
        int tamaño = listaGruposConceptos.size();

        if (tamaño == 0) {
            PrimefacesContextUI.actualizar("formularioDialogos:NuevoRegistroGruposConceptos");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroGruposConceptos').show()");
        }

        if (listaVigenciasGruposConceptos.isEmpty() && !listaGruposConceptos.isEmpty()) {
            PrimefacesContextUI.actualizar("formularioDialogos:elegirTabla");
            PrimefacesContextUI.ejecutar("PF('elegirTabla').show()");
        } else if (cualTabla == 0) {
            PrimefacesContextUI.actualizar("formularioDialogos:NuevoRegistroGruposConceptos");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroGruposConceptos').show()");
        } else if (cualTabla == 1) {
            PrimefacesContextUI.actualizar("formularioDialogos:NuevoRegistroVigenciasGruposConceptos");
            PrimefacesContextUI.ejecutar("PF('NuevoRegistroVigenciasGruposConceptos').show()");
        }
    }

    public void dialogoGruposConceptos() {
        cualTabla = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:NuevoRegistroGruposConceptos");
        PrimefacesContextUI.ejecutar("PF('NuevoRegistroGruposConceptos').show()");
    }

    public void dialogoVigenciasGruposConceptos() {
        cualTabla = 1;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:NuevoRegistroVigenciasGruposConceptos");
        PrimefacesContextUI.ejecutar("PF('NuevoRegistroVigenciasGruposConceptos').show()");
    }

    //Fechas
    public void modificarVigencias(int indiceD) {
        if (tipoListaD == 0) {
            if (!listaVigenciasGruposConceptosCrear.contains(listaVigenciasGruposConceptos.get(indiceD))) {
                if (listaVigenciasGruposConceptosModificar.isEmpty()) {
                    listaVigenciasGruposConceptosModificar.add(listaVigenciasGruposConceptos.get(indiceD));
                } else if (!listaVigenciasGruposConceptosModificar.contains(listaVigenciasGruposConceptos.get(indiceD))) {
                    listaVigenciasGruposConceptosModificar.add(listaVigenciasGruposConceptos.get(indiceD));
                }
                if (guardado == true) {
                    guardado = false;
                }
            }
        }
        if (tipoListaD == 1) {

            if (!listaVigenciasGruposConceptosCrear.contains(filtradoListaVigenciasGruposConceptos.get(indiceD))) {
                if (listaVigenciasGruposConceptosModificar.isEmpty()) {
                    listaVigenciasGruposConceptosModificar.add(filtradoListaVigenciasGruposConceptos.get(indiceD));
                } else if (!listaVigenciasGruposConceptosModificar.contains(filtradoListaVigenciasGruposConceptos.get(indiceD))) {
                    listaVigenciasGruposConceptosModificar.add(filtradoListaVigenciasGruposConceptos.get(indiceD));
                }
                if (guardado == true) {
                    guardado = false;
                    //PrimefacesContextUI.actualizar("form:aceptar");
                }
            }
        }
        indexD = -1;
        secRegistro = null;
    }

    public boolean validarFechasRegistro(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        System.err.println("fechaparametro : " + fechaParametro);
        boolean retorno = true;
        if (i == 0) {
            VigenciasGruposConceptos auxiliar = null;
            if (tipoLista == 0) {
                auxiliar = listaVigenciasGruposConceptos.get(index);
            }
            if (tipoLista == 1) {
                auxiliar = filtradoListaVigenciasGruposConceptos.get(index);
            }
            if (auxiliar.getFechafinal() != null) {
                if (auxiliar.getFechainicial().after(fechaParametro) && auxiliar.getFechainicial().before(auxiliar.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (auxiliar.getFechafinal() == null) {
                if (auxiliar.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (i == 1) {
            if (nuevoVigenciasGruposConceptos.getFechafinal() != null) {
                if (nuevoVigenciasGruposConceptos.getFechainicial().after(fechaParametro) && nuevoVigenciasGruposConceptos.getFechainicial().before(nuevoVigenciasGruposConceptos.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (nuevoVigenciasGruposConceptos.getFechafinal() == null) {
                if (nuevoVigenciasGruposConceptos.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        if (i == 2) {
            if (duplicarVigenciaGruposConceptos.getFechafinal() != null) {
                if (duplicarVigenciaGruposConceptos.getFechainicial().after(fechaParametro) && duplicarVigenciaGruposConceptos.getFechainicial().before(duplicarVigenciaGruposConceptos.getFechafinal())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
            if (duplicarVigenciaGruposConceptos.getFechafinal() == null) {
                if (duplicarVigenciaGruposConceptos.getFechainicial().after(fechaParametro)) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public void modificarFechas(int i, int c) {
        VigenciasGruposConceptos auxiliar = null;
        if (tipoLista == 0) {
            auxiliar = listaVigenciasGruposConceptos.get(i);
        }
        if (tipoLista == 1) {
            auxiliar = filtradoListaVigenciasGruposConceptos.get(i);
        }
        if (auxiliar.getFechainicial() != null) {
            boolean retorno = false;
            index = i;
            retorno = validarFechasRegistro(0);
            if (retorno == true) {
                cambiarIndiceD(i, c);
                modificarVigencias(i);
            } else {
                if (tipoLista == 0) {
                    listaVigenciasGruposConceptos.get(i).setFechafinal(fechaFinal);
                    listaVigenciasGruposConceptos.get(i).setFechainicial(fechaInicial);
                }
                if (tipoLista == 1) {
                    filtradoListaVigenciasGruposConceptos.get(i).setFechafinal(fechaFinal);
                    filtradoListaVigenciasGruposConceptos.get(i).setFechainicial(fechaInicial);

                }
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
                PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
            }
        } else {
            if (tipoLista == 0) {
                listaVigenciasGruposConceptos.get(i).setFechainicial(fechaInicial);
            }
            if (tipoLista == 1) {
                filtradoListaVigenciasGruposConceptos.get(i).setFechainicial(fechaInicial);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
            PrimefacesContextUI.ejecutar("PF('errorRegNew').show()");
        }
    }

    public void actualizarGruposConceptos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (!listaGruposConceptos.isEmpty()) {
            listaGruposConceptos.clear();
            listaGruposConceptos.add(gruposSeleccionado);
            grupoConceptoSeleccionado = listaGruposConceptos.get(0);
        } else {
            listaGruposConceptos.add(gruposSeleccionado);
        }
        secuenciaGrupoConcepto = grupoConceptoSeleccionado.getSecuencia();
        listaVigenciasGruposConceptos = null;
        getListaVigenciasGruposConceptos();
        context.reset("formularioDialogos:LOVGrupos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVGrupos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('gruposConceptosDialogo').hide()");
        //PrimefacesContextUI.actualizar("formularioDialogos:LOVGrupos");
        PrimefacesContextUI.actualizar("form:datosGruposConceptos");
        PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
        filtradoListaGruposConceptos = null;
        gruposSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
    }

    public void cancelarCambioGruposConceptos() {
        lovfiltradoListaGruposConceptos = null;
        gruposSeleccionado = null;
        aceptar = true;
        index = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVGrupos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('LOVGrupos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('gruposConceptosDialogo').hide()");
    }

    //AUTOCOMPLETAR Vigencias
    public void modificarGruposConceptos(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaGruposConceptosCrear.contains(listaGruposConceptos.get(index))) {

                    if (listaGruposConceptosModificar.isEmpty()) {
                        listaGruposConceptosModificar.add(listaGruposConceptos.get(index));
                    } else if (!listaGruposConceptosModificar.contains(listaGruposConceptos.get(index))) {
                        listaGruposConceptosModificar.add(listaGruposConceptos.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaGruposConceptosCrear.contains(filtradoListaGruposConceptos.get(index))) {

                    if (listaGruposConceptosCrear.isEmpty()) {
                        listaGruposConceptosCrear.add(filtradoListaGruposConceptos.get(index));
                    } else if (!listaGruposConceptosCrear.contains(filtradoListaGruposConceptos.get(index))) {
                        listaGruposConceptosCrear.add(filtradoListaGruposConceptos.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            }
            PrimefacesContextUI.actualizar("form:datosGruposConceptos");
        }
    }

    //AUTOCOMPLETAR VigenciasGruposConceptos
    public void modificarVigenciasGruposConceptos(int indiceD, String confirmarCambio, String valorConfirmar) {
        indexD = indiceD;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaVigenciasGruposConceptosCrear.contains(listaVigenciasGruposConceptos.get(indexD))) {

                    if (listaVigenciasGruposConceptosModificar.isEmpty()) {
                        listaVigenciasGruposConceptosModificar.add(listaVigenciasGruposConceptos.get(indexD));
                    } else if (!listaVigenciasGruposConceptosModificar.contains(listaVigenciasGruposConceptos.get(indexD))) {
                        listaVigenciasGruposConceptosModificar.add(listaVigenciasGruposConceptos.get(indexD));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
                indexD = -1;
                secRegistro = null;
            } else {
                if (!listaVigenciasGruposConceptosCrear.contains(filtradoListaVigenciasGruposConceptos.get(indexD))) {

                    if (listaVigenciasGruposConceptosCrear.isEmpty()) {
                        listaVigenciasGruposConceptosCrear.add(filtradoListaVigenciasGruposConceptos.get(indexD));
                    } else if (!listaVigenciasGruposConceptosCrear.contains(filtradoListaVigenciasGruposConceptos.get(indexD))) {
                        listaVigenciasGruposConceptosCrear.add(filtradoListaVigenciasGruposConceptos.get(indexD));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
                indexD = -1;
                secRegistro = null;
            }
            PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");
        } else if (confirmarCambio.equalsIgnoreCase("CONCEPTO")) { //Va la lista de valores de Conceptos con el query re ficti
            if (tipoLista == 0) {
                listaVigenciasGruposConceptos.get(indiceD).getConcepto().setCodigoSTR(codigo);
            } else {
                filtradoListaVigenciasGruposConceptos.get(indiceD).getConcepto().setCodigoSTR(codigo);
            }

            for (int i = 0; i < lovlistaConceptos.size(); i++) {
                if (lovlistaConceptos.get(i).getCodigoSTR().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaVigenciasGruposConceptos.get(indiceD).setConcepto(lovlistaConceptos.get(indiceUnicoElemento));
                } else {
                    filtradoListaVigenciasGruposConceptos.get(indiceD).setConcepto(lovlistaConceptos.get(indiceUnicoElemento));
                }
                lovlistaConceptos.clear();
                getLovlistaConceptos();
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:conceptosDialogo");
                PrimefacesContextUI.ejecutar("PF('conceptosDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
            if (tipoLista == 0) {
                listaVigenciasGruposConceptos.get(indiceD).getConcepto().getEmpresa().setNombre(empresa);
            } else {
                filtradoListaVigenciasGruposConceptos.get(indiceD).getConcepto().getEmpresa().setNombre(empresa);
            }

            for (int i = 0; i < lovlistaConceptos.size(); i++) {
                if (lovlistaConceptos.get(i).getEmpresa().getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    listaVigenciasGruposConceptos.get(indiceD).setConcepto(lovlistaConceptos.get(indiceUnicoElemento));
                } else {
                    filtradoListaVigenciasGruposConceptos.get(indiceD).setConcepto(lovlistaConceptos.get(indiceUnicoElemento));
                }
                lovlistaConceptos.clear();
                getLovlistaConceptos();
                cambiosPagina = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            } else {
                permitirIndex = false;
                PrimefacesContextUI.actualizar("formularioDialogos:conceptosDialogo");
                PrimefacesContextUI.ejecutar("PF('conceptosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listaVigenciasGruposConceptosCrear.contains(listaVigenciasGruposConceptos.get(indiceD))) {
                    if (listaVigenciasGruposConceptosModificar.isEmpty()) {
                        listaVigenciasGruposConceptosModificar.add(listaVigenciasGruposConceptos.get(indiceD));
                    } else if (!listaVigenciasGruposConceptosModificar.contains(listaVigenciasGruposConceptos.get(indiceD))) {
                        listaVigenciasGruposConceptosModificar.add(listaVigenciasGruposConceptos.get(indiceD));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaVigenciasGruposConceptosCrear.contains(filtradoListaVigenciasGruposConceptos.get(indiceD))) {

                    if (listaVigenciasGruposConceptosModificar.isEmpty()) {
                        listaVigenciasGruposConceptosModificar.add(filtradoListaVigenciasGruposConceptos.get(indiceD));
                    } else if (!listaVigenciasGruposConceptosModificar.contains(filtradoListaVigenciasGruposConceptos.get(indiceD))) {
                        listaVigenciasGruposConceptosModificar.add(filtradoListaVigenciasGruposConceptos.get(indiceD));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                }
                index = -1;
                secRegistro = null;
            }
        }
        PrimefacesContextUI.actualizar("form:datosVigenciasGruposConceptos");

    }

    public void gruposConceptosDialogo() {
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:gruposConceptosDialogo");
        PrimefacesContextUI.ejecutar("PF('gruposConceptosDialogo').show()");

    }

    public void asignarIndex(Integer indiceD, int dlg, int LND) {
        indexD = indiceD;
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
            indexD = -1;
            secRegistro = null;
            System.out.println("Tipo Actualizacion: " + tipoActualizacion);
        } else if (LND == 2) {
            indexD = -1;
            secRegistro = null;
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            PrimefacesContextUI.actualizar("formularioDialogos:conceptosDialogo");
            PrimefacesContextUI.ejecutar("PF('conceptosDialogo').show()");
        }
    }

    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            cualTabla = 0;
            tablaImprimir = ":formExportar:datosGruposConceptosExportar";
            nombreArchivo = "GruposConceptosXML";
            System.out.println("CualTabla = " + cualTabla);
            grupoConceptoSeleccionado = listaGruposConceptos.get(index);
            listaVigenciasGruposConceptos = null;
            getListaVigenciasGruposConceptos();
            cambiarVigencia();

            if (tipoLista == 0) {
                secRegistro = listaGruposConceptos.get(index).getSecuencia();

            } else {
                secRegistro = filtradoListaGruposConceptos.get(index).getSecuencia();
            }
        }
    }

    //UBICACION CELDA
    public void cambiarIndiceD(int indiceD, int celda) {
        if (permitirIndex == true) {
            indexD = indiceD;
            cualCeldaD = celda;
            cualTabla = 1;
            fechaInicial = listaVigenciasGruposConceptos.get(indexD).getFechainicial();
            fechaFinal = listaVigenciasGruposConceptos.get(indexD).getFechafinal();
            tablaImprimir = ":formExportar:datosVigenciasGruposConceptosExportar";
            nombreArchivo = "VigenciasGruposConceptosXML";
            System.out.println("CualTabla = " + cualTabla);
            vigenciasGruposConceptosSeleccionado = listaVigenciasGruposConceptos.get(indexD);
            if (tipoLista == 0) {
                secRegistro = listaVigenciasGruposConceptos.get(indexD).getSecuencia();
                codigo = listaVigenciasGruposConceptos.get(indexD).getConcepto().getCodigoSTR();
                empresa = listaVigenciasGruposConceptos.get(indexD).getConcepto().getEmpresa().getNombre();
            } else {
                secRegistro = filtradoListaVigenciasGruposConceptos.get(indexD).getSecuencia();
                codigo = filtradoListaVigenciasGruposConceptos.get(indexD).getConcepto().getCodigoSTR();
                empresa = filtradoListaVigenciasGruposConceptos.get(indexD).getConcepto().getEmpresa().getNombre();
            }
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        if (cualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposConceptosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "GruposConceptosPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            index = -1;
            secRegistro = null;
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasGruposConceptosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDFTablasAnchas();
            exporter.export(context, tabla, "VigenciasGruposConceptosPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexD = -1;
            secRegistro = null;
        }
    }

    public void exportXLS() throws IOException {
        if (cualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposConceptosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "GruposConceptosXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            index = -1;
            secRegistro = null;
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasGruposConceptosExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "VigenciasGruposConceptosXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexD = -1;
            secRegistro = null;
        }
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevoGruposConceptos() {
        nuevoGruposConceptos = new GruposConceptos();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO DETALLE EMBARGO
    public void limpiarNuevoVigenciaGruposConceptos() {
        nuevoVigenciasGruposConceptos = new VigenciasGruposConceptos();
        indexD = -1;
        secRegistro = null;
    }

    //LIMPIAR DUPLICAR
    public void limpiarduplicarGruposConceptos() {
        duplicarGruposConceptos = new GruposConceptos();
    }
    //LIMPIAR DUPLICAR NO FORMAL

    public void limpiarduplicarVigenciaGruposConceptos() {
        duplicarVigenciaGruposConceptos = new VigenciasGruposConceptos();
    }

    public void verificarRastro() {
        if (cualTabla == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("lol");
            if (!listaGruposConceptos.isEmpty()) {
                if (secRegistro != null) {
                    System.out.println("lol 2");
                    int resultado = administrarRastros.obtenerTabla(secRegistro, "GRUPOSCONCEPTOS");
                    System.out.println("resultado: " + resultado);
                    if (resultado == 1) {
                        PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
                    } else if (resultado == 2) {
                        PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
                    } else if (resultado == 3) {
                        PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                    } else if (resultado == 4) {
                        PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
                    } else if (resultado == 5) {
                        PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                    }
                } else {
                    PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
                }
            } else {
                if (administrarRastros.verificarHistoricosTabla("GRUPOSCONCEPTOS")) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
                } else {
                    PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
                }

            }
            index = -1;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("D");
            if (!listaVigenciasGruposConceptos.isEmpty()) {
                if (secRegistro != null) {
                    System.out.println("NF2");
                    int resultadoNF = administrarRastros.obtenerTabla(secRegistro, "VIGENCIASGRUPOSCONCEPTOS");
                    System.out.println("resultado: " + resultadoNF);
                    if (resultadoNF == 1) {
                        PrimefacesContextUI.ejecutar("PF('errorObjetosDBNF').show()");
                    } else if (resultadoNF == 2) {
                        PrimefacesContextUI.ejecutar("PF('confirmarRastroNF').show()");
                    } else if (resultadoNF == 3) {
                        PrimefacesContextUI.ejecutar("PF('errorRegistroRastroNF').show()");
                    } else if (resultadoNF == 4) {
                        PrimefacesContextUI.ejecutar("PF('errorTablaConRastroNF').show()");
                    } else if (resultadoNF == 5) {
                        PrimefacesContextUI.ejecutar("PF('errorTablaSinRastroNF').show()");
                    }
                } else {
                    PrimefacesContextUI.ejecutar("PF('seleccionarRegistroNF').show()");
                }
            } else {
                if (administrarRastros.verificarHistoricosTabla("VIGENCIASGRUPOSCONCEPTOS")) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastroHistoricoNF').show()");
                } else {
                    PrimefacesContextUI.ejecutar("PF('errorRastroHistoricoNF').show()");
                }

            }
            indexD = -1;
        }

    }

    public void recibirPagina(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }
    
    public String volverPaginaAnterior() {
        return paginaAnterior;
    }
    //Getter & Setter

    public List<GruposConceptos> getListaGruposConceptos() {
        if (listaGruposConceptos == null) {
            listaGruposConceptos = administrarGruposConceptos.buscarGruposConceptos();
            if (!listaGruposConceptos.isEmpty()) {
                grupoConceptoSeleccionado = listaGruposConceptos.get(0);
                secuenciaGrupoConcepto = grupoConceptoSeleccionado.getSecuencia();
            }
        }
        return listaGruposConceptos;
    }

    public void setListaGruposConceptos(List<GruposConceptos> listaGruposConceptos) {
        this.listaGruposConceptos = listaGruposConceptos;
    }

    public List<GruposConceptos> getFiltradoListaGruposConceptos() {
        return filtradoListaGruposConceptos;
    }

    public void setFiltradoListaGruposConceptos(List<GruposConceptos> filtradoListaGruposConceptos) {
        this.filtradoListaGruposConceptos = filtradoListaGruposConceptos;
    }

    public GruposConceptos getGrupoConceptoSeleccionado() {
        return grupoConceptoSeleccionado;
    }

    public void setGrupoConceptoSeleccionado(GruposConceptos grupoConceptoSeleccionado) {
        this.grupoConceptoSeleccionado = grupoConceptoSeleccionado;
    }

    public int getRegistroActual() {
        return registroActual;
    }

    public void setRegistroActual(int registroActual) {
        this.registroActual = registroActual;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<VigenciasGruposConceptos> getListaVigenciasGruposConceptos() {
        if (listaVigenciasGruposConceptos == null && grupoConceptoSeleccionado != null) {
            listaVigenciasGruposConceptos = administrarGruposConceptos.buscarVigenciasGruposConceptos(secuenciaGrupoConcepto);
        }
        return listaVigenciasGruposConceptos;
    }

    public void setListaVigenciasGruposConceptos(List<VigenciasGruposConceptos> listaVigenciasGruposConceptos) {
        this.listaVigenciasGruposConceptos = listaVigenciasGruposConceptos;
    }

    public List<VigenciasGruposConceptos> getFiltradoListaVigenciasGruposConceptos() {
        return filtradoListaVigenciasGruposConceptos;
    }

    public void setFiltradoListaVigenciasGruposConceptos(List<VigenciasGruposConceptos> filtradoListaVigenciasGruposConceptos) {
        this.filtradoListaVigenciasGruposConceptos = filtradoListaVigenciasGruposConceptos;
    }

    public VigenciasGruposConceptos getVigenciasGruposConceptosSeleccionado() {
        return vigenciasGruposConceptosSeleccionado;
    }

    public void setVigenciasGruposConceptosSeleccionado(VigenciasGruposConceptos vigenciasGruposConceptosSeleccionado) {
        this.vigenciasGruposConceptosSeleccionado = vigenciasGruposConceptosSeleccionado;
    }

    public String getAltoScrollGruposConceptos() {
        return altoScrollGruposConceptos;
    }

    public void setAltoScrollGruposConceptos(String altoScrollGruposConceptos) {
        this.altoScrollGruposConceptos = altoScrollGruposConceptos;
    }

    public String getAltoScrollVigenciasGruposConceptos() {
        return altoScrollVigenciasGruposConceptos;
    }

    public void setAltoScrollVigenciasGruposConceptos(String altoScrollVigenciasGruposConceptos) {
        this.altoScrollVigenciasGruposConceptos = altoScrollVigenciasGruposConceptos;
    }

    public GruposConceptos getEditarGruposConceptos() {
        return editarGruposConceptos;
    }

    public void setEditarGruposConceptos(GruposConceptos editarGruposConceptos) {
        this.editarGruposConceptos = editarGruposConceptos;
    }

    public VigenciasGruposConceptos getEditarVigenciasGruposConceptos() {
        return editarVigenciasGruposConceptos;
    }

    public void setEditarVigenciasGruposConceptos(VigenciasGruposConceptos editarVigenciasGruposConceptos) {
        this.editarVigenciasGruposConceptos = editarVigenciasGruposConceptos;
    }

    public String getTablaImprimir() {
        return tablaImprimir;
    }

    public void setTablaImprimir(String tablaImprimir) {
        this.tablaImprimir = tablaImprimir;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public GruposConceptos getNuevoGruposConceptos() {
        return nuevoGruposConceptos;
    }

    public void setNuevoGruposConceptos(GruposConceptos nuevoGruposConceptos) {
        this.nuevoGruposConceptos = nuevoGruposConceptos;
    }

    public VigenciasGruposConceptos getNuevoVigenciaGruposConceptos() {
        return nuevoVigenciasGruposConceptos;
    }

    public void setNuevoVigenciaGruposConceptos(VigenciasGruposConceptos nuevoVigenciaGruposConceptos) {
        this.nuevoVigenciasGruposConceptos = nuevoVigenciaGruposConceptos;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public GruposConceptos getDuplicarGruposConceptos() {
        return duplicarGruposConceptos;
    }

    public void setDuplicarGruposConceptos(GruposConceptos duplicarGruposConceptos) {
        this.duplicarGruposConceptos = duplicarGruposConceptos;
    }

    public VigenciasGruposConceptos getDuplicarVigenciaGruposConceptos() {
        return duplicarVigenciaGruposConceptos;
    }

    public void setDuplicarVigenciaGruposConceptos(VigenciasGruposConceptos duplicarVigenciaGruposConceptos) {
        this.duplicarVigenciaGruposConceptos = duplicarVigenciaGruposConceptos;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public boolean isCambiosPagina() {
        return cambiosPagina;
    }

    public void setCambiosPagina(boolean cambiosPagina) {
        this.cambiosPagina = cambiosPagina;
    }

    public List<Conceptos> getLovlistaConceptos() {
        if (lovlistaConceptos == null) {
            lovlistaConceptos = administrarGruposConceptos.lovConceptos();
        }

        return lovlistaConceptos;
    }

    public void setLovlistaConceptos(List<Conceptos> lovlistaConceptos) {
        this.lovlistaConceptos = lovlistaConceptos;
    }

    public List<Conceptos> getLovfiltradoListaConceptos() {
        return lovfiltradoListaConceptos;
    }

    public void setLovfiltradoListaConceptos(List<Conceptos> lovfiltradoListaConceptos) {
        this.lovfiltradoListaConceptos = lovfiltradoListaConceptos;
    }

    public Conceptos getConceptoSeleccionado() {
        return conceptoSeleccionado;
    }

    public void setConceptoSeleccionado(Conceptos conceptoSeleccionado) {
        this.conceptoSeleccionado = conceptoSeleccionado;
    }

    public List<GruposConceptos> getLovlistaGruposConceptos() {
        if (lovlistaGruposConceptos == null) {
            lovlistaGruposConceptos = administrarGruposConceptos.buscarGruposConceptos();
        }
        return lovlistaGruposConceptos;
    }

    public void setLovlistaGruposConceptos(List<GruposConceptos> lovlistaGruposConceptos) {
        this.lovlistaGruposConceptos = lovlistaGruposConceptos;
    }

    public List<GruposConceptos> getLovfiltradoListaGruposConceptos() {
        return lovfiltradoListaGruposConceptos;
    }

    public void setLovfiltradoListaGruposConceptos(List<GruposConceptos> lovfiltradoListaGruposConceptos) {
        this.lovfiltradoListaGruposConceptos = lovfiltradoListaGruposConceptos;
    }

    public GruposConceptos getGruposSeleccionado() {
        return gruposSeleccionado;
    }

    public void setGruposSeleccionado(GruposConceptos gruposSeleccionado) {
        this.gruposSeleccionado = gruposSeleccionado;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }
}
