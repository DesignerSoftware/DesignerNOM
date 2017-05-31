/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.EstadosCiviles;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarEstadosCivilesInterface;
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
public class ControlEstadosCiviles implements Serializable {

    @EJB
    AdministrarEstadosCivilesInterface administrarEstadosCiviles;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<EstadosCiviles> listEstadosCiviles;
    private List<EstadosCiviles> filtrarEstadosCiviles;
    private List<EstadosCiviles> crearEstadosCiviles;
    private List<EstadosCiviles> modificarEstadosCiviles;
    private List<EstadosCiviles> borrarEstadosCiviles;
    private EstadosCiviles nuevoEstadoCivil;
    private EstadosCiviles duplicarEstadoCivil;
    private EstadosCiviles editarEstadoCivil;
    private EstadosCiviles estadoCivilSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger vigenciasEstadosAficilaciones;
    private Integer a;
    private int tamano;
    private Integer backUpCodigo;
    private String backUpDescripcion;
    private DataTable tablaC;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
    private String infoRegistro;

    public ControlEstadosCiviles() {
        listEstadosCiviles = null;
        crearEstadosCiviles = new ArrayList<EstadosCiviles>();
        modificarEstadosCiviles = new ArrayList<EstadosCiviles>();
        borrarEstadosCiviles = new ArrayList<EstadosCiviles>();
        permitirIndex = true;
        editarEstadoCivil = new EstadosCiviles();
        nuevoEstadoCivil = new EstadosCiviles();
        duplicarEstadoCivil = new EstadosCiviles();
        a = null;
        guardado = true;
        tamano = 270;
        mapParametros.put("paginaAnterior", paginaAnterior);
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
        String pagActual = "estadocivil";
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

    public void limpiarListasValor() {

    }

    @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEstadosCiviles.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPag(String pagina) {
        paginaAnterior = pagina;
        listEstadosCiviles = null;
        getListEstadosCiviles();
        if (listEstadosCiviles != null) {
            if (!listEstadosCiviles.isEmpty()) {
                estadoCivilSeleccionado = listEstadosCiviles.get(0);
            }
        }
    }

    public String retornarPagina() {
        return paginaAnterior;
    }

    public void cambiarIndice(EstadosCiviles estadoCivil, int celda) {
        if (permitirIndex == true) {
            estadoCivilSeleccionado = estadoCivil;
            cualCelda = celda;
            if (cualCelda == 0) {
                backUpCodigo = estadoCivilSeleccionado.getCodigo();
            }
            if (cualCelda == 1) {
                backUpDescripcion = estadoCivilSeleccionado.getDescripcion();
            }
            estadoCivilSeleccionado.getSecuencia();
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            bandera = 0;
            filtrarEstadosCiviles = null;
            tipoLista = 0;
        }

        borrarEstadosCiviles.clear();
        crearEstadosCiviles.clear();
        modificarEstadosCiviles.clear();
        k = 0;
        listEstadosCiviles = null;
        estadoCivilSeleccionado = null;
        getListEstadosCiviles();
        contarRegistros();
        guardado = true;
        permitirIndex = true;
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:infoRegistro");
        RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            bandera = 0;
            filtrarEstadosCiviles = null;
            tipoLista = 0;
        }

        borrarEstadosCiviles.clear();
        crearEstadosCiviles.clear();
        modificarEstadosCiviles.clear();
        estadoCivilSeleccionado = null;
        k = 0;
        listEstadosCiviles = null;
        guardado = true;
        permitirIndex = true;
        getListEstadosCiviles();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        context.update("form:infoRegistro");
        context.update("form:datosEstadosCiviles");
        context.update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            tamano = 270;
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            bandera = 0;
            filtrarEstadosCiviles = null;
            tipoLista = 0;
        }
    }

    public void modificarEstadoCivil(EstadosCiviles estadoCivil) {
        estadoCivilSeleccionado = estadoCivil;
        if (!crearEstadosCiviles.contains(estadoCivilSeleccionado)) {
            if (modificarEstadosCiviles.isEmpty()) {
                modificarEstadosCiviles.add(estadoCivilSeleccionado);
            } else if (!modificarEstadosCiviles.contains(estadoCivilSeleccionado)) {
                modificarEstadosCiviles.add(estadoCivilSeleccionado);
            }
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
        RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
    }

    public void borrandoEstadoCivil() {

        if (estadoCivilSeleccionado != null) {
            if (!modificarEstadosCiviles.isEmpty() && modificarEstadosCiviles.contains(estadoCivilSeleccionado)) {
                modificarEstadosCiviles.remove(modificarEstadosCiviles.indexOf(estadoCivilSeleccionado));
                borrarEstadosCiviles.add(estadoCivilSeleccionado);
            } else if (!crearEstadosCiviles.isEmpty() && crearEstadosCiviles.contains(estadoCivilSeleccionado)) {
                crearEstadosCiviles.remove(crearEstadosCiviles.indexOf(estadoCivilSeleccionado));
            } else {
                borrarEstadosCiviles.add(estadoCivilSeleccionado);
            }
            listEstadosCiviles.remove(estadoCivilSeleccionado);
            if (tipoLista == 1) {
                filtrarEstadosCiviles.remove(estadoCivilSeleccionado);
            }
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:infoRegistro");
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF(seleccionarRegistro').show()");
        }

    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        try {
            System.err.println("Control Secuencia de EstadosCiviles a borrar");
            vigenciasEstadosAficilaciones = administrarEstadosCiviles.verificarVigenciasEstadosCiviles(estadoCivilSeleccionado.getSecuencia());

            if (!vigenciasEstadosAficilaciones.equals(new BigInteger("0"))) {
                System.out.println("Borrado>0");

                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                estadoCivilSeleccionado = null;

                vigenciasEstadosAficilaciones = new BigInteger("-1");

            } else {
                System.out.println("Borrado==0");
                borrandoEstadoCivil();
            }
        } catch (Exception e) {
            System.err.println("ERROR ControlEstadosCiviles verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarEstadosCiviles.isEmpty() || !crearEstadosCiviles.isEmpty() || !modificarEstadosCiviles.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarEstadoCivil() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando EstadosCiviles");
            if (!borrarEstadosCiviles.isEmpty()) {
                administrarEstadosCiviles.borrarEstadosCiviles(borrarEstadosCiviles);
                //mostrarBorrados
                registrosBorrados = borrarEstadosCiviles.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarEstadosCiviles.clear();
            }
            if (!crearEstadosCiviles.isEmpty()) {
                administrarEstadosCiviles.crearEstadosCiviles(crearEstadosCiviles);
                crearEstadosCiviles.clear();
            }
            if (!modificarEstadosCiviles.isEmpty()) {
                administrarEstadosCiviles.modificarEstadosCiviles(modificarEstadosCiviles);
                modificarEstadosCiviles.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listEstadosCiviles = null;
            getListEstadosCiviles();
            contarRegistros();
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            k = 0;
            guardado = true;
        }
        estadoCivilSeleccionado = null;
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (estadoCivilSeleccionado != null) {
            if (tipoLista == 0) {
                editarEstadoCivil = estadoCivilSeleccionado;
            }
            if (tipoLista == 1) {
                editarEstadoCivil = estadoCivilSeleccionado;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            System.out.println("Entro a editar... valor celda: " + cualCelda);
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
            RequestContext.getCurrentInstance().execute("PF(seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoEstadoCivil() {
        int contador = 0;
        int duplicados = 0;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoEstadoCivil.getCodigo() == a) {
            mensajeValidacion = " Campo código vacío \n";
        } else {
            for (int x = 0; x < listEstadosCiviles.size(); x++) {
                if (listEstadosCiviles.get(x).getCodigo().equals(nuevoEstadoCivil.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El Código ingresado está relacionado con un registro anterior \n";
            } else {
                contador++;
            }
        }
        if (nuevoEstadoCivil.getDescripcion() == (null)) {
            mensajeValidacion = mensajeValidacion + " Campo Descripción vacío \n";
        } else {
            System.out.println("bandera");
            contador++;
        }

        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
                bandera = 0;
                filtrarEstadosCiviles = null;
                tipoLista = 0;
                tamano = 270;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoEstadoCivil.setSecuencia(l);
            crearEstadosCiviles.add(nuevoEstadoCivil);
            listEstadosCiviles.add(nuevoEstadoCivil);
            estadoCivilSeleccionado = nuevoEstadoCivil;
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            nuevoEstadoCivil = new EstadosCiviles();
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEstadoCivil').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoEstadoCivil");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoEstadoCivil').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoEstadoCivil() {
        System.out.println("limpiarNuevoEstadosCiviles");
        nuevoEstadoCivil = new EstadosCiviles();

    }

    //------------------------------------------------------------------------------
    public void duplicarEstadosCiviles() {
        System.out.println("duplicarEstadosCiviles");
        if (estadoCivilSeleccionado != null) {
            duplicarEstadoCivil = new EstadosCiviles();
            k++;
            l = BigInteger.valueOf(k);

            if (tipoLista == 0) {
                duplicarEstadoCivil.setSecuencia(l);
                duplicarEstadoCivil.setCodigo(estadoCivilSeleccionado.getCodigo());
                duplicarEstadoCivil.setDescripcion(estadoCivilSeleccionado.getDescripcion());
            }
            if (tipoLista == 1) {
                duplicarEstadoCivil.setSecuencia(l);
                duplicarEstadoCivil.setCodigo(estadoCivilSeleccionado.getCodigo());
                duplicarEstadoCivil.setDescripcion(estadoCivilSeleccionado.getDescripcion());
                tamano = 270;
            }

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEC");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEstadoCivil').show()");

        } else {
            RequestContext.getCurrentInstance().execute("PF(seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarEstadoCivil.getCodigo() == a) {
            mensajeValidacion = "Campo Código vacío \n";
        } else {
            for (int x = 0; x < listEstadosCiviles.size(); x++) {
                if (listEstadosCiviles.get(x).getCodigo().equals(duplicarEstadoCivil.getCodigo())) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = " El Código ingresado está relacionado con un registro anterior \n";
            } else {
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarEstadoCivil.getDescripcion() == null || duplicarEstadoCivil.getDescripcion().isEmpty()) {
            mensajeValidacion = " Campo Descripción vacío \n";

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {

            if (crearEstadosCiviles.contains(duplicarEstadoCivil)) {
            }
            listEstadosCiviles.add(duplicarEstadoCivil);
            crearEstadosCiviles.add(duplicarEstadoCivil);
            estadoCivilSeleccionado = duplicarEstadoCivil;
            RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
            contarRegistros();
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosEstadosCiviles:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEstadosCiviles");
                bandera = 0;
                filtrarEstadosCiviles = null;
                tipoLista = 0;
            }
            duplicarEstadoCivil = new EstadosCiviles();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEstadoCivil').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarEstadosCiviles() {
        duplicarEstadoCivil = new EstadosCiviles();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEstadosCivilesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "ESTADOSCIVILES", false, false, "UTF-8", null, null);
        context.responseComplete();
        estadoCivilSeleccionado = null;
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEstadosCivilesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "ESTADOSCIVILES", false, false, "UTF-8", null, null);
        context.responseComplete();
        estadoCivilSeleccionado = null;
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        System.out.println("lol");
        if (estadoCivilSeleccionado != null) {
            System.out.println("lol 2");
            int resultado = administrarRastros.obtenerTabla(estadoCivilSeleccionado.getSecuencia(), "ESTADOSCIVILES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("ESTADOSCIVILES")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void eventoFiltrar() {
        try {
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR ControlEstadosCiviles eventoFiltrar ERROR== " + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:infoRegistro");
    }

    public void recordarSeleccion() {
        if (estadoCivilSeleccionado != null) {
            FacesContext c = FacesContext.getCurrentInstance();
            tablaC = (DataTable) c.getViewRoot().findComponent("form:datosEstadosCiviles");
            tablaC.setSelection(estadoCivilSeleccionado);
        }
    }
    //////////* SETS Y GETS*/////////////////////////////////

    public List<EstadosCiviles> getListEstadosCiviles() {
        if (listEstadosCiviles == null) {
            listEstadosCiviles = administrarEstadosCiviles.consultarEstadosCiviles();
        }
        return listEstadosCiviles;
    }

    public void setListEstadosCiviles(List<EstadosCiviles> listEstadosCiviles) {
        this.listEstadosCiviles = listEstadosCiviles;
    }

    public List<EstadosCiviles> getFiltrarEstadosCiviles() {
        return filtrarEstadosCiviles;
    }

    public void setFiltrarEstadosCiviles(List<EstadosCiviles> filtrarEstadosCiviles) {
        this.filtrarEstadosCiviles = filtrarEstadosCiviles;
    }

    public List<EstadosCiviles> getModificarEstadosCiviles() {
        return modificarEstadosCiviles;
    }

    public void setModificarEstadosCiviles(List<EstadosCiviles> modificarEstadosCiviles) {
        this.modificarEstadosCiviles = modificarEstadosCiviles;
    }

    public EstadosCiviles getNuevoEstadoCivil() {
        return nuevoEstadoCivil;
    }

    public void setNuevoEstadoCivil(EstadosCiviles nuevoEstadoCivil) {
        this.nuevoEstadoCivil = nuevoEstadoCivil;
    }

    public EstadosCiviles getDuplicarEstadoCivil() {
        return duplicarEstadoCivil;
    }

    public void setDuplicarEstadoCivil(EstadosCiviles duplicarEstadoCivil) {
        this.duplicarEstadoCivil = duplicarEstadoCivil;
    }

    public EstadosCiviles getEditarEstadoCivil() {
        return editarEstadoCivil;
    }

    public void setEditarEstadoCivil(EstadosCiviles editarEstadoCivil) {
        this.editarEstadoCivil = editarEstadoCivil;
    }

    public int getRegistrosBorrados() {
        return registrosBorrados;
    }

    public void setRegistrosBorrados(int registrosBorrados) {
        this.registrosBorrados = registrosBorrados;
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

    public EstadosCiviles getEstadoCivilSeleccionado() {
        return estadoCivilSeleccionado;
    }

    public void setEstadoCivilSeleccionado(EstadosCiviles estadoCivilSeleccionado) {
        this.estadoCivilSeleccionado = estadoCivilSeleccionado;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEstadosCiviles");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getPaginaanterior() {
        return paginaAnterior;
    }

    public void setPaginaanterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }
}
