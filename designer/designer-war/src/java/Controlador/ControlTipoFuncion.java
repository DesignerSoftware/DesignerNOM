/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Operandos;
import Entidades.TiposFunciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarTiposFuncionesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
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
public class ControlTipoFuncion implements Serializable {

   private static Logger log = Logger.getLogger(ControlTipoFuncion.class);

   @EJB
   AdministrarTiposFuncionesInterface administrarTiposFunciones;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   //Parametros que llegan
   private Operandos operando;
   //LISTA INFOREPORTES
   private List<TiposFunciones> listaTiposFunciones;
   private List<TiposFunciones> filtradosListaTiposFunciones;
   private TiposFunciones tipoFuncionSeleccionada;
   //L.O.V INFOREPORTES
   private List<TiposFunciones> lovTiposFunciones;
   private List<TiposFunciones> filtrarLovFunciones;
   private TiposFunciones tipoFuncionLovSeleccionada;
   //editar celda
   private TiposFunciones editarTiposFunciones;
   private boolean aceptarEditar;
   private int cualCelda, tipoLista;
   //OTROS
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   //RASTROS
   private boolean guardado;
   //Crear Novedades
   private List<TiposFunciones> listaTiposFuncionesCrear;
   public TiposFunciones nuevoTipoFuncion;
   public TiposFunciones duplicarTipoFuncion;
   private int k;
   private BigInteger l;
   private String mensajeValidacion;
   //Modificar Novedades
   private List<TiposFunciones> listaTiposFuncionesModificar;
   //Borrar Novedades
   private List<TiposFunciones> listaTiposFuncionesBorrar;
   //AUTOCOMPLETAR
   private String Modulo;
   //Columnas Tabla Ciudades
   private Column tiposFuncionesIniciales, tiposFuncionesFinales, tiposFuncionesObjetos;
   //ALTO SCROLL TABLA
   private String altoTabla;
   private boolean cambiosPagina;
   private String paginaAnterior = "nominaf";
   public String infoRegistro;
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlTipoFuncion() {
      cambiosPagina = true;
      nuevoTipoFuncion = new TiposFunciones();
      nuevoTipoFuncion.setFechainicial(new Date());
      aceptar = true;
      tipoFuncionSeleccionada = null;
      guardado = true;
      tipoLista = 0;
      listaTiposFuncionesBorrar = new ArrayList<TiposFunciones>();
      listaTiposFuncionesCrear = new ArrayList<TiposFunciones>();
      listaTiposFuncionesModificar = new ArrayList<TiposFunciones>();
      altoTabla = "280";
      duplicarTipoFuncion = new TiposFunciones();
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void limpiarListasValor() {
      lovTiposFunciones = null;
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarTiposFunciones.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ": " + e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      //inicializarCosas(); Inicializar cosas de ser necesario
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      operando = (Operandos) mapParametros.get("operandoActual");
      listaTiposFunciones = null;
      getListaTiposFunciones();
   }

   //Reemplazar la funcion volverAtras, retornarPagina, Redirigir.....Atras.etc
   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      String pagActual = "tipofuncion";
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

   //UBICACION CELDA
   public void cambiarIndice(TiposFunciones tipoF, int celda) {
      tipoFuncionSeleccionada = tipoF;
      cualCelda = celda;
   }

   //AUTOCOMPLETAR
   public void modificarTiposFunciones(TiposFunciones tipoF, String confirmarCambio, String valorConfirmar) {
      tipoFuncionSeleccionada = tipoF;

      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (!listaTiposFuncionesCrear.contains(tipoFuncionSeleccionada)) {

            if (listaTiposFuncionesModificar.isEmpty()) {
               listaTiposFuncionesModificar.add(tipoFuncionSeleccionada);
            } else if (!listaTiposFuncionesModificar.contains(tipoFuncionSeleccionada)) {
               listaTiposFuncionesModificar.add(tipoFuncionSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");

            }
         }
         RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
         contarRegistros();
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      tipoFuncionSeleccionada = null;
      contarRegistros();
   }

   public void confirmarDuplicar() {
      int pasa = 0;
      mensajeValidacion = new String();
      if (duplicarTipoFuncion.getFechainicial() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoFuncion");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoFuncion').show()");
      }
      if (pasa == 0) {
         if (bandera == 1) {
            restaurarTabla();
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         //Falta Ponerle el Operando al cual se agregará
         duplicarTipoFuncion.setOperando(operando);
         listaTiposFunciones.add(duplicarTipoFuncion);
         listaTiposFuncionesCrear.add(duplicarTipoFuncion);
         tipoFuncionSeleccionada = listaTiposFunciones.get(listaTiposFunciones.indexOf(duplicarTipoFuncion));
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
         contarRegistros();
         duplicarTipoFuncion = new TiposFunciones();
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarTipoFuncion");
         RequestContext.getCurrentInstance().execute("PF('DuplicarTipoFuncion').hide()");
      }
   }

   public void restaurarTabla() {
      FacesContext c = FacesContext.getCurrentInstance();
      altoTabla = "280";
      tiposFuncionesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesIniciales");
      tiposFuncionesIniciales.setFilterStyle("display: none; visibility: hidden;");
      tiposFuncionesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesFinales");
      tiposFuncionesFinales.setFilterStyle("display: none; visibility: hidden;");
      tiposFuncionesObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesObjetos");
      tiposFuncionesObjetos.setFilterStyle("display: none; visibility: hidden;");
      RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
      contarRegistros();
      bandera = 0;
      filtradosListaTiposFunciones = null;
      tipoLista = 0;
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();

      if (bandera == 0) {
         altoTabla = "260";
         tiposFuncionesIniciales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesIniciales");
         tiposFuncionesIniciales.setFilterStyle("width: 85% !important;");
         tiposFuncionesFinales = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesFinales");
         tiposFuncionesFinales.setFilterStyle("width: 85% !important;");
         tiposFuncionesObjetos = (Column) c.getViewRoot().findComponent("form:datosTiposFunciones:tiposFuncionesObjetos");
         tiposFuncionesObjetos.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
         contarRegistros();
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         restaurarTabla();
      }
   }

   public void cancelarYSalir() {
      cancelarModificacion();
      salir();
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         restaurarTabla();
      }
      listaTiposFuncionesBorrar.clear();
      listaTiposFuncionesCrear.clear();
      listaTiposFuncionesModificar.clear();
      tipoFuncionSeleccionada = null;
      k = 0;
      listaTiposFunciones = null;
      guardado = true;
      cambiosPagina = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
      contarRegistros();
   }

   //MOSTRAR DATOS CELDA
   public void editarCelda() {
      if (tipoFuncionSeleccionada != null) {
         editarTiposFunciones = tipoFuncionSeleccionada;

         RequestContext context = RequestContext.getCurrentInstance();
         log.info("Entro a editar... valor celda: " + cualCelda);
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasIniciales");
            RequestContext.getCurrentInstance().execute("PF('editarFechasIniciales').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFechasFinales");
            RequestContext.getCurrentInstance().execute("PF('editarFechasFinales').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarObjetos");
            RequestContext.getCurrentInstance().execute("PF('editarObjetos').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   //EXPORTAR
   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposFuncionesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "TiposFuncionesPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosTiposFuncionesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "TiposFuncionesXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   //LIMPIAR NUEVO REGISTRO CIUDAD
   public void limpiarNuevoTiposFunciones() {
      nuevoTipoFuncion = new TiposFunciones();
   }

   public void limpiarduplicarTiposFunciones() {
      duplicarTipoFuncion = new TiposFunciones();
   }

   //DUPLICAR Operando
   public void duplicarTF() {
      if (tipoFuncionSeleccionada != null) {
         duplicarTipoFuncion = new TiposFunciones();
         k++;
         l = BigInteger.valueOf(k);

         duplicarTipoFuncion.setSecuencia(l);
         duplicarTipoFuncion.setNombreobjeto(tipoFuncionSeleccionada.getNombreobjeto());
         duplicarTipoFuncion.setFechainicial(tipoFuncionSeleccionada.getFechainicial());
         duplicarTipoFuncion.setFechafinal(tipoFuncionSeleccionada.getFechafinal());
         duplicarTipoFuncion.setOperando(tipoFuncionSeleccionada.getOperando());

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarTipoFuncion");
         RequestContext.getCurrentInstance().execute("PF('DuplicarTipoFuncion').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void guardarYSalir() {
      guardarCambiosTiposFunciones();
      salir();
   }

   //GUARDAR
   public void guardarCambiosTiposFunciones() {
      if (guardado == false) {
         if (!listaTiposFuncionesBorrar.isEmpty()) {
            for (int i = 0; i < listaTiposFuncionesBorrar.size(); i++) {
               log.info("Borrando..." + listaTiposFuncionesBorrar.size());
               if (listaTiposFuncionesBorrar.get(i).getFechafinal() == null) {
                  listaTiposFuncionesBorrar.get(i).setFechafinal(null);
               }
               if (listaTiposFuncionesBorrar.get(i).getNombreobjeto() == null) {
                  listaTiposFuncionesBorrar.get(i).setNombreobjeto(null);
               }
               administrarTiposFunciones.borrarTiposFunciones(listaTiposFuncionesBorrar.get(i));
            }
            listaTiposFuncionesBorrar.clear();
         }
         if (!listaTiposFuncionesCrear.isEmpty()) {
            for (int i = 0; i < listaTiposFuncionesCrear.size(); i++) {
               if (listaTiposFuncionesCrear.get(i).getFechafinal() == null) {
                  listaTiposFuncionesCrear.get(i).setFechafinal(null);
               }
               if (listaTiposFuncionesCrear.get(i).getNombreobjeto() == null) {
                  listaTiposFuncionesCrear.get(i).setNombreobjeto(null);
               }
               administrarTiposFunciones.crearTiposFunciones(listaTiposFuncionesCrear.get(i));
            }
            listaTiposFuncionesCrear.clear();
         }
         if (!listaTiposFuncionesModificar.isEmpty()) {
            administrarTiposFunciones.modificarTiposFunciones(listaTiposFuncionesModificar);
            listaTiposFuncionesModificar.clear();
         }

         log.info("Se guardaron los datos con exito");
         listaTiposFunciones = null;
         tipoFuncionSeleccionada = null;

         RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
         contarRegistros();
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
         //  k = 0;
      }
   }

//RASTROS 
   public void verificarRastro() {
      if (tipoFuncionSeleccionada != null) {
         int result = administrarRastros.obtenerTabla(tipoFuncionSeleccionada.getSecuencia(), "TIPOSFUNCIONES");
         log.info("resultado: " + result);
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
      } else if (administrarRastros.verificarHistoricosTabla("TIPOSFUNCIONES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void agregarNuevoTipoFuncion() {
      int pasa = 0;
      int pasa2 = 0;
      mensajeValidacion = new String();
      if (nuevoTipoFuncion.getFechainicial() == null) {
         mensajeValidacion = mensajeValidacion + " * Fecha Inicial\n";
         pasa++;
      }
      if (nuevoTipoFuncion.getFechafinal().before(nuevoTipoFuncion.getFechainicial())) {
         RequestContext.getCurrentInstance().update("formularioDialogos:errorFechas");
         RequestContext.getCurrentInstance().execute("PF('errorFechas').show()");
         pasa2++;
      }
      if (pasa != 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoTipoFuncion");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoTipoFuncion').show()");
      }
      if (pasa == 0 && pasa2 == 0) {
         if (bandera == 1) {
            restaurarTabla();
         }
         //AGREGAR REGISTRO A LA LISTA NOVEDADES .
         k++;
         l = BigInteger.valueOf(k);
         nuevoTipoFuncion.setSecuencia(l);
         log.info("Operando: " + operando);
         nuevoTipoFuncion.setOperando(operando);

         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         //Falta Agregar el operando al cual se va a adicionar
         listaTiposFuncionesCrear.add(nuevoTipoFuncion);
         listaTiposFunciones.add(nuevoTipoFuncion);
         tipoFuncionSeleccionada = listaTiposFunciones.get(listaTiposFunciones.indexOf(nuevoTipoFuncion));
         nuevoTipoFuncion = new TiposFunciones();
         RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoTipoFuncion').hide()");
      }
   }

   //BORRAR CIUDADES
   public void borrarTipoFuncion() {
      if (tipoFuncionSeleccionada != null) {
         if (!listaTiposFuncionesModificar.isEmpty() && listaTiposFuncionesModificar.contains(tipoFuncionSeleccionada)) {
            listaTiposFuncionesModificar.remove(tipoFuncionSeleccionada);
            listaTiposFuncionesBorrar.add(tipoFuncionSeleccionada);
         } else if (!listaTiposFuncionesCrear.isEmpty() && listaTiposFuncionesCrear.contains(tipoFuncionSeleccionada)) {
            listaTiposFuncionesCrear.remove(tipoFuncionSeleccionada);
         } else {
            listaTiposFuncionesBorrar.add(tipoFuncionSeleccionada);
         }
         listaTiposFunciones.remove(tipoFuncionSeleccionada);
         if (tipoLista == 1) {
            filtradosListaTiposFunciones.remove(tipoFuncionSeleccionada);
         }
         RequestContext.getCurrentInstance().update("form:datosTiposFunciones");
         contarRegistros();
         tipoFuncionSeleccionada = null;
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
      listaTiposFuncionesBorrar.clear();
      listaTiposFuncionesCrear.clear();
      listaTiposFuncionesModificar.clear();
      tipoFuncionSeleccionada = null;
      k = 0;
      listaTiposFunciones = null;
      guardado = true;
      navegar("atras");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   //Getter & Setter
   public List<TiposFunciones> getListaTiposFunciones() {
      if (listaTiposFunciones == null && operando != null) {
         listaTiposFunciones = administrarTiposFunciones.buscarTiposFunciones(operando.getSecuencia());
      }
      return listaTiposFunciones;
   }

   public void setListaTiposFunciones(List<TiposFunciones> listaTiposFunciones) {
      this.listaTiposFunciones = listaTiposFunciones;
   }

   public List<TiposFunciones> getFiltradosListaTiposFunciones() {
      return filtradosListaTiposFunciones;
   }

   public void setFiltradosListaTiposFunciones(List<TiposFunciones> filtradosListaTiposFunciones) {
      this.filtradosListaTiposFunciones = filtradosListaTiposFunciones;
   }

   public TiposFunciones getEditarTiposFunciones() {
      return editarTiposFunciones;
   }

   public void setEditarTiposFunciones(TiposFunciones editarTiposFunciones) {
      this.editarTiposFunciones = editarTiposFunciones;
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

   public TiposFunciones getNuevoTipoFuncion() {
      return nuevoTipoFuncion;
   }

   public void setNuevoTipoFuncion(TiposFunciones nuevoTipoFuncion) {
      this.nuevoTipoFuncion = nuevoTipoFuncion;
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

   public TiposFunciones getDuplicarTipoFuncion() {
      return duplicarTipoFuncion;
   }

   public void setDuplicarTipoFuncion(TiposFunciones duplicarTipoFuncion) {
      this.duplicarTipoFuncion = duplicarTipoFuncion;
   }

   public TiposFunciones getTipoFuncionSeleccionada() {
      return tipoFuncionSeleccionada;
   }

   public void setTipoFuncionSeleccionada(TiposFunciones tipoFuncionSeleccionada) {
      this.tipoFuncionSeleccionada = tipoFuncionSeleccionada;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosTiposFunciones");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }
}
