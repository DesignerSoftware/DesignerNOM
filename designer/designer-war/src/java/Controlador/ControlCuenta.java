/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.Cuentas;
import Entidades.Empresas;
import Entidades.Rubrospresupuestales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCuentasInterface;
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
public class ControlCuenta implements Serializable {

    @EJB
    AdministrarCuentasInterface administrarCuentas;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    //
    private List<Cuentas> listCuentas;
    private List<Cuentas> filtrarListCuentas;
    private Cuentas cuentaTablaSeleccionada;
    //
    private List<Empresas> listaEmpresas;
    private List<Empresas> filtrarListEmpresas;
    private Empresas empresaSeleccionada;
    private Empresas empresaActual;
    //
    private List<Rubrospresupuestales> listRubros;
    private List<Rubrospresupuestales> filtrarListRubros;
    private Rubrospresupuestales rubroSeleccionado;
    //
    private List<Cuentas> listCuentasTesoreria;
    private List<Cuentas> filtrarListCuentasTesoreria;
    private Cuentas cuentaSeleccionada;
    //
    private int tipoActualizacion;
    private int bandera;
    private Empresas backUpEmpresaActual;
    //Columnas Tabla VL
    private Column cuentaCodigo, cuentasDescripcion, cuentaContracuenta, cuentaRubro, cuentaManejaNit, cuentaManejaNitEmpleado, cuentaProrrateo, cuentaCodigoA, cuentaConsolidaNit, cuentaIncluyeShort, cuentaAsociadaSAP, cuentaSubCuenta;
    //Otros
    private boolean aceptar;
    private int index;
    private List<Cuentas> listCuentasModificar;
    private boolean guardado;
    public Cuentas nuevoCuentas;
    private List<Cuentas> listCuentasCrear;
    private BigInteger l;
    private int k;
    private List<Cuentas> listCuentasBorrar;
    private Cuentas editarCuentas;
    private int cualCelda, tipoLista;
    private Cuentas duplicarCuentas;
    private boolean permitirIndex;
    //Variables Autompletar
    //private String motivoCambioSueldo, tiposEntidades, tiposSueldos, terceros;
    //Indices VigenciaProrrateo / VigenciaProrrateoProyecto
    private String nombreXML;
    private String nombreTabla;
    private boolean cambiosCuentas;
    private BigInteger secRegistroCuentas;
    private BigInteger backUpSecRegistroCuentas;
    private String msnConfirmarRastro, msnConfirmarRastroHistorico;
    private BigInteger backUp;
    private String nombreTablaRastro;
    private String cuenta, rubroP;
    private Cuentas cuentaActual;
    //
    private String altoTabla;
    //
    private String infoRegistro;
    //
    private boolean activoDetalle;
    //
    private String infoRegistroBuscarCuenta, infoRegistroRubro, infoRegistroContraCuenta, infoRegistroEmpresa;
    //
    private String auxCodigoCuenta, auxDescripcionCuenta;
    private String paginaAnterior;

    public ControlCuenta() {
        empresaSeleccionada = new Empresas();
        activoDetalle = true;
        altoTabla = "300";
        cuentaActual = null;
        listRubros = null;
        listCuentasTesoreria = null;
        backUpEmpresaActual = new Empresas();
        listaEmpresas = null;
        nombreTablaRastro = "";
        backUp = null;
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        secRegistroCuentas = null;
        backUpSecRegistroCuentas = null;
        //Otros
        aceptar = true;
        //borrar aficiones
        listCuentasBorrar = new ArrayList<Cuentas>();
        //crear aficiones
        listCuentasCrear = new ArrayList<Cuentas>();
        k = 0;
        //modificar aficiones
        listCuentasModificar = new ArrayList<Cuentas>();
        //editar
        editarCuentas = new Cuentas();

        cualCelda = -1;
        tipoLista = 0;
        //guardar
        guardado = true;
        //Crear VC
        nuevoCuentas = new Cuentas();
        nuevoCuentas.setRubropresupuestal(new Rubrospresupuestales());
        nuevoCuentas.setContracuentatesoreria(new Cuentas());
        index = -1;
        bandera = 0;
        permitirIndex = true;
        nombreTabla = ":formExportarCuentas:datosCuentasExportar";
        nombreXML = "CuentasXML";
        duplicarCuentas = new Cuentas();
        cambiosCuentas = false;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarCuentas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

     public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        index = -1;
        activoDetalle = true;
        listCuentas = null;
        getListEmpresas();
        if (listaEmpresas != null) {
            if (listaEmpresas.size() > 0) {
                empresaActual = listaEmpresas.get(0);
                backUpEmpresaActual = empresaActual;
            }
        }
        empresaSeleccionada = empresaActual;
        getListCuentas();
        RequestContext context = RequestContext.getCurrentInstance();
    }
    
    public String redirigir() {
        return paginaAnterior;
    }
    
    public void obtenerEmpresa() {
        index = -1;
        activoDetalle = true;
        listCuentas = null;
        getListEmpresas();
        if (listaEmpresas != null) {
            if (listaEmpresas.size() > 0) {
                empresaActual = listaEmpresas.get(0);
                backUpEmpresaActual = empresaActual;
            }
        }
        empresaSeleccionada = empresaActual;
        getListCuentas();
        contarRegistro();
    }

    public void modificarCuenta(int indice) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (validarDatosNull(0) == true) {
            if (tipoLista == 0) {
                if (!listCuentasCrear.contains(listCuentas.get(indice))) {
                    if (listCuentasModificar.isEmpty()) {
                        listCuentasModificar.add(listCuentas.get(indice));
                    } else if (!listCuentasModificar.contains(listCuentas.get(indice))) {
                        listCuentasModificar.add(listCuentas.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;

                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                index = -1;
                secRegistroCuentas = null;
            } else {
                if (!listCuentasCrear.contains(filtrarListCuentas.get(indice))) {
                    if (listCuentasModificar.isEmpty()) {
                        listCuentasModificar.add(filtrarListCuentas.get(indice));
                    } else if (!listCuentasModificar.contains(filtrarListCuentas.get(indice))) {
                        listCuentasModificar.add(filtrarListCuentas.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                index = -1;
                secRegistroCuentas = null;
            }
            cambiosCuentas = true;
            activoDetalle = true;
            RequestContext.getCurrentInstance().update("form:DETALLES");
        } else {
            if (tipoLista == 0) {
                listCuentas.get(indice).setCodigo(auxCodigoCuenta);
                listCuentas.get(indice).setDescripcion(auxDescripcionCuenta);
            } else {
                filtrarListCuentas.get(indice).setCodigo(auxCodigoCuenta);
                filtrarListCuentas.get(indice).setDescripcion(auxDescripcionCuenta);
            }
            RequestContext.getCurrentInstance().execute("PF('errorDatosNullCuenta').show()");
        }
        RequestContext.getCurrentInstance().update("form:datosCuenta");
    }

    public void modificarCuentaAutocompletar(int indice, String confirmarCambio, String valorConfirmar) {
        index = indice;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CONTRACUENTA")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    listCuentas.get(indice).getContracuentatesoreria().setDescripcion(cuenta);
                } else {
                    filtrarListCuentas.get(indice).getContracuentatesoreria().setDescripcion(cuenta);
                }
                for (int i = 0; i < listCuentasTesoreria.size(); i++) {
                    if (listCuentasTesoreria.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listCuentas.get(indice).setContracuentatesoreria(listCuentasTesoreria.get(indiceUnicoElemento));
                    } else {
                        filtrarListCuentas.get(indice).setContracuentatesoreria(listCuentasTesoreria.get(indiceUnicoElemento));
                    }
                    listCuentasTesoreria.clear();
                    getListCuentasTesoreria();
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:ContracuentaDialogo");
                    RequestContext.getCurrentInstance().execute("PF('ContracuentaDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                if (tipoLista == 0) {
                    listCuentas.get(indice).setContracuentatesoreria(new Cuentas());
                } else {
                    filtrarListCuentas.get(indice).setContracuentatesoreria(new Cuentas());
                }
                listCuentasTesoreria.clear();
                getListCuentasTesoreria();
            }
        }
        if (confirmarCambio.equalsIgnoreCase("RUBRO")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoLista == 0) {
                    listCuentas.get(indice).getRubropresupuestal().setDescripcion(rubroP);
                } else {
                    filtrarListCuentas.get(indice).getRubropresupuestal().setDescripcion(rubroP);
                }
                for (int i = 0; i < listRubros.size(); i++) {
                    if (listRubros.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoLista == 0) {
                        listCuentas.get(indice).setRubropresupuestal(listRubros.get(indiceUnicoElemento));
                    } else {
                        filtrarListCuentas.get(indice).setRubropresupuestal(listRubros.get(indiceUnicoElemento));
                    }
                    listRubros.clear();
                    getListRubros();
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("form:RubrosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('RubrosDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                if (tipoLista == 0) {
                    listCuentas.get(indice).setRubropresupuestal(new Rubrospresupuestales());
                } else {
                    filtrarListCuentas.get(indice).setRubropresupuestal(new Rubrospresupuestales());
                }
                listRubros.clear();
                getListRubros();
            }
        }
        if (coincidencias == 1) {
            if (tipoLista == 0) {
                if (!listCuentasCrear.contains(listCuentas.get(indice))) {
                    if (listCuentasModificar.isEmpty()) {
                        listCuentasModificar.add(listCuentas.get(indice));
                    } else if (!listCuentasModificar.contains(listCuentas.get(indice))) {
                        listCuentasModificar.add(listCuentas.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                index = -1;
                secRegistroCuentas = null;
                activoDetalle = true;
                RequestContext.getCurrentInstance().update("form:DETALLES");
            } else {
                if (!listCuentasCrear.contains(filtrarListCuentas.get(indice))) {
                    if (listCuentasModificar.isEmpty()) {
                        listCuentasModificar.add(filtrarListCuentas.get(indice));
                    } else if (!listCuentasModificar.contains(filtrarListCuentas.get(indice))) {
                        listCuentasModificar.add(filtrarListCuentas.get(indice));
                    }
                    if (guardado == true) {
                        guardado = false;
                        RequestContext.getCurrentInstance().update("form:ACEPTAR");
                    }
                }
                index = -1;
                secRegistroCuentas = null;
                activoDetalle = true;
                RequestContext.getCurrentInstance().update("form:DETALLES");
            }
            cambiosCuentas = true;
        }
        RequestContext.getCurrentInstance().update("form:datosCuenta");
    }

    public void cambiarIndice(int indice, int celda) {
        if (permitirIndex == true) {
            cualCelda = celda;
            index = indice;
            activoDetalle = false;
            RequestContext.getCurrentInstance().update("form:DETALLES");
            if (tipoLista == 0) {
                secRegistroCuentas = listCuentas.get(index).getSecuencia();
                cuentaActual = listCuentas.get(index);
                auxCodigoCuenta = listCuentas.get(index).getCodigo();
                auxDescripcionCuenta = listCuentas.get(index).getDescripcion();
                if (cualCelda == 5) {
                    cuenta = listCuentas.get(index).getContracuentatesoreria().getCodigo();
                }
                if (cualCelda == 3) {
                    rubroP = listCuentas.get(index).getRubropresupuestal().getDescripcion();
                }
            } else {
                secRegistroCuentas = filtrarListCuentas.get(index).getSecuencia();
                cuentaActual = filtrarListCuentas.get(index);
                auxCodigoCuenta = filtrarListCuentas.get(index).getCodigo();
                auxDescripcionCuenta = filtrarListCuentas.get(index).getDescripcion();
                if (cualCelda == 5) {
                    cuenta = filtrarListCuentas.get(index).getContracuentatesoreria().getCodigo();
                }
                if (cualCelda == 3) {
                    rubroP = filtrarListCuentas.get(index).getRubropresupuestal().getDescripcion();
                }
            }
        }
    }

    public void guardadoGeneral() {
        if (cambiosCuentas == true) {
            guardarCambiosCuenta();
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void guardarCambiosCuenta() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listCuentasBorrar.isEmpty()) {
                    administrarCuentas.borrarCuentas(listCuentasBorrar);
                    listCuentasBorrar.clear();
                }
                if (!listCuentasCrear.isEmpty()) {
                    administrarCuentas.crearCuentas(listCuentasCrear);
                    listCuentasCrear.clear();
                }
                if (!listCuentasModificar.isEmpty()) {
                    administrarCuentas.modificarCuentas(listCuentasModificar);
                    listCuentasModificar.clear();
                }
                listCuentas = null;
                getListCuentas();
                contarRegistro();
                RequestContext.getCurrentInstance().update("form:informacionRegistro");

                RequestContext.getCurrentInstance().update("form:datosCuenta");
                k = 0;
                index = -1;
                secRegistroCuentas = null;
                cambiosCuentas = false;
                activoDetalle = true;
                guardado = true;
                cambiosCuentas = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                RequestContext.getCurrentInstance().update("form:DETALLES");
                FacesMessage msg = new FacesMessage("Información", "Se gurdarón los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambiosCuenta Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado+, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            altoTabla = "335";
            FacesContext c = FacesContext.getCurrentInstance();
            cuentaCodigo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigo");
            cuentaCodigo.setFilterStyle("display: none; visibility: hidden;");
            cuentasDescripcion = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentasDescripcion");
            cuentasDescripcion.setFilterStyle("display: none; visibility: hidden;");
            cuentaContracuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaContracuenta");
            cuentaContracuenta.setFilterStyle("display: none; visibility: hidden;");
            cuentaRubro = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaRubro");
            cuentaRubro.setFilterStyle("display: none; visibility: hidden;");
            cuentaManejaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNit");
            cuentaManejaNit.setFilterStyle("display: none; visibility: hidden;");
            cuentaManejaNitEmpleado = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNitEmpleado");
            cuentaManejaNitEmpleado.setFilterStyle("display: none; visibility: hidden;");
            cuentaProrrateo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaProrrateo");
            cuentaProrrateo.setFilterStyle("display: none; visibility: hidden;");
            cuentaCodigoA = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigoA");
            cuentaCodigoA.setFilterStyle("display: none; visibility: hidden;");
            cuentaConsolidaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaConsolidaNit");
            cuentaConsolidaNit.setFilterStyle("display: none; visibility: hidden;");
            cuentaIncluyeShort = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaIncluyeShort");
            cuentaIncluyeShort.setFilterStyle("display: none; visibility: hidden;");
            cuentaAsociadaSAP = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaAsociadaSAP");
            cuentaAsociadaSAP.setFilterStyle("display: none; visibility: hidden;");
            cuentaSubCuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaSubCuenta");
            cuentaSubCuenta.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            bandera = 0;
            filtrarListCuentas = null;
            tipoLista = 0;
        }
        listCuentasBorrar.clear();
        listCuentasCrear.clear();
        listCuentasModificar.clear();
        index = -1;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
        secRegistroCuentas = null;
        k = 0;
        listCuentas = null;
        listCuentasTesoreria = null;
        guardado = true;
        cambiosCuentas = false;
        cuentaActual = null;
        getListCuentas();
     contarRegistro();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
        getListCuentasTesoreria();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosCuenta");
    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarCuentas = listCuentas.get(index);
            }
            if (tipoLista == 1) {
                editarCuentas = filtrarListCuentas.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoCuentaD");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoCuentaD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionCuentaD");
                RequestContext.getCurrentInstance().execute("PF('editarDescripcionCuentaD').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCuentaTCuentaD");
                RequestContext.getCurrentInstance().execute("PF('editarCuentaTCuentaD').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarRubroCuentaD");
                RequestContext.getCurrentInstance().execute("PF('editarRubroCuentaD').show()");
                cualCelda = -1;
            } else if (cualCelda == 11) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoACuentaD");
                RequestContext.getCurrentInstance().execute("PF('editarCodigoACuentaD').show()");
                cualCelda = -1;
            }
        }

        index = -1;
        secRegistroCuentas = null;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    public void validarIngresoNuevoRegistro() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:NuevoRegistroCuenta");
        RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCuenta').show()");

    }

    public void validarDuplicadoRegistro() {
        if (index >= 0) {
            duplicarCuenta();
        }
    }

    public void validarBorradoRegistro() {
        if (index >= 0) {
            borrarCuenta();
        }
    }

    public boolean validarDatosNull(int i) {
        boolean retorno = true;
        if (i == 0) {
            Cuentas aux = null;
            if (tipoLista == 0) {
                aux = listCuentas.get(index);
            } else {
                aux = filtrarListCuentas.get(index);
            }
            if (aux.getDescripcion() == null) {
                retorno = false;
            } else {
                if (aux.getDescripcion().isEmpty()) {
                    retorno = false;
                }
            }
            if (aux.getCodigo() == null) {
                retorno = false;
            } else {
                if (aux.getCodigo().isEmpty()) {
                    retorno = false;
                }
            }
        }
        if (i == 1) {
            if (nuevoCuentas.getDescripcion() == null) {
                retorno = false;
            } else {
                if (nuevoCuentas.getDescripcion().isEmpty()) {
                    retorno = false;
                }
            }
            if (nuevoCuentas.getCodigo() == null) {
                retorno = false;
            } else {
                if (nuevoCuentas.getCodigo().isEmpty()) {
                    retorno = false;
                }
            }
        }
        if (i == 2) {
            if (duplicarCuentas.getDescripcion() == null) {
                retorno = false;
            } else {
                if (duplicarCuentas.getDescripcion().isEmpty()) {
                    retorno = false;
                }
            }
            if (duplicarCuentas.getCodigo() == null) {
                retorno = false;
            } else {
                if (duplicarCuentas.getCodigo().isEmpty()) {
                    retorno = false;
                }
            }
        }
        return retorno;
    }

    public void agregarNuevoCuenta() {
        if (validarDatosNull(1) == true) {
            if (bandera == 1) {
                altoTabla = "335";
                FacesContext c = FacesContext.getCurrentInstance();
                cuentaCodigo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigo");
                cuentaCodigo.setFilterStyle("display: none; visibility: hidden;");
                cuentasDescripcion = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentasDescripcion");
                cuentasDescripcion.setFilterStyle("display: none; visibility: hidden;");
                cuentaContracuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaContracuenta");
                cuentaContracuenta.setFilterStyle("display: none; visibility: hidden;");
                cuentaRubro = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaRubro");
                cuentaRubro.setFilterStyle("display: none; visibility: hidden;");
                cuentaManejaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNit");
                cuentaManejaNit.setFilterStyle("display: none; visibility: hidden;");
                cuentaManejaNitEmpleado = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNitEmpleado");
                cuentaManejaNitEmpleado.setFilterStyle("display: none; visibility: hidden;");
                cuentaProrrateo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaProrrateo");
                cuentaProrrateo.setFilterStyle("display: none; visibility: hidden;");
                cuentaCodigoA = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigoA");
                cuentaCodigoA.setFilterStyle("display: none; visibility: hidden;");
                cuentaConsolidaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaConsolidaNit");
                cuentaConsolidaNit.setFilterStyle("display: none; visibility: hidden;");
                cuentaIncluyeShort = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaIncluyeShort");
                cuentaIncluyeShort.setFilterStyle("display: none; visibility: hidden;");
                cuentaAsociadaSAP = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaAsociadaSAP");
                cuentaAsociadaSAP.setFilterStyle("display: none; visibility: hidden;");
                cuentaSubCuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaSubCuenta");
                cuentaSubCuenta.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosCuenta");
                bandera = 0;
                filtrarListCuentas = null;
                tipoLista = 0;
            }
            //AGREGAR REGISTRO A LA LISTA VIGENCIAS 
            k++;
            BigInteger var = BigInteger.valueOf(k);
            nuevoCuentas.setSecuencia(var);
            nuevoCuentas.setEmpresa(empresaActual);
            listCuentasCrear.add(nuevoCuentas);
            listCuentas.add(nuevoCuentas);
            //infoRegistro = "Cantidad de registros : " + listCuentas.size();
            contarRegistro();
            ////------////
            nuevoCuentas = new Cuentas();
            nuevoCuentas.setRubropresupuestal(new Rubrospresupuestales());
            nuevoCuentas.setContracuentatesoreria(new Cuentas());
            ////-----////
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroCuenta').hide()");
            cambiosCuentas = true;
            index = -1;
            secRegistroCuentas = null;
            activoDetalle = true;
            RequestContext.getCurrentInstance().update("form:DETALLES");
        } else {
            RequestContext.getCurrentInstance().execute("errorDatosNullCuenta').show()");
        }
    }

    public void limpiarNuevoCuenta() {
        nuevoCuentas = new Cuentas();
        nuevoCuentas.setRubropresupuestal(new Rubrospresupuestales());
        nuevoCuentas.setContracuentatesoreria(new Cuentas());
        index = -1;
        secRegistroCuentas = null;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    public void duplicarCuenta() {
        duplicarCuentas = new Cuentas();
        if (tipoLista == 0) {
            duplicarCuentas.setCodigo(listCuentas.get(index).getCodigo());
            duplicarCuentas.setDescripcion(listCuentas.get(index).getDescripcion());
            duplicarCuentas.setNaturaleza(listCuentas.get(index).getNaturaleza());
            duplicarCuentas.setContracuentatesoreria(listCuentas.get(index).getContracuentatesoreria());
            duplicarCuentas.setTipo(listCuentas.get(index).getTipo());
            duplicarCuentas.setRubropresupuestal(listCuentas.get(index).getRubropresupuestal());
            duplicarCuentas.setManejanit(listCuentas.get(index).getManejanit());
            duplicarCuentas.setManejanitempleado(listCuentas.get(index).getManejanitempleado());
            duplicarCuentas.setProrrateo(listCuentas.get(index).getProrrateo());
            duplicarCuentas.setManejacentrocosto(listCuentas.get(index).getManejacentrocosto());
            duplicarCuentas.setIncluyecentrocostocodigocuenta(listCuentas.get(index).getIncluyecentrocostocodigocuenta());
            duplicarCuentas.setCodigoalternativo(listCuentas.get(index).getCodigoalternativo());
            duplicarCuentas.setConsolidanitempresa(listCuentas.get(index).getConsolidanitempresa());
            duplicarCuentas.setCuentaasociadasap(listCuentas.get(index).getCuentaasociadasap());
            duplicarCuentas.setManejasubcuenta(listCuentas.get(index).getManejasubcuenta());
        }
        if (tipoLista == 1) {

            duplicarCuentas.setCodigo(filtrarListCuentas.get(index).getCodigo());
            duplicarCuentas.setDescripcion(filtrarListCuentas.get(index).getDescripcion());
            duplicarCuentas.setNaturaleza(filtrarListCuentas.get(index).getNaturaleza());
            duplicarCuentas.setContracuentatesoreria(filtrarListCuentas.get(index).getContracuentatesoreria());
            duplicarCuentas.setTipo(filtrarListCuentas.get(index).getTipo());
            duplicarCuentas.setRubropresupuestal(filtrarListCuentas.get(index).getRubropresupuestal());
            duplicarCuentas.setManejanit(filtrarListCuentas.get(index).getManejanit());
            duplicarCuentas.setManejanitempleado(filtrarListCuentas.get(index).getManejanitempleado());
            duplicarCuentas.setProrrateo(filtrarListCuentas.get(index).getProrrateo());
            duplicarCuentas.setManejacentrocosto(filtrarListCuentas.get(index).getManejacentrocosto());
            duplicarCuentas.setIncluyecentrocostocodigocuenta(filtrarListCuentas.get(index).getIncluyecentrocostocodigocuenta());
            duplicarCuentas.setCodigoalternativo(filtrarListCuentas.get(index).getCodigoalternativo());
            duplicarCuentas.setConsolidanitempresa(filtrarListCuentas.get(index).getConsolidanitempresa());
            duplicarCuentas.setCuentaasociadasap(filtrarListCuentas.get(index).getCuentaasociadasap());
            duplicarCuentas.setManejasubcuenta(filtrarListCuentas.get(index).getManejasubcuenta());

        }
        if (duplicarCuentas.getRubropresupuestal().getSecuencia() == null) {
            duplicarCuentas.setRubropresupuestal(new Rubrospresupuestales());
        }
        if (duplicarCuentas.getContracuentatesoreria().getSecuencia() == null) {
            duplicarCuentas.setContracuentatesoreria(new Cuentas());
        }
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroCuenta");
        RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCuenta').show()");
        index = -1;
        secRegistroCuentas = null;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    public void confirmarDuplicar() {
        if (validarDatosNull(2) == true) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                altoTabla = "335";
                cuentaCodigo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigo");
                cuentaCodigo.setFilterStyle("display: none; visibility: hidden;");
                cuentasDescripcion = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentasDescripcion");
                cuentasDescripcion.setFilterStyle("display: none; visibility: hidden;");
                cuentaContracuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaContracuenta");
                cuentaContracuenta.setFilterStyle("display: none; visibility: hidden;");
                cuentaRubro = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaRubro");
                cuentaRubro.setFilterStyle("display: none; visibility: hidden;");
                cuentaManejaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNit");
                cuentaManejaNit.setFilterStyle("display: none; visibility: hidden;");
                cuentaManejaNitEmpleado = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNitEmpleado");
                cuentaManejaNitEmpleado.setFilterStyle("display: none; visibility: hidden;");
                cuentaProrrateo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaProrrateo");
                cuentaProrrateo.setFilterStyle("display: none; visibility: hidden;");
                cuentaCodigoA = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigoA");
                cuentaCodigoA.setFilterStyle("display: none; visibility: hidden;");
                cuentaConsolidaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaConsolidaNit");
                cuentaConsolidaNit.setFilterStyle("display: none; visibility: hidden;");
                cuentaIncluyeShort = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaIncluyeShort");
                cuentaIncluyeShort.setFilterStyle("display: none; visibility: hidden;");
                cuentaAsociadaSAP = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaAsociadaSAP");
                cuentaAsociadaSAP.setFilterStyle("display: none; visibility: hidden;");
                cuentaSubCuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaSubCuenta");
                cuentaSubCuenta.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosCuenta");
                bandera = 0;
                filtrarListCuentas = null;
                tipoLista = 0;
            }
            k++;
            BigInteger var = BigInteger.valueOf(k);
            duplicarCuentas.setSecuencia(var);
            duplicarCuentas.setEmpresa(empresaActual);
            listCuentasCrear.add(duplicarCuentas);
            listCuentas.add(duplicarCuentas);
            duplicarCuentas = new Cuentas();
            //infoRegistro = "Cantidad de registros : " + listCuentas.size();
            contarRegistro();
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroCuenta').hide()");
            cambiosCuentas = true;
            index = -1;
            secRegistroCuentas = null;
            activoDetalle = true;
            RequestContext.getCurrentInstance().update("form:DETALLES");
        } else {
            RequestContext.getCurrentInstance().execute("errorDatosNullCuenta').show()");
        }
    }

    public void limpiarDuplicarCuenta() {
        duplicarCuentas = new Cuentas();
        nuevoCuentas.setRubropresupuestal(new Rubrospresupuestales());
        nuevoCuentas.setContracuentatesoreria(new Cuentas());
        index = -1;
        secRegistroCuentas = null;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    public void borrarCuenta() {
        if (index >= 0) {
            if (tipoLista == 0) {
                if (!listCuentasModificar.isEmpty() && listCuentasModificar.contains(listCuentas.get(index))) {
                    int modIndex = listCuentasModificar.indexOf(listCuentas.get(index));
                    listCuentasModificar.remove(modIndex);
                    listCuentasBorrar.add(listCuentas.get(index));
                } else if (!listCuentasCrear.isEmpty() && listCuentasCrear.contains(listCuentas.get(index))) {
                    int crearIndex = listCuentasCrear.indexOf(listCuentas.get(index));
                    listCuentasCrear.remove(crearIndex);
                } else {
                    listCuentasBorrar.add(listCuentas.get(index));
                }
                listCuentas.remove(index);
                //infoRegistro = "Cantidad de registros : " + listCuentas.size();
                contarRegistro();
            }
            if (tipoLista == 1) {
                if (!listCuentasModificar.isEmpty() && listCuentasModificar.contains(filtrarListCuentas.get(index))) {
                    int modIndex = listCuentasModificar.indexOf(filtrarListCuentas.get(index));
                    listCuentasModificar.remove(modIndex);
                    listCuentasBorrar.add(filtrarListCuentas.get(index));
                } else if (!listCuentasCrear.isEmpty() && listCuentasCrear.contains(filtrarListCuentas.get(index))) {
                    int crearIndex = listCuentasCrear.indexOf(filtrarListCuentas.get(index));
                    listCuentasCrear.remove(crearIndex);
                } else {
                    listCuentasBorrar.add(filtrarListCuentas.get(index));
                }
                int VLIndex = listCuentas.indexOf(filtrarListCuentas.get(index));
                listCuentas.remove(VLIndex);
                filtrarListCuentas.remove(index);
                //infoRegistro = "Cantidad de registros : " + filtrarListCuentas.size();
                contarRegistro();
            }
            RequestContext.getCurrentInstance().update("form:informacionRegistro");
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosCuenta");

            index = -1;
            activoDetalle = true;
            RequestContext.getCurrentInstance().update("form:DETALLES");
            secRegistroCuentas = null;
            cambiosCuentas = true;
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        }
    }

    public void activarCtrlF11() {
        filtradoCuenta();
    }

    public void filtradoCuenta() {

        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "315";
            cuentaCodigo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigo");
            cuentaCodigo.setFilterStyle("width: 85% !important");
            cuentasDescripcion = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentasDescripcion");
            cuentasDescripcion.setFilterStyle("width: 85% !important");
            cuentaContracuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaContracuenta");
            cuentaContracuenta.setFilterStyle("width: 85% !important");
            cuentaRubro = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaRubro");
            cuentaRubro.setFilterStyle("width: 85% !important");
            cuentaManejaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNit");
            cuentaManejaNit.setFilterStyle("width: 85% !important");
            cuentaManejaNitEmpleado = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNitEmpleado");
            cuentaManejaNitEmpleado.setFilterStyle("width: 85% !important");
            cuentaProrrateo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaProrrateo");
            cuentaProrrateo.setFilterStyle("width: 85% !important");
            cuentaCodigoA = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigoA");
            cuentaCodigoA.setFilterStyle("width: 85% !important");
            cuentaConsolidaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaConsolidaNit");
            cuentaConsolidaNit.setFilterStyle("width: 85% !important");
            cuentaIncluyeShort = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaIncluyeShort");
            cuentaIncluyeShort.setFilterStyle("width: 85% !important");
            cuentaAsociadaSAP = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaAsociadaSAP");
            cuentaAsociadaSAP.setFilterStyle("width: 85% !important");
            cuentaSubCuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaSubCuenta");
            cuentaSubCuenta.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "335";
            cuentaCodigo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigo");
            cuentaCodigo.setFilterStyle("display: none; visibility: hidden;");
            cuentasDescripcion = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentasDescripcion");
            cuentasDescripcion.setFilterStyle("display: none; visibility: hidden;");
            cuentaContracuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaContracuenta");
            cuentaContracuenta.setFilterStyle("display: none; visibility: hidden;");
            cuentaRubro = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaRubro");
            cuentaRubro.setFilterStyle("display: none; visibility: hidden;");
            cuentaManejaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNit");
            cuentaManejaNit.setFilterStyle("display: none; visibility: hidden;");
            cuentaManejaNitEmpleado = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNitEmpleado");
            cuentaManejaNitEmpleado.setFilterStyle("display: none; visibility: hidden;");
            cuentaProrrateo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaProrrateo");
            cuentaProrrateo.setFilterStyle("display: none; visibility: hidden;");
            cuentaCodigoA = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigoA");
            cuentaCodigoA.setFilterStyle("display: none; visibility: hidden;");
            cuentaConsolidaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaConsolidaNit");
            cuentaConsolidaNit.setFilterStyle("display: none; visibility: hidden;");
            cuentaIncluyeShort = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaIncluyeShort");
            cuentaIncluyeShort.setFilterStyle("display: none; visibility: hidden;");
            cuentaAsociadaSAP = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaAsociadaSAP");
            cuentaAsociadaSAP.setFilterStyle("display: none; visibility: hidden;");
            cuentaSubCuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaSubCuenta");
            cuentaSubCuenta.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            bandera = 0;
            filtrarListCuentas = null;
            tipoLista = 0;
        }

    }

    public void salir() {
        if (bandera == 1) {
            altoTabla = "335";
            FacesContext c = FacesContext.getCurrentInstance();
            cuentaCodigo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigo");
            cuentaCodigo.setFilterStyle("display: none; visibility: hidden;");
            cuentasDescripcion = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentasDescripcion");
            cuentasDescripcion.setFilterStyle("display: none; visibility: hidden;");
            cuentaContracuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaContracuenta");
            cuentaContracuenta.setFilterStyle("display: none; visibility: hidden;");
            cuentaRubro = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaRubro");
            cuentaRubro.setFilterStyle("display: none; visibility: hidden;");
            cuentaManejaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNit");
            cuentaManejaNit.setFilterStyle("display: none; visibility: hidden;");
            cuentaManejaNitEmpleado = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaManejaNitEmpleado");
            cuentaManejaNitEmpleado.setFilterStyle("display: none; visibility: hidden;");
            cuentaProrrateo = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaProrrateo");
            cuentaProrrateo.setFilterStyle("display: none; visibility: hidden;");
            cuentaCodigoA = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaCodigoA");
            cuentaCodigoA.setFilterStyle("display: none; visibility: hidden;");
            cuentaConsolidaNit = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaConsolidaNit");
            cuentaConsolidaNit.setFilterStyle("display: none; visibility: hidden;");
            cuentaIncluyeShort = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaIncluyeShort");
            cuentaIncluyeShort.setFilterStyle("display: none; visibility: hidden;");
            cuentaAsociadaSAP = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaAsociadaSAP");
            cuentaAsociadaSAP.setFilterStyle("display: none; visibility: hidden;");
            cuentaSubCuenta = (Column) c.getViewRoot().findComponent("form:datosCuenta:cuentaSubCuenta");
            cuentaSubCuenta.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            bandera = 0;
            filtrarListCuentas = null;
            tipoLista = 0;
        }

        listCuentasBorrar.clear();
        listCuentasCrear.clear();
        listCuentasModificar.clear();
        index = -1;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
        secRegistroCuentas = null;
        listCuentas = null;
        listaEmpresas = null;
        empresaActual = null;
        k = 0;
        listCuentas = null;
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void asignarIndex(Integer indice, int dlg, int LND) {
        RequestContext context = RequestContext.getCurrentInstance();
        if (LND == 0) {
            index = indice;
            tipoActualizacion = 0;
        } else if (LND == 1) {
            tipoActualizacion = 1;
        } else if (LND == 2) {
            tipoActualizacion = 2;
        }
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("form:ContracuentaDialogo");
            RequestContext.getCurrentInstance().execute("PF('ContracuentaDialogo').show()");
        } else if (dlg == 1) {
            RequestContext.getCurrentInstance().update("form:RubrosDialogo");
            RequestContext.getCurrentInstance().execute("PF('RubrosDialogo').show()");
        }
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (index >= 0) {
            if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("form:ContracuentaDialogo");
                RequestContext.getCurrentInstance().execute("PF('ContracuentaDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("form:RubrosDialogo");
                RequestContext.getCurrentInstance().execute("PF('RubrosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("CONTRACUENTA")) {
            if (tipoNuevo == 1) {
                cuenta = nuevoCuentas.getContracuentatesoreria().getDescripcion();
            } else if (tipoNuevo == 2) {
                cuenta = duplicarCuentas.getContracuentatesoreria().getDescripcion();
            }
        } else if (Campo.equals("RUBRO")) {
            if (tipoNuevo == 1) {
                rubroP = nuevoCuentas.getRubropresupuestal().getDescripcion();
            } else if (tipoNuevo == 2) {
                rubroP = duplicarCuentas.getRubropresupuestal().getDescripcion();
            }
        }
    }

    public void autocompletarNuevoyDuplicadoCuenta(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("CONTRACUENTA")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevoCuentas.getContracuentatesoreria().setDescripcion(cuenta);
                } else if (tipoNuevo == 2) {
                    duplicarCuentas.getContracuentatesoreria().setDescripcion(cuenta);
                }
                for (int i = 0; i < listCuentasTesoreria.size(); i++) {
                    if (listCuentasTesoreria.get(i).getCodigo().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevoCuentas.setContracuentatesoreria(listCuentasTesoreria.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
                    } else if (tipoNuevo == 2) {
                        duplicarCuentas.setContracuentatesoreria(listCuentasTesoreria.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroCT");
                    }
                    listCuentasTesoreria.clear();
                    getListCuentasTesoreria();
                } else {
                    RequestContext.getCurrentInstance().update("form:TerceroDialogo");
                    RequestContext.getCurrentInstance().execute("PF('TerceroDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroCT");
                    }
                }
            } else {
                if (tipoNuevo == 1) {
                    nuevoCuentas.setContracuentatesoreria(new Cuentas());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
                } else if (tipoNuevo == 2) {
                    duplicarCuentas.setContracuentatesoreria(new Cuentas());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTerceroCT");
                }
                listCuentasTesoreria.clear();
                getListCuentasTesoreria();
            }
        } else if (confirmarCambio.equalsIgnoreCase("RUBROS")) {
            if (!valorConfirmar.isEmpty()) {
                if (tipoNuevo == 1) {
                    nuevoCuentas.getRubropresupuestal().setDescripcion(rubroP);
                } else if (tipoNuevo == 2) {
                    duplicarCuentas.getRubropresupuestal().setDescripcion(rubroP);
                }
                for (int i = 0; i < listRubros.size(); i++) {
                    if (listRubros.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    if (tipoNuevo == 1) {
                        nuevoCuentas.setRubropresupuestal(listRubros.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
                    } else if (tipoNuevo == 2) {
                        duplicarCuentas.setRubropresupuestal(listRubros.get(indiceUnicoElemento));
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
                    }
                    listRubros.clear();
                    getListRubros();
                } else {
                    RequestContext.getCurrentInstance().update("form:CiudadDialogo");
                    RequestContext.getCurrentInstance().execute("PF('CiudadDialogo').show()");
                    tipoActualizacion = tipoNuevo;
                    if (tipoNuevo == 1) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
                    } else if (tipoNuevo == 2) {
                        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
                    }
                }
            } else {
                if (tipoNuevo == 1) {
                    nuevoCuentas.setRubropresupuestal(new Rubrospresupuestales());
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCodigoAT");
                } else if (tipoNuevo == 2) {
                    duplicarCuentas.setRubropresupuestal(new Rubrospresupuestales());
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigoAT");
                }
                listRubros.clear();
                getListRubros();
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarCuenta() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listCuentas.get(index).setContracuentatesoreria(cuentaSeleccionada);
                if (!listCuentasCrear.contains(listCuentas.get(index))) {
                    if (listCuentasModificar.isEmpty()) {
                        listCuentasModificar.add(listCuentas.get(index));
                    } else if (!listCuentasModificar.contains(listCuentas.get(index))) {
                        listCuentasModificar.add(listCuentas.get(index));
                    }
                }
            } else {
                filtrarListCuentas.get(index).setContracuentatesoreria(cuentaSeleccionada);
                if (!listCuentasCrear.contains(filtrarListCuentas.get(index))) {
                    if (listCuentasModificar.isEmpty()) {
                        listCuentasModificar.add(filtrarListCuentas.get(index));
                    } else if (!listCuentasModificar.contains(filtrarListCuentas.get(index))) {
                        listCuentasModificar.add(filtrarListCuentas.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosCuentas = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:datosCuenta");
        } else if (tipoActualizacion == 1) {
            nuevoCuentas.setContracuentatesoreria(cuentaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTerceroCT");
        } else if (tipoActualizacion == 2) {
            duplicarCuentas.setContracuentatesoreria(cuentaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTTerceroCT");
        }
        filtrarListCuentas = null;
        cuentaSeleccionada = null;
        aceptar = true;
        index = -1;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
        secRegistroCuentas = null;
        tipoActualizacion = -1;
        /*
         RequestContext.getCurrentInstance().update("form:ContracuentaDialogo");
         RequestContext.getCurrentInstance().update("form:lovContracuenta");
         RequestContext.getCurrentInstance().update("form:aceptarCC");*/
        context.reset("form:lovContracuenta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovContracuenta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ContracuentaDialogo').hide()");
    }

    public void cancelarCambioCuenta() {
        filtrarListCuentas = null;
        cuentaSeleccionada = null;
        aceptar = true;
        index = -1;
        activoDetalle = true;
        secRegistroCuentas = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:DETALLES");
        context.reset("form:lovContracuenta:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovContracuenta').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('ContracuentaDialogo').hide()");
    }

    public void actualizarRubro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                listCuentas.get(index).setRubropresupuestal(rubroSeleccionado);
                if (!listCuentasCrear.contains(listCuentas.get(index))) {
                    if (listCuentasModificar.isEmpty()) {
                        listCuentasModificar.add(listCuentas.get(index));
                    } else if (!listCuentasModificar.contains(listCuentas.get(index))) {
                        listCuentasModificar.add(listCuentas.get(index));
                    }
                }
            } else {
                filtrarListCuentas.get(index).setRubropresupuestal(rubroSeleccionado);
                if (!listCuentasCrear.contains(filtrarListCuentas.get(index))) {
                    if (listCuentasModificar.isEmpty()) {
                        listCuentasModificar.add(filtrarListCuentas.get(index));
                    } else if (!listCuentasModificar.contains(filtrarListCuentas.get(index))) {
                        listCuentasModificar.add(filtrarListCuentas.get(index));
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            cambiosCuentas = true;
            permitirIndex = true;

            RequestContext.getCurrentInstance().update("form:datosCuenta");
        } else if (tipoActualizacion == 1) {
            nuevoCuentas.setRubropresupuestal(rubroSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudadT");
        } else if (tipoActualizacion == 2) {
            duplicarCuentas.setRubropresupuestal(rubroSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTCiudadT");
        }
        filtrarListRubros = null;
        rubroSeleccionado = null;
        aceptar = true;
        index = -1;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
        secRegistroCuentas = null;
        tipoActualizacion = -1;
        /*
         RequestContext.getCurrentInstance().update("form:RubrosDialogo");
         RequestContext.getCurrentInstance().update("form:lovRubros");
         RequestContext.getCurrentInstance().update("form:aceptarRP");*/
        context.reset("form:lovRubros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovRubros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('RubrosDialogo').hide()");
    }

    public void cancelarCambioRubro() {
        filtrarListRubros = null;
        rubroSeleccionado = null;
        aceptar = true;
        index = -1;
        activoDetalle = true;
        secRegistroCuentas = null;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:DETALLES");
        context.reset("form:lovRubros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovRubros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('RubrosDialogo').hide()");
    }

    public String exportXML() {
        if (index >= 0) {
            nombreTabla = ":formExportarCuentas:datosCuentasExportar";
            nombreXML = "CuentasXML";
        }
        return nombreTabla;
    }

    public void validarExportPDF() throws IOException {
        if (index >= 0) {
            exportPDFT();
        }
    }

    public void exportPDFT() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarCuentas:datosCuentasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "CuentasPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistroCuentas = null;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    public void verificarExportXLS() throws IOException {
        if (index >= 0) {
            exportXLST();
        }

    }

    public void exportXLST() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarCuentas:datosCuentasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "CuentasXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistroCuentas = null;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
 contarRegistro();
    }

    public void dialogoSeleccionarCuenta() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("Empresa Actual : " + empresaActual.getNombre());
        getListCuentasTesoreria();
        RequestContext.getCurrentInstance().update("form:BuscarCuentasDialogo");
        RequestContext.getCurrentInstance().execute("PF('BuscarCuentasDialogo').show()");
    }

    public void validarSeleccionCuenta() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (cambiosCuentas == false) {
            listCuentas = null;
            listCuentas = new ArrayList<Cuentas>();
            listCuentas.add(cuentaSeleccionada);
            cuentaSeleccionada = new Cuentas();
            filtrarListCuentasTesoreria = null;
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            /*
             RequestContext.getCurrentInstance().update("form:BuscarCuentasDialogo");
             RequestContext.getCurrentInstance().update("form:lovCuentas");
             RequestContext.getCurrentInstance().update("form:aceptarBC");*/
            context.reset("form:lovCuentas:globalFilter");
            RequestContext.getCurrentInstance().execute("PF('lovCuentas').clearFilters()");
            RequestContext.getCurrentInstance().execute("PF('BuscarCuentasDialogo').hide()");
        } else {
            cuentaSeleccionada = new Cuentas();
            filtrarListCuentasTesoreria = null;
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void cancelarSeleccionCuenta() {
        RequestContext context = RequestContext.getCurrentInstance();
        cuentaSeleccionada = new Cuentas();
        filtrarListCuentasTesoreria = null;
        context.reset("form:lovCuentas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovCuentas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('BuscarCuentasDialogo').hide()");
    }

    public void mostrarTodos() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (cambiosCuentas == false) {
            listCuentas = null;
            getListCuentas();
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            cuentaActual = null;
        } else {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    //METODO RASTROS PARA LAS TABLAS EN EMPLVIGENCIASUELDOS
    public void verificarRastroTabla() {
        verificarRastroTerceros();
        index = -1;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    //Verificar Rastro Vigencia Sueldos
    public void verificarRastroTerceros() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listCuentas != null) {
            if (secRegistroCuentas != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistroCuentas, "CUENTAS");
                backUpSecRegistroCuentas = secRegistroCuentas;
                backUp = secRegistroCuentas;
                secRegistroCuentas = null;
                if (resultado == 1) {
                    RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
                } else if (resultado == 2) {
                    nombreTablaRastro = "Cuentas";
                    msnConfirmarRastro = "La tabla CUENTAS tiene rastros para el registro seleccionado, ¿desea continuar?";
                    RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
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
            if (administrarRastros.verificarHistoricosTabla("CUENTAS")) {
                nombreTablaRastro = "Cuentas";
                msnConfirmarRastroHistorico = "La tabla CUENTAS tiene rastros historicos, ¿Desea continuar?";
                RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
                RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
            } else {
                RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
            }
        }
        index = -1;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
    }

    public void lovEmpresas() {
        index = -1;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
        secRegistroCuentas = null;
        cualCelda = -1;
        RequestContext.getCurrentInstance().execute("EmpresasDialogo').show()");
    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (cambiosCuentas == false) {
            aceptar = true;
            empresaActual = empresaSeleccionada;
            backUpEmpresaActual = empresaActual;
            listCuentasTesoreria = null;
            cuentaActual = null;
            listCuentas = null;
            getListCuentas();
         contarRegistro();
            getListCuentasTesoreria();
            RequestContext.getCurrentInstance().update("form:nombreEmpresa");
            RequestContext.getCurrentInstance().update("form:nitEmpresa");
            RequestContext.getCurrentInstance().update("form:datosCuenta");
            RequestContext.getCurrentInstance().update("form:informacionRegistro");

            //empresaSeleccionada = new Empresas();
            filtrarListEmpresas = null;

            RequestContext.getCurrentInstance().update("formularioDialogos:EmpresasDialogo");
            RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
            RequestContext.getCurrentInstance().update("formularioDialogos:aceptarE");
            context.reset("formularioDialogos:lovEmpresas:globalFilter");
            RequestContext.getCurrentInstance().execute("PF('EmpresasDialogo').hide()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void cancelarCambioEmpresa() {
        index = -1;
        activoDetalle = true;
        RequestContext.getCurrentInstance().update("form:DETALLES");
        filtrarListEmpresas = null;
    }

    public void limpiarMSNRastros() {
        msnConfirmarRastro = "";
        msnConfirmarRastroHistorico = "";
        nombreTablaRastro = "";
    }
    private void contarRegistro() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }
    
    private void contarRegistroLov(){
        RequestContext.getCurrentInstance().update("form:infoRegistroBuscarCuenta");
    }
    
    public void verDetalle(Cuentas cuentaS) {
        cuentaSeleccionada = cuentaS;
        //RequestContext context = RequestContext.getCurrentInstance();
        FacesContext fc = FacesContext.getCurrentInstance();
        //((ControlDetalleConcepto) fc.getApplication().evaluateExpressionGet(fc, "#{ControlDetalleConcepto}", ControlDetalleConcepto.class)).obtenerConcepto(secuencia);

        fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "detalleCuenta");
//.getElementById('datosConceptos').scrollTop;
    }
    
   

    //GET - SET 
    public List<Cuentas> getListCuentas() {
        try {
            if (listCuentas == null) {
                if (empresaActual.getSecuencia() != null) {
                    listCuentas = administrarCuentas.consultarCuentasEmpresa(empresaActual.getSecuencia());
                    if (listCuentas != null) {
                        for (int i = 0; i < listCuentas.size(); i++) {
                            if (listCuentas.get(i).getRubropresupuestal() == null) {
                                listCuentas.get(i).setRubropresupuestal(new Rubrospresupuestales());
                            }
                            if (listCuentas.get(i).getContracuentatesoreria() == null) {
                                listCuentas.get(i).setContracuentatesoreria(new Cuentas());
                            }
                        }
                    }
                }
            }
            return listCuentas;
        } catch (Exception e) {
            System.out.println("Error getListCuentas " + e.toString());
            return null;
        }
    }

    public void setListCuentas(List<Cuentas> t) {
        this.listCuentas = t;
    }

    public List<Cuentas> getFiltrarListCuentas() {
        return filtrarListCuentas;
    }

    public void setFiltrarListCuentas(List<Cuentas> t) {
        this.filtrarListCuentas = t;
    }

    public String getNombreXML() {
        return nombreXML;
    }

    public void setNombreXML(String nombreXML) {
        this.nombreXML = nombreXML;
    }

    public String getNombreTabla() {
        return nombreTabla;
    }

    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public BigInteger getSecRegistroCuentas() {
        return secRegistroCuentas;
    }

    public void setSecRegistroCuentas(BigInteger secRegistroCuentas) {
        this.secRegistroCuentas = secRegistroCuentas;
    }

    public BigInteger getBackUpSecRegistroCuentas() {
        return backUpSecRegistroCuentas;
    }

    public void setBackUpSecRegistroCuentas(BigInteger b) {
        this.backUpSecRegistroCuentas = b;
    }

    public List<Cuentas> getListCuentasModificar() {
        return listCuentasModificar;
    }

    public void setListCuentasModificar(List<Cuentas> listTerceroCuentas) {
        this.listCuentasModificar = listTerceroCuentas;
    }

    public Cuentas getNuevoCuentas() {
        return nuevoCuentas;
    }

    public void setNuevoCuentas(Cuentas nuevoCuentas) {
        this.nuevoCuentas = nuevoCuentas;
    }

    public List<Cuentas> getListCuentasCrear() {
        return listCuentasCrear;
    }

    public void setListCuentasCrear(List<Cuentas> listCuentasCrear) {
        this.listCuentasCrear = listCuentasCrear;
    }

    public List<Cuentas> getListCuentasBorrar() {
        return listCuentasBorrar;
    }

    public void setListCuentasBorrar(List<Cuentas> listCuentasBorrar) {
        this.listCuentasBorrar = listCuentasBorrar;
    }

    public Cuentas getEditarCuentas() {
        return editarCuentas;
    }

    public void setEditarCuentas(Cuentas editarCuentas) {
        this.editarCuentas = editarCuentas;
    }

    public Cuentas getDuplicarCuentas() {
        return duplicarCuentas;
    }

    public void setDuplicarCuentas(Cuentas duplicarCuentas) {
        this.duplicarCuentas = duplicarCuentas;
    }

    public BigInteger getSecRegistroCuentaso() {
        return secRegistroCuentas;
    }

    public String getMsnConfirmarRastro() {
        return msnConfirmarRastro;
    }

    public void setMsnConfirmarRastro(String msnConfirmarRastro) {
        this.msnConfirmarRastro = msnConfirmarRastro;
    }

    public String getMsnConfirmarRastroHistorico() {
        return msnConfirmarRastroHistorico;
    }

    public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
        this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
    }

    public BigInteger getBackUp() {
        return backUp;
    }

    public void setBackUp(BigInteger backUp) {
        this.backUp = backUp;
    }

    public String getNombreTablaRastro() {
        return nombreTablaRastro;
    }

    public void setNombreTablaRastro(String nombreTablaRastro) {
        this.nombreTablaRastro = nombreTablaRastro;
    }

    public List<Empresas> getListEmpresas() {
        listaEmpresas = administrarCuentas.consultarEmpresas();
        return listaEmpresas;
    }

    public void setListEmpresas(List<Empresas> listEmpresas) {
        this.listaEmpresas = listEmpresas;
    }

    public List<Empresas> getFiltrarListEmpresas() {
        return filtrarListEmpresas;
    }

    public void setFiltrarListEmpresas(List<Empresas> filtrarListEmpresas) {
        this.filtrarListEmpresas = filtrarListEmpresas;
    }

    public Empresas getEmpresaActual() {
        return empresaActual;
    }

    public void setEmpresaActual(Empresas empresaActual) {
        this.empresaActual = empresaActual;
    }

    public Empresas getBackUpEmpresaActual() {
        return backUpEmpresaActual;
    }

    public void setBackUpEmpresaActual(Empresas backUpEmpresaActual) {
        this.backUpEmpresaActual = backUpEmpresaActual;
    }

    public List<Rubrospresupuestales> getListRubros() {
        listRubros = administrarCuentas.consultarLOVRubros();

        return listRubros;
    }

    public void setListRubros(List<Rubrospresupuestales> listCiudades) {
        this.listRubros = listCiudades;
    }

    public List<Rubrospresupuestales> getFiltrarListRubros() {
        return filtrarListRubros;
    }

    public void setFiltrarListRubros(List<Rubrospresupuestales> filtrar) {
        this.filtrarListRubros = filtrar;
    }

    public Rubrospresupuestales getRubroSeleccionado() {
        return rubroSeleccionado;
    }

    public void setRubroSeleccionado(Rubrospresupuestales seleccionado) {
        this.rubroSeleccionado = seleccionado;
    }

    public List<Cuentas> getListCuentasTesoreria() {
        if (empresaActual.getSecuencia() != null) {
            listCuentasTesoreria = administrarCuentas.consultarCuentasEmpresa(empresaActual.getSecuencia());
        }
        return listCuentasTesoreria;
    }

    public void setListCuentasTesoreria(List<Cuentas> CuentasTesoreria) {
        this.listCuentasTesoreria = CuentasTesoreria;
    }

    public List<Cuentas> getFiltrarListCuentasTesoreria() {
        return filtrarListCuentasTesoreria;
    }

    public void setFiltrarListCuentasTesoreria(List<Cuentas> filtrarList) {
        this.filtrarListCuentasTesoreria = filtrarList;
    }

    public Cuentas getCuentaSeleccionada() {
        return cuentaSeleccionada;
    }

    public void setCuentaSeleccionada(Cuentas seleccionado) {
        this.cuentaSeleccionada = seleccionado;
    }

    public Cuentas getCuentaActual() {
        return cuentaActual;
    }

    public void setCuentaActual(Cuentas cuentaActual) {
        this.cuentaActual = cuentaActual;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public String getInfoRegistro() {
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isActivoDetalle() {
        return activoDetalle;
    }

    public void setActivoDetalle(boolean activoDetalle) {
        this.activoDetalle = activoDetalle;
    }

    public String getInfoRegistroBuscarCuenta() {
        getListCuentasTesoreria();
        if (listCuentasTesoreria != null) {
            //infoRegistroBuscarCuenta = "Cantidad de registros : " + listCuentasTesoreria.size();
            infoRegistroBuscarCuenta = String.valueOf(listCuentasTesoreria.size());
        } else {
            //infoRegistroBuscarCuenta = "Cantidad de registros : 0";
            infoRegistroBuscarCuenta = "0";
        }
        return infoRegistroBuscarCuenta;
    }

    public void setInfoRegistroBuscarCuenta(String infoRegistroBuscarCuenta) {
        this.infoRegistroBuscarCuenta = infoRegistroBuscarCuenta;
    }

    public String getInfoRegistroRubro() {
        getListRubros();
        if (listRubros != null) {
            //infoRegistroRubro = "Cantidad de registros : " + listRubros.size();
            infoRegistroRubro = String.valueOf(listRubros.size());
        } else {
            //infoRegistroRubro = "Cantidad de registros : 0";
            infoRegistroRubro = "0";
        }
        return infoRegistroRubro;
    }

    public void setInfoRegistroRubro(String infoRegistroRubro) {
        this.infoRegistroRubro = infoRegistroRubro;
    }

    public String getInfoRegistroContraCuenta() {
        getListCuentasTesoreria();
        if (listCuentasTesoreria != null) {
            //infoRegistroContraCuenta = "Cantidad de registros : " + listCuentasTesoreria.size();
            infoRegistroContraCuenta = String.valueOf(listCuentasTesoreria.size());
        } else {
            //infoRegistroContraCuenta = "Cantidad de registros : 0";
            infoRegistroContraCuenta = "0";
        }
        return infoRegistroContraCuenta;
    }

    public void setInfoRegistroContraCuenta(String infoRegistroContraCuenta) {
        this.infoRegistroContraCuenta = infoRegistroContraCuenta;
    }

    public String getInfoRegistroEmpresa() {
        getListEmpresas();
        if (listaEmpresas != null) {
            //infoRegistroEmpresa = "Cantidad de registros : " + listaEmpresas.size();
            infoRegistroEmpresa = String.valueOf(listaEmpresas.size());
        } else {
            //infoRegistroEmpresa = "Cantidad de registros : 0";
            infoRegistroEmpresa = "0";
        }
        return infoRegistroEmpresa;
    }

    public void setInfoRegistroEmpresa(String infoRegistroEmpresa) {
        this.infoRegistroEmpresa = infoRegistroEmpresa;
    }

    public Cuentas getCuentaTablaSeleccionada() {
        getListCuentas();
        if (listCuentas != null) {
            int tam = listCuentas.size();
            if (tam > 0) {
                cuentaTablaSeleccionada = listCuentas.get(0);
            }
        }
        return cuentaTablaSeleccionada;
    }

    public void setCuentaTablaSeleccionada(Cuentas cuentaTablaSeleccionada) {
        this.cuentaTablaSeleccionada = cuentaTablaSeleccionada;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

}
