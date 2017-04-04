/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Operandos;
import Entidades.ConceptosSoportes;
import Entidades.Conceptos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarConceptosSoportesInterface;
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
public class ControlConceptosSoportes implements Serializable {

   @EJB
   AdministrarConceptosSoportesInterface administrarConceptosSoportes;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<ConceptosSoportes> listConceptosSoportes;
   private List<ConceptosSoportes> filtrarConceptosSoportes;
   private ConceptosSoportes conceptoSoportesSeleccionado;
   //
   private List<ConceptosSoportes> conceptosSoportesCrear;
   private List<ConceptosSoportes> conceptosSoportesModificar;
   private List<ConceptosSoportes> conceptosSoportesBorrar;
   private ConceptosSoportes nuevoConceptosSoportes;
   private ConceptosSoportes duplicarConceptosSoportes;
   private ConceptosSoportes editarConceptosSoportes;
   //otros
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   //AutoCompletar
   private boolean permitirIndex;
   //RASTRO
   private Column codigo, personafir, cargo;
   //borrado
   private String mensajeValidacion;
   //filtrado table
   private int tamano;
   private BigInteger backupCodigo;
   private String backupPais;
   //---------------------------------
   private String backupOperando;
   private List<Conceptos> lovConceptos;
   private List<Conceptos> filtradoLovConceptos;
   private Conceptos conceptoLovSeleccionado;
   private String nuevoYduplicarCompletarPersona;
   //--------------------------------------
   private String backupConcepto;
   private List<Operandos> lovOperandos;
   private List<Operandos> filtradoLovOperandos;
   private Operandos operandoLovSeleccionado;
   private String nuevoYduplicarCompletarCargo;

   //---------------------------------
   private List<ConceptosSoportes> lovConceptosSoportes;
   private List<ConceptosSoportes> filterLovConceptosSoportes;
   private ConceptosSoportes conceptoSoporteLovSeleccionado;

   private boolean cambioConceptosSoportes;
   private String infoRegistro, infoRegistroLovCS, infoRegistroLovConceptos, infoRegistroLovOperandos;

   private boolean mostrarTodos;
   private boolean buscarConceptoSoporte;
   private BigInteger nuevoYduplicarCompletarCodigoConcepto;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlConceptosSoportes() {
      paginaAnterior = "nominaf";
      banderaConceptoEscogido = true;
      lovConceptosSoportes = null;
      listConceptosSoportes = null;
      conceptosSoportesCrear = new ArrayList<ConceptosSoportes>();
      conceptosSoportesModificar = new ArrayList<ConceptosSoportes>();
      conceptosSoportesBorrar = new ArrayList<ConceptosSoportes>();
      permitirIndex = true;
      conceptoSoportesSeleccionado = null;
      editarConceptosSoportes = new ConceptosSoportes();
      nuevoConceptosSoportes = new ConceptosSoportes();
      nuevoConceptosSoportes.setConcepto(new Conceptos());
      nuevoConceptosSoportes.setOperando(new Operandos());
      duplicarConceptosSoportes = new ConceptosSoportes();
      duplicarConceptosSoportes.setConcepto(new Conceptos());
      duplicarConceptosSoportes.setOperando(new Operandos());
      lovConceptos = null;
      filtradoLovConceptos = null;
      lovOperandos = null;
      filtradoLovOperandos = null;
      guardado = true;
      tamano = 320;
      mostrarTodos = true;
      aceptar = true;
      buscarConceptoSoporte = false;
      cambioConceptosSoportes = false;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String paginaAnterior) {
      conceptoSoportesSeleccionado = null;
      this.paginaAnterior = paginaAnterior;
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      this.paginaAnterior = paginaAnterior;
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
         String pagActual = "conceptosoporte";
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
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarConceptosSoportes.obtenerConexion(ses.getId());
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
      conceptoSoportesSeleccionado = null;
      contarRegistros();
   }

   public void cambiarIndice(ConceptosSoportes conceptoS, int celda) {
      System.err.println("TIPO LISTA = " + tipoLista);
      if (permitirIndex == true) {
         conceptoSoportesSeleccionado = conceptoS;
         cualCelda = celda;
         if (cualCelda == 0) {
            backupCodigo = conceptoSoportesSeleccionado.getConcepto().getCodigo();
            backupConcepto = conceptoSoportesSeleccionado.getConcepto().getDescripcion();
         }
         if (cualCelda == 1) {
            backupCodigo = conceptoSoportesSeleccionado.getConcepto().getCodigo();
            backupConcepto = conceptoSoportesSeleccionado.getConcepto().getDescripcion();
            System.out.println("CONCEPTO : " + backupConcepto);
         }
         if (cualCelda == 2) {
            backupOperando = conceptoSoportesSeleccionado.getOperando().getNombre();
            System.out.println("OPERANDO : " + backupOperando);
         }
      }
   }

   public void asignarIndex(ConceptosSoportes conceptoS, int tipoAct, int campo) {
      conceptoSoportesSeleccionado = conceptoS;
      tipoActualizacion = tipoAct;
      if (campo == 0 || campo == 1) {
         RequestContext.getCurrentInstance().update("form:conceptosDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
         RequestContext.getCurrentInstance().update("form:lovConceptos");
         campo = -1;
      }
      if (campo == 2) {
         lovOperandos = null;
         getLovOperandos();
         FacesContext c = FacesContext.getCurrentInstance();
         DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOperandos");
         tabla.setValue(lovOperandos);

         filtradoLovConceptos = null;
         conceptoLovSeleccionado = null;
         RequestContext.getCurrentInstance().update("form:operandosDialogo");
         RequestContext.getCurrentInstance().update("form:lovOperandos");
         RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
         campo = -1;
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void listaValoresBoton() {
      if (conceptoSoportesSeleccionado != null) {
         if (cualCelda == 0 || cualCelda == 0) {
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            RequestContext.getCurrentInstance().update("form:lovConceptos");
         }
         if (cualCelda == 2) {
            lovOperandos = null;
            getLovOperandos();
            FacesContext c = FacesContext.getCurrentInstance();
            DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOperandos");
            tabla.setValue(lovOperandos);
            filtradoLovConceptos = null;
            conceptoLovSeleccionado = null;
            RequestContext.getCurrentInstance().update("form:operandosDialogo");
            RequestContext.getCurrentInstance().update("form:lovOperandos");
            RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
         }
      }
   }

   public void cancelarModificacionCambio() {
      if (bandera == 1) {
         restaurarTabla();
      }

      conceptosSoportesBorrar.clear();
      conceptosSoportesCrear.clear();
      conceptosSoportesModificar.clear();
      conceptoSoportesSeleccionado = null;
      k = 0;
      listConceptosSoportes = null;
      guardado = true;
      permitirIndex = true;
      seleccionConceptoSoporte();
      RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         restaurarTabla();
      }
      conceptosSoportesBorrar.clear();
      conceptosSoportesCrear.clear();
      conceptosSoportesModificar.clear();
      conceptoSoportesSeleccionado = null;
      k = 0;
      listConceptosSoportes = null;
      guardado = true;
      permitirIndex = true;
      getListConceptosSoportes();
      mostrarTodos = true;
      buscarConceptoSoporte = false;
      RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
      RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
      RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      contarRegistros();
   }

   public void salir() {  limpiarListasValor();
      if (bandera == 1) {
         restaurarTabla();
      }

      conceptosSoportesBorrar.clear();
      conceptosSoportesCrear.clear();
      conceptosSoportesModificar.clear();
      conceptoSoportesSeleccionado = null;
      k = 0;
      listConceptosSoportes = null;
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void activarCtrlF11() {
      if (bandera == 0) {
         tamano = 300;
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:codigo");
         codigo.setFilterStyle("width: 85% !important");
         personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:personafir");
         personafir.setFilterStyle("width: 85% !important");
         cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:cargo");
         cargo.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
         bandera = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

   public void actualizarConceptos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         conceptoSoportesSeleccionado.setConcepto(conceptoLovSeleccionado);

         if (!conceptosSoportesCrear.contains(conceptoSoportesSeleccionado)) {
            if (conceptosSoportesModificar.isEmpty()) {
               conceptosSoportesModificar.add(conceptoSoportesSeleccionado);
            } else if (!conceptosSoportesModificar.contains(conceptoSoportesSeleccionado)) {
               conceptosSoportesModificar.add(conceptoSoportesSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         System.out.println("ACTUALIZAR CONCEPTO NUEVO DEPARTAMENTO: " + conceptoLovSeleccionado.getDescripcion());
         nuevoConceptosSoportes.setConcepto(conceptoLovSeleccionado);
         lovOperandos = null;
         getLovOperandos();
         FacesContext c = FacesContext.getCurrentInstance();
         DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOperandos");
         tabla.setValue(lovOperandos);
         banderaConceptoEscogido = false;
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
         RequestContext.getCurrentInstance().update("formularioDialogos:btnnuevoCargo");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");
      } else if (tipoActualizacion == 2) {
         System.out.println("ACTUALIZAR CONCEPTO DUPLICAR DEPARTAMENO: " + conceptoLovSeleccionado.getDescripcion());
         duplicarConceptosSoportes.setConcepto(conceptoLovSeleccionado);
         lovOperandos = null;
         getLovOperandos();
         FacesContext c = FacesContext.getCurrentInstance();
         DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOperandos");
         tabla.setValue(lovOperandos);
         banderaConceptoEscogido = false;
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
         RequestContext.getCurrentInstance().update("formularioDialogos:btnduplicarCargo");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
      }
      filtradoLovConceptos = null;
      conceptoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      cambioConceptosSoportes = true;
      RequestContext.getCurrentInstance().reset("form:lovConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:conceptosDialogo");
      RequestContext.getCurrentInstance().update("form:lovConceptos");
      RequestContext.getCurrentInstance().update("form:aceptarPer");
   }

   public void actualizarOperandos() {
      if (tipoActualizacion == 0) {
         conceptoSoportesSeleccionado.setOperando(operandoLovSeleccionado);

         if (!conceptosSoportesCrear.contains(conceptoSoportesSeleccionado)) {
            if (conceptosSoportesModificar.isEmpty()) {
               conceptosSoportesModificar.add(conceptoSoportesSeleccionado);
            } else if (!conceptosSoportesModificar.contains(conceptoSoportesSeleccionado)) {
               conceptosSoportesModificar.add(conceptoSoportesSeleccionado);
            }
         }
         if (guardado == true) {
            guardado = false;
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else if (tipoActualizacion == 1) {
         System.out.println("ACTUALIZAR OPERANDOS NUEVO DEPARTAMENTO: " + operandoLovSeleccionado.getNombre());
         nuevoConceptosSoportes.setOperando(operandoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
      } else if (tipoActualizacion == 2) {
         System.out.println("ACTUALIZAR OPERANDOS DUPLICAR DEPARTAMENO: " + operandoLovSeleccionado.getNombre());
         duplicarConceptosSoportes.setOperando(operandoLovSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
      }
      filtradoLovConceptos = null;
      conceptoLovSeleccionado = null;
      lovOperandos = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      cambioConceptosSoportes = true;
      RequestContext.getCurrentInstance().reset("form:lovOperandos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOperandos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('operandosDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:lovOperandos");
      RequestContext.getCurrentInstance().update("form:operandosDialogo");
      RequestContext.getCurrentInstance().update("form:aceptarCar");
   }

   public void cancelarCambioConceptos() {
      if (conceptoSoportesSeleccionado != null) {
         conceptoSoportesSeleccionado.getConcepto().setDescripcion(backupConcepto);
         conceptoSoportesSeleccionado.getConcepto().setCodigo(backupCodigo);
      }
      filtradoLovConceptos = null;
      conceptoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovConceptos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovConceptos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').hide()");
   }

   public void cancelarCambioOperandos() {
      filtradoLovOperandos = null;
      operandoLovSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovOperandos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovOperandos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('operandosDialogo').hide()");
   }

   public void modificarConceptosSoportes(ConceptosSoportes conceptoS, String confirmarCambio, String valorConfirmar) {
      conceptoSoportesSeleccionado = conceptoS;
      int coincidencias = 0;
      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;
      boolean banderita2 = false;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("TIPO LISTA = " + tipoLista);

      if (confirmarCambio.equalsIgnoreCase("PERSONASCODIGO")) {
         if (!conceptoSoportesSeleccionado.getConcepto().getDescripcion().equals("")) {
            conceptoSoportesSeleccionado.getConcepto().setCodigo(backupCodigo);
            conceptoSoportesSeleccionado.getConcepto().setDescripcion(backupConcepto);

            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getCodigo().equals(new BigInteger(valorConfirmar))) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               conceptoSoportesSeleccionado.setConcepto(lovConceptos.get(indiceUnicoElemento));

            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:conceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
               RequestContext.getCurrentInstance().update("form:lovConceptos");
               tipoActualizacion = 0;
            }
         } else {
            if (backupConcepto != null) {
               conceptoSoportesSeleccionado.getConcepto().setCodigo(backupCodigo);
               conceptoSoportesSeleccionado.getConcepto().setDescripcion(backupConcepto);
            }
            tipoActualizacion = 0;
            System.out.println("CODIGO DIALOGO CONCEPTO : " + backupCodigo);
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            RequestContext.getCurrentInstance().update("form:lovConceptos");
         }

         if (coincidencias == 1) {
            if (!conceptosSoportesCrear.contains(conceptoSoportesSeleccionado)) {

               if (conceptosSoportesModificar.isEmpty()) {
                  conceptosSoportesModificar.add(conceptoSoportesSeleccionado);
               } else if (!conceptosSoportesModificar.contains(conceptoSoportesSeleccionado)) {
                  conceptosSoportesModificar.add(conceptoSoportesSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
         }

         RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         cambioConceptosSoportes = true;

      } else if (confirmarCambio.equalsIgnoreCase("PERSONAS")) {
         System.out.println("MODIFICANDO NORMA LABORAL : " + conceptoSoportesSeleccionado.getConcepto().getDescripcion());
         if (!conceptoSoportesSeleccionado.getConcepto().getDescripcion().equals("")) {
            conceptoSoportesSeleccionado.getConcepto().setCodigo(backupCodigo);
            conceptoSoportesSeleccionado.getConcepto().setDescripcion(backupConcepto);

            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               conceptoSoportesSeleccionado.setConcepto(lovConceptos.get(indiceUnicoElemento));
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:conceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
               RequestContext.getCurrentInstance().update("form:lovConceptos");
               tipoActualizacion = 0;
            }
         } else {
            if (backupConcepto != null) {
               conceptoSoportesSeleccionado.getConcepto().setCodigo(backupCodigo);
               conceptoSoportesSeleccionado.getConcepto().setDescripcion(backupConcepto);
            }
            tipoActualizacion = 0;
            System.out.println("BACKUPCONCEPTO CUANDO ES NULO : " + backupConcepto);
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            RequestContext.getCurrentInstance().update("form:lovConceptos");
         }

         if (coincidencias == 1) {
            if (!conceptosSoportesCrear.contains(conceptoSoportesSeleccionado)) {

               if (conceptosSoportesModificar.isEmpty()) {
                  conceptosSoportesModificar.add(conceptoSoportesSeleccionado);
               } else if (!conceptosSoportesModificar.contains(conceptoSoportesSeleccionado)) {
                  conceptosSoportesModificar.add(conceptoSoportesSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
         }

         RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

      } else if (confirmarCambio.equalsIgnoreCase("CARGOS")) {
         System.out.println("MODIFICANDO CARGO: " + conceptoSoportesSeleccionado.getOperando().getNombre());
         if (!conceptoSoportesSeleccionado.getOperando().getNombre().equals("")) {
            conceptoSoportesSeleccionado.getOperando().setNombre(backupOperando);

            for (int i = 0; i < lovOperandos.size(); i++) {
               if (lovOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }

            if (coincidencias == 1) {
               conceptoSoportesSeleccionado.setOperando(lovOperandos.get(indiceUnicoElemento));

            } else {
               lovOperandos = null;
               getLovOperandos();
               FacesContext c = FacesContext.getCurrentInstance();
               DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOperandos");
               tabla.setValue(lovOperandos);
               permitirIndex = false;
               filtradoLovConceptos = null;
               conceptoLovSeleccionado = null;
               RequestContext.getCurrentInstance().update("form:operandosDialogo");
               RequestContext.getCurrentInstance().update("form:lovOperandos");
               RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
               tipoActualizacion = 0;
            }
         } else {
            if (backupOperando != null) {
               conceptoSoportesSeleccionado.getOperando().setNombre(backupOperando);
            }
            tipoActualizacion = 0;
            System.out.println("PAIS ANTES DE MOSTRAR DIALOGO CARGOS : " + backupOperando);
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            RequestContext.getCurrentInstance().update("form:lovConceptos");
         }

         if (coincidencias == 1) {
            if (!conceptosSoportesCrear.contains(conceptoSoportesSeleccionado)) {

               if (conceptosSoportesModificar.isEmpty()) {
                  conceptosSoportesModificar.add(conceptoSoportesSeleccionado);
               } else if (!conceptosSoportesModificar.contains(conceptoSoportesSeleccionado)) {
                  conceptosSoportesModificar.add(conceptoSoportesSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
         }
         RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         cambioConceptosSoportes = true;
      }
   }

   public void modificarConceptosSoportesCodigo(ConceptosSoportes conceptoS, String confirmarCambio, BigInteger valorConfirmar) {
      System.err.println("ENTRE A MODIFICAR SUB CATEGORIA");
      conceptoSoportesSeleccionado = conceptoS;
      int coincidencias = 0;
      int contador = 0;
      boolean banderita = false;
      boolean banderita1 = false;
      boolean banderita2 = false;
      int indiceUnicoElemento = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      System.err.println("TIPO LISTA = " + tipoLista);

      if (confirmarCambio.equalsIgnoreCase("PERSONASCODIGO")) {
         System.out.println("MODIFICANDO CODIGO CONCEPTO: " + conceptoSoportesSeleccionado.getConcepto().getCodigo());
         if (!conceptoSoportesSeleccionado.getConcepto().getDescripcion().equals("")) {
            conceptoSoportesSeleccionado.getConcepto().setCodigo(backupCodigo);
            conceptoSoportesSeleccionado.getConcepto().setDescripcion(backupConcepto);

            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getCodigo().equals(valorConfirmar)) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            if (coincidencias == 1) {
               conceptoSoportesSeleccionado.setConcepto(lovConceptos.get(indiceUnicoElemento));
               lovConceptos.clear();
               lovConceptos = null;
            } else {
               permitirIndex = false;
               RequestContext.getCurrentInstance().update("form:conceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
               RequestContext.getCurrentInstance().update("form:lovConceptos");
               tipoActualizacion = 0;
            }
         } else {
            if (backupCodigo != null) {
               conceptoSoportesSeleccionado.getConcepto().setCodigo(backupCodigo);
               conceptoSoportesSeleccionado.getConcepto().setDescripcion(backupConcepto);
            }
            tipoActualizacion = 0;
            System.out.println("CODIGO DIALOGO CONCEPTO : " + backupCodigo);
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            RequestContext.getCurrentInstance().update("form:lovConceptos");
         }

         if (coincidencias == 1) {
            if (!conceptosSoportesCrear.contains(conceptoSoportesSeleccionado)) {

               if (conceptosSoportesModificar.isEmpty()) {
                  conceptosSoportesModificar.add(conceptoSoportesSeleccionado);
               } else if (!conceptosSoportesModificar.contains(conceptoSoportesSeleccionado)) {
                  conceptosSoportesModificar.add(conceptoSoportesSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
               }
            }
         }
         cambioConceptosSoportes = true;
         RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void borrandoConceptosSoportes() {
      if (conceptoSoportesSeleccionado != null) {
         System.out.println("Entro a borrandoConceptosSoportes");
         if (!conceptosSoportesModificar.isEmpty() && conceptosSoportesModificar.contains(conceptoSoportesSeleccionado)) {
            conceptosSoportesModificar.remove(conceptoSoportesSeleccionado);
            conceptosSoportesBorrar.add(conceptoSoportesSeleccionado);
         } else if (!conceptosSoportesCrear.isEmpty() && conceptosSoportesCrear.contains(conceptoSoportesSeleccionado)) {
            conceptosSoportesCrear.remove(conceptoSoportesSeleccionado);
         } else {
            conceptosSoportesBorrar.add(conceptoSoportesSeleccionado);
         }
         listConceptosSoportes.remove(conceptoSoportesSeleccionado);
         if (tipoLista == 1) {
            filtrarConceptosSoportes.remove(conceptoSoportesSeleccionado);
         }
         RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
         contarRegistros();

         conceptoSoportesSeleccionado = null;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         cambioConceptosSoportes = true;
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void valoresBackupAutocompletar(int tipoNuevo, String valorCambio) {
      System.out.println("1...");
      if (valorCambio.equals("CODIGOCONCEPTO")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarCodigoConcepto = nuevoConceptosSoportes.getConcepto().getCodigo();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarCodigoConcepto = duplicarConceptosSoportes.getConcepto().getCodigo();
         }
         System.out.println("CARGO : " + nuevoYduplicarCompletarCodigoConcepto);
      } else if (valorCambio.equals("PERSONA")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarPersona = nuevoConceptosSoportes.getConcepto().getDescripcion();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarPersona = duplicarConceptosSoportes.getConcepto().getDescripcion();
         }

         System.out.println("PERSONA : " + nuevoYduplicarCompletarPersona);
      } else if (valorCambio.equals("CARGO")) {
         if (tipoNuevo == 1) {
            nuevoYduplicarCompletarCargo = nuevoConceptosSoportes.getOperando().getNombre();
         } else if (tipoNuevo == 2) {
            nuevoYduplicarCompletarCargo = duplicarConceptosSoportes.getOperando().getNombre();
         }
         System.out.println("CARGO : " + nuevoYduplicarCompletarCargo);
      }

   }

   public void autocompletarNuevoCodigoBigInteger(String valor, int tipoNuevo) {

      int coincidencias = 0;
      int indiceUnicoElemento = 0;

      System.out.println(" nueva Operando    Entro al if 'Centro costo'");
      System.out.println("NUEVO PERSONA :-------> " + nuevoConceptosSoportes.getConcepto().getCodigo());

      if (!nuevoConceptosSoportes.getConcepto().getDescripcion().equals("")) {
         nuevoConceptosSoportes.getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getCodigo().toString().equalsIgnoreCase(valor)) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         System.out.println("Coincidencias: " + coincidencias);
         if (coincidencias == 1) {
            nuevoConceptosSoportes.setConcepto(lovConceptos.get(indiceUnicoElemento));
            banderaConceptoEscogido = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
            RequestContext.getCurrentInstance().update("formularioDialogos:btnnuevoCargo");

         } else {
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            RequestContext.getCurrentInstance().update("form:lovConceptos");
            tipoActualizacion = tipoNuevo;
         }
      } else {
         nuevoConceptosSoportes.getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
         nuevoConceptosSoportes.setConcepto(new Conceptos());
      }
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");
   }

   private boolean banderaConceptoEscogido = true;

   public void autocompletarNuevo(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;

      if (confirmarCambio.equalsIgnoreCase("PERSONA")) {

         if (!nuevoConceptosSoportes.getConcepto().getDescripcion().equals("")) {
            nuevoConceptosSoportes.getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoConceptosSoportes.setConcepto(lovConceptos.get(indiceUnicoElemento));
               banderaConceptoEscogido = false;
               RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
               RequestContext.getCurrentInstance().update("formularioDialogos:btnnuevoCargo");
            } else {
               RequestContext.getCurrentInstance().update("form:conceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
               RequestContext.getCurrentInstance().update("form:lovConceptos");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoConceptosSoportes.getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
            System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoConceptosSoportes.setConcepto(new Conceptos());
            nuevoConceptosSoportes.getConcepto().setDescripcion(" ");
            System.out.println("NUEVA NORMA LABORAL" + nuevoConceptosSoportes.getConcepto().getDescripcion());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoPersona");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCodigo");

      } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
         if (!nuevoConceptosSoportes.getOperando().getNombre().equals("")) {
            nuevoConceptosSoportes.getOperando().setNombre(nuevoYduplicarCompletarCargo);
            for (int i = 0; i < lovOperandos.size(); i++) {
               if (lovOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               nuevoConceptosSoportes.setOperando(lovOperandos.get(indiceUnicoElemento));
            } else {
               lovOperandos = null;
               getLovOperandos();
               FacesContext c = FacesContext.getCurrentInstance();
               DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOperandos");
               tabla.setValue(lovOperandos);
               filtradoLovConceptos = null;
               conceptoLovSeleccionado = null;
               RequestContext.getCurrentInstance().update("form:operandosDialogo");
               RequestContext.getCurrentInstance().update("form:lovOperandos");
               RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else {
            nuevoConceptosSoportes.getOperando().setNombre(nuevoYduplicarCompletarCargo);
            System.out.println("valorConfirmar cuando es vacio: " + valorConfirmar);
            nuevoConceptosSoportes.setOperando(new Operandos());
            nuevoConceptosSoportes.getOperando().setNombre(" ");
            System.out.println("NUEVO CARGO " + nuevoConceptosSoportes.getOperando().getNombre());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
      }
   }

   public void asignarVariableConceptos(int tipoNuevo) {
      if (tipoNuevo == 0) {
         tipoActualizacion = 1;
      }
      if (tipoNuevo == 1) {
         tipoActualizacion = 2;
      }
      RequestContext.getCurrentInstance().update("form:conceptosDialogo");
      RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
      RequestContext.getCurrentInstance().update("form:lovConceptos");
   }

   public void asignarVariableOperandos(int tipoNuevo) {
      lovOperandos = null;
      getLovOperandos();
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOperandos");
      tabla.setValue(lovOperandos);
      tipoActualizacion = tipoNuevo;
      filtradoLovConceptos = null;
      conceptoLovSeleccionado = null;
      RequestContext.getCurrentInstance().update("form:operandosDialogo");
      RequestContext.getCurrentInstance().update("form:lovOperandos");
      RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
   }

   public void autocompletarDuplicadoCodigoBigInteger(BigInteger valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;

      if (duplicarConceptosSoportes.getConcepto().getCodigo() != null) {
         duplicarConceptosSoportes.getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
         for (int i = 0; i < lovConceptos.size(); i++) {
            if (lovConceptos.get(i).getCodigo().equals(valorConfirmar)) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         System.out.println("Coincidencias: " + coincidencias);
         if (coincidencias == 1) {
            duplicarConceptosSoportes.setConcepto(lovConceptos.get(indiceUnicoElemento));
            banderaConceptoEscogido = false;
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
            RequestContext.getCurrentInstance().update("formularioDialogos:btnduplicarCargo");
         } else {
            RequestContext.getCurrentInstance().update("form:conceptosDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
            RequestContext.getCurrentInstance().update("form:lovConceptos");
            tipoActualizacion = tipoNuevo;
         }
      } else if (tipoNuevo == 2) {
         duplicarConceptosSoportes.setConcepto(new Conceptos());

         System.out.println("DUPLICAR PERSONA  : " + duplicarConceptosSoportes.getConcepto().getCodigo());
         System.out.println("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarCodigoConcepto);
         conceptoSoportesSeleccionado.getConcepto().setCodigo(nuevoYduplicarCompletarCodigoConcepto);
         System.err.println("tipo lista" + tipoLista);
         System.err.println("Secuencia Parentesco " + conceptoSoportesSeleccionado.getConcepto().getSecuencia());
      }
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
   }

   public void autocompletarDuplicado(String confirmarCambio, String valorConfirmar, int tipoNuevo) {
      System.out.println("DUPLICAR entrooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!");
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (confirmarCambio.equalsIgnoreCase("PERSONA")) {
         System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
         System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarPersona);

         if (!duplicarConceptosSoportes.getConcepto().getDescripcion().equals("")) {
            System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
            System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarPersona);
            duplicarConceptosSoportes.getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
            for (int i = 0; i < lovConceptos.size(); i++) {
               if (lovConceptos.get(i).getDescripcion().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarConceptosSoportes.setConcepto(lovConceptos.get(indiceUnicoElemento));
               banderaConceptoEscogido = false;
               RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
               RequestContext.getCurrentInstance().update("formularioDialogos:btnduplicarCargo");
            } else {
               RequestContext.getCurrentInstance().update("form:conceptosDialogo");
               RequestContext.getCurrentInstance().execute("PF('conceptosDialogo').show()");
               RequestContext.getCurrentInstance().update("form:lovConceptos");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            duplicarConceptosSoportes.setConcepto(new Conceptos());
            duplicarConceptosSoportes.getConcepto().setDescripcion(" ");

            System.out.println("DUPLICAR PERSONA  : " + duplicarConceptosSoportes.getConcepto().getDescripcion());
            System.out.println("nuevoYduplicarCompletarPERSONA : " + nuevoYduplicarCompletarPersona);
            conceptoSoportesSeleccionado.getConcepto().setDescripcion(nuevoYduplicarCompletarPersona);
            System.err.println("tipo lista" + tipoLista);
            System.err.println("Secuencia Parentesco " + conceptoSoportesSeleccionado.getConcepto().getSecuencia());
         }

         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCodigo");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarPersona");
      } else if (confirmarCambio.equalsIgnoreCase("CARGO")) {
         System.out.println("DUPLICAR valorConfirmar : " + valorConfirmar);
         System.out.println("DUPLICAR CIUDAD bkp : " + nuevoYduplicarCompletarCargo);

         if (!duplicarConceptosSoportes.getOperando().getNombre().equals("")) {
            System.out.println("DUPLICAR ENTRO DONDE NO TENIA QUE ENTRAR");
            System.out.println("DUPLICAR valorConfirmar: " + valorConfirmar);
            System.out.println("DUPLICAR nuevoTipoCCAutoCompletar: " + nuevoYduplicarCompletarCargo);
            duplicarConceptosSoportes.getOperando().setNombre(nuevoYduplicarCompletarCargo);
            for (int i = 0; i < lovOperandos.size(); i++) {
               if (lovOperandos.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
                  indiceUnicoElemento = i;
                  coincidencias++;
               }
            }
            System.out.println("Coincidencias: " + coincidencias);
            if (coincidencias == 1) {
               duplicarConceptosSoportes.setOperando(lovOperandos.get(indiceUnicoElemento));
            } else {
               lovOperandos = null;
               getLovOperandos();
               FacesContext c = FacesContext.getCurrentInstance();
               DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOperandos");
               tabla.setValue(lovOperandos);
               filtradoLovConceptos = null;
               conceptoLovSeleccionado = null;
               RequestContext.getCurrentInstance().update("form:operandosDialogo");
               RequestContext.getCurrentInstance().update("form:lovOperandos");
               RequestContext.getCurrentInstance().execute("PF('operandosDialogo').show()");
               tipoActualizacion = tipoNuevo;
            }
         } else if (tipoNuevo == 2) {
            duplicarConceptosSoportes.setOperando(new Operandos());
            duplicarConceptosSoportes.getOperando().setNombre(" ");

            System.out.println("DUPLICAR CARGO  : " + duplicarConceptosSoportes.getOperando().getNombre());
            System.out.println("nuevoYduplicarCompletarCARGO : " + nuevoYduplicarCompletarCargo);
            conceptoSoportesSeleccionado.getOperando().setNombre(nuevoYduplicarCompletarCargo);
            System.err.println("tipo lista" + tipoLista);
            System.err.println("Secuencia Parentesco " + conceptoSoportesSeleccionado.getOperando().getSecuencia());
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
      }
   }

   public void revisarDialogoGuardar() {
      if (!conceptosSoportesBorrar.isEmpty() || !conceptosSoportesCrear.isEmpty() || !conceptosSoportesModificar.isEmpty()) {
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarConceptosSoportes() {
      if (guardado == false) {
         if (!conceptosSoportesBorrar.isEmpty()) {
            administrarConceptosSoportes.borrarConceptosSoportes(conceptosSoportesBorrar);
            conceptosSoportesBorrar.clear();
         }
         if (!conceptosSoportesModificar.isEmpty()) {
            administrarConceptosSoportes.modificarConceptosSoportes(conceptosSoportesModificar);
            conceptosSoportesModificar.clear();
         }
         if (!conceptosSoportesCrear.isEmpty()) {
            administrarConceptosSoportes.crearConceptosSoportes(conceptosSoportesCrear);
            conceptosSoportesCrear.clear();
         }
         listConceptosSoportes = null;
         k = 0;
         guardado = true;
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void guardarConceptosSoportesCambios() {
      if (guardado == false) {
         System.out.println("Realizando guardarConceptosSoportes");
         if (!conceptosSoportesBorrar.isEmpty()) {
            administrarConceptosSoportes.borrarConceptosSoportes(conceptosSoportesBorrar);
            conceptosSoportesBorrar.clear();
         }
         if (!conceptosSoportesModificar.isEmpty()) {
            administrarConceptosSoportes.modificarConceptosSoportes(conceptosSoportesModificar);
            conceptosSoportesModificar.clear();
         }
         if (!conceptosSoportesCrear.isEmpty()) {
            administrarConceptosSoportes.crearConceptosSoportes(conceptosSoportesCrear);
            conceptosSoportesCrear.clear();
         }
         System.out.println("Se guardaron los datos con exito");
         listConceptosSoportes = null;
         k = 0;
         guardado = true;
         //seleccionConceptoSoporte();
         RequestContext.getCurrentInstance().update("form:lovConceptosSoporte");
         RequestContext.getCurrentInstance().update("form:conceptosSoporteDialogo");
         RequestContext.getCurrentInstance().execute("PF('conceptosSoporteDialogo').show()");
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void editarCelda() {
      if (conceptoSoportesSeleccionado != null) {
         editarConceptosSoportes = conceptoSoportesSeleccionado;
         System.out.println("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
            RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editConceptos");
            RequestContext.getCurrentInstance().execute("PF('editConceptos').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editOperandos");
            RequestContext.getCurrentInstance().execute("PF('editOperandos').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void restaurarTabla() {
      //CERRAR FILTRADO
      FacesContext c = FacesContext.getCurrentInstance();
      tamano = 320;
      codigo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:codigo");
      codigo.setFilterStyle("display: none; visibility: hidden;");
      personafir = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:personafir");
      personafir.setFilterStyle("display: none; visibility: hidden;");
      cargo = (Column) c.getViewRoot().findComponent("form:datosConceptosSoportes:cargo");
      cargo.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
      bandera = 0;
      filtrarConceptosSoportes = null;
      tipoLista = 0;
   }

   public void agregarNuevoConceptosSoportes() {
      System.out.println("agregarNuevoConceptosSoportes");
      int contador = 0;
      int duplicados = 0;

      Integer a = 0;
      a = null;
      mensajeValidacion = " ";

      if (nuevoConceptosSoportes.getConcepto().getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Debe Tener un Concepto \n";
      } else if (nuevoConceptosSoportes.getConcepto().getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Debe Tener un Concepto \n";
      } else {
         contador++;//3
      }
      if (nuevoConceptosSoportes.getOperando().getNombre() == null) {
         mensajeValidacion = mensajeValidacion + " *Debe Tener una Operando \n";
      } else if (nuevoConceptosSoportes.getOperando().getNombre().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Debe Tener una Operando \n";
      } else {
         contador++;//4
      }
      BigInteger contarConceptosOperandos = administrarConceptosSoportes.contarConceptosOperandos(nuevoConceptosSoportes.getConcepto().getSecuencia(), nuevoConceptosSoportes.getOperando().getSecuencia());
      if (contador == 2 && contarConceptosOperandos.equals(new BigInteger("0"))) {
         if (bandera == 1) {
            restaurarTabla();
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoConceptosSoportes.setSecuencia(l);
         nuevoConceptosSoportes.setTipo("UNIDAD");
         conceptosSoportesCrear.add(nuevoConceptosSoportes);
         listConceptosSoportes.add(nuevoConceptosSoportes);
         conceptoSoportesSeleccionado = listConceptosSoportes.get(listConceptosSoportes.indexOf(nuevoConceptosSoportes));
         RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
         contarRegistros();

         nuevoConceptosSoportes = new ConceptosSoportes();
         nuevoConceptosSoportes.setOperando(new Operandos());
         nuevoConceptosSoportes.setConcepto(new Conceptos());
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroConceptosSoportes').hide()");
         cambioConceptosSoportes = true;
      } else {
         if (contarConceptosOperandos.intValue() > 0) {
            mensajeValidacion = "El OPERANDO y el CONCEPTO elegidos ya fueron insertados";
         }
         RequestContext.getCurrentInstance().update("form:validacionNuevaCentroCosto");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCentroCosto').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoConceptosSoportes() {
      System.out.println("limpiarNuevoConceptosSoportes");
      nuevoConceptosSoportes = new ConceptosSoportes();
      nuevoConceptosSoportes.setConcepto(new Conceptos());
      nuevoConceptosSoportes.setOperando(new Operandos());
      banderaConceptoEscogido = true;
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevoCargo");
      RequestContext.getCurrentInstance().update("formularioDialogos:btnnuevoCargo");
      RequestContext.getCurrentInstance().update("formularioDialogos:nuevaTipoempresa");
   }

   //------------------------------------------------------------------------------
   public void cargarNuevoConceptosSoportes() {
      System.out.println("cargarNuevoConceptosSoportes");
      duplicarConceptosSoportes = new ConceptosSoportes();
      duplicarConceptosSoportes.setConcepto(new Conceptos());
      duplicarConceptosSoportes.setOperando(new Operandos());
      RequestContext.getCurrentInstance().execute("PF('nuevoRegistroConceptosSoportes').show()");
   }

   public void duplicandoConceptosSoportes() {
      System.out.println("duplicandoConceptosSoportes");
      if (conceptoSoportesSeleccionado != null) {
         duplicarConceptosSoportes = new ConceptosSoportes();
         duplicarConceptosSoportes.setConcepto(new Conceptos());
         duplicarConceptosSoportes.setOperando(new Operandos());
         k++;
         l = BigInteger.valueOf(k);

         duplicarConceptosSoportes.setSecuencia(l);
         duplicarConceptosSoportes.setConcepto(conceptoSoportesSeleccionado.getConcepto());
         duplicarConceptosSoportes.setOperando(conceptoSoportesSeleccionado.getOperando());
         duplicarConceptosSoportes.setTipo(conceptoSoportesSeleccionado.getTipo());
         banderaConceptoEscogido = false;
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroConceptosSoportes').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      mensajeValidacion = " ";
      int duplicados = 0;
      Integer a = 0;
      a = null;
      if (duplicarConceptosSoportes.getConcepto().getDescripcion() == null) {
         mensajeValidacion = mensajeValidacion + " *Debe Tener un Concepto \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else if (duplicarConceptosSoportes.getConcepto().getDescripcion().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Debe Tener un Concepto \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("bandera");
         contador++;//3
      }

      if (duplicarConceptosSoportes.getOperando().getNombre() == null) {
         mensajeValidacion = mensajeValidacion + " *Debe Tener una Operando \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);

      } else if (duplicarConceptosSoportes.getOperando().getNombre().isEmpty()) {
         mensajeValidacion = mensajeValidacion + " *Debe Tener una Operando \n";
         System.out.println("Mensaje validacion : " + mensajeValidacion);
      } else {
         System.out.println("bandera");
         contador++;//4
      }

      BigInteger contarConceptosOperandos = administrarConceptosSoportes.contarConceptosOperandos(duplicarConceptosSoportes.getConcepto().getSecuencia(), duplicarConceptosSoportes.getOperando().getSecuencia());
      if (contador == 2 && contarConceptosOperandos.equals(new BigInteger("0"))) {

         if (conceptosSoportesCrear.contains(duplicarConceptosSoportes)) {
            System.out.println("Ya lo contengo.");
         }
         listConceptosSoportes.add(duplicarConceptosSoportes);
         conceptosSoportesCrear.add(duplicarConceptosSoportes);
         conceptoSoportesSeleccionado = listConceptosSoportes.get(listConceptosSoportes.indexOf(duplicarConceptosSoportes));
         RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");

         if (bandera == 1) {
            restaurarTabla();
         }
         duplicarConceptosSoportes = new ConceptosSoportes();
         duplicarConceptosSoportes.setOperando(new Operandos());
         duplicarConceptosSoportes.setConcepto(new Conceptos());

         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroConceptosSoportes').hide()");
      } else {
         contador = 0;
         if (contarConceptosOperandos.intValue() > 0) {
            mensajeValidacion = "El OPERANDO y el CONCEPTO elegidos ya fueron insertados";
         }
         RequestContext.getCurrentInstance().update("form:validacionDuplicarVigencia");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarVigencia').show()");
      }
   }

   public void limpiarDuplicarConceptosSoportes() {
      duplicarConceptosSoportes = new ConceptosSoportes();
      duplicarConceptosSoportes.setConcepto(new Conceptos());
      duplicarConceptosSoportes.setOperando(new Operandos());
      banderaConceptoEscogido = true;
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCargo");
      RequestContext.getCurrentInstance().update("formularioDialogos:btnduplicarCargo");
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTE");
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosSoportesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CONCEPTOSSOPORTES", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosConceptosSoportesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CONCEPTOSSOPORTES", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      System.out.println("lol");
      if (conceptoSoportesSeleccionado != null) {
         System.out.println("lol 2");
         int resultado = administrarRastros.obtenerTabla(conceptoSoportesSeleccionado.getSecuencia(), "CONCEPTOSSOPORTES"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
//         } else {
//            RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
//         }
      } else if (administrarRastros.verificarHistoricosTabla("CONCEPTOSSOPORTES")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void llamadoDialogoBuscarConceptos() {
      try {
         if (guardado == false) {
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
         } else {
            lovConceptosSoportes = null;
            getLovConceptosSoportes();
            RequestContext.getCurrentInstance().update("form:lovConceptosSoporte");
            RequestContext.getCurrentInstance().update("form:conceptosSoporteDialogo");
            RequestContext.getCurrentInstance().execute("PF('conceptosSoporteDialogo').show()");
         }
      } catch (Exception e) {
         System.err.println("ERROR LLAMADO DIALOGO BUSCAR CENTROS COSTOS " + e);
      }
   }

   public void seleccionConceptoSoporte() {
      try {
         RequestContext context = RequestContext.getCurrentInstance();
         if (guardado == true) {
            listConceptosSoportes.clear();
            listConceptosSoportes.add(conceptoSoporteLovSeleccionado);
            conceptoSoporteLovSeleccionado = null;
            filterLovConceptosSoportes = null;
            aceptar = true;
            RequestContext.getCurrentInstance().update("form:datosConceptosSoportes");
            RequestContext.getCurrentInstance().execute("PF('conceptosSoporteDialogo').hide()");
            context.reset("form:lovConceptosSoporte:globalFilter");
            mostrarTodos = false;
            buscarConceptoSoporte = true;
            RequestContext.getCurrentInstance().update("form:MOSTRARTODOS");
            RequestContext.getCurrentInstance().update("form:BUSCARCENTROCOSTO");
            contarRegistros();
         } else {
            RequestContext.getCurrentInstance().update("form:confirmarGuardarConceptos");
            RequestContext.getCurrentInstance().execute("PF('confirmarGuardarConceptos').show()");
         }
      } catch (Exception e) {
         System.out.println("ERROR CONTROLCONCEPTOSSOPORTES.seleccionaVigencia ERROR = " + e.getMessage());
      }
   }

   public void cancelarSeleccionConceptoSoporte() {
      try {
         conceptoSoporteLovSeleccionado = null;
         filterLovConceptosSoportes = null;
         aceptar = true;
         tipoActualizacion = -1;
         RequestContext.getCurrentInstance().update("formularioDialogos:aceptarNCC");
      } catch (Exception e) {
         System.out.println("ERROR CONTROLBETACENTROSCOSTOS.cancelarSeleccionVigencia ERROR====" + e.getMessage());
      }
   }

   public String volverPaginaAnterior() {
      return paginaAnterior;
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosLovCS() {
      RequestContext.getCurrentInstance().update("form:infoRecursoConceptoLista");
   }

   public void contarRegistrosLovConceptos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroConceptos");
   }

   public void contarRegistrosLovOperandos() {
      RequestContext.getCurrentInstance().update("form:infoRegistroOperandos");
   }

   public List<ConceptosSoportes> getLovConceptosSoportes() {
      if (lovConceptosSoportes == null) {
         lovConceptosSoportes = administrarConceptosSoportes.consultarConceptosSoportes();
      }
      return lovConceptosSoportes;
   }

   public void setLovConceptosSoportes(List<ConceptosSoportes> lovConceptosSoportes) {
      this.lovConceptosSoportes = lovConceptosSoportes;
   }

   public List<ConceptosSoportes> getFilterLovConceptosSoportes() {
      return filterLovConceptosSoportes;
   }

   public void setFilterLovConceptosSoportes(List<ConceptosSoportes> filterLovConceptosSoportes) {
      this.filterLovConceptosSoportes = filterLovConceptosSoportes;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public ConceptosSoportes getConceptoSoporteLovSeleccionado() {
      return conceptoSoporteLovSeleccionado;
   }

   public void setConceptoSoporteLovSeleccionado(ConceptosSoportes conceptoSoporteLovSeleccionado) {
      this.conceptoSoporteLovSeleccionado = conceptoSoporteLovSeleccionado;
   }

   //*/*/*/*/*/*/*/*/*/*-/-*//-*/-*/*/*-*/-*/-*/*/*/*/*/---/*/*/*/*/-*/-*/-*/-*/-*/
   public List<ConceptosSoportes> getListConceptosSoportes() {
      if (listConceptosSoportes == null) {
         listConceptosSoportes = administrarConceptosSoportes.consultarConceptosSoportes();
      }
      return listConceptosSoportes;
   }

   public void setListConceptosSoportes(List<ConceptosSoportes> listConceptosSoportes) {
      this.listConceptosSoportes = listConceptosSoportes;
   }

   public List<ConceptosSoportes> getFiltrarConceptosSoportes() {
      return filtrarConceptosSoportes;
   }

   public void setFiltrarConceptosSoportes(List<ConceptosSoportes> filtrarConceptosSoportes) {
      this.filtrarConceptosSoportes = filtrarConceptosSoportes;
   }

   public ConceptosSoportes getNuevoConceptosSoportes() {
      return nuevoConceptosSoportes;
   }

   public void setNuevoConceptosSoportes(ConceptosSoportes nuevoConceptosSoportes) {
      this.nuevoConceptosSoportes = nuevoConceptosSoportes;
   }

   public ConceptosSoportes getDuplicarConceptosSoportes() {
      return duplicarConceptosSoportes;
   }

   public void setDuplicarConceptosSoportes(ConceptosSoportes duplicarConceptosSoportes) {
      this.duplicarConceptosSoportes = duplicarConceptosSoportes;
   }

   public ConceptosSoportes getEditarConceptosSoportes() {
      return editarConceptosSoportes;
   }

   public void setEditarConceptosSoportes(ConceptosSoportes editarConceptosSoportes) {
      this.editarConceptosSoportes = editarConceptosSoportes;
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

   public List<Conceptos> getLovConceptos() {
      if (lovConceptos == null) {
         lovConceptos = administrarConceptosSoportes.consultarLOVConceptos();
      }
      return lovConceptos;
   }

   public void setLovConceptos(List<Conceptos> lovConceptos) {
      this.lovConceptos = lovConceptos;
   }

   public List<Conceptos> getFiltradoLovConceptos() {
      return filtradoLovConceptos;
   }

   public void setFiltradoLovConceptos(List<Conceptos> filtradoLovConceptos) {
      this.filtradoLovConceptos = filtradoLovConceptos;
   }

   public Conceptos getConceptoLovSeleccionado() {
      return conceptoLovSeleccionado;
   }

   public void setConceptoLovSeleccionado(Conceptos conceptoLovSeleccionado) {
      this.conceptoLovSeleccionado = conceptoLovSeleccionado;
   }

   public List<Operandos> getLovOperandos() {
      if (tipoActualizacion == 0) {
         if (lovOperandos == null && conceptoSoportesSeleccionado != null) {
            lovOperandos = administrarConceptosSoportes.consultarLOVOperandosPorConcepto(conceptoSoportesSeleccionado.getConcepto().getSecuencia());
         }
      } else if (tipoActualizacion == 1) {
         if (lovOperandos == null && nuevoConceptosSoportes != null) {
            lovOperandos = administrarConceptosSoportes.consultarLOVOperandosPorConcepto(nuevoConceptosSoportes.getConcepto().getSecuencia());
         }
      } else if (tipoActualizacion == 2) {
         if (lovOperandos == null && duplicarConceptosSoportes != null) {
            lovOperandos = administrarConceptosSoportes.consultarLOVOperandosPorConcepto(duplicarConceptosSoportes.getConcepto().getSecuencia());
         }
      }
      return lovOperandos;
   }

   public void setLovOperandos(List<Operandos> lovOperandos) {
      this.lovOperandos = lovOperandos;
   }

   public List<Operandos> getFiltradoLovOperandos() {
      return filtradoLovOperandos;
   }

   public void setFiltradoLovOperandos(List<Operandos> filtradoLovOperandos) {
      this.filtradoLovOperandos = filtradoLovOperandos;
   }

   public Operandos getOperandoLovSeleccionado() {
      return operandoLovSeleccionado;
   }

   public void setOperandoLovSeleccionado(Operandos operandoLovSeleccionado) {
      this.operandoLovSeleccionado = operandoLovSeleccionado;
   }

   public boolean isBanderaConceptoEscogido() {
      return banderaConceptoEscogido;
   }

   public void setBanderaConceptoEscogido(boolean banderaConceptoEscogido) {
      this.banderaConceptoEscogido = banderaConceptoEscogido;
   }

   public ConceptosSoportes getConceptoSoportesSeleccionado() {
      return conceptoSoportesSeleccionado;
   }

   public void setConceptoSoportesSeleccionado(ConceptosSoportes conceptoSoportesSeleccionado) {
      this.conceptoSoportesSeleccionado = conceptoSoportesSeleccionado;
   }

   public boolean isBuscarConceptoSoporte() {
      return buscarConceptoSoporte;
   }

   public void setBuscarConceptoSoporte(boolean buscarConceptoSoporte) {
      this.buscarConceptoSoporte = buscarConceptoSoporte;
   }

   public boolean isMostrarTodos() {
      return mostrarTodos;
   }

   public void setMostrarTodos(boolean mostrarTodos) {
      this.mostrarTodos = mostrarTodos;
   }

   public String getPaginaAnterior() {
      return paginaAnterior;
   }

   public void setPaginaAnterior(String paginaAnterior) {
      this.paginaAnterior = paginaAnterior;
   }

   public String getInfoRegistroLovCS() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovConceptosSoporte");
      infoRegistroLovCS = String.valueOf(tabla.getRowCount());
      return infoRegistroLovCS;
   }

   public void setInfoRegistroLovCS(String infoRegistroLovCS) {
      this.infoRegistroLovCS = infoRegistroLovCS;
   }

   public String getInfoRegistroLovConceptos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovConceptos");
      infoRegistroLovConceptos = String.valueOf(tabla.getRowCount());
      return infoRegistroLovConceptos;
   }

   public void setInfoRegistroLovConceptos(String infoRegistroLovConceptos) {
      this.infoRegistroLovConceptos = infoRegistroLovConceptos;
   }

   public String getInfoRegistroLovOperandos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovOperandos");
      infoRegistroLovOperandos = String.valueOf(tabla.getRowCount());
      return infoRegistroLovOperandos;
   }

   public void setInfoRegistroLovOperandos(String infoRegistroLovOperandos) {
      this.infoRegistroLovOperandos = infoRegistroLovOperandos;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosConceptosSoportes");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

}
