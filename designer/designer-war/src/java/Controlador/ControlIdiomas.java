/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Idiomas;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarIdiomasInterface;
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
public class ControlIdiomas implements Serializable {

   @EJB
   AdministrarIdiomasInterface administrarIdiomas;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<Idiomas> listIdiomas;
   private List<Idiomas> filtrarIdiomas;
   private List<Idiomas> crearIdiomas;
   private List<Idiomas> modificarIdiomas;
   private List<Idiomas> borrarIdiomas;
   private Idiomas nuevoIdiomas;
   private Idiomas duplicarIdiomas;
   private Idiomas editarIdiomas;
   private Idiomas idiomaSeleccionado;
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
   private Integer backupCodigo;
   private String backupDescripcion;
   private String infoRegistro;
   private DataTable tablaC;
   private boolean activarLov;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlIdiomas() {
      listIdiomas = null;
      crearIdiomas = new ArrayList<Idiomas>();
      modificarIdiomas = new ArrayList<Idiomas>();
      borrarIdiomas = new ArrayList<Idiomas>();
      permitirIndex = true;
      editarIdiomas = new Idiomas();
      nuevoIdiomas = new Idiomas();
      duplicarIdiomas = new Idiomas();
      guardado = true;
      tamano = 270;
      activarLov = true;
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
      /*if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual);

      } else {
         */
String pagActual = "idioma";
         
         
         


         
         
         
         
         
         
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
         administrarIdiomas.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void cambiarIndice(Idiomas idioma, int celda) {
      if (permitirIndex == true) {
         idiomaSeleccionado = idioma;
         cualCelda = celda;
         idiomaSeleccionado.getSecuencia();
         if (tipoLista == 0) {
            backupCodigo = idiomaSeleccionado.getCodigo();
            backupDescripcion = idiomaSeleccionado.getNombre();
         } else if (tipoLista == 1) {
            backupCodigo = idiomaSeleccionado.getCodigo();
            backupDescripcion = idiomaSeleccionado.getNombre();
         }
      }
   }

   public void recibirPag(String pagina) {
      paginaAnterior = pagina;
      listIdiomas = null;
      getListIdiomas();
      deshabilitarBotonLov();
      if (!listIdiomas.isEmpty()) {
         idiomaSeleccionado = listIdiomas.get(0);
      }
   }

   public String retornarPagina() {
      return paginaAnterior;
   }

   public void asignarIndex(Idiomas idioma, int LND, int dig) {
      try {
         idiomaSeleccionado = idioma;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }

      } catch (Exception e) {
         System.out.println("ERROR ControlIdiomas.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosIdiomas:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosIdiomas:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosIdiomas");
         bandera = 0;
         filtrarIdiomas = null;
         tipoLista = 0;
      }

      borrarIdiomas.clear();
      crearIdiomas.clear();
      modificarIdiomas.clear();
      idiomaSeleccionado = null;
      k = 0;
      listIdiomas = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      getListIdiomas();
      contarRegistros();
      context.update("form:informacionRegistro");
      context.update("form:datosIdiomas");
      context.update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         codigo = (Column) c.getViewRoot().findComponent("form:datosIdiomas:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosIdiomas:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosIdiomas");
         bandera = 0;
         filtrarIdiomas = null;
         tipoLista = 0;
      }

      borrarIdiomas.clear();
      crearIdiomas.clear();
      modificarIdiomas.clear();
      idiomaSeleccionado = null;
      k = 0;
      listIdiomas = null;
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      getListIdiomas();
      contarRegistros();
      context.update("form:informacionRegistro");
      context.update("form:datosIdiomas");
      context.update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosIdiomas:codigo");
         codigo.setFilterStyle("width: 85% !important");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosIdiomas:descripcion");
         descripcion.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosIdiomas");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         tamano = 270;
         codigo = (Column) c.getViewRoot().findComponent("form:datosIdiomas:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         descripcion = (Column) c.getViewRoot().findComponent("form:datosIdiomas:descripcion");
         descripcion.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosIdiomas");
         bandera = 0;
         filtrarIdiomas = null;
         tipoLista = 0;
      }
   }

   public void modificarIdiomas(Idiomas idioma) {
      idiomaSeleccionado = idioma;
      if (!crearIdiomas.contains(idiomaSeleccionado)) {
         if (modificarIdiomas.isEmpty()) {
            modificarIdiomas.add(idiomaSeleccionado);
         } else if (!modificarIdiomas.contains(idiomaSeleccionado)) {
            modificarIdiomas.add(idiomaSeleccionado);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosIdiomas");
   }

   public void borrandoIdiomas() {

      if (idiomaSeleccionado != null) {
         if (!modificarIdiomas.isEmpty() && modificarIdiomas.contains(idiomaSeleccionado)) {
            int modIndex = modificarIdiomas.indexOf(idiomaSeleccionado);
            modificarIdiomas.remove(modIndex);
            borrarIdiomas.add(idiomaSeleccionado);
         } else if (!crearIdiomas.isEmpty() && crearIdiomas.contains(idiomaSeleccionado)) {
            int crearIndex = crearIdiomas.indexOf(idiomaSeleccionado);
            crearIdiomas.remove(crearIndex);
         } else {
            borrarIdiomas.add(idiomaSeleccionado);
         }
         listIdiomas.remove(idiomaSeleccionado);
         if (tipoLista == 1) {
            filtrarIdiomas.remove(idiomaSeleccionado);
         }
         contarRegistros();
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosIdiomas");
         idiomaSeleccionado = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }

   }

   public void verificarBorrado() {
      System.out.println("Estoy en verificarBorrado");
      BigInteger verificarBorradoIdiomasPersonas;

      try {
         System.err.println("Control Secuencia de ControlIdiomas ");
         if (tipoLista == 0) {
            verificarBorradoIdiomasPersonas = administrarIdiomas.verificarBorradoIdiomasPersonas(idiomaSeleccionado.getSecuencia());
         } else {
            verificarBorradoIdiomasPersonas = administrarIdiomas.verificarBorradoIdiomasPersonas(idiomaSeleccionado.getSecuencia());
         }
         if (verificarBorradoIdiomasPersonas.equals(new BigInteger("0"))) {
            System.out.println("Borrado==0");
            borrandoIdiomas();
         } else {
            System.out.println("Borrado>0");

            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:validacionBorrar");
            RequestContext.getCurrentInstance().execute("PF('validacionBorrar').show()");
            idiomaSeleccionado = null;
            verificarBorradoIdiomasPersonas = new BigInteger("-1");

         }
      } catch (Exception e) {
         System.err.println("ERROR ControlIdiomas verificarBorrado ERROR " + e);
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarIdiomas.isEmpty() || !crearIdiomas.isEmpty() || !modificarIdiomas.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarIdiomas() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         if (!borrarIdiomas.isEmpty()) {
            administrarIdiomas.borrarIdiomas(borrarIdiomas);
            //mostrarBorrados
            registrosBorrados = borrarIdiomas.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarIdiomas.clear();
         }
         if (!modificarIdiomas.isEmpty()) {
            administrarIdiomas.modificarIdiomas(modificarIdiomas);
            modificarIdiomas.clear();
         }
         if (!crearIdiomas.isEmpty()) {
            administrarIdiomas.crearIdiomas(crearIdiomas);
            crearIdiomas.clear();
         }
         listIdiomas = null;
         getListIdiomas();
         contarRegistros();
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosIdiomas");
         k = 0;
         guardado = true;
      }
      idiomaSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (idiomaSeleccionado != null) {
         if (tipoLista == 0) {
            editarIdiomas = idiomaSeleccionado;
         }
         if (tipoLista == 1) {
            editarIdiomas = idiomaSeleccionado;
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
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }

   }

   public void agregarNuevoIdiomas() {
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (nuevoIdiomas.getCodigo() == a) {
         mensajeValidacion = "Campo Código vacío \n";
      } else {
         for (int x = 0; x < listIdiomas.size(); x++) {
            if (listIdiomas.get(x).getCodigo().equals(nuevoIdiomas.getCodigo())) {
               duplicados++;
            }
         }

         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado ya existe \n";
         } else {
            contador++;
         }
      }
      if (nuevoIdiomas.getNombre() == null || nuevoIdiomas.getNombre().isEmpty()) {
         mensajeValidacion = "Campo Idioma vacío \n";

      } else {
         contador++;

      }

      if (contador == 2) {
         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            //CERRAR FILTRADO
            System.out.println("Desactivar");
            codigo = (Column) c.getViewRoot().findComponent("form:datosIdiomas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosIdiomas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIdiomas");
            bandera = 0;
            filtrarIdiomas = null;
            tipoLista = 0;
            tamano = 270;
         }
         System.out.println("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoIdiomas.setSecuencia(l);
         crearIdiomas.add(nuevoIdiomas);
         listIdiomas.add(nuevoIdiomas);
         idiomaSeleccionado = nuevoIdiomas;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         nuevoIdiomas = new Idiomas();
         RequestContext.getCurrentInstance().update("form:datosIdiomas");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroIdiomas').hide()");
      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevoIdioma");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoIdioma').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoIdiomas() {
      nuevoIdiomas = new Idiomas();

   }

   //------------------------------------------------------------------------------
   public void duplicandoIdiomas() {
      System.out.println("duplicandoIdiomas");
      if (idiomaSeleccionado != null) {
         duplicarIdiomas = new Idiomas();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarIdiomas.setSecuencia(l);
            duplicarIdiomas.setCodigo(idiomaSeleccionado.getCodigo());
            duplicarIdiomas.setNombre(idiomaSeleccionado.getNombre());
         }
         if (tipoLista == 1) {
            duplicarIdiomas.setSecuencia(l);
            duplicarIdiomas.setCodigo(idiomaSeleccionado.getCodigo());
            duplicarIdiomas.setNombre(idiomaSeleccionado.getNombre());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroIdiomas').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;
      if (duplicarIdiomas.getCodigo() == a) {
         mensajeValidacion = "Campo código vacío´\n";
      } else {
         for (int x = 0; x < listIdiomas.size(); x++) {
            if (listIdiomas.get(x).getCodigo().equals(duplicarIdiomas.getCodigo())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "El código ingresado ya existe \n";
         } else {
            contador++;
            duplicados = 0;
         }
      }
      if (duplicarIdiomas.getNombre() == null || duplicarIdiomas.getNombre().isEmpty()) {
         mensajeValidacion = "Campo Idioma vacío \n";

      } else {
         contador++;
      }

      if (contador == 2) {
         if (crearIdiomas.contains(duplicarIdiomas)) {
            System.out.println("Ya lo contengo.");
         }
         listIdiomas.add(duplicarIdiomas);
         crearIdiomas.add(duplicarIdiomas);
         RequestContext.getCurrentInstance().update("form:datosIdiomas");
         idiomaSeleccionado = duplicarIdiomas;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            codigo = (Column) c.getViewRoot().findComponent("form:datosIdiomas:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            descripcion = (Column) c.getViewRoot().findComponent("form:datosIdiomas:descripcion");
            descripcion.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosIdiomas");
            bandera = 0;
            filtrarIdiomas = null;
            tipoLista = 0;
            tamano = 270;
         }
         duplicarIdiomas = new Idiomas();
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroIdiomas').hide()");

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarIdioma");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarIdioma').show()");
      }
   }

   public void limpiarDuplicarIdiomas() {
      duplicarIdiomas = new Idiomas();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIdiomasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "IDIOMAS", false, false, "UTF-8", null, null);
      context.responseComplete();
      idiomaSeleccionado = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosIdiomasExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "IDIOMAS", false, false, "UTF-8", null, null);
      context.responseComplete();
      idiomaSeleccionado = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (idiomaSeleccionado != null) {
         System.out.println("lol 2");
         int resultado = administrarRastros.obtenerTabla(idiomaSeleccionado.getSecuencia(), "IDIOMAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("IDIOMAS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void eventoFiltrar() {
      try {
         System.out.println("\n ENTRE A ControlIdiomas.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         contarRegistros();
      } catch (Exception e) {
         System.out.println("ERROR ControlIdiomas eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void recordarSeleccion() {
      if (idiomaSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosIdiomas");
         tablaC.setSelection(idiomaSeleccionado);
      }
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<Idiomas> getListIdiomas() {
      if (listIdiomas == null) {
         listIdiomas = administrarIdiomas.mostrarIdiomas();
      }
      return listIdiomas;
   }

   public void setListIdiomas(List<Idiomas> listIdiomas) {
      this.listIdiomas = listIdiomas;
   }

   public List<Idiomas> getFiltrarIdiomas() {
      return filtrarIdiomas;
   }

   public void setFiltrarIdiomas(List<Idiomas> filtrarIdiomas) {
      this.filtrarIdiomas = filtrarIdiomas;
   }

   public Idiomas getNuevoIdiomas() {
      return nuevoIdiomas;
   }

   public void setNuevoIdiomas(Idiomas nuevoIdiomas) {
      this.nuevoIdiomas = nuevoIdiomas;
   }

   public Idiomas getDuplicarIdiomas() {
      return duplicarIdiomas;
   }

   public void setDuplicarIdiomas(Idiomas duplicarIdiomas) {
      this.duplicarIdiomas = duplicarIdiomas;
   }

   public Idiomas getEditarIdiomas() {
      return editarIdiomas;
   }

   public void setEditarIdiomas(Idiomas editarIdiomas) {
      this.editarIdiomas = editarIdiomas;
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

   public Idiomas getIdiomaSeleccionado() {
      return idiomaSeleccionado;
   }

   public void setIdiomaSeleccionado(Idiomas idiomaSeleccionado) {
      this.idiomaSeleccionado = idiomaSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosIdiomas");
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

   public String getPaginaanterior() {
      return paginaAnterior;
   }

   public void setPaginaanterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

}
