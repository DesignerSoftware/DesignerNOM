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
        altotabla = 260;
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

    public void recibirParametros(Short anio, Short mes, BigInteger secuenciaEmpresa) {
        anioParametro = BigInteger.valueOf(anio);
        mesParametro = BigInteger.valueOf(mes);
        secuenciaParametro = secuenciaEmpresa;
        System.out.println("valor del año: " + anioParametro);
        System.out.println("valor del mes : " + mesParametro);
        System.out.println("secuencia de la empresa :" + secuenciaEmpresa);
        listaNovedades = null;
        getListaNovedades();
        contarRegistros();

    }

    public void recibirPag(String pag) {
        paginaAnterior = pag;
        listaEmpresas = null;
        getListaEmpresas();
        modificarInfoRegistroEmpresas(listaEmpresas.size());
        nuevanovedad.setAnio(anioParametro);
        nuevanovedad.setMes(mesParametro);
        if(listaNovedades != null){
            novedadSeleccionada = listaNovedades.get(0);
        }
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
                } else{
                novedadSeleccionada.getEmpresa();
                }
            } else if (cualCelda == 1) {
                habilitarBotonLov();
                if (novedadSeleccionada.getSucursalpila() == null) {
                    novedadSeleccionada.setSucursalpila(new SucursalesPila());
                } else{
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
                } else{
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
                context.update("formularioDialogos:empresasDialogo");
                context.execute("empresasDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 1) {
                context.update("formularioDialogos:sucursalesDialogo");
                context.execute("sucursalesDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 4) {
                context.update("formularioDialogos:tiposEntidadesDialogo");
                context.execute("tiposEntidadesDialogo.show()");
                tipoActualizacion = 0;
            }
            if (cualCelda == 13) {
                context.update("formularioDialogos:tercerosDialogo");
                context.execute("tercerosDialogo.show()");
                tipoActualizacion = 0;
            }
        }
    }

    public void editarCelda() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            editarnovedad = novedadSeleccionada;
            if (cualCelda == 0) {
                context.update("formularioDialogos:editarEmpresa");
                context.execute("editarEmpresa.show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                context.update("formularioDialogos:editarSucursal");
                context.execute("editarSucursal.show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                context.update("formularioDialogos:editarAnio");
                context.execute("editarAnio.show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                context.update("formularioDialogos:editarMes");
                context.execute("editarMes.show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                context.update("formularioDialogos:editarTipoEntidad");
                context.execute("editarTipoEntidad.show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                context.update("formularioDialogos:editarFormularioUnico");
                context.execute("editarFormularioUnico.show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                context.update("formularioDialogos:editarNumRadicado");
                context.execute("editarNumRadicado.show()");
            } else if (cualCelda == 7) {
                context.update("formularioDialogos:editarCorrecciones");
                context.execute("editarCorrecciones.show()");
            } else if (cualCelda == 8) {
                context.update("formularioDialogos:editarAnioMes");
                context.execute("editarAnioMes.show()");
            } else if (cualCelda == 9) {
                context.update("formularioDialogos:editarPlanilla");
                context.execute("editarPlanilla.show()");
            } else if (cualCelda == 10) {
                context.update("formularioDialogos:editarDiasM");
                context.execute("editarDiasM.show()");
            } else if (cualCelda == 11) {
                context.update("formularioDialogos:editarIntMora");
                context.execute("editarIntMora.show()");
            } else if (cualCelda == 12) {
                context.update("formularioDialogos:editarRadicaciondescuento");
                context.execute("editarRadicaciondescuento.show()");
            } else if (cualCelda == 13) {
                context.update("formularioDialogos:editarNitTercero");
                context.execute("editarNitTercero.show()");
            } else if (cualCelda == 14) {
                context.update("formularioDialogos:editarNombreTercero");
                context.execute("editarNombreTercero.show()");
            } else if (cualCelda == 15) {
                context.update("formularioDialogos:editarSaldoFavor");
                context.execute("editarSaldoFavor.show()");
            } else if (cualCelda == 16) {
                context.update("formularioDialogos:editarValorMoraUpc");
                context.execute("editarValorMoraUpc.show()");
            } else if (cualCelda == 17) {
                context.update("formularioDialogos:editarSaldoFavorUpc");
                context.execute("editarSaldoFavorUpc.show()");
            } else if (cualCelda == 18) {
                context.update("formularioDialogos:editarValorMoraSolidaridad");
                context.execute("editarValorMoraSolidaridad.show()");
            } else if (cualCelda == 19) {
                context.update("formularioDialogos:editarValorMoraSubsistencia");
                context.execute("editarValorMoraSubsistencia.show()");
            } else if (cualCelda == 20) {
                context.update("formularioDialogos:editarValorOtros");
                context.execute("editarValorOtros.show()");
            } else if (cualCelda == 21) {
                context.update("formularioDialogos:editarDestino");
                context.execute("editarDestino.show()");
            }

        } else {
            context.execute("seleccionarRegistro.show()");
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
            context.update("formularioDialogos:empresasDialogo");
            context.execute("empresasDialogo.show()");
        } else if (dlg == 1) {
            listaSucursales = null;
            getListaSucursales();
            context.update("formularioDialogos:sucursalesDialogo");
            context.execute("sucursalesDialogo.show()");
        } else if (dlg == 4) {
            context.update("formularioDialogos:tiposEntidadesDialogo");
            context.execute("tiposEntidadesDialogo.show()");
        } else if (dlg == 13) {
            context.update("formularioDialogos:tercerosDialogo");
            context.execute("tercerosDialogo.show()");
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
                        System.out.println("entra a borrar");
                        administrarNovedadAutoLiquidaciones.borrarNovedades(listaNovedadesBorrar.get(i));
                        System.out.println("sale de borrar");
                    }
                    listaNovedadesBorrar.clear();
                }
                if (!listaNovedadesCrear.isEmpty()) {
                    System.out.println("entra a crear");
                    for (int i = 0; i < listaNovedadesCrear.size(); i++) {
                        administrarNovedadAutoLiquidaciones.crearNovedades(listaNovedadesCrear.get(i));
                    }
                    listaNovedadesCrear.clear();
                    System.out.println("sale de crear");
                }
                if (!listaNovedadesModificar.isEmpty()) {
                    System.out.println("entra a modificar");
                    for (int i = 0; i < listaNovedadesModificar.size(); i++) {
                        administrarNovedadAutoLiquidaciones.editarNovedades(listaNovedadesModificar.get(i));
                    }
                    System.out.println("sale de modificar");
                    listaNovedadesModificar.clear();
                }

                listaNovedades = null;
                getListaNovedades();
//                if (listaNovedades != null) {
                modificarInfoRegistroNovedad(listaNovedades.size());
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
            context.update("form:novedadesAuto");
            novedadSeleccionada = null;

            if (guardado == true) {
                guardado = false;
                context.update("form:ACEPTAR");
            }
        } else {
            context.execute("seleccionarRegistro.show()");
        }
    }

    public void activarCtrlF11() {
        System.out.println("TipoLista= " + tipoLista);
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 0) {
            altotabla = 236;
            empresa = (Column) c.getViewRoot().findComponent("form:novedadesAuto:empresa");
            empresa.setFilterStyle("width: 85%");
            sucursal = (Column) c.getViewRoot().findComponent("form:novedadesAuto:sucursal");
            sucursal.setFilterStyle("width: 85%");
            anio = (Column) c.getViewRoot().findComponent("form:novedadesAuto:anio");
            anio.setFilterStyle("width: 85%");
            mes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:mes");
            mes.setFilterStyle("width: 85%");
            tipoEntidad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:tipoentidad");
            tipoEntidad.setFilterStyle("width: 85%");
            formularioUnico = (Column) c.getViewRoot().findComponent("form:novedadesAuto:formunico");
            formularioUnico.setFilterStyle("width: 85%");
            numRadicado = (Column) c.getViewRoot().findComponent("form:novedadesAuto:numradicado");
            numRadicado.setFilterStyle("width: 85%");
            correccion = (Column) c.getViewRoot().findComponent("form:novedadesAuto:correccion");
            correccion.setFilterStyle("width: 85%");
            anioMes = (Column) c.getViewRoot().findComponent("form:novedadesAuto:aniomes");
            anioMes.setFilterStyle("width: 85%");
            planillaCorregir = (Column) c.getViewRoot().findComponent("form:novedadesAuto:planillacorregir");
            planillaCorregir.setFilterStyle("width: 85%");
            dias = (Column) c.getViewRoot().findComponent("form:novedadesAuto:diasmora");
            dias.setFilterStyle("width: 85%");
            intMora = (Column) c.getViewRoot().findComponent("form:novedadesAuto:intmora");
            intMora.setFilterStyle("width: 85%");
            radicacionDcto = (Column) c.getViewRoot().findComponent("form:novedadesAuto:radicaciondcto");
            radicacionDcto.setFilterStyle("width: 85%");
            nit = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nit");
            nit.setFilterStyle("width: 85%");
            nombreTercero = (Column) c.getViewRoot().findComponent("form:novedadesAuto:nombretercero");
            nombreTercero.setFilterStyle("width: 85%");
            saldoFavor = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavor");
            saldoFavor.setFilterStyle("width: 85%");
            vlrMoraUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmora");
            vlrMoraUPC.setFilterStyle("width: 85%");
            saldofavorUPC = (Column) c.getViewRoot().findComponent("form:novedadesAuto:saldofavorupc");
            saldofavorUPC.setFilterStyle("width: 85%");
            vlrMoraSolidaridad = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlomorasoli");
            vlrMoraSolidaridad.setFilterStyle("width: 85%");
            vlrSusbsistencia = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrmorasubs");
            vlrSusbsistencia.setFilterStyle("width: 85%");
            vlrOtros = (Column) c.getViewRoot().findComponent("form:novedadesAuto:vlrotros");
            vlrOtros.setFilterStyle("width: 85%");
            destino = (Column) c.getViewRoot().findComponent("form:novedadesAuto:destino");
            destino.setFilterStyle("width: 85%");

            RequestContext.getCurrentInstance().update("form:novedadesAuto");
            bandera = 1;
            tipoLista = 1;
        } else if (bandera == 1) {
            altotabla = 260;
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
                altotabla = 236;
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
            modificarInfoRegistroNovedad(listaNovedades.size());
            context.update("form:novedadesAuto");
            context.execute("nuevaNovedadAuto.hide()");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
        } else {
            context.execute("form:validarNuevo.show()");
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
            context.update("formularioDialogos:duplicarNovedades");
            context.execute("duplicarNovedadAuto.show()");
        } else {
            context.execute("formularioDialogos:seleccionarRegistro.show()");
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
        modificarInfoRegistroNovedad(listaNovedades.size());
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:novedadesAuto");
        context.execute("duplicarNovedadAuto.hide()");
        if (guardado == true) {
            guardado = false;
            context.update("form:ACEPTAR");
        }
        if (bandera == 1) {
            altotabla = 260;
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
                    context.update("form:ACEPTAR");
                }
            }
        } else {
            if (!listaNovedadesCrear.contains(novedadSeleccionada)) {

                if (listaNovedadesModificar.isEmpty()) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                } else if (!listaNovedadesModificar.contains(novedadSeleccionada)) {
                    listaNovedadesModificar.add(novedadSeleccionada);
                }
                if (guardado == true) {
                    guardado = false;
                    context.update("form:ACEPTAR");
                }
            }
        }
        context.update("form:novedadesAuto");
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
                context.update("formularioDialogos:tiposEntidadesDialogo");
                context.execute("tiposEntidadesDialogo.show()");
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
                    context.update("formularioDialogos:tercerosDialogo");
                    context.execute("tercerosDialogo.show()");
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
                context.update("formularioDialogos:empresasDialogo");
                context.execute("empresasDialogo.show()");
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
                context.update("formularioDialogos:sucursalesDialogo");
                context.execute("sucursalesDialogo.show()");
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
                context.update("form:ACEPTAR");
            }
        }
        context.update("form:novedadesAuto");
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (novedadSeleccionada != null) {
            int result = administrarRastros.obtenerTabla(novedadSeleccionada.getSecuencia(), "NOVEDADESAUTOLIQUIDACION");
            if (result == 1) {
                context.execute("errorObjetosDB.show()");
            } else if (result == 2) {
                context.execute("confirmarRastro.show()");
            } else if (result == 3) {
                context.execute("errorRegistroRastro.show()");
            } else if (result == 4) {
                context.execute("errorTablaConRastro.show()");
            } else if (result == 5) {
                context.execute("errorTablaSinRastro.show()");
            }
        } else {
            if (administrarRastros.verificarHistoricosTabla("NOVEDADESAUTOLIQUIDACION")) {
                context.execute("confirmarRastroHistorico.show()");
            } else {
                context.execute("errorRastroHistorico.show()");
            }
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
                context.update("form:ACEPTAR");
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            context.update("form:novedadesAuto");
        } else if (tipoActualizacion == 1) {
            nuevanovedad.setEmpresa(empresaSeleccionada);
            context.update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarnovedad.setEmpresa(empresaSeleccionada);
            context.update("formularioDialogos:duplicarNovedad");
        }
        auxiliar = empresaSeleccionada.getSecuencia();
        filtrarlistaEmpresas = null;
        empresaSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        context.execute("lovEmpresas.clearFilters()");
        context.execute("empresasDialogo.hide()");
        context.update("formularioDialogos:empresasDialogo");
        context.update("formularioDialogos:lovEmpresas");
        context.update("formularioDialogos:aceptarD");
        context.update("formularioDialogos:sucursalesDialogo");

    }

    public void cancelarCambioEmpresa() {
        filtrarlistaEmpresas = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        empresaSeleccionada = null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovEmpresas:globalFilter");
        context.execute("lovEmpresas.clearFilters()");
        context.execute("empresasDialogo.hide()");
        context.update("formularioDialogos:empresasDialogo");
        context.update("formularioDialogos:lovEmpresas");
        context.update("formularioDialogos:aceptarD");
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
                context.update("form:ACEPTAR");
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            context.update("form:novedadesAuto");
        } else if (tipoActualizacion == 1) {
            nuevanovedad.setTercero(terceroSeleccionado);
            context.update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarnovedad.setTercero(terceroSeleccionado);
            context.update("formularioDialogos:duplicarNovedad");
        }
        filtrarlistaTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:lovTerceros:globalFilter");
        context.execute("lovTerceros.clearFilters()");
        context.execute("tercerosDialogo.hide()");
        context.update("formularioDialogos:tercerosDialogo");
        context.update("formularioDialogos:lovTerceros");
        context.update("formularioDialogos:aceptarT");

    }

    public void cancelarCambioTercero() {
        filtrarlistaTerceros = null;
        terceroSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovTerceros:globalFilter");
        context.execute("lovTerceros.clearFilters()");
        context.execute("tercerosDialogo.hide()");
        context.update("formularioDialogos:tercerosDialogo");
        context.update("formularioDialogos:lovTerceros");
        context.update("formularioDialogos:aceptarT");
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
                context.update("form:ACEPTAR");
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            context.update("form:novedadesAuto");
        } else if (tipoActualizacion == 1) {
            nuevanovedad.setSucursalpila(sucursalSeleccionada);
            context.update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarnovedad.setSucursalpila(sucursalSeleccionada);
            context.update("formularioDialogos:duplicarNovedad");
        }
        filtrarlistaSucursales = null;
        sucursalSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:lovSucursales:globalFilter");
        context.execute("lovSucursales.clearFilters()");
        context.execute("sucursalesDialogo.hide()");
        context.update("formularioDialogos:sucursalesDialogo");
        context.update("formularioDialogos:lovSucursales");
        context.update("formularioDialogos:aceptarS");

    }

    public void cancelarCambioSucursal() {
        filtrarlistaSucursales = null;
        sucursalSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        listaSucursales= null;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovSucursales:globalFilter");
        context.execute("lovSucursales.clearFilters()");
        context.execute("sucursalesDialogo.hide()");
        context.update("formularioDialogos:sucursalesDialogo");
        context.update("formularioDialogos:lovSucursales");
        context.update("formularioDialogos:aceptarS");
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
                context.update("form:ACEPTAR");
                //RequestContext.getCurrentInstance().update("form:aceptar");
            }
            permitirIndex = true;
            //deshabilitarBotonLov();
            context.update("form:novedadesAuto");
        } else if (tipoActualizacion == 1) {
            nuevanovedad.setTipoentidad(tipoEntidadSeleccionada);
            context.update("formularioDialogos:nuevaNovedad");
        } else if (tipoActualizacion == 2) {
            duplicarnovedad.setTipoentidad(tipoEntidadSeleccionada);
            context.update("formularioDialogos:duplicarNovedad");
        }
        filtrarlistaTiposEntidades = null;
        tipoEntidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        context.reset("formularioDialogos:lovTiposEntidades:globalFilter");
        context.execute("lovTiposEntidades.clearFilters()");
        context.execute("tiposEntidadesDialogo.hide()");
        context.update("formularioDialogos:tiposEntidadesDialogo");
        context.update("formularioDialogos:lovTiposEntidades");
        context.update("formularioDialogos:aceptarTE");

    }

    public void cancelarCambioTipoEntidad() {
        filtrarlistaTiposEntidades = null;
        tipoEntidadSeleccionada = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.reset("formularioDialogos:lovTiposEntidades:globalFilter");
        context.execute("lovTiposEntidades.clearFilters()");
        context.execute("tiposEntidadesDialogo.hide()");
        context.update("formularioDialogos:tiposEntidadesDialogo");
        context.update("formularioDialogos:lovTiposEntidades");
        context.update("formularioDialogos:aceptarTE");
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
                    context.update("formularioDialogos:nuevaEmpresa");
                } else if (tipoNuevo == 2) {
                    duplicarnovedad.setEmpresa(listaEmpresas.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarEmpresa");
                }
                listaEmpresas.clear();
                getListaEmpresas();
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaEmpresa");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarEmpresa");
                }
            }
            context.update("formularioDialogos:empresasDialogo");
            context.execute("empresasDialogo.show()");
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
                    context.update("formularioDialogos:nuevaSucursal");
                } else if (tipoNuevo == 2) {
                    duplicarnovedad.setSucursalpila(listaSucursales.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarSucursal");
                }
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevaSucursal");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarSucursal");
                }
            }
            context.update("formularioDIalogos:sucursalesDialogo");
            context.execute("sucursalesDialogo.show()");
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
                    context.update("formularioDialogos:nuevoTercero");
                } else if (tipoNuevo == 2) {
                    duplicarnovedad.setTercero(listaTerceros.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarTercero");
                }
                listaTerceros.clear();
                getListaTerceros();
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoTercero");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarTipoEntidad");
                }
                context.update("formularioDIalogos:tercerosDialogo");
                context.execute("tercerosDialogo.show()");
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
                    context.update("formularioDialogos:nuevoTipoEntidad");
                } else if (tipoNuevo == 2) {
                    duplicarnovedad.setTipoentidad(listaTiposEntidades.get(indiceUnicoElemento));
                    context.update("formularioDialogos:duplicarTipoEntidad");
                }
                listaTiposEntidades.clear();
                getListaTerceros();
            } else {
                tipoActualizacion = tipoNuevo;
                if (tipoNuevo == 1) {
                    context.update("formularioDialogos:nuevoTipoEntidad");
                } else if (tipoNuevo == 2) {
                    context.update("formularioDialogos:duplicarTipoEntidad");
                }
            }
            context.update("formularioDIalogos:tiposEntidadesDialogo");
            context.execute("tiposEntidadesDialogo.show()");
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
            altotabla = 260;
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
        altotabla = 260;
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:novedadesAuto");
        context.update("form:ACEPTAR");

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
            altotabla = 260;
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
        modificarInfoRegistroNovedad(filtrarlistaNovedades.size());

    }

    public void eventoFiltrarEmpresas() {
        modificarInfoRegistroEmpresas(filtrarlistaEmpresas.size());
    }

    public void eventoFiltrarTerceros() {
        modificarInfoRegistroTerceros(filtrarlistaTerceros.size());
    }

    public void eventoFiltrarSucursales() {
        modificarInfoRegistroSucursales(filtrarlistaSucursales.size());
    }

    public void eventoFiltrarTiposEntidades() {
        modificarInfoRegistroTipoEntidad(filtrarlistaTiposEntidades.size());
    }

    public void contarRegistros() {
        if (listaNovedades != null) {
            modificarInfoRegistroNovedad(listaNovedades.size());
        } else {
            modificarInfoRegistroNovedad(0);
        }
    }

    public void modificarInfoRegistroNovedad(int valor) {
        infoRegistroNovedades = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("form:infoRegistroNovedad");
    }

    public void modificarInfoRegistroEmpresas(int valor) {
        infoRegistroEmpresas = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroEmpresa");
    }

    public void modificarInfoRegistroTerceros(int valor) {
        infoRegistroTerceros = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTercero");
    }

    public void modificarInfoRegistroSucursales(int valor) {
        infoRegistroSucursales = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroSucursal");
    }

    public void modificarInfoRegistroTipoEntidad(int valor) {
        infoRegistroTipoEntidad = String.valueOf(valor);
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTipoEntidad");
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
            modificarInfoRegistroTipoEntidad(listaTiposEntidades.size());
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
            modificarInfoRegistroTerceros(listaTerceros.size());
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
            modificarInfoRegistroSucursales(listaSucursales.size());
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
        return infoRegistroNovedades;
    }

    public void setInfoRegistroNovedades(String infoRegistroNovedades) {
        this.infoRegistroNovedades = infoRegistroNovedades;
    }

    public String getInfoRegistroEmpresas() {
        return infoRegistroEmpresas;
    }

    public void setInfoRegistroEmpresas(String infoRegistroEmpresas) {
        this.infoRegistroEmpresas = infoRegistroEmpresas;
    }

    public String getInfoRegistroTerceros() {
        return infoRegistroTerceros;
    }

    public void setInfoRegistroTerceros(String infoRegistroTerceros) {
        this.infoRegistroTerceros = infoRegistroTerceros;
    }

    public String getInfoRegistroSucursales() {
        return infoRegistroSucursales;
    }

    public void setInfoRegistroSucursales(String infoRegistroSucursales) {
        this.infoRegistroSucursales = infoRegistroSucursales;
    }

    public String getInfoRegistroTipoEntidad() {
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
