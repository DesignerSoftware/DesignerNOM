/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Administrar.AdministrarNovedadCesantiasRC;
import Entidades.Empleados;
import Entidades.MotivosCesantias;
import Entidades.NovedadesSistema;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNovedadesSistemaInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
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
public class ControlNovedadCesantiasRC implements Serializable {

    @EJB
    AdministrarNovedadCesantiasRC administrarNovedadesPagoCesantias;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarNovedadesSistemaInterface administrarNovedadesSistema;
    //SECUENCIA DE LA PERSONA
    private BigInteger secuenciaEmpleado;
    //Lista Empleados Novedad Pago Parcial Cesantías
    private List<Empleados> listaEmpleadosNovedad;
    private List<Empleados> listaFiltrarEmpleadosNovedad;
    private Empleados empleadoSeleccionado;
    //Lista Novedades Pago Parcial Cesantias
    private List<NovedadesSistema> listaNovedades;
    private List<NovedadesSistema> listaFiltrarNovedades;
    private NovedadesSistema novedadSeleccionada;
    //LOV EMPLEADOS
    private List<Empleados> listaEmpleadosLOV;
    private List<Empleados> listaFiltrarEmpleadosLOV;
    private Empleados empleadoSeleccionadoLOV;
    //LISTA CESANTIAS NO LIQUIDADAS
    private List<Empleados> listaaux;
    private List<NovedadesSistema> listaauxnov;
    //LOV MOTIVO CESANTIAS
    private List<MotivosCesantias> listaMotivosCesantiasLOV;
    private List<MotivosCesantias> listaFiltrarMotivosCesantiasLOV;
    private MotivosCesantias motivoCesantiaSeleccionado;
    //Duplicar
    private NovedadesSistema duplicarNovedad;
    //editar celda
    private NovedadesSistema editarNovedades;
    private int cualCelda, tipoLista;
    //Crear Novedades
    private List<NovedadesSistema> listaNovedadesCrear;
    private NovedadesSistema nuevaNovedad;
    //Modificar Novedades
    private List<NovedadesSistema> listaNovedadesModificar;
    //Borrar Novedades
    private List<NovedadesSistema> listaNovedadesBorrar;
    //OTROS
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex;
    //RASTROS
    private boolean guardado;
    //COLUMNAS TABLA NOVEDADES
    private Column fechaCorteCesantias, fechaCorteIntCesantias, valorCesantias, valorIntCesantias, valorSolicitado, motivoCesantia, observacion, beneficiarios;
    private String paginaAnterior, motivoCesantias;
    private int altotabla;
    //variable para activar boton mostrar todos
    private boolean activarMTodos;
    private String infoRegistroEmpleados, infoRegistroEmpleadosLov, infoRegistroMotivosCesantias, infoRegistroNovedadCesantias;
    private int paraNuevaNovedad;
    private String mensajeValidacion;
    private int k;
    private String observaciones, beneficiario, motivo;
    //LISTA QUE TRAE EL VALOR DE LA CESANTIAS
    private List<BigDecimal> listavalorcesantias;
    private List<BigDecimal> listafiltrarvalorcesantias;
    private BigDecimal valorcesantiaSeleccionado;
    //LISTA QUE TRAE EL VALOR DE LOS INTERESES DE CESANTIAS
    private List<BigDecimal> listavalorintcesantias;
    private List<BigDecimal> listafiltrarvalorintcesantias;
    private BigDecimal valorintcesantiaSeleccionado;
    private BigDecimal provisionlov, provisioncesantias;
    private boolean todas, actuales;

    public ControlNovedadCesantiasRC() {
        listaEmpleadosNovedad = null;
        listaNovedadesBorrar = new ArrayList<NovedadesSistema>();
        listaNovedadesCrear = new ArrayList<NovedadesSistema>();
        listaNovedadesModificar = new ArrayList<NovedadesSistema>();
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        listaEmpleadosLOV = null;
        permitirIndex = true;
        altotabla = 110;
        paginaAnterior = "";
        empleadoSeleccionado = null;
        novedadSeleccionada = null;
        nuevaNovedad = new NovedadesSistema();
        nuevaNovedad.setEstado("ABIERTO");
        nuevaNovedad.setDias(BigInteger.valueOf(0));
        nuevaNovedad.setPagarporfuera("S");
        nuevaNovedad.setTipo("CESANTIA");
        nuevaNovedad.setSubtipo("DINERO");
        nuevaNovedad.setFechasistema(new Date());
        nuevaNovedad.setMotivocesantia(new MotivosCesantias());
        duplicarNovedad = new NovedadesSistema();
        duplicarNovedad.setFechainicialdisfrute(new Date());
        duplicarNovedad.setEstado("ABIERTO");
        duplicarNovedad.setDias(BigInteger.valueOf(0));
        duplicarNovedad.setPagarporfuera("S");
        duplicarNovedad.setTipo("CESANTIA");
        duplicarNovedad.setSubtipo("DINERO");
        duplicarNovedad.setFechasistema(new Date());
        duplicarNovedad.setMotivocesantia(new MotivosCesantias());
        provisionlov = null;
        provisioncesantias = null;
        actuales = false;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNovedadesPagoCesantias.obtenerConexion(ses.getId());
            administrarNovedadesSistema.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPag(String pag) {
        paginaAnterior = pag;
        if (!listaEmpleadosNovedad.isEmpty()) {
            empleadoSeleccionado = listaEmpleadosNovedad.get(0);
        }
    }

    public String volverPagAnterior() {
        return paginaAnterior;
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            editarNovedades = novedadSeleccionada;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechaCorteCesantias");
                RequestContext.getCurrentInstance().execute("PF('editFechaCorteCesantias').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechaCorteIntCesantias");
                RequestContext.getCurrentInstance().execute("PF('editFechaCorteIntCesantias').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editValorCesantias");
                RequestContext.getCurrentInstance().execute("PF('editValorCesantias').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editValorIntCesantias");
                RequestContext.getCurrentInstance().execute("PF('editValorIntCesantias').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editValorSolicitado");
                RequestContext.getCurrentInstance().execute("PF('editValorSolicitado').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editMotivoCesantias");
                RequestContext.getCurrentInstance().execute("PF('editMotivoCesantias').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editObservaciones");
                RequestContext.getCurrentInstance().execute("PF('editObservaciones').show()");
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editBeneficiarios");
                RequestContext.getCurrentInstance().execute("PF('editBeneficiarios').show()");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void limpiarNuevaNovedad() {
        nuevaNovedad = new NovedadesSistema();
        nuevaNovedad.setEstado("ABIERTO");
        nuevaNovedad.setDias(BigInteger.valueOf(0));
        nuevaNovedad.setPagarporfuera("S");
        nuevaNovedad.setTipo("CESANTIA");
        nuevaNovedad.setSubtipo("DINERO");
        nuevaNovedad.setFechasistema(new Date());
        provisioncesantias = null;
        provisionlov = null;

    }

    public void limpiarDuplicarNovedad() {
        duplicarNovedad = new NovedadesSistema();
        duplicarNovedad.setEstado("ABIERTO");
        duplicarNovedad.setDias(BigInteger.valueOf(0));
        duplicarNovedad.setPagarporfuera("S");
        duplicarNovedad.setTipo("CESANTIA");
        duplicarNovedad.setSubtipo("DINERO");
        duplicarNovedad.setFechasistema(new Date());
        provisioncesantias = null;
        provisionlov = null;
    }

    public void empleadosCesantiasNoLiquidadas() {
        if (!listaEmpleadosNovedad.isEmpty()) {
            listaEmpleadosNovedad.clear();
        }
        listaaux = administrarNovedadesPagoCesantias.empleadoscesantiasnoliquidados();
        System.out.println("lista aux :" + listaaux);
        if (listaaux != null) {
            for (int i = 0; i < listaaux.size(); i++) {
                listaEmpleadosNovedad.add(listaaux.get(i));
            }
        }

        contarRegistroEmpleados();
        RequestContext context = RequestContext.getCurrentInstance();
        actuales = false;
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:NOLIQUIDADAS");
    }

    public void modificarNovedadCesantias(NovedadesSistema novedadS, String confirmarCambio, String valorConfirmar) {
        novedadSeleccionada = novedadS;
        System.out.println("modificarNovedadPagoParcialCesantias");
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (tipoLista == 0) {
                if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                    if (listaNovedadesModificar.isEmpty()) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }

            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        } else if (confirmarCambio.equalsIgnoreCase("M")) {
            if (tipoLista == 0) {
                if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                    if (listaNovedadesModificar.isEmpty()) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
            } else if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }

            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        } else if (confirmarCambio.equalsIgnoreCase("MOTIVO")) {
            novedadSeleccionada.getMotivocesantia().setNombre(motivo);
            for (int i = 0; i < listaMotivosCesantiasLOV.size(); i++) {
                if (listaMotivosCesantiasLOV.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoLista == 0) {
                    novedadSeleccionada.setMotivocesantia(listaMotivosCesantiasLOV.get(indiceUnicoElemento));
                } else {
                    novedadSeleccionada.setMotivocesantia(listaMotivosCesantiasLOV.get(indiceUnicoElemento));
                }
                listaMotivosCesantiasLOV.clear();
                getListaMotivosCesantiasLOV();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:motivoscesantiasDialogo");
                RequestContext.getCurrentInstance().execute("PF('motivoscesantiasDialogo').show()");
                tipoActualizacion = 0;
            }
        }

    }

    public void agregarNuevaNovedadPagoParcialCesantias() {
        int pasa = 0;
//        Empleados emp = new Empleados();
        mensajeValidacion = new String();
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevaNovedad.getFechainicialdisfrute() == null) {
            System.out.println("Entro a Fecha Inicial Disfrute");
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial Disfrute\n";
            pasa++;
        }
        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadEmpleado");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadEmpleado').show()");
        }

        if (pasa == 0) {
            if (bandera == 1) {
                cargarTablaDefault();
            }
            //AGREGAR REGISTRO A LA LISTA NOVEDADES .
            paraNuevaNovedad++;
            nuevaNovedad.setSecuencia(BigInteger.valueOf(paraNuevaNovedad));
            nuevaNovedad.setEmpleado(empleadoSeleccionado); //Envia empleado
            System.out.println("Empleado enviado: " + empleadoSeleccionado.getPersona().getNombreCompleto());
            //-------Datos Ingresados    
            System.out.println("empleado : " + empleadoSeleccionado);
            System.out.println("fecha cesantias" + nuevaNovedad.getFechacortecesantia());
            System.out.println("valor cesantias " + nuevaNovedad.getValorcesantia());
            System.out.println("valor intereses cesantias " + nuevaNovedad.getValorinterescesantia());
            System.out.println("valor solicitado " + nuevaNovedad.getValorsolicitado());
            System.out.println("observaciones " + nuevaNovedad.getObservaciones());
            System.out.println("beneficiario " + nuevaNovedad.getBeneficiario());
            listaNovedadesCrear.add(nuevaNovedad);
            listaNovedades.add(nuevaNovedad);
            nuevaNovedad.setObservaciones(nuevaNovedad.getObservaciones());
            nuevaNovedad.setBeneficiario(nuevaNovedad.getBeneficiario());
            System.out.println("agregarNuevaNovedadPagoParcialCesantias.observacion : " + nuevaNovedad.getObservaciones());
            System.out.println("agregarNuevaNovedadPagoParcialCesantias.Beneficiario : " + nuevaNovedad.getBeneficiario());
            novedadSeleccionada = nuevaNovedad;
            contarRegistroMotivosNovedades();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            nuevaNovedad = new NovedadesSistema();
            nuevaNovedad.setEstado("ABIERTO");
            nuevaNovedad.setDias(BigInteger.valueOf(0));
            nuevaNovedad.setPagarporfuera("S");
            nuevaNovedad.setTipo("CESANTIA");
            nuevaNovedad.setSubtipo("DINERO");
            nuevaNovedad.setFechasistema(new Date());
            RequestContext.getCurrentInstance().execute("PF('nuevanovedadpagoparcialcesantias').hide()");
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        } else {
        }
    }

    public void duplicarNV() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            duplicarNovedad = new NovedadesSistema();
            paraNuevaNovedad++;
//            Empleados emple = new Empleados();
//            for (int i = 0; i < listaEmpleadosLOV.size(); i++) {
//                if (empleadoSeleccionado.getSecuencia().compareTo(listaEmpleadosLOV.get(i).getSecuencia()) == 0) {
//                    emple = listaEmpleadosLOV.get(i);
//                }
//            }
            duplicarNovedad.setSecuencia(BigInteger.valueOf(paraNuevaNovedad));
            duplicarNovedad.setEmpleado(novedadSeleccionada.getEmpleado());
            duplicarNovedad.setFechainicialdisfrute(novedadSeleccionada.getFechainicialdisfrute());
            duplicarNovedad.setFechacortecesantia(novedadSeleccionada.getFechacortecesantia());
            duplicarNovedad.setValorcesantia(novedadSeleccionada.getValorcesantia());
            duplicarNovedad.setValorinterescesantia(novedadSeleccionada.getValorinterescesantia());
            duplicarNovedad.setValorsolicitado(novedadSeleccionada.getValorsolicitado());
            duplicarNovedad.setSubtipo(novedadSeleccionada.getSubtipo());
            duplicarNovedad.setMotivocesantia(novedadSeleccionada.getMotivocesantia());
            duplicarNovedad.setObservaciones(novedadSeleccionada.getObservaciones());
            duplicarNovedad.setBeneficiario(novedadSeleccionada.getBeneficiario());
            duplicarNovedad.setDias(BigInteger.valueOf(0));

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedad");
            RequestContext.getCurrentInstance().execute("PF('duplicarregistroNovedad').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int pasa = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (duplicarNovedad.getFechainicialdisfrute() == null) {
            System.out.println("Entro a Fecha Inicial");
            mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
            pasa++;
        }
        if (duplicarNovedad.getEmpleado() == null) {
            System.out.println("Entro a Empleado");
            mensajeValidacion = mensajeValidacion + " * Empleado\n";
            pasa++;
        }
        System.out.println("Valor Pasa: " + pasa);
        if (pasa != 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaNovedadEmpleado");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaNovedadEmpleado').show()");
        }
        if (pasa == 0) {
            listaNovedadesCrear.add(duplicarNovedad);
            listaNovedades.add(duplicarNovedad);
            novedadSeleccionada = duplicarNovedad;
            contarRegistroMotivosNovedades();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                cargarTablaDefault();
            }
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            duplicarNovedad = new NovedadesSistema();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarregistroNovedad");
            RequestContext.getCurrentInstance().execute("PF('duplicarregistroNovedad').hide()");
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "NovedadPagoParcialCesantiasPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "NovedadPagoParcialCesantiasXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            altotabla = 90;
            fechaCorteCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:fechacortecesantias");
            fechaCorteCesantias.setFilterStyle("width: 85% !important");
            fechaCorteIntCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:fechacorteintcesantias");
            fechaCorteIntCesantias.setFilterStyle("");
            valorCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:valorcesantias");
            valorCesantias.setFilterStyle("width: 85% !important");
            valorIntCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:valorintcesantias");
            valorIntCesantias.setFilterStyle("width: 85% !important");
            valorSolicitado = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:valorsolicitado");
            valorSolicitado.setFilterStyle("width: 85% !important");
            motivoCesantia = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:motivocesantia");
            motivoCesantia.setFilterStyle("width: 85% !important");
            observacion = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:observaciones");
            observacion.setFilterStyle("width: 85% !important");
            beneficiarios = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:beneficiarios");
            beneficiarios.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            cargarTablaDefault();
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        }
    }

    public void mostrarTodos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaEmpleadosNovedad.isEmpty()) {
            listaEmpleadosNovedad.clear();
        }
        if (listaEmpleadosLOV != null) {
            for (int i = 0; i < listaEmpleadosLOV.size(); i++) {
                listaEmpleadosNovedad.add(listaEmpleadosLOV.get(i));
            }
        }

        empleadoSeleccionado = listaEmpleadosNovedad.get(0);
        listaNovedades = administrarNovedadesSistema.cesantiasEmpleado(empleadoSeleccionado.getSecuencia());
        listaFiltrarNovedades = null;
        aceptar = true;
        novedadSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        activarMTodos = true;
        contarRegistroEmpleados();
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
    }

    public void cargarTablaDefault() {
        FacesContext c = FacesContext.getCurrentInstance();
        altotabla = 110;
        fechaCorteCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:fechacortecesantias");
        fechaCorteCesantias.setFilterStyle("display: none; visibility: hidden;");
        fechaCorteIntCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:fechacorteintcesantias");
        fechaCorteIntCesantias.setFilterStyle("display: none; visibility: hidden;");
        valorCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:valorcesantias");
        valorCesantias.setFilterStyle("display: none; visibility: hidden;");
        valorIntCesantias = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:valorintcesantias");
        valorIntCesantias.setFilterStyle("display: none; visibility: hidden;");
        valorSolicitado = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:valorsolicitado");
        valorSolicitado.setFilterStyle("display: none; visibility: hidden;");
        motivoCesantia = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:motivocesantia");
        motivoCesantia.setFilterStyle("display: none; visibility: hidden;");
        observacion = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:observaciones");
        observacion.setFilterStyle("display: none; visibility: hidden;");
        beneficiarios = (Column) c.getViewRoot().findComponent("form:datosNovedadesEmpleado:beneficiarios");
        beneficiarios.setFilterStyle("display: none; visibility: hidden;");
        bandera = 0;
        listaFiltrarNovedades = null;
        tipoLista = 0;
    }

    public void cambiarIndice(NovedadesSistema novedadS, int celda) {
        if (permitirIndex == true) {
            novedadSeleccionada = novedadS;
            cualCelda = celda;
            novedadSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                novedadSeleccionada.getFechainicialdisfrute();
            } else if (cualCelda == 1) {
                novedadSeleccionada.getFechacortecesantia();
            } else if (cualCelda == 2) {
                novedadSeleccionada.getValorcesantia();
            } else if (cualCelda == 3) {
                novedadSeleccionada.getValorinterescesantia();
            } else if (cualCelda == 4) {
                novedadSeleccionada.getValorsolicitado();
            } else if (cualCelda == 5) {
                novedadSeleccionada.getMotivocesantia().getNombre();
            } else if (cualCelda == 6) {
                novedadSeleccionada.getObservaciones();
            } else if (cualCelda == 7) {
                novedadSeleccionada.getBeneficiario();
            }
        }
    }

    public void asignarIndex(NovedadesSistema novedadS, int dlg, int LND) {
        novedadSeleccionada = novedadS;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        contarRegistrosNovedades();
        if (dlg == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:motivoscesantiasDialogo");
            RequestContext.getCurrentInstance().execute("PF('motivoscesantiasDialogo').show()");
        }
    }

    public void autocompletarVlrCesantias() {
        System.out.println("Entró a autocompletarVlrCesantias");
        System.out.println("controlador valor cesantias : " + retornarprovisioncesantias());
        if (retornarprovisioncesantias().toBigInteger().equals(BigInteger.ZERO)) {
            RequestContext.getCurrentInstance().execute("PF('validacionvlrcesantias').show()");
            nuevaNovedad.setValorcesantia(BigInteger.ZERO);
            RequestContext.getCurrentInstance().update("formularioDialogos:vlrcesantias");
        } else if (retornarprovision() != null) {
            nuevaNovedad.setValorcesantia(retornarprovisioncesantias().toBigInteger());
            RequestContext.getCurrentInstance().update("formularioDialogos:vlrcesantias");
        }
    }

    public void autocompletarVlrIntCesantias() {
        System.out.println("Entró a autocompletarVlrIntCesantias");
        System.out.println("VlrIntCesantias del controlador " + retornarprovision());
        if (retornarprovision().toBigInteger().equals(BigInteger.ZERO)) {
            RequestContext.getCurrentInstance().execute("PF('validacionvlrintcesantias').show()");
            nuevaNovedad.setValorinterescesantia(BigInteger.ZERO);
            RequestContext.getCurrentInstance().update("formularioDialogos:vlrintcesantias");
        } else if (retornarprovision() != null) {
            nuevaNovedad.setValorinterescesantia(retornarprovision().toBigInteger());
            RequestContext.getCurrentInstance().update("formularioDialogos:vlrintcesantias");
        }
    }

    public void autocompletarVlrSolicitado() {
        System.out.println("Entró a autocompletarVlrSolicitado");
        if (retornarprovision() != null && retornarprovisioncesantias() != null) {
            nuevaNovedad.setValorsolicitado(sumarvalorsolicitado(nuevaNovedad.getValorcesantia(), nuevaNovedad.getValorinterescesantia()));
            RequestContext.getCurrentInstance().update("formularioDialogos:vlrsolicitado");
        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:validacionvlrsolicitado");
        }
    }

    public void autocompletarduplicarVlrSolicitado() {
        System.out.println("Entró a autocompletarduplicarVlrSolicitado");
        duplicarNovedad.setValorsolicitado(sumarvalorsolicitado(duplicarNovedad.getValorcesantia(), duplicarNovedad.getValorinterescesantia()));
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarvlrsolicitado");
    }

    public void autocompletarduplicarVlrIntCesantias() {
        System.out.println("Entró a autocompletarduplicaVlrIntCesantias");
        duplicarNovedad.setValorinterescesantia(retornarprovision().toBigInteger());
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarvlrintcesantias");
    }

    public void autocompletarduplicarVlrCesantias() {
        System.out.println("Entró a autocompletarduplicaVlrCesantias");
        duplicarNovedad.setValorcesantia(retornarprovisioncesantias().toBigInteger());
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarvlrcesantias");
    }

    public void cambiarEmpleado() {
        if (listaNovedadesCrear.isEmpty() && listaNovedadesBorrar.isEmpty() && listaNovedadesModificar.isEmpty()) {
            secuenciaEmpleado = empleadoSeleccionado.getSecuencia();
            //Se recargan las novedades para el empleado
            listaNovedades = administrarNovedadesSistema.cesantiasEmpleado(empleadoSeleccionado.getSecuencia());
            if (listaNovedades == null) {
                listaNovedades = new ArrayList<NovedadesSistema>();
            }
            contarRegistrosNovedades();

//            System.out.println("lista cesantias:  " + getCesantias());
//            System.out.println("lista Intcesantias:  " + getIntcesantias());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:cambiar");
            RequestContext.getCurrentInstance().execute("PF('cambiar').show()");
        }
    }

    public void abrirLista(int listaV) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaV == 0) {
            empleadoSeleccionadoLOV = null;
            contarRegistroEmpleadosLov();
            contarRegistrosNovedades();
            RequestContext.getCurrentInstance().update("formularioDialogos:empleadosDialogo");
            RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').show()");
        }
    }

    public void actualizarEmpleadosNovedad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (!listaEmpleadosNovedad.isEmpty()) {
            listaEmpleadosNovedad.clear();
            listaEmpleadosNovedad.add(empleadoSeleccionadoLOV);
            empleadoSeleccionado = listaEmpleadosNovedad.get(0);
        }
        secuenciaEmpleado = empleadoSeleccionadoLOV.getSecuencia();
        listaNovedades = administrarNovedadesSistema.cesantiasEmpleado(empleadoSeleccionado.getSecuencia());
        aceptar = true;
        listaFiltrarEmpleadosNovedad = null;
        novedadSeleccionada = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        activarMTodos = false;
        contarRegistroEmpleados();
        contarRegistrosNovedades();
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:LOVEmpleados");
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
        RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        RequestContext.getCurrentInstance().update("form:btnMostrarTodos");
    }

    public void actualizarMotivosCesantias() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            novedadSeleccionada.setMotivocesantia(motivoCesantiaSeleccionado);
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
            }

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("formularioDialogos:motivoces");
        } else if (tipoActualizacion == 1) {
            nuevaNovedad.setMotivocesantia(motivoCesantiaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:motivoces");
        } else if (tipoActualizacion == 2) {
            duplicarNovedad.setMotivocesantia(motivoCesantiaSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarmotivoces");
        }
        listaFiltrarMotivosCesantiasLOV = null;
        motivoCesantiaSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:motivoscesantiasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovmotivoscesantias");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");

        context.reset("formularioDialogos:lovmotivoscesantias:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovmotivoscesantias').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('motivoscesantiasDialogo').hide()");
    }

    public BigInteger sumarvalorsolicitado(BigInteger valorcesantia, BigInteger valorintcesantia) {
        BigInteger valorsolicitado;
        if (valorcesantia.equals(BigInteger.ZERO) && valorintcesantia.equals(BigInteger.ZERO)) {
            valorsolicitado = BigInteger.ZERO;
        } else {
            valorsolicitado = valorcesantia.add(valorintcesantia);
        }
        System.out.println("El valor solicitado es : " + valorsolicitado);
        return valorsolicitado;
        // RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
    }

    public void borrarNovedades() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (novedadSeleccionada != null) {
            if (!listaNovedadesModificar.isEmpty() && listaNovedadesModificar.contains(novedadSeleccionada)) {
                int modIndex = listaNovedadesModificar.indexOf(novedadSeleccionada);
                listaNovedadesModificar.remove(modIndex);
                listaNovedadesBorrar.add(novedadSeleccionada);
            } else if (!listaNovedadesCrear.isEmpty() && listaNovedadesCrear.contains(novedadSeleccionada)) {
                int crearIndex = listaNovedadesCrear.indexOf(novedadSeleccionada);
                listaNovedadesCrear.remove(crearIndex);
            } else {
                listaNovedadesBorrar.add(novedadSeleccionada);
            }
            listaNovedades.remove(novedadSeleccionada);
            if (tipoLista == 1) {
                listaFiltrarNovedades.remove(novedadSeleccionada);
            }
            contarRegistroMotivosNovedades();
            RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
            novedadSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    /////GUARDARCAMBIOSNOVEDADES
    public void guardarCambiosNovedades() {
        try {
            Empleados emp = new Empleados();
            if (guardado == false) {
                System.out.println("Realizando Operaciones Novedades");

                if (!listaNovedadesBorrar.isEmpty()) {
                    for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
                        System.out.println("Borrando..." + listaNovedadesBorrar.size());
                        if (listaNovedadesBorrar.get(i).getObservaciones() == null) {
                            listaNovedadesBorrar.get(i).setObservaciones(" ");
                        }
                        if (listaNovedadesBorrar.get(i).getBeneficiario() == null) {
                            listaNovedadesBorrar.get(i).setBeneficiario(null);
                        }
                        administrarNovedadesSistema.borrarNovedades(listaNovedadesBorrar.get(i));
                    }
                    System.out.println("Entra");
                    listaNovedadesBorrar.clear();
                }

                if (!listaNovedadesCrear.isEmpty()) {
                        for (int i = 0; i < listaNovedadesCrear.size(); i++) {
                            System.out.println("Creando...");
                            if (listaNovedadesCrear.get(i).getObservaciones() == null) {
                                listaNovedadesCrear.get(i).setObservaciones(" ");
                            }
                            if (listaNovedadesCrear.get(i).getBeneficiario() == null) {
                                listaNovedadesCrear.get(i).setBeneficiario(null);
                            }
                            System.out.println(listaNovedadesCrear.get(i).getTipo());
                            administrarNovedadesSistema.crearNovedades(listaNovedadesCrear.get(i));
                        }
                        System.out.println("LimpiaLista");
                        listaNovedadesCrear.clear();
                }

                if (!listaNovedadesModificar.isEmpty()) {
                    for (int i = 0; i < listaNovedadesModificar.size(); i++) {
                        System.out.println(" modificando");
                        administrarNovedadesSistema.modificarNovedades(listaNovedadesModificar.get(i));
                    }
                }
                System.out.println("Se guardaron los datos con exito");
                listaNovedades = null;
                getListaNovedades();
                contarRegistrosNovedades();
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
                guardado = true;
                permitirIndex = true;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                k = 0;
            }
            System.out.println("Valor k: " + k);
        } catch (Exception e) {
            System.out.println("Error guardando datos : " + e.getMessage());
            RequestContext.getCurrentInstance().execute("PF('errorGuardado').show()");
            FacesMessage msg = new FacesMessage("Información", "Error en el guardado, Intente nuevamente");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");

        }

    }

    public void cancelarCambioMotivoCesantias() {
        listaFiltrarMotivosCesantiasLOV = null;
        motivoCesantiaSeleccionado = null;
        aceptar = true;
        provisioncesantias = null;
        provisionlov = null;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:motivoscesantiasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovmotivoscesantias");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarP");
        context.reset("formularioDialogos:lovmotivoscesantias:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovmotivoscesantias').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('motivoscesantiasDialogo').hide()");

    }

    //////SUMAR VALOR CESANTIA E INT CESANTIAS PARA SACAR EL VALOR SOLICITADO
    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarCambioEmpleados() {
        listaFiltrarEmpleadosLOV = null;
        empleadoSeleccionadoLOV = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:LOVEmpleados:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('LOVEmpleados').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empleadosDialogo').hide()");
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            cargarTablaDefault();
        }
        if (!listaEmpleadosNovedad.isEmpty()) {
            listaEmpleadosNovedad.clear();
        }
        if (listaEmpleadosLOV != null) {
            for (int i = 0; i < listaEmpleadosLOV.size(); i++) {
                listaEmpleadosNovedad.add(listaEmpleadosLOV.get(i));
            }
        }
        contarRegistrosNovedades();
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        permitirIndex = true;
        nuevaNovedad = new NovedadesSistema();
        nuevaNovedad.setEstado("ABIERTO");
        nuevaNovedad.setDias(BigInteger.valueOf(0));
        nuevaNovedad.setPagarporfuera("S");
        nuevaNovedad.setTipo("CESANTIA");
        nuevaNovedad.setSubtipo("DINERO");
        nuevaNovedad.setFechasistema(new Date());
        altotabla = 110;
        listaNovedadesBorrar.clear();
        listaNovedadesCrear.clear();
        listaNovedadesModificar.clear();
        listaNovedades = null;
        listaEmpleadosLOV = null;
        activarMTodos = true;
        empleadoSeleccionado = null;
        novedadSeleccionada = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
        RequestContext.getCurrentInstance().update("form:datosEmpleados");
    }

    public void salir() {

        if (bandera == 1) {
            cargarTablaDefault();
        }
        listaEmpleadosNovedad = null;
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        listaEmpleadosLOV = null;
        permitirIndex = true;
        nuevaNovedad = new NovedadesSistema();
        nuevaNovedad.setEstado("ABIERTO");
        nuevaNovedad.setDias(BigInteger.valueOf(0));
        nuevaNovedad.setPagarporfuera("S");
        nuevaNovedad.setTipo("CESANTIA");
        nuevaNovedad.setSubtipo("DINERO");
        nuevaNovedad.setFechasistema(new Date());
        altotabla = 110;
        listaNovedadesBorrar.clear();
        listaNovedadesCrear.clear();
        listaNovedadesModificar.clear();
        novedadSeleccionada = null;
        listaNovedades = null;
        activarMTodos = true;
        RequestContext.getCurrentInstance().update("form:datosNovedadesEmpleado");
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosNovedades();
    }

    public void contarRegistroEmpleados() {
        RequestContext.getCurrentInstance().update("form:infoRegistroEmpleados");
    }

    public void contarRegistroEmpleadosLov() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpleadosLOV");
    }

    public void contarRegistroMotivosNovedades() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroMotivosNovedades");
    }

    public void contarRegistrosNovedades() {
        RequestContext.getCurrentInstance().update("form:infoRegistroNovedad");
    }

    public void entrarNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("entrarNuevoRegistro.empleadoSeleccionado : " + empleadoSeleccionado.getSecuencia());

        if (empleadoSeleccionado != null) {
            System.out.println("empleadoSeleccionado : " + empleadoSeleccionado.getSecuencia());
            RequestContext.getCurrentInstance().update(":formularioDialogos:nuevaNovedad");
            RequestContext.getCurrentInstance().execute("PF('nuevanovedadpagoparcialcesantias').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADESSISTEMA");
            if (result == 1) {
                RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
            } else if (result == 2) {
                RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
            } else if (result == 3) {
                RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
            } else if (result == 4) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
            } else if (result == 5) {
                RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
            }
        } else if (administrarRastros.verificarHistoricosTabla("NOVEDADESSISTEMA")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        novedadSeleccionada = null;
    }

    public BigDecimal retornarprovision() {
        if (provisionlov == null) {
            getProvisionlov();
        }
        return provisionlov;
    }

    public BigDecimal retornarprovisioncesantias() {
        if (provisioncesantias == null) {
            getProvisioncesantias();
        }
        return provisioncesantias;
    }

    public void autocompletar(int tipoNuevo, String campo) {

        if (campo.equals("MOTIVO")) {
            if (tipoNuevo == 1) {
                motivo = nuevaNovedad.getMotivocesantia().getNombre();
            } else if (tipoNuevo == 2) {
                motivo = duplicarNovedad.getMotivocesantia().getNombre();
            }

        }
    }

    public void valoresautocompletarnuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("MOTIVO")) {
            if (tipoNuevo == 1) {
                nuevaNovedad.getMotivocesantia().setNombre(motivo);
            } else if (tipoNuevo == 2) {
                duplicarNovedad.getMotivocesantia().setNombre(motivo);
            }
            for (int i = 0; i < listaMotivosCesantiasLOV.size(); i++) {
                if (listaMotivosCesantiasLOV.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaNovedad.setMotivocesantia(listaMotivosCesantiasLOV.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigencias");
                } else if (tipoNuevo == 2) {
                    duplicarNovedad.setMotivocesantia(listaMotivosCesantiasLOV.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigencias");
                }
                listaMotivosCesantiasLOV.clear();
                getListaMotivosCesantiasLOV();
            } else {
                RequestContext.getCurrentInstance().update("form:motivoscesantiasDialogo");
                RequestContext.getCurrentInstance().execute("PF('motivoscesantiasDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevanovedadpagoparcialcesantias");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarregistroNovedad");
                }
            }
        }
    }

    ///////////////////////GETS Y SETS////////////////////////////////////////////////
    public List<Empleados> getListaEmpleadosNovedad() {
        if (listaEmpleadosNovedad == null) {
            listaEmpleadosNovedad = administrarNovedadesPagoCesantias.empleadosCesantias();
            empleadoSeleccionado = listaEmpleadosNovedad.get(0);
        }
        return listaEmpleadosNovedad;
    }

    public void setListaEmpleadosNovedad(List<Empleados> listaEmpleadosNovedad) {
        this.listaEmpleadosNovedad = listaEmpleadosNovedad;
    }

    public List<Empleados> getListaFiltrarEmpleadosNovedad() {
        return listaFiltrarEmpleadosNovedad;
    }

    public void setListaFiltrarEmpleadosNovedad(List<Empleados> listaFiltrarEmpleadosNovedad) {
        this.listaFiltrarEmpleadosNovedad = listaFiltrarEmpleadosNovedad;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public List<NovedadesSistema> getListaNovedades() {
        if (listaNovedades == null) {
            if (empleadoSeleccionado != null) {
                listaNovedades = administrarNovedadesSistema.cesantiasEmpleado(empleadoSeleccionado.getSecuencia());
            }
        }
        return listaNovedades;
    }

    public void setListaNovedades(List<NovedadesSistema> listaNovedades) {
        this.listaNovedades = listaNovedades;
    }

    public List<NovedadesSistema> getListaFiltrarNovedades() {
        return listaFiltrarNovedades;
    }

    public void setListaFiltrarNovedades(List<NovedadesSistema> listaFiltrarNovedades) {
        this.listaFiltrarNovedades = listaFiltrarNovedades;
    }

    public NovedadesSistema getNovedadSeleccionada() {
        return novedadSeleccionada;
    }

    public void setNovedadSeleccionada(NovedadesSistema novedadSeleccionada) {
        this.novedadSeleccionada = novedadSeleccionada;
    }

    public NovedadesSistema getDuplicarNovedad() {
        return duplicarNovedad;
    }

    public void setDuplicarNovedad(NovedadesSistema duplicarNovedad) {
        this.duplicarNovedad = duplicarNovedad;
    }

    public NovedadesSistema getEditarNovedades() {
        return editarNovedades;
    }

    public void setEditarNovedades(NovedadesSistema editarNovedades) {
        this.editarNovedades = editarNovedades;
    }

    public List<NovedadesSistema> getListaNovedadesCrear() {
        return listaNovedadesCrear;
    }

    public void setListaNovedadesCrear(List<NovedadesSistema> listaNovedadesCrear) {
        this.listaNovedadesCrear = listaNovedadesCrear;
    }

    public NovedadesSistema getNuevaNovedad() {
        return nuevaNovedad;
    }

    public void setNuevaNovedad(NovedadesSistema nuevaNovedad) {
        this.nuevaNovedad = nuevaNovedad;
    }

    public List<NovedadesSistema> getListaNovedadesModificar() {
        return listaNovedadesModificar;
    }

    public void setListaNovedadesModificar(List<NovedadesSistema> listaNovedadesModificar) {
        this.listaNovedadesModificar = listaNovedadesModificar;
    }

    public List<NovedadesSistema> getListaNovedadesBorrar() {
        return listaNovedadesBorrar;
    }

    public void setListaNovedadesBorrar(List<NovedadesSistema> listaNovedadesBorrar) {
        this.listaNovedadesBorrar = listaNovedadesBorrar;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public List<Empleados> getListaEmpleadosLOV() {
        if (listaEmpleadosLOV == null) {
            listaEmpleadosLOV = administrarNovedadesPagoCesantias.empleadosCesantias();
        }
        return listaEmpleadosLOV;
    }

    public void setListaEmpleadosLOV(List<Empleados> listaEmpleadosLOV) {
        this.listaEmpleadosLOV = listaEmpleadosLOV;
    }

    public List<Empleados> getListaFiltrarEmpleadosLOV() {
        return listaFiltrarEmpleadosLOV;
    }

    public void setListaFiltrarEmpleadosLOV(List<Empleados> listaFiltrarEmpleadosLOV) {
        this.listaFiltrarEmpleadosLOV = listaFiltrarEmpleadosLOV;
    }

    public Empleados getEmpleadoSeleccionadoLOV() {
        return empleadoSeleccionadoLOV;
    }

    public void setEmpleadoSeleccionadoLOV(Empleados empleadoSeleccionadoLOV) {
        this.empleadoSeleccionadoLOV = empleadoSeleccionadoLOV;
    }

    public List<MotivosCesantias> getListaMotivosCesantiasLOV() {
        listaMotivosCesantiasLOV = administrarNovedadesPagoCesantias.consultarMotivosCesantias();
        return listaMotivosCesantiasLOV;
    }

    public void setListaMotivosCesantiasLOV(List<MotivosCesantias> listaMotivosCesantiasLOV) {
        this.listaMotivosCesantiasLOV = listaMotivosCesantiasLOV;
    }

    public List<MotivosCesantias> getListaFiltrarMotivosCesantiasLOV() {
        return listaFiltrarMotivosCesantiasLOV;
    }

    public void setListaFiltrarMotivosCesantiasLOV(List<MotivosCesantias> listaFiltrarMotivosCesantiasLOV) {
        this.listaFiltrarMotivosCesantiasLOV = listaFiltrarMotivosCesantiasLOV;
    }

    public MotivosCesantias getMotivoCesantiaSeleccionado() {
        return motivoCesantiaSeleccionado;
    }

    public void setMotivoCesantiaSeleccionado(MotivosCesantias motivoCesantiaSeleccionado) {
        this.motivoCesantiaSeleccionado = motivoCesantiaSeleccionado;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public int getAltotabla() {
        return altotabla;
    }

    public void setAltotabla(int altotabla) {
        this.altotabla = altotabla;
    }

    public boolean isActivarMTodos() {
        return activarMTodos;
    }

    public void setActivarMTodos(boolean activarMTodos) {
        this.activarMTodos = activarMTodos;
    }

    public String getInfoRegistroEmpleados() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEmpleados");
        infoRegistroEmpleados = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleados;
    }

    public void setInfoRegistroEmpleados(String infoRegistroEmpleados) {
        this.infoRegistroEmpleados = infoRegistroEmpleados;
    }

    public String getInfoRegistroEmpleadosLov() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVEmpleados");
        infoRegistroEmpleadosLov = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpleadosLov;
    }

    public void setInfoRegistroEmpleadosLov(String infoRegistroEmpleadosLov) {
        this.infoRegistroEmpleadosLov = infoRegistroEmpleadosLov;
    }

    public String getInfoRegistroMotivosCesantias() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovmotivoscesantias");
        infoRegistroMotivosCesantias = String.valueOf(tabla.getRowCount());
        return infoRegistroMotivosCesantias;
    }

    public void setInfoRegistroMotivosCesantias(String infoRegistroMotivosCesantias) {
        this.infoRegistroMotivosCesantias = infoRegistroMotivosCesantias;
    }

    public String getInfoRegistroNovedadCesantias() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNovedadesEmpleado");
        infoRegistroNovedadCesantias = String.valueOf(tabla.getRowCount());
        return infoRegistroNovedadCesantias;
    }

    public void setInfoRegistroNovedadCesantias(String infoRegistroNovedadCesantias) {
        this.infoRegistroNovedadCesantias = infoRegistroNovedadCesantias;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public List<BigDecimal> getListavalorcesantias() {
        return listavalorcesantias;
    }

    public void setListavalorcesantias(List<BigDecimal> listavalorcesantias) {
        this.listavalorcesantias = listavalorcesantias;
    }

    public List<BigDecimal> getListavalorintcesantias() {
        return listavalorintcesantias;
    }

    public void setListavalorintcesantias(List<BigDecimal> listavalorintcesantias) {
        this.listavalorintcesantias = listavalorintcesantias;
    }

    public List<BigDecimal> getListafiltrarvalorcesantias() {
        return listafiltrarvalorcesantias;
    }

    public void setListafiltrarvalorcesantias(List<BigDecimal> listafiltrarvalorcesantias) {
        this.listafiltrarvalorcesantias = listafiltrarvalorcesantias;
    }

    public BigDecimal getValorcesantiaSeleccionado() {
        return valorcesantiaSeleccionado;
    }

    public void setValorcesantiaSeleccionado(BigDecimal valorcesantiaSeleccionado) {
        this.valorcesantiaSeleccionado = valorcesantiaSeleccionado;
    }

    public List<BigDecimal> getListafiltrarvalorintcesantias() {
        return listafiltrarvalorintcesantias;
    }

    public void setListafiltrarvalorintcesantias(List<BigDecimal> listafiltrarvalorintcesantias) {
        this.listafiltrarvalorintcesantias = listafiltrarvalorintcesantias;
    }

    public BigDecimal getValorintcesantiaSeleccionado() {
        return valorintcesantiaSeleccionado;
    }

    public void setValorintcesantiaSeleccionado(BigDecimal valorintcesantiaSeleccionado) {
        this.valorintcesantiaSeleccionado = valorintcesantiaSeleccionado;
    }

    public BigDecimal getProvisionlov() {
        provisionlov = administrarNovedadesSistema.valorIntCesantias(empleadoSeleccionado.getSecuencia());
        return provisionlov;
    }

    public void setProvisionlov(BigDecimal provisionlov) {
        this.provisionlov = provisionlov;
    }

    public BigDecimal getProvisioncesantias() {
        provisioncesantias = administrarNovedadesSistema.valorCesantias(empleadoSeleccionado.getSecuencia());
        return provisioncesantias;
    }

    public void setProvisioncesantias(BigDecimal provisioncesantias) {
        this.provisioncesantias = provisioncesantias;
    }

    public boolean isTodas() {
        return todas;
    }

    public void setTodas(boolean todas) {
        this.todas = todas;
    }

    public boolean isActuales() {
        return actuales;
    }

    public void setActuales(boolean actuales) {
        this.actuales = actuales;
    }

}
