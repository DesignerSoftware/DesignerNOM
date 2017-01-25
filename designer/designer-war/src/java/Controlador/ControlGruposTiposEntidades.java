/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Grupostiposentidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarGruposTiposEntidadesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
public class ControlGruposTiposEntidades implements Serializable {

    @EJB
    AdministrarGruposTiposEntidadesInterface administrarGruposTiposEntidades;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<Grupostiposentidades> listGruposTiposEntidades;
    private List<Grupostiposentidades> filtrarGruposTiposEntidades;
    private List<Grupostiposentidades> crearGruposTiposEntidades;
    private List<Grupostiposentidades> modificarGruposTiposEntidades;
    private List<Grupostiposentidades> borrarGruposTiposEntidades;
    private Grupostiposentidades nuevoGruposTiposEntidades;
    private Grupostiposentidades duplicarGruposTiposEntidades;
    private Grupostiposentidades editarGruposTiposEntidades;
    private Grupostiposentidades grupoTipoEntidadSeleccionado;
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
    private String paginaAnterior;
    private boolean activarLov;

    public ControlGruposTiposEntidades() {
        listGruposTiposEntidades = null;
        crearGruposTiposEntidades = new ArrayList<Grupostiposentidades>();
        modificarGruposTiposEntidades = new ArrayList<Grupostiposentidades>();
        borrarGruposTiposEntidades = new ArrayList<Grupostiposentidades>();
        permitirIndex = true;
        editarGruposTiposEntidades = new Grupostiposentidades();
        nuevoGruposTiposEntidades = new Grupostiposentidades();
        duplicarGruposTiposEntidades = new Grupostiposentidades();
        guardado = true;
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
            administrarGruposTiposEntidades.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void recibirPaginaEntrante(String pagina) {
        paginaAnterior = pagina;
    }

    public String redirigirPaginaAnterior() {
        return paginaAnterior;
    }

    public void cambiarIndice(Grupostiposentidades grupote, int celda) {

        if (permitirIndex == true) {
            grupoTipoEntidadSeleccionado = grupote;
            cualCelda = celda;
            grupoTipoEntidadSeleccionado.getSecuencia();
            if (cualCelda == 0) {
                grupoTipoEntidadSeleccionado.getCodigo();
            } else if (cualCelda == 1) {
                grupoTipoEntidadSeleccionado.getNombre();
            }
        }
    }

    public void activarAceptar() {
        aceptar = false;
    }

    public void cancelarModificacion() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            bandera = 0;
            filtrarGruposTiposEntidades = null;
            tipoLista = 0;
        }
        borrarGruposTiposEntidades.clear();
        crearGruposTiposEntidades.clear();
        modificarGruposTiposEntidades.clear();
        grupoTipoEntidadSeleccionado = null;
        k = 0;
        listGruposTiposEntidades = null;
        guardado = true;
        permitirIndex = true;
        getListGruposTiposEntidades();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            bandera = 0;
            filtrarGruposTiposEntidades = null;
            tipoLista = 0;
        }

        borrarGruposTiposEntidades.clear();
        crearGruposTiposEntidades.clear();
        modificarGruposTiposEntidades.clear();
        grupoTipoEntidadSeleccionado = null;
        k = 0;
        listGruposTiposEntidades = null;
        guardado = true;
        permitirIndex = true;
        getListGruposTiposEntidades();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            bandera = 0;
            filtrarGruposTiposEntidades = null;
            tipoLista = 0;
        }
    }

    public void modificarGruposTiposEntidades(Grupostiposentidades grupote) {
        grupoTipoEntidadSeleccionado = grupote;

        int contador = 0;
        boolean banderita = false;
        boolean banderita1 = false;

        RequestContext context = RequestContext.getCurrentInstance();
        if (!crearGruposTiposEntidades.contains(grupoTipoEntidadSeleccionado)) {
            if (modificarGruposTiposEntidades.isEmpty()) {
                modificarGruposTiposEntidades.add(grupoTipoEntidadSeleccionado);
            } else if (!modificarGruposTiposEntidades.contains(grupoTipoEntidadSeleccionado)) {
                modificarGruposTiposEntidades.add(grupoTipoEntidadSeleccionado);
            }
            if (guardado == true) {
                guardado = false;
            }

            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrandoGruposTiposEntidades() {

        if (grupoTipoEntidadSeleccionado != null) {
            if (!modificarGruposTiposEntidades.isEmpty() && modificarGruposTiposEntidades.contains(grupoTipoEntidadSeleccionado)) {
                int modIndex = modificarGruposTiposEntidades.indexOf(grupoTipoEntidadSeleccionado);
                modificarGruposTiposEntidades.remove(modIndex);
                borrarGruposTiposEntidades.add(grupoTipoEntidadSeleccionado);
            } else if (!crearGruposTiposEntidades.isEmpty() && crearGruposTiposEntidades.contains(grupoTipoEntidadSeleccionado)) {
                int crearIndex = crearGruposTiposEntidades.indexOf(grupoTipoEntidadSeleccionado);
                crearGruposTiposEntidades.remove(crearIndex);
            } else {
                borrarGruposTiposEntidades.add(grupoTipoEntidadSeleccionado);
            }
            listGruposTiposEntidades.remove(grupoTipoEntidadSeleccionado);
            if (tipoLista == 1) {
                filtrarGruposTiposEntidades.remove(grupoTipoEntidadSeleccionado);

            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            grupoTipoEntidadSeleccionado = null;

            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }

    }

    public void revisarDialogoGuardar() {
        if (!borrarGruposTiposEntidades.isEmpty() || !crearGruposTiposEntidades.isEmpty() || !modificarGruposTiposEntidades.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarGruposTiposEntidades() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            System.out.println("Realizando guardarGruposTiposEntidades");
            if (!borrarGruposTiposEntidades.isEmpty()) {
                administrarGruposTiposEntidades.borrarGruposTiposEntidades(borrarGruposTiposEntidades);
                //mostrarBorrados
                registrosBorrados = borrarGruposTiposEntidades.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarGruposTiposEntidades.clear();
            }
            if (!modificarGruposTiposEntidades.isEmpty()) {
                administrarGruposTiposEntidades.modificarGruposTiposEntidades(modificarGruposTiposEntidades);
                modificarGruposTiposEntidades.clear();
            }
            if (!crearGruposTiposEntidades.isEmpty()) {
                administrarGruposTiposEntidades.crearGruposTiposEntidades(crearGruposTiposEntidades);
                crearGruposTiposEntidades.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listGruposTiposEntidades = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardarón los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            k = 0;
            guardado = true;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (grupoTipoEntidadSeleccionado != null) {
            editarGruposTiposEntidades = grupoTipoEntidadSeleccionado;

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
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoGruposTiposEntidades() {
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoGruposTiposEntidades.getCodigo() == a) {
            mensajeValidacion = " El campo código es obligatorio";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {

            for (int x = 0; x < listGruposTiposEntidades.size(); x++) {
                if (listGruposTiposEntidades.get(x).getCodigo() == nuevoGruposTiposEntidades.getCodigo()) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = "Ya existe un registro con el código ingresado.";
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoGruposTiposEntidades.getNombre() == null || nuevoGruposTiposEntidades.getNombre().isEmpty()) {
            mensajeValidacion = " El campo nombre es obligatorio";

        } else {
            System.out.println("bandera");
            contador++;
        }

        if (contador == 2) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
                bandera = 0;
                filtrarGruposTiposEntidades = null;
                tipoLista = 0;
            }
            System.out.println("Despues de la bandera");

            k++;
            l = BigInteger.valueOf(k);
            nuevoGruposTiposEntidades.setSecuencia(l);
            crearGruposTiposEntidades.add(nuevoGruposTiposEntidades);
            listGruposTiposEntidades.add(nuevoGruposTiposEntidades);
            grupoTipoEntidadSeleccionado = nuevoGruposTiposEntidades;
            contarRegistros();
            nuevoGruposTiposEntidades = new Grupostiposentidades();
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }

            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroGruposTiposEntidades').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoGrupoTipoEntidad");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoGrupoTipoEntidad').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoGruposTiposEntidades() {
        System.out.println("limpiarNuevoGruposTiposEntidades");
        nuevoGruposTiposEntidades = new Grupostiposentidades();

    }

    public void duplicandoGruposTiposEntidades() {
        if (grupoTipoEntidadSeleccionado != null) {
            duplicarGruposTiposEntidades = new Grupostiposentidades();
            k++;
            l = BigInteger.valueOf(k);

            duplicarGruposTiposEntidades.setSecuencia(l);
            duplicarGruposTiposEntidades.setCodigo(grupoTipoEntidadSeleccionado.getCodigo());
            duplicarGruposTiposEntidades.setNombre(grupoTipoEntidadSeleccionado.getNombre());

            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroGruposTiposEntidades').show()");
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
        if (duplicarGruposTiposEntidades.getCodigo() == a) {
            mensajeValidacion = " El campo código es obligatorio";
        } else {
            for (int x = 0; x < listGruposTiposEntidades.size(); x++) {
                if (listGruposTiposEntidades.get(x).getCodigo() == duplicarGruposTiposEntidades.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "Ya existe un registro con el código ingresado.";
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarGruposTiposEntidades.getNombre() == null || duplicarGruposTiposEntidades.getNombre().isEmpty()) {
            mensajeValidacion = " El campo nombre es obligatorio";

        } else {
            System.out.println("Bandera : ");
            contador++;
        }

        if (contador == 2) {
            listGruposTiposEntidades.add(duplicarGruposTiposEntidades);
            crearGruposTiposEntidades.add(duplicarGruposTiposEntidades);
            grupoTipoEntidadSeleccionado = duplicarGruposTiposEntidades;
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposTiposEntidades:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosGruposTiposEntidades");
                bandera = 0;
                filtrarGruposTiposEntidades = null;
                tipoLista = 0;
            }
            duplicarGruposTiposEntidades = new Grupostiposentidades();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroGruposTiposEntidades').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarGruposTiposEntidades() {
        duplicarGruposTiposEntidades = new Grupostiposentidades();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposTiposEntidadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "GRUPOSTIPOSENTIDADES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposTiposEntidadesExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "GRUPOSTIPOSENTIDADES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (grupoTipoEntidadSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(grupoTipoEntidadSeleccionado.getSecuencia(), "GRUPOSTIPOSENTIDADES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("GRUPOSTIPOSENTIDADES")) { // igual acá
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
            System.out.println("ERROR ControlGruposTiposEntidades eventoFiltrar ERROR===" + e.getMessage());
        }
    }

    public void contarRegistros() {
        RequestContext.getCurrentInstance().update("form:informacionRegistro");
    }

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<Grupostiposentidades> getListGruposTiposEntidades() {
        if (listGruposTiposEntidades == null) {
            listGruposTiposEntidades = administrarGruposTiposEntidades.consultarGruposTiposEntidades();
        }
        return listGruposTiposEntidades;
    }

    public void setListGruposTiposEntidades(List<Grupostiposentidades> listGruposTiposEntidades) {
        this.listGruposTiposEntidades = listGruposTiposEntidades;
    }

    public List<Grupostiposentidades> getFiltrarGruposTiposEntidades() {
        return filtrarGruposTiposEntidades;
    }

    public void setFiltrarGruposTiposEntidades(List<Grupostiposentidades> filtrarGruposTiposEntidades) {
        this.filtrarGruposTiposEntidades = filtrarGruposTiposEntidades;
    }

    public Grupostiposentidades getNuevoGruposTiposEntidades() {
        return nuevoGruposTiposEntidades;
    }

    public void setNuevoGruposTiposEntidades(Grupostiposentidades nuevoGruposTiposEntidades) {
        this.nuevoGruposTiposEntidades = nuevoGruposTiposEntidades;
    }

    public Grupostiposentidades getDuplicarGruposTiposEntidades() {
        return duplicarGruposTiposEntidades;
    }

    public void setDuplicarGruposTiposEntidades(Grupostiposentidades duplicarGruposTiposEntidades) {
        this.duplicarGruposTiposEntidades = duplicarGruposTiposEntidades;
    }

    public Grupostiposentidades getEditarGruposTiposEntidades() {
        return editarGruposTiposEntidades;
    }

    public void setEditarGruposTiposEntidades(Grupostiposentidades editarGruposTiposEntidades) {
        this.editarGruposTiposEntidades = editarGruposTiposEntidades;
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

    public Grupostiposentidades getGrupoTipoEntidadSeleccionado() {
        return grupoTipoEntidadSeleccionado;
    }

    public void setGrupoTipoEntidadSeleccionado(Grupostiposentidades grupoTipoEntidadSeleccionado) {
        this.grupoTipoEntidadSeleccionado = grupoTipoEntidadSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosGruposTiposEntidades");
        infoRegistro = String.valueOf(tabla.getRowCount());
        return infoRegistro;
    }

    public void setInfoRegistro(String infoRegistro) {
        this.infoRegistro = infoRegistro;
    }

    public boolean isActivarLov() {
        return activarLov;
    }

    public void setActivarLov(boolean activarLov) {
        this.activarLov = activarLov;
    }
    
    
}
