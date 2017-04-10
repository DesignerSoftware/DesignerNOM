/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.NovedadesOperandos;
import Entidades.Operandos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarNovedadesOperandosInterface;
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
public class ControlNovedadOperando implements Serializable {

   @EJB
   AdministrarNovedadesOperandosInterface administrarNovedadesOperandos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //Parametros que llegan
   private Operandos operando;
   //LISTA INFOREPORTES
   private List<NovedadesOperandos> listaNovedadesOperandos;
   private List<NovedadesOperandos> filtradosListaNovedadesOperandos;
   private NovedadesOperandos novedadOpeSeleccionada;
   //L.O.V INFOREPORTES
   private List<NovedadesOperandos> lovNovedadesOperandos;
   private List<NovedadesOperandos> lovfiltradosNovedadesOpe;
   private NovedadesOperandos novedadOpeLovSeleccionado;
   //editar celda
   private NovedadesOperandos editarNovedadesOperandos;
   private boolean aceptarEditar;
   private int cualCelda, tipoLista;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   //RASTROS
   private boolean guardado;
   //Crear Novedades
   private List<NovedadesOperandos> listaNovedadesOperandosCrear;
   public NovedadesOperandos nuevoNovedadOperando;
   public NovedadesOperandos duplicarNovedadOperando;
   private int k;
   private BigInteger l;
   private String mensajeValidacion;
   //Modificar Novedades
   private List<NovedadesOperandos> listaNovedadesOperandosModificar;
   //Borrar Novedades
   private List<NovedadesOperandos> listaNovedadesOperandosBorrar;
   //AUTOCOMPLETAR
   private String Operando;
   //Columnas Tabla Ciudades
   private Column novedadesOperandosNombre;
   //ALTO SCROLL TABLA
   private String altoTabla;
   private boolean cambiosPagina;
   //L.O.V OPERANDOS
   private List<Operandos> lovListaOperandos;
   private List<Operandos> lovFiltradosListaOperandos;
   private Operandos seleccionOperandos;
   private String paginaAnterior = "nominaf";
   public String infoRegistro;
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlNovedadOperando() {
      cambiosPagina = true;
      nuevoNovedadOperando = new NovedadesOperandos();
      aceptar = true;
      novedadOpeSeleccionada = null;
      guardado = true;
      tipoLista = 0;
      listaNovedadesOperandosBorrar = new ArrayList<NovedadesOperandos>();
      listaNovedadesOperandosCrear = new ArrayList<NovedadesOperandos>();
      listaNovedadesOperandosModificar = new ArrayList<NovedadesOperandos>();
      altoTabla = "275";
      duplicarNovedadOperando = new NovedadesOperandos();
      lovListaOperandos = null;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      operando = (Operandos) mapParametros.get("operandoActual");
      listaNovedadesOperandos = null;
      getListaNovedadesOperandos();
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
         String pagActual = "novedadoperando";
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
      limpiarListasValor();
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

   public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarNovedadesOperandos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   //UBICACION CELDA
   public void cambiarIndice(NovedadesOperandos novOp, int celda) {
      novedadOpeSeleccionada = novOp;
      cualCelda = celda;
   }

   //AUTOCOMPLETAR
   public void modificarNovedadesOperandos(NovedadesOperandos novOp, String confirmarCambio, String valorConfirmar) {
      novedadOpeSeleccionada = novOp;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (!listaNovedadesOperandosCrear.contains(novedadOpeSeleccionada)) {
            if (listaNovedadesOperandosModificar.isEmpty()) {
               listaNovedadesOperandosModificar.add(novedadOpeSeleccionada);
            } else if (!listaNovedadesOperandosModificar.contains(novedadOpeSeleccionada)) {
               listaNovedadesOperandosModificar.add(novedadOpeSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
            }
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
         contarRegistros();
      } else if (confirmarCambio.equalsIgnoreCase("OPERANDO")) {
         novedadOpeSeleccionada.getOperando().setNombre(Operando);
         for (int i = 0; i < lovListaOperandos.size(); i++) {
            if (lovListaOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            novedadOpeSeleccionada.setOperando(lovListaOperandos.get(indiceUnicoElemento));
            lovListaOperandos.clear();
            getLovListaOperandos();
         } else {
            RequestContext.getCurrentInstance().update("formularioDialogos:operandosDialogo");
            RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void asignarIndex(NovedadesOperandos novOp, int dlg, int tipoAct) {
      novedadOpeSeleccionada = novOp;
      tipoActualizacion = tipoAct;
      if (dlg == 0) {
         lovListaOperandos.clear();
         lovListaOperandos.add(operando);
         System.out.println("Operando en asignar Index" + operando);
         RequestContext.getCurrentInstance().update("formularioDialogos:operandosDialogo");
         RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
      }
   }

   public void asignarIndex(int dlg, int tipoAct) {
      tipoActualizacion = tipoAct;
      if (dlg == 0) {
         lovListaOperandos.clear();
         lovListaOperandos.add(operando);
         System.out.println("Operando en asignar Index" + operando);
         RequestContext.getCurrentInstance().update("formularioDialogos:operandosDialogo");
         RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
      }
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      if (novedadOpeSeleccionada != null) {
         if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("form:formulasDialogo");
            RequestContext.getCurrentInstance().execute("PF('formulasDialogo').show()");
            tipoActualizacion = 0;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {
      if (Campo.equals("OPERANDO")) {
         if (tipoNuevo == 1) {
            Operando = nuevoNovedadOperando.getOperando().getNombre();
         } else if (tipoNuevo == 2) {
            Operando = duplicarNovedadOperando.getOperando().getNombre();
         }
      }

   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("OPERANDO")) {
         if (tipoNuevo == 1) {
            nuevoNovedadOperando.getOperando().setNombre(Operando);
         } else if (tipoNuevo == 2) {
            duplicarNovedadOperando.getOperando().setNombre(Operando);
         }
         for (int i = 0; i < lovListaOperandos.size(); i++) {
            if (lovListaOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoNovedadOperando.setOperando(lovListaOperandos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperando");

            } else if (tipoNuevo == 2) {
               duplicarNovedadOperando.setOperando(lovListaOperandos.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperando");

            }
            lovListaOperandos.clear();
            getLovListaOperandos();
         } else {
            RequestContext.getCurrentInstance().update("form:operandosDialogo");
            RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperando");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperando");
            }
         }
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      novedadOpeSeleccionada = null;
      contarRegistros();
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void activarCtrlF11() {

      if (bandera == 0) {
         altoTabla = "255";
         novedadesOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesOperandos:novedadesOperandosNombre");
         novedadesOperandosNombre.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
         contarRegistros();
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

   public void restaurarTabla() {
      altoTabla = "275";
      System.out.println("Desactivar");
      System.out.println("TipoLista= " + tipoLista);
      novedadesOperandosNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosNovedadesOperandos:novedadesOperandosNombre");
      novedadesOperandosNombre.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
      contarRegistros();
      bandera = 0;
      filtradosListaNovedadesOperandos = null;
      tipoLista = 0;
   }

   public void cancelarYSalir() {
      cancelarModificacion();
      salir();
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         restaurarTabla();
      }
      listaNovedadesOperandosBorrar.clear();
      listaNovedadesOperandosCrear.clear();
      listaNovedadesOperandosModificar.clear();
      lovListaOperandos.clear();
      novedadOpeSeleccionada = null;
      k = 0;
      listaNovedadesOperandos = null;
      getListaNovedadesOperandos();
      guardado = true;
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
      contarRegistros();
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (novedadOpeSeleccionada != null) {
         editarNovedadesOperandos = novedadOpeSeleccionada;
         System.out.println("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarOperandos");
            RequestContext.getCurrentInstance().execute("PF('editarOperandos').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesOperandosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "NovedadesOperandosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosNovedadesOperandosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "NovedadesOperandosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //LIMPIAR NUEVO REGISTRO CIUDAD
   public void limpiarNuevoNovedadesOperandos() {
      nuevoNovedadOperando = new NovedadesOperandos();
   }

   public void limpiarduplicarNovedadesOperandos() {
      duplicarNovedadOperando = new NovedadesOperandos();
   }

   //DUPLICAR Operando
   public void duplicarNO() {
      if (novedadOpeSeleccionada != null) {
         duplicarNovedadOperando = new NovedadesOperandos();
         k++;
         l = BigInteger.valueOf(k);
         duplicarNovedadOperando.setSecuencia(l);
         duplicarNovedadOperando.setOperando(novedadOpeSeleccionada.getOperando());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNovedadOperando");
         RequestContext.getCurrentInstance().execute("PF('DuplicarNovedadOperando').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void guardarYSalir() {
      guardarCambiosNovedadesOperandos();
      salir();
   }

   //GUARDAR
   public void guardarCambiosNovedadesOperandos() {
      if (guardado == false) {
         if (!listaNovedadesOperandosBorrar.isEmpty()) {
            for (int i = 0; i < listaNovedadesOperandosBorrar.size(); i++) {
               System.out.println("Borrando..." + listaNovedadesOperandosBorrar.size());
               administrarNovedadesOperandos.borrarNovedadesOperandos(listaNovedadesOperandosBorrar.get(i));
            }
            System.out.println("Entra");
            listaNovedadesOperandosBorrar.clear();
         }
         if (!listaNovedadesOperandosCrear.isEmpty()) {
            for (int i = 0; i < listaNovedadesOperandosCrear.size(); i++) {
               System.out.println("Creando...");
               administrarNovedadesOperandos.crearNovedadesOperandos(listaNovedadesOperandosCrear.get(i));
            }
            System.out.println("LimpiaLista");
            listaNovedadesOperandosCrear.clear();
         }
         if (!listaNovedadesOperandosModificar.isEmpty()) {
            for (int i = 0; i < listaNovedadesOperandosModificar.size(); i++) {
               administrarNovedadesOperandos.modificarNovedadesOperandos(listaNovedadesOperandosModificar.get(i));
            }
            listaNovedadesOperandosModificar.clear();
         }
         listaNovedadesOperandos = null;
         novedadOpeSeleccionada = null;
         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
         contarRegistros();
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   //RASTROS 
   public void verificarRastro() {
      if (novedadOpeSeleccionada != null) {
         int result = administrarRastros.obtenerTabla(novedadOpeSeleccionada.getSecuencia(), "NOVEDADESOPERANDOS");
         System.out.println("resultado: " + result);
         if (result == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (result == 2) {
            RequestContext.getCurrentInstance().execute("PF('confirmarRastro').show()");
         } else if (result == 3) {
            RequestContext.getCurrentInstance().execute("PF('errorRegistroRastro').show()");
         } else if (result == 4) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaConRastro').show()");
         } else if (result == 5) {
            RequestContext.getCurrentInstance().execute("PF('errorTablaSinRastro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("NOVEDADESOPERANDOS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void actualizarOperando() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         novedadOpeSeleccionada.setOperando(seleccionOperandos);
         if (!listaNovedadesOperandosCrear.contains(novedadOpeSeleccionada)) {
            if (listaNovedadesOperandosModificar.isEmpty()) {
               listaNovedadesOperandosModificar.add(novedadOpeSeleccionada);
            } else if (!listaNovedadesOperandosModificar.contains(novedadOpeSeleccionada)) {
               listaNovedadesOperandosModificar.add(novedadOpeSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
         contarRegistros();
      } else if (tipoActualizacion == 1) {
         nuevoNovedadOperando.setOperando(seleccionOperandos);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoOperando");
      } else if (tipoActualizacion == 2) {
         duplicarNovedadOperando.setOperando(seleccionOperandos);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarOperando");
      }
      filtradosListaNovedadesOperandos = null;
      seleccionOperandos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("formularioDialogos:LOVOperandos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVOperandos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('operandosDialogo').hide()");
   }

   public void cancelarCambioOperandos() {
      lovFiltradosListaOperandos = null;
      seleccionOperandos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("formularioDialogos:LOVOperandos:globalFilter");
      context.execute("PF('LOVOperandos').clearFilters()");
      context.execute("PF('operandosDialogo').hide()");
   }

   public void agregarNuevoNovedadOperando() {
      int pasa = 0;
      int pasa2 = 0;
      mensajeValidacion = new String();
      if (nuevoNovedadOperando.getOperando().getNombre().equals(" ")) {
         mensajeValidacion = mensajeValidacion + " * Nombre\n";
         pasa++;
      }
      for (int i = 0; i < listaNovedadesOperandos.size(); i++) {
         if (nuevoNovedadOperando.getOperando().getNombre().equals(listaNovedadesOperandos.get(i).getOperando().getNombre())) {
            RequestContext.getCurrentInstance().update("formularioDialogos:operandorecalculado");
            RequestContext.getCurrentInstance().execute("PF('operandorecalculado').show()");
            pasa2++;
         }
      }
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoNovedadOperando");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoNovedadOperando').show()");
      }
      if (pasa == 0 && pasa2 == 0) {
         if (bandera == 1) {
            restaurarTabla();
         }
         //AGREGAR REGISTRO A LA LISTA NOVEDADES .
         k++;
         l = BigInteger.valueOf(k);
         nuevoNovedadOperando.setSecuencia(l);
         System.out.println("Operando: " + operando);
         nuevoNovedadOperando.setOperando(operando);

         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         listaNovedadesOperandosCrear.add(nuevoNovedadOperando);
         listaNovedadesOperandos.add(nuevoNovedadOperando);
         novedadOpeSeleccionada = listaNovedadesOperandos.get(listaNovedadesOperandos.indexOf(nuevoNovedadOperando));
         nuevoNovedadOperando = new NovedadesOperandos();
         RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoNovedadOperando').hide()");
      }
   }

   //BORRAR CIUDADES
   public void borrarNovedadOperando() {
      if (novedadOpeSeleccionada != null) {
         if (!listaNovedadesOperandosModificar.isEmpty() && listaNovedadesOperandosModificar.contains(novedadOpeSeleccionada)) {
            listaNovedadesOperandosModificar.remove(novedadOpeSeleccionada);
            listaNovedadesOperandosBorrar.add(novedadOpeSeleccionada);
         } else if (!listaNovedadesOperandosCrear.isEmpty() && listaNovedadesOperandosCrear.contains(novedadOpeSeleccionada)) {
            listaNovedadesOperandosCrear.remove(novedadOpeSeleccionada);
         } else {
            listaNovedadesOperandosBorrar.add(novedadOpeSeleccionada);
         }
         listaNovedadesOperandos.remove(novedadOpeSeleccionada);
         if (tipoLista == 1) {
            filtradosListaNovedadesOperandos.remove(novedadOpeSeleccionada);
         }
         novedadOpeSeleccionada = null;
         RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         restaurarTabla();
      }
      listaNovedadesOperandosBorrar.clear();
      listaNovedadesOperandosCrear.clear();
      listaNovedadesOperandosModificar.clear();
      lovListaOperandos.clear();
      novedadOpeSeleccionada = null;
      k = 0;
      listaNovedadesOperandos = null;
      guardado = true;
      navegar("atras");
   }

   public void confirmarDuplicar() {
      int pasa = 0;
      mensajeValidacion = new String();
      if (duplicarNovedadOperando.getOperando().getNombre() == null) {
         mensajeValidacion = mensajeValidacion + " * Nombre\n";
         pasa++;
      }
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoNovedadOperando");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoNovedadOperando').show()");
      }
      if (pasa == 0) {
         if (bandera == 1) {
            restaurarTabla();
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         duplicarNovedadOperando.setOperando(operando);
         listaNovedadesOperandos.add(duplicarNovedadOperando);
         listaNovedadesOperandosCrear.add(duplicarNovedadOperando);
         novedadOpeSeleccionada = listaNovedadesOperandos.get(listaNovedadesOperandos.indexOf(duplicarNovedadOperando));
         if (guardado == true) {
            guardado = false;
         }
         duplicarNovedadOperando = new NovedadesOperandos();
         RequestContext.getCurrentInstance().update("form:datosNovedadesOperandos");
         contarRegistros();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarNovedadOperando");
         RequestContext.getCurrentInstance().execute("PF('DuplicarNovedadOperando').hide()");
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   //Getter & Setter
   public List<NovedadesOperandos> getListaNovedadesOperandos() {
      if (listaNovedadesOperandos == null && operando != null) {
         listaNovedadesOperandos = administrarNovedadesOperandos.buscarNovedadesOperandos(operando.getSecuencia());
      }
      return listaNovedadesOperandos;
   }

   public void setListaNovedadesOperandos(List<NovedadesOperandos> listaNovedadesOperandos) {
      this.listaNovedadesOperandos = listaNovedadesOperandos;
   }

   public List<NovedadesOperandos> getFiltradosListaNovedadesOperandos() {
      return filtradosListaNovedadesOperandos;
   }

   public void setFiltradosListaNovedadesOperandos(List<NovedadesOperandos> filtradosListaNovedadesOperandos) {
      this.filtradosListaNovedadesOperandos = filtradosListaNovedadesOperandos;
   }

   public NovedadesOperandos getEditarNovedadesOperandos() {
      return editarNovedadesOperandos;
   }

   public void setEditarNovedadesOperandos(NovedadesOperandos editarNovedadesOperandos) {
      this.editarNovedadesOperandos = editarNovedadesOperandos;
   }

   public boolean isAceptarEditar() {
      return aceptarEditar;
   }

   public void setAceptarEditar(boolean aceptarEditar) {
      this.aceptarEditar = aceptarEditar;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public NovedadesOperandos getNuevoNovedadOperando() {
      return nuevoNovedadOperando;
   }

   public void setNuevoNovedadOperando(NovedadesOperandos nuevoNovedadOperando) {
      this.nuevoNovedadOperando = nuevoNovedadOperando;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public Operandos getOperando() {
      return operando;
   }

   public void setOperando(Operandos operando) {
      this.operando = operando;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean cambiosPagina) {
      this.cambiosPagina = cambiosPagina;
   }

   public NovedadesOperandos getDuplicarNovedadOperando() {
      return duplicarNovedadOperando;
   }

   public void setDuplicarNovedadOperando(NovedadesOperandos duplicarNovedadOperando) {
      this.duplicarNovedadOperando = duplicarNovedadOperando;
   }

   public List<Operandos> getLovListaOperandos() {
      if (lovListaOperandos == null) {
         lovListaOperandos = administrarNovedadesOperandos.buscarOperandos();
      }
      return lovListaOperandos;
   }

   public void setLovListaOperandos(List<Operandos> lovListaOperandos) {
      this.lovListaOperandos = lovListaOperandos;
   }

   public List<Operandos> getLovFiltradosListaOperandos() {
      return lovFiltradosListaOperandos;
   }

   public void setLovFiltradosListaOperandos(List<Operandos> lovFiltradosListaOperandos) {
      this.lovFiltradosListaOperandos = lovFiltradosListaOperandos;
   }

   public Operandos getSeleccionOperandos() {
      return seleccionOperandos;
   }

   public void setSeleccionOperandos(Operandos seleccionOperandos) {
      this.seleccionOperandos = seleccionOperandos;
   }

   public NovedadesOperandos getNovedadOpeSeleccionada() {
      return novedadOpeSeleccionada;
   }

   public void setNovedadOpeSeleccionada(NovedadesOperandos novedadOpeSeleccionada) {
      this.novedadOpeSeleccionada = novedadOpeSeleccionada;
   }

   public NovedadesOperandos getNovedadOpeLovSeleccionado() {
      return novedadOpeLovSeleccionado;
   }

   public void setNovedadOpeLovSeleccionado(NovedadesOperandos novedadOpeLovSeleccionado) {
      this.novedadOpeLovSeleccionado = novedadOpeLovSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosNovedadesOperandos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
