/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.OpcionesKioskos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarOpcionesKioskosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
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
@Named(value = "controlOpcionesKioscos")
@SessionScoped
public class ControlOpcionesKioscos implements Serializable {

    @EJB
    AdministrarOpcionesKioskosInterface adminstrarOpciones;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<OpcionesKioskos> listOpciones;
    private List<OpcionesKioskos> listOpcionesFiltrar;
    private OpcionesKioskos editarOpcion;
    private OpcionesKioskos opcionSeleccionada;
    private List<OpcionesKioskos> lovOpciones;
    private List<OpcionesKioskos> lovOpcionesFiltrar;
    private OpcionesKioskos lovOpcionSeleccionada;
    private int cualCelda, tipoLista, bandera;
    private boolean aceptar, guardado;
    private Column codigo, descripcion, empresa, ayuda, clase, reporte, archivo, extension, padre;
    private int tamano;
    private boolean activarLov;
    private String infoRegistro, infoRegistroLov;
    private DataTable tablaC;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlOpcionesKioscos() {
        listOpciones = null;
        editarOpcion = new OpcionesKioskos();
        lovOpciones = null;
        guardado = true;
        tamano = 330;
        cualCelda = -1;
        opcionSeleccionada = null;
        lovOpcionSeleccionada = null;
        activarLov = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            adminstrarOpciones.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listOpciones = null;
        getListOpciones();
        if (listOpciones != null) {
            opcionSeleccionada = listOpciones.get(0);
        }
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listOpciones = null;
        getListOpciones();
        if (listOpciones != null) {
            opcionSeleccionada = listOpciones.get(0);
        }
    }

    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "opckiosko";
        if (pag.equals("atras")) {
            pag = paginaAnterior;
            paginaAnterior = "nominaf";
            controlListaNavegacion.quitarPagina(pagActual);
        } else {
            controlListaNavegacion.guardarNavegacion(pagActual, pag);
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
//Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
            //mapParaEnviar.put("paginaAnterior", pagActual);
            //mas Parametros
//         if (pag.equals("rastrotabla")) {
//           ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //           controlRastro.recibirDatosTabla(conceptoSeleccionado.getSecuencia(), "Conceptos", pagActual);
            //      } else if (pag.equals("rastrotablaH")) {
            //       ControlRastro controlRastro = (ControlRastro) fc.getApplication().evaluateExpressionGet(fc, "#{controlRastro}", ControlRastro.class);
            //     controlRastro.historicosTabla("Conceptos", pagActual);
            //   pag = "rastrotabla";
            //}
        }
        limpiarListasValor();
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void cambiarIndice(OpcionesKioskos opcion, int celda) {
        opcionSeleccionada = opcion;
        cualCelda = celda;
        opcionSeleccionada.getSecuencia();
        if (cualCelda == 0) {
            opcionSeleccionada.getCodigo();
        } else if (cualCelda == 1) {
            opcionSeleccionada.getDescripcion();
        } else if (cualCelda == 2) {
            opcionSeleccionada.getEmpresa().getNombre();
        } else if (cualCelda == 3) {
            opcionSeleccionada.getAyuda();
        } else if (cualCelda == 5) {
            opcionSeleccionada.getTiporeporte();
        } else if (cualCelda == 6) {
            opcionSeleccionada.getNombrearchivo();
        } else if (cualCelda == 7) {
            opcionSeleccionada.getExtension();
        } else if (cualCelda == 8) {
            opcionSeleccionada.getOpcionkioskopadre().getDescripcion();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosOpciones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosOpciones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) c.getViewRoot().findComponent("form:datosOpciones:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            ayuda = (Column) c.getViewRoot().findComponent("form:datosOpciones:ayuda");
            ayuda.setFilterStyle("display: none; visibility: hidden;");
            clase = (Column) c.getViewRoot().findComponent("form:datosOpciones:clase");
            clase.setFilterStyle("display: none; visibility: hidden;");
            reporte = (Column) c.getViewRoot().findComponent("form:datosOpciones:reporte");
            reporte.setFilterStyle("display: none; visibility: hidden;");
            archivo = (Column) c.getViewRoot().findComponent("form:datosOpciones:archivo");
            archivo.setFilterStyle("display: none; visibility: hidden;");
            extension = (Column) c.getViewRoot().findComponent("form:datosOpciones:extension");
            extension.setFilterStyle("display: none; visibility: hidden;");
            padre = (Column) c.getViewRoot().findComponent("form:datosOpciones:padre");
            padre.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listOpcionesFiltrar = null;
            tamano = 330;
            RequestContext.getCurrentInstance().update("form:datosOpciones");
            tipoLista = 0;
        }
        opcionSeleccionada = null;
        contarRegistros();
        listOpciones = null;
        guardado = true;
        RequestContext.getCurrentInstance().update("form:datosOpciones");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosOpciones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosOpciones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) c.getViewRoot().findComponent("form:datosOpciones:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            ayuda = (Column) c.getViewRoot().findComponent("form:datosOpciones:ayuda");
            ayuda.setFilterStyle("display: none; visibility: hidden;");
            clase = (Column) c.getViewRoot().findComponent("form:datosOpciones:clase");
            clase.setFilterStyle("display: none; visibility: hidden;");
            reporte = (Column) c.getViewRoot().findComponent("form:datosOpciones:reporte");
            reporte.setFilterStyle("display: none; visibility: hidden;");
            archivo = (Column) c.getViewRoot().findComponent("form:datosOpciones:archivo");
            archivo.setFilterStyle("display: none; visibility: hidden;");
            extension = (Column) c.getViewRoot().findComponent("form:datosOpciones:extension");
            extension.setFilterStyle("display: none; visibility: hidden;");
            padre = (Column) c.getViewRoot().findComponent("form:datosOpciones:padre");
            padre.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosOpciones");
            bandera = 0;
            listOpcionesFiltrar = null;
            tipoLista = 0;
            tamano = 330;
        }
        opcionSeleccionada = null;
        listOpciones = null;
        guardado = true;
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            codigo = (Column) c.getViewRoot().findComponent("form:datosOpciones:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosOpciones:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            empresa = (Column) c.getViewRoot().findComponent("form:datosOpciones:empresa");
            empresa.setFilterStyle("width: 85% !important;");
            ayuda = (Column) c.getViewRoot().findComponent("form:datosOpciones:ayuda");
            ayuda.setFilterStyle("width: 85% !important;");
            clase = (Column) c.getViewRoot().findComponent("form:datosOpciones:clase");
            clase.setFilterStyle("width: 85% !important;");
            reporte = (Column) c.getViewRoot().findComponent("form:datosOpciones:reporte");
            reporte.setFilterStyle("width: 85% !important;");
            archivo = (Column) c.getViewRoot().findComponent("form:datosOpciones:archivo");
            archivo.setFilterStyle("width: 85% !important;");
            extension = (Column) c.getViewRoot().findComponent("form:datosOpciones:extension");
            extension.setFilterStyle("width: 85% !important;");
            padre = (Column) c.getViewRoot().findComponent("form:datosOpciones:padre");
            padre.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosOpciones");
            bandera = 1;
            tamano = 310;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 330;
            codigo = (Column) c.getViewRoot().findComponent("form:datosOpciones:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosOpciones:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            empresa = (Column) c.getViewRoot().findComponent("form:datosOpciones:empresa");
            empresa.setFilterStyle("display: none; visibility: hidden;");
            ayuda = (Column) c.getViewRoot().findComponent("form:datosOpciones:ayuda");
            ayuda.setFilterStyle("display: none; visibility: hidden;");
            clase = (Column) c.getViewRoot().findComponent("form:datosOpciones:clase");
            clase.setFilterStyle("display: none; visibility: hidden;");
            reporte = (Column) c.getViewRoot().findComponent("form:datosOpciones:reporte");
            reporte.setFilterStyle("display: none; visibility: hidden;");
            archivo = (Column) c.getViewRoot().findComponent("form:datosOpciones:archivo");
            archivo.setFilterStyle("display: none; visibility: hidden;");
            extension = (Column) c.getViewRoot().findComponent("form:datosOpciones:extension");
            extension.setFilterStyle("display: none; visibility: hidden;");
            padre = (Column) c.getViewRoot().findComponent("form:datosOpciones:padre");
            padre.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosOpciones");
            bandera = 0;
            listOpcionesFiltrar = null;
            tipoLista = 0;
        }
    }

    public void guardarCambios() {
        RequestContext context = RequestContext.getCurrentInstance();
        try {
            if (guardado == false) {
                listOpciones = null;
                getListOpciones();
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
                FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                RequestContext.getCurrentInstance().update("form:growl");
                contarRegistros();
                opcionSeleccionada = null;
            }
            guardado = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosOpciones");
        } catch (Exception e) {
            System.out.println("Error guardarCambios : " + e.toString());
            FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }

    }

    public void editarCelda() {
        if (opcionSeleccionada != null) {
            editarOpcion = opcionSeleccionada;

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
                RequestContext.getCurrentInstance().update("formularioDialogos:editEmpresa");
                RequestContext.getCurrentInstance().execute("PF('editEmpresa').show()");
                cualCelda = -1;
            } else if (cualCelda == 3) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editAyuda");
                RequestContext.getCurrentInstance().execute("PF('editAyuda').show()");
                cualCelda = -1;
            } else if (cualCelda == 5) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editReporte");
                RequestContext.getCurrentInstance().execute("PF('editReporte').show()");
                cualCelda = -1;
            } else if (cualCelda == 6) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editArchivo");
                RequestContext.getCurrentInstance().execute("PF('editArchivo').show()");
                cualCelda = -1;
            } else if (cualCelda == 7) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editExtension");
                RequestContext.getCurrentInstance().execute("PF('editExtension').show()");
                cualCelda = -1;
            } else if (cualCelda == 8) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editPadre");
                RequestContext.getCurrentInstance().execute("PF('editPadre').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosOpcionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "OpcionesKioskos", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosOpcionesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "OpcionesKioskos", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (opcionSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(opcionSeleccionada.getSecuencia(), "OPCIONESKIOSKOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("OPCIONESKIOSKOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void limpiarExportar() {
        opcionSeleccionada = new OpcionesKioskos();
    }

    public void revisarDialogoGuardar() {
        RequestContext.getCurrentInstance().update("form:confirmarGuardar");
        RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
    }

    public void posicionCelda(){
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> map = context.getExternalContext().getRequestParameterMap();
        String name = map.get("n"); // name attribute of node
        String type = map.get("t"); // type attribute of node
        int indice = Integer.parseInt(type);
        int columna = Integer.parseInt(name);
        opcionSeleccionada = listOpciones.get(indice);
        cualCelda = columna;
        cambiarIndice(opcionSeleccionada, cualCelda);
    }
    
    
    ///SETS Y GETS////////
    public List<OpcionesKioskos> getListOpciones() {
        if (listOpciones == null) {
            listOpciones = adminstrarOpciones.consultarOpcionesKioskos();
        }
        return listOpciones;
    }

    public void setListOpciones(List<OpcionesKioskos> listOpciones) {
        this.listOpciones = listOpciones;
    }

    public List<OpcionesKioskos> getListOpcionesFiltrar() {
        return listOpcionesFiltrar;
    }

    public void setListOpcionesFiltrar(List<OpcionesKioskos> listOpcionesFiltrar) {
        this.listOpcionesFiltrar = listOpcionesFiltrar;
    }

    public OpcionesKioskos getEditarOpcion() {
        return editarOpcion;
    }

    public void setEditarOpcion(OpcionesKioskos editarOpcion) {
        this.editarOpcion = editarOpcion;
    }

    public OpcionesKioskos getOpcionSeleccionada() {
        return opcionSeleccionada;
    }

    public void setOpcionSeleccionada(OpcionesKioskos opcionSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
    }

    public List<OpcionesKioskos> getLovOpciones() {
        return lovOpciones;
    }

    public void setLovOpciones(List<OpcionesKioskos> lovOpciones) {
        this.lovOpciones = lovOpciones;
    }

    public List<OpcionesKioskos> getLovOpcionesFiltrar() {
        return lovOpcionesFiltrar;
    }

    public void setLovOpcionesFiltrar(List<OpcionesKioskos> lovOpcionesFiltrar) {
        this.lovOpcionesFiltrar = lovOpcionesFiltrar;
    }

    public OpcionesKioskos getLovOpcionSeleccionada() {
        return lovOpcionSeleccionada;
    }

    public void setLovOpcionSeleccionada(OpcionesKioskos lovOpcionSeleccionada) {
        this.lovOpcionSeleccionada = lovOpcionSeleccionada;
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

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosOpciones");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getInfoRegistroLov() {
        return infoRegistroLov;
    }

    public void setInfoRegistroLov(String infoRegistroLov) {
        this.infoRegistroLov = infoRegistroLov;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

}
