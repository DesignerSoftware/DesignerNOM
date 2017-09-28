/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvReferencias;
import Entidades.Personas;
import Entidades.TiposFamiliares;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarHvReferencias1Interface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
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
public class ControlHvReferencias1 implements Serializable {

   private static Logger log = Logger.getLogger(ControlHvReferencias1.class);

   @EJB
   AdministrarHvReferencias1Interface administrarHvReferencias1;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   private List<HvReferencias> listHvReferencias1;
   private List<HvReferencias> filtrarHvReferencias1;
   private List<HvReferencias> crearHvReferencias1;
   private List<HvReferencias> modificarHvReferencias1;
   private List<HvReferencias> borrarHvReferencias1;
   private HvReferencias nuevoHvReferencia1;
   private HvReferencias duplicarHvReferencia1;
   private HvReferencias editarHvReferencia1;
   private HvReferencias hvReferencia1Seleccionada;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private Column cargo, nombre, numTelefono, numCelular, parentesco;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   private BigInteger secuenciaPersona;
//Empleado
   private Personas personaSeleccionada;
//otros
   private HVHojasDeVida hvHojasDeVida;
   private List<HVHojasDeVida> listHvHojasDeVida;
   //autocompletar
   private String tiposFamiliares;
   private List<TiposFamiliares> lovTiposFamiliares;
   private List<TiposFamiliares> filtrarLovTiposFamiliares;
   private TiposFamiliares tipoFamiliarLovSeleccionado;
   private String nuevoParentesco;
   private String infoRegistro;
   private String infoRegistroParentesco;
   private int tamano;
//    private String backUpNombre;
//    private Long backUpTelefono;
   private Empleados empleado;
   private DataTable tablaC;
   private boolean activarLov;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlHvReferencias1() {
      listHvReferencias1 = null;
      crearHvReferencias1 = new ArrayList<HvReferencias>();
      modificarHvReferencias1 = new ArrayList<HvReferencias>();
      borrarHvReferencias1 = new ArrayList<HvReferencias>();
      permitirIndex = true;
      guardado = true;
      editarHvReferencia1 = new HvReferencias();
      nuevoHvReferencia1 = new HvReferencias();
      nuevoHvReferencia1.setTipo("FAMILIARES");
      duplicarHvReferencia1 = new HvReferencias();
      personaSeleccionada = null;
      secuenciaPersona = null;
      listHvHojasDeVida = new ArrayList<HVHojasDeVida>();
      lovTiposFamiliares = null;
      filtrarLovTiposFamiliares = null;
      tipoLista = 0;
      tamano = 270;
      aceptar = true;
      empleado = new Empleados();
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
      String pagActual = "perreferencialaboral1";

      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina(pagActual, this.getClass().getSimpleName());
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
      lovTiposFamiliares = null;
   }

   @PreDestroy
   public void destruyendoce() {
      log.info(this.getClass().getName() + ".destruyendoce() @Destroy");
   }
   
   @PostConstruct
   public void inicializarAdministrador() {
      log.info(this.getClass().getName() + ".inicializarAdministrador() @PostConstruct");
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarHvReferencias1.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirEmpleado(BigInteger sec) {
      secuenciaPersona = sec;
      listHvReferencias1 = null;
      empleado = administrarHvReferencias1.empleadoActual(sec);
      getListHvReferencias1();
      deshabilitarBotonLov();
      if (!listHvReferencias1.isEmpty()) {
         hvReferencia1Seleccionada = listHvReferencias1.get(0);
      }
   }

   public void mostrarNuevo() {
      log.error("nuevo Tipo Entrevista " + nuevoHvReferencia1.getTipo());
   }

   public void cambiarIndice(HvReferencias hvReferencia, int celda) {
      if (permitirIndex == true) {
         hvReferencia1Seleccionada = hvReferencia;
         cualCelda = celda;
         hvReferencia1Seleccionada.getSecuencia();

         if (cualCelda == 0) {
//                backUpNombre = hvReferencia1Seleccionada.getNombrepersona();
         } else if (cualCelda == 1) {
            hvReferencia1Seleccionada.getCargo();
         } else if (cualCelda == 2) {
//                backUpTelefono = hvReferencia1Seleccionada.getTelefono();
         } else if (cualCelda == 3) {
            hvReferencia1Seleccionada.getCelular();
         } else if (cualCelda == 4) {
            tiposFamiliares = hvReferencia1Seleccionada.getParentesco().getTipo();
         }
      }
   }

   public void asignarIndex(HvReferencias hvReferencia, int LND, int dig) {
      try {
         log.info("\n ENTRE A ControlHvReferencias.asignarIndex \n");
         RequestContext context = RequestContext.getCurrentInstance();

         hvReferencia1Seleccionada = hvReferencia;
         tipoActualizacion = LND;
         if (dig == 4) {
            RequestContext.getCurrentInstance().update("form:tipoFamiliarDialogo");
            RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').show()");
            dig = -1;
         }

      } catch (Exception e) {
         log.warn("Error ControlHvReferencias.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (hvReferencia1Seleccionada != null) {
         RequestContext context = RequestContext.getCurrentInstance();

         if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("form:tipoFamiliarDialogo");
            RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').show()");
            tipoActualizacion = 0;
         }

      }
   }

   public void cancelarModificacion() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
         numTelefono.setFilterStyle("display: none; visibility: hidden;");
         numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
         numCelular.setFilterStyle("display: none; visibility: hidden;");
         parentesco = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:parentesco");
         parentesco.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosHvReferencia");
         bandera = 0;
         filtrarHvReferencias1 = null;
         tipoLista = 0;
      }

      borrarHvReferencias1.clear();
      crearHvReferencias1.clear();
      modificarHvReferencias1.clear();
      hvReferencia1Seleccionada = null;
      hvReferencia1Seleccionada = null;
      k = 0;
      listHvReferencias1 = null;
      guardado = true;
      permitirIndex = true;
      getListHvReferencias1();
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosHvReferencia");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 1) {
         //CERRAR FILTRADO
         nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
         numTelefono.setFilterStyle("display: none; visibility: hidden;");
         numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
         numCelular.setFilterStyle("display: none; visibility: hidden;");
         parentesco = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:parentesco");
         parentesco.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosHvReferencia");
         bandera = 0;
         filtrarHvReferencias1 = null;
         tipoLista = 0;
      }

      borrarHvReferencias1.clear();
      crearHvReferencias1.clear();
      modificarHvReferencias1.clear();
      hvReferencia1Seleccionada = null;
      hvReferencia1Seleccionada = null;
      k = 0;
      listHvReferencias1 = null;
      guardado = true;
      permitirIndex = true;
      getListHvReferencias1();
      RequestContext context = RequestContext.getCurrentInstance();
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosHvReferencia");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
         nombre.setFilterStyle("width: 85% !important");
         cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
         cargo.setFilterStyle("width: 85% !important");
         numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
         numTelefono.setFilterStyle("width: 85% !important");
         numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
         numCelular.setFilterStyle("width: 85% !important");
         parentesco = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:parentesco");
         parentesco.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosHvReferencia");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         tamano = 270;
         log.info("Desactivar");
         nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
         cargo.setFilterStyle("display: none; visibility: hidden;");
         numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
         numTelefono.setFilterStyle("display: none; visibility: hidden;");
         numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
         numCelular.setFilterStyle("display: none; visibility: hidden;");
         parentesco = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:parentesco");
         parentesco.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosHvReferencia");
         bandera = 0;
         filtrarHvReferencias1 = null;
         tipoLista = 0;
      }
   }

   /**
    *
    * @param indice donde se encuentra posicionado
    * @param confirmarCambio nombre de la columna
    * @param valorConfirmar valor ingresado
    */
   public void modificarHvrReferencia1(HvReferencias hvReferencia) {
      hvReferencia1Seleccionada = hvReferencia;
      if (!crearHvReferencias1.contains(hvReferencia1Seleccionada)) {
         if (modificarHvReferencias1.isEmpty()) {
            modificarHvReferencias1.add(hvReferencia1Seleccionada);
         } else if (!modificarHvReferencias1.contains(hvReferencia1Seleccionada)) {
            modificarHvReferencias1.add(hvReferencia1Seleccionada);
         }
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosHvReferencia");
      }
   }

   public void actualizarTipoFamiliar() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            hvReferencia1Seleccionada.setParentesco(tipoFamiliarLovSeleccionado);

            if (!crearHvReferencias1.contains(hvReferencia1Seleccionada)) {
               if (modificarHvReferencias1.isEmpty()) {
                  modificarHvReferencias1.add(hvReferencia1Seleccionada);
               } else if (!modificarHvReferencias1.contains(hvReferencia1Seleccionada)) {
                  modificarHvReferencias1.add(hvReferencia1Seleccionada);
               }
            }
         } else {
            hvReferencia1Seleccionada.setParentesco(tipoFamiliarLovSeleccionado);

            if (!crearHvReferencias1.contains(hvReferencia1Seleccionada)) {
               if (modificarHvReferencias1.isEmpty()) {
                  modificarHvReferencias1.add(hvReferencia1Seleccionada);
               } else if (!modificarHvReferencias1.contains(hvReferencia1Seleccionada)) {
                  modificarHvReferencias1.add(hvReferencia1Seleccionada);
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         // RequestContext.getCurrentInstance().update("form:datosHvReferencia");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         nuevoHvReferencia1.setParentesco(tipoFamiliarLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevooHvReferenciaLab");
      } else if (tipoActualizacion == 2) {
         duplicarHvReferencia1.setParentesco(tipoFamiliarLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRRL");
      }
      filtrarLovTiposFamiliares = null;
      tipoFamiliarLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      context.reset("form:lovTiposFamiliares:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposFamiliares').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovTiposFamiliares");
      RequestContext.getCurrentInstance().update("form:datosHvReferencia");
   }

   public void cancelarCambioTiposFamiliares() {
      filtrarLovTiposFamiliares = null;
      tipoFamiliarLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovTiposFamiliares:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovTiposFamiliares').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').hide()");
   }

   public void borrandoHvReferencias() {

      if (hvReferencia1Seleccionada != null) {
         log.info("Entro a borrandoHvReferencias");
         if (!modificarHvReferencias1.isEmpty() && modificarHvReferencias1.contains(hvReferencia1Seleccionada)) {
            modificarHvReferencias1.remove(modificarHvReferencias1.indexOf(hvReferencia1Seleccionada));
            borrarHvReferencias1.add(hvReferencia1Seleccionada);
         } else if (!crearHvReferencias1.isEmpty() && crearHvReferencias1.contains(hvReferencia1Seleccionada)) {
            crearHvReferencias1.remove(crearHvReferencias1.indexOf(hvReferencia1Seleccionada));
         } else {
            borrarHvReferencias1.add(hvReferencia1Seleccionada);
         }
         listHvReferencias1.remove(hvReferencia1Seleccionada);

         if (tipoLista == 1) {
            filtrarHvReferencias1.remove(hvReferencia1Seleccionada);

         }
         RequestContext context = RequestContext.getCurrentInstance();
         modificarInfoRegistro(listHvReferencias1.size());
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:datosHvReferencia");
         hvReferencia1Seleccionada = null;
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }

   }

   public void revisarDialogoGuardar() {

      if (!borrarHvReferencias1.isEmpty() || !crearHvReferencias1.isEmpty() || !modificarHvReferencias1.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarYSalir() {
      guardarHvReferencia();
      salir();
   }

   public void guardarHvReferencia() {
      if (guardado == false) {
         log.info("Realizando guardarHvReferencia");
         if (!borrarHvReferencias1.isEmpty()) {
            for (int i = 0; i < borrarHvReferencias1.size(); i++) {
               log.info("Borrando...");
               if (borrarHvReferencias1.get(i).getParentesco().getSecuencia() == null) {
                  borrarHvReferencias1.get(i).setParentesco(null);
               }
            }
            administrarHvReferencias1.borrarHvReferencias(borrarHvReferencias1);
            //mostrarBorrados
            registrosBorrados = borrarHvReferencias1.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarHvReferencias1.clear();
         }
         if (!crearHvReferencias1.isEmpty()) {
            for (int i = 0; i < crearHvReferencias1.size(); i++) {

               if (crearHvReferencias1.get(i).getParentesco().getSecuencia() == null) {
                  crearHvReferencias1.get(i).setParentesco(null);
               }
            }
            log.info("Creando...");
            administrarHvReferencias1.crearHvReferencias(crearHvReferencias1);
            crearHvReferencias1.clear();
         }
         if (!modificarHvReferencias1.isEmpty()) {
            for (int i = 0; i < modificarHvReferencias1.size(); i++) {
               if (modificarHvReferencias1.get(i).getParentesco().getSecuencia() == null) {
                  modificarHvReferencias1.get(i).setParentesco(null);
               }
            }
            administrarHvReferencias1.modificarHvReferencias(modificarHvReferencias1);
            modificarHvReferencias1.clear();
         }
         log.info("Se guardaron los datos con exito");
         listHvReferencias1 = null;
         getListHvReferencias1();
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosHvReferencia");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");

         k = 0;
      }
      hvReferencia1Seleccionada = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (hvReferencia1Seleccionada != null) {
         if (tipoLista == 0) {
            editarHvReferencia1 = hvReferencia1Seleccionada;
         }
         if (tipoLista == 1) {
            editarHvReferencia1 = hvReferencia1Seleccionada;
         }

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
            RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCargo");
            RequestContext.getCurrentInstance().execute("PF('editCargo').show()");
            cualCelda = -1;

         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editTelefono");
            RequestContext.getCurrentInstance().execute("PF('editTelefono').show()");
            cualCelda = -1;

         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCelular");
            RequestContext.getCurrentInstance().execute("PF('editCelular').show()");
            cualCelda = -1;

         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editParentesco");
            RequestContext.getCurrentInstance().execute("PF('editParentesco').show()");
            cualCelda = -1;
         }

      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String Campo) {

      if (Campo.equalsIgnoreCase("TIPOSFAMILIARES")) {
         if (tipoNuevo == 1) {
            nuevoParentesco = nuevoHvReferencia1.getParentesco().getTipo();
         } else if (tipoNuevo == 2) {
            nuevoParentesco = duplicarHvReferencia1.getParentesco().getTipo();
         }
      }
      log.error("NUEVO PARENTESCO " + nuevoParentesco);
   }

   public void autocompletarNuevoyDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("TIPOSFAMILIARES")) {
         if (!nuevoHvReferencia1.getParentesco().getTipo().equals("")) {
            if (tipoNuevo == 1) {
               nuevoHvReferencia1.getParentesco().setTipo(nuevoParentesco);
            }
            for (int i = 0; i < lovTiposFamiliares.size(); i++) {
               if (lovTiposFamiliares.get(i).getTipo().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
         } else {

            if (tipoNuevo == 1) {
               nuevoHvReferencia1.setParentesco(new TiposFamiliares());
               coincidencias = 1;
            }
            coincidencias = 1;
         }
         if (coincidencias == 1) {
            if (tipoNuevo == 1) {
               nuevoHvReferencia1.setParentesco(lovTiposFamiliares.get(indiceUnicoElemento));
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
            }
            lovTiposFamiliares.clear();
            lovTiposFamiliares = null;
            getLovTiposFamiliares();
         } else {
            RequestContext.getCurrentInstance().update("form:tipoFamiliarDialogo");
            RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').show()");
            tipoActualizacion = tipoNuevo;
            if (tipoNuevo == 1) {
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoNombreSucursal");
            } else if (tipoNuevo == 2) {
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");
            }
         }
      }
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("TIPOSFAMILIARES")) {
         if (!duplicarHvReferencia1.getParentesco().getTipo().equals("")) {
            if (tipoNuevo == 2) {
               duplicarHvReferencia1.getParentesco().setTipo(nuevoParentesco);
            }
            for (int i = 0; i < lovTiposFamiliares.size(); i++) {
               if (lovTiposFamiliares.get(i).getTipo().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               if (tipoNuevo == 2) {
                  duplicarHvReferencia1.setParentesco(lovTiposFamiliares.get(indiceUnicoElemento));
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");

               }
               lovTiposFamiliares.clear();
               lovTiposFamiliares = null;
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");
               getLovTiposFamiliares();
            } else {
               RequestContext.getCurrentInstance().update("form:tipoFamiliarDialogo");
               RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').show()");
               tipoActualizacion = tipoNuevo;
               if (tipoNuevo == 2) {
                  RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");
               }
            }
         } else if (tipoNuevo == 2) {
            duplicarHvReferencia1.setParentesco(new TiposFamiliares());
            log.info("NUEVO PARENTESCO " + nuevoParentesco);
            if (tipoLista == 0) {
               if (hvReferencia1Seleccionada != null) {
                  hvReferencia1Seleccionada.getParentesco().setTipo(nuevoParentesco);
                  log.error("tipo lista" + tipoLista);
                  log.error("Secuencia Parentesco " + hvReferencia1Seleccionada.getParentesco().getSecuencia());
               }
            } else if (tipoLista == 1) {
               hvReferencia1Seleccionada.getParentesco().setTipo(nuevoParentesco);
            }

         }

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarNombreSucursal");

      }
   }

   public void asignarVariableTiposFamiliaresNuevo(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:tipoFamiliarDialogo");
      RequestContext.getCurrentInstance().execute("PF('tipoFamiliarDialogo').show()");
   }

   public void agregarNuevoHvRefencias() {
      log.info("agregarNuevoHvRefencias");
      int contador = 0;
      nuevoHvReferencia1.setHojadevida(new HVHojasDeVida());
      Short a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();

      if (nuevoHvReferencia1.getNombrepersona() == (null)) {
         mensajeValidacion = mensajeValidacion + " *Nombre \n";
         log.info("Mensaje validacion : " + mensajeValidacion);
      } else {
         log.info("bandera");
         contador++;
      }

      listHvHojasDeVida = administrarHvReferencias1.consultarHvHojasDeVida(empleado.getPersona());
      if (listHvHojasDeVida == null) {
         log.error("ERROR NULO HVHOJASDEVIDA PARA LA SECUENCIA DE PERSONA :" + empleado.getPersona());
      } else {
         log.error("tamaño listHojasDeVida " + listHvHojasDeVida.size());
         hvHojasDeVida = listHvHojasDeVida.get(0);
         log.error("Agregar nuevo HvHojasDeVida " + hvHojasDeVida.getSecuencia());
         nuevoHvReferencia1.setHojadevida(hvHojasDeVida);
      }
      nuevoHvReferencia1.setTipo("FAMILIARES");
      log.error("agregar tipo entrevista " + nuevoHvReferencia1.getTipo());
      log.info("contador " + contador);

      if (contador == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         if (bandera == 1) {
            //CERRAR FILTRADO
            log.info("Desactivar");
            nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
            numTelefono.setFilterStyle("display: none; visibility: hidden;");
            numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
            numCelular.setFilterStyle("display: none; visibility: hidden;");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:parentesco");
            parentesco.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosHvReferencia");
            bandera = 0;
            filtrarHvReferencias1 = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoHvReferencia1.setSecuencia(l);
         log.error("---------------AGREGAR REGISTRO----------------");
         log.error("nombre " + nuevoHvReferencia1.getNombrepersona());
         log.error("cargo " + nuevoHvReferencia1.getCargo());
         log.error("numero de telefono " + nuevoHvReferencia1.getTelefono());
         log.error("numero de celular" + nuevoHvReferencia1.getCelular());
         log.error("-----------------------------------------------");

         crearHvReferencias1.add(nuevoHvReferencia1);
         listHvReferencias1.add(nuevoHvReferencia1);
         hvReferencia1Seleccionada = nuevoHvReferencia1;
         nuevoHvReferencia1 = new HvReferencias();
         nuevoHvReferencia1.setParentesco(new TiposFamiliares());
         nuevoHvReferencia1.setHojadevida(new HVHojasDeVida());
         RequestContext.getCurrentInstance().update("form:datosHvReferencia");
         modificarInfoRegistro(listHvReferencias1.size());
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroHvReferencias').hide()");

      } else {
         RequestContext.getCurrentInstance().update("form:validacionNuevaRefPersonal");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaRefPersonal').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoHvReferencias() {
      log.info("limpiarNuevoHvEntrevistas");
      nuevoHvReferencia1 = new HvReferencias();
   }

   //------------------------------------------------------------------------------
   public void duplicandoHvEntrevistas() {
      log.info("duplicandoHvEntrevistas");
      if (hvReferencia1Seleccionada != null) {
         duplicarHvReferencia1 = new HvReferencias();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarHvReferencia1.setSecuencia(l);
            duplicarHvReferencia1.setNombrepersona(hvReferencia1Seleccionada.getNombrepersona());
            duplicarHvReferencia1.setCargo(hvReferencia1Seleccionada.getCargo());
            duplicarHvReferencia1.setTelefono(hvReferencia1Seleccionada.getTelefono());
            duplicarHvReferencia1.setCelular(hvReferencia1Seleccionada.getCelular());
            duplicarHvReferencia1.setHojadevida(hvReferencia1Seleccionada.getHojadevida());
            duplicarHvReferencia1.setTipo(hvReferencia1Seleccionada.getTipo());
            TiposFamiliares t = new TiposFamiliares();
            t = hvReferencia1Seleccionada.getParentesco();
            duplicarHvReferencia1.setParentesco(t);
         }
         if (tipoLista == 1) {
            duplicarHvReferencia1.setSecuencia(l);
            duplicarHvReferencia1.setNombrepersona(hvReferencia1Seleccionada.getNombrepersona());
            duplicarHvReferencia1.setCargo(hvReferencia1Seleccionada.getCargo());
            duplicarHvReferencia1.setTelefono(hvReferencia1Seleccionada.getTelefono());
            duplicarHvReferencia1.setCelular(hvReferencia1Seleccionada.getCelular());
            duplicarHvReferencia1.setHojadevida(hvReferencia1Seleccionada.getHojadevida());
            duplicarHvReferencia1.setTipo(hvReferencia1Seleccionada.getTipo());
            duplicarHvReferencia1.setParentesco(hvReferencia1Seleccionada.getParentesco());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRRL");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroHvReferencias').show()");
         hvReferencia1Seleccionada = null;
      } else {
         RequestContext.getCurrentInstance().execute("PF('formularioDialogos:seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      log.error("ESTOY EN CONFIRMAR DUPLICAR HVENTREVISTAS");
      int contador = 0;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      if (duplicarHvReferencia1.getNombrepersona() == null) {
         mensajeValidacion = mensajeValidacion + "   *Nombre \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }
      if (duplicarHvReferencia1.getTelefono() == null) {
         mensajeValidacion = mensajeValidacion + "*Telefono \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         log.info("Bandera : ");
         contador++;
      }
      if (contador == 2) {

         if (crearHvReferencias1.contains(duplicarHvReferencia1)) {
            log.info("Ya lo contengo.");
         } else {
            crearHvReferencias1.add(duplicarHvReferencia1);
         }
         listHvReferencias1.add(duplicarHvReferencia1);
         hvReferencia1Seleccionada = duplicarHvReferencia1;
         RequestContext.getCurrentInstance().update("form:datosHvReferencia");

         log.error("---------------DUPLICAR REGISTRO----------------");
         log.error("nombre  " + duplicarHvReferencia1.getNombrepersona());
         log.error("cargo  " + duplicarHvReferencia1.getCargo());
         log.error("numero de telefono  " + duplicarHvReferencia1.getTelefono());
         log.error("numero de celular  " + duplicarHvReferencia1.getCelular());
         log.error("numero de Parentesco  " + duplicarHvReferencia1.getParentesco().getTipo());
         log.error("-----------------------------------------------");
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         modificarInfoRegistro(listHvReferencias1.size());
         RequestContext.getCurrentInstance().update("form:informacionRegistro");

         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            //CERRAR FILTRADO
            nombre = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            cargo = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:cargo");
            cargo.setFilterStyle("display: none; visibility: hidden;");
            numTelefono = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numTelefono");
            numTelefono.setFilterStyle("display: none; visibility: hidden;");
            numCelular = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:numCelular");
            numCelular.setFilterStyle("display: none; visibility: hidden;");
            parentesco = (Column) c.getViewRoot().findComponent("form:datosHvReferencia:parentesco");
            parentesco.setFilterStyle("display: none; visibility: hidden;");

            RequestContext.getCurrentInstance().update("form:datosHvReferencia");
            bandera = 0;
            filtrarHvReferencias1 = null;
            tipoLista = 0;
         }
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroHvReferencias').hide()");
         duplicarHvReferencia1 = new HvReferencias();

      } else {
         contador = 0;
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarHvReferencias() {
      duplicarHvReferencia1 = new HvReferencias();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosHvReferenciaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "REFERENCIASFAMILIARES", false, false, "UTF-8", null, null);
      context.responseComplete();
      hvReferencia1Seleccionada = null;
      hvReferencia1Seleccionada = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosHvReferenciaExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "REFERENCIASFAMILIARES", false, false, "UTF-8", null, null);
      context.responseComplete();
      hvReferencia1Seleccionada = null;
      hvReferencia1Seleccionada = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (hvReferencia1Seleccionada != null) {
         log.info("lol 2");
         int resultado = administrarRastros.obtenerTabla(hvReferencia1Seleccionada.getSecuencia(), "HVREFERENCIAS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
         log.info("resultado: " + resultado);
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
      } else if (administrarRastros.verificarHistoricosTabla("HVREFERENCIAS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      hvReferencia1Seleccionada = null;
   }

   public void recordarSeleccion() {
      if (hvReferencia1Seleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosHvReferencia");
         tablaC.setSelection(hvReferencia1Seleccionada);
      }
   }

   public void eventoFiltrar() {
      try {
         log.info("\n ENTRE A ControlHvReferencias.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         modificarInfoRegistro(filtrarHvReferencias1.size());
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlHvReferencias eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void eventoFiltrarParentesco() {
      modificarInfoRegistroParentesco(filtrarLovTiposFamiliares.size());
      RequestContext.getCurrentInstance().update("form:infoRegistroParentesco");
   }

   public void modificarInfoRegistro(int valor) {
      infoRegistro = String.valueOf(valor);
   }

   public void modificarInfoRegistroParentesco(int valor) {
      infoRegistroParentesco = String.valueOf(valor);
   }

   public void contarRegistros() {
      if (listHvReferencias1 != null) {
         modificarInfoRegistro(listHvReferencias1.size());
      } else {
         modificarInfoRegistro(0);
      }
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<HvReferencias> getListHvReferencias1() {
      if (listHvReferencias1 == null) {
         listHvReferencias1 = administrarHvReferencias1.consultarHvReferenciasFamiliaresPorPersona(empleado.getPersona());
      }
      return listHvReferencias1;
   }

   public void setListHvReferencias1(List<HvReferencias> listHvReferencias1) {
      this.listHvReferencias1 = listHvReferencias1;
   }

   public List<HvReferencias> getFiltrarHvReferencias1() {
      return filtrarHvReferencias1;
   }

   public void setFiltrarHvReferencias1(List<HvReferencias> filtrarHvReferencias1) {
      this.filtrarHvReferencias1 = filtrarHvReferencias1;
   }

   public List<HvReferencias> getCrearHvReferencias1() {
      return crearHvReferencias1;
   }

   public void setCrearHvReferencias1(List<HvReferencias> crearHvReferencias1) {
      this.crearHvReferencias1 = crearHvReferencias1;
   }

   public HvReferencias getNuevoHvReferencia1() {
      return nuevoHvReferencia1;
   }

   public void setNuevoHvReferencia1(HvReferencias nuevoHvReferencia1) {
      this.nuevoHvReferencia1 = nuevoHvReferencia1;
   }

   public HvReferencias getDuplicarHvReferencia1() {
      return duplicarHvReferencia1;
   }

   public void setDuplicarHvReferencia1(HvReferencias duplicarHvReferencia1) {
      this.duplicarHvReferencia1 = duplicarHvReferencia1;
   }

   public int getRegistrosBorrados() {
      return registrosBorrados;
   }

   public void setRegistrosBorrados(int registrosBorrados) {
      this.registrosBorrados = registrosBorrados;
   }

   public BigInteger getSecuenciaPersona() {
      return secuenciaPersona;
   }

   public void setSecuenciaPersona(BigInteger secuenciaPersona) {
      this.secuenciaPersona = secuenciaPersona;
   }

   public List<TiposFamiliares> getLovTiposFamiliares() {
      if (lovTiposFamiliares == null) {
         lovTiposFamiliares = administrarHvReferencias1.consultarLOVTiposFamiliares();
      }
      return lovTiposFamiliares;
   }

   public void setLovTiposFamiliares(List<TiposFamiliares> lovTiposFamiliares) {
      this.lovTiposFamiliares = lovTiposFamiliares;
   }

   public List<TiposFamiliares> getFiltrarLovTiposFamiliares() {
      return filtrarLovTiposFamiliares;
   }

   public void setFiltrarLovTiposFamiliares(List<TiposFamiliares> filtrarLovTiposFamiliares) {
      this.filtrarLovTiposFamiliares = filtrarLovTiposFamiliares;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public HvReferencias getEditarHvReferencia1() {
      return editarHvReferencia1;
   }

   public void setEditarHvReferencia1(HvReferencias editarHvReferencia1) {
      this.editarHvReferencia1 = editarHvReferencia1;
   }

   public TiposFamiliares getTipoFamiliarLovSeleccionado() {
      return tipoFamiliarLovSeleccionado;
   }

   public void setTipoFamiliarLovSeleccionado(TiposFamiliares tipoFamiliarLovSeleccionado) {
      this.tipoFamiliarLovSeleccionado = tipoFamiliarLovSeleccionado;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public Personas getPersonaSeleccionada() {
      if (personaSeleccionada == null) {
         personaSeleccionada = administrarHvReferencias1.consultarPersonas(secuenciaPersona);
      }
      return personaSeleccionada;
   }

   public void setPersonaSeleccionada(Personas personaSeleccionada) {
      this.personaSeleccionada = personaSeleccionada;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public HvReferencias getHvReferencia1Seleccionada() {
      return hvReferencia1Seleccionada;
   }

   public void setHvReferencia1Seleccionada(HvReferencias hvReferencia1Seleccionada) {
      this.hvReferencia1Seleccionada = hvReferencia1Seleccionada;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosHvReferencia");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroParentesco() {
      return infoRegistroParentesco;
   }

   public void setInfoRegistroParentesco(String infoRegistroParentesco) {
      this.infoRegistroParentesco = infoRegistroParentesco;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public Empleados getEmpleado() {
      return empleado;
   }

   public void setEmpleado(Empleados empleado) {
      this.empleado = empleado;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

}
