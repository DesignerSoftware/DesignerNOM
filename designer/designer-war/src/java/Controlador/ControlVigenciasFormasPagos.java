/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import utilidadesUI.PrimefacesContextUI;
import Entidades.Empleados;
import Entidades.MetodosPagos;
import Entidades.Periodicidades;
import Entidades.Sucursales;
import Entidades.VigenciasFormasPagos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmplVigenciasFormasPagosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
public class ControlVigenciasFormasPagos implements Serializable {

    @EJB
    AdministrarEmplVigenciasFormasPagosInterface administrarEmplVigenciasFormasPagos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private BigInteger secuenciaEmpleado;
    private Empleados empleadoSeleccionado;
    private List<VigenciasFormasPagos> listVigenciasFormasPagosPorEmpleado;
    private List<VigenciasFormasPagos> filtrarVigenciasFormasPagosPorEmpleado;
    private List<VigenciasFormasPagos> borrarVFP;
    private List<VigenciasFormasPagos> crearVFP;
    private List<VigenciasFormasPagos> modificarVFP;
    private VigenciasFormasPagos editarVigenciaFormasPagoPorEmpleado;
    private VigenciasFormasPagos nuevaVigenciaFormasPago;
    private VigenciasFormasPagos duplicarVigenciaFormasPago;
    private VigenciasFormasPagos vigenciaSeleccionada;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
//lista estados de valores
    private List<Sucursales> listaSucursales;
    private List<Sucursales> filtradoSucursales;
    private Sucursales sucursalSeleccionada;
    private List<Periodicidades> listaPeriodicidades;
    private List<Periodicidades> filtradoPeriodicidades;
    private Periodicidades PeriodicidadSeleccionada;
    private List<MetodosPagos> listaMetodosPagos;
    private List<MetodosPagos> filtradoMetodosPagos;
    private MetodosPagos metodoPagoSeleccionado;
    //Variables Autompletar
    private String backUpSucursales, periodicidad, metodosPagos, mensajeValidacion;
    private boolean permitirIndex;
//COLUMNAS
    private Column fechaVigencia, cuenta, fechaCuenta, sucursal, formaPago, tc, metodoPago;
    public String altoTabla;
    public String infoRegistro;
    private String infoRegistroMetodosPagos;
    private DataTable tablaC;
    //
    private boolean activarLOV;

    public ControlVigenciasFormasPagos() {
        empleadoSeleccionado = null;
        secuenciaEmpleado = BigInteger.valueOf(10664356);
        listVigenciasFormasPagosPorEmpleado = null;
        crearVFP = new ArrayList();
        modificarVFP = new ArrayList();
        borrarVFP = new ArrayList();
        listaSucursales = null;
        listaPeriodicidades = null;
        listaMetodosPagos = null;
        //
        permitirIndex = true;
        vigenciaSeleccionada = null;
        //
        editarVigenciaFormasPagoPorEmpleado = new VigenciasFormasPagos();
        //
        nuevaVigenciaFormasPago = new VigenciasFormasPagos();
        nuevaVigenciaFormasPago.setFormapago(new Periodicidades());
        nuevaVigenciaFormasPago.setSucursal(new Sucursales());
        nuevaVigenciaFormasPago.setMetodopago(new MetodosPagos());
        guardado = true;
        altoTabla = "292";
        aceptar = true;
        activarLOV = true;

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEmplVigenciasFormasPagos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlVigenciasCargos: " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger sec) {
        RequestContext context = RequestContext.getCurrentInstance();
        empleadoSeleccionado = null;
        secuenciaEmpleado = sec;
        //listVigenciasFormasPagosPorEmpleado = null;
        getListVigenciasFormasPagosPorEmpleado();
        contarRegistrosFormaPago();
        if (listVigenciasFormasPagosPorEmpleado != null) {
            vigenciaSeleccionada = listVigenciasFormasPagosPorEmpleado.get(0);
            modificarInfoRegistro(listVigenciasFormasPagosPorEmpleado.size());
        } else {
            modificarInfoRegistro(0);
        }
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //RASTROS 
    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (vigenciaSeleccionada != null) {
            int result = administrarRastros.obtenerTabla(vigenciaSeleccionada.getSecuencia(), "VIGENCIASUBICACIONES");
            if (result == 1) {
                PrimefacesContextUI.ejecutar("PF('errorObjetosDB').show()");
            } else if (result == 2) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastro').show()");
            } else if (result == 3) {
                PrimefacesContextUI.ejecutar("PF('errorRegistroRastro').show()");
            } else if (result == 4) {
                PrimefacesContextUI.ejecutar("PF('errorTablaConRastro').show()");
            } else if (result == 5) {
                PrimefacesContextUI.ejecutar("PF('errorTablaSinRastro').show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("VIGENCIASUBICACIONES")) {
                PrimefacesContextUI.ejecutar("PF('confirmarRastroHistorico').show()");
            } else {
                PrimefacesContextUI.ejecutar("PF('errorRastroHistorico').show()");
            }

        }
        vigenciaSeleccionada = null;
    }

//Ubicacion Celda.
    /**
     * Metodo que obtiene la posicion dentro de la tabla VigenciasLocalizaciones
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    public void cambiarIndice(VigenciasFormasPagos vFormas, int celda) {
        if (permitirIndex) {
            vigenciaSeleccionada = vFormas;
            cualCelda = celda;
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");

            if (cualCelda == 3) {
                activarLOV = false;
                RequestContext.getCurrentInstance().update("form:listaValores");
                backUpSucursales = vigenciaSeleccionada.getSucursal().getNombre();
            } else if (cualCelda == 4) {
                activarLOV = false;
                RequestContext.getCurrentInstance().update("form:listaValores");
                periodicidad = vigenciaSeleccionada.getFormapago().getNombre();
            } else if (cualCelda == 6) {
                activarLOV = false;
                RequestContext.getCurrentInstance().update("form:listaValores");
                metodosPagos = vigenciaSeleccionada.getMetodopago().getDescripcion();
            }
        }
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        } else {
            if (vigenciaSeleccionada != null) {
                if (cualCelda == 3) {
                    sucursalSeleccionada = null;
                    modificarInfoRegistroSucursales(listaSucursales.size());
                    RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
                    PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                    tipoActualizacion = 0;
                }
                if (cualCelda == 4) {
                    PeriodicidadSeleccionada = null;
                    modificarInfoRegistroPeriodicidad(listaPeriodicidades.size());
                    RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
                    PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
                    tipoActualizacion = 0;
                }
                if (cualCelda == 6) {
                    metodoPagoSeleccionado = null;
                    modificarInfoRegistroMetodoPago(listaMetodosPagos.size());
                    RequestContext.getCurrentInstance().update("form:metodosPagosialogo");
                    PrimefacesContextUI.ejecutar("PF('metodosPagosialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
            }
        }
    }

    /**
     *
     * @param indice
     * @param LND
     * @param dig muestra el dialogo respectivo
     */
    public void asignarIndex(VigenciasFormasPagos vFormas, int LND, int dig) {
        try {
            vigenciaSeleccionada = vFormas;
            RequestContext context = RequestContext.getCurrentInstance();
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            if (LND == 0) {
                tipoActualizacion = 0;
            } else if (LND == 1) {
                tipoActualizacion = 1;
            } else if (LND == 2) {
                tipoActualizacion = 2;
            }

            if (dig == 3) {
                sucursalSeleccionada = null;
                modificarInfoRegistroSucursales(listaSucursales.size());
                RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
                PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                dig = -1;
            }
            if (dig == 4) {
                PeriodicidadSeleccionada = null;
                modificarInfoRegistroPeriodicidad(listaPeriodicidades.size());
                RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
                PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
                dig = -1;
            }
            if (dig == 6) {
                metodoPagoSeleccionado = null;
                modificarInfoRegistroMetodoPago(listaMetodosPagos.size());
                RequestContext.getCurrentInstance().update("form:metodosPagosialogo");
                PrimefacesContextUI.ejecutar("PF('metodosPagosialogo').show()");
                dig = -1;
            }
        } catch (Exception e) {
            System.out.println("ERROR ControlVigenciasAfiliaciones3.asignarIndex ERROR======" + e.getMessage());
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    /**
     *
     */
    public void guardarCambiosVigenciasFormasPagos() {
        if (guardado == false) {
            if (!borrarVFP.isEmpty()) {

                administrarEmplVigenciasFormasPagos.borrarVigenciasFormasPagos(borrarVFP);
                borrarVFP.clear();
            }
            if (!crearVFP.isEmpty()) {
                for (int i = 0; i < crearVFP.size(); i++) {
                    if (crearVFP.get(i).getSucursal() != null) {
                        if (crearVFP.get(i).getSucursal().getSecuencia() == null) {
                            crearVFP.get(i).setSucursal(null);
                        }
                        administrarEmplVigenciasFormasPagos.crearVigencasFormasPagos(crearVFP.get(i));
                    } else {
                        mensajeValidacion = "Fecha Inicial";
                        RequestContext context = RequestContext.getCurrentInstance();
                        RequestContext.getCurrentInstance().update("form:validacioNuevaVigencia");
                        PrimefacesContextUI.ejecutar("PF('validacioNuevaVigencia').show()");
                    }
                }
                crearVFP.clear();
            }
            if (!modificarVFP.isEmpty()) {
                administrarEmplVigenciasFormasPagos.modificarVigenciasFormasPagos(modificarVFP);
                modificarVFP.clear();
            }
            listVigenciasFormasPagosPorEmpleado = null;
            getListVigenciasFormasPagosPorEmpleado();
            contarRegistrosFormaPago();
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            k = 0;
            guardado = true;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    /**
     *
     * @param indice donde se encuentra posicionado
     * @param confirmarCambio nombre de la columna
     * @param valorConfirmar valor ingresado
     */
    public void modificarVigenciasFormasPagos(VigenciasFormasPagos vFormas, String confirmarCambio, String valorConfirmar) {
        vigenciaSeleccionada = vFormas;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        int contador = 0;
        boolean banderita = false;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            System.err.println("Fecha a modificar " + vigenciaSeleccionada.getFechavigencia());
            for (int z = 0; z < listVigenciasFormasPagosPorEmpleado.size(); z++) {
                System.err.println("Contador vigenciasformas pagos modificar " + z);
                if (vigenciaSeleccionada != listVigenciasFormasPagosPorEmpleado.get(z)) {
                    if (vigenciaSeleccionada.getFechavigencia().equals(listVigenciasFormasPagosPorEmpleado.get(z).getFechavigencia())) {
                        contador++;
                        z = listVigenciasFormasPagosPorEmpleado.size();
                    }
                }
            }
            if (contador > 0) {
                mensajeValidacion = "Fechas Repetidas";
                RequestContext.getCurrentInstance().update("form:validacioModificarVigenciaFechas");
                PrimefacesContextUI.ejecutar("PF('validacioModificarVigenciaFechas').show()");
                banderita = false;
            } else {
                banderita = true;
            }
            if (vigenciaSeleccionada.getMetodopago().getDescripcion().equals("TRANSFERENCIA")) {
                activarLOV = true;
                RequestContext.getCurrentInstance().update("form:listaValores");
                if (vigenciaSeleccionada.getTipocuenta().equals("")) {
                    mensajeValidacion = "Tipo cuenta";
                    RequestContext.getCurrentInstance().update("form:validacioModificarMetodoPago");
                    PrimefacesContextUI.ejecutar("PF('validacioModificarMetodoPago').show()");
                    banderita = false;
                } else {
                    banderita = true;
                }

                if (vigenciaSeleccionada.getCuenta().equals("")) {
                    mensajeValidacion = "Cuenta";
                    RequestContext.getCurrentInstance().update("form:validacioModificarMetodoPago");
                    PrimefacesContextUI.ejecutar("PF('validacioModificarMetodoPago').show()");
                    banderita = false;
                } else {
                    banderita = true;
                }
            }
            if (banderita == true) {

                if (!crearVFP.contains(vigenciaSeleccionada)) {
                    if (modificarVFP.isEmpty()) {
                        modificarVFP.add(vigenciaSeleccionada);
                    } else if (!modificarVFP.contains(vigenciaSeleccionada)) {
                        modificarVFP.add(vigenciaSeleccionada);
                    }
                    if (guardado) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");

                    }
                }
            }
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
        } else if (confirmarCambio.equalsIgnoreCase("SUCURSAL")) {
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            if (!valorConfirmar.isEmpty()) {
                if (!vigenciaSeleccionada.getSucursal().getNombre().equals("")) {
                    vigenciaSeleccionada.getSucursal().setNombre(backUpSucursales);
                    for (int i = 0; i < listaSucursales.size(); i++) {
                        if (listaSucursales.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                            indiceUnicoElemento = i;
                            coincidencias++;
                        }
                    }

                    if (coincidencias == 1) {
                        vigenciaSeleccionada.setSucursal(listaSucursales.get(indiceUnicoElemento));
                        listaSucursales.clear();
                        getListaSucursales();
                        //getListaTiposFamiliares();

                    } else {
                        permitirIndex = false;
                        RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
                        PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                        tipoActualizacion = 0;
                    }
                } else {
                    vigenciaSeleccionada.getSucursal().setNombre(backUpSucursales);
                    vigenciaSeleccionada.setSucursal(new Sucursales());
                    coincidencias = 1;
                    tipoActualizacion = 0;
                }
                if (coincidencias == 1) {
                    if (!crearVFP.contains(vigenciaSeleccionada)) {
                        if (modificarVFP.isEmpty()) {
                            modificarVFP.add(vigenciaSeleccionada);
                        } else if (!modificarVFP.contains(vigenciaSeleccionada)) {
                            modificarVFP.add(vigenciaSeleccionada);
                        }
                        if (guardado) {
                            guardado = false;
                        }
                    }
                }

                RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
                RequestContext.getCurrentInstance().update("form:ACEPTAR");

            } else {
                listaSucursales.clear();
                getListaSucursales();
                vigenciaSeleccionada.setSucursal(new Sucursales());
                RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else if (confirmarCambio.equalsIgnoreCase("FORMAPAGO")) {
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            vigenciaSeleccionada.getFormapago().setNombre(periodicidad);
            for (int i = 0; i < listaPeriodicidades.size(); i++) {
                if (listaPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaSeleccionada.setFormapago(listaPeriodicidades.get(indiceUnicoElemento));
                listaPeriodicidades.clear();
                listaPeriodicidades = null;
                getListaPeriodicidades();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
                PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
                RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
                tipoActualizacion = 0;
            }
        } else if (confirmarCambio.equalsIgnoreCase("METODOPAGO")) {
            activarLOV = false;
            RequestContext.getCurrentInstance().update("form:listaValores");
            vigenciaSeleccionada.getMetodopago().setDescripcion(metodosPagos);
            for (int i = 0; i < listaMetodosPagos.size(); i++) {
                if (listaMetodosPagos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                vigenciaSeleccionada.setMetodopago(listaMetodosPagos.get(indiceUnicoElemento));
                listaMetodosPagos.clear();
                listaMetodosPagos = null;
                getListaMetodosPagos();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("form:metodosPagosialogo");
                PrimefacesContextUI.ejecutar("PF('metodosPagosialogo').show()");
                RequestContext.getCurrentInstance().update("form:metodosPagosialogo");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
            if (!crearVFP.contains(vigenciaSeleccionada)) {
                if (modificarVFP.isEmpty()) {
                    modificarVFP.add(vigenciaSeleccionada);
                } else if (!modificarVFP.contains(vigenciaSeleccionada)) {
                    modificarVFP.add(vigenciaSeleccionada);
                }
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
        RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
        mensajeValidacion = "";
        int val = 0;
        if (vigenciaSeleccionada.getMetodopago().getDescripcion().equalsIgnoreCase("TRANSFERENCIA")) {
            if (vigenciaSeleccionada.getCuenta() == null) {
                mensajeValidacion = mensajeValidacion + " Cuenta ";
                System.err.println("modificarVigenciasFormasPagos mensaje validacion : " + mensajeValidacion);
                val++;
            }
            if (vigenciaSeleccionada.getSucursal().getNombre() == null) {
                mensajeValidacion = mensajeValidacion + " Sucursal ";
                val++;
                System.err.println("modificarVigenciasFormasPagos mensaje validacion : " + mensajeValidacion);

            }
            if (vigenciaSeleccionada.getTipocuenta() == null) {
                mensajeValidacion = mensajeValidacion + " Tipo de cuenta ";
                val++;
                System.err.println("modificarVigenciasFormasPagos mensaje validacion : " + mensajeValidacion);

            }
            if (val > 0) {
                System.err.println("modificarVigenciasFormasPagos mensaje validacion : " + mensajeValidacion);

                RequestContext.getCurrentInstance().update("form:validacioModificarMetodoPago");
                PrimefacesContextUI.ejecutar("PF('validacioModificarMetodoPago').show()");
            }
        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            cerrarFiltrado();
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        borrarVFP.clear();
        crearVFP.clear();
        modificarVFP.clear();
        vigenciaSeleccionada = null;
        k = 0;
        listVigenciasFormasPagosPorEmpleado = null;
        getListVigenciasFormasPagosPorEmpleado();
        contarRegistrosFormaPago();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void salir() {
        cerrarFiltrado();
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        borrarVFP.clear();
        crearVFP.clear();
        modificarVFP.clear();
        vigenciaSeleccionada = null;
        k = 0;
        listVigenciasFormasPagosPorEmpleado = null;
        guardado = true;
    }

    private void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        altoTabla = "292";
        //CERRAR FILTRADO
        fechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:fechaVigencia");
        fechaVigencia.setFilterStyle("display: none; visibility: hidden;");
        cuenta = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:cuenta");
        cuenta.setFilterStyle("display: none; visibility: hidden;");
        fechaCuenta = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:fechaCuenta");
        fechaCuenta.setFilterStyle("display: none; visibility: hidden;");
        sucursal = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:sucursal");
        sucursal.setFilterStyle("display: none; visibility: hidden;");
        formaPago = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:formaPago");
        formaPago.setFilterStyle("display: none; visibility: hidden;");
        tc = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:tc");
        tc.setFilterStyle("display: none; visibility: hidden;");
        metodoPago = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:metodoPago");
        metodoPago.setFilterStyle("display: none; visibility: hidden;");
        RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
        bandera = 0;
        filtrarVigenciasFormasPagosPorEmpleado = null;
        tipoLista = 0;
    }

    public void actualizarSucursal() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            vigenciaSeleccionada.setSucursal(sucursalSeleccionada);
            if (!crearVFP.contains(vigenciaSeleccionada)) {
                if (modificarVFP.isEmpty()) {
                    modificarVFP.add(vigenciaSeleccionada);
                } else if (!modificarVFP.contains(vigenciaSeleccionada)) {
                    modificarVFP.add(vigenciaSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormasPago.setSucursal(sucursalSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaFormasPagos");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormasPago.setSucursal(sucursalSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormasPagos");
        }
        filtradoPeriodicidades = null;
        sucursalSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("form:lovSucursales:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovSucursales').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').hide()");
    }

    public void cancelarCambioSucursal() {
        RequestContext context = RequestContext.getCurrentInstance();
        filtradoSucursales = null;
        sucursalSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        context.reset("form:lovSucursales:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovSucursales').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').hide()");
    }

    public void actualizarPeriodicidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            vigenciaSeleccionada.setFormapago(PeriodicidadSeleccionada);
            if (!crearVFP.contains(vigenciaSeleccionada)) {
                if (modificarVFP.isEmpty()) {
                    modificarVFP.add(vigenciaSeleccionada);
                } else if (!modificarVFP.contains(vigenciaSeleccionada)) {
                    modificarVFP.add(vigenciaSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormasPago.setFormapago(PeriodicidadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaFormasPagos");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormasPago.setFormapago(PeriodicidadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormasPagos");
        }
        filtradoPeriodicidades = null;
        PeriodicidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;
        context.reset("form:lovperiodicidades:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovperiodicidades').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').hide()");
    }

    public void cancelarCambioPeriodicidad() {
        filtradoPeriodicidades = null;
        PeriodicidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovperiodicidades:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovperiodicidades').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').hide()");
    }

    public void actualizarMetodoPago() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            vigenciaSeleccionada.setMetodopago(metodoPagoSeleccionado);
            if (!crearVFP.contains(vigenciaSeleccionada)) {
                if (modificarVFP.isEmpty()) {
                    modificarVFP.add(vigenciaSeleccionada);
                } else if (!modificarVFP.contains(vigenciaSeleccionada)) {
                    modificarVFP.add(vigenciaSeleccionada);
                }
            }
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            mensajeValidacion = "";
            int val = 0;
            //validacion cuando se cambia el valor a transferencia utilizando la lista de valores
            if (vigenciaSeleccionada.getMetodopago().getDescripcion().equalsIgnoreCase("TRANSFERENCIA")) {
                if (vigenciaSeleccionada.getCuenta() == null) {
                    mensajeValidacion = mensajeValidacion + " Cuenta ";
                    System.err.println("actualizarMetodoPago mensaje validacion : " + mensajeValidacion);
                    val++;
                }
                if (vigenciaSeleccionada.getSucursal().getNombre() == null) {
                    mensajeValidacion = mensajeValidacion + " Sucursal ";
                    val++;
                    System.err.println("actualizarMetodoPago mensaje validacion : " + mensajeValidacion);

                }
                if (vigenciaSeleccionada.getTipocuenta() == null) {
                    mensajeValidacion = mensajeValidacion + " Tipo de cuenta ";
                    val++;
                    System.err.println("actualizarMetodoPago mensaje validacion : " + mensajeValidacion);

                }
                if (val > 0) {
                    System.err.println("actualizarMetodoPago mensaje validacion : " + mensajeValidacion);

                    RequestContext.getCurrentInstance().update("form:validacionModificarMetodoPago");
                    PrimefacesContextUI.ejecutar("PF('validacionModificarMetodoPago').show()");
                }
            }
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
        } else if (tipoActualizacion == 1) {
            nuevaVigenciaFormasPago.setMetodopago(metodoPagoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaVigenciaFormasPagos");
        } else if (tipoActualizacion == 2) {
            duplicarVigenciaFormasPago.setMetodopago(metodoPagoSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormasPagos");
        }
        filtradoMetodosPagos = null;
        metodoPagoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        cualCelda = -1;

        context.reset("form:lovmetodospagos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovmetodospagos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('metodosPagosialogo').hide()");
    }

    public void cancelarCambioMetodoPago() {
        filtradoMetodosPagos = null;
        metodoPagoSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("form:lovmetodospagos:globalFilter");
        PrimefacesContextUI.ejecutar("PF('lovmetodospagos').clearFilters()");
        PrimefacesContextUI.ejecutar("PF('metodosPagosialogo').hide()");
    }

    //CTRL + F11 ACTIVAR/DESACTIVAR
    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            //Columnas de la Tabla  
            fechaVigencia = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:fechaVigencia");
            fechaVigencia.setFilterStyle("width: 85%;");
            cuenta = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:cuenta");
            cuenta.setFilterStyle("width: 85%;");
            fechaCuenta = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:fechaCuenta");
            fechaCuenta.setFilterStyle("width: 85%;");
            sucursal = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:sucursal");
            sucursal.setFilterStyle("width: 85%;");
            formaPago = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:formaPago");
            formaPago.setFilterStyle("width: 85%;");
            tc = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:tc");
            tc.setFilterStyle("width: 85%;");
            metodoPago = (Column) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos:metodoPago");
            metodoPago.setFilterStyle("width: 85%;");
            altoTabla = "272";
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
            bandera = 1;
        } else if (bandera == 1) {
            cerrarFiltrado();
        }
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        } else {
            if (vigenciaSeleccionada != null) {
                editarVigenciaFormasPagoPorEmpleado = vigenciaSeleccionada;
                if (cualCelda == 0) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaVigencia");
                    PrimefacesContextUI.ejecutar("PF('editarFechaVigencia').show()");
                    cualCelda = -1;
                } else if (cualCelda == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editCuenta");
                    PrimefacesContextUI.ejecutar("PF('editCuenta').show()");
                    cualCelda = -1;
                } else if (cualCelda == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:fechaCuentaEditar");
                    PrimefacesContextUI.ejecutar("PF('fechaCuentaEditar').show()");
                    cualCelda = -1;
                } else if (cualCelda == 3) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editSucursal");
                    PrimefacesContextUI.ejecutar("PF('editSucursal').show()");
                    cualCelda = -1;
                } else if (cualCelda == 4) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editFormaPago");
                    PrimefacesContextUI.ejecutar("PF('editFormaPago').show()");
                    cualCelda = -1;
                } else if (cualCelda == 5) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editTC");
                    PrimefacesContextUI.ejecutar("PF('editTC').show()");
                    cualCelda = -1;
                } else if (cualCelda == 6) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:editMetodoPago");
                    PrimefacesContextUI.ejecutar("PF('editMetodoPago').show()");
                    cualCelda = -1;
                }
            }
        }
    }

    public void validacionesNuevaVigenciaFormasPagos() {
        int pasa = 0;
        int contador = 0;
        int pasa2 = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();

        if (nuevaVigenciaFormasPago.getFechavigencia() == null) {
            mensajeValidacion = " * Fecha Vigencia \n";
            pasa++;
        } else {
            for (int i = 0; i < listVigenciasFormasPagosPorEmpleado.size(); i++) {
                if (vigenciaSeleccionada.getFechavigencia().equals(nuevaVigenciaFormasPago.getFechavigencia())) {
                    contador++;
                }
            }
            if (contador > 0) {
                RequestContext.getCurrentInstance().update("form:fechas");
                PrimefacesContextUI.ejecutar("PF('fechas').show()");
                pasa2++;
            }

        }
        if (nuevaVigenciaFormasPago.getFormapago().getSecuencia() == null) {
            mensajeValidacion = mensajeValidacion + " * Forma de pago \n";
            pasa++;
        }

        if (nuevaVigenciaFormasPago.getMetodopago().getSecuencia() == null) {
            mensajeValidacion = mensajeValidacion + "   * Metodo de pago \n";
            pasa++;

        } else {
            if (nuevaVigenciaFormasPago.getMetodopago().getDescripcion().equals("TRANSFERENCIA")) {
                if (nuevaVigenciaFormasPago.getTipocuenta() == null || nuevaVigenciaFormasPago.getSucursal().getSecuencia() == null || nuevaVigenciaFormasPago.getCuenta() == null) {

                    if (nuevaVigenciaFormasPago.getTipocuenta() == null) {
                        mensajeValidacion = mensajeValidacion + " * Tipo de cuenta \n";
                        pasa++;
                    }
                    if (nuevaVigenciaFormasPago.getSucursal().getSecuencia() == null) {
                        mensajeValidacion = mensajeValidacion + " * Sucursal \n";
                        pasa++;
                    }
                    if (nuevaVigenciaFormasPago.getCuenta() == null) {
                        mensajeValidacion = mensajeValidacion + " * Cuenta \n";
                        pasa++;
                    }
                }
            }
        }
        if (pasa == 0 && pasa2 == 0) {
            agregarNuevaVigenciasFormasPagos();
        } else if (pasa == 0 && pasa2 > 0) {
            RequestContext.getCurrentInstance().update("form:fechas");
            PrimefacesContextUI.ejecutar("PF('fechas').show()");
        } else if (pasa > 0 && pasa2 == 0) {
            RequestContext.getCurrentInstance().update("form:validacioNuevaVigencia");
            PrimefacesContextUI.ejecutar("PF('validacioNuevaVigencia').show()");
        } else if (pasa > 0 && pasa2 > 0) {
            RequestContext.getCurrentInstance().update("form:validacioNuevaVigencia");
            PrimefacesContextUI.ejecutar("PF('validacioNuevaVigencia').show()");
            RequestContext.getCurrentInstance().update("form:fechas");
            PrimefacesContextUI.ejecutar("PF('fechas').show()");
        }
    }

    public void agregarNuevaVigenciasFormasPagos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (bandera == 1) {
            cerrarFiltrado();
        }
        //AGREGAR REGISTRO A LA LISTA VIGENCIAS CARGOS EMPLEADO.
        k++;
        l = BigInteger.valueOf(k);
        nuevaVigenciaFormasPago.setSecuencia(l);
        nuevaVigenciaFormasPago.setEmpleado(empleadoSeleccionado);
        if (nuevaVigenciaFormasPago.getSucursal().getSecuencia() == null) {
            nuevaVigenciaFormasPago.setSucursal(null);
        }
        crearVFP.add(nuevaVigenciaFormasPago);
        listVigenciasFormasPagosPorEmpleado.add(nuevaVigenciaFormasPago);
        vigenciaSeleccionada = listVigenciasFormasPagosPorEmpleado.get(listVigenciasFormasPagosPorEmpleado.indexOf(nuevaVigenciaFormasPago));
        modificarInfoRegistro(listVigenciasFormasPagosPorEmpleado.size());
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        nuevaVigenciaFormasPago = new VigenciasFormasPagos();
        nuevaVigenciaFormasPago.setSucursal(new Sucursales());
        nuevaVigenciaFormasPago.setFormapago(new Periodicidades());
        nuevaVigenciaFormasPago.setMetodopago(new MetodosPagos());
        RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
        if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        PrimefacesContextUI.ejecutar("PF('NuevoRegistroVigenciasFormasPagos').hide()");
    }

    public void limpiarNuevaVigenciaFormpasPagos() {

        nuevaVigenciaFormasPago = new VigenciasFormasPagos();
        nuevaVigenciaFormasPago.setFormapago(new Periodicidades());
        nuevaVigenciaFormasPago.getFormapago().setNombre(" ");
        nuevaVigenciaFormasPago.setSucursal(new Sucursales());
        nuevaVigenciaFormasPago.getSucursal().setNombre(" ");
        nuevaVigenciaFormasPago.setMetodopago(new MetodosPagos());
        nuevaVigenciaFormasPago.getMetodopago().setDescripcion(" ");
        RequestContext.getCurrentInstance().update("form:nuevoTipoEntidad");
    }
    private String nuevoNombreSucursal, nuevoNombrePeriodicidad, nuevoNombreMetodoPago;

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {

        if (Campo.equalsIgnoreCase("SUCURSAL")) {
            if (tipoNuevo == 1) {
                nuevoNombreSucursal = nuevaVigenciaFormasPago.getSucursal().getNombre();
            } else if (tipoNuevo == 2) {
                nuevoNombreSucursal = duplicarVigenciaFormasPago.getSucursal().getNombre();
            }
        } else if (Campo.equalsIgnoreCase("FORMAPAGO")) {
            if (tipoNuevo == 1) {
                nuevoNombrePeriodicidad = nuevaVigenciaFormasPago.getFormapago().getNombre();
            } else if (tipoNuevo == 2) {
                nuevoNombrePeriodicidad = duplicarVigenciaFormasPago.getFormapago().getNombre();
            }
        } else if (Campo.equalsIgnoreCase("METODOPAGO")) {
            nuevoNombreMetodoPago = nuevaVigenciaFormasPago.getMetodopago().getDescripcion();
        } else if (tipoNuevo == 2) {
            nuevoNombreMetodoPago = duplicarVigenciaFormasPago.getMetodopago().getDescripcion();
        }
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("SUCURSAL")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormasPago.getSucursal().setNombre(nuevoNombreSucursal);
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormasPago.getSucursal().setNombre(nuevoNombreSucursal);
                }
                for (int i = 0; i < listaSucursales.size(); i++) {
                    if (listaSucursales.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevaVigenciaFormasPago.setSucursal(listaSucursales.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
                    } else if (tipoNuevo == 2) {
                        duplicarVigenciaFormasPago.setSucursal(listaSucursales.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");
                    }
                    listaSucursales.clear();
                    getListaSucursales();
                } else {
                    RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
                    PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");
                    }
                }
            } else {
                listaSucursales.clear();
                getListaSucursales();
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormasPago.setSucursal(new Sucursales());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormasPago.setSucursal(new Sucursales());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");
                }
            }
        }
        if (confirmarCambio.equalsIgnoreCase("FORMAPAGO")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaFormasPago.getFormapago().setNombre(nuevoNombrePeriodicidad);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaFormasPago.getFormapago().setNombre(nuevoNombrePeriodicidad);
            }
            for (int i = 0; i < listaPeriodicidades.size(); i++) {
                if (listaPeriodicidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormasPago.setFormapago(listaPeriodicidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormaPago");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormasPago.setFormapago(listaPeriodicidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormaPago");
                }
                listaSucursales.clear();
                listaSucursales = null;
                getListaSucursales();
            } else {
                RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
                PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormaPago");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormaPago");
                }
            }
        }
        if (confirmarCambio.equalsIgnoreCase("METODOPAGO")) {
            if (tipoNuevo == 1) {
                nuevaVigenciaFormasPago.getMetodopago().setDescripcion(nuevoNombreMetodoPago);
            } else if (tipoNuevo == 2) {
                duplicarVigenciaFormasPago.getMetodopago().setDescripcion(nuevoNombreMetodoPago);
            }
            for (int i = 0; i < listaMetodosPagos.size(); i++) {
                if (listaMetodosPagos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevaVigenciaFormasPago.setMetodopago(listaMetodosPagos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaMetodoPago");
                } else if (tipoNuevo == 2) {
                    duplicarVigenciaFormasPago.setMetodopago(listaMetodosPagos.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarMetodoPago");
                }
                listaSucursales.clear();
                listaSucursales = null;
                getListaSucursales();
            } else {
                RequestContext.getCurrentInstance().update("form:metodosPagosialogo");
                PrimefacesContextUI.ejecutar("PF('metodosPagosialogo').show()");
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaFormaPago");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarFormaPago");
                }
            }
        }
    }

    public void asignarVariableSucursalNueva(int tipoNuevo) {
        sucursalSeleccionada = null;
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
        PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
    }

    public void cargarSucursalesNuevoRegistro(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
            PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
        } else if (tipoNuevo == 1) {
            tipoActualizacion = 2;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:sucursalesDialogo");
            PrimefacesContextUI.ejecutar("PF('sucursalesDialogo').show()");
        }
    }

    public void asignarVariableMetodosPagosNueva(int tipoNuevo) {
        metodoPagoSeleccionado = null;
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:metodosPagosialogo");
        PrimefacesContextUI.ejecutar("PF('metodosPagosialogo').show()");
    }

    public void cargarMetodosPagosNuevoRegistro(int tipoNuevo) {
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:metodosPagosialogo");
            PrimefacesContextUI.ejecutar("PF('metodosPagosialogo').show()");
        } else if (tipoNuevo == 1) {
            tipoActualizacion = 2;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:metodosPagosialogo");
            PrimefacesContextUI.ejecutar("PF('metodosPagosialogo').show()");
        }
    }

    public void asignarVariablePeriodicidadNueva(int tipoNuevo) {
        PeriodicidadSeleccionada = null;
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
        }
        if (tipoNuevo == 1) {
            tipoActualizacion = 2;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
        PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
    }

    public void cargarPeriodicidadNuevoRegistro(int tipoNuevo) {
        activarLOV = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
        if (tipoNuevo == 0) {
            tipoActualizacion = 1;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
            PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
        } else if (tipoNuevo == 1) {
            tipoActualizacion = 2;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:periodicidadesDialogo");
            PrimefacesContextUI.ejecutar("PF('periodicidadesDialogo').show()");
        }

    }

    //BORRAR VC
    public void borrarVigenciasFormasPagos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (vigenciaSeleccionada == null) {
            PrimefacesContextUI.ejecutar("PF('seleccionarRegistro').show()");
        } else {
            if (vigenciaSeleccionada != null) {
                if (!modificarVFP.isEmpty() && modificarVFP.contains(vigenciaSeleccionada)) {
                    int modIndex = modificarVFP.indexOf(vigenciaSeleccionada);
                    modificarVFP.remove(modIndex);
                    borrarVFP.add(vigenciaSeleccionada);
                } else if (!crearVFP.isEmpty() && crearVFP.contains(vigenciaSeleccionada)) {
                    int crearIndex = crearVFP.indexOf(vigenciaSeleccionada);
                    crearVFP.remove(crearIndex);
                } else {
                    borrarVFP.add(vigenciaSeleccionada);
                }
                listVigenciasFormasPagosPorEmpleado.remove(vigenciaSeleccionada);
                if (tipoLista == 1) {
                    filtrarVigenciasFormasPagosPorEmpleado.remove(vigenciaSeleccionada);
                }

                modificarInfoRegistro(listVigenciasFormasPagosPorEmpleado.size());
                vigenciaSeleccionada = null;
                activarLOV = true;
                RequestContext.getCurrentInstance().update("form:listaValores");
                RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
                RequestContext.getCurrentInstance().update("form:informacionRegistro");
                if (guardado) {
                    guardado = false;
                    RequestContext.getCurrentInstance().update("form:ACEPTAR");
                }
            }
        }
    }

    //DUPLICAR VC
    public void duplicarVigenciaFormasPagos() {
        if (vigenciaSeleccionada != null) {
            duplicarVigenciaFormasPago = new VigenciasFormasPagos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarVigenciaFormasPago.setSecuencia(l);
            duplicarVigenciaFormasPago.setFechavigencia(vigenciaSeleccionada.getFechavigencia());
            duplicarVigenciaFormasPago.setCuenta(vigenciaSeleccionada.getCuenta());
            duplicarVigenciaFormasPago.setEmpleado(vigenciaSeleccionada.getEmpleado());
            duplicarVigenciaFormasPago.setFechacuenta(vigenciaSeleccionada.getFechacuenta());
            duplicarVigenciaFormasPago.setFormapago(vigenciaSeleccionada.getFormapago());
            duplicarVigenciaFormasPago.setMetodopago(vigenciaSeleccionada.getMetodopago());
            duplicarVigenciaFormasPago.setSucursal(vigenciaSeleccionada.getSucursal());
            duplicarVigenciaFormasPago.setTipocuenta(vigenciaSeleccionada.getTipocuenta());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarVigenciaFormasPagos");
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroVigenciasFormasPagos').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
//        int pasa = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        boolean banderaConfirmar = false;
        if (duplicarVigenciaFormasPago.getFormapago().getSecuencia() == null) {
            mensajeValidacion = mensajeValidacion + "   *Forma de pago \n";
            banderaConfirmar = false;
        }
        if (duplicarVigenciaFormasPago.getMetodopago().getSecuencia() == null) {
            mensajeValidacion = mensajeValidacion + "   *Metodo de pago \n";
            banderaConfirmar = false;
        } else {
            if (duplicarVigenciaFormasPago.getMetodopago().getDescripcion().equals("TRANSFERENCIA")) {
                if (duplicarVigenciaFormasPago.getTipocuenta() == null || duplicarVigenciaFormasPago.getSucursal().getSecuencia() == null || nuevaVigenciaFormasPago.getCuenta() == null) {

                    if (duplicarVigenciaFormasPago.getTipocuenta() == null) {
                        mensajeValidacion = mensajeValidacion + " *Tipo de cuenta \n";
                    } else {
                        banderaConfirmar = true;
                    }
                    if (duplicarVigenciaFormasPago.getSucursal().getSecuencia() == null) {
                        mensajeValidacion = mensajeValidacion + " *Sucursal \n";
                        banderaConfirmar = false;
                    }
                    if (duplicarVigenciaFormasPago.getCuenta() == null) {
                        mensajeValidacion = mensajeValidacion + " *Cuenta \n";
                        banderaConfirmar = false;
                    }
                }
            } else {
                duplicarVigenciaFormasPago.getSucursal().setNombre(" ");
                banderaConfirmar = true;

            }
        }
            for (int j = 0; j < listVigenciasFormasPagosPorEmpleado.size(); j++) {
                if (duplicarVigenciaFormasPago.getFechavigencia().equals(listVigenciasFormasPagosPorEmpleado.get(j).getFechavigencia())) {
                    contador++;
                }
            }
            
        if (contador > 0) {
            mensajeValidacion = "Fechas Repetidas";
            RequestContext.getCurrentInstance().update("form:validacionFechas");
            PrimefacesContextUI.ejecutar("PF('validacionFechas').show()");
        } else {
            listVigenciasFormasPagosPorEmpleado.add(duplicarVigenciaFormasPago);
            crearVFP.add(duplicarVigenciaFormasPago);
            vigenciaSeleccionada = listVigenciasFormasPagosPorEmpleado.get(listVigenciasFormasPagosPorEmpleado.indexOf(duplicarVigenciaFormasPago));
            modificarInfoRegistro(listVigenciasFormasPagosPorEmpleado.size());
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            activarLOV = true;
            RequestContext.getCurrentInstance().update("form:listaValores");
            RequestContext.getCurrentInstance().update("form:datosVigenciasFormasPagos");
            if (guardado) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            if (bandera == 1) {
                cerrarFiltrado();
            }
            duplicarVigenciaFormasPago = new VigenciasFormasPagos();
            PrimefacesContextUI.ejecutar("PF('duplicarRegistroVigenciasFormasPagos').hide()");
        }
 }
    
    public void limpiarduplicarVigenciasFormasPagos() {
        duplicarVigenciaFormasPago = new VigenciasFormasPagos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVFPEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "VigenciasFormasPagosPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVFPEmpleadoExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "VigenciasFormasPagosXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
        modificarInfoRegistro(filtrarVigenciasFormasPagosPorEmpleado.size());
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void eventoFiltrarSucursales() {
        modificarInfoRegistroSucursales(filtradoSucursales.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroSucursales");
    }

    public void eventoFiltrarPeriodicidad() {
        modificarInfoRegistroPeriodicidad(filtradoPeriodicidades.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroPeriodicidades");
    }

    public void eventoFiltrarMetodoPago() {
        modificarInfoRegistroMetodoPago(filtradoMetodosPagos.size());
        RequestContext.getCurrentInstance().update("form:infoRegistroMetodosPagos");
    }

    private void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
        System.out.println("infoRegistro: " + infoRegistro);
    }

    private void modificarInfoRegistroSucursales(int valor) {
        infoRegistroSucursales = String.valueOf(valor);
    }

    private void modificarInfoRegistroPeriodicidad(int valor) {
        infoRegistroPeriodicidades = String.valueOf(valor);
    }

    private void modificarInfoRegistroMetodoPago(int valor) {
        infoRegistroMetodosPagos = String.valueOf(valor);
    }

    public void recordarSeleccion() {
        if (vigenciaSeleccionada != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosVigenciasFormasPagos");
            tablaC.setSelection(vigenciaSeleccionada);
        } else {
            vigenciaSeleccionada = null;
        }
        System.out.println("vigenciaSeleccionada: " + vigenciaSeleccionada);
    }

    public void contarRegistrosFormaPago() {
        if (listVigenciasFormasPagosPorEmpleado != null) {
            modificarInfoRegistro(listVigenciasFormasPagosPorEmpleado.size());
        } else {
            modificarInfoRegistro(0);
        }
    }

    public void anularLOV() {
        activarLOV = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

//GETTERS AND SETTERS
    /**
     *
     * @return
     */
    public Empleados getEmpleadoSeleccionado() {
        if (empleadoSeleccionado == null) {
            empleadoSeleccionado = administrarEmplVigenciasFormasPagos.consultarEmpleado(secuenciaEmpleado);
        }
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public List<VigenciasFormasPagos> getListVigenciasFormasPagosPorEmpleado() {
        try {
            if (listVigenciasFormasPagosPorEmpleado == null) {
                listVigenciasFormasPagosPorEmpleado = administrarEmplVigenciasFormasPagos.consultarVigenciasFormasPagosPorEmpleado(secuenciaEmpleado);
            }
//
//            RequestContext context = RequestContext.getCurrentInstance();
//            if (listVigenciasFormasPagosPorEmpleado == null || listVigenciasFormasPagosPorEmpleado.isEmpty()) {
//                infoRegistro = "0";
//            } else {
//                infoRegistro = String.valueOf(listVigenciasFormasPagosPorEmpleado.size());
//            }
//            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            return listVigenciasFormasPagosPorEmpleado;
        } catch (Exception e) {
            return listVigenciasFormasPagosPorEmpleado = null;
        }
    }

    public void setListVigenciasFormasPagosPorEmpleado(List<VigenciasFormasPagos> listVigenciasFormasPagosPorEmpleado) {
        this.listVigenciasFormasPagosPorEmpleado = listVigenciasFormasPagosPorEmpleado;
    }

    public List<VigenciasFormasPagos> getFiltrarVigenciasFormasPagosPorEmpleado() {
        return filtrarVigenciasFormasPagosPorEmpleado;
    }

    public void setFiltrarVigenciasFormasPagosPorEmpleado(List<VigenciasFormasPagos> filtrarVigenciasFormasPagosPorEmpleado) {
        this.filtrarVigenciasFormasPagosPorEmpleado = filtrarVigenciasFormasPagosPorEmpleado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }
    private String infoRegistroSucursales;

    public List<Sucursales> getListaSucursales() {

        if (listaSucursales == null) {
            listaSucursales = administrarEmplVigenciasFormasPagos.consultarLOVSucursales();
        }
        return listaSucursales;
    }

    public void setListaSucursales(List<Sucursales> listaSucursales) {
        this.listaSucursales = listaSucursales;
    }

    public List<Sucursales> getFiltradoSucursales() {
        return filtradoSucursales;
    }

    public void setFiltradoSucursales(List<Sucursales> filtradoSucursales) {
        this.filtradoSucursales = filtradoSucursales;
    }

    public Sucursales getSucursalSeleccionada() {
        return sucursalSeleccionada;
    }

    public void setSucursalSeleccionada(Sucursales sucursalSeleccionada) {
        this.sucursalSeleccionada = sucursalSeleccionada;
    }
    private String infoRegistroPeriodicidades;

    public List<Periodicidades> getListaPeriodicidades() {

        if (listaPeriodicidades == null) {
            listaPeriodicidades = administrarEmplVigenciasFormasPagos.consultarLOVPerdiocidades();
        }
        return listaPeriodicidades;
    }

    public void setListaPeriodicidades(List<Periodicidades> listaPeriodicidades) {
        this.listaPeriodicidades = listaPeriodicidades;
    }

    public List<Periodicidades> getFiltradoPeriodicidades() {
        return filtradoPeriodicidades;
    }

    public void setFiltradoPeriodicidades(List<Periodicidades> filtradoPeriodicidades) {
        this.filtradoPeriodicidades = filtradoPeriodicidades;
    }

    public Periodicidades getPeriodicidadSeleccionada() {
        return PeriodicidadSeleccionada;
    }

    public void setPeriodicidadSeleccionada(Periodicidades PeriodicidadSeleccionada) {
        this.PeriodicidadSeleccionada = PeriodicidadSeleccionada;
    }

    public List<MetodosPagos> getListaMetodosPagos() {
        if (listaMetodosPagos == null) {
            listaMetodosPagos = administrarEmplVigenciasFormasPagos.consultarLOVMetodosPagos();
        }
        return listaMetodosPagos;
    }

    public void setListaMetodosPagos(List<MetodosPagos> listaMetodosPagos) {
        this.listaMetodosPagos = listaMetodosPagos;
    }

    public List<MetodosPagos> getFiltradoMetodosPagos() {
        return filtradoMetodosPagos;
    }

    public void setFiltradoMetodosPagos(List<MetodosPagos> filtradoMetodosPagos) {
        this.filtradoMetodosPagos = filtradoMetodosPagos;
    }

    public MetodosPagos getMetodoPagoSeleccionado() {
        return metodoPagoSeleccionado;
    }

    public void setMetodoPagoSeleccionado(MetodosPagos metodoPagoSeleccionado) {
        this.metodoPagoSeleccionado = metodoPagoSeleccionado;
    }

    public VigenciasFormasPagos getEditarVigenciaFormasPagoPorEmpleado() {
        return editarVigenciaFormasPagoPorEmpleado;
    }

    public void setEditarVigenciaFormasPagoPorEmpleado(VigenciasFormasPagos editarVigenciaFormasPagoPorEmpleado) {
        this.editarVigenciaFormasPagoPorEmpleado = editarVigenciaFormasPagoPorEmpleado;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public VigenciasFormasPagos getNuevaVigenciaFormasPago() {
        return nuevaVigenciaFormasPago;
    }

    public void setNuevaVigenciaFormasPago(VigenciasFormasPagos nuevaVigenciaFormasPago) {
        this.nuevaVigenciaFormasPago = nuevaVigenciaFormasPago;
    }

    public VigenciasFormasPagos getDuplicarVigenciaFormasPago() {
        return duplicarVigenciaFormasPago;
    }

    public void setDuplicarVigenciaFormasPago(VigenciasFormasPagos duplicarVigenciaFormasPago) {
        this.duplicarVigenciaFormasPago = duplicarVigenciaFormasPago;
    }

    public VigenciasFormasPagos getVigenciaSeleccionada() {
        return vigenciaSeleccionada;
    }

    public void setVigenciaSeleccionada(VigenciasFormasPagos vigenciaSeleccionada) {
        this.vigenciaSeleccionada = vigenciaSeleccionada;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public String getInfoRegistroSucursales() {
        return infoRegistroSucursales;
    }

    public String getInfoRegistroPeriodicidades() {
        return infoRegistroPeriodicidades;
    }

    public String getInfoRegistroMetodosPagos() {
        return infoRegistroMetodosPagos;
    }

    public boolean isActivarLOV() {
        return activarLOV;
    }

    public void setActivarLOV(boolean activarLOV) {
        this.activarLOV = activarLOV;
    }
}
