/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.TiposCertificados;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarTiposCertificadosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
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
public class ControlTiposCertificados implements Serializable {

    @EJB
    AdministrarTiposCertificadosInterface administrarTiposCertificados;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<TiposCertificados> listTiposCertificados;
    private List<TiposCertificados> filtrarTiposCertificados;
    private List<TiposCertificados> crearTiposCertificados;
    private List<TiposCertificados> modificarTiposCertificados;
    private List<TiposCertificados> borrarTiposCertificados;
    private TiposCertificados nuevoTiposCertificados;
    private TiposCertificados duplicarTiposCertificados;
    private TiposCertificados editarTiposCertificados;
    private TiposCertificados tiposCertificadoSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private String infoRegistro;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlTiposCertificados() {
        listTiposCertificados = null;
        crearTiposCertificados = new ArrayList<TiposCertificados>();
        modificarTiposCertificados = new ArrayList<TiposCertificados>();
        borrarTiposCertificados = new ArrayList<TiposCertificados>();
        permitirIndex = true;
        tiposCertificadoSeleccionado = null;
        editarTiposCertificados = new TiposCertificados();
        nuevoTiposCertificados = new TiposCertificados();
        duplicarTiposCertificados = new TiposCertificados();
        guardado = true;
        tamano = 330;
        System.out.println("controlTiposCertificados Constructor");
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

   public void limpiarListasValor() {

   }

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            System.out.println("ControlTiposCertificados PostConstruct ");
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarTiposCertificados.obtenerConexion(ses.getId());
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
         System.out.println("navegar('Atras') : " + pag);
        } else {
            String pagActual = "tipocertificado";
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

    public String redirigirPaginaAnterior() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        tiposCertificadoSeleccionado = null;
        contarRegistros();
    }

    public void cambiarIndice(TiposCertificados tc, int celda) {
        tiposCertificadoSeleccionado = tc;
        if (permitirIndex == true) {
            cualCelda = celda;
            if (tipoLista == 0) {
                if (cualCelda == 0) {
                    backUpCodigo = tiposCertificadoSeleccionado.getCodigo();
                } else if (cualCelda == 1) {
                    backUpDescripcion = tiposCertificadoSeleccionado.getDescripcion();
                }
            }
        }
    }

    public void asignarIndex(TiposCertificados tc, int LND, int dig) {
        tiposCertificadoSeleccionado = tc;
        tipoActualizacion = LND;
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
            bandera = 0;
            filtrarTiposCertificados = null;
            tipoLista = 0;
            tamano = 330;
        }
        borrarTiposCertificados.clear();
        crearTiposCertificados.clear();
        modificarTiposCertificados.clear();
        tiposCertificadoSeleccionado = null;
        k = 0;
        listTiposCertificados = null;
        guardado = true;
        permitirIndex = true;
        getListTiposCertificados();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {  limpiarListasValor();
        if (bandera == 1) {
            //CERRAR FILTRADO
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
            bandera = 0;
            filtrarTiposCertificados = null;
            tipoLista = 0;
            tamano = 330;
        }
        borrarTiposCertificados.clear();
        crearTiposCertificados.clear();
        modificarTiposCertificados.clear();
        tiposCertificadoSeleccionado = null;
        k = 0;
        listTiposCertificados = null;
        guardado = true;
        permitirIndex = true;
        RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 310;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 330;
            codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
            bandera = 0;
            filtrarTiposCertificados = null;
            tipoLista = 0;
        }
    }

    public void modificarTiposCertificados(TiposCertificados tc) {
        tiposCertificadoSeleccionado = tc;
        if (!crearTiposCertificados.contains(tiposCertificadoSeleccionado)) {
            if (modificarTiposCertificados.isEmpty()) {
                modificarTiposCertificados.add(tiposCertificadoSeleccionado);
            } else if (!modificarTiposCertificados.contains(tiposCertificadoSeleccionado)) {
                modificarTiposCertificados.add(tiposCertificadoSeleccionado);
            }
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
    }

    public void borrandoTiposCertificados() {
        if (tiposCertificadoSeleccionado != null) {
            if (!modificarTiposCertificados.isEmpty() && modificarTiposCertificados.contains(tiposCertificadoSeleccionado)) {
                modificarTiposCertificados.remove(tiposCertificadoSeleccionado);
                borrarTiposCertificados.add(tiposCertificadoSeleccionado);
            } else if (!crearTiposCertificados.isEmpty() && crearTiposCertificados.contains(tiposCertificadoSeleccionado)) {
                crearTiposCertificados.remove(tiposCertificadoSeleccionado);
            } else {
                borrarTiposCertificados.add(tiposCertificadoSeleccionado);
            }
            listTiposCertificados.remove(tiposCertificadoSeleccionado);
            if (tipoLista == 1) {
                filtrarTiposCertificados.remove(tiposCertificadoSeleccionado);
            }
            RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
            contarRegistros();
            tiposCertificadoSeleccionado = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarTiposCertificados.isEmpty() || !crearTiposCertificados.isEmpty() || !modificarTiposCertificados.isEmpty()) {
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarTiposCertificados() {
        if (guardado == false) {
            System.out.println("Realizando guardarTiposCertificados");
            if (!borrarTiposCertificados.isEmpty()) {
                administrarTiposCertificados.borrarTiposCertificados(borrarTiposCertificados);
                //mostrarBorrados
                borrarTiposCertificados.clear();
            }
            if (!modificarTiposCertificados.isEmpty()) {
                administrarTiposCertificados.modificarTiposCertificados(modificarTiposCertificados);
                modificarTiposCertificados.clear();
            }
            if (!crearTiposCertificados.isEmpty()) {
                administrarTiposCertificados.crearTiposCertificados(crearTiposCertificados);
                crearTiposCertificados.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listTiposCertificados = null;
            RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
            k = 0;
            guardado = true;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void editarCelda() {
        if (tiposCertificadoSeleccionado != null) {
            editarTiposCertificados = tiposCertificadoSeleccionado;
            if (cualCelda == 0) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
                RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
                cualCelda = -1;
            } else if (cualCelda == 1) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcion");
                RequestContext.getCurrentInstance().execute("PF('editDescripcion').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoTiposCertificados() {
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        if (nuevoTiposCertificados.getCodigo() == a) {
            mensajeValidacion = " *Codigo \n";
        } else {
            for (int x = 0; x < listTiposCertificados.size(); x++) {
                if (listTiposCertificados.get(x).getCodigo() == nuevoTiposCertificados.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Hayan Codigos Repetidos \n";
            } else {
                contador++;
            }
        }
        if (nuevoTiposCertificados.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else if (nuevoTiposCertificados.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            System.out.println("bandera");
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
                bandera = 0;
                filtrarTiposCertificados = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoTiposCertificados.setSecuencia(l);
            crearTiposCertificados.add(nuevoTiposCertificados);
            listTiposCertificados.add(nuevoTiposCertificados);
            tiposCertificadoSeleccionado = listTiposCertificados.get(listTiposCertificados.indexOf(nuevoTiposCertificados));
            nuevoTiposCertificados = new TiposCertificados();
            RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
            contarRegistros();

            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroTiposCertificados').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoTiposCertificados() {
        System.out.println("limpiarNuevoTiposCertificados");
        nuevoTiposCertificados = new TiposCertificados();
    }

    //------------------------------------------------------------------------------
    public void duplicandoTiposCertificados() {
        if (tiposCertificadoSeleccionado != null) {
            duplicarTiposCertificados = new TiposCertificados();
            k++;
            l = BigInteger.valueOf(k);
            duplicarTiposCertificados.setSecuencia(l);
            duplicarTiposCertificados.setCodigo(tiposCertificadoSeleccionado.getCodigo());
            duplicarTiposCertificados.setDescripcion(tiposCertificadoSeleccionado.getDescripcion());
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposCertificados').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        System.err.println("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        Integer a = 0;
        a = null;
        System.err.println("ConfirmarDuplicar codigo " + duplicarTiposCertificados.getCodigo());
        System.err.println("ConfirmarDuplicar Descripcion " + duplicarTiposCertificados.getDescripcion());

        if (duplicarTiposCertificados.getCodigo() == a) {
            mensajeValidacion = mensajeValidacion + "   *Codigo \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listTiposCertificados.size(); x++) {
                if (listTiposCertificados.get(x).getCodigo() == duplicarTiposCertificados.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " *Que NO Existan Codigo Repetidos \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarTiposCertificados.getDescripcion() == null) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
        } else if (duplicarTiposCertificados.getDescripcion().isEmpty()) {
            mensajeValidacion = mensajeValidacion + " *Nombre \n";
        } else {
            contador++;
        }

        if (contador == 2) {
            listTiposCertificados.add(duplicarTiposCertificados);
            crearTiposCertificados.add(duplicarTiposCertificados);
            tiposCertificadoSeleccionado = listTiposCertificados.get(listTiposCertificados.indexOf(duplicarTiposCertificados));
            RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();

            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosTiposCertificados:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosTiposCertificados");
                bandera = 0;
                filtrarTiposCertificados = null;
                tipoLista = 0;
            }
            duplicarTiposCertificados = new TiposCertificados();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroTiposCertificados').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    public void limpiarDuplicarTiposCertificados() {
        duplicarTiposCertificados = new TiposCertificados();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCertificadosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "TIPOSCERTIFICADOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposCertificadosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "TIPOSCERTIFICADOS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        System.out.println("lol");
        if (tiposCertificadoSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(tiposCertificadoSeleccionado.getSecuencia(), "TIPOSCERTIFICADOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("TIPOSCERTIFICADOS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }
    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/

    public List<TiposCertificados> getListTiposCertificados() {
        if (listTiposCertificados == null) {
            listTiposCertificados = administrarTiposCertificados.consultarTiposCertificados();
        }
        return listTiposCertificados;
    }

    public void setListTiposCertificados(List<TiposCertificados> listTiposCertificados) {
        this.listTiposCertificados = listTiposCertificados;
    }

    public List<TiposCertificados> getFiltrarTiposCertificados() {
        return filtrarTiposCertificados;
    }

    public void setFiltrarTiposCertificados(List<TiposCertificados> filtrarTiposCertificados) {
        this.filtrarTiposCertificados = filtrarTiposCertificados;
    }

    public TiposCertificados getNuevoTiposCertificados() {
        return nuevoTiposCertificados;
    }

    public void setNuevoTiposCertificados(TiposCertificados nuevoTiposCertificados) {
        this.nuevoTiposCertificados = nuevoTiposCertificados;
    }

    public TiposCertificados getDuplicarTiposCertificados() {
        return duplicarTiposCertificados;
    }

    public void setDuplicarTiposCertificados(TiposCertificados duplicarTiposCertificados) {
        this.duplicarTiposCertificados = duplicarTiposCertificados;
    }

    public TiposCertificados getEditarTiposCertificados() {
        return editarTiposCertificados;
    }

    public void setEditarTiposCertificados(TiposCertificados editarTiposCertificados) {
        this.editarTiposCertificados = editarTiposCertificados;
    }

    public String getMensajeValidacion() {
        return mensajeValidacion;
    }

    public void setMensajeValidacion(String mensajeValidacion) {
        this.mensajeValidacion = mensajeValidacion;
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public TiposCertificados getTiposCertificadoSeleccionado() {
        return tiposCertificadoSeleccionado;
    }

    public void setTiposCertificadoSeleccionado(TiposCertificados clasesPensionesSeleccionado) {
        this.tiposCertificadoSeleccionado = clasesPensionesSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposCertificados");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

}
