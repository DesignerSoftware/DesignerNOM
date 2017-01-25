/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import ControlNavegacion.ControlListaNavegacion;
import Entidades.Bancos;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarBancosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.Exporter;
import org.primefaces.context.RequestContext;

@Named(value = "controlBancos")
@SessionScoped
public class ControlBancos implements Serializable {

   @EJB
   AdministrarBancosInterface administrarBancos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<Bancos> listaBancos;
   private List<Bancos> listaBancosCrear;
   private List<Bancos> listaBancosModificar;
   private List<Bancos> listaBancosBorrar;
   private List<Bancos> listaBancosFiltrar;
   private Bancos nuevoBanco;
   private Bancos duplicarBanco;
   private Bancos editarBanco;
   private Bancos bancoSeleccionado;

   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   private boolean permitirIndex;
   private Column codigo, nombre, compensacion, codalt, prefijo, numid, cuenta;
   private int registrosBorrados;
   private String mensajeValidacion, paginaanterior;
   private int tamano;
   private boolean activarLov;
   private String infoRegistro;

   private String paginaAnterior;
   private Map<String, Object> mapParametros;

   public ControlBancos() {
      listaBancos = null;
      listaBancosCrear = new ArrayList<Bancos>();
      listaBancosModificar = new ArrayList<Bancos>();
      listaBancosBorrar = new ArrayList<Bancos>();
      permitirIndex = true;
      editarBanco = new Bancos();
      nuevoBanco = new Bancos();
      duplicarBanco = new Bancos();
      guardado = true;
      tamano = 270;
      cualCelda = -1;
      bancoSeleccionado = null;
      activarLov = true;

      paginaAnterior = "nominaf";
      mapParametros = new LinkedHashMap<String, Object>();
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarBancos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaanterior = pagina;
      listaBancos = null;
      getListaBancos();
      if (listaBancos != null) {
         if (!listaBancos.isEmpty()) {
            bancoSeleccionado = listaBancos.get(0);
         }
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listaBancos = null;
      getListaBancos();
      if (listaBancos != null) {
         if (!listaBancos.isEmpty()) {
            bancoSeleccionado = listaBancos.get(0);
         }
      }
   }

   public void navegar(String pag) {
      FacesContext fc = FacesContext.getCurrentInstance();
      ControlListaNavegacion controlListaNavegacion = (ControlListaNavegacion) fc.getApplication().evaluateExpressionGet(fc, "#{controlListaNavegacion}", ControlListaNavegacion.class);
      if (pag.equals("atras")) {
         pag = paginaAnterior;
         paginaAnterior = "nominaf";
         controlListaNavegacion.quitarPagina();
      } else {
            String pagActual = "banco";
//         Map<String, Object> mapParaEnviar = new LinkedHashMap<String, Object>();
//         mapParametros.put("paginaAnterior", pagActual);
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

   public void cambiarIndice(Bancos banco, int celda) {
      bancoSeleccionado = banco;
      cualCelda = celda;
      bancoSeleccionado.getSecuencia();
      if (cualCelda == 0) {
         bancoSeleccionado.getCodigo();
      } else if (cualCelda == 1) {
         bancoSeleccionado.getNombre();
      } else if (cualCelda == 2) {
         bancoSeleccionado.getCodigocompensacion();
      } else if (cualCelda == 3) {
         bancoSeleccionado.getCodigoalternativo();
      } else if (cualCelda == 4) {
         bancoSeleccionado.getPrefijocuentas();
      } else if (cualCelda == 5) {
         bancoSeleccionado.getNumeroidentificaciont();
      } else if (cualCelda == 6) {
         bancoSeleccionado.getCuentaempresa();
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosBancos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosBancos:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         compensacion = (Column) c.getViewRoot().findComponent("form:datosBancos:compensacion");
         compensacion.setFilterStyle("display: none; visibility: hidden;");
         codalt = (Column) c.getViewRoot().findComponent("form:datosBancos:codalt");
         codalt.setFilterStyle("display: none; visibility: hidden;");
         prefijo = (Column) c.getViewRoot().findComponent("form:datosBancos:prefijo");
         prefijo.setFilterStyle("display: none; visibility: hidden;");
         numid = (Column) c.getViewRoot().findComponent("form:datosBancos:numid");
         numid.setFilterStyle("display: none; visibility: hidden;");
         cuenta = (Column) c.getViewRoot().findComponent("form:datosBancos:cuenta");
         cuenta.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listaBancosFiltrar = null;
         RequestContext.getCurrentInstance().update("form:datosBancos");
         tipoLista = 0;
      }

      listaBancosBorrar.clear();
      listaBancosCrear.clear();
      listaBancosModificar.clear();
      bancoSeleccionado = null;
      contarRegistros();
      k = 0;
      listaBancos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosBancos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void cancelarModificacionSalir() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosBancos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosBancos:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         compensacion = (Column) c.getViewRoot().findComponent("form:datosBancos:compensacion");
         compensacion.setFilterStyle("display: none; visibility: hidden;");
         codalt = (Column) c.getViewRoot().findComponent("form:datosBancos:codalt");
         codalt.setFilterStyle("display: none; visibility: hidden;");
         prefijo = (Column) c.getViewRoot().findComponent("form:datosBancos:prefijo");
         prefijo.setFilterStyle("display: none; visibility: hidden;");
         numid = (Column) c.getViewRoot().findComponent("form:datosBancos:numid");
         numid.setFilterStyle("display: none; visibility: hidden;");
         cuenta = (Column) c.getViewRoot().findComponent("form:datosBancos:cuenta");
         cuenta.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listaBancosFiltrar = null;
         RequestContext.getCurrentInstance().update("form:datosBancos");
         tipoLista = 0;
      }

      listaBancosBorrar.clear();
      listaBancosCrear.clear();
      listaBancosModificar.clear();
      bancoSeleccionado = null;
      contarRegistros();
      k = 0;
      listaBancos = null;
      guardado = true;
      permitirIndex = true;
      RequestContext.getCurrentInstance().update("form:datosBancos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         codigo = (Column) c.getViewRoot().findComponent("form:datosBancos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosBancos:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         compensacion = (Column) c.getViewRoot().findComponent("form:datosBancos:compensacion");
         compensacion.setFilterStyle("display: none; visibility: hidden;");
         codalt = (Column) c.getViewRoot().findComponent("form:datosBancos:codalt");
         codalt.setFilterStyle("display: none; visibility: hidden;");
         prefijo = (Column) c.getViewRoot().findComponent("form:datosBancos:prefijo");
         prefijo.setFilterStyle("display: none; visibility: hidden;");
         numid = (Column) c.getViewRoot().findComponent("form:datosBancos:numid");
         numid.setFilterStyle("display: none; visibility: hidden;");
         cuenta = (Column) c.getViewRoot().findComponent("form:datosBancos:cuenta");
         cuenta.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listaBancosFiltrar = null;
         RequestContext.getCurrentInstance().update("form:datosBancos");
         tipoLista = 0;
      }
      listaBancosBorrar.clear();
      listaBancosCrear.clear();
      listaBancosModificar.clear();
      bancoSeleccionado = null;
      k = 0;
      listaBancos = null;
      guardado = true;
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         codigo = (Column) c.getViewRoot().findComponent("form:datosBancos:codigo");
         codigo.setFilterStyle("width: 85% !important;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosBancos:nombre");
         nombre.setFilterStyle("width: 85% !important;");
         compensacion = (Column) c.getViewRoot().findComponent("form:datosBancos:compensacion");
         compensacion.setFilterStyle("width: 85% !important;");
         codalt = (Column) c.getViewRoot().findComponent("form:datosBancos:codalt");
         codalt.setFilterStyle("width: 85% !important;");
         prefijo = (Column) c.getViewRoot().findComponent("form:datosBancos:prefijo");
         prefijo.setFilterStyle("width: 85% !important;");
         numid = (Column) c.getViewRoot().findComponent("form:datosBancos:numid");
         numid.setFilterStyle("width: 85% !important;");
         cuenta = (Column) c.getViewRoot().findComponent("form:datosBancos:cuenta");
         cuenta.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosBancos");
         bandera = 1;

      } else if (bandera == 1) {
         codigo = (Column) c.getViewRoot().findComponent("form:datosBancos:codigo");
         codigo.setFilterStyle("display: none; visibility: hidden;");
         nombre = (Column) c.getViewRoot().findComponent("form:datosBancos:nombre");
         nombre.setFilterStyle("display: none; visibility: hidden;");
         compensacion = (Column) c.getViewRoot().findComponent("form:datosBancos:compensacion");
         compensacion.setFilterStyle("display: none; visibility: hidden;");
         codalt = (Column) c.getViewRoot().findComponent("form:datosBancos:codalt");
         codalt.setFilterStyle("display: none; visibility: hidden;");
         prefijo = (Column) c.getViewRoot().findComponent("form:datosBancos:prefijo");
         prefijo.setFilterStyle("display: none; visibility: hidden;");
         numid = (Column) c.getViewRoot().findComponent("form:datosBancos:numid");
         numid.setFilterStyle("display: none; visibility: hidden;");
         cuenta = (Column) c.getViewRoot().findComponent("form:datosBancos:cuenta");
         cuenta.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listaBancosFiltrar = null;
         tamano = 270;
         RequestContext.getCurrentInstance().update("form:datosBancos");
         tipoLista = 0;
      }
   }

   public void modificarBanco(Bancos banco) {
      bancoSeleccionado = banco;
      if (!listaBancosCrear.contains(bancoSeleccionado)) {
         if (listaBancosModificar.isEmpty()) {
            listaBancosModificar.add(bancoSeleccionado);
         } else if (!listaBancosModificar.contains(bancoSeleccionado)) {
            listaBancosModificar.add(bancoSeleccionado);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      RequestContext.getCurrentInstance().update("form:datosBancos");
   }

   public void borrarBancos() {
      if (bancoSeleccionado != null) {
         if (!listaBancosModificar.isEmpty() && listaBancosModificar.contains(bancoSeleccionado)) {
            listaBancosModificar.remove(listaBancosModificar.indexOf(bancoSeleccionado));
            listaBancosBorrar.add(bancoSeleccionado);
         } else if (!listaBancosCrear.isEmpty() && listaBancosCrear.contains(bancoSeleccionado)) {
            listaBancosCrear.remove(listaBancosCrear.indexOf(bancoSeleccionado));
         } else {
            listaBancosBorrar.add(bancoSeleccionado);
         }
         listaBancos.remove(bancoSeleccionado);
         if (tipoLista == 1) {
            listaBancosFiltrar.remove(bancoSeleccionado);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosBancos");
         contarRegistros();
         bancoSeleccionado = null;
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void revisarDialogoGuardar() {
      if (!listaBancosBorrar.isEmpty() || !listaBancosCrear.isEmpty() || !listaBancosModificar.isEmpty()) {
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarCambiosBancos() {
      try {
         if (guardado == false) {
            System.out.println("tamaño lista crear : " + listaBancosCrear.size());
            System.out.println("tamaño lista borrar : " + listaBancosBorrar.size());
            System.out.println("tamaño lista modificar : " + listaBancosModificar.size());
            if (!listaBancosBorrar.isEmpty()) {
               administrarBancos.borrarBanco(listaBancosBorrar);
               registrosBorrados = listaBancosBorrar.size();
               RequestContext.getCurrentInstance().update("formularioDialogos:mostrarBorrados");
               RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
               listaBancosBorrar.clear();
            }
            if (!listaBancosModificar.isEmpty()) {
               administrarBancos.modificarBanco(listaBancosModificar);
               listaBancosModificar.clear();
            }
            if (!listaBancosCrear.isEmpty()) {
               administrarBancos.crearBanco(listaBancosCrear);
               listaBancosModificar.clear();
            }
            listaBancos = null;
            getListaBancos();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            bancoSeleccionado = null;
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosBancos");
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosBancosSalir() {
      try {
         if (guardado == false) {
            System.out.println("tamaño lista crear : " + listaBancosCrear.size());
            System.out.println("tamaño lista borrar : " + listaBancosBorrar.size());
            System.out.println("tamaño lista modificar : " + listaBancosModificar.size());
            if (!listaBancosBorrar.isEmpty()) {
               administrarBancos.borrarBanco(listaBancosBorrar);
               registrosBorrados = listaBancosBorrar.size();
               RequestContext.getCurrentInstance().update("formularioDialogos:mostrarBorrados");
               RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
               listaBancosBorrar.clear();
            }
            if (!listaBancosModificar.isEmpty()) {
               administrarBancos.modificarBanco(listaBancosModificar);
               listaBancosModificar.clear();
            }
            if (!listaBancosCrear.isEmpty()) {
               administrarBancos.crearBanco(listaBancosCrear);
               listaBancosModificar.clear();
            }
            listaBancos = null;
            getListaBancos();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            bancoSeleccionado = null;
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosBancos");
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
      navegar("atras");
   }

   public void editarCelda() {
      if (bancoSeleccionado != null) {
         editarBanco = bancoSeleccionado;

         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCodigo");
            RequestContext.getCurrentInstance().execute("PF('editCodigo').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editNombre");
            RequestContext.getCurrentInstance().execute("PF('editNombre').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCodCompensacion");
            RequestContext.getCurrentInstance().execute("PF('editCodCompensacion').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCodAlt");
            RequestContext.getCurrentInstance().execute("PF('editCodAlt').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editPrefijo");
            RequestContext.getCurrentInstance().execute("PF('editPrefijo').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editNumeroId");
            RequestContext.getCurrentInstance().execute("PF('editNumeroId').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCuenta");
            RequestContext.getCurrentInstance().execute("PF('editCuenta').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void agregarNuevoBanco() {
      int contador = 0;
      int duplicados = 0;
      mensajeValidacion = " ";

      if (nuevoBanco.getCodigo() == null) {
         mensajeValidacion = "El campo código es obligatorio";
      } else {
         for (int x = 0; x < listaBancos.size(); x++) {
            if (listaBancos.get(x).getCodigo().equals(nuevoBanco.getCodigo()) == true) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
         } else {
            contador++;
         }
      }
      if (nuevoBanco.getNombre() == null || nuevoBanco.getNombre().isEmpty()) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }

      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosBancos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosBancos:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            compensacion = (Column) c.getViewRoot().findComponent("form:datosBancos:compensacion");
            compensacion.setFilterStyle("display: none; visibility: hidden;");
            codalt = (Column) c.getViewRoot().findComponent("form:datosBancos:codalt");
            codalt.setFilterStyle("display: none; visibility: hidden;");
            prefijo = (Column) c.getViewRoot().findComponent("form:datosBancos:prefijo");
            prefijo.setFilterStyle("display: none; visibility: hidden;");
            numid = (Column) c.getViewRoot().findComponent("form:datosBancos:numid");
            numid.setFilterStyle("display: none; visibility: hidden;");
            cuenta = (Column) c.getViewRoot().findComponent("form:datosBancos:cuenta");
            cuenta.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listaBancosFiltrar = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevoBanco.setSecuencia(l);
         listaBancosCrear.add(nuevoBanco);
         listaBancos.add(nuevoBanco);
         bancoSeleccionado = nuevoBanco;
         nuevoBanco = new Bancos();
         RequestContext.getCurrentInstance().update("form:datosBancos");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroBancos').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevoBanco");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevoBanco').show()");
         contador = 0;
      }
   }

   public void limpiarNuevoBanco() {
      nuevoBanco = new Bancos();
   }

   public void duplicandoBancos() {
      if (bancoSeleccionado != null) {
         duplicarBanco = new Bancos();
         k++;
         l = BigInteger.valueOf(k);

         duplicarBanco.setSecuencia(l);
         duplicarBanco.setCodigo(bancoSeleccionado.getCodigo());
         duplicarBanco.setNombre(bancoSeleccionado.getNombre());
         duplicarBanco.setCodigocompensacion(bancoSeleccionado.getCodigocompensacion());
         duplicarBanco.setCodigoalternativo(bancoSeleccionado.getCodigoalternativo());
         duplicarBanco.setPrefijocuentas(bancoSeleccionado.getPrefijocuentas());
         duplicarBanco.setNumeroidentificaciont(bancoSeleccionado.getNumeroidentificaciont());
         duplicarBanco.setCuentaempresa(bancoSeleccionado.getCuentaempresa());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarBanco");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroBancos').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      int duplicados = 0;
      mensajeValidacion = " ";

      if (duplicarBanco.getCodigo() == null) {
         mensajeValidacion = "El campo código es obligatorio";
      } else {
         for (int x = 0; x < listaBancos.size(); x++) {
            if (listaBancos.get(x).getCodigo().equals(duplicarBanco.getCodigo()) == true) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "Existe un registro con el código ingresado. Por favor ingrese un código válido";
         } else {
            contador++;
         }
      }
      if (duplicarBanco.getNombre() == null || duplicarBanco.getNombre().isEmpty()) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }

      if (contador == 2) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            codigo = (Column) c.getViewRoot().findComponent("form:datosBancos:codigo");
            codigo.setFilterStyle("display: none; visibility: hidden;");
            nombre = (Column) c.getViewRoot().findComponent("form:datosBancos:nombre");
            nombre.setFilterStyle("display: none; visibility: hidden;");
            compensacion = (Column) c.getViewRoot().findComponent("form:datosBancos:compensacion");
            compensacion.setFilterStyle("display: none; visibility: hidden;");
            codalt = (Column) c.getViewRoot().findComponent("form:datosBancos:codalt");
            codalt.setFilterStyle("display: none; visibility: hidden;");
            prefijo = (Column) c.getViewRoot().findComponent("form:datosBancos:prefijo");
            prefijo.setFilterStyle("display: none; visibility: hidden;");
            numid = (Column) c.getViewRoot().findComponent("form:datosBancos:numid");
            numid.setFilterStyle("display: none; visibility: hidden;");
            cuenta = (Column) c.getViewRoot().findComponent("form:datosBancos:cuenta");
            cuenta.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listaBancosFiltrar = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         duplicarBanco.setSecuencia(l);
         listaBancosCrear.add(duplicarBanco);
         listaBancos.add(duplicarBanco);
         bancoSeleccionado = duplicarBanco;
         duplicarBanco = new Bancos();
         RequestContext.getCurrentInstance().update("form:datosBancos");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroBancos').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionDuplicarBanco");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarBanco').show()");
         contador = 0;
      }
   }

   public void limpiarDuplicarBancos() {
      duplicarBanco = new Bancos();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosBancosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "BANCOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosBancosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "BANCOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (bancoSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(bancoSeleccionado.getSecuencia(), "BANCOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("BANCOS")) { // igual acá
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
         System.out.println("ERROR ControlTiposCursos eventoFiltrar ERROR===" + e.getMessage());
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   //////GETS Y SETS //////
   public List<Bancos> getListaBancos() {
      if (listaBancos == null) {
         listaBancos = administrarBancos.consultarBancos();
      }
      return listaBancos;
   }

   public void setListaBancos(List<Bancos> listaBancos) {
      this.listaBancos = listaBancos;
   }

   public List<Bancos> getListaBancosFiltrar() {
      return listaBancosFiltrar;
   }

   public void setListaBancosFiltrar(List<Bancos> listaBancosFiltrar) {
      this.listaBancosFiltrar = listaBancosFiltrar;
   }

   public Bancos getNuevoBanco() {
      return nuevoBanco;
   }

   public void setNuevoBanco(Bancos nuevoBanco) {
      this.nuevoBanco = nuevoBanco;
   }

   public Bancos getDuplicarBanco() {
      return duplicarBanco;
   }

   public void setDuplicarBanco(Bancos duplicarBanco) {
      this.duplicarBanco = duplicarBanco;
   }

   public Bancos getEditarBanco() {
      return editarBanco;
   }

   public void setEditarBanco(Bancos editarBanco) {
      this.editarBanco = editarBanco;
   }

   public Bancos getBancoSeleccionado() {
      return bancoSeleccionado;
   }

   public void setBancoSeleccionado(Bancos bancoSeleccionado) {
      this.bancoSeleccionado = bancoSeleccionado;
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

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosBancos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public int getRegistrosBorrados() {
      return registrosBorrados;
   }

   public void setRegistrosBorrados(int registrosBorrados) {
      this.registrosBorrados = registrosBorrados;
   }

}
