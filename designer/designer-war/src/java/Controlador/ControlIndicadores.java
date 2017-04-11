/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Indicadores;
import Entidades.TiposIndicadores;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarIndicadoresInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
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
@Named(value = "controlIndicadores")
@SessionScoped
public class ControlIndicadores implements Serializable {

    @EJB
    AdministrarIndicadoresInterface administrarIndicadores;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<Indicadores> listIndicadores;
    private List<Indicadores> filtrarListIndicadores;
    private List<Indicadores> listCrearIndicadores;
    private List<Indicadores> listModificarIndicadores;
    private List<Indicadores> listBorrarIndicadores;
    private Indicadores indicadorSeleccionado;
    private Indicadores nuevoIndicador;
    private Indicadores editarIndicador;
    private Indicadores duplicarIndicador;
    private int tamano;
    ///lov tipos indicadores
    private List<TiposIndicadores> lovTiposIndicadores;
    private List<TiposIndicadores> filtrarLovTiposIndicadores;
    private TiposIndicadores tipoIndicadorSeleccionado;
    //
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    private boolean permitirIndex;

    private Column codigo, descripcion, tipo;
    private int registroBorrados;
    private String mensajeValidacion;
    private String infoRegistro, infoRegistroTipo;
    private DataTable tablaC;
    private boolean activarLov;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlIndicadores() {
        listIndicadores = null;
        listBorrarIndicadores = new ArrayList<Indicadores>();
        listCrearIndicadores = new ArrayList<Indicadores>();
        listModificarIndicadores = new ArrayList<Indicadores>();
        permitirIndex = true;
        nuevoIndicador = new Indicadores();
        nuevoIndicador.setTipoindicador(new TiposIndicadores());
        editarIndicador = new Indicadores();
        duplicarIndicador = new Indicadores();
        guardado = true;
        tamano = 270;
        paginaAnterior = "nominaf";
        activarLov = true;
        lovTiposIndicadores = null;
        tipoIndicadorSeleccionado = null;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        getListIndicadores();
        if (listIndicadores != null) {
            if (!listIndicadores.isEmpty()) {
                indicadorSeleccionado = listIndicadores.get(0);
            }
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        getListIndicadores();
        if (listIndicadores != null) {
            if (!listIndicadores.isEmpty()) {
                indicadorSeleccionado = listIndicadores.get(0);
            }
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
         System.out.println("navegar('Atras') : " + pag);
        } else {
            String pagActual = "indicador";
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
            administrarIndicadores.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public String retornarPagina() {
        return paginaAnterior;
    }

    public void cambiarIndice(Indicadores indicador, int celda) {
        if (permitirIndex == true) {
            indicadorSeleccionado = indicador;
            cualCelda = celda;
            if (cualCelda == 0) {
                indicadorSeleccionado.getCodigo();
                deshabilitarBotonLov();
            } else if (cualCelda == 1) {
                indicadorSeleccionado.getDescripcion();
                deshabilitarBotonLov();
            } else if (cualCelda == 2) {
                indicadorSeleccionado.getTipoindicador().getDescripcion();
                habilitarBotonLov();
            }
            indicadorSeleccionado.getSecuencia();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosIndicadores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosIndicadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            tipo = (Column) c.getViewRoot().findComponent("form:datosIndicadores:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIndicadores");
            bandera = 0;
            filtrarListIndicadores = null;
            tipoLista = 0;
            tamano = 270;
        }

        listBorrarIndicadores.clear();
        listCrearIndicadores.clear();
        listModificarIndicadores.clear();
        k = 0;
        listIndicadores = null;
        indicadorSeleccionado = null;
        guardado = true;
        permitirIndex = true;
        getListIndicadores();
        contarRegistros();
        RequestContext context = RequestContext.getCurrentInstance();
        context.update("form:infoRegistro");
        context.update("form:datosIndicadores");
        context.update("form:ACEPTAR");
    }

    public void salir() {  limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosIndicadores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosIndicadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            tipo = (Column) c.getViewRoot().findComponent("form:datosIndicadores:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosIndicadores");
            bandera = 0;
            filtrarListIndicadores = null;
            tipoLista = 0;
        }

        listBorrarIndicadores.clear();
        listCrearIndicadores.clear();
        listModificarIndicadores.clear();
        indicadorSeleccionado = null;
        k = 0;
        listIndicadores = null;
        guardado = true;
        permitirIndex = true;
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosIndicadores");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosIndicadores:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosIndicadores:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            tipo = (Column) c.getViewRoot().findComponent("form:datosIndicadores:tipo");
            tipo.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosIndicadores");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosIndicadores:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosIndicadores:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            tipo = (Column) c.getViewRoot().findComponent("form:datosIndicadores:tipo");
            tipo.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIndicadores");
            bandera = 0;
            filtrarListIndicadores = null;
            tipoLista = 0;
        }
    }

    public void modificarIndicador(Indicadores indicador, String confirmarCambio, String valorConfirmar) {
        indicadorSeleccionado = indicador;
        if (!listCrearIndicadores.contains(indicadorSeleccionado)) {
            if (listModificarIndicadores.isEmpty()) {
                listModificarIndicadores.add(indicadorSeleccionado);
            } else if (!listModificarIndicadores.contains(indicadorSeleccionado)) {
                listModificarIndicadores.add(indicadorSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosIndicadores");
    }

    public void borrandoIndicador() {
        if (indicadorSeleccionado != null) {
            if (!listModificarIndicadores.isEmpty() && listModificarIndicadores.contains(indicadorSeleccionado)) {
                listModificarIndicadores.remove(listModificarIndicadores.indexOf(indicadorSeleccionado));
                listBorrarIndicadores.add(indicadorSeleccionado);
            } else if (!listCrearIndicadores.isEmpty() && listCrearIndicadores.contains(indicadorSeleccionado)) {
                listCrearIndicadores.remove(listCrearIndicadores.indexOf(indicadorSeleccionado));
            } else {
                listBorrarIndicadores.add(indicadorSeleccionado);
            }
            listIndicadores.remove(indicadorSeleccionado);
            if (tipoLista == 1) {
                filtrarListIndicadores.remove(indicadorSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosIndicadores");
            contarRegistros();
            indicadorSeleccionado = null;
            guardado = true;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!listBorrarIndicadores.isEmpty() || !listCrearIndicadores.isEmpty() || !listModificarIndicadores.isEmpty()) {
            RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarIndicador() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                if (!listBorrarIndicadores.isEmpty()) {
                    administrarIndicadores.borrarIndicador(listBorrarIndicadores);
                    registroBorrados = listBorrarIndicadores.size();
                    RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                    RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                    listBorrarIndicadores.clear();
                }
                if (!listModificarIndicadores.isEmpty()) {
                    administrarIndicadores.modificarIndicador(listModificarIndicadores);
                    listModificarIndicadores.clear();
                }
                if (!listCrearIndicadores.isEmpty()) {
                    administrarIndicadores.crearIndicador(listCrearIndicadores);
                    listCrearIndicadores.clear();
                }
                listIndicadores = null;
                getListIndicadores();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                k = 0;
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                indicadorSeleccionado = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosIndicadores");
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void asignarIndex(Indicadores indicador, int dlg, int LND) {
        indicadorSeleccionado = indicador;
        tipoActualizacion = LND;
        if (dlg == 1) {
            getLovTiposIndicadores();
            contarRegistrosTiposIndicadores();
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposIndicadoresDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposIndicadoresDialogo').show()");

        }
    }

    public void listaValorBoton() {
        if (cualCelda == 2) {
            habilitarBotonLov();
            getLovTiposIndicadores();
            contarRegistrosTiposIndicadores();
            RequestContext.getCurrentInstance().update("formularioDialogos:tiposIndicadoresDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposIndicadoresDialogo').show()");
        }
    }

    public void editarCelda() {
        if (indicadorSeleccionado != null) {
            editarIndicador = indicadorSeleccionado;

            RequestContext context = RequestContext.getCurrentInstance();
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editTipo");
                RequestContext.getCurrentInstance().execute("PF('editTipo').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoIndicador() {
        int contador = 0;
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        mensajeValidacion = " ";

        if (nuevoIndicador.getDescripcion().equals(" ") || nuevoIndicador.getDescripcion().equals("")) {
            mensajeValidacion = "El campo descripción es obligatorio";
            contador++;
        }

        for (int i = 0; i < listIndicadores.size(); i++) {
            if (listIndicadores.get(i).getCodigo() == nuevoIndicador.getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                duplicados++;
            }
            if (contador != 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoIndicador");
                RequestContext.getCurrentInstance().execute("PF('validacionNuevoIndicador').show()");

            }
        }

        if (contador == 0 && duplicados == 0) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosIndicadores:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosIndicadores:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                tipo = (Column) c.getViewRoot().findComponent("form:datosIndicadores:tipo");
                tipo.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarListIndicadores = null;
                tipoLista = 0;
                tamano = 270;
                RequestContext.getCurrentInstance().update("form:datosIndicadores");
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoIndicador.setSecuencia(l);
            listCrearIndicadores.add(nuevoIndicador);
            listIndicadores.add(nuevoIndicador);
            contarRegistros();
            indicadorSeleccionado = nuevoIndicador;
            nuevoIndicador = new Indicadores();
            RequestContext.getCurrentInstance().update("form:datosIndicadores");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroIndicadores').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoIndicador");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoIndicador').show()");
        }
    }

    public void limpiarNuevoIndicador() {
        nuevoIndicador = new Indicadores();
        nuevoIndicador.setTipoindicador(new TiposIndicadores());
    }

    public void duplicarIndicadores() {
        if (indicadorSeleccionado != null) {
            duplicarIndicador = new Indicadores();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarIndicador.setSecuencia(l);
                duplicarIndicador.setCodigo(indicadorSeleccionado.getCodigo());
                duplicarIndicador.setDescripcion(indicadorSeleccionado.getDescripcion());
                duplicarIndicador.setTipoindicador(indicadorSeleccionado.getTipoindicador());
            }
            if (tipoLista == 1) {
                duplicarIndicador.setSecuencia(l);
                duplicarIndicador.setCodigo(indicadorSeleccionado.getCodigo());
                duplicarIndicador.setDescripcion(indicadorSeleccionado.getDescripcion());
                duplicarIndicador.setTipoindicador(indicadorSeleccionado.getTipoindicador());
                tamano = 270;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIndicador");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroIndicadores').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        RequestContext context = RequestContext.getCurrentInstance();
        int contador = 0;

        for (int i = 0; i < listIndicadores.size(); i++) {
            if (duplicarIndicador.getCodigo() == listIndicadores.get(i).getCodigo()) {
                RequestContext.getCurrentInstance().update("formularioDialogos:existeCodigo");
                RequestContext.getCurrentInstance().execute("PF('existeCodigo').show()");
                contador++;
            }
        }

        if (contador == 0) {
            listIndicadores.add(duplicarIndicador);
            listCrearIndicadores.add(duplicarIndicador);
            indicadorSeleccionado = duplicarIndicador;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosIndicadores");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosIndicadores:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosIndicadores:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                tipo = (Column) c.getViewRoot().findComponent("form:datosIndicadores:tipo");
                tipo.setFilterStyle("display: none; visibility: hidden;");
                bandera = 0;
                filtrarListIndicadores = null;
                RequestContext.getCurrentInstance().update("form:datosIndicadores");
                tipoLista = 0;
            }
            duplicarIndicador = new Indicadores();
        }
        RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroIndicadores");
        RequestContext.getCurrentInstance().execute("PF('duplicarRegistroIndicadores').hide()");
    }

    public void limpiarDuplicar() {
        duplicarIndicador = new Indicadores();
    }

    public void actualizarTipoIndicador() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (tipoActualizacion == 0) {
            if (tipoLista == 0) {
                indicadorSeleccionado.setTipoindicador(tipoIndicadorSeleccionado);
                if (!listCrearIndicadores.contains(indicadorSeleccionado)) {
                    if (listModificarIndicadores.isEmpty()) {
                        listModificarIndicadores.add(indicadorSeleccionado);
                    } else if (!listModificarIndicadores.contains(indicadorSeleccionado)) {
                        listModificarIndicadores.add(indicadorSeleccionado);
                    }
                }
            } else {
                indicadorSeleccionado.setTipoindicador(tipoIndicadorSeleccionado);
                if (!listCrearIndicadores.contains(indicadorSeleccionado)) {
                    if (listModificarIndicadores.isEmpty()) {
                        listModificarIndicadores.add(indicadorSeleccionado);
                    } else if (!listModificarIndicadores.contains(indicadorSeleccionado)) {
                        listModificarIndicadores.add(indicadorSeleccionado);
                    }
                }
            }
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            permitirIndex = true;
            deshabilitarBotonLov();
            RequestContext.getCurrentInstance().update("form:datosIndicadores");
        } else if (tipoActualizacion == 1) {
            nuevoIndicador.setTipoindicador(tipoIndicadorSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoIndicador");
        } else if (tipoActualizacion == 2) {
            duplicarIndicador.setTipoindicador(tipoIndicadorSeleccionado);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarIndicador");
        }
        filtrarLovTiposIndicadores = null;
        tipoIndicadorSeleccionado = null;
        aceptar = true;
        tipoActualizacion = -1;

        RequestContext.getCurrentInstance().update("formularioDialogos:tiposIndicadoresDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTipoIndicador");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTI");

        context.reset("formularioDialogos:lovTipoIndicador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoIndicador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposIndicadoresDialogo').hide()");
    }

    public void cancelarCambioTipoIndicador() {
        filtrarLovTiposIndicadores = null;
        aceptar = true;
        tipoActualizacion = -1;
        permitirIndex = true;
        tipoIndicadorSeleccionado = null;
        RequestContext context = RequestContext.getCurrentInstance();
        RequestContext.getCurrentInstance().update("formularioDialogos:tiposIndicadoresDialogo");
        RequestContext.getCurrentInstance().update("formularioDialogos:lovTipoIndicador");
        RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTI");

        context.reset("formularioDialogos:lovTipoIndicador:globalFilter");
        RequestContext.getCurrentInstance().execute("PF('lovTipoIndicador').clearFilters()");
        RequestContext.getCurrentInstance().execute("PF('tiposIndicadoresDialogo').hide()");
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIndicadoresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "INDICADORES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIndicadoresExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "INDICADORES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (indicadorSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(indicadorSeleccionado.getSecuencia(), "TIPOSCURSOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
            System.out.println("resultado: " + resultado);
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSCURSOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void recordarSeleccion() {
        if (indicadorSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosIndicadores");
            tablaC.setSelection(indicadorSeleccionado);
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void contarRegistrosTiposIndicadores() {
        RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroTipo");
    }

    public void habilitarBotonLov() {
        activarLov = false;
    }

    public void deshabilitarBotonLov() {
        activarLov = true;
    }

    ///////GETS Y SETS///////////
    public List<Indicadores> getListIndicadores() {
        if (listIndicadores == null) {
            listIndicadores = administrarIndicadores.consultarIndicadores();
        }
        return listIndicadores;
    }

    public void setListIndicadores(List<Indicadores> listIndicadores) {
        this.listIndicadores = listIndicadores;
    }

    public List<TiposIndicadores> getLovTiposIndicadores() {
        if (lovTiposIndicadores == null) {
            lovTiposIndicadores = administrarIndicadores.consultarTiposIndicadores();
        }
        return lovTiposIndicadores;
    }

    public void setLovTiposIndicadores(List<TiposIndicadores> lovTiposIndicadores) {
        this.lovTiposIndicadores = lovTiposIndicadores;
    }

    public List<TiposIndicadores> getFiltrarLovTiposIndicadores() {
        return filtrarLovTiposIndicadores;
    }

    public void setFiltrarLovTiposIndicadores(List<TiposIndicadores> filtrarLovTiposIndicadores) {
        this.filtrarLovTiposIndicadores = filtrarLovTiposIndicadores;
    }

    public TiposIndicadores getTipoIndicadorSeleccionado() {
        return tipoIndicadorSeleccionado;
    }

    public void setTipoIndicadorSeleccionado(TiposIndicadores tipoIndicadorSeleccionado) {
        this.tipoIndicadorSeleccionado = tipoIndicadorSeleccionado;
    }

    public List<Indicadores> getFiltrarListIndicadores() {
        return filtrarListIndicadores;
    }

    public void setFiltrarListIndicadores(List<Indicadores> filtrarListIndicadores) {
        this.filtrarListIndicadores = filtrarListIndicadores;
    }

    public Indicadores getIndicadorSeleccionado() {
        return indicadorSeleccionado;
    }

    public void setIndicadorSeleccionado(Indicadores indicadorSeleccionado) {
        this.indicadorSeleccionado = indicadorSeleccionado;
    }

    public Indicadores getNuevoIndicador() {
        return nuevoIndicador;
    }

    public void setNuevoIndicador(Indicadores nuevoIndicador) {
        this.nuevoIndicador = nuevoIndicador;
    }

    public Indicadores getEditarIndicador() {
        return editarIndicador;
    }

    public void setEditarIndicador(Indicadores editarIndicador) {
        this.editarIndicador = editarIndicador;
    }

    public Indicadores getDuplicarIndicador() {
        return duplicarIndicador;
    }

    public void setDuplicarIndicador(Indicadores duplicarIndicador) {
        this.duplicarIndicador = duplicarIndicador;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
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

    public int getRegistroBorrados() {
        return registroBorrados;
    }

    public void setRegistroBorrados(int registroBorrados) {
        this.registroBorrados = registroBorrados;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosIndicadores");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroTipo() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovTipoIndicador");
        infoRegistroTipo = String.valueOf(tabla.getRowCount());
        return infoRegistroTipo;
    }

    public void setInfoRegistroTipo(String infoRegistroTipo) {
        this.infoRegistroTipo = infoRegistroTipo;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
