/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.IndicesExternos;
import Entidades.ResultadosIndicesExternos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarResultadosIndicesExternosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
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
@Named(value = "controlResultadosIndicesExternos")
@SessionScoped
public class ControlResultadosIndicesExternos implements Serializable {

   @EJB
   AdministrarResultadosIndicesExternosInterface administrarResultados;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<ResultadosIndicesExternos> listResultados;
   private List<ResultadosIndicesExternos> listResultadosCrear;
   private List<ResultadosIndicesExternos> listResultadosBorrar;
   private List<ResultadosIndicesExternos> listResultadosModificar;
   private List<ResultadosIndicesExternos> listResultadosFiltrar;
   private ResultadosIndicesExternos resultadoSeleccionado;
   private ResultadosIndicesExternos nuevoResultado;
   private ResultadosIndicesExternos duplicarResultado;
   private ResultadosIndicesExternos editarResultado;
   /// lov indices externos///
   private List<IndicesExternos> lovIndicesExternos;
   private List<IndicesExternos> filtrarLovIndicesExternos;
   private IndicesExternos indiceExternoSeleccionado;
   ///
   private int tamano;
   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   private boolean permitirIndex;
   private Column indiceexterno, anio, mes, valor, referencia;
   private int registroBorrados;
   private String mensajeValidacion;
   private String infoRegistro, infoRegistroIndice;
   private DataTable tablaC;
   private boolean activarLov;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlResultadosIndicesExternos() {
      listResultados = null;
      listResultadosCrear = new ArrayList<ResultadosIndicesExternos>();
      listResultadosBorrar = new ArrayList<ResultadosIndicesExternos>();
      listResultadosModificar = new ArrayList<ResultadosIndicesExternos>();
      permitirIndex = true;
      nuevoResultado = new ResultadosIndicesExternos();
      nuevoResultado.setIndiceexterno(new IndicesExternos());
      duplicarResultado = new ResultadosIndicesExternos();
      editarResultado = new ResultadosIndicesExternos();
      guardado = true;
      tamano = 270;
      paginaAnterior = "nominaf";
      activarLov = true;
      lovIndicesExternos = null;
      indiceExternoSeleccionado = null;
      mapParametros.put("paginaAnterior", paginaAnterior);

   }

   public void limpiarListasValor() {

   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarResultados.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      getListResultados();
      if (listResultados != null) {
         if (!listResultados.isEmpty()) {
            resultadoSeleccionado = listResultados.get(0);
         }
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      getListResultados();
      if (listResultados != null) {
         if (!listResultados.isEmpty()) {
            resultadoSeleccionado = listResultados.get(0);
         }
      }
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
String pagActual = "resultadosindicesexternos";
         
         
         


         
         
         
         
         
         
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

   public String retornarPagina() {
      return paginaAnterior;
   }

   public void cambiarIndice(ResultadosIndicesExternos resultados, int celda) {
      if (permitirIndex == true) {
         resultadoSeleccionado = resultados;
         cualCelda = celda;
         if (cualCelda == 0) {
            resultadoSeleccionado.getIndiceexterno().getDescripcion();
            habilitarBotonLov();
         } else if (cualCelda == 1) {
            resultadoSeleccionado.getAno();
            deshabilitarBotonLov();
         } else if (cualCelda == 2) {
            resultadoSeleccionado.getMes();
            deshabilitarBotonLov();
         } else if (cualCelda == 3) {
            resultadoSeleccionado.getValor();
            deshabilitarBotonLov();
         } else if (cualCelda == 4) {
            resultadoSeleccionado.getReferencia();
            deshabilitarBotonLov();
         }
         resultadoSeleccionado.getSecuencia();
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         indiceexterno = (Column) c.getViewRoot().findComponent("form:datosResultados:indiceexterno");
         indiceexterno.setFilterStyle("display: none; visibility: hidden;");
         anio = (Column) c.getViewRoot().findComponent("form:datosResultados:anio");
         anio.setFilterStyle("display: none; visibility: hidden;");
         mes = (Column) c.getViewRoot().findComponent("form:datosResultados:mes");
         mes.setFilterStyle("display: none; visibility: hidden;");
         valor = (Column) c.getViewRoot().findComponent("form:datosResultados:valor");
         valor.setFilterStyle("display: none; visibility: hidden;");
         referencia = (Column) c.getViewRoot().findComponent("form:datosResultados:referencias");
         referencia.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosResultados");
         bandera = 0;
         listResultadosFiltrar = null;
         tipoLista = 0;
         tamano = 270;
      }

      listResultadosBorrar.clear();
      listResultadosCrear.clear();
      listResultadosModificar.clear();
      k = 0;
      listResultados = null;
      resultadoSeleccionado = null;
      guardado = true;
      permitirIndex = true;
      getListResultados();
      contarRegistros();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosResultados");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         indiceexterno = (Column) c.getViewRoot().findComponent("form:datosResultados:indiceexterno");
         indiceexterno.setFilterStyle("display: none; visibility: hidden;");
         anio = (Column) c.getViewRoot().findComponent("form:datosResultados:anio");
         anio.setFilterStyle("display: none; visibility: hidden;");
         mes = (Column) c.getViewRoot().findComponent("form:datosResultados:mes");
         mes.setFilterStyle("display: none; visibility: hidden;");
         valor = (Column) c.getViewRoot().findComponent("form:datosResultados:valor");
         valor.setFilterStyle("display: none; visibility: hidden;");
         referencia = (Column) c.getViewRoot().findComponent("form:datosResultados:referencias");
         referencia.setFilterStyle("display: none; visibility: hidden;");
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosResultados");
         bandera = 0;
         listResultadosFiltrar = null;
         tipoLista = 0;
      }
      listResultadosBorrar.clear();
      listResultadosCrear.clear();
      listResultadosModificar.clear();
      resultadoSeleccionado = null;
      k = 0;
      listResultados = null;
      guardado = true;
      permitirIndex = true;
      contarRegistros();
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:datosResultados");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         indiceexterno = (Column) c.getViewRoot().findComponent("form:datosResultados:indiceexterno");
         indiceexterno.setFilterStyle("width: 85% !important");
         anio = (Column) c.getViewRoot().findComponent("form:datosResultados:anio");
         anio.setFilterStyle("width: 85% !important");
         mes = (Column) c.getViewRoot().findComponent("form:datosResultados:mes");
         mes.setFilterStyle("width: 85% !important");
         valor = (Column) c.getViewRoot().findComponent("form:datosResultados:valor");
         valor.setFilterStyle("width: 85% !important");
         referencia = (Column) c.getViewRoot().findComponent("form:datosResultados:referencias");
         referencia.setFilterStyle("width: 85% !important");
         RequestContext.getCurrentInstance().update("form:datosResultados");
         System.out.println("Activar");
         bandera = 1;
      } else if (bandera == 1) {
         System.out.println("Desactivar");
         tamano = 270;
         indiceexterno = (Column) c.getViewRoot().findComponent("form:datosResultados:indiceexterno");
         indiceexterno.setFilterStyle("display: none; visibility: hidden;");
         anio = (Column) c.getViewRoot().findComponent("form:datosResultados:anio");
         anio.setFilterStyle("display: none; visibility: hidden;");
         mes = (Column) c.getViewRoot().findComponent("form:datosResultados:mes");
         mes.setFilterStyle("display: none; visibility: hidden;");
         valor = (Column) c.getViewRoot().findComponent("form:datosResultados:valor");
         valor.setFilterStyle("display: none; visibility: hidden;");
         referencia = (Column) c.getViewRoot().findComponent("form:datosResultados:referencias");
         referencia.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosResultados");
         bandera = 0;
         listResultadosFiltrar = null;
         tipoLista = 0;
      }
   }

   public void modificarResultado(ResultadosIndicesExternos resultado, String confirmarCambio, String valorConfirmar) {
      resultadoSeleccionado = resultado;
      int contador = 0;
      boolean banderita = false;
      RequestContext context = RequestContext.getCurrentInstance();
      if (confirmarCambio.equalsIgnoreCase("N")) {
         if (tipoLista == 0) {
            if (!listResultadosCrear.contains(resultadoSeleccionado)) {
               if (listResultadosModificar.isEmpty()) {
                  listResultadosModificar.add(resultadoSeleccionado);
               } else if (!listResultadosModificar.contains(resultadoSeleccionado)) {
                  listResultadosModificar.add(resultadoSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
         } else if (!listResultadosCrear.contains(resultadoSeleccionado)) {
            if (!listResultadosModificar.contains(resultadoSeleccionado)) {
               if (listResultadosModificar.isEmpty()) {
                  listResultadosModificar.add(resultadoSeleccionado);
               } else if (!listResultadosModificar.contains(resultadoSeleccionado)) {
                  listResultadosModificar.add(resultadoSeleccionado);
               }
               if (guardado == true) {
                  guardado = false;
                  RequestContext.getCurrentInstance().update("form:ACEPTAR");
               }
            }
            RequestContext.getCurrentInstance().update("form:datosResultados");
         }
      }
   }

   public void modificarResultado(ResultadosIndicesExternos resultado) {
      resultadoSeleccionado = resultado;
      if (tipoLista == 0) {
         if (!listResultadosCrear.contains(resultadoSeleccionado)) {
            if (listResultadosModificar.isEmpty()) {
               listResultadosModificar.add(resultadoSeleccionado);
            } else if (!listResultadosModificar.contains(resultadoSeleccionado)) {
               listResultadosModificar.add(resultadoSeleccionado);
            }
            if (guardado == true) {
               guardado = false;
               RequestContext context = RequestContext.getCurrentInstance();
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         }
      } else if (!listResultadosCrear.contains(resultadoSeleccionado)) {
         if (listResultadosModificar.isEmpty()) {
            listResultadosModificar.add(resultadoSeleccionado);
         } else if (!listResultadosModificar.contains(resultadoSeleccionado)) {
            listResultadosModificar.add(resultadoSeleccionado);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
   }

   public void borrarResultado() {
      if (resultadoSeleccionado != null) {
         if (!listResultadosModificar.isEmpty() && listResultadosModificar.contains(resultadoSeleccionado)) {
            listResultadosModificar.remove(listResultadosModificar.indexOf(resultadoSeleccionado));
            listResultadosBorrar.add(resultadoSeleccionado);
         } else if (!listResultadosCrear.isEmpty() && listResultadosCrear.contains(resultadoSeleccionado)) {
            listResultadosCrear.remove(listResultadosCrear.indexOf(resultadoSeleccionado));
         } else {
            listResultadosBorrar.add(resultadoSeleccionado);
         }
         listResultados.remove(resultadoSeleccionado);
         if (tipoLista == 1) {
            listResultadosFiltrar.remove(resultadoSeleccionado);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosResultados");
         contarRegistros();
         resultadoSeleccionado = null;
         guardado = true;

         if (guardado == true) {
            guardado = false;
         }
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void revisarDialogoGuardar() {
      if (!listResultadosBorrar.isEmpty() || !listResultadosCrear.isEmpty() || !listResultadosModificar.isEmpty()) {
         RequestContext.getCurrentInstance().update("formularioDialogos:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarYSalir() {
      guardarResultado();
      salir();
   }

   public void guardarResultado() {
      try {
         if (guardado == false) {
            if (!listResultadosBorrar.isEmpty()) {
               administrarResultados.borrarResultado(listResultadosBorrar);
               registroBorrados = listResultadosBorrar.size();
               RequestContext.getCurrentInstance().update("form:mostrarBorrados");
               RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
               listResultadosBorrar.clear();
            }
            if (!listResultadosModificar.isEmpty()) {
               administrarResultados.modificarResultado(listResultadosModificar);
               listResultadosModificar.clear();
            }
            if (!listResultadosCrear.isEmpty()) {
               administrarResultados.crearResultado(listResultadosCrear);
               listResultadosCrear.clear();
            }
            listResultados = null;
            getListResultados();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            resultadoSeleccionado = null;
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosResultados");
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void asignarIndex(ResultadosIndicesExternos resultado, int dlg, int LND) {
      resultadoSeleccionado = resultado;
      tipoActualizacion = LND;
      if (dlg == 1) {
         getLovIndicesExternos();
         contarRegistrosIndicesExternos();
         RequestContext.getCurrentInstance().update("formularioDialogos:indicesExternosDialogo");
         RequestContext.getCurrentInstance().execute("PF('indicesExternosDialogo').show()");
      }
   }

   public void listaValorBoton() {
      if (cualCelda == 0) {
         getLovIndicesExternos();
         contarRegistrosIndicesExternos();
         RequestContext.getCurrentInstance().update("formularioDialogos:indicesExternosDialogo");
         RequestContext.getCurrentInstance().execute("PF('indicesExternosDialogo').show()");
      }
   }

   public void editarCelda() {
      if (resultadoSeleccionado != null) {
         editarResultado = resultadoSeleccionado;

         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editIndiceD");
            RequestContext.getCurrentInstance().execute("PF('editIndiceD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editAnioD");
            RequestContext.getCurrentInstance().execute("PF('editAnioD').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editMesD");
            RequestContext.getCurrentInstance().execute("PF('editMesD').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editValorD");
            RequestContext.getCurrentInstance().execute("PF('editValorD').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editReferenciaD");
            RequestContext.getCurrentInstance().execute("PF('editReferenciaD').show()");
            cualCelda = -1;
         }

      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void agregarNuevoResultado() {
      int contador = 0;
      int duplicados = 0;
      RequestContext context = RequestContext.getCurrentInstance();

      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         System.out.println("Desactivar");
         indiceexterno = (Column) c.getViewRoot().findComponent("form:datosResultados:indiceexterno");
         indiceexterno.setFilterStyle("display: none; visibility: hidden;");
         anio = (Column) c.getViewRoot().findComponent("form:datosResultados:anio");
         anio.setFilterStyle("display: none; visibility: hidden;");
         mes = (Column) c.getViewRoot().findComponent("form:datosResultados:mes");
         mes.setFilterStyle("display: none; visibility: hidden;");
         valor = (Column) c.getViewRoot().findComponent("form:datosResultados:valor");
         valor.setFilterStyle("display: none; visibility: hidden;");
         referencia = (Column) c.getViewRoot().findComponent("form:datosResultados:referencias");
         referencia.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listResultadosFiltrar = null;
         tipoLista = 0;
         tamano = 270;
         RequestContext.getCurrentInstance().update("form:datosResultados");
      }
      k++;
      l = BigInteger.valueOf(k);
      nuevoResultado.setSecuencia(l);
      listResultadosCrear.add(nuevoResultado);
      listResultados.add(nuevoResultado);
      contarRegistros();
      resultadoSeleccionado = nuevoResultado;
      nuevoResultado = new ResultadosIndicesExternos();
      RequestContext.getCurrentInstance().update("form:datosResultados");
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().execute("PF('nuevoRegistroResultados').hide()");
   }

   public void limpiarNuevoResultado() {
      nuevoResultado = new ResultadosIndicesExternos();
      nuevoResultado.setIndiceexterno(new IndicesExternos());
   }

   public void duplicarResultados() {
      if (resultadoSeleccionado != null) {
         duplicarResultado = new ResultadosIndicesExternos();
         k++;
         l = BigInteger.valueOf(k);

         if (tipoLista == 0) {
            duplicarResultado.setSecuencia(l);
            duplicarResultado.setIndiceexterno(resultadoSeleccionado.getIndiceexterno());
            duplicarResultado.setAno(resultadoSeleccionado.getAno());
            duplicarResultado.setMes(resultadoSeleccionado.getMes());
            duplicarResultado.setValor(resultadoSeleccionado.getValor());
            duplicarResultado.setReferencia(resultadoSeleccionado.getReferencia());
         }
         if (tipoLista == 1) {
            duplicarResultado.setSecuencia(l);
            duplicarResultado.setIndiceexterno(resultadoSeleccionado.getIndiceexterno());
            duplicarResultado.setAno(resultadoSeleccionado.getAno());
            duplicarResultado.setMes(resultadoSeleccionado.getMes());
            duplicarResultado.setValor(resultadoSeleccionado.getValor());
            duplicarResultado.setReferencia(resultadoSeleccionado.getReferencia());
            tamano = 270;
         }

         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarResultado");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroResultados').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }

   }

   public void confirmarDuplicar() {

      listResultados.add(duplicarResultado);
      listResultadosCrear.add(duplicarResultado);
      resultadoSeleccionado = duplicarResultado;
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:datosResultados");
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }

      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         //CERRAR FILTRADO
         indiceexterno = (Column) c.getViewRoot().findComponent("form:datosResultados:indiceexterno");
         indiceexterno.setFilterStyle("display: none; visibility: hidden;");
         anio = (Column) c.getViewRoot().findComponent("form:datosResultados:anio");
         anio.setFilterStyle("display: none; visibility: hidden;");
         mes = (Column) c.getViewRoot().findComponent("form:datosResultados:mes");
         mes.setFilterStyle("display: none; visibility: hidden;");
         valor = (Column) c.getViewRoot().findComponent("form:datosResultados:valor");
         valor.setFilterStyle("display: none; visibility: hidden;");
         referencia = (Column) c.getViewRoot().findComponent("form:datosResultados:referencias");
         referencia.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listResultadosFiltrar = null;
         RequestContext.getCurrentInstance().update("form:datosResultados");
         tipoLista = 0;
      }
      duplicarResultado = new ResultadosIndicesExternos();
      RequestContext.getCurrentInstance().update("formularioDialogos:duplicarRegistroResultados");
      RequestContext.getCurrentInstance().execute("PF('duplicarRegistroResultados').hide()");
   }

   public void limpiarDuplicar() {
      duplicarResultado = new ResultadosIndicesExternos();
   }

   public void actualizarIndiceExterno() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         if (tipoLista == 0) {
            resultadoSeleccionado.setIndiceexterno(indiceExternoSeleccionado);
            if (!listResultadosCrear.contains(resultadoSeleccionado)) {
               if (listResultadosModificar.isEmpty()) {
                  listResultadosModificar.add(resultadoSeleccionado);
               } else if (!listResultadosModificar.contains(resultadoSeleccionado)) {
                  listResultadosModificar.add(resultadoSeleccionado);
               }
            }
         } else {
            resultadoSeleccionado.setIndiceexterno(indiceExternoSeleccionado);
            if (!listResultadosCrear.contains(resultadoSeleccionado)) {
               if (listResultadosModificar.isEmpty()) {
                  listResultadosModificar.add(resultadoSeleccionado);
               } else if (!listResultadosModificar.contains(resultadoSeleccionado)) {
                  listResultadosModificar.add(resultadoSeleccionado);
               }
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosResultados");
      } else if (tipoActualizacion == 1) {
         nuevoResultado.setIndiceexterno(indiceExternoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevoResultadoIE");
      } else if (tipoActualizacion == 2) {
         duplicarResultado.setIndiceexterno(indiceExternoSeleccionado
         );
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarResultadoIE");
      }
      filtrarLovIndicesExternos = null;
      indiceExternoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("formularioDialogos:indicesExternosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovIndicesExternos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTI");

      context.reset("formularioDialogos:lovIndicesExternos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovIndicesExternos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('indicesExternosDialogo').hide()");

   }

   public void cancelarCambioIndiceExterno() {
      filtrarLovIndicesExternos = null;
      aceptar = true;
      tipoActualizacion = -1;
      permitirIndex = true;
      indiceExternoSeleccionado = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:indicesExternosDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovIndicesExternos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarTI");
      context.reset("formularioDialogos:lovIndicesExternos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovIndicesExternos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('indicesExternosDialogo').hide()");

   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosResultadosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "RESULTADOSINDICESEXTERNOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosResultadosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "RESULTADOSINDICESEXTERNOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      System.out.println("lol");
      if (resultadoSeleccionado != null) {
         System.out.println("lol 2");
         int resultado = administrarRastros.obtenerTabla(resultadoSeleccionado.getSecuencia(), "RESULTADOSINDICESEXTERNOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("RESULTADOSINDICESEXTERNOS")) { // igual acá
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void recordarSeleccion() {
      if (resultadoSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosResultados");
         tablaC.setSelection(resultadoSeleccionado);
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosIndicesExternos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroIndiceExterno");
   }

   public void habilitarBotonLov() {
      activarLov = false;
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
   }

   ///////SETS Y GETS //////////////
   public List<ResultadosIndicesExternos> getListResultados() {
      if (listResultados == null) {
         listResultados = administrarResultados.consultarResultadosIndicesExternos();
      }
      return listResultados;
   }

   public void setListResultados(List<ResultadosIndicesExternos> listResultados) {
      this.listResultados = listResultados;
   }

   public List<ResultadosIndicesExternos> getListResultadosFiltrar() {
      return listResultadosFiltrar;
   }

   public void setListResultadosFiltrar(List<ResultadosIndicesExternos> listResultadosFiltrar) {
      this.listResultadosFiltrar = listResultadosFiltrar;
   }

   public ResultadosIndicesExternos getResultadoSeleccionado() {
      return resultadoSeleccionado;
   }

   public void setResultadoSeleccionado(ResultadosIndicesExternos resultadoSeleccionado) {
      this.resultadoSeleccionado = resultadoSeleccionado;
   }

   public ResultadosIndicesExternos getNuevoResultado() {
      return nuevoResultado;
   }

   public void setNuevoResultado(ResultadosIndicesExternos nuevoResultado) {
      this.nuevoResultado = nuevoResultado;
   }

   public ResultadosIndicesExternos getDuplicarResultado() {
      return duplicarResultado;
   }

   public void setDuplicarResultado(ResultadosIndicesExternos duplicarResultado) {
      this.duplicarResultado = duplicarResultado;
   }

   public ResultadosIndicesExternos getEditarResultado() {
      return editarResultado;
   }

   public void setEditarResultado(ResultadosIndicesExternos editarResultado) {
      this.editarResultado = editarResultado;
   }

   public List<IndicesExternos> getLovIndicesExternos() {
      if (lovIndicesExternos == null) {
         lovIndicesExternos = administrarResultados.consultarIndicesExternos();
      }
      return lovIndicesExternos;
   }

   public void setLovIndicesExternos(List<IndicesExternos> lovIndicesExternos) {
      this.lovIndicesExternos = lovIndicesExternos;
   }

   public List<IndicesExternos> getFiltrarLovIndicesExternos() {
      return filtrarLovIndicesExternos;
   }

   public void setFiltrarLovIndicesExternos(List<IndicesExternos> filtrarLovIndicesExternos) {
      this.filtrarLovIndicesExternos = filtrarLovIndicesExternos;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public void setAceptar(boolean aceptar) {
      this.aceptar = aceptar;
   }

   public boolean isGuardado() {
      return guardado;
   }

   public void setGuardado(boolean guardado) {
      this.guardado = guardado;
   }

   public int getRegistroBorrados() {
      return registroBorrados;
   }

   public void setRegistroBorrados(int registroBorrados) {
      this.registroBorrados = registroBorrados;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosResultados");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroIndice() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovIndicesExternos");
      infoRegistroIndice = String.valueOf(tabla.getRowCount());
      return infoRegistroIndice;
   }

   public void setInfoRegistroIndice(String infoRegistroIndice) {
      this.infoRegistroIndice = infoRegistroIndice;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public IndicesExternos getIndiceExternoSeleccionado() {
      return indiceExternoSeleccionado;
   }

   public void setIndiceExternoSeleccionado(IndicesExternos indiceExternoSeleccionado) {
      this.indiceExternoSeleccionado = indiceExternoSeleccionado;
   }

}
