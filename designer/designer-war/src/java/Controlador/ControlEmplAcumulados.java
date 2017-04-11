/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;


import Entidades.Empleados;
import Entidades.VWAcumulados;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEmplAcumuladosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;import ControlNavegacion.ControlListaNavegacion;
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
 * @author user
 */
@ManagedBean
@SessionScoped
public class ControlEmplAcumulados implements Serializable {

    @EJB
    AdministrarEmplAcumuladosInterface administrarEmplAcumulados;
    private BigInteger secuenciaEmpleado;
    private Empleados empleadoSeleccionado;
//valores tablas
    private List<VWAcumulados> listVWAcumuladosPorEmpleado;
    private List<VWAcumulados> filtrarVWAcumuladosPorEmpleado;
    private VWAcumulados nuevaEmplAcumulados;
    private VWAcumulados editarVWAcumuladosPorEmpleado;
    private VWAcumulados acumuladosPorEmpleadoSeleccionado;
//otros
    private int tipoLista, bandera, index, cualCelda;
    //revisarcambios
    private BigInteger revisarConceptoCodigo;
    //
    private Column conceptoCodigo, conceptoDescripcion, fechaDesde, fechaPago, unidades,
            valor, saldo, tipo, corteProceso, tercero, formula, debito, centroCostoDebito,
            credito, centroCostoCredito, ultimaModificacion, observaciones, motivoNovedad;
    private BigInteger secRegistro;
    private int tamano;
    public String altoTabla;
    public String infoRegistro;
       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>(); 

    public ControlEmplAcumulados() {
        empleadoSeleccionado = null;
        secuenciaEmpleado = BigInteger.valueOf(11349682);
        listVWAcumuladosPorEmpleado = null;
        editarVWAcumuladosPorEmpleado = new VWAcumulados();
        secRegistro = null;
        altoTabla = "292";
   mapParametros.put ("paginaAnterior", paginaAnterior);
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
         System.out.println("navegar('Atras') : " + pag);
      } else {
         String pagActual = "emplacumulados";
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
         controlListaNavegacion.guardarNavegacion(pagActual, pag);
      }
      limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
    }

  public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEmplAcumulados.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirEmpleado(BigInteger sec) {
        if (sec == null) {
            System.out.println("ControlVigenciasFormasPagos.recibirEmpleado");
            System.out.println("La secuencia pasada como parametro es null: " + sec.toString());
        }
        empleadoSeleccionado = null;
        secuenciaEmpleado = sec;
        getListVWAcumuladosPorEmpleado();
        if (listVWAcumuladosPorEmpleado != null && !listVWAcumuladosPorEmpleado.isEmpty()) {
            System.out.println("Entra al primer IF");
            if (listVWAcumuladosPorEmpleado.size() == 1) {
                //INFORMACION REGISTRO
                acumuladosPorEmpleadoSeleccionado = listVWAcumuladosPorEmpleado.get(0);
                //infoRegistro = "Registro 1 de 1";
               // infoRegistro = "Cantidad de registros: 1";
                modificarInfoRegistro(1);
            } else if (listVWAcumuladosPorEmpleado.size() > 1) {
                System.out.println("Else If");
                //INFORMACION REGISTRO
                acumuladosPorEmpleadoSeleccionado = listVWAcumuladosPorEmpleado.get(0);
                //infoRegistro = "Registro 1 de " + vigenciasCargosEmpleado.size();
               // infoRegistro = "Cantidad de registros: " + listVWAcumuladosPorEmpleado.size();
                modificarInfoRegistro(listVWAcumuladosPorEmpleado.size());
            }

        } else {
            //infoRegistro = "Cantidad de registros: 0";
            modificarInfoRegistro(0);
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        modificarInfoRegistro(filtrarVWAcumuladosPorEmpleado.size());
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            altoTabla = "272";
            conceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:conceptoCodigo");
            conceptoCodigo.setFilterStyle("width: 85% !important");
            conceptoDescripcion = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:conceptoDescripcion");
            conceptoDescripcion.setFilterStyle("width: 85% !important");
            fechaDesde = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:fechaDesde");
            fechaDesde.setFilterStyle("width: 85% !important");
            fechaPago = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:fechaPago");
            fechaPago.setFilterStyle("width: 85% !important");
            unidades = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:unidades");
            unidades.setFilterStyle("width: 85% !important");
            valor = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:valor");
            valor.setFilterStyle("width: 85% !important");
            saldo = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:saldo");
            saldo.setFilterStyle("width: 85% !important");
            tipo = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:tipo");
            tipo.setFilterStyle("width: 85% !important");
            corteProceso = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:corteProceso");
            corteProceso.setFilterStyle("width: 85% !important");
            tercero = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:tercero");
            tercero.setFilterStyle("width: 85% !important");
            formula = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:formula");
            formula.setFilterStyle("width: 85% !important");
            debito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:debito");
            debito.setFilterStyle("width: 85% !important");
            centroCostoDebito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:centroCostoDebito");
            centroCostoDebito.setFilterStyle("width: 85% !important");
            credito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:credito");
            credito.setFilterStyle("width: 85% !important");
            centroCostoCredito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:centroCostoCredito");
            centroCostoCredito.setFilterStyle("width: 85% !important");
            ultimaModificacion = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:ultimaModificacion");
            ultimaModificacion.setFilterStyle("width: 85% !important");
            observaciones = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:observaciones");
            observaciones.setFilterStyle("width: 85% !important");
            motivoNovedad = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:motivoNovedad");
            motivoNovedad.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosEmplAcumulados");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            altoTabla = "292";
            System.out.println("Desactivar");
            cerrarFiltrado();
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVWAEmpleadoExportar");
        //DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:aficiones");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "EmplAcumuladosPDF", false, false, "UTF-8", null, null);
        //exporter.export(context, tabla, "AficionesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosVWAEmpleadoExportar");
        //DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:aficiones");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "EmplAcumuladosXLS", false, false, "UTF-8", null, null);
        //exporter.export(context, tabla, "AficionesPDF", false, false, "UTF-8", null, null);
        context.responseComplete();
        index = -1;
        secRegistro = null;
    }

    public void limpiarNuevaVigenciaFormpasPagos() {
        System.out.println("LimpiarNuevaVWAcumulados");
        nuevaEmplAcumulados = new VWAcumulados();

        index = -1;
        secRegistro = null;
    }

    public void editarCelda() {
        if (index >= 0) {
            if (tipoLista == 0) {
                editarVWAcumuladosPorEmpleado = listVWAcumuladosPorEmpleado.get(index);
            }
            if (tipoLista == 1) {
                editarVWAcumuladosPorEmpleado = filtrarVWAcumuladosPorEmpleado.get(index);
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
            if (cualCelda == 0) {
                System.err.println("EditarCelda cualcelda = " + cualCelda);
                RequestContext.getCurrentInstance().update("formularioDialogos:editConceptoCodigo");
                RequestContext.getCurrentInstance().execute("PF('editConceptoCodigo').show()");
                cualCelda = -1;
            }
            if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editConceptoDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editConceptoDescripcion').show()");
                cualCelda = -1;
            }
            if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechaDesde");
                RequestContext.getCurrentInstance().execute("PF('editFechaDesde').show()");
                cualCelda = -1;
            }
            if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechaPago");
                RequestContext.getCurrentInstance().execute("PF('editFechaPago').show()");
                cualCelda = -1;
            }
            if (cualCelda == 4) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editUnidad");
                RequestContext.getCurrentInstance().execute("PF('editUnidad').show()");
                cualCelda = -1;
            }
            if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editValor");
                RequestContext.getCurrentInstance().execute("PF('editValor').show()");
                cualCelda = -1;
            }
            if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editSaldo");
                RequestContext.getCurrentInstance().execute("PF('editSaldo').show()");
                cualCelda = -1;
            }
            if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTipo");
                RequestContext.getCurrentInstance().execute("PF('editTipo').show()");
                cualCelda = -1;
            }
            if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editProceso");
                RequestContext.getCurrentInstance().execute("PF('editProceso').show()");
                cualCelda = -1;
            }
            if (cualCelda == 9) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editNitNombre");
                RequestContext.getCurrentInstance().execute("PF('editNitNombre').show()");
                cualCelda = -1;
            }
            if (cualCelda == 10) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFormula");
                RequestContext.getCurrentInstance().execute("PF('editFormula').show()");
                cualCelda = -1;
            }
            if (cualCelda == 11) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDebito");
                RequestContext.getCurrentInstance().execute("PF('editDebito').show()");
                cualCelda = -1;
            }
            if (cualCelda == 12) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCentroCostoDebito");
                RequestContext.getCurrentInstance().execute("PF('editCentroCostoDebito').show()");
                cualCelda = -1;
            }
            if (cualCelda == 13) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCredito");
                RequestContext.getCurrentInstance().execute("PF('editCredito').show()");
                cualCelda = -1;
            }
            if (cualCelda == 13) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCredito");
                RequestContext.getCurrentInstance().execute("PF('editCredito').show()");
                cualCelda = -1;
            }
            if (cualCelda == 14) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCentroCostoCredito");
                RequestContext.getCurrentInstance().execute("PF('editCentroCostoCredito').show()");
                cualCelda = -1;
            }
            if (cualCelda == 15) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editFechaModificacion");
                RequestContext.getCurrentInstance().execute("PF('editFechaModificacion').show()");
                cualCelda = -1;
            }
            if (cualCelda == 16) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editObservaciones");
                RequestContext.getCurrentInstance().execute("PF('editObservaciones').show()");
                cualCelda = -1;
            }
            if (cualCelda == 17) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editMotivoNovedad");
                RequestContext.getCurrentInstance().execute("PF('editMotivoNovedad').show()");
                cualCelda = -1;
            }
            if (cualCelda == 18) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDetallesConcepto");
                RequestContext.getCurrentInstance().execute("PF('editDetallesConcepto').show()");
                cualCelda = -1;
            }
        }
        index = -1;
        secRegistro = null;
    }

    /**
     * Metodo que obtiene la posicion dentro de la tabla VigenciasLocalizaciones
     *
     * @param indice Fila de la tabla
     * @param celda Columna de la tabla
     */
    public void cambiarIndice(int indice, int celda) {

        index = indice;
        cualCelda = celda;
        secRegistro = listVWAcumuladosPorEmpleado.get(index).getSecuencia();
        System.out.println("Indice: " + index + " Celda: " + cualCelda);

    }

    public void revisarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().execute("PF('repeticiones').show()");
        listVWAcumuladosPorEmpleado = null;
        context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEmplAcumulados");
    }
//------------------------------------------------------------------------------

    public void cancelarModificacion() {
        if (bandera == 1) {
            cerrarFiltrado();
        }

        index = -1;
        secRegistro = null;
        listVWAcumuladosPorEmpleado = null;
        acumuladosPorEmpleadoSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("form:datosEmplAcumulados");
    }

    public void cerrarFiltrado() {
        FacesContext c = FacesContext.getCurrentInstance();
        System.out.println("CancelarModificacion");
        conceptoCodigo = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:conceptoCodigo");
        conceptoCodigo.setFilterStyle("display: none; visibility: hidden;");
        conceptoDescripcion = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:conceptoDescripcion");
        conceptoDescripcion.setFilterStyle("display: none; visibility: hidden;");
        fechaDesde = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:fechaDesde");
        fechaDesde.setFilterStyle("display: none; visibility: hidden;");
        fechaPago = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:fechaPago");
        fechaPago.setFilterStyle("display: none; visibility: hidden;");
        unidades = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:unidades");
        unidades.setFilterStyle("display: none; visibility: hidden;");
        valor = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:valor");
        valor.setFilterStyle("display: none; visibility: hidden;");
        saldo = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:saldo");
        saldo.setFilterStyle("display: none; visibility: hidden;");
        tipo = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:tipo");
        tipo.setFilterStyle("display: none; visibility: hidden;");
        corteProceso = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:corteProceso");
        corteProceso.setFilterStyle("display: none; visibility: hidden;");
        tercero = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:tercero");
        tercero.setFilterStyle("display: none; visibility: hidden;");
        formula = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:formula");
        formula.setFilterStyle("display: none; visibility: hidden;");
        debito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:debito");
        debito.setFilterStyle("display: none; visibility: hidden;");
        centroCostoDebito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:centroCostoDebito");
        centroCostoDebito.setFilterStyle("display: none; visibility: hidden;");
        credito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:credito");
        credito.setFilterStyle("display: none; visibility: hidden;");
        centroCostoCredito = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:centroCostoCredito");
        centroCostoCredito.setFilterStyle("display: none; visibility: hidden;");
        ultimaModificacion = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:ultimaModificacion");
        ultimaModificacion.setFilterStyle("display: none; visibility: hidden;");
        observaciones = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:observaciones");
        observaciones.setFilterStyle("display: none; visibility: hidden;");
        motivoNovedad = (Column) c.getViewRoot().findComponent("form:datosEmplAcumulados:motivoNovedad");
        motivoNovedad.setFilterStyle("display: none; visibility: hidden;");
        altoTabla = "292";
        RequestContext.getCurrentInstance().update("form:datosEmplAcumulados");
        bandera = 0;
        filtrarVWAcumuladosPorEmpleado = null;
        tipoLista = 0;
    }
    
     private void modificarInfoRegistro(int valor) {
        infoRegistro = String.valueOf(valor);
        System.out.println("infoRegistro: " + infoRegistro);
    }


    //-----------------------------------------------------------------------------
    public Empleados getEmpleadoSeleccionado() {
        if (empleadoSeleccionado == null) {
            empleadoSeleccionado = administrarEmplAcumulados.consultarEmpleado(secuenciaEmpleado);
        }
        return empleadoSeleccionado;
    }

    public void setEmpleadoSeleccionado(Empleados empleadoSeleccionado) {
        this.empleadoSeleccionado = empleadoSeleccionado;
    }

    public List<VWAcumulados> getListVWAcumuladosPorEmpleado() {
        if (listVWAcumuladosPorEmpleado == null) {
            listVWAcumuladosPorEmpleado = administrarEmplAcumulados.consultarVWAcumuladosEmpleado(secuenciaEmpleado);
        }
        return listVWAcumuladosPorEmpleado;

    }

    public void setListVWAcumuladosPorEmpleado(List<VWAcumulados> listVWAcumuladosPorEmpleado) {
        this.listVWAcumuladosPorEmpleado = listVWAcumuladosPorEmpleado;
    }

    public List<VWAcumulados> getFiltrarVWAcumuladosPorEmpleado() {
        return filtrarVWAcumuladosPorEmpleado;
    }

    public void setFiltrarVWAcumuladosPorEmpleado(List<VWAcumulados> filtrarVWAcumuladosPorEmpleado) {
        this.filtrarVWAcumuladosPorEmpleado = filtrarVWAcumuladosPorEmpleado;
    }

    public VWAcumulados getEditarVWAcumuladosPorEmpleado() {
        return editarVWAcumuladosPorEmpleado;
    }

    public void setEditarVWAcumuladosPorEmpleado(VWAcumulados editarVWAcumuladosPorEmpleado) {
        this.editarVWAcumuladosPorEmpleado = editarVWAcumuladosPorEmpleado;
    }

    public VWAcumulados getAcumuladosPorEmpleadoSeleccionado() {
        return acumuladosPorEmpleadoSeleccionado;
    }

    public void setAcumuladosPorEmpleadoSeleccionado(VWAcumulados acumuladosPorEmpleadoSeleccionado) {
        this.acumuladosPorEmpleadoSeleccionado = acumuladosPorEmpleadoSeleccionado;
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
}
