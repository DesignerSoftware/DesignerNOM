/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.AportesEntidades;
import Entidades.AportesEntidadesXDia;
import Entidades.Empleados;
import Entidades.Empresas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarAportesEntidadesXDiaInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
@Named(value = "controlAportesEntidadesXDia")
@SessionScoped
public class ControlAportesEntidadesXDia implements Serializable {

    @EJB
    AdministrarRastrosInterface administrarRastros;
    @EJB
    AdministrarAportesEntidadesXDiaInterface administrarAportesEntidades;

    private List<AportesEntidadesXDia> listAportesEntidades;
    private List<AportesEntidadesXDia> listAportesEntidadesFiltrar;
    private List<AportesEntidadesXDia> listAportesEntidadesModificar;
    private List<AportesEntidadesXDia> listAportesEntidadesCrear;
    private List<AportesEntidadesXDia> listAportesEntidadesBorrar;
    private AportesEntidadesXDia aporteSeleccionado;
    private AportesEntidadesXDia editarAporte;
    private AportesEntidadesXDia nuevoAporte;
    private AportesEntidadesXDia duplicarAporte;
    private int cualCelda;
    private int bandera, tipoLista;
    private boolean aceptar, guardado, activarLov;
    private String altoTabla;
    private String infoRegistroAporte;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private AportesEntidades aporteEntidad;
    private Column dia, novedad, tarifa, ibcdiario, aportediario, tarifa2, ibcespecial, aporteentidad;
    private BigDecimal ibcmensual, aportemensual, aux, ibcespecialmensual, aporteentidadmensual;
    private int k;
    private BigInteger l;

    public ControlAportesEntidadesXDia() {
        altoTabla = "245";
        tipoLista = 0;
        cualCelda = -1;
        aceptar = true;
        guardado = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
        activarLov = true;
        listAportesEntidades = null;
        listAportesEntidadesBorrar = new ArrayList<AportesEntidadesXDia>();
        listAportesEntidadesModificar = new ArrayList<AportesEntidadesXDia>();
        listAportesEntidadesCrear = new ArrayList<AportesEntidadesXDia>();
        aporteSeleccionado = null;
        editarAporte = new AportesEntidadesXDia();
        nuevoAporte = new AportesEntidadesXDia();
        duplicarAporte = new AportesEntidadesXDia();
        nuevoAporte = new AportesEntidadesXDia();
        nuevoAporte.setEmpresa(new Empresas());
        nuevoAporte.setEmpleado(new Empleados());
        duplicarAporte = new AportesEntidadesXDia();
        duplicarAporte.setEmpresa(new Empresas());
        duplicarAporte.setEmpleado(new Empleados());
    }

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarAportesEntidades.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
    }

    public void recibirAporteEntidad(String pagina, AportesEntidades aporteEntidadParametro) {
        paginaAnterior = pagina;
        aporteEntidad = aporteEntidadParametro;
        listAportesEntidades = null;
        getListAportesEntidades();
        if (listAportesEntidades != null) {
            if (!listAportesEntidades.isEmpty()) {
                aporteSeleccionado = listAportesEntidades.get(0);
            }
        }
    }

    public void modificarAporteEntidadXDia(AportesEntidadesXDia aporte) {
        aporteSeleccionado = aporte;
        if (listAportesEntidadesModificar.isEmpty()) {
            listAportesEntidadesModificar.add(aporteSeleccionado);
        } else if (!listAportesEntidadesModificar.contains(aporteSeleccionado)) {
            listAportesEntidadesModificar.add(aporteSeleccionado);
        }

        for (int i = 0; i < listAportesEntidadesModificar.size(); i++) {
            if (aporteEntidad.getTipoentidad().getCodigo() == 1) {
                listAportesEntidadesModificar.get(i).setTarifasalud(listAportesEntidadesModificar.get(i).getTarifaaux());
                listAportesEntidadesModificar.get(i).setAportesalud(listAportesEntidadesModificar.get(i).getAporteaux());
                listAportesEntidadesModificar.get(i).setIbcsalud(listAportesEntidadesModificar.get(i).getIbcaux());
                listAportesEntidadesModificar.get(i).setTarifasaludsobrante(listAportesEntidadesModificar.get(i).getTarifa2aux());
                listAportesEntidadesModificar.get(i).setIbcsaludsobrante(listAportesEntidadesModificar.get(i).getIbcespaux());
                listAportesEntidadesModificar.get(i).setAportesaludsobrante(listAportesEntidadesModificar.get(i).getAporteentaux());
            } else if (aporteEntidad.getTipoentidad().getCodigo() == 2) {
                listAportesEntidadesModificar.get(i).setTarifariesgo(listAportesEntidadesModificar.get(i).getTarifaaux());
                listAportesEntidadesModificar.get(i).setAporteriesgo(listAportesEntidadesModificar.get(i).getAporteaux());
                listAportesEntidadesModificar.get(i).setIbcriesgo(listAportesEntidadesModificar.get(i).getIbcaux());
                listAportesEntidadesModificar.get(i).setTarifariesgosobrante(listAportesEntidadesModificar.get(i).getTarifa2aux());
                listAportesEntidadesModificar.get(i).setIbcriesgosobrante(listAportesEntidadesModificar.get(i).getIbcespaux());
                listAportesEntidadesModificar.get(i).setAporteriesgosobrante(listAportesEntidadesModificar.get(i).getAporteentaux());
            } else if (aporteEntidad.getTipoentidad().getCodigo() == 3) {
                listAportesEntidadesModificar.get(i).setTarifapension(listAportesEntidadesModificar.get(i).getTarifaaux());
                listAportesEntidadesModificar.get(i).setAportepension(listAportesEntidadesModificar.get(i).getAporteaux());
                listAportesEntidadesModificar.get(i).setIbcpension(listAportesEntidadesModificar.get(i).getIbcaux());
                listAportesEntidadesModificar.get(i).setTarifapensionsobrante(listAportesEntidadesModificar.get(i).getTarifa2aux());
                listAportesEntidadesModificar.get(i).setIbcpensionsobrante(listAportesEntidadesModificar.get(i).getIbcespaux());
                listAportesEntidadesModificar.get(i).setAportepensionsobrante(listAportesEntidadesModificar.get(i).getAporteentaux());
            } else if (aporteEntidad.getTipoentidad().getCodigo() == 11) {
                listAportesEntidadesModificar.get(i).setTarifasena(listAportesEntidadesModificar.get(i).getTarifaaux());
                listAportesEntidadesModificar.get(i).setAportesena(listAportesEntidadesModificar.get(i).getAporteaux());
                listAportesEntidadesModificar.get(i).setIbcsena(listAportesEntidadesModificar.get(i).getIbcaux());
                listAportesEntidadesModificar.get(i).setTarifasenasobrante(listAportesEntidadesModificar.get(i).getTarifa2aux());
                listAportesEntidadesModificar.get(i).setIbcsenasobrante(listAportesEntidadesModificar.get(i).getIbcespaux());
                listAportesEntidadesModificar.get(i).setAportesenasobrante(listAportesEntidadesModificar.get(i).getAporteentaux());
            } else if (aporteEntidad.getTipoentidad().getCodigo() == 13) {
                listAportesEntidadesModificar.get(i).setTarifaicbf(listAportesEntidadesModificar.get(i).getTarifaaux());
                listAportesEntidadesModificar.get(i).setAporteicbf(listAportesEntidadesModificar.get(i).getAporteaux());
                listAportesEntidadesModificar.get(i).setIbcicbf(listAportesEntidadesModificar.get(i).getIbcaux());
                listAportesEntidadesModificar.get(i).setTarifaicbfsobrante(listAportesEntidadesModificar.get(i).getTarifa2aux());
                listAportesEntidadesModificar.get(i).setIbcicbfsobrante(listAportesEntidadesModificar.get(i).getIbcespaux());
                listAportesEntidadesModificar.get(i).setAporteicbfsobrante(listAportesEntidadesModificar.get(i).getAporteentaux());
            } else if (aporteEntidad.getTipoentidad().getCodigo() == 14) {
                listAportesEntidadesModificar.get(i).setTarifacaja(listAportesEntidadesModificar.get(i).getTarifaaux());
                listAportesEntidadesModificar.get(i).setAportecaja(listAportesEntidadesModificar.get(i).getAporteaux());
                listAportesEntidadesModificar.get(i).setIbccaja(listAportesEntidadesModificar.get(i).getIbcaux());
                listAportesEntidadesModificar.get(i).setTarifacajasobrante(listAportesEntidadesModificar.get(i).getTarifa2aux());
                listAportesEntidadesModificar.get(i).setIbccajasobrante(listAportesEntidadesModificar.get(i).getIbcespaux());
                listAportesEntidadesModificar.get(i).setAportecajasobrante(listAportesEntidadesModificar.get(i).getAporteentaux());
            }
            ibcmensual = ibcmensual.add(listAportesEntidadesModificar.get(i).getIbcaux());
            ibcespecialmensual = ibcespecialmensual.add(listAportesEntidadesModificar.get(i).getIbcespaux());
            aportemensual = aportemensual.add(listAportesEntidadesModificar.get(i).getAporteaux());
            aporteentidadmensual = aporteentidadmensual.add(listAportesEntidadesModificar.get(i).getAporteentaux());
        }
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        RequestContext.getCurrentInstance().update("form:ibcmensual");
        RequestContext.getCurrentInstance().update("form:aportemensual");
        RequestContext.getCurrentInstance().update("form:ibcespecialmensual");
        RequestContext.getCurrentInstance().update("form:aporteentidadmensual");

    }

    public void borrarAporteEntidad() {
        if (aporteSeleccionado != null) {
            if (!listAportesEntidadesModificar.isEmpty() && listAportesEntidadesModificar.contains(aporteSeleccionado)) {
                int modIndex = listAportesEntidadesModificar.indexOf(aporteSeleccionado);
                listAportesEntidadesModificar.remove(modIndex);
                listAportesEntidadesBorrar.add(aporteSeleccionado);
            } else {
                listAportesEntidadesBorrar.add(aporteSeleccionado);
            }
            ibcmensual = ibcmensual.subtract(aporteSeleccionado.getIbcaux());
            ibcespecialmensual = ibcespecialmensual.subtract(aporteSeleccionado.getIbcespaux());
            aportemensual = aportemensual.subtract(aporteSeleccionado.getAporteaux());
            aporteentidadmensual = aporteentidadmensual.subtract(aporteSeleccionado.getAporteentaux());

            listAportesEntidades.remove(aporteSeleccionado);
            if (tipoLista == 1) {
                listAportesEntidadesFiltrar.remove(aporteSeleccionado);
            }
            contarRegistrosAporte();
            RequestContext.getCurrentInstance().update("form:ibcmensual");
            RequestContext.getCurrentInstance().update("form:aportemensual");
            RequestContext.getCurrentInstance().update("form:ibcespecialmensual");
            RequestContext.getCurrentInstance().update("form:aporteentidadmensual");
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            aporteSeleccionado = null;
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
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
            String pagActual = "parametroautoliq";
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

    public void cambiarIndice(AportesEntidadesXDia aporte, int celda) {
        aporteSeleccionado = aporte;
        cualCelda = celda;
        aporteSeleccionado.getSecuencia();
        if (cualCelda == 0) {
            aporteSeleccionado.getDia();
        } else if (cualCelda == 1) {
            aporteSeleccionado.getNovedad();
        } else if (cualCelda == 2) {
            aporteSeleccionado.getTarifaaux();
        } else if (cualCelda == 3) {
            aporteSeleccionado.getIbcaux();
        } else if (cualCelda == 4) {
            aporteSeleccionado.getAporteaux();
        } else if (cualCelda == 5) {
            aporteSeleccionado.getTarifa2aux();
        } else if (cualCelda == 6) {
            aporteSeleccionado.getIbcespaux();
        } else if (cualCelda == 7) {
            aporteSeleccionado.getAporteentaux();
        }
    }

    public void guardarYSalir() {
        guardarCambios();
        salir();
    }

    public void cancelarYSalir() {
        cancelarModificacion();
        salir();
    }

    public void agregarNuevoAporteEntidad() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 1) {
            altoTabla = "245";
            dia = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:dia");
            dia.setFilterStyle("display: none; visibility: hidden;");
            novedad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:novedad");
            novedad.setFilterStyle("display: none; visibility: hidden;");
            tarifa = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:tarifa");
            tarifa.setFilterStyle("display: none; visibility: hidden;");
            ibcdiario = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:ibcdiario");
            ibcdiario.setFilterStyle("display: none; visibility: hidden;");
            aportediario = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aportediario");
            aportediario.setFilterStyle("display: none; visibility: hidden;");
            tarifa2 = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:tarifa2");
            tarifa2.setFilterStyle("display: none; visibility: hidden;");
            ibcespecial = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:ibcespecial");
            ibcespecial.setFilterStyle("display: none; visibility: hidden;");
            aporteentidad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteentidad");
            aporteentidad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            bandera = 0;
            listAportesEntidadesFiltrar = null;
            tipoLista = 0;
        }
        k++;
        l = BigInteger.valueOf(k);
        nuevoAporte.setSecuencia(l);
        nuevoAporte.setEmpresa(aporteEntidad.getEmpresa());
        nuevoAporte.setEmpleado(aporteEntidad.getEmpleado());
        if (aporteEntidad.getTipoentidad().getCodigo() == 1) {
            nuevoAporte.setTarifasalud(nuevoAporte.getTarifaaux());
            nuevoAporte.setAportesalud(nuevoAporte.getAporteaux());
            nuevoAporte.setIbcsalud(nuevoAporte.getIbcaux());
            nuevoAporte.setTarifasaludsobrante(nuevoAporte.getTarifa2aux());
            nuevoAporte.setIbcsaludsobrante(nuevoAporte.getIbcespaux());
            nuevoAporte.setAportesaludsobrante(nuevoAporte.getAporteentaux());
        } else if (aporteEntidad.getTipoentidad().getCodigo() == 2) {
            nuevoAporte.setTarifariesgo(nuevoAporte.getTarifaaux());
            nuevoAporte.setAporteriesgo(nuevoAporte.getAporteaux());
            nuevoAporte.setIbcriesgo(nuevoAporte.getIbcaux());
            nuevoAporte.setTarifariesgosobrante(nuevoAporte.getTarifa2aux());
            nuevoAporte.setIbcriesgosobrante(nuevoAporte.getIbcespaux());
            nuevoAporte.setAporteriesgosobrante(nuevoAporte.getAporteentaux());
        } else if (aporteEntidad.getTipoentidad().getCodigo() == 3) {
            nuevoAporte.setTarifapension(nuevoAporte.getTarifaaux());
            nuevoAporte.setAportepension(nuevoAporte.getAporteaux());
            nuevoAporte.setIbcpension(nuevoAporte.getIbcaux());
            nuevoAporte.setTarifapensionsobrante(nuevoAporte.getTarifa2aux());
            nuevoAporte.setIbcpensionsobrante(nuevoAporte.getIbcespaux());
            nuevoAporte.setAportepensionsobrante(nuevoAporte.getAporteentaux());
        } else if (aporteEntidad.getTipoentidad().getCodigo() == 11) {
            nuevoAporte.setTarifasena(nuevoAporte.getTarifaaux());
            nuevoAporte.setAportesena(nuevoAporte.getAporteaux());
            nuevoAporte.setIbcsena(nuevoAporte.getIbcaux());
            nuevoAporte.setTarifasenasobrante(nuevoAporte.getTarifa2aux());
            nuevoAporte.setIbcsenasobrante(nuevoAporte.getIbcespaux());
            nuevoAporte.setAportesenasobrante(nuevoAporte.getAporteentaux());
        } else if (aporteEntidad.getTipoentidad().getCodigo() == 13) {
            nuevoAporte.setTarifaicbf(nuevoAporte.getTarifaaux());
            nuevoAporte.setAporteicbf(nuevoAporte.getAporteaux());
            nuevoAporte.setIbcicbf(nuevoAporte.getIbcaux());
            nuevoAporte.setTarifaicbfsobrante(nuevoAporte.getTarifa2aux());
            nuevoAporte.setIbcicbfsobrante(nuevoAporte.getIbcespaux());
            nuevoAporte.setAporteicbfsobrante(nuevoAporte.getAporteentaux());
        } else if (aporteEntidad.getTipoentidad().getCodigo() == 14) {
            nuevoAporte.setTarifacaja(nuevoAporte.getTarifaaux());
            nuevoAporte.setAportecaja(nuevoAporte.getAporteaux());
            nuevoAporte.setIbccaja(nuevoAporte.getIbcaux());
            nuevoAporte.setTarifacajasobrante(nuevoAporte.getTarifa2aux());
            nuevoAporte.setIbccajasobrante(nuevoAporte.getIbcespaux());
            nuevoAporte.setAportecajasobrante(nuevoAporte.getAporteentaux());
        }
        listAportesEntidadesCrear.add(nuevoAporte);
        listAportesEntidades.add(nuevoAporte);
        aporteSeleccionado = nuevoAporte;
        nuevoAporte = new AportesEntidadesXDia();
        nuevoAporte.setEmpresa(new Empresas());
        nuevoAporte.setEmpleado(new Empleados());
        contarRegistrosAporte();
        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        RequestContext.getCurrentInstance().execute("PF('nuevoAporte').hide()");
        guardado = false;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void limpiarNuevoAporte() {
        nuevoAporte = new AportesEntidadesXDia();
        nuevoAporte.setEmpresa(new Empresas());
        nuevoAporte.setEmpleado(new Empleados());
    }

    public void duplicarAporteEntidad() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (aporteSeleccionado != null) {
            duplicarAporte = new AportesEntidadesXDia();

            duplicarAporte.setAno(aporteSeleccionado.getAno());
            duplicarAporte.setMes(aporteSeleccionado.getMes());
            duplicarAporte.setEmpresa(aporteSeleccionado.getEmpresa());
            duplicarAporte.setEmpleado(aporteSeleccionado.getEmpleado());
            duplicarAporte.setDia(aporteSeleccionado.getDia());
            duplicarAporte.setNovedad(aporteSeleccionado.getNovedad());
            if (aporteEntidad.getTipoentidad().getCodigo() == 1) {
                duplicarAporte.setTarifasalud(aporteSeleccionado.getTarifaaux());
                duplicarAporte.setAportesalud(aporteSeleccionado.getAporteaux());
                duplicarAporte.setIbcsalud(aporteSeleccionado.getIbcaux());
                duplicarAporte.setTarifasaludsobrante(aporteSeleccionado.getTarifa2aux());
                duplicarAporte.setIbcsaludsobrante(aporteSeleccionado.getIbcespaux());
                duplicarAporte.setAportesaludsobrante(aporteSeleccionado.getAporteentaux());
            } else if (aporteEntidad.getTipoentidad().getCodigo() == 2) {
                duplicarAporte.setTarifariesgo(aporteSeleccionado.getTarifaaux());
                duplicarAporte.setAporteriesgo(aporteSeleccionado.getAporteaux());
                duplicarAporte.setIbcriesgo(aporteSeleccionado.getIbcaux());
                duplicarAporte.setTarifariesgosobrante(aporteSeleccionado.getTarifa2aux());
                duplicarAporte.setIbcriesgosobrante(aporteSeleccionado.getIbcespaux());
                duplicarAporte.setAporteriesgosobrante(aporteSeleccionado.getAporteentaux());
            } else if (aporteEntidad.getTipoentidad().getCodigo() == 3) {
                duplicarAporte.setTarifapension(aporteSeleccionado.getTarifaaux());
                duplicarAporte.setAportepension(aporteSeleccionado.getAporteaux());
                duplicarAporte.setIbcpension(aporteSeleccionado.getIbcaux());
                duplicarAporte.setTarifapensionsobrante(aporteSeleccionado.getTarifa2aux());
                duplicarAporte.setIbcpensionsobrante(aporteSeleccionado.getIbcespaux());
                duplicarAporte.setAportepensionsobrante(aporteSeleccionado.getAporteentaux());
            } else if (aporteEntidad.getTipoentidad().getCodigo() == 11) {
                duplicarAporte.setTarifasena(aporteSeleccionado.getTarifaaux());
                duplicarAporte.setAportesena(aporteSeleccionado.getAporteaux());
                duplicarAporte.setIbcsena(aporteSeleccionado.getIbcaux());
                duplicarAporte.setTarifasenasobrante(aporteSeleccionado.getTarifa2aux());
                duplicarAporte.setIbcsenasobrante(aporteSeleccionado.getIbcespaux());
                duplicarAporte.setAportesenasobrante(aporteSeleccionado.getAporteentaux());
            } else if (aporteEntidad.getTipoentidad().getCodigo() == 13) {
                duplicarAporte.setTarifaicbf(aporteSeleccionado.getTarifaaux());
                duplicarAporte.setAporteicbf(aporteSeleccionado.getAporteaux());
                duplicarAporte.setIbcicbf(aporteSeleccionado.getIbcaux());
                duplicarAporte.setTarifaicbfsobrante(aporteSeleccionado.getTarifa2aux());
                duplicarAporte.setIbcicbfsobrante(aporteSeleccionado.getIbcespaux());
                duplicarAporte.setAporteicbfsobrante(aporteSeleccionado.getAporteentaux());
            } else if (aporteEntidad.getTipoentidad().getCodigo() == 14) {
                duplicarAporte.setTarifacaja(aporteSeleccionado.getTarifaaux());
                duplicarAporte.setAportecaja(aporteSeleccionado.getAporteaux());
                duplicarAporte.setIbccaja(aporteSeleccionado.getIbcaux());
                duplicarAporte.setTarifacajasobrante(aporteSeleccionado.getTarifa2aux());
                duplicarAporte.setIbccajasobrante(aporteSeleccionado.getIbcespaux());
                duplicarAporte.setAportecajasobrante(aporteSeleccionado.getAporteentaux());
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarAporteEntidad");
            RequestContext.getCurrentInstance().execute("PF('duplicarAporteEntidad').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicarAporteEntidad() {
        FacesContext c = FacesContext.getCurrentInstance();

        if (bandera == 1) {
            altoTabla = "245";
            dia = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:dia");
            dia.setFilterStyle("display: none; visibility: hidden;");
            novedad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:novedad");
            novedad.setFilterStyle("display: none; visibility: hidden;");
            tarifa = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:tarifa");
            tarifa.setFilterStyle("display: none; visibility: hidden;");
            ibcdiario = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:ibcdiario");
            ibcdiario.setFilterStyle("display: none; visibility: hidden;");
            aportediario = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aportediario");
            aportediario.setFilterStyle("display: none; visibility: hidden;");
            tarifa2 = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:tarifa2");
            tarifa2.setFilterStyle("display: none; visibility: hidden;");
            ibcespecial = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:ibcespecial");
            ibcespecial.setFilterStyle("display: none; visibility: hidden;");
            aporteentidad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteentidad");
            aporteentidad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            bandera = 0;
            listAportesEntidadesFiltrar = null;
            tipoLista = 0;
        }
        k++;
        l = BigInteger.valueOf(k);
        duplicarAporte.setSecuencia(l);
        listAportesEntidadesCrear.add(duplicarAporte);
        listAportesEntidades.add(duplicarAporte);
        aporteSeleccionado = duplicarAporte;
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistrosAporte();
        limpiarDuplicar();
        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
        RequestContext.getCurrentInstance().execute("PF('duplicarAporteEntidad').hide()");
        if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void limpiarDuplicar() {
        duplicarAporte = new AportesEntidadesXDia();
        duplicarAporte.setEmpresa(new Empresas());
        duplicarAporte.setEmpleado(new Empleados());

    }

    public void guardarCambios() {
        try {
            if (!listAportesEntidadesBorrar.isEmpty()) {
                administrarAportesEntidades.borrarAportesEntidadesXDia(listAportesEntidadesBorrar);
                listAportesEntidadesBorrar.clear();
            }

            if (!listAportesEntidadesModificar.isEmpty()) {
                administrarAportesEntidades.editarAportesEntidadesXDia(listAportesEntidadesModificar);
                listAportesEntidadesModificar.clear();
            }
            listAportesEntidades = null;
            getListAportesEntidades();
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            contarRegistrosAporte();

            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } catch (Exception e) {
            System.out.println("Error guardarCambios  Controlador : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado de Aporte Entidad X Dia, Por favor intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            altoTabla = "245";
            dia = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:dia");
            dia.setFilterStyle("display: none; visibility: hidden;");
            novedad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:novedad");
            novedad.setFilterStyle("display: none; visibility: hidden;");
            tarifa = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:tarifa");
            tarifa.setFilterStyle("display: none; visibility: hidden;");
            ibcdiario = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:ibcdiario");
            ibcdiario.setFilterStyle("display: none; visibility: hidden;");
            aportediario = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aportediario");
            aportediario.setFilterStyle("display: none; visibility: hidden;");
            tarifa2 = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:tarifa2");
            tarifa2.setFilterStyle("display: none; visibility: hidden;");
            ibcespecial = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:ibcespecial");
            ibcespecial.setFilterStyle("display: none; visibility: hidden;");
            aporteentidad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteentidad");
            aporteentidad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            bandera = 0;
            listAportesEntidadesFiltrar = null;
            tipoLista = 0;
        }
        listAportesEntidadesBorrar.clear();
        listAportesEntidadesModificar.clear();
        aporteSeleccionado = null;
        listAportesEntidades = null;
        getListAportesEntidades();
        contarRegistrosAporte();
        guardado = true;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
    }

    public void editarCelda() {
        if (aporteSeleccionado != null) {
            editarAporte = aporteSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarDia");
                RequestContext.getCurrentInstance().execute("PF('editarDia').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarNovedad");
                RequestContext.getCurrentInstance().execute("PF('editarNovedad').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifa");
                RequestContext.getCurrentInstance().execute("PF('editarTarifa').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarIBCDiario");
                RequestContext.getCurrentInstance().execute("PF('editarIBCDiario').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarAporteDiario");
                RequestContext.getCurrentInstance().execute("PF('editarAporteDiario').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarTarifa2");
                RequestContext.getCurrentInstance().execute("PF('editarTarifa2').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarIBCEspecial");
                RequestContext.getCurrentInstance().execute("PF('editarIBCEspecial').show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editaAporteEntidad");
                RequestContext.getCurrentInstance().execute("PF('editaAporteEntidad').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "225";
            dia = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:dia");
            dia.setFilterStyle("width: 85% !important");
            novedad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:novedad");
            novedad.setFilterStyle("width: 85% !important");
            tarifa = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:tarifa");
            tarifa.setFilterStyle("width: 85% !important");
            ibcdiario = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:ibcdiario");
            ibcdiario.setFilterStyle("width: 85% !important");
            aportediario = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aportediario");
            aportediario.setFilterStyle("width: 85% !important");
            tarifa2 = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:tarifa2");
            tarifa2.setFilterStyle("width: 85% !important");
            ibcespecial = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:ibcespecial");
            ibcespecial.setFilterStyle("width: 85% !important");
            aporteentidad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteentidad");
            aporteentidad.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "245";
            dia = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:dia");
            dia.setFilterStyle("display: none; visibility: hidden;");
            novedad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:novedad");
            novedad.setFilterStyle("display: none; visibility: hidden;");
            tarifa = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:tarifa");
            tarifa.setFilterStyle("display: none; visibility: hidden;");
            ibcdiario = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:ibcdiario");
            ibcdiario.setFilterStyle("display: none; visibility: hidden;");
            aportediario = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aportediario");
            aportediario.setFilterStyle("display: none; visibility: hidden;");
            tarifa2 = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:tarifa2");
            tarifa2.setFilterStyle("display: none; visibility: hidden;");
            ibcespecial = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:ibcespecial");
            ibcespecial.setFilterStyle("display: none; visibility: hidden;");
            aporteentidad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteentidad");
            aporteentidad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            bandera = 0;
            listAportesEntidadesFiltrar = null;
            tipoLista = 0;
        }
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            altoTabla = "245";
            dia = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:dia");
            dia.setFilterStyle("display: none; visibility: hidden;");
            novedad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:novedad");
            novedad.setFilterStyle("display: none; visibility: hidden;");
            tarifa = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:tarifa");
            tarifa.setFilterStyle("display: none; visibility: hidden;");
            ibcdiario = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:ibcdiario");
            ibcdiario.setFilterStyle("display: none; visibility: hidden;");
            aportediario = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aportediario");
            aportediario.setFilterStyle("display: none; visibility: hidden;");
            tarifa2 = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:tarifa2");
            tarifa2.setFilterStyle("display: none; visibility: hidden;");
            ibcespecial = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:ibcespecial");
            ibcespecial.setFilterStyle("display: none; visibility: hidden;");
            aporteentidad = (Column) c.getViewRoot().findComponent("form:tablaAportesEntidades:aporteentidad");
            aporteentidad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
            bandera = 0;
            listAportesEntidadesFiltrar = null;
            tipoLista = 0;
        }

        listAportesEntidadesBorrar.clear();
        listAportesEntidadesModificar.clear();
        aporteSeleccionado = null;
        listAportesEntidades = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public String exportXMLTabla() {
        String tabla = "";
        if (aporteSeleccionado != null) {
            tabla = ":formExportar:datosAporteEntidadExportar";
        }
        return tabla;
    }

    public String exportXMLNombreArchivo() {
        String nombre = "";
        if (aporteSeleccionado != null) {
            nombre = "AportesEntidadesXDia_XML";
        }
        return nombre;
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAporteEntidadExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "AportesEntidadesXDia_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosAporteEntidadExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "AportesEntidadesXDia_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistrosAporte();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (aporteEntidad != null) {
            int resultado = administrarRastros.obtenerTabla(aporteEntidad.getSecuencia(), "APORTESENTIDADESXDIA");
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
        } else if (administrarRastros.verificarHistoricosTabla("APORTESENTIDADESXDIA")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistrosAporte() {
        RequestContext.getCurrentInstance().update("form:infoRegistroAporte");
    }

    /*
     public void posicionAporteEntidad() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        aporteEntidadSeleccionado = listAportesEntidades.get(indice);
        cambiarIndiceAporteEntidad(aporteEntidadSeleccionado, columna);
    }
     */
    /////////////////GETS Y SETS ////////
    public List<AportesEntidadesXDia> getListAportesEntidades() {
        if (listAportesEntidades == null) {
            listAportesEntidades = administrarAportesEntidades.consultarAportesEntidadesPorEmpleadoMesYAnio(aporteEntidad.getEmpleado().getSecuencia(), aporteEntidad.getMes(), aporteEntidad.getAno());
            if (listAportesEntidades != null) {
                if (!listAportesEntidades.isEmpty()) {
                    for (int i = 0; i < listAportesEntidades.size(); i++) {
                        aux = administrarAportesEntidades.consultarTarifas(aporteEntidad.getEmpresa().getSecuencia(), aporteEntidad.getMes(), aporteEntidad.getAno(), aporteEntidad.getEmpleado().getSecuencia(), aporteEntidad.getTipoentidad().getSecuencia());
                        if (aux.equals(BigDecimal.valueOf(1))) {
                            listAportesEntidades.get(i).setTarifaaux(listAportesEntidades.get(i).getTarifasalud());
                            listAportesEntidades.get(i).setAporteaux(listAportesEntidades.get(i).getAportesalud());
                            listAportesEntidades.get(i).setIbcaux(listAportesEntidades.get(i).getIbcsalud());
                            listAportesEntidades.get(i).setTarifa2aux(listAportesEntidades.get(i).getTarifasaludsobrante());
                            listAportesEntidades.get(i).setIbcespaux(listAportesEntidades.get(i).getIbcsaludsobrante());
                            listAportesEntidades.get(i).setAporteentaux(listAportesEntidades.get(i).getAportesaludsobrante());
                        } else if (aux.equals(BigDecimal.valueOf(2))) {
                            listAportesEntidades.get(i).setTarifaaux(listAportesEntidades.get(i).getTarifariesgo());
                            listAportesEntidades.get(i).setAporteaux(listAportesEntidades.get(i).getAporteriesgo());
                            listAportesEntidades.get(i).setIbcaux(listAportesEntidades.get(i).getIbcriesgo());
                            listAportesEntidades.get(i).setTarifa2aux(listAportesEntidades.get(i).getTarifariesgosobrante());
                            listAportesEntidades.get(i).setIbcespaux(listAportesEntidades.get(i).getIbcriesgosobrante());
                            listAportesEntidades.get(i).setAporteentaux(listAportesEntidades.get(i).getAporteriesgosobrante());
                        } else if (aux.equals(BigDecimal.valueOf(3))) {
                            listAportesEntidades.get(i).setTarifaaux(listAportesEntidades.get(i).getTarifapension());
                            listAportesEntidades.get(i).setAporteaux(listAportesEntidades.get(i).getAportepension());
                            listAportesEntidades.get(i).setIbcaux(listAportesEntidades.get(i).getIbcpension());
                            listAportesEntidades.get(i).setTarifa2aux(listAportesEntidades.get(i).getTarifapensionsobrante());
                            listAportesEntidades.get(i).setIbcespaux(listAportesEntidades.get(i).getIbcpensionsobrante());
                            listAportesEntidades.get(i).setAporteentaux(listAportesEntidades.get(i).getAportepensionsobrante());
                        } else if (aux.equals(BigDecimal.valueOf(11))) {
                            listAportesEntidades.get(i).setTarifaaux(listAportesEntidades.get(i).getTarifasena());
                            listAportesEntidades.get(i).setAporteaux(listAportesEntidades.get(i).getAportesena());
                            listAportesEntidades.get(i).setIbcaux(listAportesEntidades.get(i).getIbcsena());
                            listAportesEntidades.get(i).setTarifa2aux(listAportesEntidades.get(i).getTarifasenasobrante());
                            listAportesEntidades.get(i).setIbcespaux(listAportesEntidades.get(i).getIbcsenasobrante());
                            listAportesEntidades.get(i).setAporteentaux(listAportesEntidades.get(i).getAportesenasobrante());
                        } else if (aux.equals(BigDecimal.valueOf(13))) {
                            listAportesEntidades.get(i).setTarifaaux(listAportesEntidades.get(i).getTarifaicbf());
                            listAportesEntidades.get(i).setAporteaux(listAportesEntidades.get(i).getAporteicbf());
                            listAportesEntidades.get(i).setIbcaux(listAportesEntidades.get(i).getIbcicbf());
                            listAportesEntidades.get(i).setTarifa2aux(listAportesEntidades.get(i).getTarifaicbfsobrante());
                            listAportesEntidades.get(i).setIbcespaux(listAportesEntidades.get(i).getIbcicbfsobrante());
                            listAportesEntidades.get(i).setAporteentaux(listAportesEntidades.get(i).getAporteicbfsobrante());
                        } else if (aux.equals(BigDecimal.valueOf(14))) {
                            listAportesEntidades.get(i).setTarifaaux(listAportesEntidades.get(i).getTarifacaja());
                            listAportesEntidades.get(i).setAporteaux(listAportesEntidades.get(i).getAportecaja());
                            listAportesEntidades.get(i).setIbcaux(listAportesEntidades.get(i).getIbccaja());
                            listAportesEntidades.get(i).setTarifa2aux(listAportesEntidades.get(i).getTarifacajasobrante());
                            listAportesEntidades.get(i).setIbcespaux(listAportesEntidades.get(i).getIbccajasobrante());
                            listAportesEntidades.get(i).setAporteentaux(listAportesEntidades.get(i).getAportecajasobrante());
                        }
                    }
                    RequestContext.getCurrentInstance().update("form:tablaAportesEntidades");
                }
            }
        }
        return listAportesEntidades;
    }

    public void setListAportesEntidades(List<AportesEntidadesXDia> listAportesEntidades) {
        this.listAportesEntidades = listAportesEntidades;
    }

    public List<AportesEntidadesXDia> getListAportesEntidadesFiltrar() {
        return listAportesEntidadesFiltrar;
    }

    public void setListAportesEntidadesFiltrar(List<AportesEntidadesXDia> listAportesEntidadesFiltrar) {
        this.listAportesEntidadesFiltrar = listAportesEntidadesFiltrar;
    }

    public AportesEntidadesXDia getAporteSeleccionado() {
        return aporteSeleccionado;
    }

    public void setAporteSeleccionado(AportesEntidadesXDia aporteSeleccionado) {
        this.aporteSeleccionado = aporteSeleccionado;
    }

    public AportesEntidadesXDia getEditarAporte() {
        return editarAporte;
    }

    public void setEditarAporte(AportesEntidadesXDia editarAporte) {
        this.editarAporte = editarAporte;
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

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

    public String getAltoTabla() {
        return altoTabla;
    }

    public void setAltoTabla(String altoTabla) {
        this.altoTabla = altoTabla;
    }

    public String getInfoRegistroAporte() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:tablaAportesEntidades");
        infoRegistroAporte = String.valueOf(tabla.getRowCount());
        return infoRegistroAporte;
    }

    public void setInfoRegistroAporte(String infoRegistroAporte) {
        this.infoRegistroAporte = infoRegistroAporte;
    }

    public AportesEntidades getAporteEntidad() {
        return aporteEntidad;
    }

    public void setAporteEntidad(AportesEntidades aporteEntidad) {
        this.aporteEntidad = aporteEntidad;
    }

    public BigDecimal getIbcmensual() {
        ibcmensual = BigDecimal.ZERO;
        if (listAportesEntidades != null) {
            if (!listAportesEntidades.isEmpty()) {
                for (int i = 0; i < listAportesEntidades.size(); i++) {
                    ibcmensual = ibcmensual.add(listAportesEntidades.get(i).getIbcaux());
                }
            }
        }
        return ibcmensual;
    }

    public void setIbcmensual(BigDecimal ibcmensual) {
        this.ibcmensual = ibcmensual;
    }

    public BigDecimal getAportemensual() {
        aportemensual = BigDecimal.ZERO;
        if (listAportesEntidades != null) {
            if (!listAportesEntidades.isEmpty()) {
                for (int i = 0; i < listAportesEntidades.size(); i++) {
                    aportemensual = aportemensual.add(listAportesEntidades.get(i).getAporteaux());
                }
            }
        }
        return aportemensual;
    }

    public void setAportemensual(BigDecimal aportemensual) {
        this.aportemensual = aportemensual;
    }

    public BigDecimal getIbcespecialmensual() {
        ibcespecialmensual = BigDecimal.ZERO;
        if (listAportesEntidades != null) {
            if (!listAportesEntidades.isEmpty()) {
                for (int i = 0; i < listAportesEntidades.size(); i++) {
                    ibcespecialmensual = ibcespecialmensual.add(listAportesEntidades.get(i).getIbcespaux());
                }
            }
        }
        return ibcespecialmensual;
    }

    public void setIbcespecialmensual(BigDecimal ibcespecialmensual) {
        this.ibcespecialmensual = ibcespecialmensual;
    }

    public BigDecimal getAporteentidadmensual() {
        aporteentidadmensual = BigDecimal.ZERO;
        if (listAportesEntidades != null) {
            if (!listAportesEntidades.isEmpty()) {
                for (int i = 0; i < listAportesEntidades.size(); i++) {
                    aporteentidadmensual = aporteentidadmensual.add(listAportesEntidades.get(i).getAporteentaux());
                }
            }
        }
        return aporteentidadmensual;
    }

    public void setAporteentidadmensual(BigDecimal aporteentidadmensual) {
        this.aporteentidadmensual = aporteentidadmensual;
    }

    public AportesEntidadesXDia getNuevoAporte() {
        return nuevoAporte;
    }

    public void setNuevoAporte(AportesEntidadesXDia nuevoAporte) {
        this.nuevoAporte = nuevoAporte;
    }

    public AportesEntidadesXDia getDuplicarAporte() {
        return duplicarAporte;
    }

    public void setDuplicarAporte(AportesEntidadesXDia duplicarAporte) {
        this.duplicarAporte = duplicarAporte;
    }

}
