/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.VigenciasRetenciones;
import Entidades.Retenciones;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarRetencionesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlRetencion implements Serializable {

    @EJB
    AdministrarRetencionesInterface administrarRetenciones;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //Lista Vigencias Retenciones (Arriba)
    private List<VigenciasRetenciones> listaVigenciasRetenciones;
    private List<VigenciasRetenciones> filtradoListaVigenciasRetenciones;
    private VigenciasRetenciones vigenciaRetencionSeleccionado;
    //Lista Retenciones
    private List<Retenciones> listaRetenciones;
    private List<Retenciones> filtradoListaRetenciones;
    private Retenciones retencionSeleccionado;
    //REGISTRO ACTUAL
    private int registroActual, index, tablaActual, indexD;
    //OTROS
    private boolean aceptar, mostrarTodos;
    private String altoScrollVigenciasRetenciones, altoScrollRetenciones;
    //Crear Vigencias Retenciones (Arriba)
    private List<VigenciasRetenciones> listaVigenciasRetencionesCrear;
    public VigenciasRetenciones nuevoVigenciasRetenciones;
    public VigenciasRetenciones duplicarVigenciasRetenciones;
    private int k;
    private BigInteger l;
    private String mensajeValidacion;
    //Modificar Vigencias Retenciones
    private List<VigenciasRetenciones> listaVigenciasRetencionesModificar;
    //Borrar Vigencias Retenciones
    private List<VigenciasRetenciones> listaVigenciasRetencionesBorrar;
    //Crear Retenciones (Abajo)
    private List<Retenciones> listaRetencionesCrear;
    public Retenciones nuevoRetencion;
    public Retenciones duplicarRetencion;
    //Modificar Retenciones
    private List<Retenciones> listaRetencionesModificar;
    //Borrar Retenciones
    private List<Retenciones> listaRetencionesBorrar;
    //OTROS
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //editar celda
    private VigenciasRetenciones editarVigenciasRetenciones;
    private Retenciones editarRetenciones;
    private boolean cambioEditor, aceptarEditar;
    private int cualCelda, tipoLista;
    private int cualCeldaD;
    private int tipoListaD;
    //RASTROS
    private BigInteger secRegistro;
    private BigInteger secRegistroD;
    private boolean guardado, guardarOk;
    private boolean cambiosPagina;
    //FILTRADO
    private Column vCodigo, vFechaVigencia, vUvt;
    private Column rValorMinimo, rValorMaximo, rPorcentaje, rValor, rAdicionarUvt;
    //Tabla a Imprimir
    private String tablaImprimir, nombreArchivo;
    //Sec Abajo Duplicar
    private int m;
    private BigInteger n;
    //SECUENCIA DE VIGENCIA
    private BigInteger secuenciaVigenciaRetencion;
    private Date fechaParametro;
    private Date fechaVigencia;

    private Integer cualTabla;

    public ControlRetencion() {
        permitirIndex = true;
        cualTabla = 0;
        bandera = 0;
        registroActual = 0;
        mostrarTodos = true;
        altoScrollVigenciasRetenciones = "90";
        altoScrollRetenciones = "90";
        listaVigenciasRetencionesBorrar = new ArrayList<VigenciasRetenciones>();
        listaVigenciasRetencionesCrear = new ArrayList<VigenciasRetenciones>();
        listaVigenciasRetencionesModificar = new ArrayList<VigenciasRetenciones>();
        listaRetencionesBorrar = new ArrayList<Retenciones>();
        listaRetencionesCrear = new ArrayList<Retenciones>();
        listaRetencionesModificar = new ArrayList<Retenciones>();
        tablaImprimir = ":formExportar:datosVigenciasRetencionesExportar";
        nombreArchivo = "VigenciasRetencionesXML";
        //Crear Vigencia Retenciones
        nuevoVigenciasRetenciones = new VigenciasRetenciones();
        //Crear Retenciones
        nuevoRetencion = new Retenciones();
        m = 0;
        cambiosPagina = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarRetenciones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct "+ this.getClass().getName() +": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
    
    //CREAR Vigencia Retencion
    public void agregarNuevoVigencia() {
        int pasa = 0;
        int pasar = 0;
        
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();
        
        

        if (nuevoVigenciasRetenciones.getCodigo() == null) {
            mensajeValidacion = mensajeValidacion + " * Codigo\n";
            pasa++;
        }
        if (nuevoVigenciasRetenciones.getFechavigencia() == null) {
            mensajeValidacion = mensajeValidacion + " * Fecha Vigencia\n";
            pasa++;
        }

        if (nuevoVigenciasRetenciones.getUvt() == null) {
            mensajeValidacion = mensajeValidacion + " * UVT\n";
            pasa++;
        }
        
        for (int i = 0; i < listaVigenciasRetenciones.size(); i++) {
            if (nuevoVigenciasRetenciones.getCodigo() == listaVigenciasRetenciones.get(i).getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:codigos");
                RequestContext.getCurrentInstance().execute("PF('codigos').show()");
                pasar++;
            }
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
        }

        if (pasa == 0 && pasar == 0) {
            if (bandera == 1) {
                vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
                vCodigo.setFilterStyle("display: none; visibility: hidden;");
                vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
                vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                vUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vUvt");
                vUvt.setFilterStyle("display: none; visibility: hidden;");
                altoScrollVigenciasRetenciones = "90";
                RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
                bandera = 0;
                filtradoListaVigenciasRetenciones = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoVigenciasRetenciones.setSecuencia(l);
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaVigenciasRetencionesCrear.add(nuevoVigenciasRetenciones);
            listaVigenciasRetenciones.add(nuevoVigenciasRetenciones);
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:aceptar");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasRetenciones').hide()");
            nuevoVigenciasRetenciones = new VigenciasRetenciones();
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciasRetenciones");
            index = -1;
            secRegistro = null;
        }
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
        if (listaVigenciasRetencionesCrear.isEmpty() && listaVigenciasRetencionesBorrar.isEmpty() && listaVigenciasRetencionesModificar.isEmpty()) {
            secuenciaVigenciaRetencion = vigenciaRetencionSeleccionado.getSecuencia();
            listaRetenciones = null;
            getListaRetenciones();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            //}
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
            RequestContext.getCurrentInstance().execute("PF('cambiar').show()");

        }
    }

    public void limpiarListas() {
        listaVigenciasRetencionesCrear.clear();
        listaVigenciasRetencionesBorrar.clear();
        listaVigenciasRetencionesModificar.clear();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");

    }

    //BORRAR RETENCION / TABLA RETENCION
    public void borrar() {

        if (index >= 0 && cualTabla == 0) {
            if (listaRetenciones.isEmpty()) {
                if (tipoLista == 0) {
                    if (!listaVigenciasRetencionesModificar.isEmpty() && listaVigenciasRetencionesModificar.contains(listaVigenciasRetenciones.get(index))) {
                        int modIndex = listaVigenciasRetencionesModificar.indexOf(listaVigenciasRetenciones.get(index));
                        listaVigenciasRetencionesModificar.remove(modIndex);
                        listaVigenciasRetencionesBorrar.add(listaVigenciasRetenciones.get(index));
                    } else if (!listaVigenciasRetencionesCrear.isEmpty() && listaVigenciasRetencionesCrear.contains(listaVigenciasRetenciones.get(index))) {
                        int crearIndex = listaVigenciasRetencionesCrear.indexOf(listaVigenciasRetenciones.get(index));
                        listaVigenciasRetencionesCrear.remove(crearIndex);
                    } else {
                        listaVigenciasRetencionesBorrar.add(listaVigenciasRetenciones.get(index));
                    }
                    listaVigenciasRetenciones.remove(index);
                }

                if (tipoLista == 1) {
                    if (!listaVigenciasRetencionesModificar.isEmpty() && listaVigenciasRetencionesModificar.contains(filtradoListaVigenciasRetenciones.get(index))) {
                        int modIndex = listaVigenciasRetencionesModificar.indexOf(filtradoListaVigenciasRetenciones.get(index));
                        listaVigenciasRetencionesModificar.remove(modIndex);
                        listaVigenciasRetencionesBorrar.add(filtradoListaVigenciasRetenciones.get(index));
                    } else if (!listaVigenciasRetencionesCrear.isEmpty() && listaVigenciasRetencionesCrear.contains(filtradoListaVigenciasRetenciones.get(index))) {
                        int crearIndex = listaVigenciasRetencionesCrear.indexOf(filtradoListaVigenciasRetenciones.get(index));
                        listaVigenciasRetencionesCrear.remove(crearIndex);
                    } else {
                        listaVigenciasRetencionesBorrar.add(filtradoListaVigenciasRetenciones.get(index));
                    }
                    int CIndex = listaVigenciasRetenciones.indexOf(filtradoListaVigenciasRetenciones.get(index));
                    listaVigenciasRetenciones.remove(CIndex);
                    filtradoListaVigenciasRetenciones.remove(index);
                    System.out.println("Realizado");
                }

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
                cambiosPagina = false;
                index = -1;
                secRegistro = null;

                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:aceptar");
                }
            } else {
                System.out.println("No se puede borrar porque tiene registros en la tabla de abajo");
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formularioDialogos:registro");
                RequestContext.getCurrentInstance().execute("PF('registro').show()");
            }
        } else if (indexD >= 0 && cualTabla == 1) {

            if (tipoListaD == 0) {
                if (!listaRetencionesModificar.isEmpty() && listaRetencionesModificar.contains(listaRetenciones.get(indexD))) {
                    int modIndex = listaRetencionesModificar.indexOf(listaRetenciones.get(indexD));
                    listaRetencionesModificar.remove(modIndex);
                    listaRetencionesBorrar.add(listaRetenciones.get(indexD));
                } else if (!listaRetencionesCrear.isEmpty() && listaRetencionesCrear.contains(listaRetenciones.get(indexD))) {
                    int crearIndex = listaRetencionesCrear.indexOf(listaRetenciones.get(indexD));
                    listaRetencionesCrear.remove(crearIndex);
                } else {
                    listaRetencionesBorrar.add(listaRetenciones.get(indexD));
                }
                listaRetenciones.remove(indexD);
            }

            if (tipoListaD == 1) {
                if (!listaRetencionesModificar.isEmpty() && listaRetencionesModificar.contains(filtradoListaRetenciones.get(indexD))) {
                    int modIndex = listaRetencionesModificar.indexOf(filtradoListaRetenciones.get(indexD));
                    listaRetencionesModificar.remove(modIndex);
                    listaRetencionesBorrar.add(filtradoListaRetenciones.get(indexD));
                } else if (!listaRetencionesCrear.isEmpty() && listaRetencionesCrear.contains(filtradoListaRetenciones.get(indexD))) {
                    int crearIndex = listaRetencionesCrear.indexOf(filtradoListaRetenciones.get(indexD));
                    listaRetencionesCrear.remove(crearIndex);
                } else {
                    listaRetencionesBorrar.add(filtradoListaRetenciones.get(indexD));
                }
                int CIndex = listaRetenciones.indexOf(filtradoListaRetenciones.get(indexD));
                listaRetenciones.remove(CIndex);
                filtradoListaRetenciones.remove(indexD);
                System.out.println("Realizado");
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            indexD = -1;
            secRegistro = null;
            cambiosPagina = false;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:aceptar");
            }
        }
    }

    public void guardarTodo() {
        if (guardado == false) {
            System.out.println("Realizando Operaciones retenciones");
            if (!listaVigenciasRetencionesBorrar.isEmpty()) {
                for (int i = 0; i < listaVigenciasRetencionesBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    if (listaVigenciasRetencionesBorrar.get(i).getUvt() == null) {
                        listaVigenciasRetencionesBorrar.get(i).setUvt(null);
                    }

                    administrarRetenciones.borrarVigenciaRetencion(listaVigenciasRetencionesBorrar.get(i));
                    System.out.println("Entra");
                    listaVigenciasRetencionesBorrar.clear();
                }
            }
            if (!listaVigenciasRetencionesCrear.isEmpty()) {
                for (int i = 0; i < listaVigenciasRetencionesCrear.size(); i++) {
                    System.out.println("Creando...");
                    System.out.println(listaVigenciasRetencionesCrear.size());
                    if (listaVigenciasRetencionesCrear.get(i).getUvt() == null) {
                        listaVigenciasRetencionesCrear.get(i).setUvt(null);
                    }
                    administrarRetenciones.crearVigenciaRetencion(listaVigenciasRetencionesCrear.get(i));
                }

                System.out.println("LimpiaLista");
                listaVigenciasRetencionesCrear.clear();
            }
            if (!listaVigenciasRetencionesModificar.isEmpty()) {
                administrarRetenciones.modificarVigenciaRetencion(listaVigenciasRetencionesModificar);
                listaVigenciasRetencionesModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaVigenciasRetenciones = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            guardado = true;
            permitirIndex = true;
            cambiosPagina = true;
            RequestContext.getCurrentInstance().update("form:aceptar");
            //  k = 0;
        }
        System.out.println("Valor k: " + k);
        index = -1;
        secRegistro = null;

        if (guardado == false) {
            System.out.println("Realizando Operaciones VigenciasNoFormales");
            if (!listaRetencionesBorrar.isEmpty()) {
                for (int i = 0; i < listaRetencionesBorrar.size(); i++) {
                    System.out.println("Borrando...");
                    if (listaRetencionesBorrar.get(i).getAdicionauvt() == null) {
                        listaRetencionesBorrar.get(i).setAdicionauvt(null);
                    }
                    administrarRetenciones.borrarRetencion(listaRetencionesBorrar.get(i));
                }

                System.out.println("Entra");
                listaRetencionesBorrar.clear();
            }
        }
        if (!listaRetencionesCrear.isEmpty()) {
            for (int i = 0; i < listaRetencionesCrear.size(); i++) {
                System.out.println("Creando...");
                System.out.println(listaRetencionesCrear.size());
                if (listaRetencionesCrear.get(i).getAdicionauvt() == null) {
                    listaRetencionesCrear.get(i).setAdicionauvt(null);
                }

                administrarRetenciones.crearRetencion(listaRetencionesCrear.get(i));

            }

            System.out.println("LimpiaLista");
            listaRetencionesCrear.clear();
        }
        if (!listaRetencionesModificar.isEmpty()) {
            administrarRetenciones.modificarRetencion(listaRetencionesModificar);

            listaRetencionesModificar.clear();
        }

        System.out.println("Se guardaron los datos con exito");
        listaRetenciones = null;
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = new FacesMessage("Información", "Se han guardado los datos exitosamente.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        RequestContext.getCurrentInstance().update("form:datosRetenciones");
        RequestContext.getCurrentInstance().update("form:growl");
        guardado = true;
        permitirIndex = true;
        RequestContext.getCurrentInstance().update("form:aceptar");
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
            //Tabla Vigencias Retenciones
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
            vCodigo.setFilterStyle("width: 85% !important;");
            vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
            vFechaVigencia.setFilterStyle("width: 85% !important;");
            vUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vUvt");
            vUvt.setFilterStyle("width: 85% !important;");
            altoScrollVigenciasRetenciones = "70";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            bandera = 1;

        } else if (bandera == 1 && cualTabla == 0) {
            System.out.println("Desactiva 1");
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
            vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            vUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vUvt");
            vUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasRetenciones = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            bandera = 0;
            filtradoListaVigenciasRetenciones = null;

        } else if (bandera == 0 && cualTabla == 1) {
            System.out.println("Activa 2");
            rValorMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMinimo");
            rValorMinimo.setFilterStyle("width: 85% !important;");
            rValorMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMaximo");
            rValorMaximo.setFilterStyle("width: 85% !important;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("width: 85% !important;");
            rValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValor");
            rValor.setFilterStyle("");
            rAdicionarUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rAdicionarUvt");
            rAdicionarUvt.setFilterStyle("");
            altoScrollRetenciones = "66";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 1;
            tipoListaD = 1;

        } else if (bandera == 1 && cualTabla == 1) {
            //SOLUCIONES NODOS EMPLEADO
            System.out.println("Desactiva 2");
            rValorMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMinimo");
            rValorMinimo.setFilterStyle("display: none; visibility: hidden;");
            rValorMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMaximo");
            rValorMaximo.setFilterStyle("display: none; visibility: hidden;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            rValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValor");
            rValor.setFilterStyle("display: none; visibility: hidden;");
            rAdicionarUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rAdicionarUvt");
            rAdicionarUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollRetenciones = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 0;
            tipoListaD = 0;
            filtradoListaRetenciones = null;
        }
    }

    public void salir() {
        if (bandera == 1) {
            System.out.println("Desactiva 1");
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
            vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            vUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vUvt");
            vUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasRetenciones = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            bandera = 0;
            filtradoListaVigenciasRetenciones = null;
            altoScrollVigenciasRetenciones = "90";

            bandera = 0;
            filtradoListaVigenciasRetenciones = null;
            tipoLista = 0;
        }

        if (bandera == 1) {
            //SOLUCIONES NODOS EMPLEADO
            System.out.println("Desactiva 2");
            rValorMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMinimo");
            rValorMinimo.setFilterStyle("display: none; visibility: hidden;");
            rValorMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMaximo");
            rValorMaximo.setFilterStyle("display: none; visibility: hidden;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            rValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValor");
            rValor.setFilterStyle("display: none; visibility: hidden;");
            rAdicionarUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rAdicionarUvt");
            rAdicionarUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollRetenciones = "90";
            bandera = 0;
            tipoListaD = 0;
            filtradoListaRetenciones = null;

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 0;
            filtradoListaRetenciones = null;
            tipoListaD = 0;
        }
        listaVigenciasRetencionesBorrar.clear();
        listaVigenciasRetencionesCrear.clear();
        listaVigenciasRetencionesModificar.clear();
        index = -1;
        secRegistro = null;

        listaVigenciasRetenciones = null;

        listaRetencionesBorrar.clear();
        listaRetencionesCrear.clear();
        listaRetencionesModificar.clear();
        indexD = -1;

        listaRetenciones = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
        RequestContext.getCurrentInstance().update("form:datosRetenciones");

    }

    //CANCELAR MODIFICACIONES
    public void cancelarModificacion() {
        if (bandera == 1) {
            System.out.println("Desactiva 1");
            vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
            vCodigo.setFilterStyle("display: none; visibility: hidden;");
            vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
            vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
            vUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vUvt");
            vUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollVigenciasRetenciones = "90";
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            bandera = 0;
            filtradoListaVigenciasRetenciones = null;
            altoScrollVigenciasRetenciones = "90";

            bandera = 0;
            filtradoListaVigenciasRetenciones = null;
            tipoLista = 0;
        }

        if (bandera == 1) {
            //SOLUCIONES NODOS EMPLEADO
            System.out.println("Desactiva 2");
            rValorMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMinimo");
            rValorMinimo.setFilterStyle("display: none; visibility: hidden;");
            rValorMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMaximo");
            rValorMaximo.setFilterStyle("display: none; visibility: hidden;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            rValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValor");
            rValor.setFilterStyle("display: none; visibility: hidden;");
            rAdicionarUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rAdicionarUvt");
            rAdicionarUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollRetenciones = "90";
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetencionesDetalles");
            bandera = 0;
            filtradoListaRetenciones = null;
            tipoListaD = 0;
        }
        listaVigenciasRetencionesBorrar.clear();
        listaVigenciasRetencionesCrear.clear();
        listaVigenciasRetencionesModificar.clear();
        index = -1;
        secRegistro = null;

        listaVigenciasRetenciones = null;

        listaRetencionesBorrar.clear();
        listaRetencionesCrear.clear();
        listaRetencionesModificar.clear();
        indexD = -1;

        listaRetenciones = null;
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
        RequestContext.getCurrentInstance().update("form:datosVigenciasRetencionesDetalles");
    }

    //GUARDAR
    public void guardarCambiosRetenciones() {
        if (cualTabla == 0) {
            System.out.println("Guardado: " + guardado);
            if (guardado == false) {
                System.out.println("Realizando Operaciones Retenciones");
                if (!listaVigenciasRetencionesBorrar.isEmpty()) {
                    for (int i = 0; i < listaVigenciasRetencionesBorrar.size(); i++) {
                        System.out.println("Borrando...");
                        if (listaVigenciasRetencionesBorrar.get(i).getUvt() == null) {
                            listaVigenciasRetencionesBorrar.get(i).setUvt(null);
                        }
                        administrarRetenciones.borrarVigenciaRetencion(listaVigenciasRetencionesBorrar.get(i));

                        System.out.println("Entra");
                        listaVigenciasRetencionesBorrar.clear();
                    }
                }
                if (!listaVigenciasRetencionesCrear.isEmpty()) {
                    for (int i = 0; i < listaVigenciasRetencionesCrear.size(); i++) {
                        System.out.println("Creando...");
                        System.out.println(listaVigenciasRetencionesCrear.size());
                        if (listaVigenciasRetencionesCrear.get(i).getUvt() == null) {
                            listaVigenciasRetencionesCrear.get(i).setUvt(null);
                        }
                        administrarRetenciones.crearVigenciaRetencion(listaVigenciasRetencionesCrear.get(i));
                    }

                    System.out.println("LimpiaLista");
                    listaVigenciasRetencionesCrear.clear();
                }
                if (!listaVigenciasRetencionesModificar.isEmpty()) {
                    administrarRetenciones.modificarVigenciaRetencion(listaVigenciasRetencionesModificar);
                    listaVigenciasRetencionesModificar.clear();
                }

                System.out.println("Se guardaron los datos con exito");
                listaVigenciasRetenciones = null;
                RequestContext context = RequestContext.getCurrentInstance();
                FacesMessage msg = new FacesMessage("Información", "Se han guardado los datos exitosamente.");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
                guardado = true;
                permitirIndex = true;
                RequestContext.getCurrentInstance().update("form:aceptar");
                //  k = 0;
            }
            System.out.println("Tamaño lista: " + listaVigenciasRetencionesCrear.size());
            System.out.println("Valor k: " + k);
            index = -1;
            secRegistro = null;

        } else {

            System.out.println("Está en la Tabla de Abajo");

            if (guardado == false) {
                System.out.println("Realizando Operaciones VigenciasNoFormales");
                if (!listaRetencionesBorrar.isEmpty()) {
                    for (int i = 0; i < listaRetencionesBorrar.size(); i++) {
                        System.out.println("Borrando...");
                        if (listaRetencionesBorrar.get(i).getAdicionauvt() == null) {
                            listaRetencionesBorrar.get(i).setAdicionauvt(null);
                        }
                        administrarRetenciones.borrarRetencion(listaRetencionesBorrar.get(i));
                    }

                    System.out.println("Entra");
                    listaRetencionesBorrar.clear();
                }
            }
            if (!listaRetencionesCrear.isEmpty()) {
                for (int i = 0; i < listaRetencionesCrear.size(); i++) {
                    System.out.println("Creando...");
                    System.out.println(listaRetencionesCrear.size());
                    if (listaRetencionesCrear.get(i).getAdicionauvt() == null) {
                        listaRetencionesCrear.get(i).setAdicionauvt(null);
                    }
                    administrarRetenciones.crearRetencion(listaRetencionesCrear.get(i));

                }

                System.out.println("LimpiaLista");
                listaRetencionesCrear.clear();
            }
            if (!listaRetencionesModificar.isEmpty()) {
                administrarRetenciones.modificarRetencion(listaRetencionesModificar);

                listaRetencionesModificar.clear();
            }

            System.out.println("Se guardaron los datos con exito");
            listaRetenciones = null;
            RequestContext context = RequestContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage("Información", "Se han guardado los datos exitosamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            RequestContext.getCurrentInstance().update("form:growl");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:aceptar");
            //  k = 0;
        }
        System.out.println("Valor k: " + k);
        indexD = -1;
        secRegistro = null;

    }

//CREAR Retencion
    public void agregarNuevoRetencion() {
        int pasa = 0;
        int pasar = 0;
        mensajeValidacion = new String();

        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevoRetencion.getValorminimo() == null) {
            mensajeValidacion = mensajeValidacion + " * Valor Minimo \n";
            pasa++;
        }

        if (nuevoRetencion.getValormaximo() == null) {
            mensajeValidacion = mensajeValidacion + " * Valor Maximo \n";
            pasa++;
        }

        if (nuevoRetencion.getPorcentaje() == null) {
            mensajeValidacion = mensajeValidacion + " * Porcentaje \n";
            pasa++;
        }

        if (nuevoRetencion.getValor() == null) {
            mensajeValidacion = mensajeValidacion + " * Valor\n";
            pasa++;
        }

        if (nuevoRetencion.getValorminimo() != null && nuevoRetencion.getValormaximo() != null) {
            if (nuevoRetencion.getValorminimo().compareTo(nuevoRetencion.getValormaximo()) == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:valores");
                RequestContext.getCurrentInstance().execute("PF('valores').show()");
                pasar++;
            }
        }

        if (nuevoRetencion.getValorminimo() != null && nuevoRetencion.getValormaximo() != null) {
            if (nuevoRetencion.getValorminimo().compareTo(nuevoRetencion.getValormaximo()) == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:valores2");
                RequestContext.getCurrentInstance().execute("PF('valores2').show()");
                pasar++;
            }
        }

        if (nuevoRetencion.getValor() != null) {
            if (nuevoRetencion.getValor().compareTo(BigDecimal.ZERO) == -1) {
                System.out.println("Valor es menor a 0");
                RequestContext.getCurrentInstance().update("formularioDialogos:valores3");
                RequestContext.getCurrentInstance().execute("PF('valores3').show()");
                pasar++;
            }
        }

        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevo");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevo').show()");
        }

        if (pasa == 0 && pasar == 0) {
            if (bandera == 1) {
                //SOLUCIONES NODOS EMPLEADO
                System.out.println("Desactiva 2");
                rValorMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMinimo");
                rValorMinimo.setFilterStyle("display: none; visibility: hidden;");
                rValorMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMaximo");
                rValorMaximo.setFilterStyle("display: none; visibility: hidden;");
                rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
                rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
                rValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValor");
                rValor.setFilterStyle("display: none; visibility: hidden;");
                rAdicionarUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rAdicionarUvt");
                rAdicionarUvt.setFilterStyle("display: none; visibility: hidden;");
                altoScrollRetenciones = "90";
                RequestContext.getCurrentInstance().update("form:datosRetenciones");
                bandera = 0;
                filtradoListaRetenciones = null;
                tipoListaD = 0;
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            k++;
            l = BigInteger.valueOf(k);
            nuevoRetencion.setSecuencia(l);
            System.out.println("vigenciaRetencionSeleccionado" + vigenciaRetencionSeleccionado.getCodigo());
            nuevoRetencion.setVigencia(vigenciaRetencionSeleccionado);
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            listaRetencionesCrear.add(nuevoRetencion);
            listaRetenciones.add(nuevoRetencion);

            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:aceptar");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroRetenciones').hide()");
            nuevoRetencion = new Retenciones();
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroRetenciones");
            index = -1;
            secRegistro = null;
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        
        for (int i = 0; i < listaVigenciasRetenciones.size(); i++) {
            if (duplicarVigenciasRetenciones.getCodigo() == listaVigenciasRetenciones.get(i).getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:valores");
                RequestContext.getCurrentInstance().execute("PF('valores').show()");
                pasa++;
            }
        }

        if (pasa == 0) {
            listaVigenciasRetenciones.add(duplicarVigenciasRetenciones);
            listaVigenciasRetencionesCrear.add(duplicarVigenciasRetenciones);
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            index = -1;
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:aceptar");
            }
            if (bandera == 1) {
                vCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vCodigo");
                vCodigo.setFilterStyle("display: none; visibility: hidden;");
                vFechaVigencia = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vFechaVigencia");
                vFechaVigencia.setFilterStyle("display: none; visibility: hidden;");
                vUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosVigenciasRetenciones:vUvt");
                vUvt.setFilterStyle("display: none; visibility: hidden;");
                altoScrollVigenciasRetenciones = "90";
                RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
                bandera = 0;
                filtradoListaVigenciasRetenciones = null;
                tipoLista = 0;
            }
        }
        duplicarVigenciasRetenciones = new VigenciasRetenciones();
    }

    public void confirmarDuplicarD() {

        listaRetenciones.add(duplicarRetencion);
        listaRetencionesCrear.add(duplicarRetencion);
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosRetenciones");
        index = -1;
        secRegistro = null;
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:aceptar");
        }
        if (bandera == 1) {
            rValorMinimo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMinimo");
            rValorMinimo.setFilterStyle("display: none; visibility: hidden;");
            rValorMaximo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValorMaximo");
            rValorMaximo.setFilterStyle("display: none; visibility: hidden;");
            rPorcentaje = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rPorcentaje");
            rPorcentaje.setFilterStyle("display: none; visibility: hidden;");
            rValor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rValor");
            rValor.setFilterStyle("display: none; visibility: hidden;");
            rAdicionarUvt = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosRetenciones:rAdicionarUvt");
            rAdicionarUvt.setFilterStyle("display: none; visibility: hidden;");
            altoScrollRetenciones = "90";
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
            bandera = 0;
            filtradoListaRetenciones = null;
            tipoListaD = 0;
        }

        duplicarRetencion = new Retenciones();
    }

    //DUPLICAR VIGENCIAS RETENCIONES/RETENCIONES
    public void duplicarE() {
        if (index >= 0 && cualTabla == 0) {
            duplicarVigenciasRetenciones = new VigenciasRetenciones();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarVigenciasRetenciones.setSecuencia(l);
                duplicarVigenciasRetenciones.setCodigo(listaVigenciasRetenciones.get(index).getCodigo());
                duplicarVigenciasRetenciones.setFechavigencia(listaVigenciasRetenciones.get(index).getFechavigencia());
                duplicarVigenciasRetenciones.setUvt(listaVigenciasRetenciones.get(index).getUvt());
            }
            if (tipoLista == 1) {
                duplicarVigenciasRetenciones.setSecuencia(l);
                duplicarVigenciasRetenciones.setCodigo(filtradoListaVigenciasRetenciones.get(index).getCodigo());
                duplicarVigenciasRetenciones.setFechavigencia(filtradoListaVigenciasRetenciones.get(index).getFechavigencia());
                duplicarVigenciasRetenciones.setUvt(filtradoListaVigenciasRetenciones.get(index).getUvt());
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaRetencion");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroVigenciasRetenciones').show()");
            index = -1;
            secRegistro = null;
        } else if (indexD >= 0 && cualTabla == 1) {
            System.out.println("Entra Duplicar Detalle Embargo");

            duplicarRetencion = new Retenciones();
            m++;
            n = BigInteger.valueOf(m);

            if (tipoListaD == 0) {
                duplicarRetencion.setSecuencia(n);
                duplicarRetencion.setValorminimo(listaRetenciones.get(indexD).getValorminimo());
                duplicarRetencion.setValormaximo(listaRetenciones.get(indexD).getValormaximo());
                duplicarRetencion.setPorcentaje(listaRetenciones.get(indexD).getPorcentaje());
                duplicarRetencion.setValor(listaRetenciones.get(indexD).getValor());
                duplicarRetencion.setAdicionauvt(listaRetenciones.get(indexD).getAdicionauvt());
                duplicarRetencion.setVigencia(listaRetenciones.get(indexD).getVigencia());
            }
            if (tipoListaD == 1) {
                duplicarRetencion.setSecuencia(n);
                duplicarRetencion.setValorminimo(filtradoListaRetenciones.get(indexD).getValorminimo());
                duplicarRetencion.setValormaximo(filtradoListaRetenciones.get(indexD).getValormaximo());
                duplicarRetencion.setPorcentaje(filtradoListaRetenciones.get(indexD).getPorcentaje());
                duplicarRetencion.setValor(filtradoListaRetenciones.get(indexD).getValor());
                duplicarRetencion.setAdicionauvt(filtradoListaRetenciones.get(indexD).getAdicionauvt());
                duplicarRetencion.setVigencia(filtradoListaRetenciones.get(indexD).getVigencia());

            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRetencion");
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroRetencion').show()");
            indexD = -1;
            secRegistro = null;

        }
    }

    //MOSTRAR DATOS CELDA
    public void editarCelda() {
        if (index >= 0 && cualTabla == 0) {
            if (tipoLista == 0) {
                editarVigenciasRetenciones = listaVigenciasRetenciones.get(index);
            }
            if (tipoLista == 1) {
                editarVigenciasRetenciones = filtradoListaVigenciasRetenciones.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoVR");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoVR').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVigenciaVR");
                RequestContext.getCurrentInstance().execute("PF('editarFechaVigenciaVR').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarUvtVR");
                RequestContext.getCurrentInstance().execute("PF('editarUvtVR').show()");
                cualCelda = -1;
            }
            index = -1;
        } else if (indexD >= 0 && cualTabla == 1) {
            if (tipoListaD == 0) {
                editarRetenciones = listaRetenciones.get(indexD);
            }
            if (tipoListaD == 1) {
                editarRetenciones = filtradoListaRetenciones.get(indexD);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCeldaD);
            System.out.println("Cual Tabla: " + cualTabla);
            if (cualCeldaD == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorMinimoR");
                RequestContext.getCurrentInstance().execute("PF('editarValorMinimoR').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorMaximoR");
                RequestContext.getCurrentInstance().execute("PF('editarValorMaximoR').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPorcentajeR");
                RequestContext.getCurrentInstance().execute("PF('editarPorcentajeR').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorR");
                RequestContext.getCurrentInstance().execute("PF('editarValorR').show()");
                cualCeldaD = -1;
            } else if (cualCeldaD == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarAdicionaR");
                RequestContext.getCurrentInstance().execute("PF('editarAdicionaR').show()");
                cualCeldaD = -1;
            }
            indexD = -1;
        }
        secRegistro = null;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void chiste() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (!listaVigenciasRetenciones.isEmpty() && listaRetenciones.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
            RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
        }
        int tamaño = listaVigenciasRetenciones.size();

        if (tamaño == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciasRetenciones");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasRetenciones').show()");
        }

        if (listaRetenciones.isEmpty() && !listaVigenciasRetenciones.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:elegirTabla");
            RequestContext.getCurrentInstance().execute("PF('elegirTabla').show()");
        } else if (cualTabla == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroVigenciasRetenciones");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasRetenciones').show()");
        } else if (cualTabla == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroRetenciones");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroRetenciones').show()");
        }
    }

    public void dialogoVigencias() {
        cualTabla = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:NuevoRegistroVigenciasRetenciones");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroVigenciasRetenciones').show()");
    }

    public void dialogoRetenciones() {
        cualTabla = 1;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:NuevoRegistroRetenciones");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroRetenciones').show()");
    }

    public void modificarFechas(int i, int c) {
        VigenciasRetenciones auxiliar = null;
        RequestContext context = RequestContext.getCurrentInstance();

        if (tipoLista == 0) {
            auxiliar = listaVigenciasRetenciones.get(i);
        }
        if (tipoLista == 1) {
            auxiliar = filtradoListaVigenciasRetenciones.get(i);
        }

        if (auxiliar.getFechavigencia() != null) {
            System.out.println("Yay");
            /*
             if (listaDeclarantes.get(index).getFechafinal().before(listaDeclarantes.get(index).getFechainicial())) {
             listaDeclarantes.get(index).setFechafinal(fechaFinal);
             RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
             RequestContext.getCurrentInstance().execute("PF('fechas').show()");
             RequestContext.getCurrentInstance().update("form:datosDeclarantes");
             }

             if (listaDeclarantes.get(index).getFechainicial().after(listaDeclarantes.get(index).getFechafinal())) {
             listaDeclarantes.get(index).setFechainicial(fechaInicial);
             RequestContext.getCurrentInstance().update("formularioDialogos:fechas");
             RequestContext.getCurrentInstance().execute("PF('fechas').show()");
             RequestContext.getCurrentInstance().update("form:datosDeclarantes");
             }
             */
        } else {
            if (tipoLista == 0) {
                listaVigenciasRetenciones.get(i).setFechavigencia(fechaVigencia);
            }
            if (tipoLista == 1) {
                filtradoListaVigenciasRetenciones.get(i).setFechavigencia(fechaVigencia);

            }
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
            RequestContext.getCurrentInstance().execute("PF('errorRegNew').show()");
        }
    }

    public boolean validarFechasRegistro(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        System.err.println("fechaparametro : " + fechaParametro);
        boolean retorno = true;
        if (i == 0) {
            VigenciasRetenciones auxiliar = null;
            if (tipoLista == 0) {
                auxiliar = listaVigenciasRetenciones.get(index);
            }
            if (tipoLista == 1) {
                auxiliar = filtradoListaVigenciasRetenciones.get(index);
            }

        }

        return retorno;
    }

    //AUTOCOMPLETAR Vigencias
    public void modificarVigenciasRetenciones(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaVigenciasRetencionesCrear.contains(listaVigenciasRetenciones.get(index))) {

                    if (listaVigenciasRetencionesModificar.isEmpty()) {
                        listaVigenciasRetencionesModificar.add(listaVigenciasRetenciones.get(index));
                    } else if (!listaVigenciasRetencionesModificar.contains(listaVigenciasRetenciones.get(index))) {
                        listaVigenciasRetencionesModificar.add(listaVigenciasRetenciones.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            } else {
                if (!listaVigenciasRetencionesCrear.contains(filtradoListaVigenciasRetenciones.get(index))) {

                    if (listaVigenciasRetencionesCrear.isEmpty()) {
                        listaVigenciasRetencionesCrear.add(filtradoListaVigenciasRetenciones.get(index));
                    } else if (!listaVigenciasRetencionesCrear.contains(filtradoListaVigenciasRetenciones.get(index))) {
                        listaVigenciasRetencionesCrear.add(filtradoListaVigenciasRetenciones.get(index));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                index = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosVigenciasRetenciones");
        }
    }

    //AUTOCOMPLETAR Retenciones
    public void modificarRetenciones(int indiceD, String confirmarCambio, String valorConfirmar) {
        indexD = indiceD;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaRetencionesCrear.contains(listaRetenciones.get(indexD))) {

                    if (listaRetencionesModificar.isEmpty()) {
                        listaRetencionesModificar.add(listaRetenciones.get(indexD));
                    } else if (!listaRetencionesModificar.contains(listaRetenciones.get(indexD))) {
                        listaRetencionesModificar.add(listaRetenciones.get(indexD));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                indexD = -1;
                secRegistro = null;
            } else {
                if (!listaRetencionesCrear.contains(filtradoListaRetenciones.get(indexD))) {

                    if (listaRetencionesCrear.isEmpty()) {
                        listaRetencionesCrear.add(filtradoListaRetenciones.get(indexD));
                    } else if (!listaRetencionesCrear.contains(filtradoListaRetenciones.get(indexD))) {
                        listaRetencionesCrear.add(filtradoListaRetenciones.get(indexD));
                    }
                    if (guardado == true) {
                        guardado = false;
                    }
                    cambiosPagina = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                indexD = -1;
                secRegistro = null;
            }
            RequestContext.getCurrentInstance().update("form:datosRetenciones");
        }

    }

    //UBICACION CELDA
    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            index = indice;
            cualCelda = celda;
            cualTabla = 0;
            tablaImprimir = ":formExportar:datosVigenciasRetencionesExportar";
            nombreArchivo = "VigenciasRetencionesXML";
            System.out.println("CualTabla = " + cualTabla);
            fechaVigencia = listaVigenciasRetenciones.get(index).getFechavigencia();
            vigenciaRetencionSeleccionado = listaVigenciasRetenciones.get(index);
            cambiarVigencia();

            if (tipoLista == 0) {
                secRegistro = listaVigenciasRetenciones.get(index).getSecuencia();

            } else {
                secRegistro = filtradoListaVigenciasRetenciones.get(index).getSecuencia();
            }
        }
    }

    //UBICACION CELDA
    public void cambiarIndiceD(int indiceD, int celda) {
        if (permitirIndex == true) {
            indexD = indiceD;
            cualCeldaD = celda;
            cualTabla = 1;
            tablaImprimir = ":formExportar:datosRetencionesExportar";
            nombreArchivo = "RetencionesXML";
            System.out.println("CualTabla = " + cualTabla);
            retencionSeleccionado = listaRetenciones.get(indexD);
            if (tipoLista == 0) {
                secRegistro = listaRetenciones.get(indexD).getSecuencia();

            } else {
                secRegistro = filtradoListaRetenciones.get(indexD).getSecuencia();
            }
        }
    }

    //EXPORTAR
    public void exportPDF() throws IOException {
        if (cualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasRetencionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDFTablasAnchas();
            exporter.export(context, tabla, "VigenciasRetencionesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            index = -1;
            secRegistro = null;
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosRetencionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarPDFTablasAnchas();
            exporter.export(context, tabla, "RetencionesPDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexD = -1;
            secRegistro = null;
        }
    }

    public void exportXLS() throws IOException {
        if (cualTabla == 0) {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVigenciasRetencionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "VigenciasRetencionesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            index = -1;
            secRegistro = null;
        } else {
            DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosRetencionesExportar");
            FacesContext context = FacesContext.getCurrentInstance();
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "RetencionesXLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexD = -1;
            secRegistro = null;
        }
    }

    //LIMPIAR NUEVO REGISTRO
    public void limpiarNuevoVigencia() {
        nuevoVigenciasRetenciones = new VigenciasRetenciones();
        index = -1;
        secRegistro = null;
    }

    //LIMPIAR NUEVO DETALLE EMBARGO
    public void limpiarNuevoRetencion() {
        nuevoRetencion = new Retenciones();
        indexD = -1;
        secRegistro = null;
    }

    //LIMPIAR DUPLICAR
    public void limpiarduplicarVigencia() {
        duplicarVigenciasRetenciones = new VigenciasRetenciones();
    }
    //LIMPIAR DUPLICAR NO FORMAL

    public void limpiarduplicarRetencion() {
        duplicarRetencion = new Retenciones();
    }

    public void verificarRastro() {
        if (cualTabla == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("lol");
            if (!listaVigenciasRetenciones.isEmpty()) {
                if (secRegistro != null) {
                    System.out.println("lol 2");
                    int resultado = administrarRastros.obtenerTabla(secRegistro, "VIGENCIASRETENCIONES");
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
            } else {
                if (administrarRastros.verificarHistoricosTabla("VIGENCIASRETENCIONES")) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
                }

            }
            index = -1;
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("D");
            if (!listaRetenciones.isEmpty()) {
                if (secRegistro != null) {
                    System.out.println("NF2");
                    int resultadoNF = administrarRastros.obtenerTabla(secRegistro, "RETENCIONES");
                    System.out.println("resultado: " + resultadoNF);
                    if (resultadoNF == 1) {
                        RequestContext.getCurrentInstance().execute("PF('errorObjetosDBNF').show()");
                    } else if (resultadoNF == 2) {
                        RequestContext.getCurrentInstance().execute("PF('confirmarRastroNF').show()");
                    } else if (resultadoNF == 3) {
                        RequestContext.getCurrentInstance().execute("PF('errorRegistroRastroNF').show()");
                    } else if (resultadoNF == 4) {
                        RequestContext.getCurrentInstance().execute("PF('errorTablaConRastroNF').show()");
                    } else if (resultadoNF == 5) {
                        RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastroNF').show()");
                    }
                } else {
                    RequestContext.getCurrentInstance().execute("PF('seleccionarRegistroNF').show()");
                }
            } else {
                if (administrarRastros.verificarHistoricosTabla("RETENCIONES")) {
                    RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistoricoNF').show()");
                } else {
                    RequestContext.getCurrentInstance().execute("PF('errorRastroHistoricoNF').show()");
                }

            }
            indexD = -1;
        }

    }

    //Getter & Setter
    public List<VigenciasRetenciones> getListaVigenciasRetenciones() {
        if (listaVigenciasRetenciones == null) {
            listaVigenciasRetenciones = administrarRetenciones.consultarVigenciasRetenciones();
            if (!listaVigenciasRetenciones.isEmpty()) {
                vigenciaRetencionSeleccionado = listaVigenciasRetenciones.get(0);
                secuenciaVigenciaRetencion = vigenciaRetencionSeleccionado.getSecuencia();
            }
        }
        return listaVigenciasRetenciones;
    }

    public void setListaVigenciasRetenciones(List<VigenciasRetenciones> listaVigenciasRetenciones) {
        this.listaVigenciasRetenciones = listaVigenciasRetenciones;
    }

    public List<VigenciasRetenciones> getFiltradoListaVigenciasRetenciones() {
        return filtradoListaVigenciasRetenciones;
    }

    public void setFiltradoListaVigenciasRetenciones(List<VigenciasRetenciones> filtradoListaVigenciasRetenciones) {
        this.filtradoListaVigenciasRetenciones = filtradoListaVigenciasRetenciones;
    }

    public VigenciasRetenciones getVigenciaRetencionSeleccionado() {
        return vigenciaRetencionSeleccionado;
    }

    public void setVigenciaRetencionSeleccionado(VigenciasRetenciones vigenciaRetencionSeleccionado) {
        this.vigenciaRetencionSeleccionado = vigenciaRetencionSeleccionado;
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

    public List<Retenciones> getListaRetenciones() {
        if (listaRetenciones == null && vigenciaRetencionSeleccionado != null) {
            listaRetenciones = administrarRetenciones.consultarRetenciones(secuenciaVigenciaRetencion);
        }
        return listaRetenciones;
    }

    public void setListaRetenciones(List<Retenciones> listaRetenciones) {
        this.listaRetenciones = listaRetenciones;
    }

    public List<Retenciones> getFiltradoListaRetenciones() {
        return filtradoListaRetenciones;
    }

    public void setFiltradoListaRetenciones(List<Retenciones> filtradoListaRetenciones) {
        this.filtradoListaRetenciones = filtradoListaRetenciones;
    }

    public Retenciones getRetencionSeleccionado() {
        return retencionSeleccionado;
    }

    public void setRetencionSeleccionado(Retenciones retencionSeleccionado) {
        this.retencionSeleccionado = retencionSeleccionado;
    }

    public String getAltoScrollVigenciasRetenciones() {
        return altoScrollVigenciasRetenciones;
    }

    public void setAltoScrollVigenciasRetenciones(String altoScrollVigenciasRetenciones) {
        this.altoScrollVigenciasRetenciones = altoScrollVigenciasRetenciones;
    }

    public String getAltoScrollRetenciones() {
        return altoScrollRetenciones;
    }

    public void setAltoScrollRetenciones(String altoScrollRetenciones) {
        this.altoScrollRetenciones = altoScrollRetenciones;
    }

    public VigenciasRetenciones getEditarVigenciasRetenciones() {
        return editarVigenciasRetenciones;
    }

    public void setEditarVigenciasRetenciones(VigenciasRetenciones editarVigenciasRetenciones) {
        this.editarVigenciasRetenciones = editarVigenciasRetenciones;
    }

    public Retenciones getEditarRetenciones() {
        return editarRetenciones;
    }

    public void setEditarRetenciones(Retenciones editarRetenciones) {
        this.editarRetenciones = editarRetenciones;
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

    public VigenciasRetenciones getNuevoVigenciasRetenciones() {
        return nuevoVigenciasRetenciones;
    }

    public void setNuevoVigenciasRetenciones(VigenciasRetenciones nuevoVigenciasRetenciones) {
        this.nuevoVigenciasRetenciones = nuevoVigenciasRetenciones;
    }

    public Retenciones getNuevoRetencion() {
        return nuevoRetencion;
    }

    public void setNuevoRetencion(Retenciones nuevoRetencion) {
        this.nuevoRetencion = nuevoRetencion;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public VigenciasRetenciones getDuplicarVigenciasRetenciones() {
        return duplicarVigenciasRetenciones;
    }

    public void setDuplicarVigenciasRetenciones(VigenciasRetenciones duplicarVigenciasRetenciones) {
        this.duplicarVigenciasRetenciones = duplicarVigenciasRetenciones;
    }

    public Retenciones getDuplicarRetencion() {
        return duplicarRetencion;
    }

    public void setDuplicarRetencion(Retenciones duplicarRetencion) {
        this.duplicarRetencion = duplicarRetencion;
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

}
