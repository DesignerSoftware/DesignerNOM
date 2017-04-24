/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Bancos;
import Entidades.CuentasBancos;
import Entidades.Inforeportes;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarCuentasBancosInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
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
@Named(value = "controlCuentasBancos")
@SessionScoped
public class ControlCuentasBancos implements Serializable {

   @EJB
   AdministrarCuentasBancosInterface administrarCuentasBancos;
   @EJB
   AdministrarRastrosInterface administrarRastros;

   private List<CuentasBancos> listaCuentasBancos;
   private List<CuentasBancos> listaCuentasBancosCrear;
   private List<CuentasBancos> listaCuentasBancosModificar;
   private List<CuentasBancos> listaCuentasBancosBorrar;
   private List<CuentasBancos> listaCuentasBancosFiltrar;
   private CuentasBancos nuevaCuentaBanco;
   private CuentasBancos duplicarCuentaBanco;
   private CuentasBancos editarCuentaBanco;
   private CuentasBancos cuentaBancoSeleccionada;

   private List<Bancos> lovBancos;
   private List<Bancos> lovBancosFiltrar;
   private Bancos bancoSeleccionado;

   private List<Inforeportes> lovReportes;
   private List<Inforeportes> lovReportesFiltrar;
   private Inforeportes reporteSeleccionado;

   private int cualCelda, tipoLista, tipoActualizacion, k, bandera;
   private BigInteger l;
   private boolean aceptar, guardado;
   private Column banco, archivoplano, cuenta, archivosalida, tipocuenta, codconvenio, nombreconvenio, tesoreria, republica, reporte;
   private int registrosBorrados;
   private String mensajeValidacion;
   private int tamano;
   private boolean activarLov;
   private String infoRegistro, infoRegistroBancos, infoRegistroReportes;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlCuentasBancos() {
      listaCuentasBancos = null;
      listaCuentasBancosCrear = new ArrayList<CuentasBancos>();
      listaCuentasBancosModificar = new ArrayList<CuentasBancos>();
      listaCuentasBancosBorrar = new ArrayList<CuentasBancos>();
      editarCuentaBanco = new CuentasBancos();
      duplicarCuentaBanco = new CuentasBancos();
      nuevaCuentaBanco = new CuentasBancos();
      nuevaCuentaBanco.setBanco(new Bancos());
      nuevaCuentaBanco.setInforeporte(new Inforeportes());
      guardado = true;
      tamano = 270;
      cuentaBancoSeleccionada = null;
      activarLov = true;
      lovBancos = null;
      lovReportes = null;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      listaCuentasBancos = null;
      getListaCuentasBancos();
      if (listaCuentasBancos != null) {
         if (!listaCuentasBancos.isEmpty()) {
            cuentaBancoSeleccionada = listaCuentasBancos.get(0);
         }
      }
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      listaCuentasBancos = null;
      getListaCuentasBancos();
      if (listaCuentasBancos != null) {
         if (!listaCuentasBancos.isEmpty()) {
            cuentaBancoSeleccionada = listaCuentasBancos.get(0);
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
String pagActual = "cuentasbancos";
         
         
         


         
         
         
         
         
         
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
         administrarCuentasBancos.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void cambiarIndice(CuentasBancos cuenta, int celda) {
      cuentaBancoSeleccionada = cuenta;
      cualCelda = celda;
      cuentaBancoSeleccionada.getSecuencia();
      if (cualCelda == 0) {
         habilitarBotonLov();
         cuentaBancoSeleccionada.getBanco().getNombre();
      } else if (cualCelda == 1) {
         deshabilitarBotonLov();
         cuentaBancoSeleccionada.getNombrearchivo();
      } else if (cualCelda == 2) {
         deshabilitarBotonLov();
         cuentaBancoSeleccionada.getCuenta();
      } else if (cualCelda == 3) {
         deshabilitarBotonLov();
         cuentaBancoSeleccionada.getArchivosalida();
      } else if (cualCelda == 4) {
         deshabilitarBotonLov();
         cuentaBancoSeleccionada.getTipocuenta();
      } else if (cualCelda == 5) {
         deshabilitarBotonLov();
         cuentaBancoSeleccionada.getCodigoconvenio();
      } else if (cualCelda == 6) {
         deshabilitarBotonLov();
         cuentaBancoSeleccionada.getNombreconvenio();
      } else if (cualCelda == 7) {
         deshabilitarBotonLov();
         cuentaBancoSeleccionada.getCodigotesoreria();
      } else if (cualCelda == 8) {
         deshabilitarBotonLov();
         cuentaBancoSeleccionada.getCodigorepublica();
      } else if (cualCelda == 9) {
         habilitarBotonLov();
         cuentaBancoSeleccionada.getInforeporte().getNombre();
         System.out.println("reporte nombre  : " + cuentaBancoSeleccionada.getInforeporte().getNombre());
      }
   }

   public void asignarIndex(CuentasBancos cuenta, int dlg, int LND) {
      cuentaBancoSeleccionada = cuenta;
      tipoActualizacion = LND;
      if (dlg == 1) {
         contarRegistrosBancos();
         RequestContext.getCurrentInstance().update("formularioDialogos:bancosDialogos");
         RequestContext.getCurrentInstance().execute("PF('bancosDialogos').show()");
      } else if (dlg == 2) {
         contarRegistrosReportes();
         RequestContext.getCurrentInstance().update("formularioDialogos:reportesDialogo");
         RequestContext.getCurrentInstance().execute("PF('reportesDialogo').show()");

      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         banco = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:banco");
         banco.setFilterStyle("display: none; visibility: hidden;");
         archivoplano = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:archivoplano");
         archivoplano.setFilterStyle("display: none; visibility: hidden;");
         cuenta = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:cuenta");
         cuenta.setFilterStyle("display: none; visibility: hidden;");
         archivosalida = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:archivosalida");
         archivosalida.setFilterStyle("display: none; visibility: hidden;");
         tipocuenta = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:tipocuenta");
         tipocuenta.setFilterStyle("display: none; visibility: hidden;");
         codconvenio = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:codconvenio");
         codconvenio.setFilterStyle("display: none; visibility: hidden;");
         nombreconvenio = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:nombreconvenio");
         nombreconvenio.setFilterStyle("display: none; visibility: hidden;");
         tesoreria = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:tesoreria");
         tesoreria.setFilterStyle("display: none; visibility: hidden;");
         republica = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:republica");
         republica.setFilterStyle("display: none; visibility: hidden;");
         reporte = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:reporte");
         reporte.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listaCuentasBancosFiltrar = null;
         RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
         tipoLista = 0;
      }

      listaCuentasBancosBorrar.clear();
      listaCuentasBancosCrear.clear();
      listaCuentasBancosModificar.clear();
      cuentaBancoSeleccionada = null;
      contarRegistros();
      k = 0;
      listaCuentasBancos = null;
      guardado = true;
      RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         //CERRAR FILTRADO
         FacesContext c = FacesContext.getCurrentInstance();
         banco = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:banco");
         banco.setFilterStyle("display: none; visibility: hidden;");
         archivoplano = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:archivoplano");
         archivoplano.setFilterStyle("display: none; visibility: hidden;");
         cuenta = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:cuenta");
         cuenta.setFilterStyle("display: none; visibility: hidden;");
         archivosalida = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:archivosalida");
         archivosalida.setFilterStyle("display: none; visibility: hidden;");
         tipocuenta = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:tipocuenta");
         tipocuenta.setFilterStyle("display: none; visibility: hidden;");
         codconvenio = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:codconvenio");
         codconvenio.setFilterStyle("display: none; visibility: hidden;");
         nombreconvenio = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:nombreconvenio");
         nombreconvenio.setFilterStyle("display: none; visibility: hidden;");
         tesoreria = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:tesoreria");
         tesoreria.setFilterStyle("display: none; visibility: hidden;");
         republica = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:republica");
         republica.setFilterStyle("display: none; visibility: hidden;");
         reporte = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:reporte");
         reporte.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         listaCuentasBancosFiltrar = null;
         RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
         tipoLista = 0;
      }
      listaCuentasBancosBorrar.clear();
      listaCuentasBancosCrear.clear();
      listaCuentasBancosModificar.clear();
      cuentaBancoSeleccionada = null;
      k = 0;
      listaCuentasBancos = null;
      guardado = true;
      navegar("atras");
   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         tamano = 250;
         banco = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:banco");
         banco.setFilterStyle("width: 85% !important;");
         archivoplano = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:archivoplano");
         archivoplano.setFilterStyle("width: 85% !important;");
         cuenta = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:cuenta");
         cuenta.setFilterStyle("width: 85% !important;");
         archivosalida = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:archivosalida");
         archivosalida.setFilterStyle("width: 85% !important;");
         tipocuenta = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:tipocuenta");
         tipocuenta.setFilterStyle("width: 85% !important;");
         codconvenio = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:codconvenio");
         codconvenio.setFilterStyle("width: 85% !important;");
         nombreconvenio = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:nombreconvenio");
         nombreconvenio.setFilterStyle("width: 85% !important;");
         tesoreria = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:tesoreria");
         tesoreria.setFilterStyle("width: 85% !important;");
         republica = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:republica");
         republica.setFilterStyle("width: 85% !important;");
         reporte = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:reporte");
         reporte.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
         bandera = 1;

      } else if (bandera == 1) {
         banco = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:banco");
         banco.setFilterStyle("display: none; visibility: hidden;");
         archivoplano = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:archivoplano");
         archivoplano.setFilterStyle("display: none; visibility: hidden;");
         cuenta = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:cuenta");
         cuenta.setFilterStyle("display: none; visibility: hidden;");
         archivosalida = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:archivosalida");
         archivosalida.setFilterStyle("display: none; visibility: hidden;");
         tipocuenta = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:tipocuenta");
         tipocuenta.setFilterStyle("display: none; visibility: hidden;");
         codconvenio = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:codconvenio");
         codconvenio.setFilterStyle("display: none; visibility: hidden;");
         nombreconvenio = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:nombreconvenio");
         nombreconvenio.setFilterStyle("display: none; visibility: hidden;");
         tesoreria = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:tesoreria");
         tesoreria.setFilterStyle("display: none; visibility: hidden;");
         republica = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:republica");
         republica.setFilterStyle("display: none; visibility: hidden;");
         reporte = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:reporte");
         reporte.setFilterStyle("display: none; visibility: hidden;");
         tipoLista = 0;
         tamano = 270;
         bandera = 0;
         listaCuentasBancosFiltrar = null;
         RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
      }
   }

   public void modificarCuentaBanco(CuentasBancos cuenta) {
      cuentaBancoSeleccionada = cuenta;
      if (!listaCuentasBancosCrear.contains(cuentaBancoSeleccionada)) {
         if (listaCuentasBancosModificar.isEmpty()) {
            listaCuentasBancosModificar.add(cuentaBancoSeleccionada);
         } else if (!listaCuentasBancosModificar.contains(cuentaBancoSeleccionada)) {
            listaCuentasBancosModificar.add(cuentaBancoSeleccionada);
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      }
      RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
   }

   public void borrarCuentasBancos() {
      if (cuentaBancoSeleccionada != null) {
         if (!listaCuentasBancosModificar.isEmpty() && listaCuentasBancosModificar.contains(cuentaBancoSeleccionada)) {
            listaCuentasBancosModificar.remove(listaCuentasBancosModificar.indexOf(cuentaBancoSeleccionada));
            listaCuentasBancosBorrar.add(cuentaBancoSeleccionada);
         } else if (!listaCuentasBancosCrear.isEmpty() && listaCuentasBancosCrear.contains(cuentaBancoSeleccionada)) {
            listaCuentasBancosCrear.remove(listaCuentasBancosCrear.indexOf(cuentaBancoSeleccionada));
         } else {
            listaCuentasBancosBorrar.add(cuentaBancoSeleccionada);
         }
         listaCuentasBancos.remove(cuentaBancoSeleccionada);
         if (tipoLista == 1) {
            listaCuentasBancosFiltrar.remove(cuentaBancoSeleccionada);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
         contarRegistros();
         cuentaBancoSeleccionada = null;
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void revisarDialogoGuardar() {
      if (!listaCuentasBancosBorrar.isEmpty() || !listaCuentasBancosCrear.isEmpty() || !listaCuentasBancosModificar.isEmpty()) {
         RequestContext.getCurrentInstance().update("form:confirmarGuardar");
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void guardarCambiosCuentasBancos() {
      try {
         if (guardado == false) {
            if (!listaCuentasBancosBorrar.isEmpty()) {
               administrarCuentasBancos.borrarCuentaBanco(listaCuentasBancosBorrar);
               registrosBorrados = listaCuentasBancosBorrar.size();
               RequestContext.getCurrentInstance().update("formularioDialogos:mostrarBorrados");
               RequestContext.getCurrentInstance().execute("PF('mostrarBorrados').show()");
               listaCuentasBancosBorrar.clear();
            }
            if (!listaCuentasBancosModificar.isEmpty()) {
               administrarCuentasBancos.modificarCuentaBanco(listaCuentasBancosModificar);
               listaCuentasBancosModificar.clear();
            }
            if (!listaCuentasBancosCrear.isEmpty()) {
               administrarCuentasBancos.crearCuentaBanco(listaCuentasBancosCrear);
               listaCuentasBancosModificar.clear();
            }
            listaCuentasBancos = null;
            getListaCuentasBancos();
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            k = 0;
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            contarRegistros();
            cuentaBancoSeleccionada = null;
         }
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
      } catch (Exception e) {
         System.out.println("Error guardarCambios : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ha ocurrido un error en el guardado, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void editarCelda() {
      if (cuentaBancoSeleccionada != null) {
         editarCuentaBanco = cuentaBancoSeleccionada;

         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editBanco");
            RequestContext.getCurrentInstance().execute("PF('editBanco').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editArchivoPlano");
            RequestContext.getCurrentInstance().execute("PF('editArchivoPlano').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCuenta");
            RequestContext.getCurrentInstance().execute("PF('editCuenta').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editArchivoSalida");
            RequestContext.getCurrentInstance().execute("PF('editArchivoSalida').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editTipoCuenta");
            RequestContext.getCurrentInstance().execute("PF('editTipoCuenta').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editCodConvenio");
            RequestContext.getCurrentInstance().execute("PF('editCodConvenio').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editNombreConvenio");
            RequestContext.getCurrentInstance().execute("PF('editNombreConvenio').show()");
            cualCelda = -1;
         } else if (cualCelda == 7) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editTesoreria");
            RequestContext.getCurrentInstance().execute("PF('editTesoreria').show()");
            cualCelda = -1;
         } else if (cualCelda == 8) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editRepublica");
            RequestContext.getCurrentInstance().execute("PF('editRepublica').show()");
            cualCelda = -1;
         } else if (cualCelda == 9) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editReporte");
            RequestContext.getCurrentInstance().execute("PF('editReporte').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void agregarNuevaCuentaBanco() {
      int contador = 0;
      int duplicados = 0;
      mensajeValidacion = " ";

//        if (nuevaCuentaBanco.getBanco() == null) {
//            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
//        } else {
//            contador++;
//        }
      if (nuevaCuentaBanco.getNombrearchivo() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }

      if (contador == 1) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            banco = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:banco");
            banco.setFilterStyle("display: none; visibility: hidden;");
            archivoplano = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:archivoplano");
            archivoplano.setFilterStyle("display: none; visibility: hidden;");
            cuenta = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:cuenta");
            cuenta.setFilterStyle("display: none; visibility: hidden;");
            archivosalida = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:archivosalida");
            archivosalida.setFilterStyle("display: none; visibility: hidden;");
            tipocuenta = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:tipocuenta");
            tipocuenta.setFilterStyle("display: none; visibility: hidden;");
            codconvenio = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:codconvenio");
            codconvenio.setFilterStyle("display: none; visibility: hidden;");
            nombreconvenio = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:nombreconvenio");
            nombreconvenio.setFilterStyle("display: none; visibility: hidden;");
            tesoreria = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:tesoreria");
            tesoreria.setFilterStyle("display: none; visibility: hidden;");
            republica = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:republica");
            republica.setFilterStyle("display: none; visibility: hidden;");
            reporte = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:reporte");
            reporte.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listaCuentasBancosFiltrar = null;
            RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevaCuentaBanco.setSecuencia(l);
         listaCuentasBancosCrear.add(nuevaCuentaBanco);
         listaCuentasBancos.add(nuevaCuentaBanco);
         cuentaBancoSeleccionada = nuevaCuentaBanco;
         nuevaCuentaBanco = new CuentasBancos();
         RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('nuevoRegistroCuentasBancos').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaCuentaBanco");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaCuentaBanco').show()");
         contador = 0;
      }
   }

   public void limpiarNuevaCuentaBanco() {
      nuevaCuentaBanco = new CuentasBancos();
   }

   public void duplicandoCuentasBancos() {
      if (cuentaBancoSeleccionada != null) {
         duplicarCuentaBanco = new CuentasBancos();
         k++;
         l = BigInteger.valueOf(k);

         duplicarCuentaBanco.setSecuencia(l);
         duplicarCuentaBanco.setBanco(cuentaBancoSeleccionada.getBanco());
         duplicarCuentaBanco.setNombrearchivo(cuentaBancoSeleccionada.getNombrearchivo());
         duplicarCuentaBanco.setCuenta(cuentaBancoSeleccionada.getCuenta());
         duplicarCuentaBanco.setArchivosalida(cuentaBancoSeleccionada.getArchivosalida());
         duplicarCuentaBanco.setTipocuenta(cuentaBancoSeleccionada.getTipocuenta());
         duplicarCuentaBanco.setCodigoconvenio(cuentaBancoSeleccionada.getCodigoconvenio());
         duplicarCuentaBanco.setNombreconvenio(cuentaBancoSeleccionada.getNombreconvenio());
         duplicarCuentaBanco.setCodigotesoreria(cuentaBancoSeleccionada.getCodigotesoreria());
         duplicarCuentaBanco.setCodigorepublica(cuentaBancoSeleccionada.getCodigorepublica());
         duplicarCuentaBanco.setInforeporte(cuentaBancoSeleccionada.getInforeporte());
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCuentaBanco");
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroCuentasBancos').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int contador = 0;
      int duplicados = 0;
      mensajeValidacion = " ";

//        if (duplicarCuentaBanco.getBanco() == null) {
//            mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
//        } else {
//            contador++;
//        }
      if (duplicarCuentaBanco.getNombrearchivo() == null) {
         mensajeValidacion = "Los campos marcados con asterisco son obligatorios";
      } else {
         contador++;
      }

      if (contador == 1) {
         if (bandera == 1) {
            FacesContext c = FacesContext.getCurrentInstance();
            banco = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:banco");
            banco.setFilterStyle("display: none; visibility: hidden;");
            archivoplano = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:archivoplano");
            archivoplano.setFilterStyle("display: none; visibility: hidden;");
            cuenta = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:cuenta");
            cuenta.setFilterStyle("display: none; visibility: hidden;");
            archivosalida = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:archivosalida");
            archivosalida.setFilterStyle("display: none; visibility: hidden;");
            tipocuenta = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:tipocuenta");
            tipocuenta.setFilterStyle("display: none; visibility: hidden;");
            codconvenio = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:codconvenio");
            codconvenio.setFilterStyle("display: none; visibility: hidden;");
            nombreconvenio = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:nombreconvenio");
            nombreconvenio.setFilterStyle("display: none; visibility: hidden;");
            tesoreria = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:tesoreria");
            tesoreria.setFilterStyle("display: none; visibility: hidden;");
            republica = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:republica");
            republica.setFilterStyle("display: none; visibility: hidden;");
            reporte = (Column) c.getViewRoot().findComponent("form:datosCuentasBancos:reporte");
            reporte.setFilterStyle("display: none; visibility: hidden;");
            bandera = 0;
            listaCuentasBancosFiltrar = null;
            RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         duplicarCuentaBanco.setSecuencia(l);
         listaCuentasBancosCrear.add(duplicarCuentaBanco);
         listaCuentasBancos.add(duplicarCuentaBanco);
         cuentaBancoSeleccionada = duplicarCuentaBanco;
         duplicarCuentaBanco = new CuentasBancos();
         RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
         contarRegistros();
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('duplicarRegistroCuentasBancos').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionDuplicarCuentaBanco");
         RequestContext.getCurrentInstance().execute("PF('validacionDuplicarCuentaBanco').show()");
         contador = 0;
      }
   }

   public void limpiarDuplicarCuentasBancos() {
      duplicarCuentaBanco = new CuentasBancos();
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCuentasBancosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "CUENTASBANCOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosCuentasBancosExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "CUENTASBANCOS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (cuentaBancoSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(cuentaBancoSeleccionada.getSecuencia(), "CUENTASBANCOS"); //En ENCARGATURAS lo cambia por el nombre de su tabla
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
      } else if (administrarRastros.verificarHistoricosTabla("CUENTASBANCOS")) { // igual acá
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

   public void actualizarBancos() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         cuentaBancoSeleccionada.setBanco(bancoSeleccionado);
         if (!listaCuentasBancosCrear.contains(cuentaBancoSeleccionada)) {
            if (listaCuentasBancosModificar.isEmpty()) {
               listaCuentasBancosModificar.add(cuentaBancoSeleccionada);
            } else if (!listaCuentasBancosModificar.contains(cuentaBancoSeleccionada)) {
               listaCuentasBancosModificar.add(cuentaBancoSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         deshabilitarBotonLov();
         RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
      } else if (tipoActualizacion == 1) {
         nuevaCuentaBanco.setBanco(bancoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCuentaBanco");
      } else if (tipoActualizacion == 2) {
         duplicarCuentaBanco.setBanco(bancoSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCuentaBanco");
      }
      lovBancosFiltrar = null;
      bancoSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("formularioDialogos:bancosDialogos");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovBancos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarB");

      context.reset("formularioDialogos:lovBancos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovBancos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('bancosDialogos').hide()");
   }

   public void cancelarCambioBancos() {
      lovBancosFiltrar = null;
      aceptar = true;
      tipoActualizacion = -1;
      bancoSeleccionado = null;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:bancosDialogos");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovBancos");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarB");

      context.reset("formularioDialogos:lovBancos:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovBancos').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('bancosDialogos').hide()");
   }

   public void actualizarReportes() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         cuentaBancoSeleccionada.setInforeporte(reporteSeleccionado);
         if (!listaCuentasBancosCrear.contains(cuentaBancoSeleccionada)) {
            if (listaCuentasBancosModificar.isEmpty()) {
               listaCuentasBancosModificar.add(cuentaBancoSeleccionada);
            } else if (!listaCuentasBancosModificar.contains(cuentaBancoSeleccionada)) {
               listaCuentasBancosModificar.add(cuentaBancoSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         deshabilitarBotonLov();
         RequestContext.getCurrentInstance().update("form:datosCuentasBancos");
      } else if (tipoActualizacion == 1) {
         nuevaCuentaBanco.setInforeporte(reporteSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCuentaBanco");
      } else if (tipoActualizacion == 2) {
         duplicarCuentaBanco.setInforeporte(reporteSeleccionado);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCuentaBanco");
      }
      lovBancosFiltrar = null;
      reporteSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("formularioDialogos:reportesDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovReportes");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarR");

      context.reset("formularioDialogos:lovReportes:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReportes').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('reportesDialogo').hide()");
   }

   public void cancelarCambioReportes() {
      RequestContext context = RequestContext.getCurrentInstance();
      lovBancosFiltrar = null;
      reporteSeleccionado = null;
      aceptar = true;
      tipoActualizacion = -1;

      RequestContext.getCurrentInstance().update("formularioDialogos:reportesDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:lovReportes");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarR");

      context.reset("formularioDialogos:lovReportes:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReportes').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('reportesDialogo').hide()");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosBancos() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroBancos");
   }

   public void contarRegistrosReportes() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroReportes");
   }

   public void habilitarBotonLov() {
      activarLov = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarBotonLov() {
      activarLov = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   ///////GETS Y SETS///////////
   public List<CuentasBancos> getListaCuentasBancos() {
      if (listaCuentasBancos == null) {
         listaCuentasBancos = administrarCuentasBancos.consultarCuentasBancos();
      }
      return listaCuentasBancos;
   }

   public void setListaCuentasBancos(List<CuentasBancos> listaCuentasBancos) {
      this.listaCuentasBancos = listaCuentasBancos;
   }

   public List<CuentasBancos> getListaCuentasBancosFiltrar() {
      return listaCuentasBancosFiltrar;
   }

   public void setListaCuentasBancosFiltrar(List<CuentasBancos> listaCuentasBancosFiltrar) {
      this.listaCuentasBancosFiltrar = listaCuentasBancosFiltrar;
   }

   public CuentasBancos getDuplicarCuentaBanco() {
      return duplicarCuentaBanco;
   }

   public void setDuplicarCuentaBanco(CuentasBancos duplicarCuentaBanco) {
      this.duplicarCuentaBanco = duplicarCuentaBanco;
   }

   public CuentasBancos getEditarCuentaBanco() {
      return editarCuentaBanco;
   }

   public void setEditarCuentaBanco(CuentasBancos editarCuentaBanco) {
      this.editarCuentaBanco = editarCuentaBanco;
   }

   public CuentasBancos getCuentaBancoSeleccionada() {
      return cuentaBancoSeleccionada;
   }

   public void setCuentaBancoSeleccionada(CuentasBancos cuentaBancoSeleccionada) {
      this.cuentaBancoSeleccionada = cuentaBancoSeleccionada;
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

   public boolean isActivarLov() {
      return activarLov;
   }

   public void setActivarLov(boolean activarLov) {
      this.activarLov = activarLov;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosCuentasBancos");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroBancos() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovBancos");
      infoRegistroBancos = String.valueOf(tabla.getRowCount());
      return infoRegistroBancos;
   }

   public void setInfoRegistroBancos(String infoRegistroBancos) {
      this.infoRegistroBancos = infoRegistroBancos;
   }

   public String getInfoRegistroReportes() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:lovReportes");
      infoRegistroReportes = String.valueOf(tabla.getRowCount());
      return infoRegistroReportes;
   }

   public void setInfoRegistroReportes(String infoRegistroReportes) {
      this.infoRegistroReportes = infoRegistroReportes;
   }

   public List<Bancos> getLovBancos() {
      if (lovBancos == null) {
         lovBancos = administrarCuentasBancos.consultarBancos();
      }
      return lovBancos;
   }

   public void setLovBancos(List<Bancos> lovBancos) {
      this.lovBancos = lovBancos;
   }

   public List<Bancos> getLovBancosFiltrar() {
      return lovBancosFiltrar;
   }

   public void setLovBancosFiltrar(List<Bancos> lovBancosFiltrar) {
      this.lovBancosFiltrar = lovBancosFiltrar;
   }

   public Bancos getBancoSeleccionado() {
      return bancoSeleccionado;
   }

   public void setBancoSeleccionado(Bancos bancoSeleccionado) {
      this.bancoSeleccionado = bancoSeleccionado;
   }

   public List<Inforeportes> getLovReportesFiltrar() {
      return lovReportesFiltrar;
   }

   public void setLovReportesFiltrar(List<Inforeportes> lovReportesFiltrar) {
      this.lovReportesFiltrar = lovReportesFiltrar;
   }

   public Inforeportes getReporteSeleccionado() {
      return reporteSeleccionado;
   }

   public void setReporteSeleccionado(Inforeportes reporteSeleccionado) {
      this.reporteSeleccionado = reporteSeleccionado;
   }

   public List<Inforeportes> getLovReportes() {
      if (lovReportes == null) {
         lovReportes = administrarCuentasBancos.consultarInfoReportes();
      }
      return lovReportes;
   }

   public void setLovReportes(List<Inforeportes> lovReportes) {
      this.lovReportes = lovReportes;
   }

   public CuentasBancos getNuevaCuentaBanco() {
      return nuevaCuentaBanco;
   }

   public void setNuevaCuentaBanco(CuentasBancos nuevaCuentaBanco) {
      this.nuevaCuentaBanco = nuevaCuentaBanco;
   }

   public int getTamano() {
      return tamano;
   }

   public void setTamano(int tamano) {
      this.tamano = tamano;
   }

   public String getMensajeValidacion() {
      return mensajeValidacion;
   }

   public void setMensajeValidacion(String mensajeValidacion) {
      this.mensajeValidacion = mensajeValidacion;
   }

   public int getRegistrosBorrados() {
      return registrosBorrados;
   }

   public void setRegistrosBorrados(int registrosBorrados) {
      this.registrosBorrados = registrosBorrados;
   }

}
