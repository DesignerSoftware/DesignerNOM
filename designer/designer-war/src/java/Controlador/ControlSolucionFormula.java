package Controlador;

import Entidades.Empleados;
import Entidades.Novedades;
import Entidades.SolucionesFormulas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSolucionesFormulasInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
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
 * @author PROYECTO01
 */
@ManagedBean
@SessionScoped
public class ControlSolucionFormula implements Serializable {

    @EJB
    AdministrarSolucionesFormulasInterface administrarSolucionesFormulas;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<SolucionesFormulas> listaSolucionesFormulas;
    private List<SolucionesFormulas> filtrarListaSolucionesFormulas;
    private SolucionesFormulas solucionTablaSeleccionada;
    private Empleados empleado;
    private Novedades novedad;
    private int bandera;
    private Column fechaHasta, concepto, valor, saldo, fechaPago, proceso, formula;
    private boolean aceptar;
    private int index;
    private SolucionesFormulas editarSolucionFormula;
    private int cualCelda, tipoLista;
    private BigInteger secRegistro;
    private BigInteger backUpSecRegistro;
    private String informacionEmpleadoNovedad;
    private String algoTabla;
    //
    private String infoRegistro;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlSolucionFormula() {
        algoTabla = "300";
        empleado = new Empleados();
        novedad = new Novedades();
        backUpSecRegistro = null;
        listaSolucionesFormulas = null;
        aceptar = true;
        editarSolucionFormula = new SolucionesFormulas();
        cualCelda = -1;
        tipoLista = 0;
        secRegistro = null;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarSolucionesFormulas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        //inicializarCosas(); Inicializar cosas de ser necesario
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        //inicializarCosas(); Inicializar cosas de ser necesario
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
            String pagActual = "solucionformula";
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

    public void recibirParametros(BigInteger codEmpleado, BigInteger secNovedad) {
        empleado = administrarSolucionesFormulas.empleadoActual(codEmpleado);
        listaSolucionesFormulas = administrarSolucionesFormulas.listaSolucionesFormulaParaEmpleadoYNovedad(empleado.getSecuencia(), secNovedad);
        novedad = administrarSolucionesFormulas.novedadActual(secNovedad);
        if (novedad == null) {
            informacionEmpleadoNovedad = empleado.getPersona().getNombreCompleto().toUpperCase() + " NOVEDAD POR VALOR DE : $ 0";
        } else {
            informacionEmpleadoNovedad = empleado.getPersona().getNombreCompleto().toUpperCase() + " NOVEDAD POR VALOR DE : $ " + novedad.getValortotal().toString();
        }
        int tamanio = informacionEmpleadoNovedad.length();
        if (tamanio > 60) {
            String aux = informacionEmpleadoNovedad;
            informacionEmpleadoNovedad = "";
            for (int i = 0; i < 56; i++) {
                informacionEmpleadoNovedad = informacionEmpleadoNovedad + aux.charAt(i) + "";
            }
            informacionEmpleadoNovedad = informacionEmpleadoNovedad + "...";
        }
        if (listaSolucionesFormulas != null) {
            infoRegistro = "Cantidad de registros : " + listaSolucionesFormulas.size();
        } else {
            infoRegistro = "Cantidad de registros : 0";
        }
    }

    public void posicionTabla() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String type = map.get("t"); // type attribute of node
        String cass = map.get("n"); // type attribute of node
        int ind = Integer.parseInt(type);
        int cassi = Integer.parseInt(cass);
        cambiarIndice(ind, cassi);
    }

    public void cambiarIndice(int indice, int celda) {
        index = indice;
        cualCelda = celda;
        if (tipoLista == 0) {
            secRegistro = listaSolucionesFormulas.get(index).getSecuencia();
        }
        if (tipoLista == 1) {
            secRegistro = filtrarListaSolucionesFormulas.get(index).getSecuencia();
        }
        System.out.println("Index : " + index + "// Celda: " + cualCelda);
    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarSolucionFormula = listaSolucionesFormulas.get(index);
            }
            if (tipoLista == 1) {
                editarSolucionFormula = filtrarListaSolucionesFormulas.get(index);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaHastaD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaHastaD').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarConceptoD");
                RequestContext.getCurrentInstance().execute("PF('editarConceptoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarValorD");
                RequestContext.getCurrentInstance().execute("PF('editarValorD').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarSaldoD");
                RequestContext.getCurrentInstance().execute("PF('editarSaldoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFechaPagoD");
                RequestContext.getCurrentInstance().execute("PF('editarFechaPagoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarProcesoD");
                RequestContext.getCurrentInstance().execute("PF('editarProcesoD').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editarFormulaD");
                RequestContext.getCurrentInstance().execute("PF('editarFormulaD').show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    public void activarCtrlF11() {
        if (bandera == 0) {
            algoTabla = "280";
            fechaHasta = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaHasta");
            fechaHasta.setFilterStyle("width: 85% !important;");
            concepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:concepto");
            concepto.setFilterStyle("width: 85% !important;");
            fechaPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaPago");
            fechaPago.setFilterStyle("width: 85% !important;");
            valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:valor");
            valor.setFilterStyle("width: 85% !important;");
            saldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:saldo");
            saldo.setFilterStyle("width: 85% !important;");
            proceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:proceso");
            proceso.setFilterStyle("width: 85% !important;");
            formula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:formula");
            formula.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosSolucionFormula");
            bandera = 1;
        } else if (bandera == 1) {
            algoTabla = "300";
            fechaHasta = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaHasta");
            fechaHasta.setFilterStyle("display: none; visibility: hidden;");
            concepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:concepto");
            concepto.setFilterStyle("display: none; visibility: hidden;");
            fechaPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaPago");
            fechaPago.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            saldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:saldo");
            saldo.setFilterStyle("display: none; visibility: hidden;");
            proceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:proceso");
            proceso.setFilterStyle("display: none; visibility: hidden;");
            formula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:formula");
            formula.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSolucionFormula");
            bandera = 0;
            filtrarListaSolucionesFormulas = null;
            tipoLista = 0;
        }
        index = -1;
        secRegistro = null;
    }

    public void salir() {
        if (bandera == 1) {
            algoTabla = "300";
            fechaHasta = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaHasta");
            fechaHasta.setFilterStyle("display: none; visibility: hidden;");
            concepto = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:concepto");
            concepto.setFilterStyle("display: none; visibility: hidden;");
            fechaPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:fechaPago");
            fechaPago.setFilterStyle("display: none; visibility: hidden;");
            valor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:valor");
            valor.setFilterStyle("display: none; visibility: hidden;");
            saldo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:saldo");
            saldo.setFilterStyle("display: none; visibility: hidden;");
            proceso = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:proceso");
            proceso.setFilterStyle("display: none; visibility: hidden;");
            formula = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosSolucionFormula:formula");
            formula.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosSolucionFormula");
            bandera = 0;
            filtrarListaSolucionesFormulas = null;
            tipoLista = 0;
        }
        index = -1;
        secRegistro = null;
        listaSolucionesFormulas = null;
    }
    //EXPORTAR

    /**
     * Metodo que exporta datos a PDF
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSolucionFormulaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "SolucionesFormulas_PDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    /**
     * Metodo que exporta datos a XLS
     *
     * @throws IOException Excepcion de In-Out de datos
     */
    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosSolucionFormulaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "SolucionesFormulas_XLS", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }
    //EVENTO FILTRAR

    /**
     * Evento que cambia la lista reala a la filtrada
     */
    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        infoRegistro = "Cantidad de registros : " + filtrarListaSolucionesFormulas.size();
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }
    //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (listaSolucionesFormulas != null) {
            if (secRegistro != null) {
                int resultado = administrarRastros.obtenerTabla(secRegistro, "SOLUCIONESFORMULAS");
                backUpSecRegistro = secRegistro;
                secRegistro = null;
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
        } else if (administrarRastros.verificarHistoricosTabla("SOLUCIONESFORMULAS")) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
        index = -1;
    }

    public List<SolucionesFormulas> getListaSolucionesFormulas() {
        try {
            if (listaSolucionesFormulas == null) {
                return listaSolucionesFormulas = administrarSolucionesFormulas.listaSolucionesFormulaParaEmpleadoYNovedad(empleado.getSecuencia(), novedad.getSecuencia());
            }
            return listaSolucionesFormulas;

        } catch (Exception e) {
            System.out.println("Error...!! getListaSolucionesFormulas : " + e.toString());
            return null;
        }
    }

    public void setListaSolucionesFormulas(List<SolucionesFormulas> setListaSolucionesFormulas) {
        this.listaSolucionesFormulas = setListaSolucionesFormulas;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public List<SolucionesFormulas> getFiltrarListaSolucionesFormulas() {
        return filtrarListaSolucionesFormulas;
    }

    public void setFiltrarListaSolucionesFormulas(List<SolucionesFormulas> setFiltrarListaSolucionesFormulas) {
        this.filtrarListaSolucionesFormulas = setFiltrarListaSolucionesFormulas;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public SolucionesFormulas getEditarSolucionFormula() {
        return editarSolucionFormula;
    }

    public void setEditarSolucionFormula(SolucionesFormulas setEditarSolucionFormula) {
        this.editarSolucionFormula = setEditarSolucionFormula;
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

    public void setBackUpSecRegistro(BigInteger BackUpSecRegistro) {
        this.backUpSecRegistro = BackUpSecRegistro;
    }

    public Novedades getNovedad() {
        return novedad;
    }

    public void setNovedad(Novedades novedad) {
        this.novedad = novedad;
    }

    public String getInformacionEmpleadoNovedad() {
        return informacionEmpleadoNovedad;
    }

    public void setInformacionEmpleadoNovedad(String informacionEmpleadoNovedad) {
        this.informacionEmpleadoNovedad = informacionEmpleadoNovedad;
    }

    public String getAlgoTabla() {
        return algoTabla;
    }

    public void setAlgoTabla(String algoTabla) {
        this.algoTabla = algoTabla;
    }

    public SolucionesFormulas getSolucionTablaSeleccionada() {
        getListaSolucionesFormulas();
        if (listaSolucionesFormulas != null) {
            int tam = listaSolucionesFormulas.size();
            if (tam > 0) {
                solucionTablaSeleccionada = listaSolucionesFormulas.get(0);
            }
        }
        return solucionTablaSeleccionada;
    }

    public void setSolucionTablaSeleccionada(SolucionesFormulas solucionTablaSeleccionada) {
        this.solucionTablaSeleccionada = solucionTablaSeleccionada;
    }

    public String getInfoRegistro() {
        getListaSolucionesFormulas();
        if (listaSolucionesFormulas != null) {
            infoRegistro = "Cantidad de registros : " + listaSolucionesFormulas.size();
        } else {
            infoRegistro = "Cantidad de registros : 0";
        }
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
