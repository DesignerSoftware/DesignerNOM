/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.FormulasContratosEntidades;
import Entidades.Formulascontratos;
import Entidades.TiposEntidades;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarFormulasContratosEntidadesInterface;
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
public class ControlFormulasContratosEntidades implements Serializable {

   private static Logger log = Logger.getLogger(ControlFormulasContratosEntidades.class);

   @EJB
   AdministrarFormulasContratosEntidadesInterface administrarFormulasContratosEntidades;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<FormulasContratosEntidades> listFormulasContratosEntidades;
   private List<FormulasContratosEntidades> filtrarFormulasContratosEntidades;
   private List<FormulasContratosEntidades> crearFormulasContratosEntidades;
   private List<FormulasContratosEntidades> modificarFormulasContratosEntidades;
   private List<FormulasContratosEntidades> borrarFormulasContratosEntidades;
   private FormulasContratosEntidades nuevoFormulasContratosEntidades;
   private FormulasContratosEntidades duplicarFormulasContratosEntidades;
   private FormulasContratosEntidades editarFormulasContratosEntidades;
   private FormulasContratosEntidades formulaContratoEntidadSeleccionada;
   //otros
   private int cualCelda, tipoLista, index, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private BigInteger secRegistro;
   private Column personafir;
   //borrado
   private int registrosBorrados;
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private BigInteger secuenciaFormulasContratos;
   //---------------------------------
   private String backupTipoEntidad;
   private List<TiposEntidades> listaTiposEntidades;
   private List<TiposEntidades> filtradoTiposEntidades;
   private TiposEntidades tipoEntidadSeleccionado;
   private String nuevoYduplicarCompletarTipoEntidad;
   private Formulascontratos formulaContratoSeleccionada;
   //private BigInteger secuenciaFormulaSeleccionada;
   private String infoRegistro;
   private String infoRegistroLOVTipoEntidad;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlFormulasContratosEntidades() {
      listFormulasContratosEntidades = null;
      crearFormulasContratosEntidades = new ArrayList<FormulasContratosEntidades>();
      modificarFormulasContratosEntidades = new ArrayList<FormulasContratosEntidades>();
      borrarFormulasContratosEntidades = new ArrayList<FormulasContratosEntidades>();
      permitirIndex = true;
      editarFormulasContratosEntidades = new FormulasContratosEntidades();
      nuevoFormulasContratosEntidades = new FormulasContratosEntidades();
      nuevoFormulasContratosEntidades.setTipoentidad(new TiposEntidades());
      duplicarFormulasContratosEntidades = new FormulasContratosEntidades();
      duplicarFormulasContratosEntidades.setTipoentidad(new TiposEntidades());
      listaTiposEntidades = null;
      filtradoTiposEntidades = null;
      guardado = true;
      aceptar = true;
      tamano = 270;
      secuenciaFormulasContratos = BigInteger.valueOf(10510720);
      //secuenciaFormulaSeleccionada = BigInteger.valueOf(4613967);
      formulaContratoSeleccionada = null;
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
      String pagActual = "formulacontratoentidad";

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
         administrarFormulasContratosEntidades.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirSecuenciaFormulaContratoYPaginaAnterior(BigInteger secuencia, String pagina) {
      secuenciaFormulasContratos = secuencia;
      listFormulasContratosEntidades = administrarFormulasContratosEntidades.consultarFormulasContratosEntidadesPorFormulaContrato(secuenciaFormulasContratos);
      paginaAnterior = pagina;
      log.info("ControlFormulasContratosEntidades PaginaAnterior");
      FacesContext fc = FacesContext.getCurrentInstance();
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "formulacontratoentidad");
   }

   public String redirigirPagina() {
      return paginaAnterior;
   }

   public void eventoFiltrar() {
      try {
         log.info("\n ENTRE A ControlFormulasContratosEntidades.eventoFiltrar \n");
         if (tipoLista == 0) {
            tipoLista = 1;
         }
         RequestContext context = RequestContext.getCurrentInstance();
         infoRegistro = "Cantidad de registros: " + filtrarFormulasContratosEntidades.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
      } catch (Exception e) {
         log.warn("Error ControlFormulasContratosEntidades eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void cambiarIndice(int indice, int celda) {
      log.error("TIPO LISTA = " + tipoLista);

      if (permitirIndex == true) {
         index = indice;
         cualCelda = celda;
         secRegistro = listFormulasContratosEntidades.get(index).getSecuencia();
         if (tipoLista == 0) {
            if (cualCelda == 0) {
               //formulacodigo
               backupTipoEntidad = listFormulasContratosEntidades.get(index).getTipoentidad().getNombre();
            }

         } else if (tipoLista == 1) {
            if (cualCelda == 0) {
               backupTipoEntidad = filtrarFormulasContratosEntidades.get(index).getTipoentidad().getNombre();
            }

         }

      }
      log.info("Indice: " + index + " Celda: " + cualCelda);
   }

   public void asignarIndex(Integer indice, int LND, int dig) {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         log.info("\n ENTRE A ControlFormulasContratosEntidades.asignarIndex \n");
         index = indice;
         if (LND == 0) {
            tipoActualizacion = 0;
         } else if (LND == 1) {
            tipoActualizacion = 1;
            log.info("Tipo Actualizacion: " + tipoActualizacion);
         } else if (LND == 2) {
            tipoActualizacion = 2;
         }
         if (dig == 0) {
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
            dig = -1;
         }
      } catch (Exception e) {
         log.warn("Error ControlFormulasContratosEntidades.asignarIndex ERROR======" + e.getMessage());
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (index >= 0) {

         if (cualCelda == 0) {
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }
      }
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         personafir = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulasContratosEntidades:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
         bandera = 0;
         filtrarFormulasContratosEntidades = null;
         tipoLista = 0;
      }

      borrarFormulasContratosEntidades.clear();
      crearFormulasContratosEntidades.clear();
      modificarFormulasContratosEntidades.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listFormulasContratosEntidades = null;
      guardado = true;
      permitirIndex = true;
      getListFormulasContratosEntidades();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listFormulasContratosEntidades == null || listFormulasContratosEntidades.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listFormulasContratosEntidades.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         personafir = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulasContratosEntidades:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
         bandera = 0;
         filtrarFormulasContratosEntidades = null;
         tipoLista = 0;
      }

      borrarFormulasContratosEntidades.clear();
      crearFormulasContratosEntidades.clear();
      modificarFormulasContratosEntidades.clear();
      index = -1;
      secRegistro = null;
      k = 0;
      listFormulasContratosEntidades = null;
      guardado = true;
      permitirIndex = true;
      getListFormulasContratosEntidades();
      RequestContext context = RequestContext.getCurrentInstance();
      if (listFormulasContratosEntidades == null || listFormulasContratosEntidades.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listFormulasContratosEntidades.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         tamano = 250;
         personafir = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulasContratosEntidades:personafir");
         personafir.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
         log.info("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         tamano = 270;
         personafir = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulasContratosEntidades:personafir");
         personafir.setFilterStyle("display: none; visibility: hidden;");

         RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
         bandera = 0;
         filtrarFormulasContratosEntidades = null;
         tipoLista = 0;
      }
   }

   public void actualizarTiposEntidades() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("formula seleccionado : " + tipoEntidadSeleccionado.getNombre());
      log.info("tipo Actualizacion : " + tipoActualizacion);
      log.info("tipo Lista : " + tipoLista);

      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            listFormulasContratosEntidades.get(index).setTipoentidad(tipoEntidadSeleccionado);

            if (!crearFormulasContratosEntidades.contains(listFormulasContratosEntidades.get(index))) {
               if (modificarFormulasContratosEntidades.isEmpty()) {
                  modificarFormulasContratosEntidades.add(listFormulasContratosEntidades.get(index));
               } else if (!modificarFormulasContratosEntidades.contains(listFormulasContratosEntidades.get(index))) {
                  modificarFormulasContratosEntidades.add(listFormulasContratosEntidades.get(index));
               }
            }
         } else {
            filtrarFormulasContratosEntidades.get(index).setTipoentidad(tipoEntidadSeleccionado);

            if (!crearFormulasContratosEntidades.contains(filtrarFormulasContratosEntidades.get(index))) {
               if (modificarFormulasContratosEntidades.isEmpty()) {
                  modificarFormulasContratosEntidades.add(filtrarFormulasContratosEntidades.get(index));
               } else if (!modificarFormulasContratosEntidades.contains(filtrarFormulasContratosEntidades.get(index))) {
                  modificarFormulasContratosEntidades.add(filtrarFormulasContratosEntidades.get(index));
               }
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         log.info("ACTUALIZAR FORMULA : " + tipoEntidadSeleccionado.getNombre());
         RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         log.info("ACTUALIZAR FORMULA NUEVO DEPARTAMENTO: " + tipoEntidadSeleccionado.getNombre());
         nuevoFormulasContratosEntidades.setTipoentidad(tipoEntidadSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");
      } else if (tipoActualizacion == 2) {
         log.info("ACTUALIZAR FORMULA DUPLICAR DEPARTAMENO: " + tipoEntidadSeleccionado.getNombre());
         duplicarFormulasContratosEntidades.setTipoentidad(tipoEntidadSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
      }
      filtradoTiposEntidades = null;
      tipoEntidadSeleccionado = null;
      aceptar = true;
      secRegistro = null;
      tipoActualizacion = -1;
      cualCelda = -1;
      cambioFormulasContratosEntidades = true;
      context.reset("form:lovFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
      //RequestContext.getCurrentInstance().update("form:lovFormulas");
   }

   public void cancelarCambioTiposEntidades() {
      filtradoTiposEntidades = null;
      tipoEntidadSeleccionado = null;
      aceptar = true;
      index = -1;
      secRegistro = null;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovFormulas:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovFormulas').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').hide()");
   }

   public void modificarFormulasContratosEntidades(int indice, String confirmarCambio, String valorConfirmar) {
      log.error("ENTRE A MODIFICAR SUB CATEGORIA");
      index = indice;
      int coincidencias = 0;
      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;
      boolean banderita2 = false;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      log.error("TIPO LISTA = " + tipoLista);
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         log.info("MODIFICANDO NORMA LABORAL : " + listFormulasContratosEntidades.get(indice).getTipoentidad().getNombre());
         if (!listFormulasContratosEntidades.get(indice).getTipoentidad().getNombre().equals("")) {
            if (tipoLista == 0) {
               listFormulasContratosEntidades.get(indice).getTipoentidad().setNombre(backupTipoEntidad);
            } else {
               listFormulasContratosEntidades.get(indice).getTipoentidad().setNombre(backupTipoEntidad);
            }

            for (int i = 0; i < listaTiposEntidades.size(); i++) {
               if (listaTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               if (tipoLista == 0) {
                  listFormulasContratosEntidades.get(indice).setTipoentidad(listaTiposEntidades.get(indiceUnicoElemento));
               } else {
                  filtrarFormulasContratosEntidades.get(indice).setTipoentidad(listaTiposEntidades.get(indiceUnicoElemento));
               }
               listaTiposEntidades.clear();
               listaTiposEntidades = null;
               //getListaTiposFamiliares();

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupTipoEntidad != null) {
               if (tipoLista == 0) {
                  listFormulasContratosEntidades.get(index).getTipoentidad().setNombre(backupTipoEntidad);
               } else {
                  filtrarFormulasContratosEntidades.get(index).getTipoentidad().setNombre(backupTipoEntidad);
               }
            }
            tipoActualizacion = 0;
            RequestContext.getCurrentInstance().update("form:personasDialogo");
            RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
         }

         if (coincidencias == 1) {
            if (tipoLista == 0) {
               if (!crearFormulasContratosEntidades.contains(listFormulasContratosEntidades.get(indice))) {

                  if (modificarFormulasContratosEntidades.isEmpty()) {
                     modificarFormulasContratosEntidades.add(listFormulasContratosEntidades.get(indice));
                  } else if (!modificarFormulasContratosEntidades.contains(listFormulasContratosEntidades.get(indice))) {
                     modificarFormulasContratosEntidades.add(listFormulasContratosEntidades.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            } else {
               if (!crearFormulasContratosEntidades.contains(filtrarFormulasContratosEntidades.get(indice))) {

                  if (modificarFormulasContratosEntidades.isEmpty()) {
                     modificarFormulasContratosEntidades.add(filtrarFormulasContratosEntidades.get(indice));
                  } else if (!modificarFormulasContratosEntidades.contains(filtrarFormulasContratosEntidades.get(indice))) {
                     modificarFormulasContratosEntidades.add(filtrarFormulasContratosEntidades.get(indice));
                  }
                  if (guardado == true) {
                     guardado = false;
                  }
               }
               index = -1;
               secRegistro = null;
            }
         }

         RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      }

   }

   public void borrandoFormulasContratosEntidades() {

      if (index >= 0) {
         if (tipoLista == 0) {
            log.info("Entro a borrandoFormulasContratosEntidades");
            if (!modificarFormulasContratosEntidades.isEmpty() && modificarFormulasContratosEntidades.contains(listFormulasContratosEntidades.get(index))) {
               int modIndex = modificarFormulasContratosEntidades.indexOf(listFormulasContratosEntidades.get(index));
               modificarFormulasContratosEntidades.remove(modIndex);
               borrarFormulasContratosEntidades.add(listFormulasContratosEntidades.get(index));
            } else if (!crearFormulasContratosEntidades.isEmpty() && crearFormulasContratosEntidades.contains(listFormulasContratosEntidades.get(index))) {
               int crearIndex = crearFormulasContratosEntidades.indexOf(listFormulasContratosEntidades.get(index));
               crearFormulasContratosEntidades.remove(crearIndex);
            } else {
               borrarFormulasContratosEntidades.add(listFormulasContratosEntidades.get(index));
            }
            listFormulasContratosEntidades.remove(index);
            infoRegistro = "Cantidad de registros: " + listFormulasContratosEntidades.size();

         }
         if (tipoLista == 1) {
            log.info("borrandoFormulasContratosEntidades ");
            if (!modificarFormulasContratosEntidades.isEmpty() && modificarFormulasContratosEntidades.contains(filtrarFormulasContratosEntidades.get(index))) {
               int modIndex = modificarFormulasContratosEntidades.indexOf(filtrarFormulasContratosEntidades.get(index));
               modificarFormulasContratosEntidades.remove(modIndex);
               borrarFormulasContratosEntidades.add(filtrarFormulasContratosEntidades.get(index));
            } else if (!crearFormulasContratosEntidades.isEmpty() && crearFormulasContratosEntidades.contains(filtrarFormulasContratosEntidades.get(index))) {
               int crearIndex = crearFormulasContratosEntidades.indexOf(filtrarFormulasContratosEntidades.get(index));
               crearFormulasContratosEntidades.remove(crearIndex);
            } else {
               borrarFormulasContratosEntidades.add(filtrarFormulasContratosEntidades.get(index));
            }
            int VCIndex = listFormulasContratosEntidades.indexOf(filtrarFormulasContratosEntidades.get(index));
            listFormulasContratosEntidades.remove(VCIndex);
            filtrarFormulasContratosEntidades.remove(index);
            infoRegistro = "Cantidad de registros: " + filtrarFormulasContratosEntidades.size();

         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
         index = -1;
         secRegistro = null;
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         cambioFormulasContratosEntidades = true;
      }

   }

   public void valoresBackupAutocompletar(int tipoNuevo, String valorCambio) {
      log.info("1...");
      if (valorCambio.equals("FORMULA")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarTipoEntidad = nuevoFormulasContratosEntidades.getTipoentidad().getNombre();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarTipoEntidad = duplicarFormulasContratosEntidades.getTipoentidad().getNombre();
         }

         log.info("CONCEPTO : " + nuevoYduplicarCompletarTipoEntidad);
      }
   }

   public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {

      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();

      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         log.info(" nueva Operando    Entro al if 'Centro costo'");
         log.info("NUEVO PERSONA :-------> " + nuevoFormulasContratosEntidades.getTipoentidad().getNombre());

         if (!nuevoFormulasContratosEntidades.getTipoentidad().getNombre().equals("")) {
            log.info("ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("valorConfirmar: " + valorConfirmar);
            log.info("nuevoYduplicarCompletarPersona: " + nuevoYduplicarCompletarTipoEntidad);
            nuevoFormulasContratosEntidades.getTipoentidad().setNombre(nuevoYduplicarCompletarTipoEntidad);
            for (int i = 0; i < listaTiposEntidades.size(); i++) {
               if (listaTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoFormulasContratosEntidades.setTipoentidad(listaTiposEntidades.get(indiceUnicoElemento));
               listaTiposEntidades = null;
               log.error("PERSONA GUARDADA :-----> " + nuevoFormulasContratosEntidades.getTipoentidad().getNombre());
            } else {
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoFormulasContratosEntidades.getTipoentidad().setNombre(nuevoYduplicarCompletarTipoEntidad);
            log.info("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoFormulasContratosEntidades.setTipoentidad(new TiposEntidades());
            nuevoFormulasContratosEntidades.getTipoentidad().setNombre(" ");
            log.info("NUEVA NORMA LABORAL" + nuevoFormulasContratosEntidades.getTipoentidad().getNombre());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");

      }
   }

   public void asignarVariableFormulas(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:personasDialogo");
      RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      log.info("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("FORMULA")) {
         log.info("DUPLICAR valorConfirmar : " + valorConfirmar);
         log.info("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarTipoEntidad);

         if (!duplicarFormulasContratosEntidades.getTipoentidad().getNombre().equals("")) {
            log.info("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            log.info("DUPLICAR valorConfirmar: " + valorConfirmar);
            log.info("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarTipoEntidad);
            duplicarFormulasContratosEntidades.getTipoentidad().setNombre(nuevoYduplicarCompletarTipoEntidad);
            for (int i = 0; i < listaTiposEntidades.size(); i++) {
               if (listaTiposEntidades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            log.info("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarFormulasContratosEntidades.setTipoentidad(listaTiposEntidades.get(indiceUnicoElemento));
               listaTiposEntidades = null;
            } else {
               RequestContext.getCurrentInstance().update("form:personasDialogo");
               RequestContext.getCurrentInstance().execute("PF('personasDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            //duplicarFormulasContratosEntidades.getEmpresa().setNombre(nuevoYduplicarCompletarPais);
            log.info("DUPLICAR valorConfirmar cuando es vacio: " + valorConfirmar);
            log.info("DUPLICAR INDEX : " + index);
            duplicarFormulasContratosEntidades.setTipoentidad(new TiposEntidades());
            duplicarFormulasContratosEntidades.getTipoentidad().setNombre(" ");

            log.info("DUPLICAR PERSONA  : " + duplicarFormulasContratosEntidades.getTipoentidad().getNombre());
            log.info("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarTipoEntidad);
            if (tipoLista == 0) {
               listFormulasContratosEntidades.get(index).getTipoentidad().setNombre(nuevoYduplicarCompletarTipoEntidad);
               log.error("tipo lista" + tipoLista);
               log.error("Secuencia Parentesco " + listFormulasContratosEntidades.get(index).getTipoentidad().getSecuencia());
            } else if (tipoLista == 1) {
               filtrarFormulasContratosEntidades.get(index).getTipoentidad().setNombre(nuevoYduplicarCompletarTipoEntidad);
            }

         }

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      }
   }

   public void revisarDialogoGuardar() {

      if (!borrarFormulasContratosEntidades.isEmpty() || !crearFormulasContratosEntidades.isEmpty() || !modificarFormulasContratosEntidades.isEmpty()) {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }

   }

   public void guardarFormulasContratosEntidades() {
      RequestContext context = RequestContext.getCurrentInstance();

      if (guardado == false) {
         log.info("Realizando guardarFormulasContratosEntidades");
         if (!borrarFormulasContratosEntidades.isEmpty()) {
            administrarFormulasContratosEntidades.borrarFormulasContratosEntidades(borrarFormulasContratosEntidades);
            //mostrarBorrados
            registrosBorrados = borrarFormulasContratosEntidades.size();
            RequestContext.getCurrentInstance().update("form:mostrarBorrados");
            RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
            borrarFormulasContratosEntidades.clear();
         }
         if (!modificarFormulasContratosEntidades.isEmpty()) {
            administrarFormulasContratosEntidades.modificarFormulasContratosEntidades(modificarFormulasContratosEntidades);
            modificarFormulasContratosEntidades.clear();
         }
         if (!crearFormulasContratosEntidades.isEmpty()) {
            administrarFormulasContratosEntidades.crearFormulasContratosEntidades(crearFormulasContratosEntidades);
            crearFormulasContratosEntidades.clear();
         }
         log.info("Se guardaron los datos con exito");
         listFormulasContratosEntidades = null;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
         k = 0;
         guardado = true;

      }
      index = -1;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");

   }

   public void editarCelda() {
      if (index >= 0) {
         if (tipoLista == 0) {
            editarFormulasContratosEntidades = listFormulasContratosEntidades.get(index);
         }
         if (tipoLista == 1) {
            editarFormulasContratosEntidades = filtrarFormulasContratosEntidades.get(index);
         }

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPais");
            RequestContext.getCurrentInstance().execute("PF('editPais').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editFormulas");
            RequestContext.getCurrentInstance().execute("PF('editFormulas').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editOperandos");
            RequestContext.getCurrentInstance().execute("PF('editOperandos').show()");
            cualCelda = -1;
         }

      }
      index = -1;
      secRegistro = null;
   }

   public void agregarNuevoFormulasContratosEntidades() {
      log.info("agregarNuevoFormulasContratosEntidades");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();

      if (nuevoFormulasContratosEntidades.getTipoentidad().getNombre() == null || nuevoFormulasContratosEntidades.getTipoentidad().getNombre().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Tipo Entidad \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         for (int j = 0; j < listFormulasContratosEntidades.size(); j++) {
            if (nuevoFormulasContratosEntidades.getTipoentidad().getNombre().equals(listFormulasContratosEntidades.get(j).getTipoentidad().getNombre())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "No pueden haber Tipos Entidades Repetidas";
         } else {
            contador++;//3
         }
      }

      if (contador == 1) {
         if (bandera == 1) {
            //CERRAR FILTRADO
            log.info("Desactivar");
            personafir = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulasContratosEntidades:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            filtrarFormulasContratosEntidades = null;
            tipoLista = 0;
         }
         log.info("Despues de la bandera");

         k++;
         l = BigInteger.valueOf(k);
         nuevoFormulasContratosEntidades.setSecuencia(l);
         nuevoFormulasContratosEntidades.setFormulacontrato(formulaContratoSeleccionada);
         crearFormulasContratosEntidades.add(nuevoFormulasContratosEntidades);
         listFormulasContratosEntidades.add(nuevoFormulasContratosEntidades);
         infoRegistro = "Cantidad de registros: " + listFormulasContratosEntidades.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         nuevoFormulasContratosEntidades = new FormulasContratosEntidades();
         nuevoFormulasContratosEntidades.setTipoentidad(new TiposEntidades());
         RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }

         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroFormulasContratosEntidades').hide()");
         index = -1;
         secRegistro = null;
         cambioFormulasContratosEntidades = true;

      } else {

         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoFormulasContratosEntidades() {
      log.info("limpiarNuevoFormulasContratosEntidades");
      nuevoFormulasContratosEntidades = new FormulasContratosEntidades();
      nuevoFormulasContratosEntidades.setTipoentidad(new TiposEntidades());
      secRegistro = null;
      index = -1;

   }

   //------------------------------------------------------------------------------
   public void cargarNuevoFormulasContratosEntidades() {
      log.info("cargarNuevoFormulasContratosEntidades");

      duplicarFormulasContratosEntidades = new FormulasContratosEntidades();
      duplicarFormulasContratosEntidades.setTipoentidad(new TiposEntidades());
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().execute("PF('nuevoRegistroFormulasContratosEntidades').show()");

   }

   public void duplicandoFormulasContratosEntidades() {
      log.info("duplicandoFormulasContratosEntidades");
      if (index >= 0) {
         duplicarFormulasContratosEntidades = new FormulasContratosEntidades();
         duplicarFormulasContratosEntidades.setTipoentidad(new TiposEntidades());
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarFormulasContratosEntidades.setSecuencia(l);
            duplicarFormulasContratosEntidades.setTipoentidad(listFormulasContratosEntidades.get(index).getTipoentidad());
         }
         if (tipoLista == 1) {
            duplicarFormulasContratosEntidades.setSecuencia(l);
            duplicarFormulasContratosEntidades.setTipoentidad(filtrarFormulasContratosEntidades.get(index).getTipoentidad());
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroFormulasContratosEntidades').show()");
         secRegistro = null;
      }
   }

   public void confirmarDuplicar() {
      log.error("ESTOY EN CONFIRMAR DUPLICAR TIPOS EMPRESAS");
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      Integer a = 0;
      a = null;

      if (duplicarFormulasContratosEntidades.getTipoentidad().getNombre() == null || duplicarFormulasContratosEntidades.getTipoentidad().getNombre().isEmpty()) {
         mensajeValidacion = mensajeValidacion + "   *Tipo Entidad \n";
         log.info("Mensaje validacion : " + mensajeValidacion);

      } else {
         for (int j = 0; j < listFormulasContratosEntidades.size(); j++) {
            if (duplicarFormulasContratosEntidades.getTipoentidad().getNombre().equals(listFormulasContratosEntidades.get(j).getTipoentidad().getNombre())) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "No pueden haber Tipos Entidades Repetidas";
         } else {
            contador++;//3
         }
      }

      if (contador == 1) {

         if (crearFormulasContratosEntidades.contains(duplicarFormulasContratosEntidades)) {
            log.info("Ya lo contengo.");
         }
         duplicarFormulasContratosEntidades.setFormulacontrato(formulaContratoSeleccionada);
         listFormulasContratosEntidades.add(duplicarFormulasContratosEntidades);
         crearFormulasContratosEntidades.add(duplicarFormulasContratosEntidades);
         RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
         index = -1;
         log.info("--------------DUPLICAR------------------------");
         log.info("PERSONA : " + duplicarFormulasContratosEntidades.getTipoentidad().getNombre());
         log.info("--------------DUPLICAR------------------------");

         secRegistro = null;
         if (guardado == true) {
            guardado = false;
         }
         infoRegistro = "Cantidad de registros: " + listFormulasContratosEntidades.size();
         RequestContext.getCurrentInstance().update("form:informacionRegistro");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         if (bandera == 1) {
            //CERRAR FILTRADO
            personafir = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosFormulasContratosEntidades:personafir");
            personafir.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosFormulasContratosEntidades");
            bandera = 0;
            filtrarFormulasContratosEntidades = null;
            tipoLista = 0;
         }

         duplicarFormulasContratosEntidades = new FormulasContratosEntidades();
         duplicarFormulasContratosEntidades.setTipoentidad(new TiposEntidades());

         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroFormulasContratosEntidades').hide()");
         index = -1;
      } else {

         contador = 0;

         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarFormulasContratosEntidades() {
      duplicarFormulasContratosEntidades = new FormulasContratosEntidades();
      duplicarFormulasContratosEntidades.setTipoentidad(new TiposEntidades());
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFormulasContratosEntidadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "FORMULASCONTRATOSENTIDADES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosFormulasContratosEntidadesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "FORMULASCONTRATOSENTIDADES", false, false, "UTF-8", null, null);
      context.responseComplete();
      index = -1;
      secRegistro = null;
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      log.info("lol");
      if (!listFormulasContratosEntidades.isEmpty()) {
         if (secRegistro != null) {
            log.info("lol 2");
            int resultado = administrarRastros.obtenerTabla(secRegistro, "FORMULASCONTRATOSENTIDADES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
         } else {
            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
         }
      } else if (administrarRastros.verificarHistoricosTabla("FORMULASCONTRATOSENTIDADES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
      index = -1;
   }
   private boolean cambioFormulasContratosEntidades = false;

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<FormulasContratosEntidades> getListFormulasContratosEntidades() {
      if (listFormulasContratosEntidades == null) {
         listFormulasContratosEntidades = administrarFormulasContratosEntidades.consultarFormulasContratosEntidadesPorFormulaContrato(secuenciaFormulasContratos);
      }
      RequestContext context = RequestContext.getCurrentInstance();
      if (listFormulasContratosEntidades == null || listFormulasContratosEntidades.isEmpty()) {
         infoRegistro = "Cantidad de registros: 0 ";
      } else {
         infoRegistro = "Cantidad de registros: " + listFormulasContratosEntidades.size();
      }
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
      return listFormulasContratosEntidades;
   }

   public void setListFormulasContratosEntidades(List<FormulasContratosEntidades> listFormulasContratosEntidades) {
      this.listFormulasContratosEntidades = listFormulasContratosEntidades;
   }

   public Formulascontratos getFormulaContratoSeleccionada() {
      if (formulaContratoSeleccionada == null) {
         formulaContratoSeleccionada = administrarFormulasContratosEntidades.consultarFormulaDeFormulasContratosEntidades(secuenciaFormulasContratos);
      }
      return formulaContratoSeleccionada;
   }

   public void setFormulaContratoSeleccionada(Formulascontratos formulaContratoSeleccionada) {
      this.formulaContratoSeleccionada = formulaContratoSeleccionada;
   }

   public List<FormulasContratosEntidades> getFiltrarFormulasContratosEntidades() {
      return filtrarFormulasContratosEntidades;
   }

   public void setFiltrarFormulasContratosEntidades(List<FormulasContratosEntidades> filtrarFormulasContratosEntidades) {
      this.filtrarFormulasContratosEntidades = filtrarFormulasContratosEntidades;
   }

   public FormulasContratosEntidades getNuevoFormulasContratosEntidades() {
      return nuevoFormulasContratosEntidades;
   }

   public void setNuevoFormulasContratosEntidades(FormulasContratosEntidades nuevoFormulasContratosEntidades) {
      this.nuevoFormulasContratosEntidades = nuevoFormulasContratosEntidades;
   }

   public FormulasContratosEntidades getDuplicarFormulasContratosEntidades() {
      return duplicarFormulasContratosEntidades;
   }

   public void setDuplicarFormulasContratosEntidades(FormulasContratosEntidades duplicarFormulasContratosEntidades) {
      this.duplicarFormulasContratosEntidades = duplicarFormulasContratosEntidades;
   }

   public FormulasContratosEntidades getEditarFormulasContratosEntidades() {
      return editarFormulasContratosEntidades;
   }

   public void setEditarFormulasContratosEntidades(FormulasContratosEntidades editarFormulasContratosEntidades) {
      this.editarFormulasContratosEntidades = editarFormulasContratosEntidades;
   }

   public BigInteger getSecRegistro() {
      return secRegistro;
   }

   public void setSecRegistro(BigInteger secRegistro) {
      this.secRegistro = secRegistro;
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

   public List<TiposEntidades> getListaTiposEntidades() {
      if (listaTiposEntidades == null) {
         listaTiposEntidades = administrarFormulasContratosEntidades.consultarLOVTiposEntidades();
      }
      RequestContext context = RequestContext.getCurrentInstance();

      if (listaTiposEntidades == null || listaTiposEntidades.isEmpty()) {
         infoRegistroLOVTipoEntidad = "Cantidad de registros: 0 ";
      } else {
         infoRegistroLOVTipoEntidad = "Cantidad de registros: " + listaTiposEntidades.size();
      }
      RequestContext.getCurrentInstance().update("form:infoRegistroLOVTipoEntidad");
      return listaTiposEntidades;
   }

   public void setListaFormulas(List<TiposEntidades> listaTiposEntidades) {
      this.listaTiposEntidades = listaTiposEntidades;
   }

   public List<TiposEntidades> getFiltradoTiposEntidades() {
      return filtradoTiposEntidades;
   }

   public void setFiltradoTiposEntidades(List<TiposEntidades> filtradoTiposEntidades) {
      this.filtradoTiposEntidades = filtradoTiposEntidades;
   }

   public TiposEntidades getTipoentidadSeleccionado() {
      return tipoEntidadSeleccionado;
   }

   public void setTipoentidadSeleccionado(TiposEntidades tipoEntidadSeleccionado) {
      this.tipoEntidadSeleccionado = tipoEntidadSeleccionado;
   }

   public FormulasContratosEntidades getFormulaContratoEntidadSeleccionada() {
      return formulaContratoEntidadSeleccionada;
   }

   public void setFormulaContratoEntidadSeleccionada(FormulasContratosEntidades formulaContratoEntidadSeleccionada) {
      this.formulaContratoEntidadSeleccionada = formulaContratoEntidadSeleccionada;
   }

   public String getInfoRegistro() {
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroLOVTipoEntidad() {
      return infoRegistroLOVTipoEntidad;
   }

   public void setInfoRegistroLOVTipoEntidad(String infoRegistroLOVTipoEntidad) {
      this.infoRegistroLOVTipoEntidad = infoRegistroLOVTipoEntidad;
   }

}
