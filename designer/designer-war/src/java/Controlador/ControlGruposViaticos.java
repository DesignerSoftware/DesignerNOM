/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.GruposViaticos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarGruposViaticosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class ControlGruposViaticos implements Serializable {

    @EJB
    AdministrarGruposViaticosInterface administrarGruposViaticos;
    @EJB
    AdministrarRastrosInterface administrarRastros;
    private List<GruposViaticos> listGruposViaticos;
    private List<GruposViaticos> filtrarGruposViaticos;
    private List<GruposViaticos> crearGruposViaticos;
    private List<GruposViaticos> modificarGruposViaticos;
    private List<GruposViaticos> borrarGruposViaticos;
    private GruposViaticos nuevoGruposViaticos;
    private GruposViaticos duplicarGruposViaticos;
    private GruposViaticos editarGruposViaticos;
    private GruposViaticos grupoViaticoSeleccionado;
    //otros
    private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
    private BigInteger l;
    private boolean aceptar, guardado;
    //AutoCompletar
    private boolean permitirIndex;
    //RASTRO
    private Column codigo, descripcion, estado;
    //borrado
    private int registrosBorrados;
    private String mensajeValidacion;
    //filtrado table
    private int tamano;
    private String infoRegistro;
    private boolean activarLov;

    public ControlGruposViaticos() {
        listGruposViaticos = null;
        crearGruposViaticos = new ArrayList<GruposViaticos>();
        modificarGruposViaticos = new ArrayList<GruposViaticos>();
        borrarGruposViaticos = new ArrayList<GruposViaticos>();
        permitirIndex = true;
        editarGruposViaticos = new GruposViaticos();
        nuevoGruposViaticos = new GruposViaticos();
        duplicarGruposViaticos = new GruposViaticos();
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
            administrarGruposViaticos.obtenerConexion(ses.getId());
            administrarRastros.obtenerConexion(ses.getId());
        } catch (Exception e) {
            System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
            System.out.println("Causa: " + e.getCause());
        }
    }

    public void cambiarIndice(GruposViaticos grupo, int celda) {
        if (permitirIndex == true) {
            grupoViaticoSeleccionado = grupo;
            cualCelda = celda;
            grupoViaticoSeleccionado.getSecuencia();
            if (cualCelda == 0) {
                grupoViaticoSeleccionado.getCodigo();
            } else if (cualCelda == 1) {
                grupoViaticoSeleccionado.getDescripcion();
            } else if (cualCelda == 2) {
                grupoViaticoSeleccionado.getPorcentajelastday();
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
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estado = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:estado");
            estado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            bandera = 0;
            filtrarGruposViaticos = null;
            tipoLista = 0;
        }

        borrarGruposViaticos.clear();
        crearGruposViaticos.clear();
        modificarGruposViaticos.clear();
        grupoViaticoSeleccionado = null;
        k = 0;
        listGruposViaticos = null;
        guardado = true;
        permitirIndex = true;
        getListGruposViaticos();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void salir() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 1) {
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estado = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:estado");
            estado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            bandera = 0;
            filtrarGruposViaticos = null;
            tipoLista = 0;
        }

        borrarGruposViaticos.clear();
        crearGruposViaticos.clear();
        modificarGruposViaticos.clear();
        grupoViaticoSeleccionado = null;
        k = 0;
        listGruposViaticos = null;
        guardado = true;
        permitirIndex = true;
        getListGruposViaticos();
        RequestContext context = RequestContext.getCurrentInstance();
        contarRegistros();
        RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
        RequestContext.getCurrentInstance().update("form:ACEPTAR");
    }

    public void activarCtrlF11() {
        FacesContext c = FacesContext.getCurrentInstance();
        if (bandera == 0) {
            tamano = 250;
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:codigo");
            codigo.setFilterStyle("width: 85% !important");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:descripcion");
            descripcion.setFilterStyle("width: 85% !important");
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            estado = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:estado");
            estado.setFilterStyle("width: 85% !important");
            System.out.println("Activar");
            bandera = 1;
        } else if (bandera == 1) {
            System.out.println("Desactivar");
            tamano = 270;
            codigo = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            estado = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:estado");
            estado.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            bandera = 0;
            filtrarGruposViaticos = null;
            tipoLista = 0;
        }
    }

    public void modificarGruposViaticos(GruposViaticos grupo, String confirmarCambio, String valorConfirmar) {
        grupoViaticoSeleccionado = grupo;
        int contador = 0, pass = 0;

        RequestContext context = RequestContext.getCurrentInstance();
        if (confirmarCambio.equalsIgnoreCase("N")) {
            if (grupoViaticoSeleccionado.getCodigo() == null) {
                mensajeValidacion = "No pueden haber campos vacíos";
                grupoViaticoSeleccionado.setCodigo(grupoViaticoSeleccionado.getCodigo());
            }
            if (grupoViaticoSeleccionado.getDescripcion().isEmpty()) {
                mensajeValidacion = "No pueden haber campos vacíos";
                grupoViaticoSeleccionado.setDescripcion(grupoViaticoSeleccionado.getDescripcion());
            } else if (grupoViaticoSeleccionado.getDescripcion().equals(" ")) {
                mensajeValidacion = "No pueden haber campos vacíos";
                grupoViaticoSeleccionado.setDescripcion(grupoViaticoSeleccionado.getDescripcion());
            } else {
                pass++;
            }
            if (grupoViaticoSeleccionado.getPorcentajelastday() == null) {
                mensajeValidacion = "No pueden haber campos vacíos";
                grupoViaticoSeleccionado.setPorcentajelastday(grupoViaticoSeleccionado.getPorcentajelastday());
            } else if (grupoViaticoSeleccionado.getPorcentajelastday().equals(" ")) {
                mensajeValidacion = "No pueden haber campos vacíos";
                grupoViaticoSeleccionado.setPorcentajelastday(grupoViaticoSeleccionado.getPorcentajelastday());
            } else {
                pass++;
            }

            if (pass == 2) {
                if (modificarGruposViaticos.isEmpty()) {
                    modificarGruposViaticos.add(grupoViaticoSeleccionado);
                } else if (!modificarGruposViaticos.contains(grupoViaticoSeleccionado)) {
                    modificarGruposViaticos.add(grupoViaticoSeleccionado);
                }
                if (guardado == true) {
                    guardado = false;
                }
            } else {
                RequestContext.getCurrentInstance().update("form:validacionModificar");
                RequestContext.getCurrentInstance().execute("PF('validacionModificar').show()");
            }
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
        }
    }

    public void borrandoGruposViaticos() {
        if (grupoViaticoSeleccionado != null) {
            if (tipoLista == 0) {
                System.out.println("Entro a borrandoGruposViaticos");
                if (!modificarGruposViaticos.isEmpty() && modificarGruposViaticos.contains(grupoViaticoSeleccionado)) {
                    int modIndex = modificarGruposViaticos.indexOf(grupoViaticoSeleccionado);
                    modificarGruposViaticos.remove(modIndex);
                    borrarGruposViaticos.add(grupoViaticoSeleccionado);
                } else if (!crearGruposViaticos.isEmpty() && crearGruposViaticos.contains(grupoViaticoSeleccionado)) {
                    int crearIndex = crearGruposViaticos.indexOf(grupoViaticoSeleccionado);
                    crearGruposViaticos.remove(crearIndex);
                } else {
                    borrarGruposViaticos.add(grupoViaticoSeleccionado);
                }
                listGruposViaticos.remove(grupoViaticoSeleccionado);
            }
            if (tipoLista == 1) {
                filtrarGruposViaticos.remove(grupoViaticoSeleccionado);
            }
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            grupoViaticoSeleccionado = null;

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
        if (!borrarGruposViaticos.isEmpty() || !crearGruposViaticos.isEmpty() || !modificarGruposViaticos.isEmpty()) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:confirmarGuardar");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
        }
    }

    public void guardarGruposViaticos() {
        RequestContext context = RequestContext.getCurrentInstance();

        if (guardado == false) {
            if (!borrarGruposViaticos.isEmpty()) {
                administrarGruposViaticos.borrarGruposViaticos(borrarGruposViaticos);
                //mostrarBorrados
                registrosBorrados = borrarGruposViaticos.size();
                RequestContext.getCurrentInstance().update("form:mostrarBorrados");
                RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
                borrarGruposViaticos.clear();
            }
            if (!modificarGruposViaticos.isEmpty()) {
                administrarGruposViaticos.modificarGruposViaticos(modificarGruposViaticos);
                modificarGruposViaticos.clear();
            }
            if (!crearGruposViaticos.isEmpty()) {
                administrarGruposViaticos.crearGruposViaticos(crearGruposViaticos);
                crearGruposViaticos.clear();
            }
            System.out.println("Se guardaron los datos con exito");
            listGruposViaticos = null;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            k = 0;
            guardado = true;
        }
        RequestContext.getCurrentInstance().update("form:ACEPTAR");

    }

    public void editarCelda() {
        if (grupoViaticoSeleccionado != null) {
            if (tipoLista == 0) {
                editarGruposViaticos = grupoViaticoSeleccionado;
            }
            if (tipoLista == 1) {
                editarGruposViaticos = grupoViaticoSeleccionado;
            }

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
                RequestContext.getCurrentInstance().update("formularioDialogos:editPorcentaje");
                RequestContext.getCurrentInstance().execute("PF('editPorcentaje').show()");
                cualCelda = -1;
            }

        } else {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
        }
    }

    public void agregarNuevoGruposViaticos() {
        System.out.println("agregarNuevoGruposViaticos");
        int contador = 0;
        int duplicados = 0;

        Integer a = 0;
        a = null;
        mensajeValidacion = " ";
        RequestContext context = RequestContext.getCurrentInstance();
        if (nuevoGruposViaticos.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);
        } else {
            for (int x = 0; x < listGruposViaticos.size(); x++) {
                if (listGruposViaticos.get(x).getCodigo() == nuevoGruposViaticos.getCodigo()) {
                    duplicados++;
                }
            }

            if (duplicados > 0) {
                mensajeValidacion = " Ya existe un registro con el código ingresado \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
            }
        }
        if (nuevoGruposViaticos.getDescripcion() == null || nuevoGruposViaticos.getDescripcion().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;
        }
        if (nuevoGruposViaticos.getPorcentajelastday() == null || nuevoGruposViaticos.getPorcentajelastday() == null) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios \n";
            System.out.println("Mensaje validacion : " + mensajeValidacion);

        } else {
            System.out.println("bandera");
            contador++;
        }

        System.out.println("contador " + contador);

        if (contador == 3) {
            FacesContext c = FacesContext.getCurrentInstance();
            if (bandera == 1) {
                //CERRAR FILTRADO
                System.out.println("Desactivar");
                codigo = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                estado = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:estado");
                estado.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
                bandera = 0;
                filtrarGruposViaticos = null;
                tipoLista = 0;
            }
            k++;
            l = BigInteger.valueOf(k);
            nuevoGruposViaticos.setSecuencia(l);
            crearGruposViaticos.add(nuevoGruposViaticos);
            listGruposViaticos.add(nuevoGruposViaticos);
            grupoViaticoSeleccionado = nuevoGruposViaticos;
            contarRegistros();
            nuevoGruposViaticos = new GruposViaticos();
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            if (guardado == true) {
                guardado = false;
                RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('nuevoRegistroGruposViaticos').hide()");

        } else {
            RequestContext.getCurrentInstance().update("form:validacionNuevoGrupoViatico");
            RequestContext.getCurrentInstance().execute("PF('validacionNuevoGrupoViatico').show()");
            contador = 0;
        }
    }

    public void limpiarNuevoGruposViaticos() {
        nuevoGruposViaticos = new GruposViaticos();
    }

    public void duplicandoGruposViaticos() {
        if (grupoViaticoSeleccionado != null) {
            duplicarGruposViaticos = new GruposViaticos();
            k++;
            l = BigInteger.valueOf(k);
            duplicarGruposViaticos.setSecuencia(l);
            duplicarGruposViaticos.setCodigo(grupoViaticoSeleccionado.getCodigo());
            duplicarGruposViaticos.setDescripcion(grupoViaticoSeleccionado.getDescripcion());
            duplicarGruposViaticos.setPorcentajelastday(grupoViaticoSeleccionado.getPorcentajelastday());
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroGruposViaticos').show()");
        } else {
            RequestContext context = RequestContext.getCurrentInstance();
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

        if (duplicarGruposViaticos.getCodigo() == a) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios \n";
        } else {
            for (int x = 0; x < listGruposViaticos.size(); x++) {
                if (listGruposViaticos.get(x).getCodigo() == duplicarGruposViaticos.getCodigo()) {
                    duplicados++;
                }
            }
            if (duplicados > 0) {
                mensajeValidacion = "*Ya existe un registro con el código ingresado \n";
                System.out.println("Mensaje validacion : " + mensajeValidacion);
            } else {
                System.out.println("bandera");
                contador++;
                duplicados = 0;
            }
        }
        if (duplicarGruposViaticos.getDescripcion() == null || duplicarGruposViaticos.getDescripcion().isEmpty()) {
            mensajeValidacion = "Los campos marcados con asterisco son obligatorios \n";
        } else {
            contador++;
        }
        if (duplicarGruposViaticos.getPorcentajelastday() == null || duplicarGruposViaticos.getPorcentajelastday() == null) {
            mensajeValidacion = mensajeValidacion + "Los campos marcados con asterisco son obligatorios \n";
        } else {
            contador++;
        }

        if (contador == 3) {
            listGruposViaticos.add(duplicarGruposViaticos);
            crearGruposViaticos.add(duplicarGruposViaticos);
            grupoViaticoSeleccionado = duplicarGruposViaticos;
            RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
            contarRegistros();
            if (guardado == true) {
                guardado = false;
            }
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            if (bandera == 1) {
                FacesContext c = FacesContext.getCurrentInstance();
                //CERRAR FILTRADO
                codigo = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:codigo");
                codigo.setFilterStyle("display: none; visibility: hidden;");
                descripcion = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:descripcion");
                descripcion.setFilterStyle("display: none; visibility: hidden;");
                estado = (Column) c.getViewRoot().findComponent("form:datosGruposViaticos:estado");
                estado.setFilterStyle("display: none; visibility: hidden;");
                RequestContext.getCurrentInstance().update("form:datosGruposViaticos");
                bandera = 0;
                filtrarGruposViaticos = null;
                tipoLista = 0;
            }
            duplicarGruposViaticos = new GruposViaticos();
            RequestContext.getCurrentInstance().execute("PF('duplicarRegistroGruposViaticos').hide()");

        } else {
            contador = 0;
            RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
            RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
        }
    }

    public void limpiarDuplicarGruposViaticos() {
        duplicarGruposViaticos = new GruposViaticos();
    }

    public void exportPDF() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposViaticosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarPDF();
        exporter.export(context, tabla, "GRUPOSINFADICIONALES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void exportXLS() throws IOException {
        DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosGruposViaticosExportar");
        FacesContext context = FacesContext.getCurrentInstance();
        Exporter exporter = new ExportarXLS();
        exporter.export(context, tabla, "GRUPOSINFADICIONALES", false, false, "UTF-8", null, null);
        context.responseComplete();
    }

    public void verificarRastro() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (grupoViaticoSeleccionado != null) {
            int resultado = administrarRastros.obtenerTabla(grupoViaticoSeleccionado.getSecuencia(), "GRUPOSINFADICIONALES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
        } else if (administrarRastros.verificarHistoricosTabla("GRUPOSINFADICIONALES")) { // igual acá
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

    //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
    public List<GruposViaticos> getListGruposViaticos() {
        if (listGruposViaticos == null) {
            listGruposViaticos = administrarGruposViaticos.consultarGruposViaticos();
        }
        return listGruposViaticos;
    }

    public void setListGruposViaticos(List<GruposViaticos> listGruposViaticos) {
        this.listGruposViaticos = listGruposViaticos;
    }

    public List<GruposViaticos> getFiltrarGruposViaticos() {
        return filtrarGruposViaticos;
    }

    public void setFiltrarGruposViaticos(List<GruposViaticos> filtrarGruposViaticos) {
        this.filtrarGruposViaticos = filtrarGruposViaticos;
    }

    public GruposViaticos getNuevoGruposViaticos() {
        return nuevoGruposViaticos;
    }

    public void setNuevoGruposViaticos(GruposViaticos nuevoGruposViaticos) {
        this.nuevoGruposViaticos = nuevoGruposViaticos;
    }

    public GruposViaticos getDuplicarGruposViaticos() {
        return duplicarGruposViaticos;
    }

    public void setDuplicarGruposViaticos(GruposViaticos duplicarGruposViaticos) {
        this.duplicarGruposViaticos = duplicarGruposViaticos;
    }

    public GruposViaticos getEditarGruposViaticos() {
        return editarGruposViaticos;
    }

    public void setEditarGruposViaticos(GruposViaticos editarGruposViaticos) {
        this.editarGruposViaticos = editarGruposViaticos;
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

    public GruposViaticos getGrupoViaticoSeleccionado() {
        return grupoViaticoSeleccionado;
    }

    public void setGrupoViaticoSeleccionado(GruposViaticos grupoViaticoSeleccionado) {
        this.grupoViaticoSeleccionado = grupoViaticoSeleccionado;
    }

    public String getInfoRegistro() {
        FacesContext c = FacesContext.getCurrentInstance();
        DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosGruposViaticos");
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
