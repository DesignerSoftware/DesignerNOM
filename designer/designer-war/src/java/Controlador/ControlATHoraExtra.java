package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.MotivosTurnos;
import Entidades.TurnosEmpleados;
import Entidades.VWEstadosExtras;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarATHoraExtraInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
 * @author Administrador
 */
@ManagedBean
@SessionScoped
public class ControlATHoraExtra implements Serializable {

    @EJB
    AdministrarATHoraExtraInterface administrarATHoraExtra;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //
    private List<Empleados> listaEmpleados;
    private List<Empleados> filtrarListaEmpleados;
    private Empleados empleadoActual;
    //
    private List<TurnosEmpleados> listaTurnosEmpleados;
    private List<TurnosEmpleados> filtrarListaTurnosEmpleados;
    private TurnosEmpleados turnoEmpleadoSeleccionado;
    private String auxHoraExtraMotivo, auxHoraExtraEstructura;
    private Date auxHoraExtraFechaIni, auxHoraExtraFechaFin;
    //
    private List<VWEstadosExtras> listaVWEstadosExtras;
    private List<VWEstadosExtras> filtrarListaVWEstadosExtras;
    private VWEstadosExtras detalleSeleccionado;
    private int totalHoras, totalMinutos;
    //
    private List<Empleados> lovEmpleados;
    private List<Empleados> filtrarLovEmpleados;
    private Empleados empleadoSeleccionado;
    private String infoRegistroEmpleado;

    private List<MotivosTurnos> lovMotivosTurnos;
    private List<MotivosTurnos> filtrarLovMotivosTurnos;
    private MotivosTurnos motivoSeleccionado;
    private String infoRegistroMotivo;

    private List<Estructuras> lovEstructuras;
    private List<Estructuras> filtrarLovEstructuras;
    private Estructuras estructuraSeleccionada;
    private String infoRegistroEstructura;

    private List<TurnosEmpleados> listTurnosEmpleadosCrear;
    private List<TurnosEmpleados> listTurnosEmpleadosBorrar;
    private List<TurnosEmpleados> listTurnosEmpleadosModificar;

    private TurnosEmpleados nuevaTurno;
    private TurnosEmpleados duplicarTurno;
    private TurnosEmpleados editarTurnoEmpleado;
    private VWEstadosExtras editarDetalleTurno;

    private int indexEmpleado, cualCeldaEmpleado, indexEmpladoAux;
    private int indexHorasExtras, cualCeldaHorasExtras;
    private int indexDetalleHoraExtra, cualCeldaDetalleHoraExtra;
    private int banderaEmpleado, tipoListaEmpleado;
    private int banderaHorasExtras, tipoListaHorasExtras;
    private int banderaDetalleHoraExtra, tipoListaDetalleHoraExtra;
    private String altoTablaEmpleado;
    private String altoTablaHorasExtras;
    private String altoTablaDetalleHoraExtra;

    private boolean guardado;
    private boolean permitirIndexHoraExtra;
    private BigInteger l;
    private int k;
    private BigInteger secRegistro;
    private BigInteger backUpSecRegistro;
    private Date fechaParametro;
    private boolean aceptar;
    private String paginaAnterior;
    private String nombreTablaXML, nombreArchivoXML;
    private int tipoActualizacion;
    private boolean activarBuscar, activarMostrarTodos;
    //
    private Column empleadoCodigo, empleadoNombre;
    private Column horaExtraProcesado, horaExtraFechaInicial, horaExtraFechaFinal, horaExtraMotivo, horaExtraNHA, horaExtraNVA, horaExtraPagaVale, horaExtraEstructura, horaExtraComentario;
    private Column detalleConcepto, detalleFechaPago, detalleHoras, detalleMinutos, detalleAprobado, detalleDescripcion, detalleOpcion;

    public ControlATHoraExtra() {
        activarBuscar = false;
        activarMostrarTodos = true;

        listaEmpleados = null;
        listaTurnosEmpleados = null;
        listaVWEstadosExtras = null;

        listTurnosEmpleadosCrear = new ArrayList<TurnosEmpleados>();
        listTurnosEmpleadosBorrar = new ArrayList<TurnosEmpleados>();
        listTurnosEmpleadosModificar = new ArrayList<TurnosEmpleados>();

        nuevaTurno = new TurnosEmpleados();
        nuevaTurno.setMotivoturno(new MotivosTurnos());
        nuevaTurno.setEstructuraaprueba(new Estructuras());
        duplicarTurno = new TurnosEmpleados();
        duplicarTurno.setMotivoturno(new MotivosTurnos());
        duplicarTurno.setEstructuraaprueba(new Estructuras());
        editarTurnoEmpleado = new TurnosEmpleados();
        editarDetalleTurno = new VWEstadosExtras();

        empleadoActual = new Empleados();
        turnoEmpleadoSeleccionado = new TurnosEmpleados();
        detalleSeleccionado = new VWEstadosExtras();

        lovEmpleados = null;
        empleadoSeleccionado = new Empleados();
        lovEstructuras = null;
        estructuraSeleccionada = new Estructuras();
        lovMotivosTurnos = null;
        motivoSeleccionado = new MotivosTurnos();

        indexEmpleado = -1;
        indexEmpladoAux = -1;
        indexHorasExtras = -1;
        indexDetalleHoraExtra = -1;
        cualCeldaEmpleado = -1;
        cualCeldaHorasExtras = -1;
        cualCeldaDetalleHoraExtra = -1;
        tipoActualizacion = -1;
        banderaEmpleado = 0;
        banderaHorasExtras = 0;
        banderaDetalleHoraExtra = 0;
        tipoListaEmpleado = 0;
        tipoListaHorasExtras = 0;
        tipoListaDetalleHoraExtra = 0;
        altoTablaEmpleado = "70";
        altoTablaHorasExtras = "75";
        altoTablaDetalleHoraExtra = "70";

        guardado = true;
        secRegistro = null;
        aceptar = true;
        permitirIndexHoraExtra = true;
        totalHoras = 0;
        totalMinutos = 0;
        k = 0;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarATHoraExtra.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPagina(String page) {
        paginaAnterior = page;
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void modificarTurnoEmpleado(int indice) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoListaHorasExtras == 0) {
            if (!listTurnosEmpleadosCrear.contains(listaTurnosEmpleados.get(indice))) {

                if (listTurnosEmpleadosModificar.isEmpty()) {
                    listTurnosEmpleadosModificar.add(listaTurnosEmpleados.get(indice));
                } else if (!listTurnosEmpleadosModificar.contains(listaTurnosEmpleados.get(indice))) {
                    listTurnosEmpleadosModificar.add(listaTurnosEmpleados.get(indice));
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            indexHorasExtras = -1;
            secRegistro = null;
        } else {
            if (!listTurnosEmpleadosCrear.contains(filtrarListaTurnosEmpleados.get(indice))) {

                if (listTurnosEmpleadosModificar.isEmpty()) {
                    listTurnosEmpleadosModificar.add(filtrarListaTurnosEmpleados.get(indice));
                } else if (!listTurnosEmpleadosModificar.contains(filtrarListaTurnosEmpleados.get(indice))) {
                    listTurnosEmpleadosModificar.add(filtrarListaTurnosEmpleados.get(indice));
                }
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
            indexHorasExtras = -1;
            secRegistro = null;
        }
        RequestContext.getCurrentInstance().update("form:datosHoraExtra");
    }

    public void modificarTurnoEmpleado(int indice, String confirmarCambio, String valorConfirmar) {
        indexHorasExtras = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("MOTIVO")) {
            if (tipoListaHorasExtras == 0) {
                listaTurnosEmpleados.get(indice).getMotivoturno().setNombre(auxHoraExtraMotivo);
            } else {
                filtrarListaTurnosEmpleados.get(indice).getMotivoturno().setNombre(auxHoraExtraMotivo);
            }
            for (int i = 0; i < lovMotivosTurnos.size(); i++) {
                if (lovMotivosTurnos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaHorasExtras == 0) {
                    listaTurnosEmpleados.get(indice).setMotivoturno(lovMotivosTurnos.get(indiceUnicoElemento));
                } else {
                    filtrarListaTurnosEmpleados.get(indice).setMotivoturno(lovMotivosTurnos.get(indiceUnicoElemento));
                }
                lovMotivosTurnos.clear();
                getLovMotivosTurnos();
            } else {
                permitirIndexHoraExtra = false;
                RequestContext.getCurrentInstance().update("form:MotivoTurnoDialogo");
                PrimefacesContextUI.ejecutar("PF('MotivoTurnoDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("ESTRUCTURA")) {
            if (tipoListaHorasExtras == 0) {
                listaTurnosEmpleados.get(indice).getEstructuraaprueba().setNombre(auxHoraExtraEstructura);
            } else {
                filtrarListaTurnosEmpleados.get(indice).getEstructuraaprueba().setNombre(auxHoraExtraEstructura);
            }
            for (int i = 0; i < lovEstructuras.size(); i++) {
                if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoListaHorasExtras == 0) {
                    listaTurnosEmpleados.get(indice).setEstructuraaprueba(lovEstructuras.get(indiceUnicoElemento));
                } else {
                    filtrarListaTurnosEmpleados.get(indice).setEstructuraaprueba(lovEstructuras.get(indiceUnicoElemento));
                }
                lovEstructuras.clear();
                getLovEstructuras();
            } else {
                permitirIndexHoraExtra = false;
                RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
                PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (tipoListaHorasExtras == 0) {
                if (!listTurnosEmpleadosCrear.contains(listaTurnosEmpleados.get(indice))) {

                    if (listTurnosEmpleadosModificar.isEmpty()) {
                        listTurnosEmpleadosModificar.add(listaTurnosEmpleados.get(indice));
                    } else if (!listTurnosEmpleadosModificar.contains(listaTurnosEmpleados.get(indice))) {
                        listTurnosEmpleadosModificar.add(listaTurnosEmpleados.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                indexHorasExtras = -1;
                secRegistro = null;
            } else {
                if (!listTurnosEmpleadosCrear.contains(filtrarListaTurnosEmpleados.get(indice))) {

                    if (listTurnosEmpleadosModificar.isEmpty()) {
                        listTurnosEmpleadosModificar.add(filtrarListaTurnosEmpleados.get(indice));
                    } else if (!listTurnosEmpleadosModificar.contains(filtrarListaTurnosEmpleados.get(indice))) {
                        listTurnosEmpleadosModificar.add(filtrarListaTurnosEmpleados.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                indexHorasExtras = -1;
                secRegistro = null;
            }
        }
        RequestContext.getCurrentInstance().update("form:datosHoraExtra");
    }

    public boolean validarCamposNullTurnosEmpleados(int i) {
        boolean retorno = true;
        if (i == 0) {
            TurnosEmpleados aux = null;
            if (tipoListaHorasExtras == 0) {
                aux = listaTurnosEmpleados.get(indexHorasExtras);
            } else {
                aux = listaTurnosEmpleados.get(indexHorasExtras);
            }
            if (aux.getFechafinal() != null && aux.getFechainicial() != null) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevaTurno.getFechafinal() != null && nuevaTurno.getFechainicial() != null) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarTurno.getFechafinal() != null && duplicarTurno.getFechainicial() != null) {
                retorno = true;
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechasRegistroTurno(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        if (i == 0) {
            TurnosEmpleados auxiliar = null;
            if (tipoListaHorasExtras == 0) {
                auxiliar = listaTurnosEmpleados.get(indexHorasExtras);
            } else {
                auxiliar = filtrarListaTurnosEmpleados.get(indexHorasExtras);
            }
            if (auxiliar.getFechafinal().after(fechaParametro) && auxiliar.getFechainicial().after(fechaParametro)) {
                if (auxiliar.getFechafinal().after(auxiliar.getFechainicial())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        if (i == 1) {
            if (nuevaTurno.getFechafinal().after(fechaParametro) && nuevaTurno.getFechainicial().after(fechaParametro)) {
                if (nuevaTurno.getFechafinal().after(nuevaTurno.getFechainicial())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        if (i == 2) {
            if (duplicarTurno.getFechafinal().after(fechaParametro) && duplicarTurno.getFechainicial().after(fechaParametro)) {
                if (duplicarTurno.getFechafinal().after(duplicarTurno.getFechainicial())) {
                    retorno = true;
                } else {
                    retorno = false;
                }
            } else {
                retorno = false;
            }
        }
        return retorno;
    }

    public void modificarFechaTurno(int i, int c) {
        TurnosEmpleados auxiliar = null;
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoListaHorasExtras == 0) {
            auxiliar = listaTurnosEmpleados.get(i);
        } else {
            auxiliar = filtrarListaTurnosEmpleados.get(i);
        }
        if (auxiliar.getFechafinal() != null && auxiliar.getFechainicial() != null) {
            boolean retorno = false;
            indexHorasExtras = i;
            retorno = validarFechasRegistroTurno(0);
            if (retorno == true) {
                cambiarIndiceHoraExtra(i, c);
                modificarTurnoEmpleado(i);
            } else {
                if (tipoListaHorasExtras == 0) {
                    listaTurnosEmpleados.get(i).setFechafinal(auxHoraExtraFechaFin);
                    listaTurnosEmpleados.get(i).setFechainicial(auxHoraExtraFechaIni);
                } else {
                    filtrarListaTurnosEmpleados.get(i).setFechafinal(auxHoraExtraFechaFin);
                    filtrarListaTurnosEmpleados.get(i).setFechainicial(auxHoraExtraFechaIni);

                }
                RequestContext.getCurrentInstance().update("form:datosHoraExtra");
                PrimefacesContextUI.ejecutar("PF('errorFechaTurno').show()");
            }
        } else {
            if (tipoListaHorasExtras == 0) {
                listaTurnosEmpleados.get(i).setFechafinal(auxHoraExtraFechaFin);
                listaTurnosEmpleados.get(i).setFechainicial(auxHoraExtraFechaIni);
            } else {
                filtrarListaTurnosEmpleados.get(i).setFechafinal(auxHoraExtraFechaFin);
                filtrarListaTurnosEmpleados.get(i).setFechainicial(auxHoraExtraFechaIni);

            }
            RequestContext.getCurrentInstance().update("form:datosHoraExtra");
            PrimefacesContextUI.ejecutar("PF('errorNullTurno').show()");
        }
    }

    public void posicionEmpleado() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndiceEmpleado(indice, columna);
    }

    public void posicionDetalleHoraExtra() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        cambiarIndiceDetalleHoraExtra(indice, columna);
    }

    public void cambiarIndiceEmpleado(int i, int celda) {
        indexEmpleado = i;
        indexEmpladoAux = i;
        cualCeldaEmpleado = celda;
        indexHorasExtras = -1;
        indexDetalleHoraExtra = -1;
        if (tipoListaEmpleado == 0) {
            secRegistro = listaEmpleados.get(indexEmpleado).getSecuencia();
        } else {
            secRegistro = filtrarListaEmpleados.get(indexEmpleado).getSecuencia();
        }
        if (banderaHorasExtras == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            horaExtraProcesado = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraProcesado");
            horaExtraProcesado.setFilterStyle("display: none; visibility: hidden;");
            horaExtraFechaInicial = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaInicial");
            horaExtraFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            horaExtraFechaFinal = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaFinal");
            horaExtraFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            horaExtraMotivo = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraMotivo");
            horaExtraMotivo.setFilterStyle("display: none; visibility: hidden;");
            horaExtraNHA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNHA");
            horaExtraNHA.setFilterStyle("display: none; visibility: hidden;");
            horaExtraNVA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNVA");
            horaExtraNVA.setFilterStyle("display: none; visibility: hidden;");
            horaExtraPagaVale = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraPagaVale");
            horaExtraPagaVale.setFilterStyle("display: none; visibility: hidden;");
            horaExtraEstructura = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraEstructura");
            horaExtraEstructura.setFilterStyle("display: none; visibility: hidden;");
            horaExtraComentario = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraComentario");
            horaExtraComentario.setFilterStyle("display: none; visibility: hidden;");
            altoTablaHorasExtras = "75";
            RequestContext.getCurrentInstance().update("form:datosHoraExtra");
            banderaHorasExtras = 0;
            filtrarListaTurnosEmpleados = null;
            tipoListaHorasExtras = 0;
        }
        if (banderaDetalleHoraExtra == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
            detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
            detalleFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFechaPago");
            detalleFechaPago.setFilterStyle("display: none; visibility: hidden;");
            detalleHoras = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleHoras");
            detalleHoras.setFilterStyle("display: none; visibility: hidden;");
            detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleMinutos");
            detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
            detalleAprobado = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleAprobado");
            detalleAprobado.setFilterStyle("display: none; visibility: hidden;");
            detalleDescripcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleDescripcion");
            detalleDescripcion.setFilterStyle("display: none; visibility: hidden;");
            detalleOpcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleOpcion");
            detalleOpcion.setFilterStyle("display: none; visibility: hidden;");
            altoTablaDetalleHoraExtra = "70";
            RequestContext.getCurrentInstance().update("form:datosDetalle");
            banderaDetalleHoraExtra = 0;
            filtrarListaVWEstadosExtras = null;
            tipoListaDetalleHoraExtra = 0;
        }
        listaTurnosEmpleados = null;
        listaVWEstadosExtras = null;
        getListaTurnosEmpleados();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosHoraExtra");
        RequestContext.getCurrentInstance().update("form:datosDetalle");
        RequestContext.getCurrentInstance().update("form:totalHoras");
        RequestContext.getCurrentInstance().update("form:totalMinutos");
    }

    public void cambiarIndiceHoraExtra(int i, int celda) {
        if (permitirIndexHoraExtra == true) {
            indexHorasExtras = i;
            cualCeldaHorasExtras = celda;
            indexEmpleado = -1;
            indexDetalleHoraExtra = -1;
            if (tipoListaHorasExtras == 0) {
                secRegistro = listaTurnosEmpleados.get(indexHorasExtras).getSecuencia();
                auxHoraExtraEstructura = listaTurnosEmpleados.get(indexHorasExtras).getEstructuraaprueba().getNombre();
                auxHoraExtraMotivo = listaTurnosEmpleados.get(indexHorasExtras).getMotivoturno().getNombre();
                auxHoraExtraFechaIni = listaTurnosEmpleados.get(indexHorasExtras).getFechainicial();
                auxHoraExtraFechaFin = listaTurnosEmpleados.get(indexHorasExtras).getFechafinal();
                turnoEmpleadoSeleccionado = listaTurnosEmpleados.get(indexHorasExtras);
            } else {
                secRegistro = filtrarListaTurnosEmpleados.get(indexHorasExtras).getSecuencia();
                auxHoraExtraEstructura = filtrarListaTurnosEmpleados.get(indexHorasExtras).getEstructuraaprueba().getNombre();
                auxHoraExtraMotivo = filtrarListaTurnosEmpleados.get(indexHorasExtras).getMotivoturno().getNombre();
                auxHoraExtraFechaIni = filtrarListaTurnosEmpleados.get(indexHorasExtras).getFechainicial();
                auxHoraExtraFechaFin = filtrarListaTurnosEmpleados.get(indexHorasExtras).getFechafinal();
                turnoEmpleadoSeleccionado = filtrarListaTurnosEmpleados.get(indexHorasExtras);
            }
            if (banderaDetalleHoraExtra == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
                detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
                detalleFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFechaPago");
                detalleFechaPago.setFilterStyle("display: none; visibility: hidden;");
                detalleHoras = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleHoras");
                detalleHoras.setFilterStyle("display: none; visibility: hidden;");
                detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleMinutos");
                detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
                detalleAprobado = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleAprobado");
                detalleAprobado.setFilterStyle("display: none; visibility: hidden;");
                detalleDescripcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleDescripcion");
                detalleDescripcion.setFilterStyle("display: none; visibility: hidden;");
                detalleOpcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleOpcion");
                detalleOpcion.setFilterStyle("display: none; visibility: hidden;");
                altoTablaDetalleHoraExtra = "70";
                RequestContext.getCurrentInstance().update("form:datosDetalle");
                banderaDetalleHoraExtra = 0;
                filtrarListaVWEstadosExtras = null;
                tipoListaDetalleHoraExtra = 0;
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (!listTurnosEmpleadosCrear.isEmpty()) {
                if (listTurnosEmpleadosCrear.contains(turnoEmpleadoSeleccionado)) {
                    listaVWEstadosExtras = null;
                    PrimefacesContextUI.ejecutar("PF('infoDetalleHoraExtra').show()");
                    System.out.println("Msn guardar");
                } else {
                    listaVWEstadosExtras = null;
                    getListaVWEstadosExtras();
                    System.out.println("Dato ya almacenado");
                }
            }
            RequestContext.getCurrentInstance().update("form:datosDetalle");
            RequestContext.getCurrentInstance().update("form:totalHoras");
            RequestContext.getCurrentInstance().update("form:totalMinutos");
        }
    }

    public void cambiarIndiceDetalleHoraExtra(int i, int c) {
        indexDetalleHoraExtra = i;
        cualCeldaDetalleHoraExtra = c;

        indexHorasExtras = -1;
        indexEmpleado = -1;
        if (tipoListaDetalleHoraExtra == 0) {
            secRegistro = listaVWEstadosExtras.get(indexDetalleHoraExtra).getEerdetalle();
        } else {
            secRegistro = filtrarListaVWEstadosExtras.get(indexDetalleHoraExtra).getEerdetalle();
        }

    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (indexEmpleado >= 0) {
            if (cualCeldaEmpleado == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadoCodigo");
                PrimefacesContextUI.ejecutar("PF('editarEmpleadoCodigo').show()");
                cualCeldaEmpleado = -1;
            }
            if (cualCeldaEmpleado == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpleadoNombre");
                PrimefacesContextUI.ejecutar("PF('editarEmpleadoNombre').show()");
                cualCeldaEmpleado = -1;
            }
            indexEmpleado = -1;
        }
        if (indexHorasExtras >= 0) {
            if (tipoListaHorasExtras == 0) {
                editarTurnoEmpleado = listaTurnosEmpleados.get(indexHorasExtras);
            } else {
                editarTurnoEmpleado = filtrarListaTurnosEmpleados.get(indexHorasExtras);
            }
            if (cualCeldaHorasExtras == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraFechaInicial");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraFechaInicial').show()");
                cualCeldaHorasExtras = -1;
            }
            if (cualCeldaHorasExtras == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraFechaFinal");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraFechaFinal').show()");
                cualCeldaHorasExtras = -1;
            }
            if (cualCeldaHorasExtras == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraMotivo");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraMotivo').show()");
                cualCeldaHorasExtras = -1;
            }
            if (cualCeldaHorasExtras == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraNHA");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraNHA').show()");
                cualCeldaHorasExtras = -1;
            }
            if (cualCeldaHorasExtras == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraNVA");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraNVA').show()");
                cualCeldaHorasExtras = -1;
            }
            if (cualCeldaHorasExtras == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraEstructura");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraEstructura').show()");
                cualCeldaHorasExtras = -1;
            }
            if (cualCeldaHorasExtras == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraComentario");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraComentario').show()");
                cualCeldaHorasExtras = -1;
            }
            indexHorasExtras = -1;
        }
        if (indexDetalleHoraExtra >= 0) {
            if (tipoListaDetalleHoraExtra == 0) {
                editarDetalleTurno = listaVWEstadosExtras.get(indexDetalleHoraExtra);
            } else {
                editarDetalleTurno = filtrarListaVWEstadosExtras.get(indexDetalleHoraExtra);
            }
            if (cualCeldaDetalleHoraExtra == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraComentario");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraComentario').show()");
                cualCeldaDetalleHoraExtra = -1;
            }
            if (cualCeldaDetalleHoraExtra == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraComentario");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraComentario').show()");
                cualCeldaDetalleHoraExtra = -1;
            }
            if (cualCeldaDetalleHoraExtra == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraComentario");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraComentario').show()");
                cualCeldaDetalleHoraExtra = -1;
            }
            if (cualCeldaDetalleHoraExtra == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraComentario");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraComentario').show()");
                cualCeldaDetalleHoraExtra = -1;
            }
            if (cualCeldaDetalleHoraExtra == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraComentario");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraComentario').show()");
                cualCeldaDetalleHoraExtra = -1;
            }
            if (cualCeldaDetalleHoraExtra == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarHoraExtraComentario");
                PrimefacesContextUI.ejecutar("PF('editarHoraExtraComentario').show()");
                cualCeldaDetalleHoraExtra = -1;
            }
            indexDetalleHoraExtra = -1;
        }
        secRegistro = null;
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (indexHorasExtras >= 0) {
            if (cualCeldaHorasExtras == 3) {
                tipoActualizacion = 0;
                RequestContext.getCurrentInstance().update("form:MotivoTurnoDialogo");
                PrimefacesContextUI.ejecutar("PF('MotivoTurnoDialogo').show()");
                cualCeldaHorasExtras = -1;
            }
            if (cualCeldaHorasExtras == 7) {
                tipoActualizacion = 0;
                RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
                PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').show()");
                cualCeldaHorasExtras = -1;
            }
            secRegistro = null;
        }
    }

    public void asignarIndex(int indice, int dialogo, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (indexHorasExtras >= 0) {
            indexHorasExtras = indice;
            if (dialogo == 0) {
                tipoActualizacion = LND;
                RequestContext.getCurrentInstance().update("form:MotivoTurnoDialogo");
                PrimefacesContextUI.ejecutar("PF('MotivoTurnoDialogo').show()");
            }
            if (dialogo == 1) {
                tipoActualizacion = LND;
                RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
                PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').show()");
            }
        }

    }

    public void borrarRegistro() {
        if (indexHorasExtras >= 0) {
            if (tipoListaHorasExtras == 0) {
                if (!listTurnosEmpleadosModificar.isEmpty() && listTurnosEmpleadosModificar.contains(listaTurnosEmpleados.get(indexHorasExtras))) {
                    int modIndex = listTurnosEmpleadosModificar.indexOf(listaTurnosEmpleados.get(indexHorasExtras));
                    listTurnosEmpleadosModificar.remove(modIndex);
                    listTurnosEmpleadosBorrar.add(listaTurnosEmpleados.get(indexHorasExtras));
                } else if (!listTurnosEmpleadosCrear.isEmpty() && listTurnosEmpleadosCrear.contains(listaTurnosEmpleados.get(indexHorasExtras))) {
                    int crearIndex = listTurnosEmpleadosCrear.indexOf(listaTurnosEmpleados.get(indexHorasExtras));
                    listTurnosEmpleadosCrear.remove(crearIndex);
                } else {
                    listTurnosEmpleadosBorrar.add(listaTurnosEmpleados.get(indexHorasExtras));
                }
                listaTurnosEmpleados.remove(indexHorasExtras);
            } else {
                if (!listTurnosEmpleadosModificar.isEmpty() && listTurnosEmpleadosModificar.contains(filtrarListaTurnosEmpleados.get(indexHorasExtras))) {
                    int modIndex = listTurnosEmpleadosModificar.indexOf(filtrarListaTurnosEmpleados.get(indexHorasExtras));
                    listTurnosEmpleadosModificar.remove(modIndex);
                    listTurnosEmpleadosBorrar.add(filtrarListaTurnosEmpleados.get(indexHorasExtras));
                } else if (!listTurnosEmpleadosCrear.isEmpty() && listTurnosEmpleadosCrear.contains(filtrarListaTurnosEmpleados.get(indexHorasExtras))) {
                    int crearIndex = listTurnosEmpleadosCrear.indexOf(filtrarListaTurnosEmpleados.get(indexHorasExtras));
                    listTurnosEmpleadosCrear.remove(crearIndex);
                } else {
                    listTurnosEmpleadosBorrar.add(filtrarListaTurnosEmpleados.get(indexHorasExtras));
                }
                int VCIndex = listaTurnosEmpleados.indexOf(filtrarListaTurnosEmpleados.get(indexHorasExtras));
                listaTurnosEmpleados.remove(VCIndex);
                filtrarListaTurnosEmpleados.remove(indexHorasExtras);
            }
            if (banderaDetalleHoraExtra == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
                detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
                detalleFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFechaPago");
                detalleFechaPago.setFilterStyle("display: none; visibility: hidden;");
                detalleHoras = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleHoras");
                detalleHoras.setFilterStyle("display: none; visibility: hidden;");
                detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleMinutos");
                detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
                detalleAprobado = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleAprobado");
                detalleAprobado.setFilterStyle("display: none; visibility: hidden;");
                detalleDescripcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleDescripcion");
                detalleDescripcion.setFilterStyle("display: none; visibility: hidden;");
                detalleOpcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleOpcion");
                detalleOpcion.setFilterStyle("display: none; visibility: hidden;");
                altoTablaDetalleHoraExtra = "70";
                RequestContext.getCurrentInstance().update("form:datosDetalle");
                banderaDetalleHoraExtra = 0;
                filtrarListaVWEstadosExtras = null;
                tipoListaDetalleHoraExtra = 0;
            }
            indexHorasExtras = -1;
            listaVWEstadosExtras = null;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosHoraExtra");
            RequestContext.getCurrentInstance().update("form:datosDetalle");
            RequestContext.getCurrentInstance().update("form:totalHoras");
            RequestContext.getCurrentInstance().update("form:totalMinutos");
            secRegistro = null;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void cancelarModificacion() {

        if (banderaEmpleado == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            empleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmpleado:empleadoCodigo");
            empleadoCodigo.setFilterStyle("display: none; visibility: hidden;");
            empleadoNombre = (Column) c.getViewRoot().findComponent("form:datosEmpleado:empleadoNombre");
            empleadoNombre.setFilterStyle("display: none; visibility: hidden;");
            altoTablaEmpleado = "70";
            RequestContext.getCurrentInstance().update("form:datosCuadrilla");
            banderaEmpleado = 0;
            filtrarListaEmpleados = null;
            tipoListaEmpleado = 0;
        }
        if (banderaHorasExtras == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            horaExtraProcesado = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraProcesado");
            horaExtraProcesado.setFilterStyle("display: none; visibility: hidden;");
            horaExtraFechaInicial = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaInicial");
            horaExtraFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            horaExtraFechaFinal = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaFinal");
            horaExtraFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            horaExtraMotivo = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraMotivo");
            horaExtraMotivo.setFilterStyle("display: none; visibility: hidden;");
            horaExtraNHA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNHA");
            horaExtraNHA.setFilterStyle("display: none; visibility: hidden;");
            horaExtraNVA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNVA");
            horaExtraNVA.setFilterStyle("display: none; visibility: hidden;");
            horaExtraPagaVale = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraPagaVale");
            horaExtraPagaVale.setFilterStyle("display: none; visibility: hidden;");
            horaExtraEstructura = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraEstructura");
            horaExtraEstructura.setFilterStyle("display: none; visibility: hidden;");
            horaExtraComentario = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraComentario");
            horaExtraComentario.setFilterStyle("display: none; visibility: hidden;");
            altoTablaHorasExtras = "75";
            RequestContext.getCurrentInstance().update("form:datosHoraExtra");
            banderaHorasExtras = 0;
            filtrarListaTurnosEmpleados = null;
            tipoListaHorasExtras = 0;
        }

        if (banderaDetalleHoraExtra == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
            detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
            detalleFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFechaPago");
            detalleFechaPago.setFilterStyle("display: none; visibility: hidden;");
            detalleHoras = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleHoras");
            detalleHoras.setFilterStyle("display: none; visibility: hidden;");
            detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleMinutos");
            detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
            detalleAprobado = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleAprobado");
            detalleAprobado.setFilterStyle("display: none; visibility: hidden;");
            detalleDescripcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleDescripcion");
            detalleDescripcion.setFilterStyle("display: none; visibility: hidden;");
            detalleOpcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleOpcion");
            detalleOpcion.setFilterStyle("display: none; visibility: hidden;");
            altoTablaDetalleHoraExtra = "70";
            RequestContext.getCurrentInstance().update("form:datosDetalle");
            banderaDetalleHoraExtra = 0;
            filtrarListaVWEstadosExtras = null;
            tipoListaDetalleHoraExtra = 0;
        }

        listaEmpleados = null;
        getListaEmpleados();
        listaTurnosEmpleados = null;
        listaVWEstadosExtras = null;

        indexEmpleado = -1;
        indexHorasExtras = -1;
        indexDetalleHoraExtra = -1;
        cualCeldaEmpleado = -1;
        cualCeldaHorasExtras = -1;
        cualCeldaDetalleHoraExtra = -1;
        banderaEmpleado = 0;
        banderaHorasExtras = 0;
        banderaDetalleHoraExtra = 0;
        tipoListaEmpleado = 0;
        tipoListaHorasExtras = 0;
        tipoListaDetalleHoraExtra = 0;
        altoTablaEmpleado = "70";
        altoTablaHorasExtras = "75";
        altoTablaDetalleHoraExtra = "70";

        guardado = true;
        secRegistro = null;
        aceptar = true;

        activarBuscar = false;
        activarMostrarTodos = true;

        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:PanelTotal");
    }

    public void salir() {

        if (banderaEmpleado == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            empleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmpleado:empleadoCodigo");
            empleadoCodigo.setFilterStyle("display: none; visibility: hidden;");
            empleadoNombre = (Column) c.getViewRoot().findComponent("form:datosEmpleado:empleadoNombre");
            empleadoNombre.setFilterStyle("display: none; visibility: hidden;");
            altoTablaEmpleado = "70";
            RequestContext.getCurrentInstance().update("form:datosCuadrilla");
            banderaEmpleado = 0;
            filtrarListaEmpleados = null;
            tipoListaEmpleado = 0;
        }
        if (banderaHorasExtras == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            horaExtraProcesado = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraProcesado");
            horaExtraProcesado.setFilterStyle("display: none; visibility: hidden;");
            horaExtraFechaInicial = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaInicial");
            horaExtraFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            horaExtraFechaFinal = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaFinal");
            horaExtraFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            horaExtraMotivo = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraMotivo");
            horaExtraMotivo.setFilterStyle("display: none; visibility: hidden;");
            horaExtraNHA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNHA");
            horaExtraNHA.setFilterStyle("display: none; visibility: hidden;");
            horaExtraNVA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNVA");
            horaExtraNVA.setFilterStyle("display: none; visibility: hidden;");
            horaExtraPagaVale = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraPagaVale");
            horaExtraPagaVale.setFilterStyle("display: none; visibility: hidden;");
            horaExtraEstructura = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraEstructura");
            horaExtraEstructura.setFilterStyle("display: none; visibility: hidden;");
            horaExtraComentario = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraComentario");
            horaExtraComentario.setFilterStyle("display: none; visibility: hidden;");
            altoTablaHorasExtras = "75";
            RequestContext.getCurrentInstance().update("form:datosHoraExtra");
            banderaHorasExtras = 0;
            filtrarListaTurnosEmpleados = null;
            tipoListaHorasExtras = 0;
        }

        if (banderaDetalleHoraExtra == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
            detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
            detalleFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFechaPago");
            detalleFechaPago.setFilterStyle("display: none; visibility: hidden;");
            detalleHoras = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleHoras");
            detalleHoras.setFilterStyle("display: none; visibility: hidden;");
            detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleMinutos");
            detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
            detalleAprobado = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleAprobado");
            detalleAprobado.setFilterStyle("display: none; visibility: hidden;");
            detalleDescripcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleDescripcion");
            detalleDescripcion.setFilterStyle("display: none; visibility: hidden;");
            detalleOpcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleOpcion");
            detalleOpcion.setFilterStyle("display: none; visibility: hidden;");
            altoTablaDetalleHoraExtra = "70";
            RequestContext.getCurrentInstance().update("form:datosDetalle");
            banderaDetalleHoraExtra = 0;
            filtrarListaVWEstadosExtras = null;
            tipoListaDetalleHoraExtra = 0;
        }

        listaEmpleados = null;
        listaTurnosEmpleados = null;
        listaVWEstadosExtras = null;

        indexEmpleado = -1;
        indexHorasExtras = -1;
        indexDetalleHoraExtra = -1;
        cualCeldaEmpleado = -1;
        cualCeldaHorasExtras = -1;
        cualCeldaDetalleHoraExtra = -1;

        banderaEmpleado = 0;
        banderaHorasExtras = 0;
        banderaDetalleHoraExtra = 0;
        tipoListaEmpleado = 0;
        tipoListaHorasExtras = 0;
        tipoListaDetalleHoraExtra = 0;

        altoTablaEmpleado = "70";
        altoTablaHorasExtras = "75";
        altoTablaDetalleHoraExtra = "70";

        guardado = true;
        secRegistro = null;
        aceptar = true;

        activarBuscar = false;
        activarMostrarTodos = true;
    }

    public void guardadoGeneral() {
        if (guardado == false) {
            guardarCambiosTurnoEmpleado();
        }
    }

    public void guardarCambiosTurnoEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (!listTurnosEmpleadosBorrar.isEmpty()) {
                administrarATHoraExtra.borrarTurnosEmpleados(listTurnosEmpleadosBorrar);
                listTurnosEmpleadosBorrar.clear();
            }
            if (!listTurnosEmpleadosCrear.isEmpty()) {
                administrarATHoraExtra.crearTurnosEmpleados(listTurnosEmpleadosCrear);
                listTurnosEmpleadosCrear.clear();
            }
            if (!listTurnosEmpleadosModificar.isEmpty()) {
                administrarATHoraExtra.editarTurnosEmpleados(listTurnosEmpleadosModificar);
                listTurnosEmpleadosModificar.clear();
            }
            listaTurnosEmpleados = null;
            indexEmpleado = indexEmpladoAux;
            getListaTurnosEmpleados();
            RequestContext.getCurrentInstance().update("form:datosHoraExtra");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            indexEmpleado = -1;
            indexHorasExtras = -1;
            secRegistro = null;
            FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos de Horas Extras con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        } catch (Exception e) {
            System.out.println("Error guardarCambiosTurnoEmpleado Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en el guardado de Horas Extras");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void dispararDialogoBuscarEmpleados() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (guardado == true) {
            RequestContext.getCurrentInstance().update("formEmpleado:EmpleadoDialogo");
            PrimefacesContextUI.ejecutar("PF('EmpleadoDialogo').show()");
        } else {
            PrimefacesContextUI.ejecutar("PF('confirmarGuardar').show()");
        }
    }

    public void mostrarTodosEmpleados() {
        if (guardado == true) {
            cancelarModificacion();
        } else {
            RequestContext.getCurrentInstance().execute("confirmarGuardar').show()");
        }
    }

    public void actualizarEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (banderaEmpleado == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            empleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmpleado:empleadoCodigo");
            empleadoCodigo.setFilterStyle("display: none; visibility: hidden;");
            empleadoNombre = (Column) c.getViewRoot().findComponent("form:datosEmpleado:empleadoNombre");
            empleadoNombre.setFilterStyle("display: none; visibility: hidden;");
            altoTablaEmpleado = "70";
            RequestContext.getCurrentInstance().update("form:datosCuadrilla");
            banderaEmpleado = 0;
            filtrarListaEmpleados = null;
            tipoListaEmpleado = 0;
        }
        if (banderaHorasExtras == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            horaExtraProcesado = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraProcesado");
            horaExtraProcesado.setFilterStyle("display: none; visibility: hidden;");
            horaExtraFechaInicial = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaInicial");
            horaExtraFechaInicial.setFilterStyle("display: none; visibility: hidden;");
            horaExtraFechaFinal = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaFinal");
            horaExtraFechaFinal.setFilterStyle("display: none; visibility: hidden;");
            horaExtraMotivo = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraMotivo");
            horaExtraMotivo.setFilterStyle("display: none; visibility: hidden;");
            horaExtraNHA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNHA");
            horaExtraNHA.setFilterStyle("display: none; visibility: hidden;");
            horaExtraNVA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNVA");
            horaExtraNVA.setFilterStyle("display: none; visibility: hidden;");
            horaExtraPagaVale = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraPagaVale");
            horaExtraPagaVale.setFilterStyle("display: none; visibility: hidden;");
            horaExtraEstructura = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraEstructura");
            horaExtraEstructura.setFilterStyle("display: none; visibility: hidden;");
            horaExtraComentario = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraComentario");
            horaExtraComentario.setFilterStyle("display: none; visibility: hidden;");
            altoTablaHorasExtras = "75";
            RequestContext.getCurrentInstance().update("form:datosHoraExtra");
            banderaHorasExtras = 0;
            filtrarListaTurnosEmpleados = null;
            tipoListaHorasExtras = 0;
        }

        if (banderaDetalleHoraExtra == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
            detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
            detalleFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFechaPago");
            detalleFechaPago.setFilterStyle("display: none; visibility: hidden;");
            detalleHoras = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleHoras");
            detalleHoras.setFilterStyle("display: none; visibility: hidden;");
            detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleMinutos");
            detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
            detalleAprobado = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleAprobado");
            detalleAprobado.setFilterStyle("display: none; visibility: hidden;");
            detalleDescripcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleDescripcion");
            detalleDescripcion.setFilterStyle("display: none; visibility: hidden;");
            detalleOpcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleOpcion");
            detalleOpcion.setFilterStyle("display: none; visibility: hidden;");
            altoTablaDetalleHoraExtra = "70";
            RequestContext.getCurrentInstance().update("form:datosDetalle");
            banderaDetalleHoraExtra = 0;
            filtrarListaVWEstadosExtras = null;
            tipoListaDetalleHoraExtra = 0;
        }

        listaEmpleados = null;
        listaEmpleados = new ArrayList<Empleados>();
        listaEmpleados.add(empleadoSeleccionado);

        filtrarLovEmpleados = null;
        empleadoSeleccionado = null;
        aceptar = true;
        indexEmpleado = -1;
        secRegistro = null;

        listaTurnosEmpleados = null;
        listaVWEstadosExtras = null;

        activarBuscar = true;
        activarMostrarTodos = false;

        RequestContext.getCurrentInstance().update("form:panelTotal");
        /*
        RequestContext.getCurrentInstance().update("formEmpleado:EmpleadoDialogo");
        RequestContext.getCurrentInstance().update("formEmpleado:lovEmpleado");
        RequestContext.getCurrentInstance().update("formEmpleado:aceptarE");*/
        context.reset("formEmpleado:lovEmpleado:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleado').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoDialogo').hide()");

    }

    public void cancelarCambioEmpleado() {
        filtrarLovEmpleados = null;
        empleadoSeleccionado = null;
        aceptar = true;
        indexEmpleado = -1;
        secRegistro = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formEmpleado:lovEmpleado:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEmpleado').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EmpleadoDialogo').hide()");
    }

    public void actualizarMotivoTurno() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaHorasExtras == 0) {
                listaTurnosEmpleados.get(indexHorasExtras).setMotivoturno(motivoSeleccionado);
                if (!listTurnosEmpleadosCrear.contains(listaTurnosEmpleados.get(indexHorasExtras))) {
                    if (listTurnosEmpleadosModificar.isEmpty()) {
                        listTurnosEmpleadosModificar.add(listaTurnosEmpleados.get(indexHorasExtras));
                    } else if (!listTurnosEmpleadosModificar.contains(listaTurnosEmpleados.get(indexHorasExtras))) {
                        listTurnosEmpleadosModificar.add(listaTurnosEmpleados.get(indexHorasExtras));
                    }
                }
            } else {
                filtrarListaTurnosEmpleados.get(indexHorasExtras).setMotivoturno(motivoSeleccionado);
                if (!listTurnosEmpleadosCrear.contains(filtrarListaTurnosEmpleados.get(indexHorasExtras))) {
                    if (listTurnosEmpleadosModificar.isEmpty()) {
                        listTurnosEmpleadosModificar.add(filtrarListaTurnosEmpleados.get(indexHorasExtras));
                    } else if (!listTurnosEmpleadosModificar.contains(filtrarListaTurnosEmpleados.get(indexHorasExtras))) {
                        listTurnosEmpleadosModificar.add(filtrarListaTurnosEmpleados.get(indexHorasExtras));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosHoraExtra");
            permitirIndexHoraExtra = true;
        } else if (tipoActualizacion == 1) {
            nuevaTurno.setMotivoturno(motivoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoTurnoHoraExtra");
        } else if (tipoActualizacion == 2) {
            duplicarTurno.setMotivoturno(motivoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoTurnoHoraExtra");
        }
        filtrarLovMotivosTurnos = null;
        motivoSeleccionado = null;
        aceptar = true;
        indexHorasExtras = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        /*
         RequestContext.getCurrentInstance().update("form:MotivoTurnoDialogo");
         RequestContext.getCurrentInstance().update("form:lovMotivoTurno");
         RequestContext.getCurrentInstance().update("form:aceptarMT");*/
        context.reset("form:lovMotivoTurno:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovMotivoTurno').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('MotivoTurnoDialogo').hide()");
    }

    public void cancelarCambioMotivoTurno() {
        filtrarLovMotivosTurnos = null;
        motivoSeleccionado = null;
        aceptar = true;
        indexHorasExtras = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndexHoraExtra = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovMotivoTurno:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovMotivoTurno').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('MotivoTurnoDialogo').hide()");
    }

    public void actualizarEstructura() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoListaHorasExtras == 0) {
                listaTurnosEmpleados.get(indexHorasExtras).setEstructuraaprueba(estructuraSeleccionada);
                if (!listTurnosEmpleadosCrear.contains(listaTurnosEmpleados.get(indexHorasExtras))) {
                    if (listTurnosEmpleadosModificar.isEmpty()) {
                        listTurnosEmpleadosModificar.add(listaTurnosEmpleados.get(indexHorasExtras));
                    } else if (!listTurnosEmpleadosModificar.contains(listaTurnosEmpleados.get(indexHorasExtras))) {
                        listTurnosEmpleadosModificar.add(listaTurnosEmpleados.get(indexHorasExtras));
                    }
                }
            } else {
                filtrarListaTurnosEmpleados.get(indexHorasExtras).setEstructuraaprueba(estructuraSeleccionada);
                if (!listTurnosEmpleadosCrear.contains(filtrarListaTurnosEmpleados.get(indexHorasExtras))) {
                    if (listTurnosEmpleadosModificar.isEmpty()) {
                        listTurnosEmpleadosModificar.add(filtrarListaTurnosEmpleados.get(indexHorasExtras));
                    } else if (!listTurnosEmpleadosModificar.contains(filtrarListaTurnosEmpleados.get(indexHorasExtras))) {
                        listTurnosEmpleadosModificar.add(filtrarListaTurnosEmpleados.get(indexHorasExtras));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("form:datosHoraExtra");
            permitirIndexHoraExtra = true;
        } else if (tipoActualizacion == 1) {
            nuevaTurno.setEstructuraaprueba(estructuraSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstructuraHoraExtra");
        } else if (tipoActualizacion == 2) {
            duplicarTurno.setEstructuraaprueba(estructuraSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructuraHoraExtra");
        }
        filtrarLovEstructuras = null;
        estructuraSeleccionada = null;
        aceptar = true;
        indexHorasExtras = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        /*
         RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
         RequestContext.getCurrentInstance().update("form:lovEstructura");
         RequestContext.getCurrentInstance().update("form:aceptarEA");*/
        context.reset("form:lovEstructura:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEstructura').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').hide()");
    }

    public void cancelarCambioEstructura() {
        filtrarLovEstructuras = null;
        estructuraSeleccionada = null;
        aceptar = true;
        indexHorasExtras = -1;
        secRegistro = null;
        tipoActualizacion = -1;
        permitirIndexHoraExtra = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEstructura:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEstructura').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').hide()");
    }

    public void eventoFiltrar() {
        if (indexEmpleado >= 0) {
            if (tipoListaEmpleado == 0) {
                tipoListaEmpleado = 1;
            }
        }
        if (indexHorasExtras >= 0) {
            if (tipoListaHorasExtras == 0) {
                tipoListaHorasExtras = 1;
            }
        }
        if (indexDetalleHoraExtra >= 0) {
            if (tipoListaDetalleHoraExtra == 0) {
                tipoListaDetalleHoraExtra = 1;
            }
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (indexEmpleado >= 0) {
            if (banderaEmpleado == 0) {
                empleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmpleado:empleadoCodigo");
                empleadoCodigo.setFilterStyle("width: 50px");
                empleadoNombre = (Column) c.getViewRoot().findComponent("form:datosEmpleado:empleadoNombre");
                empleadoNombre.setFilterStyle("width: 85%;");
                altoTablaEmpleado = "52";
                RequestContext.getCurrentInstance().update("form:datosCuadrilla");
                banderaEmpleado = 1;
            } else if (banderaEmpleado == 1) {
                //CERRAR FILTRADO
                empleadoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmpleado:empleadoCodigo");
                empleadoCodigo.setFilterStyle("display: none; visibility: hidden;");
                empleadoNombre = (Column) c.getViewRoot().findComponent("form:datosEmpleado:empleadoNombre");
                empleadoNombre.setFilterStyle("display: none; visibility: hidden;");
                altoTablaEmpleado = "70";
                RequestContext.getCurrentInstance().update("form:datosCuadrilla");
                banderaEmpleado = 0;
                filtrarListaEmpleados = null;
                tipoListaEmpleado = 0;
            }
        }
        if (indexHorasExtras >= 0) {
            if (banderaHorasExtras == 0) {
                horaExtraProcesado = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraProcesado");
                horaExtraProcesado.setFilterStyle("display: none; visibility: hidden;");
                horaExtraFechaInicial = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaInicial");
                horaExtraFechaInicial.setFilterStyle("width: 85%;");
                horaExtraFechaFinal = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaFinal");
                horaExtraFechaFinal.setFilterStyle("width: 85%;");
                horaExtraMotivo = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraMotivo");
                horaExtraMotivo.setFilterStyle("width: 85%;");
                horaExtraNHA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNHA");
                horaExtraNHA.setFilterStyle("width: 50px");
                horaExtraNVA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNVA");
                horaExtraNVA.setFilterStyle("width: 50px");
                horaExtraPagaVale = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraPagaVale");
                horaExtraPagaVale.setFilterStyle("display: none; visibility: hidden;");
                horaExtraEstructura = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraEstructura");
                horaExtraEstructura.setFilterStyle("width: 85%;");
                horaExtraComentario = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraComentario");
                horaExtraComentario.setFilterStyle("width: 85%;");
                altoTablaHorasExtras = "43";
                RequestContext.getCurrentInstance().update("form:datosHoraExtra");
                banderaHorasExtras = 1;
            } else if (banderaHorasExtras == 1) {
                horaExtraProcesado = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraProcesado");
                horaExtraProcesado.setFilterStyle("display: none; visibility: hidden;");
                horaExtraFechaInicial = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaInicial");
                horaExtraFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                horaExtraFechaFinal = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaFinal");
                horaExtraFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                horaExtraMotivo = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraMotivo");
                horaExtraMotivo.setFilterStyle("display: none; visibility: hidden;");
                horaExtraNHA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNHA");
                horaExtraNHA.setFilterStyle("display: none; visibility: hidden;");
                horaExtraNVA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNVA");
                horaExtraNVA.setFilterStyle("display: none; visibility: hidden;");
                horaExtraPagaVale = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraPagaVale");
                horaExtraPagaVale.setFilterStyle("display: none; visibility: hidden;");
                horaExtraEstructura = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraEstructura");
                horaExtraEstructura.setFilterStyle("display: none; visibility: hidden;");
                horaExtraComentario = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraComentario");
                horaExtraComentario.setFilterStyle("display: none; visibility: hidden;");
                altoTablaHorasExtras = "75";
                RequestContext.getCurrentInstance().update("form:datosHoraExtra");
                banderaHorasExtras = 0;
                filtrarListaTurnosEmpleados = null;
                tipoListaHorasExtras = 0;
            }
        }
        if (indexDetalleHoraExtra >= 0) {
            if (banderaDetalleHoraExtra == 0) {

                detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
                detalleConcepto.setFilterStyle("width: 85%;");
                detalleFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFechaPago");
                detalleFechaPago.setFilterStyle("width: 85%;");
                detalleHoras = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleHoras");
                detalleHoras.setFilterStyle("width: 85%;");
                detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleMinutos");
                detalleMinutos.setFilterStyle("width: 85%;");
                detalleAprobado = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleAprobado");
                detalleAprobado.setFilterStyle("width: 85%;");
                detalleDescripcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleDescripcion");
                detalleDescripcion.setFilterStyle("width: 85%;");
                detalleOpcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleOpcion");
                detalleOpcion.setFilterStyle("width: 85%;");
                altoTablaDetalleHoraExtra = "38";
                RequestContext.getCurrentInstance().update("form:datosDetalle");
                banderaHorasExtras = 1;
            } else if (banderaDetalleHoraExtra == 1) {
                detalleConcepto = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleConcepto");
                detalleConcepto.setFilterStyle("display: none; visibility: hidden;");
                detalleFechaPago = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleFechaPago");
                detalleFechaPago.setFilterStyle("display: none; visibility: hidden;");
                detalleHoras = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleHoras");
                detalleHoras.setFilterStyle("display: none; visibility: hidden;");
                detalleMinutos = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleMinutos");
                detalleMinutos.setFilterStyle("display: none; visibility: hidden;");
                detalleAprobado = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleAprobado");
                detalleAprobado.setFilterStyle("display: none; visibility: hidden;");
                detalleDescripcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleDescripcion");
                detalleDescripcion.setFilterStyle("display: none; visibility: hidden;");
                detalleOpcion = (Column) c.getViewRoot().findComponent("form:datosDetalle:detalleOpcion");
                detalleOpcion.setFilterStyle("display: none; visibility: hidden;");
                altoTablaDetalleHoraExtra = "70";
                RequestContext.getCurrentInstance().update("form:datosDetalle");
                banderaDetalleHoraExtra = 0;
                filtrarListaVWEstadosExtras = null;
                tipoListaDetalleHoraExtra = 0;
            }
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("MOTIVO")) {
            if (tipoNuevo == 1) {
                auxHoraExtraMotivo = nuevaTurno.getMotivoturno().getNombre();
            } else if (tipoNuevo == 2) {
                auxHoraExtraEstructura = duplicarTurno.getMotivoturno().getNombre();
            }
        }
        if (Campo.equals("ESTRUCTURA")) {
            if (tipoNuevo == 1) {
                auxHoraExtraEstructura = nuevaTurno.getEstructuraaprueba().getNombre();
            } else if (tipoNuevo == 2) {
                auxHoraExtraEstructura = duplicarTurno.getEstructuraaprueba().getNombre();
            }
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("MOTIVO")) {
            if (tipoNuevo == 1) {
                nuevaTurno.getMotivoturno().setNombre(auxHoraExtraMotivo);
            } else if (tipoNuevo == 2) {
                duplicarTurno.getMotivoturno().setNombre(auxHoraExtraMotivo);
            }
            for (int i = 0; i < lovMotivosTurnos.size(); i++) {
                if (lovMotivosTurnos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaTurno.setMotivoturno(lovMotivosTurnos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoTurnoHoraExtra");
                } else if (tipoNuevo == 2) {
                    duplicarTurno.setMotivoturno(lovMotivosTurnos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoTurnoHoraExtra");
                }
                lovMotivosTurnos.clear();
                getLovMotivosTurnos();
            } else {
                RequestContext.getCurrentInstance().update("form:MotivoTurnoDialogo");
                PrimefacesContextUI.ejecutar("PF('MotivoTurnoDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMotivoTurnoHoraExtra");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMotivoTurnoHoraExtra");
                }
            }
        }
        if (confirmarCambio.equalsIgnoreCase("ESTRUCTURA")) {
            if (tipoNuevo == 1) {
                nuevaTurno.getEstructuraaprueba().setNombre(auxHoraExtraEstructura);
            } else if (tipoNuevo == 2) {
                duplicarTurno.getEstructuraaprueba().setNombre(auxHoraExtraEstructura);
            }
            for (int i = 0; i < lovEstructuras.size(); i++) {
                if (lovEstructuras.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaTurno.setEstructuraaprueba(lovEstructuras.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstructuraHoraExtra");
                } else if (tipoNuevo == 2) {
                    duplicarTurno.setEstructuraaprueba(lovEstructuras.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructuraHoraExtra");
                }
                lovEstructuras.clear();
                getLovEstructuras();
            } else {
                RequestContext.getCurrentInstance().update("form:EstructuraDialogo");
                PrimefacesContextUI.ejecutar("PF('EstructuraDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEstructuraHoraExtra");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEstructuraHoraExtra");
                }
            }
        }
    }

    public void dispararDialogoNuevoRegistro() {
        int tam = 0;
        if (listaEmpleados != null) {
            tam = listaEmpleados.size();
        }
        if (tam > 0) {
            if (indexEmpladoAux >= 0) {
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroTurno");
                PrimefacesContextUI.ejecutar("PF('NuevoRegistroTurno').show()");
            }
        }
    }

    public void limpiarNuevaHoraExtra() {
        nuevaTurno = new TurnosEmpleados();
        nuevaTurno.setMotivoturno(new MotivosTurnos());
        nuevaTurno.setEstructuraaprueba(new Estructuras());
        indexHorasExtras = -1;
        secRegistro = null;
    }

    public void agregarNuevaExtra() {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean validarNull = validarCamposNullTurnosEmpleados(1);
        if (validarNull == true) {
            if (validarFechasRegistroTurno(1) == true) {
                if (banderaHorasExtras == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    horaExtraProcesado = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraProcesado");
                    horaExtraProcesado.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraFechaInicial = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaInicial");
                    horaExtraFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraFechaFinal = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaFinal");
                    horaExtraFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraMotivo = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraMotivo");
                    horaExtraMotivo.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraNHA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNHA");
                    horaExtraNHA.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraNVA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNVA");
                    horaExtraNVA.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraPagaVale = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraPagaVale");
                    horaExtraPagaVale.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraEstructura = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraEstructura");
                    horaExtraEstructura.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraComentario = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraComentario");
                    horaExtraComentario.setFilterStyle("display: none; visibility: hidden;");
                    altoTablaHorasExtras = "75";
                    RequestContext.getCurrentInstance().update("form:datosHoraExtra");
                    banderaHorasExtras = 0;
                    filtrarListaTurnosEmpleados = null;
                    tipoListaHorasExtras = 0;
                }

                k++;
                l = BigInteger.valueOf(k);
                nuevaTurno.setSecuencia(l);
                if (tipoListaEmpleado == 0) {
                    nuevaTurno.setEmpleado(listaEmpleados.get(indexEmpladoAux));
                } else {
                    nuevaTurno.setEmpleado(filtrarListaEmpleados.get(indexEmpladoAux));
                }
                listTurnosEmpleadosCrear.add(nuevaTurno);
                listaTurnosEmpleados.add(nuevaTurno);
                nuevaTurno = new TurnosEmpleados();
                nuevaTurno.setMotivoturno(new MotivosTurnos());
                nuevaTurno.setEstructuraaprueba(new Estructuras());
                RequestContext.getCurrentInstance().update("form:datosHoraExtra");
                PrimefacesContextUI.ejecutar("PF('NuevoRegistroTurno').hide()");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                indexHorasExtras = -1;
                secRegistro = null;
            } else {
                PrimefacesContextUI.ejecutar("PF('errorFechaTurno').show()");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('errorNullTurno').show()");
        }
    }

    public void duplicarRegistro() {
        if (indexHorasExtras >= 0) {
            if (tipoListaHorasExtras == 0) {
                duplicarTurno.setProcesado(listaTurnosEmpleados.get(indexHorasExtras).getProcesado());
                duplicarTurno.setFechainicial(listaTurnosEmpleados.get(indexHorasExtras).getFechainicial());
                duplicarTurno.setFechafinal(listaTurnosEmpleados.get(indexHorasExtras).getFechafinal());
                duplicarTurno.setMotivoturno(listaTurnosEmpleados.get(indexHorasExtras).getMotivoturno());
                duplicarTurno.setDescuentahorasalimentacion(listaTurnosEmpleados.get(indexHorasExtras).getDescuentahorasalimentacion());
                duplicarTurno.setPagavalesalimentacion(listaTurnosEmpleados.get(indexHorasExtras).getPagavalesalimentacion());
                duplicarTurno.setPagasolovale(listaTurnosEmpleados.get(indexHorasExtras).getPagasolovale());
                duplicarTurno.setEstructuraaprueba(listaTurnosEmpleados.get(indexHorasExtras).getEstructuraaprueba());
                duplicarTurno.setComentario(listaTurnosEmpleados.get(indexHorasExtras).getComentario());
            } else {
                duplicarTurno.setProcesado(filtrarListaTurnosEmpleados.get(indexHorasExtras).getProcesado());
                duplicarTurno.setFechainicial(filtrarListaTurnosEmpleados.get(indexHorasExtras).getFechainicial());
                duplicarTurno.setFechafinal(filtrarListaTurnosEmpleados.get(indexHorasExtras).getFechafinal());
                duplicarTurno.setMotivoturno(filtrarListaTurnosEmpleados.get(indexHorasExtras).getMotivoturno());
                duplicarTurno.setDescuentahorasalimentacion(filtrarListaTurnosEmpleados.get(indexHorasExtras).getDescuentahorasalimentacion());
                duplicarTurno.setPagavalesalimentacion(filtrarListaTurnosEmpleados.get(indexHorasExtras).getPagavalesalimentacion());
                duplicarTurno.setPagasolovale(filtrarListaTurnosEmpleados.get(indexHorasExtras).getPagasolovale());
                duplicarTurno.setEstructuraaprueba(filtrarListaTurnosEmpleados.get(indexHorasExtras).getEstructuraaprueba());
                duplicarTurno.setComentario(filtrarListaTurnosEmpleados.get(indexHorasExtras).getComentario());
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroTurno");
            PrimefacesContextUI.ejecutar("PF('DuplicarRegistroTurno').show()");
        }
    }

    public void limpiarDuplicarHoraExtra() {
        nuevaTurno = new TurnosEmpleados();
        nuevaTurno.setMotivoturno(new MotivosTurnos());
        nuevaTurno.setEstructuraaprueba(new Estructuras());
        indexHorasExtras = -1;
        secRegistro = null;
    }

    public void confirmarDuplicarHoraExtra() {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean validarNull = validarCamposNullTurnosEmpleados(2);
        if (validarNull == true) {
            if (validarFechasRegistroTurno(2) == true) {
                if (banderaHorasExtras == 1) {
                    FacesContext c = FacesContext.getCurrentInstance();
                    horaExtraProcesado = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraProcesado");
                    horaExtraProcesado.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraFechaInicial = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaInicial");
                    horaExtraFechaInicial.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraFechaFinal = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraFechaFinal");
                    horaExtraFechaFinal.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraMotivo = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraMotivo");
                    horaExtraMotivo.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraNHA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNHA");
                    horaExtraNHA.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraNVA = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraNVA");
                    horaExtraNVA.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraPagaVale = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraPagaVale");
                    horaExtraPagaVale.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraEstructura = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraEstructura");
                    horaExtraEstructura.setFilterStyle("display: none; visibility: hidden;");
                    horaExtraComentario = (Column) c.getViewRoot().findComponent("form:datosHoraExtra:horaExtraComentario");
                    horaExtraComentario.setFilterStyle("display: none; visibility: hidden;");
                    altoTablaHorasExtras = "75";
                    RequestContext.getCurrentInstance().update("form:datosHoraExtra");
                    banderaHorasExtras = 0;
                    filtrarListaTurnosEmpleados = null;
                    tipoListaHorasExtras = 0;
                }

                k++;
                l = BigInteger.valueOf(k);
                duplicarTurno.setSecuencia(l);
                if (tipoListaEmpleado == 0) {
                    duplicarTurno.setEmpleado(listaEmpleados.get(indexEmpladoAux));
                } else {
                    duplicarTurno.setEmpleado(filtrarListaEmpleados.get(indexEmpladoAux));
                }
                listTurnosEmpleadosCrear.add(duplicarTurno);
                listaTurnosEmpleados.add(duplicarTurno);
                duplicarTurno = new TurnosEmpleados();
                duplicarTurno.setMotivoturno(new MotivosTurnos());
                duplicarTurno.setEstructuraaprueba(new Estructuras());
                RequestContext.getCurrentInstance().update("form:datosHoraExtra");
                PrimefacesContextUI.ejecutar("PF('DuplicarRegistroTurno').hide()");
                if (guardado == true) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
                indexHorasExtras = -1;
                secRegistro = null;
            } else {
                PrimefacesContextUI.ejecutar("PF('errorFechaTurno').show()");
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('errorNullTurno').show()");
        }
    }

    public void exportPDF() throws IOException {
        if (indexEmpleado >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosEmpleadoExportar");
            FacesContext context = c;
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "Empleados_PDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexEmpleado = -1;
            secRegistro = null;
        }
        if (indexHorasExtras >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosHoraExtraExportar");
            FacesContext context = c;
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "HorasExtras_PDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexHorasExtras = -1;
            secRegistro = null;
        }
        if (indexDetalleHoraExtra >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosDetalleExportar");
            FacesContext context = c;
            Exporter exporter = new ExportarPDF();
            exporter.export(context, tabla, "DetallesHorasExtrasProcesadas_PDF", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexDetalleHoraExtra = -1;
            secRegistro = null;
        }
    }

    public void exportXLS() throws IOException {
        if (indexEmpleado >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosEmpleadoExportar");
            FacesContext context = c;
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "Empleados_XLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexEmpleado = -1;
            secRegistro = null;
        }
        if (indexHorasExtras >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosHoraExtraExportar");
            FacesContext context = c;
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "HorasExtras_XLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexHorasExtras = -1;
            secRegistro = null;
        }
        if (indexDetalleHoraExtra >= 0) {
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("formExportar:datosDetalleExportar");
            FacesContext context = c;
            Exporter exporter = new ExportarXLS();
            exporter.export(context, tabla, "DetallesHorasExtrasProcesadas_XLS", false, false, "UTF-8", null, null);
            context.responseComplete();
            indexDetalleHoraExtra = -1;
            secRegistro = null;
        }
    }

    public String obtenerTablaXML() {
        if (indexEmpleado >= 0) {
            nombreTablaXML = ":formExportar:datosEmpleadoExportar";
        }
        if (indexHorasExtras >= 0) {
            nombreTablaXML = ":formExportar:datosHoraExtraExportar";
        }
        if (indexDetalleHoraExtra >= 0) {
            nombreTablaXML = ":formExportar:datosDetalleExportar";
        }
        return nombreTablaXML;
    }

    public String obtenerNombreArchivoXML() {
        if (indexEmpleado >= 0) {
            nombreArchivoXML = "Empleados_XML";
        }
        if (indexHorasExtras >= 0) {
            nombreArchivoXML = "HorasExtras_XML";
        }
        if (indexDetalleHoraExtra >= 0) {
            nombreArchivoXML = "DetallesHorasExtrasProcesadas_XML";
        }
        return nombreArchivoXML;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void validarRastro() {
        if (indexEmpleado >= 0) {
            verificarRastroEmpleado();
        }
        if (indexHorasExtras >= 0) {
            verificarRastroHoraExtra();
        }
        if (indexDetalleHoraExtra >= 0) {
            RequestContext.getCurrentInstance().execute("errorTablaSinRastro').show()");
        }
    }

    public void verificarRastroEmpleado() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaEmpleados != null) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "EMPLEADOS");
                backUpSecRegistro = secRegistro;
                secRegistro = null;
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
            if (administrarRastros.verificarHistoricosTabla("EMPLEADOS")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        indexEmpleado = -1;
    }

    public void verificarRastroHoraExtra() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaTurnosEmpleados != null) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "TURNOSEMPLEADOS");
                backUpSecRegistro = secRegistro;
                secRegistro = null;
                if (resultado == 1) {
                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB_HE').show()");
                } else if (resultado == 2) {
                    PrimefacesContextUI.ejecutar("PF('confirmarRastro_HE').show()");
                } else if (resultado == 3) {
                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
                } else if (resultado == 4) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastro_HE').show()");
                } else if (resultado == 5) {
                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("TURNOSEMPLEADOS")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico_HE').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        indexHorasExtras = -1;
    }

    public void guardarSalir() {
        guardadoGeneral();
        salir();
    }

    public void cancelarSalir() {
        cancelarModificacion();
        salir();
    }

    public List<Empleados> getListaEmpleados() {
        try {
            if (listaEmpleados == null) {
                listaEmpleados = administrarATHoraExtra.buscarEmpleados();
                if (listaEmpleados != null) {
                    if (listaEmpleados.size() > 0) {
                        empleadoActual = listaEmpleados.get(0);
                    }
                }
            }
            return listaEmpleados;
        } catch (Exception e) {
            System.out.println("Error listaEmpleados !!! Controlador : " + e.toString());
            return null;
        }
    }

    public void setListaEmpleados(List<Empleados> listaEmpleados) {
        this.listaEmpleados = listaEmpleados;
    }

    public Empleados getEmpleadoActual() {
        return empleadoActual;
    }

    public void setEmpleadoActual(Empleados empleadoActual) {
        this.empleadoActual = empleadoActual;
    }

    public List<Empleados> getLovEmpleados() {
        lovEmpleados = administrarATHoraExtra.buscarEmpleados();
        if (lovEmpleados != null) {
            infoRegistroEmpleado = "Cantidad de registros : " + lovEmpleados.size();
        } else {
            infoRegistroEmpleado = "Cantidad de registros : 0";
        }
        return lovEmpleados;
    }

    public void setLovEmpleados(List<Empleados> lovEmpleados) {
        this.lovEmpleados = lovEmpleados;
    }

    public List<Empleados> getFiltrarLovEmpleados() {
        return filtrarLovEmpleados;
    }

    public void setFiltrarLovEmpleados(List<Empleados> filtrarLovEmpleados) {
        this.filtrarLovEmpleados = filtrarLovEmpleados;
    }

    public Empleados getEmpleadoSeleccionado() {
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public String getInfoRegistroEmpleado() {
        return infoRegistroEmpleado;
    }

    public void setInfoRegistroEmpleado(String infoRegistroEmpleado) {
        this.infoRegistroEmpleado = infoRegistroEmpleado;
    }

    public String getAltoTablaEmpleado() {
        return altoTablaEmpleado;
    }

    public void setAltoTablaEmpleado(String altoTablaEmpleado) {
        this.altoTablaEmpleado = altoTablaEmpleado;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public BigInteger getSecRegistro() {
        return secRegistro;
    }

    public void setSecRegistro(BigInteger secRegistro) {
        this.secRegistro = secRegistro;
    }

    public BigInteger getBackUpSecRegistro() {
        return backUpSecRegistro;
    }

    public void setBackUpSecRegistro(BigInteger backUpSecRegistro) {
        this.backUpSecRegistro = backUpSecRegistro;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<Empleados> getFiltrarListaEmpleados() {
        return filtrarListaEmpleados;
    }

    public void setFiltrarListaEmpleados(List<Empleados> filtrarListaEmpleados) {
        this.filtrarListaEmpleados = filtrarListaEmpleados;
    }

    public List<TurnosEmpleados> getListaTurnosEmpleados() {
        if (indexEmpleado >= 0) {
            if (listaTurnosEmpleados == null) {
                listaTurnosEmpleados = administrarATHoraExtra.buscarTurnosEmpleadosPorEmpleado(empleadoActual.getSecuencia());
            }
        }
        return listaTurnosEmpleados;
    }

    public void setListaTurnosEmpleados(List<TurnosEmpleados> listaTurnosEmpleados) {
        this.listaTurnosEmpleados = listaTurnosEmpleados;
    }

    public List<TurnosEmpleados> getFiltrarListaTurnosEmpleados() {
        return filtrarListaTurnosEmpleados;
    }

    public void setFiltrarListaTurnosEmpleados(List<TurnosEmpleados> filtrarListaTurnosEmpleados) {
        this.filtrarListaTurnosEmpleados = filtrarListaTurnosEmpleados;
    }

    public TurnosEmpleados getTurnoEmpleadoSeleccionado() {
        return turnoEmpleadoSeleccionado;
    }

    public void setTurnoEmpleadoSeleccionado(TurnosEmpleados turnoEmpleadoSeleccionado) {
        this.turnoEmpleadoSeleccionado = turnoEmpleadoSeleccionado;
    }

    public List<MotivosTurnos> getLovMotivosTurnos() {
        lovMotivosTurnos = administrarATHoraExtra.lovMotivosTurnos();
        if (lovMotivosTurnos != null) {
            infoRegistroMotivo = "Cantidad de registros : " + lovMotivosTurnos.size();
        } else {
            infoRegistroMotivo = "Cantidad de registros : 0";
        }
        return lovMotivosTurnos;
    }

    public void setLovMotivosTurnos(List<MotivosTurnos> lovMotivosTurnos) {
        this.lovMotivosTurnos = lovMotivosTurnos;
    }

    public List<MotivosTurnos> getFiltrarLovMotivosTurnos() {
        return filtrarLovMotivosTurnos;
    }

    public void setFiltrarLovMotivosTurnos(List<MotivosTurnos> filtrarLovMotivosTurnos) {
        this.filtrarLovMotivosTurnos = filtrarLovMotivosTurnos;
    }

    public MotivosTurnos getMotivoSeleccionado() {
        return motivoSeleccionado;
    }

    public void setMotivoSeleccionado(MotivosTurnos motivoSeleccionado) {
        this.motivoSeleccionado = motivoSeleccionado;
    }

    public List<Estructuras> getLovEstructuras() {
        lovEstructuras = administrarATHoraExtra.lovEstructuras();
        if (lovEstructuras != null) {
            infoRegistroEstructura = "Cantidad de registros : " + lovEstructuras.size();
        } else {
            infoRegistroEstructura = "Cantidad de registros : 0";
        }
        return lovEstructuras;
    }

    public void setLovEstructuras(List<Estructuras> lovEstructuras) {
        this.lovEstructuras = lovEstructuras;
    }

    public List<Estructuras> getFiltrarLovEstructuras() {
        return filtrarLovEstructuras;
    }

    public void setFiltrarLovEstructuras(List<Estructuras> filtrarLovEstructuras) {
        this.filtrarLovEstructuras = filtrarLovEstructuras;
    }

    public Estructuras getEstructuraSeleccionada() {
        return estructuraSeleccionada;
    }

    public void setEstructuraSeleccionada(Estructuras estructuraSeleccionada) {
        this.estructuraSeleccionada = estructuraSeleccionada;
    }

    public TurnosEmpleados getNuevaTurno() {
        return nuevaTurno;
    }

    public void setNuevaTurno(TurnosEmpleados nuevaTurno) {
        this.nuevaTurno = nuevaTurno;
    }

    public TurnosEmpleados getDuplicarTurno() {
        return duplicarTurno;
    }

    public void setDuplicarTurno(TurnosEmpleados duplicarTurno) {
        this.duplicarTurno = duplicarTurno;
    }

    public TurnosEmpleados getEditarTurnoEmpleado() {
        return editarTurnoEmpleado;
    }

    public void setEditarTurnoEmpleado(TurnosEmpleados editarTurnoEmpleado) {
        this.editarTurnoEmpleado = editarTurnoEmpleado;
    }

    public String getAltoTablaHorasExtras() {
        return altoTablaHorasExtras;
    }

    public void setAltoTablaHorasExtras(String altoTablaHorasExtras) {
        this.altoTablaHorasExtras = altoTablaHorasExtras;
    }

    public String getInfoRegistroMotivo() {
        return infoRegistroMotivo;
    }

    public void setInfoRegistroMotivo(String infoRegistroMotivo) {
        this.infoRegistroMotivo = infoRegistroMotivo;
    }

    public String getInfoRegistroEstructura() {
        return infoRegistroEstructura;
    }

    public void setInfoRegistroEstructura(String infoRegistroEstructura) {
        this.infoRegistroEstructura = infoRegistroEstructura;
    }

    public List<VWEstadosExtras> getListaVWEstadosExtras() {
        if (indexHorasExtras >= 0) {
            if (listaVWEstadosExtras == null) {
                listaVWEstadosExtras = administrarATHoraExtra.buscarDetallesHorasExtrasPorTurnoEmpleado(secRegistro);
                if (listaVWEstadosExtras != null) {
                    totalHoras = 0;
                    totalMinutos = 0;
                    for (int i = 0; i < listaVWEstadosExtras.size(); i++) {
                        totalHoras = totalHoras + listaVWEstadosExtras.get(i).getHoras();
                        totalMinutos = totalMinutos + listaVWEstadosExtras.get(i).getMinutos();
                    }
                }
            }
        }
        return listaVWEstadosExtras;
    }

    public void setListaVWEstadosExtras(List<VWEstadosExtras> listaVWEstadosExtras) {
        this.listaVWEstadosExtras = listaVWEstadosExtras;
    }

    public List<VWEstadosExtras> getFiltrarListaVWEstadosExtras() {
        return filtrarListaVWEstadosExtras;
    }

    public void setFiltrarListaVWEstadosExtras(List<VWEstadosExtras> filtrarListaVWEstadosExtras) {
        this.filtrarListaVWEstadosExtras = filtrarListaVWEstadosExtras;
    }

    public VWEstadosExtras getDetalleSeleccionado() {
        return detalleSeleccionado;
    }

    public void setDetalleSeleccionado(VWEstadosExtras detalleSeleccionado) {
        this.detalleSeleccionado = detalleSeleccionado;
    }

    public VWEstadosExtras getEditarDetalleTurno() {
        return editarDetalleTurno;
    }

    public void setEditarDetalleTurno(VWEstadosExtras editarDetalleTurno) {
        this.editarDetalleTurno = editarDetalleTurno;
    }

    public String getAltoTablaDetalleHoraExtra() {
        return altoTablaDetalleHoraExtra;
    }

    public void setAltoTablaDetalleHoraExtra(String altoTablaDetalleHoraExtra) {
        this.altoTablaDetalleHoraExtra = altoTablaDetalleHoraExtra;
    }

    public int getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(int totalHoras) {
        this.totalHoras = totalHoras;
    }

    public int getTotalMinutos() {
        return totalMinutos;
    }

    public void setTotalMinutos(int totalMinutos) {
        this.totalMinutos = totalMinutos;
    }

    public boolean isActivarBuscar() {
        return activarBuscar;
    }

    public void setActivarBuscar(boolean activarBuscar) {
        this.activarBuscar = activarBuscar;
    }

    public boolean isActivarMostrarTodos() {
        return activarMostrarTodos;
    }

    public void setActivarMostrarTodos(boolean activarMostrarTodos) {
        this.activarMostrarTodos = activarMostrarTodos;
    }

}
