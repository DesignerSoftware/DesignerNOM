/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Jornadas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarJornadasInterface;
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
public class ControlJornadas implements Serializable {

    @EJB
    AdministrarJornadasInterface administrarJornadas;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Jornadas> listJornadas;
    private List<Jornadas> filtrarJornadas;
    private List<Jornadas> crearJornadas;
    private List<Jornadas> modificarJornadas;
    private List<Jornadas> borrarJornadas;
    private Jornadas nuevoJornadas;
    private Jornadas duplicarJornadas;
    private Jornadas editarJornadas;
    private Jornadas jornadaSeleccionada;
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
    //filtrado table
    private int tamano;
    private String infoRegistro;
    private boolean activarLov;
    private String paginaAnterior = "nominaf";
    private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

    public ControlJornadas() {
        listJornadas = null;
        crearJornadas = new ArrayList<Jornadas>();
        modificarJornadas = new ArrayList<Jornadas>();
        borrarJornadas = new ArrayList<Jornadas>();
        permitirIndex = true;
        editarJornadas = new Jornadas();
        nuevoJornadas = new Jornadas();
        duplicarJornadas = new Jornadas();
        guardado = true;
        tamano = 270;
        activarLov = true;
        mapParametros.put("paginaAnterior", paginaAnterior);
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
        listJornadas = null;
        getListJornadas();
    }

    public void recibirParametros(Map<String, Object> map) {
        mapParametros = map;
        paginaAnterior = (String) mapParametros.get("paginaAnterior");
        listJornadas = null;
        getListJornadas();
    }

    //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
    public void navegar(String pag) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
        String pagActual = "jornada";
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
            administrarJornadas.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public String redirigir() {
        return paginaAnterior;
    }

    public void eventoFiltrar() {
        try {
            System.out.println("\n ENTRE A ControlJornadas.eventoFiltrar \n");
            if (tipoLista == 0) {
                tipoLista = 1;
            }
            contarRegistros();
        } catch (Exception e) {
            System.out.println("ERROR ControlJornadas eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void cambiarIndice(Jornadas jornada, int celda) {
        if (permitirIndex == true) {
            jornadaSeleccionada = jornada;
            cualCelda = celda;
            jornadaSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                jornadaSeleccionada.getCodigo();
            } else if (cualCelda == 1) {
                jornadaSeleccionada.getDescripcion();
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            codigo = (Column) c.getViewRoot().findComponent("form:datosJornadas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosJornadas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosJornadas");
            bandera = 0;
            filtrarJornadas = null;
            tipoLista = 0;
        }

        borrarJornadas.clear();
        crearJornadas.clear();
        modificarJornadas.clear();
        jornadaSeleccionada = null;
        k = 0;
        listJornadas = null;
        guardado = true;
        permitirIndex = true;
        getListJornadas();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosJornadas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        limpiarListasValor();
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            codigo = (Column) c.getViewRoot().findComponent("form:datosJornadas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosJornadas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosJornadas");
            bandera = 0;
            filtrarJornadas = null;
            tipoLista = 0;
        }
        borrarJornadas.clear();
        crearJornadas.clear();
        modificarJornadas.clear();
        jornadaSeleccionada = null;
        k = 0;
        listJornadas = null;
        guardado = true;
        permitirIndex = true;
        getListJornadas();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosJornadas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
        navegar("atras");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosJornadas:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosJornadas:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosJornadas");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosJornadas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosJornadas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosJornadas");
            bandera = 0;
            filtrarJornadas = null;
            tipoLista = 0;
        }
    }

    public void modificarJornadas(Jornadas jornada) {
        jornadaSeleccionada = jornada;

        if (modificarJornadas.isEmpty()) {
            modificarJornadas.add(jornadaSeleccionada);
        } else if (!modificarJornadas.contains(jornadaSeleccionada)) {
            modificarJornadas.add(jornadaSeleccionada);
        }
        if (guardado == true) {
            guardado = false;
        }
        RequestContext.getCurrentInstance().update("form:datosJornadas");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoJornadas() {
        if (jornadaSeleccionada != null) {
            if (!modificarJornadas.isEmpty() && modificarJornadas.contains(jornadaSeleccionada)) {
                int modIndex = modificarJornadas.indexOf(jornadaSeleccionada);
                modificarJornadas.remove(modIndex);
                borrarJornadas.add(jornadaSeleccionada);
            } else if (!crearJornadas.isEmpty() && crearJornadas.contains(jornadaSeleccionada)) {
                int crearIndex = crearJornadas.indexOf(jornadaSeleccionada);
                crearJornadas.remove(crearIndex);
            } else {
                borrarJornadas.add(jornadaSeleccionada);
            }
            listJornadas.remove(jornadaSeleccionada);
            if (tipoLista == 1) {
                filtrarJornadas.remove(jornadaSeleccionada);

            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosJornadas");
            jornadaSeleccionada = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void verificarBorrado() {
        BigInteger contarJornadasLaboralesJornada;
        BigInteger contarTarifasEscalafonesJornada;

        try {
            if (jornadaSeleccionada != null) {
                System.err.println("Control Secuencia de ControlJornadas ");
                contarJornadasLaboralesJornada = administrarJornadas.contarJornadasLaboralesJornada(jornadaSeleccionada.getSecuencia());
                contarTarifasEscalafonesJornada = administrarJornadas.contarTarifasEscalafonesJornada(jornadaSeleccionada.getSecuencia());
                if (contarJornadasLaboralesJornada.equals(new BigInteger("0")) && contarTarifasEscalafonesJornada.equals(new BigInteger("0"))) {
                    System.out.println("Borrado==0");
                    borrandoJornadas();
                } else {
                    System.out.println("Borrado>0");

                    RequestContext context = RequestContext.getCurrentInstance();
                    RequestContext.getCurrentInstance().update("form:validacionBorrar");
                    RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                    contarJornadasLaboralesJornada = new BigInteger("-1");
                    contarTarifasEscalafonesJornada = new BigInteger("-1");
                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
            }
        } catch (Exception e) {
            System.err.println("ERROR ControlJornadas verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {

        if (!borrarJornadas.isEmpty() || !crearJornadas.isEmpty() || !modificarJornadas.isEmpty()) {
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarJornadas() {
        if (guardado == false) {
            System.out.println("Realizando guardarJornadas");
            if (!borrarJornadas.isEmpty()) {
                administrarJornadas.borrarJornadas(borrarJornadas);
                //mostrarBorrados
                registrosBorrados = borrarJornadas.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarJornadas.clear();
            }
            if (!modificarJornadas.isEmpty()) {
                administrarJornadas.modificarJornadas(modificarJornadas);
                modificarJornadas.clear();
            }
            if (!crearJornadas.isEmpty()) {
                administrarJornadas.crearJornadas(crearJornadas);
                crearJornadas.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listJornadas = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosJornadas");
            k = 0;
            guardado = true;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (jornadaSeleccionada != null) {
            editarJornadas = jornadaSeleccionada;

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

    public void agregarNuevoJornadas() {
        System.out.println("agregarNuevoJornadas");
        int contador = 0;
        int duplicados = 0;
        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoJornadas.getCodigo() == a) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listJornadas.size(); x++) {
                if (listJornadas.get(x).getCodigo() == nuevoJornadas.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido.";
            } else {
                contador++;
            }
        }
        if (nuevoJornadas.getDescripcion() == null || nuevoJornadas.getDescripcion().isEmpty()) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                codigo = (Column) c.getViewRoot().findComponent("form:datosJornadas:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosJornadas:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosJornadas");
                bandera = 0;
                filtrarJornadas = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoJornadas.setSecuencia(l);
            crearJornadas.add(nuevoJornadas);
            listJornadas.add(nuevoJornadas);
            jornadaSeleccionada = nuevoJornadas;
            contarRegistros();
            nuevoJornadas = new Jornadas();
            RequestContext.getCurrentInstance().update("form:datosJornadas");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroJornadas').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoJornadas() {
        nuevoJornadas = new Jornadas();
    }

    public void duplicandoJornadas() {
        if (jornadaSeleccionada != null) {
            duplicarJornadas = new Jornadas();
            k++;
            l = BigInteger.valueOf(k);
            duplicarJornadas.setSecuencia(l);
            duplicarJornadas.setCodigo(jornadaSeleccionada.getCodigo());
            duplicarJornadas.setDescripcion(jornadaSeleccionada.getDescripcion());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroJornadas').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();
        Integer a = 0;
        a = null;

        if (duplicarJornadas.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listJornadas.size(); x++) {
                if (listJornadas.get(x).getCodigo() == duplicarJornadas.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido.";
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarJornadas.getDescripcion() == null || duplicarJornadas.getDescripcion().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
        } else {
            contador++;
        }

        if (contador == 2) {
            listJornadas.add(duplicarJornadas);
            crearJornadas.add(duplicarJornadas);
            contarRegistros();
            jornadaSeleccionada = duplicarJornadas;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                codigo = (Column) c.getViewRoot().findComponent("form:datosJornadas:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosJornadas:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosJornadas");
                bandera = 0;
                filtrarJornadas = null;
                tipoLista = 0;
            }
            duplicarJornadas = new Jornadas();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroJornadas').hide()");
        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarJornadas() {
        duplicarJornadas = new Jornadas();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosJornadasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "JORNADAS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosJornadasExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "JORNADAS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (jornadaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(jornadaSeleccionada.getSecuencia(), "JORNADAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("JORNADAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Jornadas> getListJornadas() {
        if (listJornadas == null) {
            listJornadas = administrarJornadas.consultarJornadas();
        }
        return listJornadas;
    }

    public void setListJornadas(List<Jornadas> listJornadas) {
        this.listJornadas = listJornadas;
    }

    public List<Jornadas> getFiltrarJornadas() {
        return filtrarJornadas;
    }

    public void setFiltrarJornadas(List<Jornadas> filtrarJornadas) {
        this.filtrarJornadas = filtrarJornadas;
    }

    public Jornadas getNuevoJornadas() {
        return nuevoJornadas;
    }

    public void setNuevoJornadas(Jornadas nuevoJornadas) {
        this.nuevoJornadas = nuevoJornadas;
    }

    public Jornadas getDuplicarJornadas() {
        return duplicarJornadas;
    }

    public void setDuplicarJornadas(Jornadas duplicarJornadas) {
        this.duplicarJornadas = duplicarJornadas;
    }

    public Jornadas getEditarJornadas() {
        return editarJornadas;
    }

    public void setEditarJornadas(Jornadas editarJornadas) {
        this.editarJornadas = editarJornadas;
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

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public Jornadas getJornadaSeleccionada() {
        return jornadaSeleccionada;
    }

    public void setJornadaSeleccionada(Jornadas jornadaSeleccionada) {
        this.jornadaSeleccionada = jornadaSeleccionada;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosJornadas");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public String getPaginaAnterior() {
        return paginaAnterior;
    }

    public void setPaginaAnterior(String paginaAnterior) {
        this.paginaAnterior = paginaAnterior;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }

}
