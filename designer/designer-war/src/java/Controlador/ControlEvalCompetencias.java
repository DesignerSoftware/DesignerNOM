/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.EvalCompetencias;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarEvalCompetenciasInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;import ControlNavegacion.ControlListaNavegacion;
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
public class ControlEvalCompetencias implements Serializable {

    @EJB
    AdministrarEvalCompetenciasInterface administrarEvalCompetencias;
    @EJB
    AdministrarRastrosInterface administrarRastros;

    private List<EvalCompetencias> listEvalCompetencias;
    private List<EvalCompetencias> filtrarEvalCompetencias;
    private List<EvalCompetencias> crearEvalCompetencias;
    private List<EvalCompetencias> modificarEvalCompetencias;
    private List<EvalCompetencias> borrarEvalCompetencias;
    private EvalCompetencias nuevoEvalCompetencia;
    private EvalCompetencias duplicarEvalCompetencia;
    private EvalCompetencias editarEvalCompetencia;
    private EvalCompetencias evalCompetenciaSeleccionada;
    //otros
    private int tamano;
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion, descripcionCompetencia;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    private BigInteger competenciasCargos;
    private Integer a;
    private String infoRegistro;
    private boolean activarLov;

    public ControlEvalCompetencias() {
        listEvalCompetencias = null;
        crearEvalCompetencias = new ArrayList<EvalCompetencias>();
        modificarEvalCompetencias = new ArrayList<EvalCompetencias>();
        borrarEvalCompetencias = new ArrayList<EvalCompetencias>();
        permitirIndex = true;
        guardado = true;
        editarEvalCompetencia = new EvalCompetencias();
        nuevoEvalCompetencia = new EvalCompetencias();
        duplicarEvalCompetencia = new EvalCompetencias();
        a = null;
        tamano = 270;
        activarLov = true;
    }

       private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>(); mapParametros.put ("paginaAnterior", paginaAnterior);
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
         String pagActual = "cargo"XXX;
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

   @PostConstruct
    public void inicializarAdministrador() {
        try {
            FacesContext x = FacesContext.getCurrentInstance();
            HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
            administrarEvalCompetencias.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void eventoFiltrar() {
        if (tipoLista == 0) {
            tipoLista = 1;
        }
        contarRegistros();
    }

    public void cambiarIndice(EvalCompetencias eval, int celda) {
        if (permitirIndex == true) {
            evalCompetenciaSeleccionada = eval;
            cualCelda = celda;
            evalCompetenciaSeleccionada.getSecuencia();
            if (cualCelda == 0) {
                evalCompetenciaSeleccionada.getCodigo();
            } else if (cualCelda == 1) {
                evalCompetenciaSeleccionada.getDescripcion();
            } else if (cualCelda == 2) {
                evalCompetenciaSeleccionada.getDesCompetencia();
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            descripcionCompetencia = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:descripcionCompetencia");
            descripcionCompetencia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
            bandera = 0;
            filtrarEvalCompetencias = null;
            tipoLista = 0;
        }

        borrarEvalCompetencias.clear();
        crearEvalCompetencias.clear();
        modificarEvalCompetencias.clear();
        evalCompetenciaSeleccionada = null;
        k = 0;
        listEvalCompetencias = null;
        guardado = true;
        permitirIndex = true;
        getListEvalCompetencias();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            descripcionCompetencia = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:descripcionCompetencia");
            descripcionCompetencia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
            bandera = 0;
            filtrarEvalCompetencias = null;
            tipoLista = 0;
        }

        borrarEvalCompetencias.clear();
        crearEvalCompetencias.clear();
        modificarEvalCompetencias.clear();
        evalCompetenciaSeleccionada = null;
        k = 0;
        listEvalCompetencias = null;
        guardado = true;
        permitirIndex = true;
        getListEvalCompetencias();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:codigo");
            codigo.setFilterStyle("width: 85% !important;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:descripcion");
            descripcion.setFilterStyle("width: 85% !important;");
            descripcionCompetencia = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:descripcionCompetencia");
            descripcionCompetencia.setFilterStyle("width: 85% !important;");
            RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            descripcionCompetencia = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:descripcionCompetencia");
            descripcionCompetencia.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
            bandera = 0;
            filtrarEvalCompetencias = null;
            tipoLista = 0;
        }
    }

    public void modificarEvalCompetencia(EvalCompetencias eval) {
        evalCompetenciaSeleccionada = eval;
        if (!crearEvalCompetencias.contains(evalCompetenciaSeleccionada)) {
            if (modificarEvalCompetencias.isEmpty()) {
                modificarEvalCompetencias.add(evalCompetenciaSeleccionada);
            } else if (!modificarEvalCompetencias.contains(evalCompetenciaSeleccionada)) {
                modificarEvalCompetencias.add(evalCompetenciaSeleccionada);
            }
            if (guardado == true) {
                guardado = false;
            }
        }
        RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void borrandoEvalCompetencias() {

        if (evalCompetenciaSeleccionada != null) {
            System.out.println("Entro a borrandoEvalCompetencias");
            if (!modificarEvalCompetencias.isEmpty() && modificarEvalCompetencias.contains(evalCompetenciaSeleccionada)) {
                int modIndex = modificarEvalCompetencias.indexOf(evalCompetenciaSeleccionada);
                modificarEvalCompetencias.remove(modIndex);
                borrarEvalCompetencias.add(evalCompetenciaSeleccionada);
            } else if (!crearEvalCompetencias.isEmpty() && crearEvalCompetencias.contains(evalCompetenciaSeleccionada)) {
                int crearIndex = crearEvalCompetencias.indexOf(evalCompetenciaSeleccionada);
                crearEvalCompetencias.remove(crearIndex);
            } else {
                borrarEvalCompetencias.add(evalCompetenciaSeleccionada);
            }
            listEvalCompetencias.remove(evalCompetenciaSeleccionada);
            if (tipoLista == 1) {
                filtrarEvalCompetencias.remove(evalCompetenciaSeleccionada);
            }
            RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
            contarRegistros();
            evalCompetenciaSeleccionada = null;
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void verificarBorrado() {
        System.out.println("Estoy en verificarBorrado");
        try {
            System.err.println("Control Secuencia de ControlEvalCompetencias ");
            competenciasCargos = administrarEvalCompetencias.verificarCompetenciasCargos(evalCompetenciaSeleccionada.getSecuencia());
            if (competenciasCargos.equals(new BigInteger("0"))) {
                borrandoEvalCompetencias();
            } else {
                System.out.println("Borrado>0");
                RequestContext context = RequestContext.getCurrentInstance();
                RequestContext.getCurrentInstance().update("form:validacionBorrar");
                RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
                competenciasCargos = new BigInteger("-1");

            }
        } catch (Exception e) {
            System.err.println("ERROR ControlEvalCompetencias verificarBorrado ERROR " + e);
        }
    }

    public void revisarDialogoGuardar() {
        if (!borrarEvalCompetencias.isEmpty() || !crearEvalCompetencias.isEmpty() || !modificarEvalCompetencias.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarEvalCompetencias() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarEvalCompetencias");
            if (!borrarEvalCompetencias.isEmpty()) {
                administrarEvalCompetencias.borrarEvalCompetencias(borrarEvalCompetencias);
                registrosBorrados = borrarEvalCompetencias.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarEvalCompetencias.clear();
            }
            if (!crearEvalCompetencias.isEmpty()) {
                administrarEvalCompetencias.crearEvalCompetencias(crearEvalCompetencias);
                crearEvalCompetencias.clear();
            }
            if (!modificarEvalCompetencias.isEmpty()) {
                administrarEvalCompetencias.modificarEvalCompetencias(modificarEvalCompetencias);
                modificarEvalCompetencias.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listEvalCompetencias = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
            k = 0;
            guardado = true;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (evalCompetenciaSeleccionada != null) {
            editarEvalCompetencia = evalCompetenciaSeleccionada;

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

            } else if (cualCelda == 2) {
                RequestContext.getCurrentInstance().update("formularioDialogos:editDescripcionCompetencia");
                RequestContext.getCurrentInstance().execute("PF('editDescripcionCompetencia').show()");
                cualCelda = -1;
            }
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoEvalCompetencias() {
        System.out.println("agregarNuevoEvalCompetencias");
        int contador = 0;
        int duplicados = 0;

        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoEvalCompetencia.getCodigo() == a) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listEvalCompetencias.size(); x++) {
                if (listEvalCompetencias.get(x).getCodigo() == nuevoEvalCompetencia.getCodigo()) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya existe. Por favor ingrese otro código";
            } else {
                contador++;
            }
        }
        if (nuevoEvalCompetencia.getDescripcion() == (null)) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            System.out.println("bandera");
            contador++;
        }
        if (contador == 2) {
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                descripcionCompetencia = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:descripcionCompetencia");
                descripcionCompetencia.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
                bandera = 0;
                filtrarEvalCompetencias = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoEvalCompetencia.setSecuencia(l);
            crearEvalCompetencias.add(nuevoEvalCompetencia);
            listEvalCompetencias.add(nuevoEvalCompetencia);
            evalCompetenciaSeleccionada = nuevoEvalCompetencia;
            nuevoEvalCompetencia = new EvalCompetencias();
            RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
            contarRegistros();
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroEvalEmpresas').hide()");
        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevaEvalCompetencias");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevaEvalCompetencias').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoEvalCompetencias() {
        nuevoEvalCompetencia = new EvalCompetencias();
    }

    public void duplicandoEvalCompetencias() {
        if (evalCompetenciaSeleccionada != null) {
            duplicarEvalCompetencia = new EvalCompetencias();
            k++;
            l = BigInteger.valueOf(k);
            duplicarEvalCompetencia.setSecuencia(l);
            duplicarEvalCompetencia.setCodigo(evalCompetenciaSeleccionada.getCodigo());
            duplicarEvalCompetencia.setDescripcion(evalCompetenciaSeleccionada.getDescripcion());
            duplicarEvalCompetencia.setDesCompetencia(evalCompetenciaSeleccionada.getDesCompetencia());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarEvC");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalCompetencias').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void confirmarDuplicar() {
        int contador = 0;
        mensajeValidacion = " ";
        int duplicados = 0;
        RequestContext context = RequestContext.getCurrentInstance();

        if (duplicarEvalCompetencia.getCodigo() == a) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
        } else {
            for (int x = 0; x < listEvalCompetencias.size(); x++) {
                if (listEvalCompetencias.get(x).getCodigo() == duplicarEvalCompetencia.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "El código ingresado ya existe. Por favor ingrese otro código";
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarEvalCompetencia.getDescripcion().isEmpty()) {
            mensajeValidacion = " Los campos marcados con asterisco son obligatorios";

        } else {
            contador++;
        }
        if (contador == 2) {

            listEvalCompetencias.add(duplicarEvalCompetencia);
            crearEvalCompetencias.add(duplicarEvalCompetencia);
            evalCompetenciaSeleccionada = duplicarEvalCompetencia;
            RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            contarRegistros();
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                descripcionCompetencia = (Column) c.getViewRoot().findComponent("form:datosEvalCompetencia:descripcionCompetencia");
                descripcionCompetencia.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosEvalCompetencia");
                bandera = 0;
                filtrarEvalCompetencias = null;
                tipoLista = 0;
            }
            duplicarEvalCompetencia = new EvalCompetencias();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroEvalCompetencias').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarEvalCompetencias() {
        duplicarEvalCompetencia = new EvalCompetencias();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEvalCompetenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "EVALCOMPETENCIAS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosEvalCompetenciaExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "EVALCOMPETENCIAS", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (evalCompetenciaSeleccionada != null) {
            int resultado = administrarRastros.obtenerTabla(evalCompetenciaSeleccionada.getSecuencia(), "EVALCOMPETENCIAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("EVALCOMPETENCIAS")) { // igual acá
            RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
        } else {
            RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<EvalCompetencias> getListEvalCompetencias() {
        if (listEvalCompetencias == null) {
            listEvalCompetencias = administrarEvalCompetencias.consultarEvalCompetencias();
        }
        return listEvalCompetencias;
    }

    public void setListEvalCompetencias(List<EvalCompetencias> listEvalCompetencias) {
        this.listEvalCompetencias = listEvalCompetencias;
    }

    public List<EvalCompetencias> getFiltrarEvalCompetencias() {
        return filtrarEvalCompetencias;
    }

    public void setFiltrarEvalCompetencias(List<EvalCompetencias> filtrarEvalCompetencias) {
        this.filtrarEvalCompetencias = filtrarEvalCompetencias;
    }

    public EvalCompetencias getNuevoEvalCompetencia() {
        return nuevoEvalCompetencia;
    }

    public void setNuevoEvalCompetencia(EvalCompetencias nuevoEvalCompetencia) {
        this.nuevoEvalCompetencia = nuevoEvalCompetencia;
    }

    public EvalCompetencias getDuplicarEvalCompetencia() {
        return duplicarEvalCompetencia;
    }

    public void setDuplicarEvalCompetencia(EvalCompetencias duplicarEvalCompetencia) {
        this.duplicarEvalCompetencia = duplicarEvalCompetencia;
    }

    public EvalCompetencias getEditarEvalCompetencia() {
        return editarEvalCompetencia;
    }

    public void setEditarEvalCompetencia(EvalCompetencias editarEvalCompetencia) {
        this.editarEvalCompetencia = editarEvalCompetencia;
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

    public EvalCompetencias getEvalCompetenciaSeleccionada() {
        return evalCompetenciaSeleccionada;
    }

    public void setEvalCompetenciaSeleccionada(EvalCompetencias evalCompetenciaSeleccionada) {
        this.evalCompetenciaSeleccionada = evalCompetenciaSeleccionada;
    }

    public int getTamano() {
        return tamano;
    }

    public void setTamano(int tamano) {
        this.tamano = tamano;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosEvalCompetencia");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isAceptar() {
        return aceptar;
    }

    public void setAceptar(boolean aceptar) {
        this.aceptar = aceptar;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }
    
    
}
