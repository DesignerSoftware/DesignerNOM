/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarVigenciasAfiliaciones3Interface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
import utilidadesUI.PrimefacesContextUI;
import Entidades.VigenciasAfiliaciones;
import Entidades.Empleados;
import Entidades.TiposEntidades;
import Entidades.Terceros;
import Entidades.EstadosAfiliaciones;
import Entidades.TercerosSucursales;

/**
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlEmplVigenciaAfiliacion3 implements Serializable {

    @EJB
    AdministrarVigenciasAfiliaciones3Interface administrarVigenciasAfiliaciones3;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    //Vigencias Afiliaciones
    private List<VigenciasAfiliaciones> listVigenciasAfiliaciones;
    private List<VigenciasAfiliaciones> filtrarVigenciasAfiliaciones;
    private VigenciasAfiliaciones vigenciaSeleccionada;
    //Tipos Entidades
    private List<TiposEntidades> listTiposEntidades;
    private TiposEntidades tipoEntidadSeleccionado;
    private List<TiposEntidades> filtrarTiposEntidades;
    //Terceros
    private List<Terceros> listTerceros;
    private Terceros terceroSeleccionado;
    private List<Terceros> filtrarTerceros;
    //EstadosAfiliaciones
    private List<EstadosAfiliaciones> listEstadosAfiliaciones;
    private EstadosAfiliaciones estadoSSeleccionado;
    private List<EstadosAfiliaciones> filtrarEstadosAfiliaciones;
    //Empleado
    private Empleados empleado;
    //Tipo Actualizacion
    private int tipoActualizacion;
    //Autocompletar
    private boolean permitirIndexVA;
    //Activo/Desactivo VP Crtl + F11
    private int banderaVA;
    //Columnas Tabla VP
    private Column vAFechaInicial, vAFechaFinal, vATercero, vATipoEntidad, vANITTercero, vACodigo, vAEstadoAfiliacion, vAObservaciones;
    //Otros
    private boolean aceptar;
    //modificar
    private List<VigenciasAfiliaciones> listVAModificar;
    private boolean guardado, guardarOk;
    //crear VA
    public VigenciasAfiliaciones nuevaVigenciaA;
    private List<VigenciasAfiliaciones> listVACrear;
    private BigInteger l;
    private int k;
    //borrar VA
    private List<VigenciasAfiliaciones> listVABorrar;
    //editar celda
    private VigenciasAfiliaciones editarVA;
    private boolean cambioEditor, aceptarEditar;
    //Indice de celdas VigenciaProrrateo / VigenciaProrrateoProyecto
    private int cualCeldaVA, tipoListaVA;
    //duplicar
    private VigenciasAfiliaciones duplicarVA;
    //Variables Autompletar
    private String tiposEntidades, terceros, estadosAfiliacion;
    private long nit;
    //Duplicar Vigencia Prorrateo
    private boolean cambioVigenciaA;
    private VigenciasAfiliaciones vigenciaValidaciones;
    private Date fechaContratacion;
    private Date fechaParametro;
    private Date fechaIni, fechaFin;
    //ALTO TABLA
    private String altoTabla;
    private String infoRegistro;
    //RASTRO
    private String infoRegistroTipoEntidad, infoRegistroTercero, infoRegistroEstado;
    //Para validaciones de tipo entidades y terceros
    private int tipoValidacion;
    //
    private DataTable tablaC;
    //
    private boolean activarLOV;

    public ControlEmplVigenciaAfiliacion3() {
        tipoValidacion = 0;
        empleado = new Empleados();
        //Otros
        aceptar = true;
        //borrar aficiones
        listVABorrar = new ArrayList<VigenciasAfiliaciones>();
        //crear aficiones
        k = 0;
        //modificar aficiones
        listVAModificar = new ArrayList<VigenciasAfiliaciones>();
        //editar
        editarVA = new VigenciasAfiliaciones();
        cambioEditor = false;
        aceptarEditar = true;
        tipoListaVA = 0;
        //guardar
        guardado = true;
        banderaVA = 0;
        permitirIndexVA = true;
        //cualCeldaVA = 0;
        listEstadosAfiliaciones = null;
        estadoSSeleccionado = new EstadosAfiliaciones();
        listTerceros = null;
        //terceroSeleccionado = new Terceros();
        terceroSeleccionado = null;
        tipoEntidadSeleccionado = null;
        estadoSSeleccionado = null;

        listTiposEntidades = null;
        tipoEntidadSeleccionado = new TiposEntidades();
        nuevaVigenciaA = new VigenciasAfiliaciones();
        nuevaVigenciaA.setTipoentidad(new TiposEntidades());
        nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
        nuevaVigenciaA.setEstadoafiliacion(new EstadosAfiliaciones());
        listVACrear = new ArrayList<VigenciasAfiliaciones>();
        fechaContratacion = new Date();
        cambioVigenciaA = false;
        altoTabla = "292";
        vigenciaSeleccionada = null;
        activarLOV = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarVigenciasAfiliaciones3.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    //EMPLEADO DE LA VIGENCIA 
    /**
     * Metodo que recibe la secuencia empleado desde la pagina anterior y
     * obtiene el empleado referenciado
     *
     * @param empl Secuencia del Empleado
     */
    public void recibirEmpleado(BigInteger empl) {
        empleado = administrarVigenciasAfiliaciones3.obtenerEmpleado(empl);
        fechaContratacion = administrarVigenciasAfiliaciones3.fechaContratacion(empleado);
        getListVigenciasAfiliaciones();
        contarRegistrosVA();
        if (listVigenciasAfiliaciones != null) {
            vigenciaSeleccionada = listVigenciasAfiliaciones.get(0);
            modificarInfoRegistro(listVigenciasAfiliaciones.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * Modifica los elementos de la tabla VigenciaProrrateo que no usan
     * autocomplete
     *
     * @param indice Fila donde se efectu el cambio
     */
    public void modificarVA(VigenciasAfiliaciones va) {
        RequestContext context = RequestContext.getCurrentInstance();
        vigenciaSeleccionada = va;
        vigenciaValidaciones = vigenciaSeleccionada;
        boolean validacion = true;
        if (cualCeldaVA != 0 && cualCeldaVA != 1) {
            validacion = validarModificacionRegistroTabla();
        }
        if (validacion == true) {
            if (!listVACrear.contains(vigenciaSeleccionada)) {
                if (listVAModificar.isEmpty()) {
                    listVAModificar.add(vigenciaSeleccionada);
                } else if (!listVAModificar.contains(vigenciaSeleccionada)) {
                    listVAModificar.add(vigenciaSeleccionada);
                }
                if (guardado) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
            }
            vigenciaSeleccionada.getTercerosucursal().getTercero().setNit(nit);
            cambioVigenciaA = true;
        } else {
            VigenciasAfiliaciones cambio = administrarVigenciasAfiliaciones3.vigenciaAfiliacionSecuencia(vigenciaSeleccionada.getSecuencia());
            listVigenciasAfiliaciones.set(listVigenciasAfiliaciones.indexOf(vigenciaSeleccionada), cambio);
        }

        PrimefacesContextUI.actualizar("form:datosVAVigencia");
    }

    public boolean validarFechasRegistro(int i) {
        fechaParametro = new Date();
        fechaParametro.setYear(0);
        fechaParametro.setMonth(1);
        fechaParametro.setDate(1);
        boolean retorno = true;
        System.out.println("ControlEmplVigenciaAfiliacion3.validarFechasRegistro");
        System.out.println("Valor i: " + i);
        if (i == 0) {
            VigenciasAfiliaciones auxiliar;
            auxiliar = vigenciaSeleccionada;
            System.out.println("Auxiliar: " + auxiliar);
            retorno = auxiliar.getFechainicial().after(fechaParametro);
            if (auxiliar.getFechafinal() != null && retorno) {
                retorno = auxiliar.getFechainicial().before(auxiliar.getFechafinal());
            }
        }
        if (i == 1) {
            retorno = nuevaVigenciaA.getFechainicial().after(fechaParametro);
            if (nuevaVigenciaA.getFechafinal() != null && retorno) {
                retorno = nuevaVigenciaA.getFechainicial().before(nuevaVigenciaA.getFechafinal());
            }
            if (retorno) {
                retorno = nuevaVigenciaA.getFechainicial().after(fechaParametro);
            }
        }
        if (i == 2) {
            retorno = duplicarVA.getFechainicial().after(fechaParametro);
            if (duplicarVA.getFechafinal() != null && retorno) {
                retorno = duplicarVA.getFechainicial().before(duplicarVA.getFechafinal());
            }
            if (duplicarVA.getFechafinal() == null && retorno) {
                retorno = duplicarVA.getFechainicial().after(fechaParametro);
            }
        }
        System.out.println("Valor retorno: " + retorno);
        return retorno;
    }

    public void modificarFechas(VigenciasAfiliaciones va, int c) {
        System.out.println("ControlEmplVigenciaAfiliacion.modificarFechas");
        vigenciaSeleccionada = va;
        activarLOV = true;
        if (vigenciaSeleccionada.getFechainicial() != null) {
            boolean retorno = validarFechasRegistro(0);
            if (retorno) {
                if (validarFechasRegistroTabla()) {
                    System.out.println("FechasRegistroTabla true");
                    cambiarIndiceVA(vigenciaSeleccionada, c);
                    modificarVA(vigenciaSeleccionada);
                }
                System.out.println("retorno true");
            } else {
                vigenciaSeleccionada.setFechafinal(fechaFin);
                vigenciaSeleccionada.setFechainicial(fechaIni);

                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:datosVAVigencia");
                PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
            }
        } else {
            vigenciaSeleccionada.setFechainicial(fechaIni);
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosVAVigencia");
            PrimefacesContextUI.ejecutar("PF('errorRegNew').show()");
        }
    }

    public void modificarFechasFinales(VigenciasAfiliaciones vAfiliacion, int c) {
        System.out.println("ControlEmplVigenciaAfiliacion.modificarFechas");
        vigenciaSeleccionada = vAfiliacion;
        activarLOV = true;
        if (vigenciaSeleccionada.getFechainicial() != null) {
            boolean retorno = validarFechasRegistro(0);
            if (retorno) {
                boolean seModifica = false;
                if (fechaFin == null && vigenciaSeleccionada.getFechafinal() == null) {
                    seModifica = false;
                } else if (fechaFin == null && vigenciaSeleccionada.getFechafinal() != null) {
                    seModifica = true;
                } else {
                    if (fechaFin.equals(vigenciaSeleccionada.getFechafinal())) {
                        seModifica = false;
                    } else {
                        seModifica = true;
                    }
                }
                if (!seModifica) {
                    cambiarIndiceVA(vAfiliacion, c);
                } else {
                    cambiarIndiceVA(vAfiliacion, c);
                    modificarVA(vAfiliacion);
                }
            } else {
                vigenciaSeleccionada.setFechafinal(fechaFin);
                vigenciaSeleccionada.setFechainicial(fechaIni);

                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.actualizar("form:datosVAVigencia");
                PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
            }
        } else {
            vigenciaSeleccionada.setFechainicial(fechaIni);
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosVAVigencia");
            PrimefacesContextUI.ejecutar("PF('errorRegNew').show()");
        }
    }

    /**
     * Metodo que modifica los cambios efectuados en la tabla VigenciaProrrateo
     * de la pagina
     *
     * @param indice Fila en la cual se realizo el cambio
     * @param confirmarCambio Activa la validación de modificación
     * @param valor Nombre del tercero o de la entidad a confirmar
     */
    public void modificarVA(VigenciasAfiliaciones va, String confirmarCambio, String valor) {
        System.out.println("va" + va);
        vigenciaSeleccionada = va;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");

            vigenciaSeleccionada.getTipoentidad().setNombre(tiposEntidades);
            for (int i = 0; i < listTiposEntidades.size(); i++) {
                if (listTiposEntidades.get(i).getNombre().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaValidaciones = new VigenciasAfiliaciones();
                vigenciaValidaciones.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));
                //vigenciaSeleccionada.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));
                vigenciaSeleccionada = va;
                boolean cambio = validarModificacionRegistroTabla();
                if (cambio == false) {
                    cambioVigenciaA = false;
                    vigenciaSeleccionada.getTipoentidad().setNombre(tiposEntidades);
                } else {
                    cambioVigenciaA = true;
                    vigenciaSeleccionada.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));
                }

                listTiposEntidades = null;
                getListTiposEntidades();
            } else {
                permitirIndexVA = false;
                getInfoRegistroTipoEntidad();
                PrimefacesContextUI.actualizar("form:TipoEntidadDialogo");
                PrimefacesContextUI.ejecutar("PF('TipoEntidadDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("TERCEROS")) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            vigenciaSeleccionada.getTercerosucursal().getTercero().setNombre(terceros);

            for (int i = 0; i < listTerceros.size(); i++) {
                if (listTerceros.get(i).getNombre().startsWith(valor.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaValidaciones = new VigenciasAfiliaciones();
                vigenciaValidaciones.setTercerosucursal(new TercerosSucursales());
                vigenciaValidaciones.getTercerosucursal().setTercero(new Terceros());
                vigenciaValidaciones.getTercerosucursal().setTercero(listTerceros.get(indiceUnicoElemento));
                boolean cambio = validarModificacionRegistroTabla();
                if (cambio == false) {
                    cambioVigenciaA = false;
                    vigenciaSeleccionada.getTercerosucursal().getTercero().setNombre(terceros);
                } else {
                    cambioVigenciaA = true;
                    boolean banderaEncuentra = false;
                    int posicion = -1;
                    List<TercerosSucursales> listTercerosSucursales = administrarVigenciasAfiliaciones3.listTercerosSucursales();
                    for (int i = 0; i < listTercerosSucursales.size(); i++) {
                        if (listTercerosSucursales.get(i).getTercero().getNombre().equalsIgnoreCase(terceroSeleccionado.getNombre())) {
                            banderaEncuentra = true;
                            posicion = i;
                        }
                    }
                    if ((banderaEncuentra == true) && (posicion != -1)) {
                        vigenciaSeleccionada.setTercerosucursal(listTercerosSucursales.get(posicion));
                    }
                }

                listTerceros = null;
                getListTerceros();
            } else {
                permitirIndexVA = false;
                getInfoRegistroTercero();
                PrimefacesContextUI.actualizar("form:TerceroDialogo");
                PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("ESTADOAFILIACION")) {
//            activarLOV = false; 
//            PrimefacesContextUI.actualizar("form:listaValores");
//            if (!valor.isEmpty()) {
//                vigenciaSeleccionada.getEstadoafiliacion().setNombre(estadosAfiliacion);
//
//                for (int i = 0; i < listEstadosAfiliaciones.size(); i++) {
//                    if (listEstadosAfiliaciones.get(i).getNombre().startsWith(valor.toUpperCase())) {
//                        indiceUnicoElemento = i;
//                        coincidencias++;
//                    }
//                }
//                if (coincidencias == 1) {
//                    cambioVigenciaA = true;
//                    vigenciaSeleccionada.setEstadoafiliacion(listEstadosAfiliaciones.get(indiceUnicoElemento));
//
//                    cambioVigenciaA = true;
//                    listEstadosAfiliaciones = null;
//                    getListEstadosAfiliaciones();
//                } else {
//                    permitirIndexVA = false;
//                    getInfoRegistroEstado();
//                    PrimefacesContextUI.actualizar("form:EstadoAfiliacionDialogo");
//                    PrimefacesContextUI.ejecutar("PF('EstadoAfiliacionDialogo').show()");
//                    tipoActualizacion = 0;
//                }
//            } else {
//                listEstadosAfiliaciones = null;
//                getListEstadosAfiliaciones();
//                vigenciaSeleccionada.setEstadoafiliacion(new EstadosAfiliaciones());
//                if (guardado) {
//                    guardado = false;
//                    PrimefacesContextUI.actualizar("form:ACEPTAR");
//                    PrimefacesContextUI.actualizar("form:datosVAVigencia");
//                }
//            }
        }
        if (coincidencias == 1) {
            if (!listVACrear.contains(vigenciaSeleccionada)) {

                if (listVAModificar.isEmpty()) {
                    listVAModificar.add(vigenciaSeleccionada);
                } else if (!listVAModificar.contains(vigenciaSeleccionada)) {
                    listVAModificar.add(vigenciaSeleccionada);
                }
            }
            cambioVigenciaA = true;
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
                PrimefacesContextUI.actualizar("form:datosVAVigencia");
            }
        }
    }

    public void modificarVAEstado(VigenciasAfiliaciones va) {
        vigenciaSeleccionada = va;
        RequestContext context = RequestContext.getCurrentInstance();
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        activarLOV = false;
        PrimefacesContextUI.actualizar("form:listaValores");
        if (vigenciaSeleccionada.getEstadoafiliacion().getNombre() != null) {
            if (!vigenciaSeleccionada.getEstadoafiliacion().getNombre().isEmpty()) {
                vigenciaSeleccionada.getEstadoafiliacion().setNombre(estadosAfiliacion);

                for (int i = 0; i < listEstadosAfiliaciones.size(); i++) {
                    if (listEstadosAfiliaciones.get(i).getNombre().startsWith(vigenciaSeleccionada.getEstadoafiliacion().getNombre().toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    cambioVigenciaA = true;
                    vigenciaSeleccionada.setEstadoafiliacion(listEstadosAfiliaciones.get(indiceUnicoElemento));
                    cambioVigenciaA = true;
                } else {
                    permitirIndexVA = false;
                    getInfoRegistroEstado();
                    PrimefacesContextUI.actualizar("form:EstadoAfiliacionDialogo");
                    PrimefacesContextUI.ejecutar("PF('EstadoAfiliacionDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                vigenciaSeleccionada.setEstadoafiliacion(new EstadosAfiliaciones());
                if (guardado) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                    PrimefacesContextUI.actualizar("form:datosVAVigencia");
                }
            }
        } else {
            vigenciaSeleccionada.setEstadoafiliacion(new EstadosAfiliaciones());
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
                PrimefacesContextUI.actualizar("form:datosVAVigencia");
            }
        }
    }

    /**
     * Metodo que obtiene los valores de los dialogos para realizar los
     * autocomplete de los campos (VigenciaProrrateo)
     *
     * @param tipoNuevo Tipo de operacion: Nuevo Registro - Duplicar Registro
     * @param Campo Campo que toma el cambio de autocomplete
     */
    public void valoresBackupAutocompletarVA(int tipoNuevo, String Campo) {
        if (Campo.equals("TIPOENTIDAD")) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            if (tipoNuevo == 1) {
                tiposEntidades = nuevaVigenciaA.getTipoentidad().getNombre();
            } else if (tipoNuevo == 2) {
                tiposEntidades = duplicarVA.getTipoentidad().getNombre();
            }
        } else if (Campo.equals("TERCERO")) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            if (tipoNuevo == 1) {
                terceros = nuevaVigenciaA.getTercerosucursal().getTercero().getNombre();
            } else if (tipoNuevo == 2) {
                terceros = duplicarVA.getTercerosucursal().getTercero().getNombre();
            }
        } else if (Campo.equals("ESTADOAFILIACION")) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            if (tipoNuevo == 1) {
                estadosAfiliacion = nuevaVigenciaA.getEstadoafiliacion().getNombre();
            } else if (tipoNuevo == 2) {
                estadosAfiliacion = duplicarVA.getEstadoafiliacion().getNombre();
            }
        }
    }

    /**
     *
     * Metodo que genera el auto completar de un proceso nuevoRegistro o
     * duplicarRegistro de VigenciasProrrateos
     *
     * @param confirmarCambio Tipo de elemento a modificar: CENTROCOSTO -
     * PROYECTO
     * @param valorConfirmar Valor ingresado para confirmar
     * @param tipoNuevo Tipo de proceso: Nuevo - Duplicar
     */
    public void autocompletarNuevoyDuplicadoVA(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            if (tipoNuevo == 1) {
                nuevaVigenciaA.getTipoentidad().setNombre(tiposEntidades);
            } else if (tipoNuevo == 2) {
                duplicarVA.getTipoentidad().setNombre(tiposEntidades);
            }
            for (int i = 0; i < listTiposEntidades.size(); i++) {
                if (listTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaA.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaTipoEntidadVA");
                } else if (tipoNuevo == 2) {
                    duplicarVA.setTipoentidad(listTiposEntidades.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTipoEntidadVA");
                }
                listTiposEntidades = null;
                getListTiposEntidades();
            } else {
                PrimefacesContextUI.actualizar("form:TipoEntidadDialogo");
                PrimefacesContextUI.ejecutar("PF('TipoEntidadDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaTipoEntidadVA");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTipoEntidadVA");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            if (tipoNuevo == 1) {
                nuevaVigenciaA.getTercerosucursal().getTercero().setNombre(terceros);
            } else if (tipoNuevo == 2) {
                duplicarVA.getTercerosucursal().getTercero().setNombre(terceros);
            }
            for (int i = 0; i < listTerceros.size(); i++) {
                if (listTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }

            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaA.getTercerosucursal().setTercero(listTerceros.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaTerceroVA");
                } else if (tipoNuevo == 2) {
                    duplicarVA.getTercerosucursal().setTercero(listTerceros.get(indiceUnicoElemento));
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicarTerceroVA");
                }
                listTerceros = null;
                getListTerceros();
            } else {
                PrimefacesContextUI.actualizar("form:TerceroDialogo");
                PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    PrimefacesContextUI.actualizar("formularioDialogos:nuevaTerceroVA");
                } else if (tipoNuevo == 2) {
                    PrimefacesContextUI.actualizar("formularioDialogos:duplicadoTerceroVA");
                }
            }
        } else if (confirmarCambio.equalsIgnoreCase("ESTADOAFILIACION")) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaA.getEstadoafiliacion().setNombre(terceros);
                } else if (tipoNuevo == 2) {
                    duplicarVA.getEstadoafiliacion().setNombre(terceros);
                }
                for (int i = 0; i < listEstadosAfiliaciones.size(); i++) {
                    if (listEstadosAfiliaciones.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaVigenciaA.setEstadoafiliacion(listEstadosAfiliaciones.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaEstadoAfiliacionVA");
                    } else if (tipoNuevo == 2) {
                        duplicarVA.setEstadoafiliacion(listEstadosAfiliaciones.get(indiceUnicoElemento));
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicadoEstadoAfiliacionVA");
                    }
                    listEstadosAfiliaciones = null;
                    getListEstadosAfiliaciones();
                } else {
                    PrimefacesContextUI.actualizar("form:EstadoAfiliacionDialogo");
                    PrimefacesContextUI.ejecutar("PF('EstadoAfiliacionDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        PrimefacesContextUI.actualizar("formularioDialogos:nuevaEstadoAfiliacionVA");
                    } else if (tipoNuevo == 2) {
                        PrimefacesContextUI.actualizar("formularioDialogos:duplicadoEstadoAfiliacionVA");
                    }
                }
            }
        } else {
            listEstadosAfiliaciones.clear();
            getListEstadosAfiliaciones();
            if (tipoNuevo == 1) {
                nuevaVigenciaA.setEstadoafiliacion(new EstadosAfiliaciones());
                PrimefacesContextUI.actualizar("formularioDialogos:nuevaEstadoAfiliacionVA");
            } else if (tipoNuevo == 2) {
                duplicarVA.setEstadoafiliacion(new EstadosAfiliaciones());
                PrimefacesContextUI.actualizar("formularioDialogos:duplicadoEstadoAfiliacionVA");
            }
        }
    }

    public boolean validarInformacionRegistro(int tipo) {
        boolean retorno = true;
        if (tipo == 1) {
            if ((nuevaVigenciaA.getFechainicial() == null) || (nuevaVigenciaA.getTipoentidad().getSecuencia() == null) || (nuevaVigenciaA.getTercerosucursal().getSecuencia() == null)) {
                retorno = false;
            }
            if ((nuevaVigenciaA.getObservacion() != null) && (nuevaVigenciaA.getObservacion().length() > 100)) {
                retorno = false;
            }
        }
        if (tipo == 2) {
            if ((duplicarVA.getFechainicial() == null) || (duplicarVA.getTipoentidad().getNombre().isEmpty()) || (duplicarVA.getTercerosucursal().getTercero().getNombre().isEmpty())) {
                retorno = false;
            }
            if ((duplicarVA.getObservacion() != null) && (duplicarVA.getObservacion().length() > 100)) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarExistenciaTipoEntidad(int i) {
        System.out.println("ControlEmplVigenciaAfiliacion3.validarExistenciaTipoEntidad");
        boolean retorno = true;
        List<VigenciasAfiliaciones> listAuxiliar = new ArrayList<VigenciasAfiliaciones>();
        VigenciasAfiliaciones provisional = null;
        ///////////--------////////////
        /*
         * if (i == 0) { provisional = vigenciaValidaciones; }
         */
        if (i == 1) {
            provisional = nuevaVigenciaA;
        }
        if (i == 2) {
            provisional = duplicarVA;
        }
        ///////////--------////////////
        for (int cont = 0; cont < listVigenciasAfiliaciones.size(); cont++) {
            try {
                if (listVigenciasAfiliaciones.get(cont).getTipoentidad().getSecuencia() == provisional.getTipoentidad().getSecuencia()) {
                    listAuxiliar.add(listVigenciasAfiliaciones.get(cont));
                }
            } catch (NullPointerException npe) {
                System.out.println("Dato nulo");
                System.out.println(provisional.getTipoentidad());
                System.out.println(npe.getMessage());
            }
        }

        ///////////--------////////////
        int tam = listAuxiliar.size();
        ///////////--------////////////
        if (tam >= 2) {
            if (provisional.getFechafinal() == null) {
                int conteo = 0;
                for (int de = 0; de < listAuxiliar.size(); de++) {
                    if (listAuxiliar.get(de).getFechafinal() == null) {
                        conteo++;
                    }
                }
                ///////////--------////////////
                if (conteo > 0) {
                    retorno = false;
                } ///////////--------////////////
                else {
                    VigenciasAfiliaciones auxiliar = listAuxiliar.get(0);
                    retorno = provisional.getFechainicial().after(auxiliar.getFechafinal());
                }
            } ///////////--------///////////////////////--------////////////
            else {
                boolean cambiosBefore = false;
                VigenciasAfiliaciones reg1 = listAuxiliar.get(0);
                VigenciasAfiliaciones regN = listAuxiliar.get(tam - 1);
                ///////////--------////////////
                cambiosBefore = provisional.getFechainicial().before(reg1.getFechainicial());
                if (provisional.getFechafinal() != null && cambiosBefore) {
                    cambiosBefore = provisional.getFechafinal().before(reg1.getFechainicial());
                }
                ///////////--------////////////
                if (regN.getFechafinal() != null) {
                    cambiosBefore = provisional.getFechainicial().after(regN.getFechafinal());
                    if (provisional.getFechafinal() != null && cambiosBefore) {
                        cambiosBefore = provisional.getFechafinal().after(regN.getFechafinal());
                    }
                } else {
                    cambiosBefore = provisional.getFechainicial().after(regN.getFechafinal());
                }
                ///////////--------////////////
                if (cambiosBefore) {
                    retorno = true;
                } else {
                    boolean ubicoReg = false;
                    int cambios = 0;
                    for (int o = 1; o < tam - 1; o++) {
                        if (listAuxiliar.get(o + 1).getFechafinal() != null) {
                            if ((provisional.getFechainicial().after(listAuxiliar.get(o - 1).getFechafinal())) && (provisional.getFechafinal().before(listAuxiliar.get(o + 1).getFechainicial()))) {
                                cambios++;
                                ubicoReg = true;
                            }
                        }
                    }
                    ///////////--------////////////
                    if (cambios == 1) {
                        if (ubicoReg == true) {
                            retorno = true;
                        }
                    } ///////////--------////////////
                    else {
                        retorno = false;
                    }
                }
            }
        }
        ///////////--------///////////////////////--------////////////
        if (tam == 1) {
            boolean ubico = false;
            if (provisional.getFechafinal() != null) {
                if ((provisional.getFechainicial().before(listAuxiliar.get(0).getFechainicial())) && (provisional.getFechafinal().before(listAuxiliar.get(0).getFechainicial()))) {
                    ubico = true;
                } else if ((provisional.getFechainicial().after(listAuxiliar.get(0).getFechafinal())) && (provisional.getFechafinal().after(listAuxiliar.get(0).getFechafinal()))) {
                    ubico = true;
                }
            } else {
                if (listAuxiliar.get(0).getFechafinal() != null) {
                    ubico = provisional.getFechainicial().after(listAuxiliar.get(0).getFechafinal());
                } else {
                    ubico = false;
                }
            }
            retorno = ubico;
        }
        if (tam == 0) {
            retorno = true;
        }
        return retorno;
    }

    public boolean validacionTercerorSucursalesNuevoRegistro(int i) {
        boolean retorno = true;
        if (i == 1) {
            Long r = administrarVigenciasAfiliaciones3.validacionTercerosSurcursalesNuevaVigencia(empleado.getSecuencia(), nuevaVigenciaA.getFechainicial(), nuevaVigenciaA.getEstadoafiliacion().getSecuencia(), nuevaVigenciaA.getTercerosucursal().getTercero().getSecuencia());
            if (r > 0) {
                retorno = false;
            }
        }
        if (i == 2) {
            Long r = administrarVigenciasAfiliaciones3.validacionTercerosSurcursalesNuevaVigencia(empleado.getSecuencia(), duplicarVA.getFechainicial(), duplicarVA.getEstadoafiliacion().getSecuencia(), duplicarVA.getTercerosucursal().getTercero().getSecuencia());
            if (r > 0) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validacionFechasNuevoRegistro(int i) {
        boolean retorno = true;
        if (i == 1) {
            if ((nuevaVigenciaA.getFechainicial() != null) && (nuevaVigenciaA.getFechafinal() != null)) {
                if (nuevaVigenciaA.getFechainicial().after(nuevaVigenciaA.getFechafinal())) {
                    retorno = false;
                }
            }
            if ((nuevaVigenciaA.getFechainicial() != null) && (nuevaVigenciaA.getFechafinal() == null)) {
                retorno = true;
            }
        }
        if (i == 2) {
            if ((duplicarVA.getFechainicial() != null) && (duplicarVA.getFechafinal() != null)) {
                if (duplicarVA.getFechainicial().after(duplicarVA.getFechafinal())) {
                    retorno = false;
                }
            }
            if ((duplicarVA.getFechainicial() != null) && (duplicarVA.getFechafinal() == null)) {
                retorno = true;
            }
        }
        return retorno;
    }

    public void dialogoCamposNulos() {
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:errorIngresoRegistro");
        PrimefacesContextUI.ejecutar("PF('errorIngresoRegistro').show()");
    }

    public void dialogoFechasErroneas() {
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:errorFechasRegistro");
        PrimefacesContextUI.ejecutar("PF('errorFechasRegistro').show()");
    }

    public void dialogoFechaContratacionError() {
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:errorFechaMenorContratacion");
        PrimefacesContextUI.ejecutar("PF('errorFechaMenorContratacion').show()");
    }

    public void dialogoDiaInicioError() {
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:errorFechaInicialRegistro");
        PrimefacesContextUI.ejecutar("PF('errorFechaInicialRegistro').show()");
    }

    public void dialogoTipoERepetida() {
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:errorExistenciaTipoEntidad");
        PrimefacesContextUI.ejecutar("PF('errorExistenciaTipoEntidad').show()");
    }

    public void dialogoErrorTercero() {
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:errorTerceroSucursal");
        PrimefacesContextUI.ejecutar("PF('errorTerceroSucursal').show()");
    }

    public boolean validarFechasRegistroTabla() {
        boolean retorno = true;
        List<VigenciasAfiliaciones> listAuxiliar = null;
        listAuxiliar = listVigenciasAfiliaciones;
        boolean validacionCamposNulos;
        boolean validacionFechas;
        boolean validacionFechaMayorContratacion;
        boolean validacionDiaInicioFecha;
        validacionCamposNulos = camposModificacionRegistro(listAuxiliar);
        if (validacionCamposNulos) {
            //////----------////////////
            validacionFechas = fechasModificacionRegistro(listAuxiliar);
            if (!validacionFechas) {
                retorno = false;
                dialogoFechasErroneas();
            }
            //////----------////////////
            validacionFechaMayorContratacion = fechaContratacionModificacionEmpleado();
            if (!validacionFechaMayorContratacion) {
                dialogoFechaContratacionError();
            }
            //////----------////////////
            validacionDiaInicioFecha = diaInicialFechaModificacion();
            if (!validacionDiaInicioFecha) {
                dialogoDiaInicioError();
            }
            //////----------////////////
            if (!validacionFechas) {
                retorno = false;
            }
        } else {
            dialogoCamposNulos();
            retorno = false;
        }
        if (retorno) {
            listVigenciasAfiliaciones = listAuxiliar;

        }
        return retorno;
    }

    public boolean validarModificacionRegistroTabla() {
        //booleano que determina si las validaciones fueron cumplidas
        boolean retorno = true;
        //lista con la que se haran las validaciones
        List<VigenciasAfiliaciones> listAuxiliar = null;
        listAuxiliar = listVigenciasAfiliaciones;

        boolean validacionCamposNulos;
        boolean validacionTiposEntidades;
        boolean validacionTerceroSucursal;

        validacionCamposNulos = camposModificacionRegistro(listAuxiliar);

        if (validacionCamposNulos) {//Si no hay campos nulos

            if (tipoValidacion == 2) {
                validacionTerceroSucursal = terceroModificacionRegistro(listAuxiliar);
                if (validacionTerceroSucursal == false) {
                    dialogoErrorTercero();
                    retorno = false;
                }
            } else if (tipoValidacion == 1) {
                validacionTiposEntidades = tipoEntidadModificacionRegistro(listAuxiliar);
                if (validacionTiposEntidades == false) {
                    dialogoTipoERepetida();
                    retorno = false;
                }
            }
        } else {
            //dialogoCamposNulos();
            retorno = false;
        }
        if (retorno) {
            listVigenciasAfiliaciones = listAuxiliar;

        }
        tipoValidacion = 0;
        return retorno;
    }

    public boolean fechaContratacionModificacionEmpleado() {
        boolean retorna = true;
        if (vigenciaSeleccionada.getFechainicial().before(fechaContratacion)) {
            retorna = false;
        }
        return retorna;
    }

    public boolean diaInicialFechaModificacion() {
        boolean retorna = true;
        int dia = vigenciaValidaciones.getFechainicial().getDate();
        if (dia > 1) {
            retorna = false;
        }
        return retorna;
    }

    public boolean tipoEntidadModificacionRegistro(List<VigenciasAfiliaciones> listaAuxiliar) {
        boolean retorno = true;
        activarLOV = false;
        //Objeto con el tipo de entidad seleccionado
        VigenciasAfiliaciones provisional = vigenciaValidaciones;
        //Lista para llenar con los datos del tipo seleccionado
        List<VigenciasAfiliaciones> listAuxiliarTipoESeleccionado = new ArrayList<VigenciasAfiliaciones>();

        for (int cont = 0; cont < listaAuxiliar.size(); cont++) {
            if (listaAuxiliar.get(cont).getTipoentidad().getSecuencia() == provisional.getTipoentidad().getSecuencia()) {
                listAuxiliarTipoESeleccionado.add(listaAuxiliar.get(cont));
            }
        }
        int tam = listAuxiliarTipoESeleccionado.size();
        System.out.println("variable tam: " + tam);
        ///////////--------////////////
        if (tam >= 2) {
            if (provisional.getFechafinal() == null) {
                int conteo = 0;
                for (int de = 0; de < listAuxiliarTipoESeleccionado.size(); de++) {
                    if (listAuxiliarTipoESeleccionado.get(de).getFechafinal() == null) {
                        conteo++;
                    }
                }
                if (conteo > 0) {
                    conteo = 0;
                    for (int de = 0; de < listAuxiliarTipoESeleccionado.size(); de++) {
                        if (listAuxiliarTipoESeleccionado.get(de).getSecuencia() == provisional.getSecuencia()) {
                            conteo++;
                        }
                    }
                    if (conteo != 1) {
                        retorno = false;
                    }
                    if (conteo == 1) {
                        retorno = true;
                    }
                } ///////////--------////////////
                else {
                    VigenciasAfiliaciones auxiliar = listAuxiliarTipoESeleccionado.get(0);
                    retorno = provisional.getFechainicial().after(auxiliar.getFechafinal());
                }
            } ///////////--------///////////////////////--------////////////
            else {
                boolean cambiosBefore = false;
                VigenciasAfiliaciones reg1 = listAuxiliarTipoESeleccionado.get(0);
                VigenciasAfiliaciones regN = listAuxiliarTipoESeleccionado.get(tam - 1);
                ///////////--------////////////
                if ((provisional.getFechafinal().before(reg1.getFechainicial())) && (provisional.getFechainicial().before(reg1.getFechainicial()))) {
                    cambiosBefore = true;
                }
                ///////////--------////////////
                if (regN.getFechafinal() != null) {
                    if ((provisional.getFechafinal().after(regN.getFechafinal())) && (provisional.getFechainicial().after(regN.getFechafinal()))) {
                        cambiosBefore = true;
                    }
                } else {
                    if (provisional.getFechainicial().after(regN.getFechafinal())) {
                        cambiosBefore = true;
                    }
                }
                ///////////--------////////////
                if (cambiosBefore == true) {
                    retorno = true;
                } else {
                    boolean ubicoReg = false;
                    int cambios = 0;
                    for (int o = 1; o < tam - 1; o++) {
                        if (listAuxiliarTipoESeleccionado.get(o + 1).getFechafinal() != null) {
                            if ((provisional.getFechainicial().after(listAuxiliarTipoESeleccionado.get(o - 1).getFechafinal())) && (provisional.getFechafinal().before(listAuxiliarTipoESeleccionado.get(o + 1).getFechainicial()))) {
                                cambios++;
                                ubicoReg = true;
                            }
                        }
                    }
                    ///////////--------////////////
                    if (cambios == 1) {
                        if (ubicoReg == true) {
                            retorno = true;
                        }
                    } ///////////--------////////////
                    else {
                        retorno = false;
                    }
                }
            }
        }
        ///////////--------///////////////////////--------////////////
        if (tam == 1) {
            boolean ubico = false;
            if (provisional.getFechafinal() != null) {
                if ((provisional.getFechainicial().before(listAuxiliarTipoESeleccionado.get(0).getFechainicial())) && (provisional.getFechafinal().before(listAuxiliarTipoESeleccionado.get(0).getFechainicial()))) {
                    ubico = true;
                } else if ((provisional.getFechainicial().after(listAuxiliarTipoESeleccionado.get(0).getFechafinal())) && (provisional.getFechafinal().after(listAuxiliarTipoESeleccionado.get(0).getFechafinal()))) {
                    ubico = true;
                }
            } else {
                if (listAuxiliarTipoESeleccionado.get(0).getFechafinal() != null) {
                    if ((provisional.getFechainicial().after(listAuxiliarTipoESeleccionado.get(0).getFechafinal()))) {
                        ubico = true;
                    }
                } else {
                    ubico = false;
                }
            }
            retorno = ubico;
        }
        if (tam == 0) {
            retorno = true;
        }
        return retorno;
    }

    public boolean camposModificacionRegistro(List<VigenciasAfiliaciones> listaAuxiliar) {
        boolean retorno = true;
        int posicion = listVigenciasAfiliaciones.indexOf(vigenciaSeleccionada);
        //Si alguno de los campos obligatorios es nulo 
        if ((listaAuxiliar.get(posicion).getFechainicial() == null) || (listaAuxiliar.get(posicion).getTipoentidad().getNombre().isEmpty()) || (listaAuxiliar.get(posicion).getTercerosucursal().getTercero().getNombre().isEmpty())) {
            dialogoCamposNulos();
            retorno = false;
        }
        //Si Observacion excede su limite de caracteres
        if ((listaAuxiliar.get(posicion).getObservacion() != null) && (listaAuxiliar.get(posicion).getObservacion().length() > 100)) {
            retorno = false;
        }
        return retorno;
    }

    public boolean fechasModificacionRegistro(List<VigenciasAfiliaciones> listaAuxuliar) {
        boolean retorno = true;
        VigenciasAfiliaciones auxiliar = null;
        auxiliar = listaAuxuliar.get(listVigenciasAfiliaciones.indexOf(vigenciaSeleccionada));
        if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() != null)) {
            if (auxiliar.getFechainicial().after(auxiliar.getFechafinal())) {
                retorno = false;
            }
        }
        if ((auxiliar.getFechainicial() != null) && (auxiliar.getFechafinal() == null)) {
            retorno = true;
        }
        return retorno;
    }

    public boolean terceroModificacionRegistro(List<VigenciasAfiliaciones> listaAuxiliar) {
        boolean retorno = true;
        activarLOV = false;
        PrimefacesContextUI.actualizar("form:listaValores");
        if (vigenciaSeleccionada.getEstadoafiliacion() == null) {
            vigenciaSeleccionada.setEstadoafiliacion(new EstadosAfiliaciones());
        }
        //Long r = administrarVigenciasAfiliaciones3.validacionTercerosSurcursalesNuevaVigencia(empleado.getSecuencia(), listaAuxiliar.get(indexVA).getFechainicial(), BigDecimal(decimal) , listaAuxiliar.get(indexVA).getTercerosucursal().getTercero().getSecuencia());
        Long r = administrarVigenciasAfiliaciones3.validacionTercerosSurcursalesNuevaVigencia(empleado.getSecuencia(), vigenciaSeleccionada.getFechainicial(), vigenciaSeleccionada.getEstadoafiliacion().getSecuencia(), vigenciaSeleccionada.getTercerosucursal().getTercero().getSecuencia());
        if (r != null) {
            if (r > 0) {
                retorno = false;
            }
        }
        return retorno;
    }

    public boolean validarFechaContratacionEmpleado(int i) {
        boolean retorna = true;
        if (i == 1) {
            if (nuevaVigenciaA.getFechainicial().before(fechaContratacion)) {
                retorna = false;
            }
        }
        if (i == 2) {
            if (duplicarVA.getFechainicial().before(fechaContratacion)) {
                retorna = false;
            }
        }
        return retorna;
    }

    public boolean validarDiaInicialRegistro(int i) {
        boolean retorna = true;
        if (i == 1) {
            int dia = nuevaVigenciaA.getFechainicial().getDate();
            if (dia >= 2) {
                retorna = false;
            }
        }
        if (i == 2) {
            int dia = duplicarVA.getFechainicial().getDate();
            if (dia >= 2) {
                retorna = false;
            }
        }
        return retorna;
    }

    public boolean validarIngresoNuevoRegistro(int i) {
        boolean mensaje = true;
        boolean validacionCamposNulos;
        boolean validacionTiposEntidades;
        boolean validacionFechas;
        boolean validacionTerceroSucursal;
        //////----------////////////
        validacionCamposNulos = validarInformacionRegistro(i);
        if (!validacionCamposNulos) {
            dialogoCamposNulos();
        }
        //////----------////////////
        if (validacionCamposNulos) {
            validacionFechas = validacionFechasNuevoRegistro(i);
            if (!validacionFechas) {
                dialogoFechasErroneas();
            }
            //////----------////////////
            validacionTerceroSucursal = validacionTercerorSucursalesNuevoRegistro(i);
            if (!validacionTerceroSucursal) {
                dialogoErrorTercero();
            }
            //////----------////////////
            validacionTiposEntidades = validarExistenciaTipoEntidad(i);
            if (!validacionTiposEntidades) {
                dialogoTipoERepetida();
            }
            if (!validacionFechas || !validacionTerceroSucursal || !validacionTiposEntidades) {
                mensaje = false;
            }
        } //////----------////////////
        else {
            mensaje = false;
        }
        return mensaje;
    }

    public void posicionNit() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        vigenciaSeleccionada = listVigenciasAfiliaciones.get(indice);
        cambiarIndiceVA(vigenciaSeleccionada, columna);
    }

    public void cambiarIndiceVA(VigenciasAfiliaciones va, int celda) {
        System.out.println("va" + va);
//        if (permitirIndexVA) {
//            vigenciaSeleccionada = va;
//            cualCeldaVA = celda;
//            activarLOV = false;
//            tiposEntidades = vigenciaSeleccionada.getTipoentidad().getNombre();
//            terceros = vigenciaSeleccionada.getTercerosucursal().getTercero().getNombre();
//            nit = vigenciaSeleccionada.getTercerosucursal().getTercero().getNit();
//            System.out.println("vigenciaSeleccionada: " + vigenciaSeleccionada);
//            if (vigenciaSeleccionada.getEstadoafiliacion() != null) {
//                System.out.println("entro en:  if (vigenciaSeleccionada.getEstadoafiliacion() != null)");
//                if (vigenciaSeleccionada.getEstadoafiliacion().getNombre() != null) {
//                    System.out.println("entro en:  if (vigenciaSeleccionada.getEstadoafiliacion().getNombre() != null)");
//                    estadosAfiliacion = vigenciaSeleccionada.getEstadoafiliacion().getNombre();
//                }
//            }
//            fechaFin = vigenciaSeleccionada.getFechafinal();
//            fechaIni = vigenciaSeleccionada.getFechainicial();
//        }
//        PrimefacesContextUI.actualizar("form:listaValores");

        RequestContext context = RequestContext.getCurrentInstance();
        if (permitirIndexVA == true) {
            vigenciaSeleccionada = va;
            cualCeldaVA = celda;
            if (cualCeldaVA == 0) {
                activarLOV = true;
                fechaIni = vigenciaSeleccionada.getFechainicial();

            } else if (cualCeldaVA == 1) {
                activarLOV = true;
                fechaFin = vigenciaSeleccionada.getFechafinal();

            } else if (cualCeldaVA == 2) {
                activarLOV = false;
                tiposEntidades = vigenciaSeleccionada.getTipoentidad().getNombre();

            } else if (cualCeldaVA == 3) {
                activarLOV = false;
                terceros = vigenciaSeleccionada.getTercerosucursal().getTercero().getNombre();

            } else if (cualCeldaVA == 4) {
                activarLOV = true;
                nit = vigenciaSeleccionada.getTercerosucursal().getTercero().getNit();

            } else if (cualCeldaVA == 5) {
                activarLOV = true;
            } else if (cualCeldaVA == 6) {
                activarLOV = false;
                estadosAfiliacion = vigenciaSeleccionada.getEstadoafiliacion().getNombre();
            } else if (cualCeldaVA == 5) {
                activarLOV = true;
            }
        } else {
            PrimefacesContextUI.ejecutar("PF('datosVCEmpleado.selectRow(" + va + ", false); datosVCEmpleado.unselectAllRows()");
        }
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    //GUARDAR
    /**
     * Metodo de guardado general para la pagina
     */
    public void guardadoGeneral() {
        guardarCambiosVA();
        guardado = true;
        System.out.println("ControlEmplVigenciaAfiliacion3.guardadoGeneral");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
    }

    /**
     * Metodo que guarda los cambios efectuados en la pagina VigenciasProrrateos
     */
    public void guardarCambiosVA() {
        System.out.println("ControlEmplVigenciaAfiliacion3.guardarCambiosVA");
        if (!guardado) {
            System.out.println("Realizando Operaciones.....");
            if (!listVABorrar.isEmpty()) {
                for (int i = 0; i < listVABorrar.size(); i++) {
                    System.out.println("Borrando...");
                    if (listVABorrar.get(i).getTipoentidad().getSecuencia() == null) {
                        listVABorrar.get(i).setTipoentidad(null);
                    }
                    if (listVABorrar.get(i).getTercerosucursal().getTercero().getSecuencia() == null) {
                        listVABorrar.get(i).getTercerosucursal().setTercero(null);
                    }
                    if (listVABorrar.get(i).getEstadoafiliacion().getSecuencia() == null) {
                        listVABorrar.get(i).setEstadoafiliacion(null);
                    }
                    administrarVigenciasAfiliaciones3.borrarVigenciaAfiliacion(listVABorrar.get(i));
                }
                listVABorrar.clear();
            }
            if (!listVACrear.isEmpty()) {
                for (int i = 0; i < listVACrear.size(); i++) {
                    System.out.println("Creando...");
                    if (listVACrear.get(i).getTipoentidad().getSecuencia() == null) {
                        listVACrear.get(i).setTipoentidad(null);
                    }
                    if (listVACrear.get(i).getTercerosucursal().getTercero().getSecuencia() == null) {
                        listVACrear.get(i).getTercerosucursal().setTercero(null);
                    }
                    if (listVACrear.get(i).getEstadoafiliacion().getSecuencia() == null) {
                        listVACrear.get(i).setEstadoafiliacion(null);
                    }
                    administrarVigenciasAfiliaciones3.crearVigenciaAfiliacion(listVACrear.get(i));
                }
                listVACrear.clear();
            }
            if (!listVAModificar.isEmpty()) {
                for (int i = 0; i < listVAModificar.size(); i++) {
                    System.out.println("Modificando...");
                    if (listVAModificar.get(i).getTipoentidad().getSecuencia() == null) {
                        listVAModificar.get(i).setTipoentidad(null);
                    }
                    if (listVAModificar.get(i).getTercerosucursal().getTercero().getSecuencia() == null) {
                        listVAModificar.get(i).getTercerosucursal().setTercero(null);
                    }
                    if (listVAModificar.get(i).getEstadoafiliacion().getSecuencia() == null) {
                        listVAModificar.get(i).setEstadoafiliacion(null);
                    }
                    administrarVigenciasAfiliaciones3.editarVigenciaAfiliacion(listVAModificar.get(i));
                }

                listVAModificar.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            activarLOV = true;
            PrimefacesContextUI.actualizar("form:listaValores");
            int tam = 0;
            if (listVigenciasAfiliaciones != null) {
                tam = listVigenciasAfiliaciones.size();
            }
            if (tam > 0) {
                modificarInfoRegistro(listVigenciasAfiliaciones.size());
            } else {
                modificarInfoRegistro(0);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            PrimefacesContextUI.actualizar("form:datosVAVigencia");
            guardado = true;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
            PrimefacesContextUI.actualizar("form:informacionRegistro");
            k = 0;
            System.out.println("ControlEmplVigenciaAfiliacion3.guardarCambiosVA, Empleado: " + empleado);
            permitirIndexVA = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            PrimefacesContextUI.actualizar("form:growl");
//            listVigenciasAfiliaciones = null;
//            getListVigenciasAfiliaciones();
//            contarRegistrosVA();
        }
        cambioVigenciaA = false;
    }

    //CANCELAR MODIFICACIONES
    /**
     * Cancela las modificaciones realizas en la pagina
     */
    /**
     * Cancela las modifaciones de la tabla vigencias prorrateos
     */
    public void cancelarModificacionVA() {
        cerrarFiltrado();
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        listVABorrar.clear();
        listVACrear.clear();
        listVAModificar.clear();
        k = 0;
        listVigenciasAfiliaciones = null;
        vigenciaSeleccionada = null;
        guardado = true;
        permitirIndexVA = true;
        getListVigenciasAfiliaciones();
        if (listVigenciasAfiliaciones != null) {
            modificarInfoRegistro(listVigenciasAfiliaciones.size());
        } else {
            modificarInfoRegistro(0);
        }
        for (int i = 0; i < listVigenciasAfiliaciones.size(); i++) {
            System.out.println("tercero: " + listVigenciasAfiliaciones.get(i).getTercerosucursal().getTercero().getNombre()
                    + ", Codigo: " + listVigenciasAfiliaciones.get(i).getCodigo() + ", "
                    + ", Estado: " + listVigenciasAfiliaciones.get(i).getEstadoafiliacion());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosVAVigencia");
        PrimefacesContextUI.actualizar("form:ACEPTAR");
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    private void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        vAFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaInicial");
        vAFechaInicial.setFilterStyle("display: none; visibility: hidden;");
        vAFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaFinal");
        vAFechaFinal.setFilterStyle("display: none; visibility: hidden;");
        vATercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATercero");
        vATercero.setFilterStyle("display: none; visibility: hidden;");
        vATipoEntidad = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATipoEntidad");
        vATipoEntidad.setFilterStyle("display: none; visibility: hidden;");
        vANITTercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vANITTercero");
        vANITTercero.setFilterStyle("display: none; visibility: hidden;");
        vACodigo = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vACodigo");
        vACodigo.setFilterStyle("display: none; visibility: hidden;");
        vAEstadoAfiliacion = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAEstadoAfiliacion");
        vAEstadoAfiliacion.setFilterStyle("display: none; visibility: hidden;");
        vAObservaciones = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAObservaciones");
        vAObservaciones.setFilterStyle("display: none; visibility: hidden;");
        altoTabla = "292";
        PrimefacesContextUI.actualizar("form:datosVAVigencia");
        banderaVA = 0;
        filtrarVigenciasAfiliaciones = null;
        tipoListaVA = 0;
    }
    //MOSTRAR DATOS CELDA

    /**
     * Metodo que muestra los dialogos de editar con respecto a la lista real o
     * la lista filtrada y a la columna
     */
    public void editarCelda() {
        if (vigenciaSeleccionada != null) {
            editarVA = vigenciaSeleccionada;
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCeldaVA == 0) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFechaInicialVAD");
                PrimefacesContextUI.ejecutar("PF('editarFechaInicialVAD').show()");
                cualCeldaVA = -1;
            } else if (cualCeldaVA == 1) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarFechaFinalVAD");
                PrimefacesContextUI.ejecutar("PF('editarFechaFinalVAD').show()");
                cualCeldaVA = -1;
            } else if (cualCeldaVA == 2) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarTipoEntidadVAD");
                PrimefacesContextUI.ejecutar("PF('editarTipoEntidadVAD').show()");
                cualCeldaVA = -1;
            } else if (cualCeldaVA == 3) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarTerceroVAD");
                PrimefacesContextUI.ejecutar("PF('editarTerceroVAD').show()");
                cualCeldaVA = -1;
            } else if (cualCeldaVA == 4) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarNITTerceroVAD");
                PrimefacesContextUI.ejecutar("PF('editarNITTerceroVAD').show()");
                cualCeldaVA = -1;
            } else if (cualCeldaVA == 5) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarCodigoVAD");
                PrimefacesContextUI.ejecutar("PF('editarCodigoVAD').show()");
                cualCeldaVA = -1;
            } else if (cualCeldaVA == 6) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarEstadoAfiliacionVAD");
                PrimefacesContextUI.ejecutar("PF('editarEstadoAfiliacionVAD').show()");
                cualCeldaVA = -1;
            } else if (cualCeldaVA == 7) {
                PrimefacesContextUI.actualizar("formularioDialogos:editarObservacionesVAD");
                PrimefacesContextUI.ejecutar("PF('editarObservacionesVAD').show()");
                cualCeldaVA = -1;
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    /**
     * Agrega una nueva Vigencia Prorrateo
     */
    public void agregarNuevaVA() {
        System.out.println("ControlEmplVigenciaAfiliacion3.agregarNuevaVA");
        boolean msn = validarIngresoNuevoRegistro(1);
        System.out.println("mensaje: " + msn);
        if (msn) {
            RequestContext context = RequestContext.getCurrentInstance();
            if (validarFechasRegistro(1) == true) {
                System.out.println("Nueva vigencia: " + nuevaVigenciaA);
                System.out.println("fechaContratacion: " + fechaContratacion);
                System.out.println("fechaInicial: " + nuevaVigenciaA.getFechainicial());
                if (nuevaVigenciaA.getFechainicial().before(fechaContratacion)) {
                    dialogoFechaContratacionError();
                }
                int dia = nuevaVigenciaA.getFechainicial().getDate();
//                  LocalDateTime calendarioAux = LocalDateTime.fromDateFields(nuevaVigenciaA.getFechainicial());
//                  int dia= calendarioAux.getDayOfMonth();
                if (dia > 1) {
                    dialogoDiaInicioError();
                }
                cambioVigenciaA = true;
                //CERRAR FILTRADO
                System.out.println("banderaVA: " + banderaVA);
                if (banderaVA == 1) {
                    cerrarFiltrado();
                }
                //AGREGAR REGISTRO A LA LISTA VIGENCIAS
                k++;
                l = BigInteger.valueOf(k);

                nuevaVigenciaA.setSecuencia(l);
                nuevaVigenciaA.setEmpleado(empleado);
                listVigenciasAfiliaciones.add(nuevaVigenciaA);
                listVACrear.add(nuevaVigenciaA);
                vigenciaSeleccionada = listVigenciasAfiliaciones.get(listVigenciasAfiliaciones.indexOf(nuevaVigenciaA));
                modificarInfoRegistro(listVigenciasAfiliaciones.size());
                activarLOV = true;
                PrimefacesContextUI.actualizar("form:listaValores");
                //
                nuevaVigenciaA = new VigenciasAfiliaciones();
                nuevaVigenciaA.setTipoentidad(new TiposEntidades());
                nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
                nuevaVigenciaA.setEstadoafiliacion(new EstadosAfiliaciones());
                PrimefacesContextUI.actualizar("form:datosVAVigencia");
                PrimefacesContextUI.actualizar("form:informacionRegistro");
                //
                if (guardado) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
                limpiarNuevaVA();
                PrimefacesContextUI.ejecutar("PF('NuevoRegistroVA').hide()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
            }
        }
        System.out.println("fin ControlEmplVigenciaAfiliacion3.agregarNuevaVA");
    }

    /**
     * Limpia los elementos de una nueva vigencia prorrateo
     */
    public void limpiarNuevaVA() {
        nuevaVigenciaA = new VigenciasAfiliaciones();
        nuevaVigenciaA.setTipoentidad(new TiposEntidades());
        nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
        nuevaVigenciaA.setEstadoafiliacion(new EstadosAfiliaciones());
        //vigenciaSeleccionada = null;

    }

    public void cancelarNuevaVA() {
        nuevaVigenciaA = new VigenciasAfiliaciones();
        nuevaVigenciaA.setTipoentidad(new TiposEntidades());
        nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
        nuevaVigenciaA.setEstadoafiliacion(new EstadosAfiliaciones());
        // vigenciaSeleccionada = null;
        cambioVigenciaA = false;
    }

    //DUPLICAR VL
    /**
     * Metodo que verifica que proceso de duplicar se genera con respecto a la
     * posicion en la pagina que se tiene
     */
    public void verificarDuplicarVigencia() {
        System.out.println("ControlEmplVigenciaAfiliacion3.verificarDuplicarVigencia");
        if (vigenciaSeleccionada != null) {
            if (!cambioVigenciaA) {
                duplicarVigenciaA();
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('confirmarGuardarSinSalir').show()");
            }
        }
    }

    ///////////////////////////////////////////////////////////////
    /**
     * Duplica una registro de
     */
    public void duplicarVigenciaA() {
        duplicarVA = new VigenciasAfiliaciones();
        k++;
//        BigDecimal var = BigDecimal.valueOf(k);
        BigInteger var = BigInteger.valueOf(k);
        l = BigInteger.valueOf(k);

        System.out.println("ControlEmplVigenciaAfiliacion.duplicarVigenciaA, if: ");
        System.out.println("Lista tipoListaVA: " + tipoListaVA);
        duplicarVA.setSecuencia(l);
        System.out.println("Valor de l: " + l);
        duplicarVA.setEmpleado(vigenciaSeleccionada.getEmpleado());
        duplicarVA.setFechafinal(vigenciaSeleccionada.getFechafinal());
        duplicarVA.setFechainicial(vigenciaSeleccionada.getFechainicial());
        duplicarVA.setTercerosucursal(vigenciaSeleccionada.getTercerosucursal());
        duplicarVA.setTipoentidad(vigenciaSeleccionada.getTipoentidad());
        duplicarVA.setCodigo(vigenciaSeleccionada.getCodigo());
        duplicarVA.setEstadoafiliacion(vigenciaSeleccionada.getEstadoafiliacion());
        duplicarVA.setObservacion(vigenciaSeleccionada.getObservacion());
        System.out.println("Secuencia Empleado: " + vigenciaSeleccionada.getEmpleado());

        cambioVigenciaA = true;
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:duplicadoVA");
        PrimefacesContextUI.ejecutar("PF('DuplicadoRegistroVA').show()");
    }

    /**
     * Metodo que confirma el duplicado y actualiza los datos de la tabla
     * VigenciaProrrateo
     */
    public void confirmarDuplicarVA() {
        boolean msn = validarIngresoNuevoRegistro(2);
        if (msn) {
            if (validarFechasRegistro(2)) {
                System.out.println("Estoy validando la fecha " + validarFechasRegistro(2));
                if (duplicarVA.getFechainicial().before(fechaContratacion)) {
                    dialogoFechaContratacionError();
                }
                int dia = duplicarVA.getFechainicial().getDate();
                if (dia > 1) {
                    dialogoDiaInicioError();
                }
                RequestContext context = RequestContext.getCurrentInstance();
                cambioVigenciaA = true;
                k++;
                l = BigInteger.valueOf(k);
                duplicarVA.setSecuencia(l);
                listVigenciasAfiliaciones.add(duplicarVA);
                listVACrear.add(duplicarVA);
                vigenciaSeleccionada = listVigenciasAfiliaciones.get(listVigenciasAfiliaciones.indexOf(duplicarVA));
                modificarInfoRegistro(listVigenciasAfiliaciones.size());
                PrimefacesContextUI.actualizar("form:datosVAVigencia");
                PrimefacesContextUI.actualizar("form:informacionRegistro");
                
                if (guardado) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
                if (banderaVA == 1) {
                    //CERRAR FILTRADO
                    cerrarFiltrado();
                }
                duplicarVA = new VigenciasAfiliaciones();
                limpiarduplicarVA();
                activarLOV = true;
                PrimefacesContextUI.actualizar("form:listaValores");
                PrimefacesContextUI.ejecutar("PF('DuplicadoRegistroVA').hide()");
            } else {
                RequestContext context = RequestContext.getCurrentInstance();
                PrimefacesContextUI.ejecutar("PF('errorFechas').show()");
            }
        }
    }

    /**
     * Limpia los elementos del duplicar registro Vigencia Prorrateo
     */
    public void limpiarduplicarVA() {
        duplicarVA = new VigenciasAfiliaciones();
        duplicarVA.setTipoentidad(new TiposEntidades());
        duplicarVA.setTercerosucursal(new TercerosSucursales());
        duplicarVA.setEstadoafiliacion(new EstadosAfiliaciones());

    }

    public void cancelarDuplicadoVA() {
        cambioVigenciaA = false;
        duplicarVA = new VigenciasAfiliaciones();
        duplicarVA.setTipoentidad(new TiposEntidades());
        duplicarVA.setTercerosucursal(new TercerosSucursales());
        duplicarVA.setEstadoafiliacion(new EstadosAfiliaciones());
        //vigenciaSeleccionada = null;
    }

    /**
     * Valida que registro se elimina de que tabla con respecto a la posicion en
     * la pagina
     */
    public void validarBorradoVigencia() {
        if (vigenciaSeleccionada != null) {
            borrarVA();
        }
    }

    /**
     * Metodo que borra una vigencia prorrateo
     */
    public void borrarVA() {
        System.out.println("ControlEmplVigenciaAfiliacion3.borrarVA");
        cambioVigenciaA = true;
        if (!listVAModificar.isEmpty() && listVAModificar.contains(vigenciaSeleccionada)) {
            int modIndex = listVAModificar.indexOf(vigenciaSeleccionada);
            listVAModificar.remove(modIndex);
            listVABorrar.add(vigenciaSeleccionada);
        } else if (!listVACrear.isEmpty() && listVACrear.contains(vigenciaSeleccionada)) {
            int crearIndex = listVACrear.indexOf(vigenciaSeleccionada);
            listVACrear.remove(crearIndex);
        } else {
            listVABorrar.add(vigenciaSeleccionada);
        }
        listVigenciasAfiliaciones.remove(vigenciaSeleccionada);
        if (tipoListaVA == 1) {
            filtrarVigenciasAfiliaciones.remove(vigenciaSeleccionada);
        }
        modificarInfoRegistro(listVigenciasAfiliaciones.size());
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:datosVAVigencia");
        PrimefacesContextUI.actualizar("form:informacionRegistro");
        vigenciaSeleccionada = null;
        if (guardado) {
            guardado = false;
            PrimefacesContextUI.actualizar("form:ACEPTAR");
        }

    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    /**
     * Metodo que activa el filtrado por medio de la opcion en el toolbar o por
     * medio de la tecla Crtl+F11
     */
    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (banderaVA == 0) {
            //Columnas Tabla VPP
            vAFechaInicial = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaInicial");
            vAFechaInicial.setFilterStyle("width: 85%");
            vAFechaFinal = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAFechaFinal");
            vAFechaFinal.setFilterStyle("width: 85%");
            vATercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATercero");
            vATercero.setFilterStyle("width: 85%");
            vATipoEntidad = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vATipoEntidad");
            vATipoEntidad.setFilterStyle("width: 85%");
            vANITTercero = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vANITTercero");
            vANITTercero.setFilterStyle("width: 85%");
            vACodigo = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vACodigo");
            vACodigo.setFilterStyle("width: 85%");
            vAEstadoAfiliacion = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAEstadoAfiliacion");
            vAEstadoAfiliacion.setFilterStyle("width: 85%");
            vAObservaciones = (Column) c.getViewRoot().findComponent("form:datosVAVigencia:vAObservaciones");
            vAObservaciones.setFilterStyle("width: 85%");
            altoTabla = "272";
            PrimefacesContextUI.actualizar("form:datosVAVigencia");
            banderaVA = 1;
        } else if (banderaVA == 1) {
            cerrarFiltrado();
        }
    }

    //SALIR
    /**
     * Metodo que cierra la sesion y limpia los datos en la pagina
     */
    public void salir() {
        cerrarFiltrado();
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        listVABorrar.clear();
        listVACrear.clear();
        listVAModificar.clear();
        vigenciaSeleccionada = null;
        k = 0;
        fechaFin = null;
        fechaIni = null;
        listVigenciasAfiliaciones = null;
        guardado = true;
        vigenciaValidaciones = null;
        cambioVigenciaA = false;
        tipoActualizacion = -1;
    }
    //ASIGNAR INDEX PARA DIALOGOS COMUNES (LDN = LISTA - NUEVO - DUPLICADO) (list = ESTRUCTURAS - MOTIVOSLOCALIZACIONES - PROYECTOS)

    /**
     * Metodo que ejecuta los dialogos de estructuras, motivos localizaciones,
     * proyectos
     *
     * @param indice Fila de la tabla
     * @param dlg Dialogo
     * @param LND Tipo actualizacion = LISTA - NUEVO - DUPLICADO
     * VigenciaProrrateoProyecto
     */
    public void asignarIndex(VigenciasAfiliaciones va, int dlg, int LND) {
        System.out.println("va" + va);
        RequestContext context = RequestContext.getCurrentInstance();
        vigenciaSeleccionada = va;
        tipoActualizacion = 0;

        if (dlg == 0) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            modificarInfoRegistroTipoEntidad(listTiposEntidades.size());
            tipoEntidadSeleccionado = null;
            PrimefacesContextUI.actualizar("form:TipoEntidadDialogo");
            PrimefacesContextUI.ejecutar("PF('TipoEntidadDialogo').show()");
        } else if (dlg == 1) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            modificarInfoRegistroTercero(listTerceros.size());
            terceroSeleccionado = null;
            PrimefacesContextUI.actualizar("form:TerceroDialogo");
            PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
        } else if (dlg == 2) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            modificarInfoRegistroEstado(listEstadosAfiliaciones.size());
            estadoSSeleccionado = null;
            PrimefacesContextUI.actualizar("form:EstadoAfiliacionDialogo");
            PrimefacesContextUI.ejecutar("PF('EstadoAfiliacionDialogo').show()");
        }
    }

    public void asignarIndex(int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 1) {
            tipoActualizacion = 1;
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            getInfoRegistroTipoEntidad();
            PrimefacesContextUI.actualizar("form:TipoEntidadDialogo");
            PrimefacesContextUI.ejecutar("PF('TipoEntidadDialogo').show()");
        } else if (dlg == 1) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            getInfoRegistroTercero();
            PrimefacesContextUI.actualizar("form:TerceroDialogo");
            PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
        } else if (dlg == 2) {
            activarLOV = false;
            PrimefacesContextUI.actualizar("form:listaValores");
            getInfoRegistroEstado();
            PrimefacesContextUI.actualizar("form:EstadoAfiliacionDialogo");
            PrimefacesContextUI.ejecutar("PF('EstadoAfiliacionDialogo').show()");
        }
    }

    //Motivo Localizacion
    /**
     * Metodo que actualiza el motivo localizacion seleccionado (vigencia
     * localizacion)
     */
    public void actualizarTipoEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        activarLOV = false;
        PrimefacesContextUI.actualizar("form:listaValores");
        if (tipoActualizacion == 0) {
            vigenciaValidaciones = new VigenciasAfiliaciones();
            vigenciaValidaciones.setTipoentidad(tipoEntidadSeleccionado);
            tipoValidacion = 1;//Tipo Entidad
            boolean cambio = validarModificacionRegistroTabla();
            if (cambio == true) {
                vigenciaSeleccionada.setTipoentidad(tipoEntidadSeleccionado);
                if (!listVACrear.contains(vigenciaSeleccionada)) {
                    if (listVAModificar.isEmpty()) {
                        listVAModificar.add(vigenciaSeleccionada);
                    } else if (!listVAModificar.contains(vigenciaSeleccionada)) {
                        listVAModificar.add(vigenciaSeleccionada);
                    }
                }
                if (guardado) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
                cambioVigenciaA = true;
                permitirIndexVA = true;
            }

            PrimefacesContextUI.actualizar(":form:editarTipoEntidadVA");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaA.setTipoentidad(tipoEntidadSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaTipoEntidadVA");
        } else if (tipoActualizacion == 2) {
            duplicarVA.setTipoentidad(tipoEntidadSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarTipoEntidadVA");
        }
        filtrarTiposEntidades = null;
        tipoEntidadSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("form:lovTipoEntidad:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoEntidad').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TipoEntidadDialogo').hide()");
        PrimefacesContextUI.actualizar("form:datosVAVigencia");

    }

    /**
     * Metodo que cancela la seleccion del motivo localizacion (vigencia
     * localizacion)
     */
    public void cancelarCambioTipoEntidad() {
        filtrarTiposEntidades = null;
        tipoEntidadSeleccionado = null;
        aceptar = true;
        //vigenciaSeleccionada = null;
        tipoActualizacion = -1;
        permitirIndexVA = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTipoEntidad:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTipoEntidad').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TipoEntidadDialogo').hide()");

    }

    public void actualizarTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        boolean banderaEncuentra = false;//booleano que se activa 
        activarLOV = false;
        PrimefacesContextUI.actualizar("form:listaValores");
        if (tipoActualizacion == 0) {//Si la actualizacion no es para nuevo registro o duplicar
            vigenciaValidaciones = new VigenciasAfiliaciones();
            vigenciaValidaciones.setTercerosucursal(new TercerosSucursales());
            vigenciaValidaciones.getTercerosucursal().setTercero(new Terceros());
            vigenciaValidaciones.getTercerosucursal().setTercero(terceroSeleccionado);
            //booleano cambio que determina si las validaciones se cumplieron
            tipoValidacion = 2;//Tipo Terceros
            boolean cambio = validarModificacionRegistroTabla();
            int posicion = -1;
            List<TercerosSucursales> listTercerosSucursales = administrarVigenciasAfiliaciones3.listTercerosSucursales();
            for (int i = 0; i < listTercerosSucursales.size(); i++) {
                if (listTercerosSucursales.get(i).getTercero().getNombre().equalsIgnoreCase(terceroSeleccionado.getNombre())) {
                    banderaEncuentra = true;
                    posicion = i;
                }
            }
            if (cambio) {
                if ((banderaEncuentra == true) && (posicion != -1)) {
                    vigenciaSeleccionada.setTercerosucursal(listTercerosSucursales.get(posicion));
                }
                if (!listVACrear.contains(vigenciaSeleccionada)) {
                    if (listVAModificar.isEmpty()) {
                        listVAModificar.add(vigenciaSeleccionada);
                    } else if (!listVAModificar.contains(vigenciaSeleccionada)) {
                        listVAModificar.add(vigenciaSeleccionada);
                    }
                }
            }
            if (guardado) {
                guardado = false;
                PrimefacesContextUI.actualizar("form:ACEPTAR");
            }
            cambioVigenciaA = true;
            permitirIndexVA = true;
            PrimefacesContextUI.actualizar(":form:editarTerceroVA");
        } else if (tipoActualizacion == 1) {//Si es para un nuevo registro
            int posicion = -1;
            List<TercerosSucursales> listTercerosSucursales = administrarVigenciasAfiliaciones3.listTercerosSucursales();
            for (int i = 0; i < listTercerosSucursales.size(); i++) {
                if (listTercerosSucursales.get(i).getTercero().getNombre().equalsIgnoreCase(terceroSeleccionado.getNombre())) {
                    banderaEncuentra = true;
                    posicion = i;
                }
            }
            if (banderaEncuentra && (posicion != -1)) {
                nuevaVigenciaA.setTercerosucursal(listTercerosSucursales.get(posicion));
            }
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaTerceroVA");
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaNITTerceroVA");
        } else if (tipoActualizacion == 2) {// Si es para duplicar
            int posicion = -1;
            List<TercerosSucursales> listTercerosSucursales = administrarVigenciasAfiliaciones3.listTercerosSucursales();
            for (int i = 0; i < listTercerosSucursales.size(); i++) {
                if (listTercerosSucursales.get(i).getTercero().getNombre().equalsIgnoreCase(terceroSeleccionado.getNombre())) {
                    banderaEncuentra = true;
                    posicion = i;
                }
            }
            if (banderaEncuentra && (posicion != -1)) {
                duplicarVA.setTercerosucursal(listTercerosSucursales.get(posicion));
            }
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarTerceroVA");
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarNITTerceroVA");
        }
        filtrarTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        // vigenciaSeleccionada = null;
        tipoActualizacion = -1;
        context.reset("form:lovTercero:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTercero').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TerceroDialogo').hide()");
        PrimefacesContextUI.actualizar("form:datosVAVigencia");
    }

    /**
     * Metodo que cancela la seleccion del proyecto (vigencia localizacion)
     */
    public void cancelarCambioTerceros() {
        filtrarTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        // vigenciaSeleccionada = null;
        tipoActualizacion = -1;
        permitirIndexVA = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovTercero:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovTercero').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('TerceroDialogo').hide()");
    }

    public void actualizarEstadoAfiliacion() {
        RequestContext context = RequestContext.getCurrentInstance();
        activarLOV = false;
        PrimefacesContextUI.actualizar("form:listaValores");
        if (tipoActualizacion == 0) {
            //if (tipoListaVA == 0) {
            vigenciaValidaciones = new VigenciasAfiliaciones();
            vigenciaValidaciones.setEstadoafiliacion(estadoSSeleccionado);
            // vigenciaSeleccionada.setEstadoafiliacion(estadoSSeleccionado);
            tipoValidacion = 3;//Estado Afiliacion
            boolean cambio = validarModificacionRegistroTabla();
            if (cambio == true) {
                vigenciaSeleccionada.setEstadoafiliacion(estadoSSeleccionado);
                if (!listVACrear.contains(vigenciaSeleccionada)) {
                    if (listVAModificar.isEmpty()) {
                        listVAModificar.add(vigenciaSeleccionada);
                    } else if (!listVAModificar.contains(vigenciaSeleccionada)) {
                        listVAModificar.add(vigenciaSeleccionada);
                    }
                }

                if (guardado) {
                    guardado = false;
                    PrimefacesContextUI.actualizar("form:ACEPTAR");
                }
                cambioVigenciaA = true;
                permitirIndexVA = true;
            }
            PrimefacesContextUI.actualizar(":form:editarEstadoAfiliacionVA");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaA.setEstadoafiliacion(estadoSSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:nuevaEstadoAfiliacionVA");
        } else if (tipoActualizacion == 2) {
            duplicarVA.setEstadoafiliacion(estadoSSeleccionado);
            PrimefacesContextUI.actualizar("formularioDialogos:duplicarEstadoAfiliacionVA");
        }
        filtrarEstadosAfiliaciones = null;
        estadoSSeleccionado = null;
        aceptar = true;
        //vigenciaSeleccionada = null;
        tipoActualizacion = -1;
        context.reset("form:lovEstadoAfiliacion:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEstadoAfiliacion').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EstadoAfiliacionDialogo').hide()");
        PrimefacesContextUI.actualizar("form:datosVAVigencia");
        //}
    }

    /**
     * Metodo que cancela la seleccion del motivo localizacion (vigencia
     * localizacion)
     */
    public void cancelarCambioEstadoAfiliacion() {
        filtrarEstadosAfiliaciones = null;
        estadoSSeleccionado = null;
        aceptar = true;
        //vigenciaSeleccionada = null;
        tipoActualizacion = -1;
        permitirIndexVA = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovEstadoAfiliacion:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovEstadoAfiliacion').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('EstadoAfiliacionDialogo').hide()");
    }

    ///////////////////////////////////////////////////////////////////////
    /**
     *
     * //LISTA DE VALORES DINAMICA /** Metodo que activa la lista de valores de
     * todas las tablas con respecto al index activo y la columna activa
     */
    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (vigenciaSeleccionada != null) {
            if (cualCeldaVA == 2) {
                modificarInfoRegistroTipoEntidad(listTiposEntidades.size());
                PrimefacesContextUI.actualizar("form:TipoEntidadDialogo");
                PrimefacesContextUI.ejecutar("PF('TipoEntidadDialogo').show()");
                tipoEntidadSeleccionado = null;
                tipoActualizacion = 0;
            }
            if (cualCeldaVA == 3) {
                modificarInfoRegistroTercero(listTerceros.size());
                PrimefacesContextUI.actualizar("form:TerceroDialogo");
                PrimefacesContextUI.ejecutar("PF('TerceroDialogo').show()");
                terceroSeleccionado = null;
                tipoActualizacion = 0;
            }
            if (cualCeldaVA == 6) {
                modificarInfoRegistroEstado(listEstadosAfiliaciones.size());
                PrimefacesContextUI.actualizar("form:EstadoAfiliacionDialogo");
                PrimefacesContextUI.ejecutar("PF('EstadoAfiliacionDialogo').show()");
                estadoSSeleccionado = null;
                tipoActualizacion = 0;
            }
        }

    }

    /**
     * Valida un proceso de nuevo registro dentro de la pagina con respecto a la
     * posicion en la pagina
     */
    public void validarNuevoRegistro() {
//        if (cambioVigenciaA == false) {
        nuevaVigenciaA = new VigenciasAfiliaciones();
        nuevaVigenciaA.setTipoentidad(new TiposEntidades());
        nuevaVigenciaA.setTercerosucursal(new TercerosSucursales());
        nuevaVigenciaA.setEstadoafiliacion(new EstadosAfiliaciones());
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("formularioDialogos:NuevoRegistroVA");
        PrimefacesContextUI.ejecutar("PF('NuevoRegistroVA').show()");
//        } else {
//            RequestContext context = RequestContext.getCurrentInstance();
//            PrimefacesContextUI.actualizar("form:confirmarGuardarSinSalir");
//            PrimefacesContextUI.ejecutar("PF('confirmarGuardarSinSalir').show()");
//        }
    }

    /**
     * Metodo que activa el boton aceptar de la pagina y los dialogos
     */
    public void activarAceptar() {
        aceptar = false;
    }
    //EXPORTAR

    /**
     * Valida la tabla a exportar en PDF con respecto al index activo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void validarExportPDF() throws IOException {
        exportPDFVA();
    }

    /**
     * Metodo que exporta datos a PDF Vigencia Prorrateo
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDFVA() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVA:datosVAVigenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasAfiliacionesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    /**
     * Verifica que tabla exportar XLS con respecto al index activo
     *
     * @throws IOException
     */
    public void verificarExportXLS() throws IOException {
        exportXLSVA();
    }

    /**
     * Metodo que exporta datos a XLS Vigencia Afiliaciones
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLSVA() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarVA:datosVAVigenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasAfiliacionesXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
    public void verificarRastro() {
        System.out.println("ControlEmplVigenciaAfiliacion3.verificarRastro");
//        RequestContext context = RequestContext.getCurrentInstance();
//        if (!listVigenciasAfiliaciones.isEmpty()) {
//            System.out.println("LA LISTA SI TIENE ALGO");
//            //   if (listVigenciasAfiliaciones.size() > 0) {
//            if (vigenciaSeleccionada.getSecuencia() != null) {
//                int resultado = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASAFILIACIONES");
//                if (resultado == 1) {
//                    PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
//                } else if (resultado == 2) {
//                    System.out.println("ControlEmplVigenciaAfiliacion3.verificarRastro");
//                    System.out.println("La secuencia para la tabla es: " + vigenciaSeleccionada.getSecuencia());
//                    PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
//                } else if (resultado == 3) {
//                    PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
//                } else if (resultado == 4) {
//                    PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
//                } else if (resultado == 5) {
//                    PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
//                }
//            } else {
//                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
//            }
//        } else {
//            System.out.println("LA LISTA ESTA VACIA");
//            if (administrarRastros.verificarHistoricosTabla("VIGENCIASAFILIACIONES")) {
//                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
//            } else {
//                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
//            }
//        }

        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASAFILIACIONES");
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
            if (administrarRastros.verificarHistoricosTabla("VIGENCIASAFILIACIONES")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }
        }
    }

    //EVENTO FILTRAR
    /**
     * Evento que cambia la lista real a la filtrada
     */
    public void eventoFiltrar() {
        if (tipoListaVA == 0) {
            tipoListaVA = 1;
        }
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
        modificarInfoRegistro(filtrarVigenciasAfiliaciones.size());
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:informacionRegistro");
    }

    public void eventoFiltrarTipoEntidad() {
        modificarInfoRegistroTipoEntidad(filtrarTiposEntidades.size());
        PrimefacesContextUI.actualizar("form:infoRegistroTipoEntidad");
    }

    public void eventoFiltrarTercero() {
        modificarInfoRegistroTercero(filtrarTerceros.size());
        RequestContext context = RequestContext.getCurrentInstance();
        PrimefacesContextUI.actualizar("form:infoRegistroTercero");
    }

    public void eventoFiltrarEstado() {
        modificarInfoRegistroEstado(filtrarEstadosAfiliaciones.size());
        PrimefacesContextUI.actualizar("form:infoRegistroEstado");
    }

    private void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
    }

    private void modificarInfoRegistroTipoEntidad(int valor) {
        infoRegistroTipoEntidad = String.valueOf(valor);
    }

    private void modificarInfoRegistroTercero(int valor) {
        infoRegistroTercero = String.valueOf(valor);
    }

    private void modificarInfoRegistroEstado(int valor) {
        infoRegistroEstado = String.valueOf(valor);
    }

    public void contarRegistrosVA() {
        if (listVigenciasAfiliaciones != null) {
            modificarInfoRegistro(listVigenciasAfiliaciones.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void recordarSeleccion() {
        if (vigenciaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVAVigencia");
            tablaC.setSelection(vigenciaSeleccionada);
        } else {
            vigenciaSeleccionada = null;
        }
        //System.out.println("vigenciaSeleccionada: " + vigenciaSeleccionada);
    }

    public void anularLOV() {
        activarLOV = true;
        PrimefacesContextUI.actualizar("form:listaValores");
    }

    //GET - SET 
    public List<VigenciasAfiliaciones> getListVigenciasAfiliaciones() {
        try {
            if (listVigenciasAfiliaciones == null) {
                listVigenciasAfiliaciones = new ArrayList<VigenciasAfiliaciones>();
                if (empleado != null) {
                    listVigenciasAfiliaciones = administrarVigenciasAfiliaciones3.listVigenciasAfiliacionesEmpleado(empleado.getSecuencia());

                    for (int i = 0; i < listVigenciasAfiliaciones.size(); i++) {

                        if (listVigenciasAfiliaciones.get(i).getEstadoafiliacion() == null) {
                            listVigenciasAfiliaciones.get(i).setEstadoafiliacion(new EstadosAfiliaciones());
                            listVigenciasAfiliaciones.get(i).getEstadoafiliacion().setNombre("");
                        } else if (listVigenciasAfiliaciones.get(i).getEstadoafiliacion().getNombre() == null) {
                            listVigenciasAfiliaciones.get(i).getEstadoafiliacion().setNombre("");
                        }

                        if (listVigenciasAfiliaciones.get(i).getTercerosucursal().getTercero() == null) {
                            listVigenciasAfiliaciones.get(i).getTercerosucursal().setTercero(new Terceros());
                        }
                        if (listVigenciasAfiliaciones.get(i).getTipoentidad() == null) {
                            listVigenciasAfiliaciones.get(i).setTipoentidad(new TiposEntidades());
                        }
                    }
                }
            }
            return listVigenciasAfiliaciones;
        } catch (Exception e) {
            System.out.println("Error en getListVigenciasAfiliaciones : " + e.toString());
            return null;
        }
    }

    public void setListVigenciasAfiliaciones(List<VigenciasAfiliaciones> listVigenciasAfiliaciones) {
        this.listVigenciasAfiliaciones = listVigenciasAfiliaciones;
    }

    public List<VigenciasAfiliaciones> getFiltrarVigenciasAfiliaciones() {
        return filtrarVigenciasAfiliaciones;
    }

    public void setFiltrarVigenciasAfiliaciones(List<VigenciasAfiliaciones> filtrarVigenciasAfiliaciones) {
        this.filtrarVigenciasAfiliaciones = filtrarVigenciasAfiliaciones;
    }

    public List<TiposEntidades> getListTiposEntidades() {
        if (listTiposEntidades == null) {
            listTiposEntidades = administrarVigenciasAfiliaciones3.listTiposEntidades();
        }
        return listTiposEntidades;
    }

    public void setListTiposEntidades(List<TiposEntidades> listTiposEntidades) {
        this.listTiposEntidades = listTiposEntidades;
    }

    public TiposEntidades getTipoEntidadSeleccionado() {
        return tipoEntidadSeleccionado;
    }

    public void setTipoEntidadSeleccionado(TiposEntidades tipoEntidadSeleccionado) {
        this.tipoEntidadSeleccionado = tipoEntidadSeleccionado;
    }

    public List<TiposEntidades> getFiltrarTiposEntidades() {
        return filtrarTiposEntidades;
    }

    public void setFiltrarTiposEntidades(List<TiposEntidades> filtrarTiposEntidades) {
        this.filtrarTiposEntidades = filtrarTiposEntidades;
    }

    public List<Terceros> getListTerceros() {
        if (listTerceros == null) {
            listTerceros = administrarVigenciasAfiliaciones3.listTerceros();
        }
        return listTerceros;
    }

    public void setListTerceros(List<Terceros> listTerceros) {
        this.listTerceros = listTerceros;
    }

    public Terceros getTerceroSeleccionado() {
        return terceroSeleccionado;
    }

    public void setTerceroSeleccionado(Terceros terceroSeleccionado) {
        this.terceroSeleccionado = terceroSeleccionado;
    }

    public List<Terceros> getFiltrarTerceros() {
        return filtrarTerceros;
    }

    public void setFiltrarTerceros(List<Terceros> filtrarTerceros) {
        this.filtrarTerceros = filtrarTerceros;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public List<VigenciasAfiliaciones> getListVAModificar() {
        return listVAModificar;
    }

    public void setListVAModificar(List<VigenciasAfiliaciones> listVAModificar) {
        this.listVAModificar = listVAModificar;
    }

    public VigenciasAfiliaciones getNuevaVigenciaA() {
        return nuevaVigenciaA;
    }

    public void setNuevaVigenciaA(VigenciasAfiliaciones nuevaVigenciaA) {
        this.nuevaVigenciaA = nuevaVigenciaA;
    }

    public List<VigenciasAfiliaciones> getListVABorrar() {
        return listVABorrar;
    }

    public void setListVABorrar(List<VigenciasAfiliaciones> listVABorrar) {
        this.listVABorrar = listVABorrar;
    }

    public VigenciasAfiliaciones getEditarVA() {
        return editarVA;
    }

    public void setEditarVA(VigenciasAfiliaciones editarVA) {
        this.editarVA = editarVA;
    }

    public VigenciasAfiliaciones getDuplicarVA() {
        return duplicarVA;
    }

    public void setDuplicarVA(VigenciasAfiliaciones duplicarVA) {
        this.duplicarVA = duplicarVA;
    }

    public List<VigenciasAfiliaciones> getListVACrear() {
        return listVACrear;
    }

    public void setListVACrear(List<VigenciasAfiliaciones> listVACrear) {
        this.listVACrear = listVACrear;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public List<EstadosAfiliaciones> getListEstadosAfiliaciones() {
        try {
            listEstadosAfiliaciones = new ArrayList<EstadosAfiliaciones>();
            listEstadosAfiliaciones = administrarVigenciasAfiliaciones3.listEstadosAfiliaciones();

            return listEstadosAfiliaciones;
        } catch (Exception e) {
            return null;
        }
//        if (listEstadosAfiliaciones == null) {
//            listEstadosAfiliaciones = administrarVigenciasAfiliaciones3.listEstadosAfiliaciones();
//        }
//        return listEstadosAfiliaciones;
    }

    public void setListEstadosAfiliaciones(List<EstadosAfiliaciones> listEstadosAfiliaciones) {
        this.listEstadosAfiliaciones = listEstadosAfiliaciones;
    }

    public EstadosAfiliaciones getEstadoSSeleccionado() {
        return estadoSSeleccionado;
    }

    public void setEstadoSSeleccionado(EstadosAfiliaciones estadoSSeleccionado) {
        this.estadoSSeleccionado = estadoSSeleccionado;
    }

    public List<EstadosAfiliaciones> getFiltrarEstadosAfiliaciones() {
        return filtrarEstadosAfiliaciones;
    }

    public void setFiltrarEstadosAfiliaciones(List<EstadosAfiliaciones> filtrarEstadosAfiliaciones) {
        this.filtrarEstadosAfiliaciones = filtrarEstadosAfiliaciones;
    }

    public VigenciasAfiliaciones getVigenciaSeleccionada() {
        return vigenciaSeleccionada;
    }

    public void setVigenciaSeleccionada(VigenciasAfiliaciones vigenciaSeleccionada) {
        this.vigenciaSeleccionada = vigenciaSeleccionada;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public String getInfoRegistroTipoEntidad() {
        return infoRegistroTipoEntidad;
    }

    public String getInfoRegistroTercero() {
        return infoRegistroTercero;

    }

    public String getInfoRegistroEstado() {
        return infoRegistroEstado;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
