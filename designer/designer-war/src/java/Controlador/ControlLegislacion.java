package Controlador;

import Entidades.Contratos;
import Entidades.TiposCotizantes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarContratosInterface;
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

@ManagedBean
@SessionScoped
public class ControlLegislacion implements Serializable {

   @EJB
   AdministrarContratosInterface administrarContratos;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<Contratos> listaContratos;
   private List<Contratos> filtradolistaContratos;
   private Contratos contratoSeleccionado;
   private List<Contratos> lovContratos;
   private List<Contratos> filtradoListaContratosLOV;
   private Contratos contratoLovSeleccionado;
   private boolean verSeleccionContrato;
   private boolean verMostrarTodos;
   private boolean mostrarTodos;
   private List<TiposCotizantes> lovTiposCotizantes;
   private TiposCotizantes tipoCotizanteLovSeleccionado;
   private List<TiposCotizantes> filtradoListaTiposCotizantes;
   private int tipoActualizacion;
   private boolean permitirIndex;
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas Tabla 
   private Column columnaCodigo, columnaDescripción, columnaTipoCotizante, columnaEstado;
   //Otros
   private boolean aceptar;
   private String altoTabla;
   //modificar
   private List<Contratos> listaContratosModificar;
   private boolean guardado;
   //crear
   public Contratos nuevoContrato;
   private List<Contratos> listaContratosCrear;
   private BigInteger l;
   private int k;
   private String mensajeValidacion;
   //borrar
   private List<Contratos> listaContratosBorrar;
   //editar celda
   private Contratos editarContrato;
   private int cualCelda, tipoLista;
//    private boolean cambioEditor, aceptarEditar;
   //duplicar
   private Contratos duplicarContrato;
   //AUTOCOMPLETAR
   private String tipoCotizanteBack;
   private Short codigoBack;
   //REPRODUCIR CONTRATO
   private Contratos contratoOriginal;
   private Contratos contratoClon;
   private int cambioContrato;
   ///
   private String infoRegistro;
   private String infoRegistroTipo, infoRegistroContrato;
   //Activar boton LOV
   private boolean activarLOV;
   private DataTable tablaT;
   private String permitirCambioBotonLov;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlLegislacion() {
      contratoSeleccionado = null;
      lovContratos = null;
      lovTiposCotizantes = null;
      //Otros
      aceptar = true;
      listaContratosBorrar = new ArrayList<Contratos>();
      listaContratosCrear = new ArrayList<Contratos>();
      listaContratosModificar = new ArrayList<Contratos>();
      k = 0;
      //editar
      editarContrato = new Contratos();
      mostrarTodos = true;
      cualCelda = -1;
      tipoLista = 0;
      //guardar
      guardado = true;
      nuevoContrato = new Contratos();
      duplicarContrato = new Contratos();
      //CLON
      contratoClon = new Contratos();
      contratoOriginal = new Contratos();
      permitirIndex = true;
      altoTabla = "230";
      //
      contratoSeleccionado = null;
      activarLOV = true;
      permitirCambioBotonLov = "SIapagarCelda";
      //
      codigoBack = new Short("0");
      tipoCotizanteBack = "";
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listaContratos = null;
      getListaContratos();
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listaContratos = null;
      getListaContratos();
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
         String pagActual = "legislacion";
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
      limpiarListasValor();fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

  public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext context = FacesContext.getCurrentInstance();
         HttpSession sesion = (HttpSession) context.getExternalContext().getSession(false);
         administrarContratos.obtenerConexion(sesion.getId());
         administrarRastros.obtenerConexion(sesion.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void dispararDialogoConfirmarGuardar() {
      if (!listaContratosModificar.isEmpty()) {
         System.out.println("dispararDialogoConfirmarGuardar. listaContratosModificar : " + listaContratosModificar);
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
      RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
   }

   public String redirigir() {
      return paginaAnterior;
   }

   public void cambiarIndice(Contratos contrato, int celda) {
      if (permitirIndex == true) {
         System.out.println("Entro en cambiarIndice()");
         contratoSeleccionado = contrato;
         cualCelda = celda;
         if (cualCelda == 2) {
            permitirCambioBotonLov = "NOapagarCelda";
            tipoCotizanteBack = contratoSeleccionado.getTipocotizante().getDescripcion();
            activarBotonLOV();
         } else {
            permitirCambioBotonLov = "SoloHacerNull";
            anularBotonLOV();
         }
         if (cualCelda == 0) {
            codigoBack = contrato.getCodigo();
         }
         tipoCotizanteBack = contratoSeleccionado.getTipocotizante().getDescripcion();
      }
   }

   public void cambiarIndiceDefault() {
      System.out.println("cambiarIndiceDefault()");
      if (permitirCambioBotonLov.equals("SoloHacerNull")) {
         anularBotonLOV();
      } else if (permitirCambioBotonLov.equals("SIapagarCelda")) {
         anularBotonLOV();
         cualCelda = -1;
      } else if (permitirCambioBotonLov.equals("NOapagarCelda")) {
         activarBotonLOV();
      }
      permitirCambioBotonLov = "SIapagarCelda";
   }

   public void verDetalle(Contratos contrato) {
      contratoLovSeleccionado = contrato;
   }

   public String redirigirGuardar() {
      guardarCambios();
      return paginaAnterior;
   }

   public String redirigirSalir() {
      salir();
      return paginaAnterior;
   }
   //TipoCotizante-----------------------------------------------------------

   /*
     * Metodo encargado de actualizar el actualizarTipoCotizante de una
     * Legislacion
    */
   public void actualizarTipoCotizante() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         contratoSeleccionado.setTipocotizante(tipoCotizanteLovSeleccionado);
         if (!listaContratosCrear.contains(contratoSeleccionado)) {
            if (listaContratosModificar.isEmpty()) {
               listaContratosModificar.add(contratoSeleccionado);
            } else if (!listaContratosModificar.contains(contratoSeleccionado)) {
               listaContratosModificar.add(contratoSeleccionado);
            }
         }

         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosContratos");
      } else if (tipoActualizacion == 1) {
         nuevoContrato.setTipocotizante(tipoCotizanteLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDescripcionTipoCotizante");
      } else if (tipoActualizacion == 2) {
         duplicarContrato.setTipocotizante(tipoCotizanteLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDescripcionTipoCotizante");
      }
      filtradoListaTiposCotizantes = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;

      context.reset("form:lovTiposCotizantes:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposCotizantes').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:tiposCotizantesDialogo");
      RequestContext.getCurrentInstance().update("form:lovTiposCotizantes");
      RequestContext.getCurrentInstance().update("form:aceptarTC");
   }

   public void cancelarTipoCotizante() {
      RequestContext context = RequestContext.getCurrentInstance();
      filtradoListaTiposCotizantes = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      context.reset("form:lovTiposCotizantes:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposCotizantes').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:tiposCotizantesDialogo");
      RequestContext.getCurrentInstance().update("form:lovTiposCotizantes");
      RequestContext.getCurrentInstance().update("form:aceptarTC");
   }
   //OTROS---------------------------------------------------------------------

   /*
     * Metodo encargado de cambiar el valor booleano para habilitar un boton
    */
   public void activarAceptar() {
      aceptar = false;
   }

   //Metodo utilizado 'para Modificar una legislacion
   public void modificarContrato(Contratos contrato, String column, String valor) {
      contratoSeleccionado = contrato;
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (column.equalsIgnoreCase("N")) {
         int control = 0;
         System.out.println("modificarContrato Estado : " + contratoSeleccionado.getEstado());
         for (int i = 0; i < listaContratos.size(); i++) {
            if (listaContratos.get(i) == contratoSeleccionado) {
               i++;
               if (i >= listaContratos.size()) {
                  break;
               }
            }
         }
         if (control == 0) {
            if (!listaContratosCrear.contains(contratoSeleccionado)) {
               if (listaContratosModificar.isEmpty()) {
                  listaContratosModificar.add(contratoSeleccionado);
               } else if (!listaContratosModificar.contains(contratoSeleccionado)) {
                  listaContratosModificar.add(contratoSeleccionado);
               }
            }
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      } else if (column.equalsIgnoreCase("TC")) {

         contratoSeleccionado.getTipocotizante().setDescripcion(tipoCotizanteBack);

         for (int i = 0; i < lovTiposCotizantes.size(); i++) {
            if (lovTiposCotizantes.get(i).getDescripcion().startsWith(valor.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            contratoSeleccionado.setTipocotizante(lovTiposCotizantes.get(indiceUnicoElemento));
         } else {
            permitirIndex = false;
            tipoActualizacion = 0;
            RequestContext.getCurrentInstance().update("form:tiposCotizantesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').show()");
         }
      }
      if (column.equalsIgnoreCase("COD")) {
         contratoSeleccionado.setCodigo(codigoBack);
         Short cod = new Short(valor);
         System.out.println(" modificar cod = " + cod);
         for (int i = 0; i < listaContratos.size(); i++) {
            if (listaContratos.get(i).getCodigo().equals(cod)) {
               System.out.println(" modificar codigo 1 igual");
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias > 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigo");
            RequestContext.getCurrentInstance().execute("PF('validacionCodigo').show()");
            tipoActualizacion = 0;
         } else {
            contratoSeleccionado.setCodigo(cod);
            coincidencias = 1;
         }
      }
      if (coincidencias == 1) {
         if (!listaContratosCrear.contains(contratoSeleccionado)) {
            if (listaContratosModificar.isEmpty() || !listaContratosModificar.contains(contratoSeleccionado)) {
               listaContratosModificar.add(contratoSeleccionado);
            }
         }
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      RequestContext.getCurrentInstance().update("form:datosContratos");
   }

   //Metodos para Autocompletar
   public void valoresBackupAutocompletar(int tipoNuevo) {
      if (tipoNuevo == 1) {
         tipoCotizanteBack = nuevoContrato.getTipocotizante().getDescripcion();
      } else if (tipoNuevo == 2) {
         tipoCotizanteBack = duplicarContrato.getTipocotizante().getDescripcion();
      }
   }

   public void autocompletarNuevoyDuplicado(String valorConfirmar, int tipoNuevo) {
      RequestContext context = RequestContext.getCurrentInstance();
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (!valorConfirmar.isEmpty()) {
         if (tipoNuevo == 1) {
            nuevoContrato.getTipocotizante().setDescripcion(tipoCotizanteBack);
         } else if (tipoNuevo == 2) {
            duplicarContrato.getTipocotizante().setDescripcion(tipoCotizanteBack);
         }
         for (int i = 0; i < lovTiposCotizantes.size(); i++) {
            if (lovTiposCotizantes.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoContrato.setTipocotizante(lovTiposCotizantes.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDescripcionTipoCotizante");
            } else if (tipoNuevo == 2) {
               duplicarContrato.setTipocotizante(lovTiposCotizantes.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionTipoCotizante");
            }
         } else {
            RequestContext.getCurrentInstance().update("form:tiposCotizantesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoMotivo");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDescripcionTipoCotizante");
            }
         }
      }
   }

   public void guardarCambios() {
      System.out.println("listaContratosModificar : " + listaContratosModificar);

      RequestContext context = RequestContext.getCurrentInstance();
      try {
         if (guardado == false) {
            if (!listaContratosBorrar.isEmpty()) {
               administrarContratos.borrarConceptos(listaContratosBorrar);
               listaContratosBorrar.clear();
            }
            if (!listaContratosCrear.isEmpty()) {
               administrarContratos.crearConceptos(listaContratosCrear);
               listaContratosCrear.clear();
            }
            if (!listaContratosModificar.isEmpty()) {
               administrarContratos.modificarConceptos(listaContratosModificar);
               listaContratosModificar.clear();
            }
            listaContratos = null;
            getListaContratos();
            contarRegistros();
            RequestContext.getCurrentInstance().update("form:datosContratos");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            contratoSeleccionado = null;
            if (verSeleccionContrato == true) {
               lovContratos(0);
            }
            if (verMostrarTodos == true) {
               mostrarTodosContratos();
            }
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
         }
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void llamarDialogosLista(Contratos contrato) {
      contratoSeleccionado = contrato;
      tipoActualizacion = 0;
      getLovTiposCotizantes();
      contarRegistrosLovTiposCot();
      RequestContext.getCurrentInstance().update("form:tiposCotizantesDialogo");
      RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').show()");
   }

   public void agregarNuevoContrato() {
      RequestContext context = RequestContext.getCurrentInstance();
      int codigoRepetido = 0;
      if (nuevoContrato.getDescripcion() == null || nuevoContrato.getEstado() == null || nuevoContrato.getCodigo() == null) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoContrato");
         RequestContext.getCurrentInstance().execute("PF('validacioNuevoContrato').show()");
      } else {
         for (int j = 0; j < listaContratos.size(); j++) {
            if (nuevoContrato.getCodigo().equals(listaContratos.get(j).getCodigo())) {
               codigoRepetido++;
            }
         }
         if (codigoRepetido > 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigo");
            RequestContext.getCurrentInstance().execute("PF('validacionCodigo').show()");
         } else {
            k++;
            l = BigInteger.valueOf(k);
            nuevoContrato.setSecuencia(l);
            if (nuevoContrato.getTipocotizante().getSecuencia() == null) {
               nuevoContrato.setTipocotizante(null);
            }
            listaContratosCrear.add(nuevoContrato);
            listaContratos.add(nuevoContrato);
            contratoSeleccionado = listaContratos.get(listaContratos.indexOf(nuevoContrato));
            nuevoContrato = new Contratos();
            contarRegistros();
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().execute("PF('NuevoContratoDialogo').hide()");
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoContratoDialogo");
         }
      }
      if (bandera == 1) {
         restaurarTabla();
      }
   }

   //DIALOGO PARA INSERTAR UN CONTRATO
   public void dialogoIngresoNuevoRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('NuevoContratoDialogo').show()");
      RequestContext.getCurrentInstance().update("form:detalleFormula");
   }

//LIMPIAR NUEVO REGISTRO
   public void limpiarNuevoContrato() {
      nuevoContrato = new Contratos();
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (contratoSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (contratoSeleccionado != null) {
         editarContrato = contratoSeleccionado;

         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editorCodigo");
            RequestContext.getCurrentInstance().execute("PF('editorCodigo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editorDescripcion");
            RequestContext.getCurrentInstance().execute("PF('editorDescripcion').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editorTipoCotizante");
            RequestContext.getCurrentInstance().execute("PF('editorTipoCotizante').show()");
            cualCelda = -1;
         }
      }
   }

   public void borrarContrato() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (contratoSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (contratoSeleccionado != null) {
         if (!listaContratosModificar.isEmpty() && listaContratosModificar.contains(contratoSeleccionado)) {
            int modIndex = listaContratosModificar.indexOf(contratoSeleccionado);
            listaContratosModificar.remove(modIndex);
            listaContratosBorrar.add(contratoSeleccionado);
         } else if (!listaContratosCrear.isEmpty() && listaContratosCrear.contains(contratoSeleccionado)) {
            int crearIndex = listaContratosCrear.indexOf(contratoSeleccionado);
            listaContratosCrear.remove(crearIndex);
         } else {
            listaContratosBorrar.add(contratoSeleccionado);
         }
         listaContratos.remove(contratoSeleccionado);
         if (tipoLista == 1) {
            filtradolistaContratos.remove(contratoSeleccionado);
         }
         contratoSeleccionado = null;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosContratos");
         if (guardado) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   //METODOS PARA DUPLICAR
   public void duplicarRegistro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (contratoSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (contratoSeleccionado != null) {
         duplicarContrato.setCodigo(contratoSeleccionado.getCodigo());
         duplicarContrato.setDescripcion(contratoSeleccionado.getDescripcion());
         duplicarContrato.setTipocotizante(contratoSeleccionado.getTipocotizante());
         duplicarContrato.setEstado(contratoSeleccionado.getEstado());

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarContrato");
         RequestContext.getCurrentInstance().execute("PF('DuplicarContratoDialogo').show()");
      }
   }

   public void confirmarDuplicar() {
      RequestContext context = RequestContext.getCurrentInstance();
      int codigoRepetido = 0;
      if (duplicarContrato.getDescripcion() == null || duplicarContrato.getEstado() == null || duplicarContrato.getCodigo() == null) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacioNuevoContrato");
         RequestContext.getCurrentInstance().execute("PF('validacioNuevoContrato').show()");
      } else {
         for (int j = 0; j < listaContratos.size(); j++) {
            if (duplicarContrato.getCodigo().equals(listaContratos.get(j).getCodigo())) {
               codigoRepetido++;
            }
         }
         if (codigoRepetido > 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:validacionCodigo");
            RequestContext.getCurrentInstance().execute("PF('validacionCodigo').show()");
         } else {
            k++;
            l = BigInteger.valueOf(k);
            duplicarContrato.setSecuencia(l);
            if (duplicarContrato.getTipocotizante().getSecuencia() == null) {
               duplicarContrato.setTipocotizante(null);
            }
            listaContratosCrear.add(duplicarContrato);
            listaContratos.add(duplicarContrato);
            contratoSeleccionado = listaContratos.get(listaContratos.indexOf(duplicarContrato));
            duplicarContrato = new Contratos();
            contarRegistros();
            if (guardado) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarContratoDialogo");
            RequestContext.getCurrentInstance().execute("PF('DuplicarContratoDialogo').hide()");
         }
      }
      if (bandera == 1) {
         restaurarTabla();
      }
   }

   public void llamarLOVNuevo_Duplicado(int tipoAct) {
      tipoActualizacion = tipoAct;
      contarRegistrosLovTiposCot();
      RequestContext.getCurrentInstance().update("form:tiposCotizantesDialogo");
      RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').show()");

   }

//METODOS PARA LIMPIAR:
   //LIMPIAR DUPLICAR
   public void limpiarduplicar() {
      duplicarContrato = new Contratos();
   }

   public void refrescar() {
      System.out.println("ControlLegislacion.refrescar");
      if (bandera == 1) {
         restaurarTabla();
      }
      listaContratosBorrar.clear();
      listaContratosCrear.clear();
      listaContratosModificar.clear();
      contratoSeleccionado = null;
      k = 0;
      listaContratos = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      permitirIndex = true;
      mostrarTodos = true;
      contratoClon = new Contratos();
      contratoOriginal = new Contratos();
      if (verSeleccionContrato == true) {
         lovContratos(0);
      }
      if (verMostrarTodos == true) {
         mostrarTodosContratos();
      }
      RequestContext context = RequestContext.getCurrentInstance();
      getListaContratos();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      RequestContext.getCurrentInstance().update("form:mostrarTodos");
      RequestContext.getCurrentInstance().update("form:datosContratos");
      RequestContext.getCurrentInstance().update("form:descripcionContrato1");
      RequestContext.getCurrentInstance().update("form:descripcionContrato2");
   }

   //LISTA DE VALORES DINAMICA
   public void listaValoresBoton() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (contratoSeleccionado == null) {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      } else if (contratoSeleccionado != null) {
         if (cualCelda == 2) {
            contarRegistrosLovTiposCot();
            RequestContext.getCurrentInstance().update("form:tiposCotizantesDialogo");
            RequestContext.getCurrentInstance().execute("PF('tiposCotizantesDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   //CTRL + F11 ACTIVAR/DESACTIVAR
   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         altoTabla = "210";
         columnaCodigo = (Column) c.getViewRoot().findComponent("form:datosContratos:columnaCodigo");
         columnaCodigo.setFilterStyle("width: 85% !important;");
         columnaDescripción = (Column) c.getViewRoot().findComponent("form:datosContratos:columnaDescripción");
         columnaDescripción.setFilterStyle("width: 85% !important;");
         columnaTipoCotizante = (Column) c.getViewRoot().findComponent("form:datosContratos:columnaTipoCotizante");
         columnaTipoCotizante.setFilterStyle("width: 85% !important;");
         columnaEstado = (Column) c.getViewRoot().findComponent("form:datosContratos:columnaEstado");
         columnaEstado.setFilterStyle("width: 80% !important;");
         RequestContext.getCurrentInstance().update("form:datosContratos");
         bandera = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

//METODO PARA CLONAR UN CONTRATO
   public void lovContratos(int quien) {
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:detalleFormula");
      if (quien == 0) {
         if (guardado) {
            listaContratos = null;
            getListaContratos();
            contarRegistrosLovC();
            RequestContext.getCurrentInstance().update("form:ContratosDialogo");
            RequestContext.getCurrentInstance().execute("PF('ContratosDialogo').show()");
            verSeleccionContrato = false;
            cambioContrato = 0;
         } else {
            verSeleccionContrato = true;
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
         }
      } else if (quien == 1) {
         lovContratos = null;
         getLovContratos();
         contarRegistrosLovC();
         RequestContext.getCurrentInstance().update("form:ContratosDialogo");
         RequestContext.getCurrentInstance().execute("PF('ContratosDialogo').show()");
         cambioContrato = 1;
      } else if (quien == 2) {
         cambioContrato = 2;
         lovContratos = null;
         getLovContratos();
         contarRegistrosLovC();
         RequestContext.getCurrentInstance().update("form:ContratosDialogo");
         RequestContext.getCurrentInstance().execute("PF('ContratosDialogo').show()");
      }
      contratoSeleccionado = null;
      anularBotonLOV();
   }

//CLONAR CONTRATO
   public void reproducirContrato() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (contratoClon.getSecuencia() == null) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionReproducirContratoClon");
         RequestContext.getCurrentInstance().execute("PF('validacionReproducirContratoClon').show()");
      } else if (contratoOriginal.getSecuencia() == null) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionReproducirContratOrigen");
         RequestContext.getCurrentInstance().execute("PF('validacionReproducirContratOrigen').show()");
      } else {
         administrarContratos.reproducirContrato(contratoOriginal.getCodigo(), contratoClon.getCodigo());
         contratoClon = new Contratos();
         contratoOriginal = new Contratos();
         listaContratos = null;
         getListaContratos();
         contarRegistros();
         anularBotonLOV();
         FacesMessage msg = new FacesMessage("Información", "Reproducción completada");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosContratos");
         RequestContext.getCurrentInstance().update("form:descripcionContrato1");
         RequestContext.getCurrentInstance().update("form:descripcionContrato2");
      }
   }

   //METODO PARA MOSTRAR LOS CONTRATOS EXISTENTES
   public void mostrarTodosContratos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (guardado) {
         if (bandera == 1) {
            restaurarTabla();
         }
         listaContratos = null;
         mostrarTodos = true;
         verMostrarTodos = false;
         getListaContratos();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosContratos");
         RequestContext.getCurrentInstance().update("form:mostrarTodos");
      } else {
         verMostrarTodos = true;
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
      contratoSeleccionado = null;
      cualCelda = -1;
   }

   public void seleccionContrato() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (cambioContrato == 0) {
         if (bandera == 1) {
            restaurarTabla();
         }
         listaContratos.clear();
         listaContratos.add(contratoLovSeleccionado);
         contarRegistros();
         mostrarTodos = false;
         RequestContext.getCurrentInstance().update("form:datosContratos");
         RequestContext.getCurrentInstance().update("form:mostrarTodos");
      } else if (cambioContrato == 1) {
         contratoOriginal = contratoLovSeleccionado;
         RequestContext.getCurrentInstance().update("form:descripcionContrato2");
      } else if (cambioContrato == 2) {
         contratoClon = contratoLovSeleccionado;
         RequestContext.getCurrentInstance().update("form:descripcionContrato1");
      }
      filtradoListaContratosLOV = null;
      contratoLovSeleccionado = null;
      aceptar = true;

      context.reset("form:lovContratos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovContratos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ContratosDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ContratosDialogo");
      RequestContext.getCurrentInstance().update("form:lovContratos");
      RequestContext.getCurrentInstance().update("form:aceptarC");
   }

   public void cancelarSeleccionContrato() {
      System.out.println("Entro en cancelarSeleccionContrato()");
      filtradoListaContratosLOV = null;
      contratoLovSeleccionado = null;
      aceptar = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovContratos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovContratos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ContratosDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ContratosDialogo");
      RequestContext.getCurrentInstance().update("form:lovContratos");
      RequestContext.getCurrentInstance().update("form:aceptarC");
   }

   //GUARDAR
   public void guardarSalir() {
      System.out.println("guardado : " + guardado);
      guardarCambios();
      //refrescar();
   }

   public void salir() {  limpiarListasValor();
      restaurarTabla();
      listaContratosBorrar.clear();
      listaContratosCrear.clear();
      listaContratosModificar.clear();
      contratoSeleccionado = null;
      k = 0;
      listaContratos = null;
      guardado = true;
   }

   public void restaurarTabla() {
      altoTabla = "230";
      columnaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosContratos:columnaCodigo");
      columnaCodigo.setFilterStyle("display: none; visibility: hidden;");
      columnaDescripción = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosContratos:columnaDescripción");
      columnaDescripción.setFilterStyle("display: none; visibility: hidden;");
      columnaTipoCotizante = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosContratos:columnaTipoCotizante");
      columnaTipoCotizante.setFilterStyle("display: none; visibility: hidden;");
      columnaEstado = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosContratos:columnaEstado");
      columnaEstado.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosContratos");
      bandera = 0;
      filtradolistaContratos = null;
      tipoLista = 0;
   }

   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO
   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (contratoSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(contratoSeleccionado.getSecuencia(), "CONTRATOS");
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
      } else if (administrarRastros.verificarHistoricosTabla("CONTRATOS")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosContratosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ContratosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosContratosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ContratosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //EVENTO FILTRAR
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contratoSeleccionado = null;
      contarRegistros();
      anularBotonLOV();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLovC() {
      RequestContext.getCurrentInstance().update("form:infoRegistroContrato");
   }

   public void contarRegistrosLovTiposCot() {
      RequestContext.getCurrentInstance().update("form:infoRegistroTiposC");
   }

   public void anularBotonLOV() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void activarBotonLOV() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void recordarSeleccion() {
      if (contratoSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaT = (DataTable) c.getViewRoot().findComponent("form:datosContratos");
         tablaT.setSelection(contratoSeleccionado);
      } else {
         RequestContext.getCurrentInstance().execute("PF('datosContratos').unselectAllRows()");
      }
   }

   //GETTER AND SETTER
   public List<Contratos> getListaContratos() {
      if (listaContratos == null) {
         listaContratos = administrarContratos.consultarContratos();
         if (listaContratos != null) {
            for (int i = 0; i < listaContratos.size(); i++) {
               if (listaContratos.get(i).getTipocotizante() == null) {
                  listaContratos.get(i).setTipocotizante(new TiposCotizantes());
               }
            }
         }
      }
      return listaContratos;
   }

   public void setListaContratos(List<Contratos> listaContratos) {
      this.listaContratos = listaContratos;
   }

   public List<Contratos> getFiltradolistaContratos() {
      return filtradolistaContratos;
   }

   public void setFiltradolistaContratos(List<Contratos> filtradolistaContratos) {
      this.filtradolistaContratos = filtradolistaContratos;
   }

   public List<Contratos> getLovContratos() {
      if (lovContratos == null) {
         lovContratos = administrarContratos.consultarContratos();
      }
      return lovContratos;
   }

   public void setLovContratos(List<Contratos> listaContratoLOV) {
      this.lovContratos = listaContratoLOV;
   }

   public List<Contratos> getFiltradoListaContratosLOV() {
      return filtradoListaContratosLOV;
   }

   public void setFiltradoListaContratosLOV(List<Contratos> filtradoListaContratosLOV) {
      this.filtradoListaContratosLOV = filtradoListaContratosLOV;
   }

   public Contratos getContratoLovSeleccionado() {
      return contratoLovSeleccionado;
   }

   public void setContratoLovSeleccionado(Contratos contratoSeleccionado) {
      this.contratoLovSeleccionado = contratoSeleccionado;
   }

   public boolean isMostrarTodos() {
      return mostrarTodos;
   }

   public void setMostrarTodos(boolean mostrarTodos) {
      this.mostrarTodos = mostrarTodos;
   }

   public List<TiposCotizantes> getLovTiposCotizantes() {
      if (lovTiposCotizantes == null) {
         lovTiposCotizantes = administrarContratos.consultaLOVTiposCotizantes();
      }
      return lovTiposCotizantes;
   }

   public void setLovTiposCotizantes(List<TiposCotizantes> listaTiposCotizantes) {
      this.lovTiposCotizantes = listaTiposCotizantes;
   }

   public TiposCotizantes getTipoCotizanteLovSeleccionado() {
      return tipoCotizanteLovSeleccionado;
   }

   public void setTipoCotizanteLovSeleccionado(TiposCotizantes tipoCotizanteSeleccionado) {
      this.tipoCotizanteLovSeleccionado = tipoCotizanteSeleccionado;
   }

   public List<TiposCotizantes> getFiltradoListaTiposCotizantes() {
      return filtradoListaTiposCotizantes;
   }

   public void setFiltradoListaTiposCotizantes(List<TiposCotizantes> filtradoListaTiposCotizantes) {
      this.filtradoListaTiposCotizantes = filtradoListaTiposCotizantes;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public Contratos getNuevoContrato() {
      return nuevoContrato;
   }

   public void setNuevoContrato(Contratos nuevoContrato) {
      this.nuevoContrato = nuevoContrato;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public Contratos getEditarContrato() {
      return editarContrato;
   }

   public void setEditarContrato(Contratos editarContrato) {
      this.editarContrato = editarContrato;
   }

   public Contratos getDuplicarContrato() {
      return duplicarContrato;
   }

   public void setDuplicarContrato(Contratos duplicarContrato) {
      this.duplicarContrato = duplicarContrato;
   }

   public Contratos getContratoOriginal() {
      return contratoOriginal;
   }

   public void setContratoOriginal(Contratos contratoOriginal) {
      this.contratoOriginal = contratoOriginal;
   }

   public Contratos getContratoClon() {
      return contratoClon;
   }

   public void setContratoClon(Contratos contratoClon) {
      this.contratoClon = contratoClon;
   }

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public Contratos getContratoSeleccionado() {
      return contratoSeleccionado;
   }

   public void setContratoSeleccionado(Contratos contratoTablaSeleccionado) {
      this.contratoSeleccionado = contratoTablaSeleccionado;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosContratos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroTipo() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovTiposCotizantes");
      infoRegistroTipo = String.valueOf(tabla.getRowCount());
      return infoRegistroTipo;
   }

   public void setInfoRegistroTipo(String infoRegistroTipo) {
      this.infoRegistroTipo = infoRegistroTipo;
   }

   public String getInfoRegistroContrato() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovContratos");
      infoRegistroContrato = String.valueOf(tabla.getRowCount());
      return infoRegistroContrato;
   }

   public void setInfoRegistroContrato(String infoRegistroContrato) {
      this.infoRegistroContrato = infoRegistroContrato;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }
}
