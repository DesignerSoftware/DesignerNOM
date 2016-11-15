/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empresas;
import Entidades.NovedadesAutoLiquidaciones;
import Entidades.ParametrosAutoliq;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import Exportar.ExportarPDF;
import Exportar.ExportarPDFTablasAnchas;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNovedadAutoLiquidacionInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlNovedadAutoLiquidacion implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarNovedadAutoLiquidacionInterface administrarNovedadAutoLiquidaciones;

    private List<NovedadesAutoLiquidaciones> listaNovedades;
    private List<NovedadesAutoLiquidaciones> filtrarlistaNovedades;
    private NovedadesAutoLiquidaciones novedadSeleccionada;
    private NovedadesAutoLiquidaciones nuevanovedad;
    private NovedadesAutoLiquidaciones editarnovedad;
    private NovedadesAutoLiquidaciones duplicarnovedad;

    //lovEmpresas
    private List<Empresas> listaEmpresas;
    private List<Empresas> filtrarlistaEmpresas;
    private Empresas empresaSeleccionada;
    //lovTipoEntidad
    private List<TiposEntidades> listaTiposEntidades;
    private List<TiposEntidades> filtrarlistaTiposEntidades;
    private TiposEntidades tipoEntidadSeleccionada;
    //lovTerceros
    private List<Terceros> listaTerceros;
    private List<Terceros> filtrarlistaTerceros;
    private Terceros terceroSeleccionado;
    //lov sucursalesPila
    private List<SucursalesPila> listaSucursales;
    private List<SucursalesPila> filtrarlistaSucursales;
    private SucursalesPila sucursalSeleccionada;

    private int cualCelda, tipoLista, k;
    private List<NovedadesAutoLiquidaciones> listaNovedadesCrear;
    private List<NovedadesAutoLiquidaciones> listaNovedadesModificar;
    private List<NovedadesAutoLiquidaciones> listaNovedadesBorrar;
    private boolean aceptar;
    private int tipoActualizacion; //Activo/Desactivo Crtl + F11
    private int bandera;
    private boolean permitirIndex, activarlov;
    private int altotabla;
    private String paginaAnterior;
    private BigInteger l, auxiliar, secuenciaParametro, anioParametro, mesParametro;

    //RASTROS
    private boolean guardado;
    //COLUMNAS
    private Column empresa, sucursal, anio, mes, tipoEntidad, formularioUnico, numRadicado, correccion, anioMes, planillaCorregir, dias;
    private Column intMora, radicacionDcto, nit, nombreTercero, saldoFavor, vlrMoraUPC, saldofavorUPC, vlrMoraSolidaridad, vlrSusbsistencia, vlrOtros, destino;
    private String infoRegistroNovedades, infoRegistroEmpresas, infoRegistroTerceros, infoRegistroSucursales, infoRegistroTipoEntidad;
    private String mensajeValidacion;

    public ControlNovedadAutoLiquidacion() {
        listaNovedadesCrear = new ArrayList<NovedadesAutoLiquidaciones>();
        listaNovedadesBorrar = new ArrayList<NovedadesAutoLiquidaciones>();
        listaNovedadesModificar = new ArrayList<NovedadesAutoLiquidaciones>();
        listaEmpresas = null;
        empresaSeleccionada = new Empresas();
        listaSucursales = null;
        sucursalSeleccionada = new SucursalesPila();
        listaTiposEntidades = null;
        tipoEntidadSeleccionada = new TiposEntidades();
        listaTerceros = null;
        terceroSeleccionado = new Terceros();
        nuevanovedad = new NovedadesAutoLiquidaciones();
        nuevanovedad.setTipoentidad(new TiposEntidades());
        nuevanovedad.setTercero(new Terceros());
        nuevanovedad.setEmpresa(new Empresas());
        nuevanovedad.setSucursalpila(new SucursalesPila());
        nuevanovedad.setDestino("ACTIVOS");
        duplicarnovedad = new NovedadesAutoLiquidaciones();
        duplicarnovedad.setTipoentidad(new TiposEntidades());
        duplicarnovedad.setTercero(new Terceros());
        duplicarnovedad.setEmpresa(new Empresas());
        duplicarnovedad.setSucursalpila(new SucursalesPila());
        duplicarnovedad.setDestino("ACTIVOS");
        editarnovedad = new NovedadesAutoLiquidaciones();
        altotabla = 250;
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        permitirIndex = true;
        activarlov = true;
    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarNovedadAutoLiquidaciones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct ControlNovedadAutoLiquidacion: " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }
//
//    public void recibirParametros(Short anio, Short mes, BigInteger secuenciaEmpresa) {
//        anioParametro = BigInteger.valueOf(anio);
//        mesParametro = BigInteger.valueOf(mes);
//        secuenciaParametro = secuenciaEmpresa;
//        System.out.println("valor del año: " + anioParametro);
//        System.out.println("valor del mes : " + mesParametro);
//        System.out.println("secuencia de la empresa :" + secuenciaEmpresa);
//        listaNovedades = null;
//        getListaNovedades();
//        contarRegistros();
//
//    }

    public void recibirPag(String pag, Short anio, Short mes, BigInteger secuenciaEmpresa) {
        paginaAnterior = pag;
        listaEmpresas = null;
        getListaEmpresas();
        nuevanovedad.setAnio(anioParametro);
        nuevanovedad.setMes(mesParametro);
        if (listaNovedades != null) {
            if (!listaNovedades.isEmpty()) {
                novedadSeleccionada = listaNovedades.get(0);
            }
        }

        anioParametro = BigInteger.valueOf(anio);
        mesParametro = BigInteger.valueOf(mes);
        secuenciaParametro = secuenciaEmpresa;
        System.out.println("valor del año: " + anioParametro);
        System.out.println("valor del mes : " + mesParametro);
        System.out.println("secuencia de la empresa :" + secuenciaEmpresa);
        listaNovedades = null;
        getListaNovedades();
    }

    public String volverPagAnterior() {
        return paginaAnterior;
    }

    public void cambiarIndice(NovedadesAutoLiquidaciones novedad, int celda) {
        if (permitirIndex == true) {
            novedadSeleccionada = novedad;
            cualCelda = celda;
            novedadSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                habilitarBotonLov();
                if (novedadSeleccionada.getEmpresa() == null) {
                    novedadSeleccionada.setEmpresa(new Empresas());
                } else {
                    novedadSeleccionada.getEmpresa();
                }
            } else if (cualCelda == 1) {
                habilitarBotonLov();
                if (novedadSeleccionada.getSucursalpila() == null) {
                    novedadSeleccionada.setSucursalpila(new SucursalesPila());
                } else {
                    novedadSeleccionada.getSucursalpila();
                }
            } else if (cualCelda == 2) {
                deshabilitarBotonLov();
                novedadSeleccionada.getAnio();
            } else if (cualCelda == 3) {
                deshabilitarBotonLov();
                novedadSeleccionada.getMes();
            } else if (cualCelda == 4) {
                habilitarBotonLov();
                if (novedadSeleccionada.getTipoentidad() == null) {
                    novedadSeleccionada.setTipoentidad(new TiposEntidades());
                } else {
                    novedadSeleccionada.getTipoentidad();
                }
            } else if (cualCelda == 5) {
                deshabilitarBotonLov();
                novedadSeleccionada.getFormulariounico();
            } else if (cualCelda == 6) {
                deshabilitarBotonLov();
                novedadSeleccionada.getNumeroradicacion();
            } else if (cualCelda == 7) {
                deshabilitarBotonLov();
                novedadSeleccionada.getCorreccion();
            } else if (cualCelda == 8) {
                deshabilitarBotonLov();
                novedadSeleccionada.getAniomes();
            } else if (cualCelda == 9) {
                deshabilitarBotonLov();
                novedadSeleccionada.getPlanillacorregir();
            } else if (cualCelda == 10) {
                deshabilitarBotonLov();
                novedadSeleccionada.getDiasmora();
            } else if (cualCelda == 11) {
                deshabilitarBotonLov();
                novedadSeleccionada.getValorinteresesmora();
            } else if (cualCelda == 12) {
                deshabilitarBotonLov();
                novedadSeleccionada.getRadicaciondescuento();
            } else if (cualCelda == 13) {
                habilitarBotonLov();
                novedadSeleccionada.getTercero().getNit();
            } else if (cualCelda == 14) {
                deshabilitarBotonLov();
                novedadSeleccionada.getTercero().getNombre();
            } else if (cualCelda == 15) {
                deshabilitarBotonLov();
                novedadSeleccionada.getSaldoafavor();
            } else if (cualCelda == 16) {
                deshabilitarBotonLov();
                novedadSeleccionada.getValormoraupc();
            } else if (cualCelda == 17) {
                deshabilitarBotonLov();
                novedadSeleccionada.getSaldoafavorupc();
            } else if (cualCelda == 18) {
                deshabilitarBotonLov();
                novedadSeleccionada.getValormorasolidaridad();
            } else if (cualCelda == 19) {
                deshabilitarBotonLov();
                novedadSeleccionada.getValormorasubsistencia();
            } else if (cualCelda == 20) {
                deshabilitarBotonLov();
                novedadSeleccionada.getValorotros();
            } else if (cualCelda == 21) {
                deshabilitarBotonLov();
                novedadSeleccionada.getDestino();
            }
        }
    }

    public void listaValoresBoton() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
                RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesDialogo");
                RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 13) {
                RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            editarnovedad = novedadSeleccionada;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarEmpresa");
                RequestContext.getCurrentInstance().execute("PF('editarEmpresa').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSucursal");
                RequestContext.getCurrentInstance().execute("PF('editarSucursal').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarAnio");
                RequestContext.getCurrentInstance().execute("PF('editarAnio').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarMes");
                RequestContext.getCurrentInstance().execute("PF('editarMes').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoEntidad");
                RequestContext.getCurrentInstance().execute("PF('editarTipoEntidad').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFormularioUnico");
                RequestContext.getCurrentInstance().execute("PF('editarFormularioUnico').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNumRadicado");
                RequestContext.getCurrentInstance().execute("PF('editarNumRadicado').show()");
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarCorrecciones");
                RequestContext.getCurrentInstance().execute("PF('editarCorrecciones').show()");
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarAnioMes");
                RequestContext.getCurrentInstance().execute("PF('editarAnioMes').show()");
            } else if (cualCelda == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarPlanilla");
                RequestContext.getCurrentInstance().execute("PF('editarPlanilla').show()");
            } else if (cualCelda == 10) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDiasM");
                RequestContext.getCurrentInstance().execute("PF('editarDiasM').show()");
            } else if (cualCelda == 11) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarIntMora");
                RequestContext.getCurrentInstance().execute("PF('editarIntMora').show()");
            } else if (cualCelda == 12) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarRadicaciondescuento");
                RequestContext.getCurrentInstance().execute("PF('editarRadicaciondescuento').show()");
            } else if (cualCelda == 13) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNitTercero");
                RequestContext.getCurrentInstance().execute("PF('editarNitTercero').show()");
            } else if (cualCelda == 14) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreTercero");
                RequestContext.getCurrentInstance().execute("PF('editarNombreTercero').show()");
            } else if (cualCelda == 15) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSaldoFavor");
                RequestContext.getCurrentInstance().execute("PF('editarSaldoFavor').show()");
            } else if (cualCelda == 16) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorMoraUpc");
                RequestContext.getCurrentInstance().execute("PF('editarValorMoraUpc').show()");
            } else if (cualCelda == 17) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSaldoFavorUpc");
                RequestContext.getCurrentInstance().execute("PF('editarSaldoFavorUpc').show()");
            } else if (cualCelda == 18) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorMoraSolidaridad");
                RequestContext.getCurrentInstance().execute("PF('editarValorMoraSolidaridad').show()");
            } else if (cualCelda == 19) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorMoraSubsistencia");
                RequestContext.getCurrentInstance().execute("PF('editarValorMoraSubsistencia').show()");
            } else if (cualCelda == 20) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorOtros");
                RequestContext.getCurrentInstance().execute("PF('editarValorOtros').show()");
            } else if (cualCelda == 21) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDestino");
                RequestContext.getCurrentInstance().execute("PF('editarDestino').show()");
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void limpiarNuevaNovedad() {
        nuevanovedad = new NovedadesAutoLiquidaciones();
        nuevanovedad.setAnio(anioParametro);
        nuevanovedad.setMes(mesParametro);
        nuevanovedad.setTipoentidad(new TiposEntidades());
        nuevanovedad.setTercero(new Terceros());
        nuevanovedad.setEmpresa(new Empresas());
        nuevanovedad.setSucursalpila(new SucursalesPila());
        nuevanovedad.setDestino("ACTIVOS");
    }

    public void limpiarDuplicarNovedad() {
        duplicarnovedad = new NovedadesAutoLiquidaciones();
        duplicarnovedad.setAnio(anioParametro);
        duplicarnovedad.setMes(mesParametro);
        duplicarnovedad.setTipoentidad(new TiposEntidades());
        duplicarnovedad.setTercero(new Terceros());
        duplicarnovedad.setEmpresa(new Empresas());
        duplicarnovedad.setSucursalpila(new SucursalesPila());
        duplicarnovedad.setDestino("ACTIVOS");
    }

    public void asignarIndex(NovedadesAutoLiquidaciones novedad, int dlg, int LND) {
        novedadSeleccionada = novedad;
        RequestContext context = RequestContext.getCurrentInstance();
        tipoActualizacion = LND;
        if (dlg == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
        } else if (dlg == 1) {
            listaSucursales = null;
            getListaSucursales();
            RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
        } else if (dlg == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
        } else if (dlg == 13) {
            RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
            RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
        }
    }

    public void seleccionarCorreccion(String correccion, int tipoNuevo) {
        if (tipoNuevo == 1) {
            if (correccion.equals("NO")) {
                nuevanovedad.setCorreccion("N");
            } else if (correccion.equals("SI")) {
                nuevanovedad.setCorreccion("S");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevacorreccion");
        } else {
            if (correccion.equals("NO")) {
                duplicarnovedad.setCorreccion("N");
            } else if (correccion.equals("SI")) {
                duplicarnovedad.setCorreccion("S");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarcorreccion");
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDFTablasAnchas();
        exporter.export(context, tabla, "NovedadAutoLiquidacionPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "NovedadAutoLiquidacionXLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public String exportXMLTabla() {
        String tabla = "";
        if (novedadSeleccionada != null) {
            tabla = ":formExportar:datosNovedadesExportar";
        }

        return tabla;
    }

    public String exportXMLNombreArchivo() {
        String nombre = "";
        if (novedadSeleccionada != null) {
            nombre = "ParametrosAutoliquidacion_XML";
        }

        return nombre;
    }

    public void validarExportPDF() throws IOException {
        if (novedadSeleccionada != null) {
            exportPDF();
        }
    }

    public void guardarCambios() {
        try {
            if (guardado == false) {
                if (!listaNovedadesBorrar.isEmpty()) {
                    for (int i = 0; i < listaNovedadesBorrar.size(); i++) {
                        administrarNovedadAutoLiquidaciones.borrarNovedades(listaNovedadesBorrar.get(i));
                    }
                    listaNovedadesBorrar.clear();
                }
                if (!listaNovedadesCrear.isEmpty()) {
                    for (int i = 0; i < listaNovedadesCrear.size(); i++) {
                        administrarNovedadAutoLiquidaciones.crearNovedades(listaNovedadesCrear.get(i));
                    }
                    listaNovedadesCrear.clear();
                }
                if (!listaNovedadesModificar.isEmpty()) {
                    for (int i = 0; i < listaNovedadesModificar.size(); i++) {
                        administrarNovedadAutoLiquidaciones.editarNovedades(listaNovedadesModificar.get(i));
                    }
                    listaNovedadesModificar.clear();
                }

                listaNovedades = null;
                getListaNovedades();
//                if (listaNovedades != null) {
                contarRegistros();
//                }
                guardado = true;
                permitirIndex = true;
                RequestContext.getCurrentInstance().update("form:novedadesAuto");
//            k = 0;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                System.out.println("guarda datos con éxito");
                novedadSeleccionada = null;
            }
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void borrarNovedades() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (novedadSeleccionada != null) {
            if (!listaNovedadesModificar.isEmpty() && listaNovedadesModificar.contains(novedadSeleccionada)) {
                listaNovedadesModificar.remove(listaNovedadesModificar.indexOf(novedadSeleccionada));
                listaNovedadesBorrar.add(novedadSeleccionada);
            } else if (!listaNovedadesCrear.isEmpty() && listaNovedadesCrear.contains(novedadSeleccionada)) {
                listaNovedadesCrear.remove(listaNovedadesCrear.indexOf(novedadSeleccionada));
                listaNovedadesBorrar.add(novedadSeleccionada);
            } else {
                listaNovedadesBorrar.add(novedadSeleccionada);
            }
            listaNovedades.remove(novedadSeleccionada);

            if (tipoLista == 1) {
                filtrarlistaNovedades.remove(novedadSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:novedadesAuto");
            contarRegistros();
            novedadSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            altotabla = 250;
            empresa = (Column) c.getViewRoot().findComponent("form:novedadesAuto:empresa");
            empresa.setFilterStyle("width: 85% !important");
            sucursal = (Column) c.getViewRoot().findComponent("form:novedadesAuto:sucursal");
            sucursal.setFilterStyle("width: 85% !important");
            anio = (Column) c.getViewRoot().findComponent("form:novedadesAuto:anio");
            anio.setFilterStyle("width: 85% !important");
            mes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:mes");
            mes.setFilterStyle("width: 85% !important");
            tipoEntidad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:tipoentidad");
            tipoEntidad.setFilterStyle("width: 85% !important");
            formularioUnico = (Column) c.getViewRoot().findComponent("form:novedadesAuto:formunico");
            formularioUnico.setFilterStyle("width: 85% !important");
            numRadicado = (Column) c.getViewRoot().findComponent("form:novedadesAuto:numradicado");
            numRadicado.setFilterStyle("width: 85% !important");
            correccion = (Column) c.getViewRoot().findComponent("form:novedadesAuto:correccion");
            correccion.setFilterStyle("width: 85% !important");
            anioMes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:aniomes");
            anioMes.setFilterStyle("width: 85% !important");
            planillaCorregir = (Column) c.getViewRoot().findComponent("form:novedadesAuto:planillacorregir");
            planillaCorregir.setFilterStyle("width: 85% !important");
            dias = (Column) c.getViewRoot().findComponent("form:novedadesAuto:diasmora");
            dias.setFilterStyle("width: 85% !important");
            intMora = (Column) c.getViewRoot().findComponent("form:novedadesAuto:intmora");
            intMora.setFilterStyle("width: 85% !important");
            radicacionDcto = (Column) c.getViewRoot().findComponent("form:novedadesAuto:radicaciondcto");
            radicacionDcto.setFilterStyle("width: 85% !important");
            nit = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nit");
            nit.setFilterStyle("width: 85% !important");
            nombreTercero = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nombretercero");
            nombreTercero.setFilterStyle("width: 85% !important");
            saldoFavor = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavor");
            saldoFavor.setFilterStyle("width: 85% !important");
            vlrMoraUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmora");
            vlrMoraUPC.setFilterStyle("width: 85% !important");
            saldofavorUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavorupc");
            saldofavorUPC.setFilterStyle("width: 85% !important");
            vlrMoraSolidaridad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlomorasoli");
            vlrMoraSolidaridad.setFilterStyle("width: 85% !important");
            vlrSusbsistencia = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmorasubs");
            vlrSusbsistencia.setFilterStyle("width: 85% !important");
            vlrOtros = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrotros");
            vlrOtros.setFilterStyle("width: 85% !important");
            destino = (Column) c.getViewRoot().findComponent("form:novedadesAuto:destino");
            destino.setFilterStyle("width: 85% !important");

            RequestContext.getCurrentInstance().update("form:novedadesAuto");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altotabla = 225;
            empresa = (Column) c.getViewRoot().findComponent("form:novedadesAuto:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            sucursal = (Column) c.getViewRoot().findComponent("form:novedadesAuto:sucursal");
            sucursal.setFilterStyle("display: none; visibility: hidden;");
            anio = (Column) c.getViewRoot().findComponent("form:novedadesAuto:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            tipoEntidad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:tipoentidad");
            tipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            formularioUnico = (Column) c.getViewRoot().findComponent("form:novedadesAuto:formunico");
            formularioUnico.setFilterStyle("display: none; visibility: hidden;");
            numRadicado = (Column) c.getViewRoot().findComponent("form:novedadesAuto:numradicado");
            numRadicado.setFilterStyle("display: none; visibility: hidden;");
            correccion = (Column) c.getViewRoot().findComponent("form:novedadesAuto:correccion");
            correccion.setFilterStyle("display: none; visibility: hidden;");
            anioMes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:aniomes");
            anioMes.setFilterStyle("display: none; visibility: hidden;");
            planillaCorregir = (Column) c.getViewRoot().findComponent("form:novedadesAuto:planillacorregir");
            planillaCorregir.setFilterStyle("display: none; visibility: hidden;");
            dias = (Column) c.getViewRoot().findComponent("form:novedadesAuto:diasmora");
            dias.setFilterStyle("display: none; visibility: hidden;");
            intMora = (Column) c.getViewRoot().findComponent("form:novedadesAuto:intmora");
            intMora.setFilterStyle("display: none; visibility: hidden;");
            radicacionDcto = (Column) c.getViewRoot().findComponent("form:novedadesAuto:radicaciondcto");
            radicacionDcto.setFilterStyle("display: none; visibility: hidden;");
            nit = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nit");
            nit.setFilterStyle("display: none; visibility: hidden;");
            nombreTercero = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nombretercero");
            nombreTercero.setFilterStyle("display: none; visibility: hidden;");
            saldoFavor = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavor");
            saldoFavor.setFilterStyle("display: none; visibility: hidden;");
            vlrMoraUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmora");
            vlrMoraUPC.setFilterStyle("display: none; visibility: hidden;");
            saldofavorUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavorupc");
            saldofavorUPC.setFilterStyle("display: none; visibility: hidden;");
            vlrMoraSolidaridad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlomorasoli");
            vlrMoraSolidaridad.setFilterStyle("display: none; visibility: hidden;");
            vlrSusbsistencia = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmorasubs");
            vlrSusbsistencia.setFilterStyle("display: none; visibility: hidden;");
            vlrOtros = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrotros");
            vlrOtros.setFilterStyle("display: none; visibility: hidden;");
            destino = (Column) c.getViewRoot().findComponent("form:novedadesAuto:destino");
            destino.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:novedadesAuto");
            bandera = 0;
            tipoLista = 0;
        }
    }

    public void agregarNuevaNovedadAuto() {
        int pasa = 0;
        mensajeValidacion = "";
        RequestContext context = RequestContext.getCurrentInstance();
        FacesContext c = FacesContext.getCurrentInstance();
//        novedadSeleccionada = null;
        if (nuevanovedad.getAnio() == null) {
            mensajeValidacion = mensajeValidacion + " * Año \n";
            pasa++;
        }
        if (nuevanovedad.getMes() == null) {
            mensajeValidacion = mensajeValidacion + " * Mes \n";
            pasa++;
        }
        if (nuevanovedad.getTipoentidad().getNombre() == null) {
            mensajeValidacion = mensajeValidacion + "   * Tipo de Entidad \n";
            pasa++;
        }
        if (nuevanovedad.getDestino() == null) {
            mensajeValidacion = mensajeValidacion + " *Destino";
            pasa++;
        }

        if (pasa == 0) {
            if (bandera == 1) {
                altotabla = 225;
                empresa = (Column) c.getViewRoot().findComponent("form:novedadesAuto:empresa");
                empresa.setFilterStyle("display: none; visibility: hidden;");
                sucursal = (Column) c.getViewRoot().findComponent("form:novedadesAuto:sucursal");
                sucursal.setFilterStyle("display: none; visibility: hidden;");
                anio = (Column) c.getViewRoot().findComponent("form:novedadesAuto:anio");
                anio.setFilterStyle("display: none; visibility: hidden;");
                mes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:mes");
                mes.setFilterStyle("display: none; visibility: hidden;");
                tipoEntidad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:tipoentidad");
                tipoEntidad.setFilterStyle("display: none; visibility: hidden;");
                formularioUnico = (Column) c.getViewRoot().findComponent("form:novedadesAuto:formunico");
                formularioUnico.setFilterStyle("display: none; visibility: hidden;");
                numRadicado = (Column) c.getViewRoot().findComponent("form:novedadesAuto:numradicado");
                numRadicado.setFilterStyle("display: none; visibility: hidden;");
                correccion = (Column) c.getViewRoot().findComponent("form:novedadesAuto:correccion");
                correccion.setFilterStyle("display: none; visibility: hidden;");
                anioMes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:aniomes");
                anioMes.setFilterStyle("display: none; visibility: hidden;");
                planillaCorregir = (Column) c.getViewRoot().findComponent("form:novedadesAuto:planillacorregir");
                planillaCorregir.setFilterStyle("display: none; visibility: hidden;");
                dias = (Column) c.getViewRoot().findComponent("form:novedadesAuto:diasmora");
                dias.setFilterStyle("display: none; visibility: hidden;");
                intMora = (Column) c.getViewRoot().findComponent("form:novedadesAuto:intmora");
                intMora.setFilterStyle("display: none; visibility: hidden;");
                radicacionDcto = (Column) c.getViewRoot().findComponent("form:novedadesAuto:radicaciondcto");
                radicacionDcto.setFilterStyle("display: none; visibility: hidden;");
                nit = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nit");
                nit.setFilterStyle("display: none; visibility: hidden;");
                nombreTercero = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nombretercero");
                nombreTercero.setFilterStyle("display: none; visibility: hidden;");
                saldoFavor = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavor");
                saldoFavor.setFilterStyle("display: none; visibility: hidden;");
                vlrMoraUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmora");
                vlrMoraUPC.setFilterStyle("display: none; visibility: hidden;");
                saldofavorUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavorupc");
                saldofavorUPC.setFilterStyle("display: none; visibility: hidden;");
                vlrMoraSolidaridad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlomorasoli");
                vlrMoraSolidaridad.setFilterStyle("display: none; visibility: hidden;");
                vlrSusbsistencia = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmorasubs");
                vlrSusbsistencia.setFilterStyle("display: none; visibility: hidden;");
                vlrOtros = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrotros");
                vlrOtros.setFilterStyle("display: none; visibility: hidden;");
                destino = (Column) c.getViewRoot().findComponent("form:novedadesAuto:destino");
                destino.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:novedadesAuto");
                bandera = 0;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevanovedad.setSecuencia(l);
            listaNovedadesCrear.add(nuevanovedad);
            if (listaNovedades == null) {
                listaNovedades = new ArrayList<NovedadesAutoLiquidaciones>();
            }
            listaNovedades.add(nuevanovedad);
            novedadSeleccionada = nuevanovedad;
            nuevanovedad = new NovedadesAutoLiquidaciones();
            nuevanovedad.setAnio(anioParametro);
            nuevanovedad.setMes(mesParametro);
            nuevanovedad.setTipoentidad(new TiposEntidades());
            nuevanovedad.setTercero(new Terceros());
            nuevanovedad.setEmpresa(new Empresas());
            nuevanovedad.setSucursalpila(new SucursalesPila());
            nuevanovedad.setDestino("ACTIVOS");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:novedadesAuto");
            RequestContext.getCurrentInstance().execute("PF('nuevaNovedadAuto').hide()");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('form:validarNuevo').show()");
        }

    }

    public void duplicarNuevaNovedadAuto() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            duplicarnovedad = new NovedadesAutoLiquidaciones();

            if (tipoLista == 0) {

                duplicarnovedad.setEmpresa(novedadSeleccionada.getEmpresa());
                duplicarnovedad.setSucursalpila(novedadSeleccionada.getSucursalpila());
                duplicarnovedad.setAnio(novedadSeleccionada.getAnio());
                duplicarnovedad.setMes(novedadSeleccionada.getMes());
                duplicarnovedad.setTipoentidad(novedadSeleccionada.getTipoentidad());
                duplicarnovedad.setFormulariounico(novedadSeleccionada.getFormulariounico());
                duplicarnovedad.setNumeroradicacion(novedadSeleccionada.getNumeroradicacion());
                duplicarnovedad.setCorreccion(novedadSeleccionada.getCorreccion());
                duplicarnovedad.setAniomes(novedadSeleccionada.getAniomes());
                duplicarnovedad.setPlanillacorregir(novedadSeleccionada.getPlanillacorregir());
                duplicarnovedad.setDiasmora(novedadSeleccionada.getDiasmora());
                duplicarnovedad.setValorinteresesmora(novedadSeleccionada.getValorinteresesmora());
                duplicarnovedad.setRadicaciondescuento(novedadSeleccionada.getRadicaciondescuento());
                duplicarnovedad.setTercero(novedadSeleccionada.getTercero());
                duplicarnovedad.setSaldoafavor(novedadSeleccionada.getSaldoafavor());
                duplicarnovedad.setValormoraupc(novedadSeleccionada.getValormoraupc());
                duplicarnovedad.setValormorasolidaridad(novedadSeleccionada.getValormorasolidaridad());
                duplicarnovedad.setValormoraupc(novedadSeleccionada.getValormorasubsistencia());
                duplicarnovedad.setValorotros(novedadSeleccionada.getValorotros());
                duplicarnovedad.setDestino(novedadSeleccionada.getDestino());

            }
            if (tipoLista == 1) {

                duplicarnovedad.setEmpresa(novedadSeleccionada.getEmpresa());
                duplicarnovedad.setSucursalpila(novedadSeleccionada.getSucursalpila());
                duplicarnovedad.setAnio(novedadSeleccionada.getAnio());
                duplicarnovedad.setMes(novedadSeleccionada.getMes());
                duplicarnovedad.setTipoentidad(novedadSeleccionada.getTipoentidad());
                duplicarnovedad.setFormulariounico(novedadSeleccionada.getFormulariounico());
                duplicarnovedad.setNumeroradicacion(novedadSeleccionada.getNumeroradicacion());
                duplicarnovedad.setCorreccion(novedadSeleccionada.getCorreccion());
                duplicarnovedad.setAniomes(novedadSeleccionada.getAniomes());
                duplicarnovedad.setPlanillacorregir(novedadSeleccionada.getPlanillacorregir());
                duplicarnovedad.setDiasmora(novedadSeleccionada.getDiasmora());
                duplicarnovedad.setValorinteresesmora(novedadSeleccionada.getValorinteresesmora());
                duplicarnovedad.setRadicaciondescuento(novedadSeleccionada.getRadicaciondescuento());
                duplicarnovedad.setTercero(novedadSeleccionada.getTercero());
                duplicarnovedad.setSaldoafavor(novedadSeleccionada.getSaldoafavor());
                duplicarnovedad.setValormoraupc(novedadSeleccionada.getValormoraupc());
                duplicarnovedad.setValormorasolidaridad(novedadSeleccionada.getValormorasolidaridad());
                duplicarnovedad.setValormoraupc(novedadSeleccionada.getValormorasubsistencia());
                duplicarnovedad.setValorotros(novedadSeleccionada.getValorotros());
                duplicarnovedad.setDestino(novedadSeleccionada.getDestino());

            }
//            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedades");
            RequestContext.getCurrentInstance().execute("PF('duplicarNovedadAuto').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        FacesContext c = FacesContext.getCurrentInstance();
        k++;
        l = BigInteger.valueOf(k);
        duplicarnovedad.setSecuencia(l);
//        duplicarnovedad.setPersona(empleado.getPersona());
        listaNovedadesCrear.add(duplicarnovedad);
        listaNovedades.add(duplicarnovedad);
        novedadSeleccionada = duplicarnovedad;
        getListaNovedades();
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:novedadesAuto");
        RequestContext.getCurrentInstance().execute("PF('duplicarNovedadAuto').hide()");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        if (bandera == 1) {
            altotabla = 225;
            empresa = (Column) c.getViewRoot().findComponent("form:novedadesAuto:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            sucursal = (Column) c.getViewRoot().findComponent("form:novedadesAuto:sucursal");
            sucursal.setFilterStyle("display: none; visibility: hidden;");
            anio = (Column) c.getViewRoot().findComponent("form:novedadesAuto:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            tipoEntidad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:tipoentidad");
            tipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            formularioUnico = (Column) c.getViewRoot().findComponent("form:novedadesAuto:formunico");
            formularioUnico.setFilterStyle("display: none; visibility: hidden;");
            numRadicado = (Column) c.getViewRoot().findComponent("form:novedadesAuto:numradicado");
            numRadicado.setFilterStyle("display: none; visibility: hidden;");
            correccion = (Column) c.getViewRoot().findComponent("form:novedadesAuto:correccion");
            correccion.setFilterStyle("display: none; visibility: hidden;");
            anioMes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:aniomes");
            anioMes.setFilterStyle("display: none; visibility: hidden;");
            planillaCorregir = (Column) c.getViewRoot().findComponent("form:novedadesAuto:planillacorregir");
            planillaCorregir.setFilterStyle("display: none; visibility: hidden;");
            dias = (Column) c.getViewRoot().findComponent("form:novedadesAuto:diasmora");
            dias.setFilterStyle("display: none; visibility: hidden;");
            intMora = (Column) c.getViewRoot().findComponent("form:novedadesAuto:intmora");
            intMora.setFilterStyle("display: none; visibility: hidden;");
            radicacionDcto = (Column) c.getViewRoot().findComponent("form:novedadesAuto:radicaciondcto");
            radicacionDcto.setFilterStyle("display: none; visibility: hidden;");
            nit = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nit");
            nit.setFilterStyle("display: none; visibility: hidden;");
            nombreTercero = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nombretercero");
            nombreTercero.setFilterStyle("display: none; visibility: hidden;");
            saldoFavor = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavor");
            saldoFavor.setFilterStyle("display: none; visibility: hidden;");
            vlrMoraUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmora");
            vlrMoraUPC.setFilterStyle("display: none; visibility: hidden;");
            saldofavorUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavorupc");
            saldofavorUPC.setFilterStyle("display: none; visibility: hidden;");
            vlrMoraSolidaridad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlomorasoli");
            vlrMoraSolidaridad.setFilterStyle("display: none; visibility: hidden;");
            vlrSusbsistencia = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmorasubs");
            vlrSusbsistencia.setFilterStyle("display: none; visibility: hidden;");
            vlrOtros = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrotros");
            vlrOtros.setFilterStyle("display: none; visibility: hidden;");
            destino = (Column) c.getViewRoot().findComponent("form:novedadesAuto:destino");
            destino.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:novedadesAuto");

            bandera = 0;
            filtrarlistaNovedades = null;
            tipoLista = 0;
        }
        duplicarnovedad = new NovedadesAutoLiquidaciones();
//        deshabilitarBotonLov();
    }

    public void modificarNovedadAuto(NovedadesAutoLiquidaciones novedad) {
        RequestContext context = RequestContext.getCurrentInstance();
        novedadSeleccionada = novedad;
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
        RequestContext.getCurrentInstance().update("form:novedadesAuto");
    }

    public void modificarNovedadAuto(NovedadesAutoLiquidaciones novedad, String confirmarCambio, String valorConfirmar) {
        novedadSeleccionada = novedad;
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            novedadSeleccionada.getTipoentidad().setNombre(nuevanovedad.getEmpresa().getNombre());
            for (int i = 0; i < listaTiposEntidades.size(); i++) {
                if (listaTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setTipoentidad(listaTiposEntidades.get(indiceUnicoElemento));
                listaTiposEntidades.clear();
                getListaTiposEntidades();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
                RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (!valorConfirmar.isEmpty()) {
                novedadSeleccionada.getTercero().setNit(novedadSeleccionada.getTercero().getNit());
                for (int i = 0; i < listaTerceros.size(); i++) {
                    if (listaTerceros.get(i).getStrNit().startsWith(valorConfirmar.toUpperCase())) {
                        indiceUnicoElemento = i;
                        coincidencias++;
                    }
                }
                if (coincidencias == 1) {
                    novedadSeleccionada.setTercero(listaTerceros.get(indiceUnicoElemento));
                    listaTerceros.clear();
                    getListaTerceros();
                } else {
                    permitirIndex = false;
                    RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
                    RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
                    tipoActualizacion = 0;
                }
            } else {
                coincidencias = 1;
                if (tipoLista == 0) {
                    novedadSeleccionada.setTercero(new Terceros());
                } else {
                    novedadSeleccionada.setTercero(new Terceros());
                }
                listaTerceros.clear();
                getListaTerceros();
            }
        }
        if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
            novedadSeleccionada.getEmpresa().setNombre(novedadSeleccionada.getEmpresa().getNombre());
            for (int i = 0; i < listaEmpresas.size(); i++) {
                if (listaEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setEmpresa(listaEmpresas.get(indiceUnicoElemento));
                listaEmpresas.clear();
                getListaEmpresas();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
                RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (confirmarCambio.equalsIgnoreCase("SUCURSAL")) {
            novedadSeleccionada.getSucursalpila().setDescripcion(novedadSeleccionada.getSucursalpila().getDescripcion());
            for (int i = 0; i < listaSucursales.size(); i++) {
                if (listaSucursales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                novedadSeleccionada.setSucursalpila(listaSucursales.get(indiceUnicoElemento));
//                listaSucursales.clear();
//                getListaSucursales();
            } else {
                permitirIndex = false;
                RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesDialogo");
                RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
                tipoActualizacion = 0;
            }
        }
        if (coincidencias == 1) {
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
        RequestContext.getCurrentInstance().update("form:novedadesAuto");
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADESAUTOLIQUIDACION");
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
        } else if (administrarRastros.verificarHistoricosTabla("NOVEDADESAUTOLIQUIDACION")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void actualizarEmpresa() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                novedadSeleccionada.setEmpresa(empresaSeleccionada);
                if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                    if (listaNovedadesModificar.isEmpty()) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    }
                }
            } else {
                novedadSeleccionada.setEmpresa(empresaSeleccionada);
                if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                    if (listaNovedadesModificar.isEmpty()) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:novedadesAuto");
        } else if (tipoActualizacion == 1) {
            nuevanovedad.setEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarnovedad.setEmpresa(empresaSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedades");
        }
        auxiliar = empresaSeleccionada.getSecuencia();
        filtrarlistaEmpresas = null;
        empresaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empresasDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarD");
        RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesDialogo");

    }

    public void cancelarCambioEmpresa() {
        filtrarlistaEmpresas = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        empresaSeleccionada = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovEmpresas').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('empresasDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovEmpresas");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarD");
    }

    public void actualizarTercero() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                novedadSeleccionada.setTercero(terceroSeleccionado);
                if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                    if (listaNovedadesModificar.isEmpty()) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    }
                }
            } else {
                novedadSeleccionada.setTercero(terceroSeleccionado);
                if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                    if (listaNovedadesModificar.isEmpty()) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:novedadesAuto");
        } else if (tipoActualizacion == 1) {
            nuevanovedad.setTercero(terceroSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarnovedad.setTercero(terceroSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedades");
        }
        filtrarlistaTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:lovTerceros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTerceros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTerceros");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");

    }

    public void cancelarCambioTercero() {
        filtrarlistaTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovTerceros:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTerceros').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tercerosDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTerceros");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarT");
    }

    public void actualizarSucursal() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                novedadSeleccionada.setSucursalpila(sucursalSeleccionada);
                if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                    if (listaNovedadesModificar.isEmpty()) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    }
                }
            } else {
                novedadSeleccionada.setSucursalpila(sucursalSeleccionada);
                if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                    if (listaNovedadesModificar.isEmpty()) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:novedadesAuto");
        } else if (tipoActualizacion == 1) {
            nuevanovedad.setSucursalpila(sucursalSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarnovedad.setSucursalpila(sucursalSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedades");
        }
        filtrarlistaSucursales = null;
        sucursalSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:lovSucursales:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSucursales').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovSucursales");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarS");

    }

    public void cancelarCambioSucursal() {
        filtrarlistaSucursales = null;
        sucursalSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        listaSucursales = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovSucursales:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovSucursales').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:sucursalesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovSucursales");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarS");
    }

    public void actualizarTipoEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                novedadSeleccionada.setTipoentidad(tipoEntidadSeleccionada);
                if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                    if (listaNovedadesModificar.isEmpty()) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    }
                }
            } else {
                novedadSeleccionada.setTipoentidad(tipoEntidadSeleccionada);
                if (!listaNovedadesCrear.contains(novedadSeleccionada)) {
                    if (listaNovedadesModificar.isEmpty()) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                        listaNovedadesModificar.add(novedadSeleccionada);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:novedadesAuto");
        } else if (tipoActualizacion == 1) {
            nuevanovedad.setTipoentidad(tipoEntidadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarnovedad.setTipoentidad(tipoEntidadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedades");
        }
        filtrarlistaTiposEntidades = null;
        tipoEntidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:lovTiposEntidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposEntidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTiposEntidades");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTE");

    }

    public void cancelarCambioTipoEntidad() {
        filtrarlistaTiposEntidades = null;
        tipoEntidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovTiposEntidades:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTiposEntidades').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').hide()");
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposEntidadesDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTiposEntidades");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTE");
    }

    public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
        int coincidencias = 0;
        int indiceUnicoElemento = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("EMPRESA")) {
            if (tipoNuevo == 1) {
                nuevanovedad.getEmpresa().setNombre(nuevanovedad.getEmpresa().getNombre());
            } else if (tipoNuevo == 2) {
                duplicarnovedad.getEmpresa().setNombre(duplicarnovedad.getEmpresa().getNombre());
            }
            for (int i = 0; i < listaEmpresas.size(); i++) {
                if (listaEmpresas.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevanovedad.setEmpresa(listaEmpresas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
                } else if (tipoNuevo == 2) {
                    duplicarnovedad.setEmpresa(listaEmpresas.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
                }
                listaEmpresas.clear();
                getListaEmpresas();
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaEmpresa");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEmpresa");
                }
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:empresasDialogo");
            RequestContext.getCurrentInstance().execute("PF('empresasDialogo').show()");
        }
        if (confirmarCambio.equalsIgnoreCase("SUCURSAL")) {
            if (tipoNuevo == 1) {
                nuevanovedad.getSucursalpila().setDescripcion(nuevanovedad.getSucursalpila().getDescripcion());
            } else if (tipoNuevo == 2) {
                duplicarnovedad.getSucursalpila().setDescripcion(nuevanovedad.getSucursalpila().getDescripcion());
            }
            for (int i = 0; i < listaSucursales.size(); i++) {
                if (listaSucursales.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevanovedad.setSucursalpila(listaSucursales.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSucursal");
                } else if (tipoNuevo == 2) {
                    duplicarnovedad.setSucursalpila(listaSucursales.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSucursal");
                }
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevaSucursal");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarSucursal");
                }
            }
            RequestContext.getCurrentInstance().update("formularioDIalogos:sucursalesDialogo");
            RequestContext.getCurrentInstance().execute("PF('sucursalesDialogo').show()");
        }

        if (confirmarCambio.equalsIgnoreCase("TERCERO")) {
            if (tipoNuevo == 1) {
                nuevanovedad.getTercero().setNombre(nuevanovedad.getTercero().getNombre());
            } else if (tipoNuevo == 2) {
                duplicarnovedad.getTercero().setNombre(nuevanovedad.getTercero().getNombre());
            }
            for (int i = 0; i < listaTerceros.size(); i++) {
                if (listaTerceros.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevanovedad.setTercero(listaTerceros.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTercero");
                } else if (tipoNuevo == 2) {
                    duplicarnovedad.setTercero(listaTerceros.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTercero");
                }
                listaTerceros.clear();
                getListaTerceros();
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTercero");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEntidad");
                }
                RequestContext.getCurrentInstance().update("formularioDIalogos:tercerosDialogo");
                RequestContext.getCurrentInstance().execute("PF('tercerosDialogo').show()");
            }
        }

        if (confirmarCambio.equalsIgnoreCase("TIPOENTIDAD")) {
            if (tipoNuevo == 1) {
                nuevanovedad.getTipoentidad().setNombre(nuevanovedad.getTipoentidad().getNombre());
            } else if (tipoNuevo == 2) {
                duplicarnovedad.getTipoentidad().setNombre(nuevanovedad.getTipoentidad().getNombre());
            }
            for (int i = 0; i < listaTiposEntidades.size(); i++) {
                if (listaTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                    indiceUnicoElemento = i;
                    coincidencias++;
                }
            }
            if (coincidencias == 1) {
                if (tipoNuevo == 1) {
                    nuevanovedad.setTipoentidad(listaTiposEntidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoEntidad");
                } else if (tipoNuevo == 2) {
                    duplicarnovedad.setTipoentidad(listaTiposEntidades.get(indiceUnicoElemento));
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEntidad");
                }
                listaTiposEntidades.clear();
                getListaTerceros();
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:nuevoTipoEntidad");
                } else if (tipoNuevo == 2) {
                    RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoEntidad");
                }
            }
            RequestContext.getCurrentInstance().update("formularioDIalogos:tiposEntidadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposEntidadesDialogo').show()");
        }

    }

    public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
        if (Campo.equals("EMPRESA")) {
            if (tipoNuevo == 1) {
                nuevanovedad.getEmpresa().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarnovedad.getEmpresa().getNombre();
            }
        }
        if (Campo.equals("SUCURSAL")) {
            if (tipoNuevo == 1) {
                nuevanovedad.getSucursalpila().getDescripcion();
            } else if (tipoNuevo == 2) {
                duplicarnovedad.getSucursalpila().getDescripcion();
            }
        }
        if (Campo.equals("TIPOENTIDAD")) {
            if (tipoNuevo == 1) {
                nuevanovedad.getTipoentidad().getNombre();
            } else if (tipoNuevo == 2) {
                duplicarnovedad.getTipoentidad().getNombre();
            }
        }
        if (Campo.equals("TERCERO")) {
            if (tipoNuevo == 1) {
                nuevanovedad.getTercero().getNit();
            } else if (tipoNuevo == 2) {
                duplicarnovedad.getTercero().getNit();
            }
        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            altotabla = 225;
            empresa = (Column) c.getViewRoot().findComponent("form:novedadesAuto:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            sucursal = (Column) c.getViewRoot().findComponent("form:novedadesAuto:sucursal");
            sucursal.setFilterStyle("display: none; visibility: hidden;");
            anio = (Column) c.getViewRoot().findComponent("form:novedadesAuto:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            tipoEntidad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:tipoentidad");
            tipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            formularioUnico = (Column) c.getViewRoot().findComponent("form:novedadesAuto:formunico");
            formularioUnico.setFilterStyle("display: none; visibility: hidden;");
            numRadicado = (Column) c.getViewRoot().findComponent("form:novedadesAuto:numradicado");
            numRadicado.setFilterStyle("display: none; visibility: hidden;");
            correccion = (Column) c.getViewRoot().findComponent("form:novedadesAuto:correccion");
            correccion.setFilterStyle("display: none; visibility: hidden;");
            anioMes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:aniomes");
            anioMes.setFilterStyle("display: none; visibility: hidden;");
            planillaCorregir = (Column) c.getViewRoot().findComponent("form:novedadesAuto:planillacorregir");
            planillaCorregir.setFilterStyle("display: none; visibility: hidden;");
            dias = (Column) c.getViewRoot().findComponent("form:novedadesAuto:diasmora");
            dias.setFilterStyle("display: none; visibility: hidden;");
            intMora = (Column) c.getViewRoot().findComponent("form:novedadesAuto:intmora");
            intMora.setFilterStyle("display: none; visibility: hidden;");
            radicacionDcto = (Column) c.getViewRoot().findComponent("form:novedadesAuto:radicaciondcto");
            radicacionDcto.setFilterStyle("display: none; visibility: hidden;");
            nit = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nit");
            nit.setFilterStyle("display: none; visibility: hidden;");
            nombreTercero = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nombretercero");
            nombreTercero.setFilterStyle("display: none; visibility: hidden;");
            saldoFavor = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavor");
            saldoFavor.setFilterStyle("display: none; visibility: hidden;");
            vlrMoraUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmora");
            vlrMoraUPC.setFilterStyle("display: none; visibility: hidden;");
            saldofavorUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavorupc");
            saldofavorUPC.setFilterStyle("display: none; visibility: hidden;");
            vlrMoraSolidaridad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlomorasoli");
            vlrMoraSolidaridad.setFilterStyle("display: none; visibility: hidden;");
            vlrSusbsistencia = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmorasubs");
            vlrSusbsistencia.setFilterStyle("display: none; visibility: hidden;");
            vlrOtros = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrotros");
            vlrOtros.setFilterStyle("display: none; visibility: hidden;");
            destino = (Column) c.getViewRoot().findComponent("form:novedadesAuto:destino");
            destino.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            tipoLista = 0;
        }

        listaNovedadesBorrar.clear();
        listaNovedadesCrear.clear();
        listaNovedadesModificar.clear();
        listaNovedades = null;
        getListaNovedades();
        contarRegistros();
        novedadSeleccionada = null;
        aceptar = true;
        guardado = true;
        tipoLista = 0;
        permitirIndex = true;
        limpiarNuevaNovedad();
        limpiarDuplicarNovedad();
        altotabla = 250;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:novedadesAuto");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void guardarSalir() {
        guardarCambios();
        salir();
    }

    public void cancelarSalir() {
        cancelarModificacion();
        salir();
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            altotabla = 225;
            empresa = (Column) c.getViewRoot().findComponent("form:novedadesAuto:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            sucursal = (Column) c.getViewRoot().findComponent("form:novedadesAuto:sucursal");
            sucursal.setFilterStyle("display: none; visibility: hidden;");
            anio = (Column) c.getViewRoot().findComponent("form:novedadesAuto:anio");
            anio.setFilterStyle("display: none; visibility: hidden;");
            mes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:mes");
            mes.setFilterStyle("display: none; visibility: hidden;");
            tipoEntidad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:tipoentidad");
            tipoEntidad.setFilterStyle("display: none; visibility: hidden;");
            formularioUnico = (Column) c.getViewRoot().findComponent("form:novedadesAuto:formunico");
            formularioUnico.setFilterStyle("display: none; visibility: hidden;");
            numRadicado = (Column) c.getViewRoot().findComponent("form:novedadesAuto:numradicado");
            numRadicado.setFilterStyle("display: none; visibility: hidden;");
            correccion = (Column) c.getViewRoot().findComponent("form:novedadesAuto:correccion");
            correccion.setFilterStyle("display: none; visibility: hidden;");
            anioMes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:aniomes");
            anioMes.setFilterStyle("display: none; visibility: hidden;");
            planillaCorregir = (Column) c.getViewRoot().findComponent("form:novedadesAuto:planillacorregir");
            planillaCorregir.setFilterStyle("display: none; visibility: hidden;");
            dias = (Column) c.getViewRoot().findComponent("form:novedadesAuto:diasmora");
            dias.setFilterStyle("display: none; visibility: hidden;");
            intMora = (Column) c.getViewRoot().findComponent("form:novedadesAuto:intmora");
            intMora.setFilterStyle("display: none; visibility: hidden;");
            radicacionDcto = (Column) c.getViewRoot().findComponent("form:novedadesAuto:radicaciondcto");
            radicacionDcto.setFilterStyle("display: none; visibility: hidden;");
            nit = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nit");
            nit.setFilterStyle("display: none; visibility: hidden;");
            nombreTercero = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nombretercero");
            nombreTercero.setFilterStyle("display: none; visibility: hidden;");
            saldoFavor = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavor");
            saldoFavor.setFilterStyle("display: none; visibility: hidden;");
            vlrMoraUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmora");
            vlrMoraUPC.setFilterStyle("display: none; visibility: hidden;");
            saldofavorUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavorupc");
            saldofavorUPC.setFilterStyle("display: none; visibility: hidden;");
            vlrMoraSolidaridad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlomorasoli");
            vlrMoraSolidaridad.setFilterStyle("display: none; visibility: hidden;");
            vlrSusbsistencia = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmorasubs");
            vlrSusbsistencia.setFilterStyle("display: none; visibility: hidden;");
            vlrOtros = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrotros");
            vlrOtros.setFilterStyle("display: none; visibility: hidden;");
            destino = (Column) c.getViewRoot().findComponent("form:novedadesAuto:destino");
            destino.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            tipoLista = 0;
        }
        listaNovedadesBorrar.clear();
        listaNovedadesCrear.clear();
        listaNovedadesModificar.clear();
        listaNovedades = null;
        novedadSeleccionada = null;
        guardado = true;

    }

    public void posicionOtro() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String celda = map.get("celda"); // name attribute of node 
        String registro = map.get("registro"); // type attribute of node 
        int indice = Integer.parseInt(registro);
        int columna = Integer.parseInt(celda);
        novedadSeleccionada = listaNovedades.get(indice);
        cambiarIndice(novedadSeleccionada, columna);
    }

    public void habilitarBotonLov() {
        activarlov = false;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void deshabilitarBotonLov() {
        activarlov = true;
        RequestContext.getCurrentInstance().update("form:listaValores");
    }

    public void eventoFiltrar() {
        if (tipoLista == 1) {
            tipoLista = 0;
        }
        contarRegistros();

    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistroNovedad");
    }

    public void contarRegistroEmpresas() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresa");
    }

    public void contarRegistroTerceros() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTercero");
    }

    public void contarRegistroSucursales() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroSucursal");
    }

    public void contaroRegistroTipoEntidad() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTipoEntidad");
    }

    public void dispararDialogoNuevoRegistro() {
        nuevanovedad.setAnio(anioParametro);
        nuevanovedad.setMes(mesParametro);
        RequestContext.getCurrentInstance().update("formularioDialogos:nuevaNovedadAuto");
        RequestContext.getCurrentInstance().execute("PF('nuevaNovedadAuto').show()");
    }

    ////////////////SETS Y GETS//////////////////
    public List<NovedadesAutoLiquidaciones> getListaNovedades() {

        if (listaNovedades == null) {
            listaNovedades = administrarNovedadAutoLiquidaciones.listaNovedades(anioParametro, mesParametro, secuenciaParametro);
        }
        return listaNovedades;
    }

    public void setListaNovedades(List<NovedadesAutoLiquidaciones> listaNovedades) {
        this.listaNovedades = listaNovedades;
    }

    public List<NovedadesAutoLiquidaciones> getFiltrarlistaNovedades() {
        return filtrarlistaNovedades;
    }

    public void setFiltrarlistaNovedades(List<NovedadesAutoLiquidaciones> filtrarlistaNovedades) {
        this.filtrarlistaNovedades = filtrarlistaNovedades;
    }

    public NovedadesAutoLiquidaciones getNovedadSeleccionada() {
        return novedadSeleccionada;
    }

    public void setNovedadSeleccionada(NovedadesAutoLiquidaciones novedadSeleccionada) {
        this.novedadSeleccionada = novedadSeleccionada;
    }

    public List<Empresas> getListaEmpresas() {
        if (listaEmpresas == null) {
            listaEmpresas = administrarNovedadAutoLiquidaciones.empresasNovedadAuto();
        }
        return listaEmpresas;
    }

    public void setListaEmpresas(List<Empresas> listaEmpresas) {
        this.listaEmpresas = listaEmpresas;
    }

    public List<Empresas> getFiltrarlistaEmpresas() {
        return filtrarlistaEmpresas;
    }

    public void setFiltrarlistaEmpresas(List<Empresas> filtrarlistaEmpresas) {
        this.filtrarlistaEmpresas = filtrarlistaEmpresas;
    }

    public Empresas getEmpresaSeleccionada() {
        return empresaSeleccionada;
    }

    public void setEmpresaSeleccionada(Empresas empresaSeleccionada) {
        this.empresaSeleccionada = empresaSeleccionada;
    }

    public List<TiposEntidades> getListaTiposEntidades() {
        if (listaTiposEntidades == null) {
            listaTiposEntidades = administrarNovedadAutoLiquidaciones.tiposEntidadesNovedadAuto();
        }
        return listaTiposEntidades;
    }

    public void setListaTiposEntidades(List<TiposEntidades> listaTiposEntidades) {
        this.listaTiposEntidades = listaTiposEntidades;
    }

    public List<TiposEntidades> getFiltrarlistaTiposEntidades() {
        return filtrarlistaTiposEntidades;
    }

    public void setFiltrarlistaTiposEntidades(List<TiposEntidades> filtrarlistaTiposEntidades) {
        this.filtrarlistaTiposEntidades = filtrarlistaTiposEntidades;
    }

    public TiposEntidades getTipoEntidadSeleccionada() {
        return tipoEntidadSeleccionada;
    }

    public void setTipoEntidadSeleccionada(TiposEntidades tipoEntidadSeleccionada) {
        this.tipoEntidadSeleccionada = tipoEntidadSeleccionada;
    }

    public List<Terceros> getListaTerceros() {
        if (listaTerceros == null) {
            listaTerceros = administrarNovedadAutoLiquidaciones.tercerosNovedadAuto();
        }
        return listaTerceros;
    }

    public void setListaTerceros(List<Terceros> listaTerceros) {
        this.listaTerceros = listaTerceros;
    }

    public List<Terceros> getFiltrarlistaTerceros() {
        return filtrarlistaTerceros;
    }

    public void setFiltrarlistaTerceros(List<Terceros> filtrarlistaTerceros) {
        this.filtrarlistaTerceros = filtrarlistaTerceros;
    }

    public Terceros getTerceroSeleccionado() {
        return terceroSeleccionado;
    }

    public void setTerceroSeleccionado(Terceros terceroSeleccionado) {
        this.terceroSeleccionado = terceroSeleccionado;
    }

    public List<SucursalesPila> getListaSucursales() {
        listaSucursales = null;
        if (listaSucursales == null) {
            listaSucursales = administrarNovedadAutoLiquidaciones.sucursalesNovedadAuto(auxiliar);
        }
        return listaSucursales;
    }

    public void setListaSucursales(List<SucursalesPila> listaSucursales) {
        this.listaSucursales = listaSucursales;
    }

    public List<SucursalesPila> getFiltrarlistaSucursales() {
        return filtrarlistaSucursales;
    }

    public void setFiltrarlistaSucursales(List<SucursalesPila> filtrarlistaSucursales) {
        this.filtrarlistaSucursales = filtrarlistaSucursales;
    }

    public SucursalesPila getSucursalSeleccionada() {
        return sucursalSeleccionada;
    }

    public void setSucursalSeleccionada(SucursalesPila sucursalSeleccionada) {
        this.sucursalSeleccionada = sucursalSeleccionada;
    }

    public NovedadesAutoLiquidaciones getNuevanovedad() {
        return nuevanovedad;
    }

    public void setNuevanovedad(NovedadesAutoLiquidaciones nuevanovedad) {
        this.nuevanovedad = nuevanovedad;
    }

    public NovedadesAutoLiquidaciones getEditarnovedad() {
        return editarnovedad;
    }

    public void setEditarnovedad(NovedadesAutoLiquidaciones editarnovedad) {
        this.editarnovedad = editarnovedad;
    }

    public NovedadesAutoLiquidaciones getDuplicarnovedad() {
        return duplicarnovedad;
    }

    public void setDuplicarnovedad(NovedadesAutoLiquidaciones duplicarnovedad) {
        this.duplicarnovedad = duplicarnovedad;
    }

    public String getInfoRegistroNovedades() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:novedadesAuto");
        infoRegistroNovedades = String.valueOf(tabla.getRowCount());
        return infoRegistroNovedades;
    }

    public void setInfoRegistroNovedades(String infoRegistroNovedades) {
        this.infoRegistroNovedades = infoRegistroNovedades;
    }

    public String getInfoRegistroEmpresas() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovEmpresas");
        infoRegistroEmpresas = String.valueOf(tabla.getRowCount());
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
    }

    public String getInfoRegistroTerceros() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTerceros");
        infoRegistroTerceros = String.valueOf(tabla.getRowCount());
        return infoRegistroTerceros;
    }

    public void setInfoRegistroTerceros(String infoRegistroTerceros) {
        this.infoRegistroTerceros = infoRegistroTerceros;
    }

    public String getInfoRegistroSucursales() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovSucursales");
        infoRegistroSucursales = String.valueOf(tabla.getRowCount());
        return infoRegistroSucursales;
    }

    public void setInfoRegistroSucursales(String infoRegistroSucursales) {
        this.infoRegistroSucursales = infoRegistroSucursales;
    }

    public String getInfoRegistroTipoEntidad() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTiposEntidades");
        infoRegistroTipoEntidad = String.valueOf(tabla.getRowCount());
        return infoRegistroTipoEntidad;
    }

    public void setInfoRegistroTipoEntidad(String infoRegistroTipoEntidad) {
        this.infoRegistroTipoEntidad = infoRegistroTipoEntidad;
    }

    public int getAltotabla() {
        return altotabla;
    }

    public void setAltotabla(int altotabla) {
        this.altotabla = altotabla;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public BigInteger getSecuenciaParametro() {
        return secuenciaParametro;
    }

    public void setSecuenciaParametro(BigInteger secuenciaParametro) {
        this.secuenciaParametro = secuenciaParametro;
    }

    public BigInteger getAnioParametro() {
        return anioParametro;
    }

    public void setAnioParametro(BigInteger anioParametro) {
        this.anioParametro = anioParametro;
    }

    public BigInteger getMesParametro() {
        return mesParametro;
    }

    public void setMesParametro(BigInteger mesParametro) {
        this.mesParametro = mesParametro;
    }

    public boolean isActivarlov() {
        return activarlov;
    }

    public void setActivarlov(boolean activarlov) {
        this.activarlov = activarlov;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }
}
