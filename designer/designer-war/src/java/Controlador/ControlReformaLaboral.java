package Controlador;

import Entidades.DetallesReformasLaborales;
import Entidades.ReformasLaborales;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarReformasLaboralesInterface;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
public class ControlReformaLaboral implements Serializable {

   @EJB
   AdministrarReformasLaboralesInterface administrarReformaLaboral;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   //
   private List<ReformasLaborales> listaReformasLaborales;
   private List<ReformasLaborales> filtrarListaReformasLaborales;
   private ReformasLaborales reformaLaboralSeleccionada;
   ///////
   private List<DetallesReformasLaborales> listaDetallesReformasLaborales;
   private List<DetallesReformasLaborales> filtrarListaDetallesReformasLaborales;
   private DetallesReformasLaborales detalleReformaLSeleccionado;
   //Activo/Desactivo Crtl + F11
   private int bandera;
   //Columnas Tabla VC
   private Column reformaCodigo, reformaNombre, reformaIntegral;
   private Column detalleTipoPago, detalleFactor;
   //Otros
   private boolean aceptar;
   //modificar
   private List<ReformasLaborales> listReformasLaboralesModificar;
   private List<DetallesReformasLaborales> listDetallesReformasLaboralesModificar;
   private boolean guardado, guardadoDetalles;
   //crear 
   private ReformasLaborales nuevoReformaLaboral;
   private DetallesReformasLaborales nuevoDetalleReformaLaboral;
   private List<ReformasLaborales> listReformasLaboralesCrear;
   private List<DetallesReformasLaborales> listDetallesReformasLaboralesCrear;
   private BigInteger l;
   private int k;
   //borrar 
   private List<ReformasLaborales> listReformasLaboralesBorrar;
   private List<DetallesReformasLaborales> listDetallesReformasLaboralesBorrar;
   //editar celda
   private ReformasLaborales editarReformaLaboral;
   private DetallesReformasLaborales editarDetalleReformaLaboral;
   private int cualCelda, tipoLista, cualCeldaDetalles, tipoListaDetalles;
   //duplicar
   private ReformasLaborales duplicarReformaLaboral;
   private DetallesReformasLaborales duplicarDetalleReformaLaboral;
   private String msnConfirmarRastro, msnConfirmarRastroHistorico;
   private BigInteger backUp;
   private String nombreTablaRastro;
   private String nombreXML, nombreTabla;
   //////////////////////
   private int tipoActualizacion;
   private short auxCodigoReformaLaboral;
   private String auxNombreReformaLaboral;
   //
   private boolean cambiosPagina;
   private String altoTablaReforma, altoTablaDetalles;
   //
   private String nombreReformaClonar;
   private short codigoReformaClonar;
   //
   private List<ReformasLaborales> lovReformasLaborales;
   private List<ReformasLaborales> filtrarLovReformasLaborales;
   private ReformasLaborales reformaLaboralLOVSeleccionada;
   //
   private ReformasLaborales reformaLaboralAClonar;
   //
   private String auxNombreClonar;
   private short auxCodigoClonar;
   //
   private String auxTipoPagoDetalle;
   private BigDecimal auxFactorDetalle;
   //
   private String infoRegistro, infoRegistroDetalle, infoRegistroLovReforma;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();
   DataTable tablaC, tablaD;

   public ControlReformaLaboral() {
      reformaLaboralSeleccionada = null;
      detalleReformaLSeleccionado = null;
      reformaLaboralAClonar = new ReformasLaborales();
      reformaLaboralLOVSeleccionada = new ReformasLaborales();
      lovReformasLaborales = null;
      altoTablaReforma = "150";
      altoTablaDetalles = "125";
      cambiosPagina = true;
      tipoActualizacion = -1;
      listaReformasLaborales = null;
      aceptar = true;
      listDetallesReformasLaboralesModificar = new ArrayList<DetallesReformasLaborales>();
      listDetallesReformasLaboralesBorrar = new ArrayList<DetallesReformasLaborales>();
      listDetallesReformasLaboralesCrear = new ArrayList<DetallesReformasLaborales>();
      //
      listReformasLaboralesCrear = new ArrayList<ReformasLaborales>();
      listReformasLaboralesBorrar = new ArrayList<ReformasLaborales>();
      listReformasLaboralesModificar = new ArrayList<ReformasLaborales>();
      k = 0;
      editarReformaLaboral = new ReformasLaborales();
      editarDetalleReformaLaboral = new DetallesReformasLaborales();
      cualCelda = -1;
      cualCeldaDetalles = -1;
      tipoListaDetalles = 0;
      tipoLista = 0;
      guardado = true;
      guardadoDetalles = true;
      nuevoReformaLaboral = new ReformasLaborales();
      nuevoDetalleReformaLaboral = new DetallesReformasLaborales();
      duplicarDetalleReformaLaboral = new DetallesReformasLaborales();
      duplicarReformaLaboral = new ReformasLaborales();
      bandera = 0;
      mapParametros.put("paginaAnterior", paginaAnterior);
   }

   @PostConstruct
   public void inicializarAdministrador() {
      try {
         FacesContext x = FacesContext.getCurrentInstance();
         HttpSession ses = (HttpSession) x.getExternalContext().getSession(false);
         administrarReformaLaboral.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
      } catch (Exception e) {
         System.out.println("Error postconstruct " + this.getClass().getName() + ": " + e);
         System.out.println("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina) {
      paginaAnterior = pagina;
      inicializarPagina();
   }

   public void recibirParametros(Map<String, Object> map) {
      mapParametros = map;
      paginaAnterior = (String) mapParametros.get("paginaAnterior");
      inicializarPagina();
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
         String pagActual = "reformalaboral";
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
      fc.getApplication().getNavigationHandler().handleNavigation(fc, null, pag);
   }

   public void inicializarPagina() {
      listaDetallesReformasLaborales = null;
      listaReformasLaborales = null;
      getListaReformasLaborales();
      if (listaReformasLaborales.size() >= 1) {
         getListaDetallesReformasLaborales();
      }
   }
//
//   public void posicionRegistroTablaReforma() {
//      listaDetallesReformasLaborales = null;
//      getListaDetallesReformasLaborales();
//      detalleReformaLSeleccionado = new DetallesReformasLaborales();
//      RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
//      restaurartablas();
//      contarRegistrosDetalles();
//   }

   public boolean validarCamposNulosReformaLaboral(int i) {
      boolean retorno = true;
      if (i == 0) {
         if (reformaLaboralSeleccionada.getNombre() != null) {
            if (reformaLaboralSeleccionada.getCodigo() <= 0 || reformaLaboralSeleccionada.getNombre().isEmpty()) {
               retorno = false;
            }
         } else {
            retorno = false;
         }
      } else if (i == 1) {
         if (nuevoReformaLaboral.getNombre() != null) {
            if (nuevoReformaLaboral.getCodigo() <= 0 || nuevoReformaLaboral.getNombre().isEmpty()) {
               retorno = false;
            }
         } else {
            retorno = false;
         }
      } else if (i == 2) {
         if (duplicarReformaLaboral.getNombre() != null) {
            if (duplicarReformaLaboral.getCodigo() <= 0 || duplicarReformaLaboral.getNombre().isEmpty()) {
               retorno = false;
            }
         } else {
            retorno = false;
         }
      }
      return retorno;
   }

   public boolean validarCamposNulosDetalleReformaLaboral(int i) {
      boolean retorno = true;
      if (i == 0) {
         if (detalleReformaLSeleccionado.getFactor() == null || detalleReformaLSeleccionado.getTipopago() == null) {
            retorno = false;
         }
      }
      if (i == 1) {
         if (nuevoDetalleReformaLaboral.getFactor() == null || nuevoDetalleReformaLaboral.getTipopago() == null) {
            retorno = false;
         }
      }
      if (i == 2) {
         if (duplicarDetalleReformaLaboral.getFactor() == null || duplicarDetalleReformaLaboral.getTipopago() == null) {
            retorno = false;
         }
      }
      return retorno;
   }

   public void modificaReformaLaboral(ReformasLaborales reformaLaboral) {
      reformaLaboralSeleccionada = reformaLaboral;
      boolean respuesta = validarCamposNulosReformaLaboral(0);
      if (respuesta == true) {
         modificarReformaLaboral(reformaLaboralSeleccionada);
      } else {
         reformaLaboralSeleccionada.setCodigo(auxCodigoReformaLaboral);
         reformaLaboralSeleccionada.setNombre(auxNombreReformaLaboral);
         RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullReformaLaboral').show()");
      }
   }

   public void modificarReformaLaboral(ReformasLaborales reformaLaboral) {
      reformaLaboralSeleccionada = reformaLaboral;
      int tamDes = reformaLaboralSeleccionada.getNombre().length();
      if (tamDes >= 1 && tamDes <= 30) {
         String textM = reformaLaboralSeleccionada.getNombre().toUpperCase();
         reformaLaboralSeleccionada.setNombre(textM);
         if (!listReformasLaboralesCrear.contains(reformaLaboralSeleccionada)) {
            if (listReformasLaboralesModificar.isEmpty()) {
               listReformasLaboralesModificar.add(reformaLaboralSeleccionada);
            } else if (!listReformasLaboralesModificar.contains(reformaLaboralSeleccionada)) {
               listReformasLaboralesModificar.add(reformaLaboralSeleccionada);
            }
            if (guardado == true) {
               guardado = false;
            }
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
      } else {
         reformaLaboralSeleccionada.setNombre(auxNombreReformaLaboral);
         RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
         RequestContext.getCurrentInstance().execute("PF('errorNombreReformaLaboral').show()");
      }

   }

   public void modificaDetalleReformaLaboral(DetallesReformasLaborales detalleRL) {
      detalleReformaLSeleccionado = detalleRL;
      boolean respuesta = validarCamposNulosDetalleReformaLaboral(0);
      if (respuesta == true) {
         modificarDetalleReformaLaboral(detalleReformaLSeleccionado);
      } else {
         detalleReformaLSeleccionado.setFactor(auxFactorDetalle);
         detalleReformaLSeleccionado.setTipopago(auxTipoPagoDetalle);
         RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullDetalleReforma').show()");
      }
   }

   public void modificarDetalleReformaLaboral(DetallesReformasLaborales detalleRL) {
      detalleReformaLSeleccionado = detalleRL;
      int tam = 0;
      tam = detalleReformaLSeleccionado.getTipopago().length();
      if (tam >= 1 && tam <= 10) {
         if (!listDetallesReformasLaboralesCrear.contains(detalleReformaLSeleccionado)) {
            if (listDetallesReformasLaboralesModificar.isEmpty()) {
               listDetallesReformasLaboralesModificar.add(detalleReformaLSeleccionado);
            } else if (!listDetallesReformasLaboralesModificar.contains(detalleReformaLSeleccionado)) {
               listDetallesReformasLaboralesModificar.add(detalleReformaLSeleccionado);
            }
            if (guardadoDetalles == true) {
               guardadoDetalles = false;
            }
         }
         cambiosPagina = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
      } else {
         detalleReformaLSeleccionado.setTipopago(auxTipoPagoDetalle);
         RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
         RequestContext.getCurrentInstance().execute("PF('errorTipoPagoDetalleReforma').show()");
      }
   }

   public void cambiarIndice(ReformasLaborales reformaLaboral, int celda) {
      reformaLaboralSeleccionada = reformaLaboral;
      if (guardadoDetalles == true) {
         cualCelda = celda;
         auxCodigoReformaLaboral = reformaLaboralSeleccionada.getCodigo();
         auxNombreReformaLaboral = reformaLaboralSeleccionada.getNombre();
         listaDetallesReformasLaborales = null;
         getListaDetallesReformasLaborales();
         RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
         RequestContext.getCurrentInstance().execute("PF('datosDetalleReformaLaboral').unselectAllRows()");
         contarRegistrosDetalles();
         detalleReformaLSeleccionado = null;
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void cambiarIndiceDetalle(DetallesReformasLaborales detalleRL, int celda) {
      System.out.println("cambiarIndiceDetalle() detalleRL: " + detalleRL + ", celda: " + celda);
      detalleReformaLSeleccionado = detalleRL;
      cualCeldaDetalles = celda;
      auxFactorDetalle = detalleReformaLSeleccionado.getFactor();
      auxTipoPagoDetalle = detalleReformaLSeleccionado.getTipopago();
      restaurartablas();
   }
   //GUARDAR

   public void guardarYSalir() {
      guardarGeneral();
      salir();
   }

   public void cancelarYSalir() {
      cancelarModificacionGeneral();
      salir();
   }

   public void guardarGeneral() {
      if (guardado == false || guardadoDetalles == false) {
         if (guardado == false) {
            guardarCambiosReformaLaboral();
         }
         if (guardadoDetalles == false) {
            guardarCambiosDetalleReformaLaboral();
         }
         cambiosPagina = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void guardarCambiosReformaLaboral() {
      try {
         if (!listReformasLaboralesBorrar.isEmpty()) {
            for (int i = 0; i < listReformasLaboralesBorrar.size(); i++) {
               administrarReformaLaboral.borrarReformaLaboral(listReformasLaboralesBorrar);
            }
            listReformasLaboralesBorrar.clear();
         }
         if (!listReformasLaboralesCrear.isEmpty()) {
            for (int i = 0; i < listReformasLaboralesCrear.size(); i++) {
               administrarReformaLaboral.crearReformaLaboral(listReformasLaboralesCrear);
            }
            listReformasLaboralesCrear.clear();
         }
         if (!listReformasLaboralesModificar.isEmpty()) {
            administrarReformaLaboral.editarReformaLaboral(listReformasLaboralesModificar);
            listReformasLaboralesModificar.clear();
         }
         listaReformasLaborales = null;
         RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
         guardado = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;

         reformaLaboralSeleccionada = null;
         FacesMessage msg = new FacesMessage("Información", "Los datos de Reforma Laboral se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosReformaLaboral : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en el guardado de Reforma Laboral, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }

   public void guardarCambiosDetalleReformaLaboral() {
      try {
         if (!listDetallesReformasLaboralesBorrar.isEmpty()) {
            for (int i = 0; i < listDetallesReformasLaboralesBorrar.size(); i++) {
               administrarReformaLaboral.borrarDetalleReformaLaboral(listDetallesReformasLaboralesBorrar);
            }
            listDetallesReformasLaboralesBorrar.clear();
         }
         if (!listDetallesReformasLaboralesCrear.isEmpty()) {
            for (int i = 0; i < listDetallesReformasLaboralesCrear.size(); i++) {
               administrarReformaLaboral.crearDetalleReformaLaboral(listDetallesReformasLaboralesCrear);
            }
            listDetallesReformasLaboralesCrear.clear();
         }
         if (!listDetallesReformasLaboralesModificar.isEmpty()) {
            administrarReformaLaboral.editarDetalleReformaLaboral(listDetallesReformasLaboralesModificar);
            listDetallesReformasLaboralesModificar.clear();
         }
         listaDetallesReformasLaborales = null;
         RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
         guardadoDetalles = true;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
         k = 0;

         FacesMessage msg = new FacesMessage("Información", "Los datos de Detalle Reforma Laboral se guardaron con Éxito.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      } catch (Exception e) {
         System.out.println("Error guardarCambiosDetalleReformaLaboral : " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Ocurrio un error en el guardado de Detalle Reforma Laboral, intente nuevamente.");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }
   }
   //CANCELAR MODIFICACIONES

   /**
    * Cancela las modificaciones realizas en la pagina
    */
   public void cancelarModificacionGeneral() {
      cancelarModificacionReformaLaboral();
      cancelarModificacionDetalleReformaLaboral();
      cancelarProcesoClonado();
      RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
      RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
      contarRegistros();
      contarRegistrosDetalles();
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
   }

   public void cancelarProcesoClonado() {
      codigoReformaClonar = 0;
      nombreReformaClonar = " ";
      reformaLaboralAClonar = new ReformasLaborales();
      RequestContext context = RequestContext.getCurrentInstance();
      context.update("form:codigoReformaLaboralClonar");
      context.update("form:nombreReformaLaboralClonar");
      context.update("form:codigoReformaLaboralClonarBase");
      context.update("form:nombreReformaLaboralClonarBase");
   }

   public void cancelarModificacionReformaLaboral() {
      listReformasLaboralesBorrar.clear();
      listReformasLaboralesCrear.clear();
      listReformasLaboralesModificar.clear();
      reformaLaboralSeleccionada = null;
      listaReformasLaborales = null;
      k = 0;
      guardado = true;
      restaurartablas();
      RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
   }

   public void cancelarModificacionDetalleReformaLaboral() {
      listDetallesReformasLaboralesBorrar.clear();
      listDetallesReformasLaboralesCrear.clear();
      listDetallesReformasLaboralesModificar.clear();
      detalleReformaLSeleccionado = null;
      listaDetallesReformasLaborales = null;
      k = 0;
      guardadoDetalles = true;
      restaurartablas();
      RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
   }

   public void editarCelda() {
      if (detalleReformaLSeleccionado != null) {
         editarDetalleReformaLaboral = detalleReformaLSeleccionado;
         if (cualCeldaDetalles == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarTipoPagoDetalleD");
            RequestContext.getCurrentInstance().execute("PF('editarTipoPagoDetalleD').show()");
            cualCeldaDetalles = -1;
         } else if (cualCeldaDetalles == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFactorDetalleD");
            RequestContext.getCurrentInstance().execute("PF('editarFactorDetalleD').show()");
            cualCeldaDetalles = -1;
         }
      } else if (reformaLaboralSeleccionada != null) {
         editarReformaLaboral = reformaLaboralSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCodigoReformaLaboralD");
            RequestContext.getCurrentInstance().execute("PF('editarCodigoReformaLaboralD').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarNombreReformaLaboralD");
            RequestContext.getCurrentInstance().execute("PF('editarNombreReformaLaboralD').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void dialogoNuevoRegistro() {
      if (listaReformasLaborales.size() == 0 || listaDetallesReformasLaborales.size() == 0) {
         RequestContext.getCurrentInstance().update("formularioDialogos:verificarNuevoRegistro");
         RequestContext.getCurrentInstance().execute("PF('verificarNuevoRegistro').show()");
      } else {
         if (reformaLaboralSeleccionada != null) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroReformaLaboral");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroReformaLaboral').show()");
         }
         if (detalleReformaLSeleccionado != null) {
            RequestContext.getCurrentInstance().update("formularioDialogos:NuevoRegistroDetalleReformaLaboral");
            RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDetalleReformaLaboral').show()");
         }
      }
   }

   //CREAR 
   public void agregarNuevoReformaLaboral() {
      boolean respueta = validarCamposNulosReformaLaboral(1);
      if (respueta == true) {
         int tamDes = 0;
         if (nuevoReformaLaboral.getNombre() != null) {
            tamDes = nuevoReformaLaboral.getNombre().length();
         }
         if (tamDes >= 1 && tamDes <= 30) {
            restaurartablas();

            k++;
            l = BigInteger.valueOf(k);
            String text = nuevoReformaLaboral.getNombre().toUpperCase();
            nuevoReformaLaboral.setNombre(text);
            nuevoReformaLaboral.setSecuencia(l);
            listReformasLaboralesCrear.add(nuevoReformaLaboral);
            listaReformasLaborales.add(nuevoReformaLaboral);
            reformaLaboralSeleccionada = listaReformasLaborales.get(listaReformasLaborales.indexOf(nuevoReformaLaboral));
            nuevoReformaLaboral = new ReformasLaborales();
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:ACEPTAR");
            context.update("form:datosReformaLaboral");
            contarRegistros();
            context.execute("PF('NuevoRegistroReformaLaboral').hide()");
            if (guardado == true) {
               guardado = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorNombreReformaLaboral').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullReformaLaboral').show()");
      }
   }

   public void agregarNuevoDetalleReformaLaboral() {
      boolean respueta = validarCamposNulosDetalleReformaLaboral(1);
      if (respueta == true) {
         int tamDes = 0;
         if (nuevoDetalleReformaLaboral.getTipopago() != null) {
            tamDes = nuevoDetalleReformaLaboral.getTipopago().length();
         }
         if (tamDes >= 1 && tamDes <= 10) {
            restaurartablas();
            k++;
            l = BigInteger.valueOf(k);
            nuevoDetalleReformaLaboral.setSecuencia(l);
            nuevoDetalleReformaLaboral.setReformalaboral(reformaLaboralSeleccionada);
            if (listaDetallesReformasLaborales.size() == 0) {
               listaDetallesReformasLaborales = new ArrayList<DetallesReformasLaborales>();
            }
            listDetallesReformasLaboralesCrear.add(nuevoDetalleReformaLaboral);
            listaDetallesReformasLaborales.add(nuevoDetalleReformaLaboral);
            detalleReformaLSeleccionado = listaDetallesReformasLaborales.get(listaDetallesReformasLaborales.indexOf(nuevoDetalleReformaLaboral));
            cambiosPagina = false;
            RequestContext context = RequestContext.getCurrentInstance();
            context.update("form:ACEPTAR");
            context.update("form:datosDetalleReformaLaboral");
            contarRegistrosDetalles();
            context.execute("PF('NuevoRegistroDetalleReformaLaboral').hide()");
            nuevoDetalleReformaLaboral = new DetallesReformasLaborales();
            if (guardadoDetalles == true) {
               guardadoDetalles = false;
               RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorTipoPagoDetalleReforma').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullDetalleReforma').show()");
      }
   }
   //LIMPIAR NUEVO REGISTRO

   /**
    */
   public void limpiarNuevaReformaLaboral() {
      nuevoReformaLaboral = new ReformasLaborales();
      reformaLaboralSeleccionada = null;
   }

   public void limpiarNuevaDetalleReformaLaboral() {
      nuevoDetalleReformaLaboral = new DetallesReformasLaborales();
   }

   //DUPLICAR VC
   /**
    */
   public void verificarRegistroDuplicar() {
      System.out.println("ControlReformaLaboral.verificarRegistroDuplicar() detalleReformaLSeleccionado : " + detalleReformaLSeleccionado);
      if (detalleReformaLSeleccionado != null) {
         duplicarDetalleReformaLaboralM();
      } else if (reformaLaboralSeleccionada != null) {
         duplicarReformaLaboralM();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicarReformaLaboralM() {
      if (reformaLaboralSeleccionada != null) {
         duplicarReformaLaboral = new ReformasLaborales();
         k++;
         l = BigInteger.valueOf(k);
         duplicarReformaLaboral.setSecuencia(l);
         duplicarReformaLaboral.setCodigo(reformaLaboralSeleccionada.getCodigo());
         duplicarReformaLaboral.setNombre(reformaLaboralSeleccionada.getNombre());
         duplicarReformaLaboral.setIntegral(reformaLaboralSeleccionada.getIntegral());
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroReformaLaboral");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroReformaLaboral').show()");
         reformaLaboralSeleccionada = null;
      }
   }

   public void duplicarDetalleReformaLaboralM() {
      if (detalleReformaLSeleccionado != null) {
         duplicarDetalleReformaLaboral = new DetallesReformasLaborales();

         duplicarDetalleReformaLaboral.setFactor(detalleReformaLSeleccionado.getFactor());
         duplicarDetalleReformaLaboral.setTipopago(detalleReformaLSeleccionado.getTipopago());
         RequestContext.getCurrentInstance().update("formularioDialogos:DuplicarRegistroDetalleReformaLaboral");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetalleReformaLaboral').show()");
      }
   }

   /**
    * Metodo que confirma el duplicado y actualiza los datos de la tabla Sets
    */
   public void confirmarDuplicarReformaLaboral() {
      boolean respueta = validarCamposNulosReformaLaboral(2);
      if (respueta == true) {
         int tamDes = 0;
         if (nuevoReformaLaboral.getNombre() != null) {
            tamDes = nuevoReformaLaboral.getNombre().length();
         }
         if (tamDes >= 1 && tamDes <= 30) {
            k++;
            l = BigInteger.valueOf(k);
            duplicarDetalleReformaLaboral.setSecuencia(l);
            if (duplicarReformaLaboral.getNombre() != null) {
               String text = duplicarReformaLaboral.getNombre().toUpperCase();
               duplicarReformaLaboral.setNombre(text);
            }
            listaReformasLaborales.add(duplicarReformaLaboral);
            listReformasLaboralesCrear.add(duplicarReformaLaboral);
            detalleReformaLSeleccionado = listaDetallesReformasLaborales.get(listaDetallesReformasLaborales.indexOf(duplicarReformaLaboral));
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
            contarRegistros();
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroReformaLaboral').hide()");
            reformaLaboralSeleccionada = null;
            if (guardado == true) {
               guardado = false;
               //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            restaurartablas();
            duplicarReformaLaboral = new ReformasLaborales();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorNombreReformaLaboral').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullReformaLaboral').show()");
      }
   }

   public void confirmarDuplicarDetalleReformaLaboral() {
      boolean respueta = validarCamposNulosDetalleReformaLaboral(2);
      if (respueta == true) {
         int tamDes = 0;
         if (nuevoDetalleReformaLaboral.getTipopago() != null) {
            tamDes = nuevoDetalleReformaLaboral.getTipopago().length();
         }
         if (tamDes >= 1 && tamDes <= 10) {
            duplicarDetalleReformaLaboral.setReformalaboral(reformaLaboralSeleccionada);
            listaDetallesReformasLaborales.add(duplicarDetalleReformaLaboral);
            listDetallesReformasLaboralesCrear.add(duplicarDetalleReformaLaboral);
            detalleReformaLSeleccionado = listaDetallesReformasLaborales.get(listaDetallesReformasLaborales.indexOf(duplicarDetalleReformaLaboral));
            cambiosPagina = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
            contarRegistrosDetalles();
            RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDetalleReformaLaboral').hide()");
            if (guardadoDetalles == true) {
               guardadoDetalles = false;
               //RequestContext.getCurrentInstance().update("form:ACEPTAR");
            }
            restaurartablas();
            duplicarDetalleReformaLaboral = new DetallesReformasLaborales();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorTipoPagoDetalleReforma').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorDatosNullDetalleReforma').show()");
      }
   }

   //LIMPIAR DUPLICAR
   /**
    * Metodo que limpia los datos de un duplicar Set
    */
   public void limpiarDuplicarReformaLaboral() {
      duplicarReformaLaboral = new ReformasLaborales();
   }

   public void limpiarDuplicarDetalleReformaLaboral() {
      duplicarDetalleReformaLaboral = new DetallesReformasLaborales();
   }

   public void limpiarMSNRastros() {
      msnConfirmarRastro = "";
      msnConfirmarRastroHistorico = "";
      nombreTablaRastro = "";
   }

   //BORRAR VC
   /**
    */
   public void verificarRegistroBorrar() {
      if (detalleReformaLSeleccionado != null) {
         borrarDetalleReformaLaboral();
      } else if (reformaLaboralSeleccionada != null) {
         if (listaDetallesReformasLaborales.isEmpty()) {
            borrarReformaLaboral();
         } else {
            RequestContext.getCurrentInstance().execute("PF('errorBorrarRegistro').show()");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void borrarReformaLaboral() {
      if (!listReformasLaboralesModificar.isEmpty() && listReformasLaboralesModificar.contains(reformaLaboralSeleccionada)) {
         int modIndex = listReformasLaboralesModificar.indexOf(reformaLaboralSeleccionada);
         listReformasLaboralesModificar.remove(modIndex);
         listReformasLaboralesBorrar.add(reformaLaboralSeleccionada);
      } else if (!listReformasLaboralesCrear.isEmpty() && listReformasLaboralesCrear.contains(reformaLaboralSeleccionada)) {
         int crearIndex = listReformasLaboralesCrear.indexOf(reformaLaboralSeleccionada);
         listReformasLaboralesCrear.remove(crearIndex);
      } else {
         listReformasLaboralesBorrar.add(reformaLaboralSeleccionada);
      }
      if (tipoLista == 1) {
         listaReformasLaborales.remove(reformaLaboralSeleccionada);
      }

      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosReformaLaboral");
      reformaLaboralSeleccionada = null;

      if (guardado == true) {
         guardado = false;
         //RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }

   public void borrarDetalleReformaLaboral() {
      if (!listDetallesReformasLaboralesModificar.isEmpty() && listDetallesReformasLaboralesModificar.contains(detalleReformaLSeleccionado)) {
         int modIndex = listDetallesReformasLaboralesModificar.indexOf(detalleReformaLSeleccionado);
         listDetallesReformasLaboralesModificar.remove(modIndex);
         listDetallesReformasLaboralesBorrar.add(detalleReformaLSeleccionado);
      } else if (!listDetallesReformasLaboralesCrear.isEmpty() && listDetallesReformasLaboralesCrear.contains(detalleReformaLSeleccionado)) {
         int crearIndex = listDetallesReformasLaboralesCrear.indexOf(detalleReformaLSeleccionado);
         listDetallesReformasLaboralesCrear.remove(crearIndex);
      } else {
         listDetallesReformasLaboralesBorrar.add(detalleReformaLSeleccionado);
      }
      if (tipoListaDetalles == 1) {
         listaDetallesReformasLaborales.remove(detalleReformaLSeleccionado);
      }

      cambiosPagina = false;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
      detalleReformaLSeleccionado = null;

      if (guardadoDetalles == true) {
         guardadoDetalles = false;
         //RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
   }
   //CTRL + F11 ACTIVAR/DESACTIVAR

   /**
    * Metodo que activa el filtrado por medio de la opcion en el tollbar o por
    * medio de la tecla Crtl+F11
    */
   public void activarCtrlF11() {
      if (bandera == 0) {
         altoTablaReforma = "130";
         reformaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaCodigo");
         reformaCodigo.setFilterStyle("width: 85% !important;");
         reformaNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaNombre");
         reformaNombre.setFilterStyle("width: 85% !important;");
         reformaIntegral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaIntegral");
         reformaIntegral.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosReformaLaboral");

         altoTablaDetalles = "105";
         detalleFactor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleFactor");
         detalleFactor.setFilterStyle("width: 85% !important;");
         detalleTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleTipoPago");
         detalleTipoPago.setFilterStyle("width: 85% !important;");
         RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
         bandera = 1;
      } else {
         restaurartablas();
      }
   }

   //SALIR
   /**
    * Metodo que cierra la sesion y limpia los datos en la pagina
    */
   public void salir() {
      restaurartablas();

      listReformasLaboralesBorrar.clear();
      listReformasLaboralesCrear.clear();
      listReformasLaboralesModificar.clear();
      listDetallesReformasLaboralesBorrar.clear();
      listDetallesReformasLaboralesCrear.clear();
      listDetallesReformasLaboralesModificar.clear();
      reformaLaboralSeleccionada = null;
      detalleReformaLSeleccionado = null;
      k = 0;
      listaReformasLaborales = null;
      listaDetallesReformasLaborales = null;
      guardado = true;
      guardadoDetalles = true;
      cambiosPagina = true;
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      reformaLaboralSeleccionada = null;
      detalleReformaLSeleccionado = null;
      navegar("atras");
   }

   public void restaurartablas() {
      if (bandera == 1) {
         altoTablaReforma = "150";
         reformaCodigo = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaCodigo");
         reformaCodigo.setFilterStyle("display: none; visibility: hidden;");
         reformaNombre = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaNombre");
         reformaNombre.setFilterStyle("display: none; visibility: hidden;");
         reformaIntegral = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosReformaLaboral:reformaIntegral");
         reformaIntegral.setFilterStyle("display: none; visibility: hidden;");
         bandera = 0;
         filtrarListaReformasLaborales = null;
         tipoLista = 0;
         RequestContext.getCurrentInstance().update("form:datosReformaLaboral");

         altoTablaDetalles = "125";
         detalleFactor = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleFactor");
         detalleFactor.setFilterStyle("display: none; visibility: hidden;");
         detalleTipoPago = (Column) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:datosDetalleReformaLaboral:detalleTipoPago");
         detalleTipoPago.setFilterStyle("display: none; visibility: hidden;");
         filtrarListaDetallesReformasLaborales = null;
         tipoListaDetalles = 0;
         RequestContext.getCurrentInstance().update("form:datosDetalleReformaLaboral");
      }
   }

   /**
    * Metodo que activa el boton aceptar de la pantalla y dialogos
    */
   public void activarAceptar() {
      aceptar = false;
   }
   //EXPORTAR

   public String exportXML() {
      if (detalleReformaLSeleccionado != null) {
         nombreTabla = ":formExportarDRL:datosDetalleReformaLaboralExportar";
         nombreXML = "DetallesReformasLaborales_XML";
      } else if (reformaLaboralSeleccionada != null) {
         nombreTabla = ":formExportarRL:datosReformaLaboralExportar";
         nombreXML = "ReformasLaborales_XML";
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
      return nombreTabla;
   }

   /**
    * Metodo que exporta datos a PDF
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportPDF() throws IOException {
      if (detalleReformaLSeleccionado != null) {
         exportPDF_DRL();
      } else if (reformaLaboralSeleccionada != null) {
         exportPDF_RL();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void exportPDF_RL() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarRL:datosReformaLaboralExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "ReformasLaborales_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
      reformaLaboralSeleccionada = null;
   }

   public void exportPDF_DRL() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarDRL:datosDetalleReformaLaboralExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "DetallesReformasLaborales_PDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   /**
    * Metodo que exporta datos a XLS
    *
    * @throws IOException Excepcion de In-Out de datos
    */
   public void validarExportXLS() throws IOException {
      if (detalleReformaLSeleccionado != null) {
         exportXLS_DRL();
      } else if (reformaLaboralSeleccionada != null) {
         exportXLS_RL();
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void exportXLS_RL() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarRL:datosReformaLaboralExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "ReformasLaborales_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS_DRL() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportarDRL:datosDetalleReformaLaboralExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "DetallesReformasLaborales_XLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }
   //EVENTO FILTRAR

   /**
    * Evento que cambia la lista reala a la filtrada
    */
   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      reformaLaboralSeleccionada = null;
      contarRegistros();
   }

   public void eventoFiltrarDet() {
      if (tipoListaDetalles == 0) {
         tipoListaDetalles = 1;
      }
      detalleReformaLSeleccionado = null;
      contarRegistrosDetalles();
   }
   //RASTRO - COMPROBAR SI LA TABLA TIENE RASTRO ACTIVO

   public void verificarRastro() {
      if (listaDetallesReformasLaborales == null || listaReformasLaborales == null) {
         RequestContext.getCurrentInstance().execute("PF('verificarRastrosTablas').show()");
      } else if (reformaLaboralSeleccionada != null) {
         verificarRastroReformaLaboral();
      } else if (detalleReformaLSeleccionado != null) {
         verificarRastroDetalleReformaLaboral();
      }
   }

   public void verificarRastroReformaLaboral() {
      if (reformaLaboralSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(reformaLaboralSeleccionada.getSecuencia(), "REFORMASLABORALES");
         backUp = reformaLaboralSeleccionada.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "ReformasLaborales";
            msnConfirmarRastro = "La tabla REFORMASLABORALES tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
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
      } else if (administrarRastros.verificarHistoricosTabla("REFORMASLABORALES")) {
         nombreTablaRastro = "ReformasLaborales";
         msnConfirmarRastroHistorico = "La tabla REFORMASLABORALES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void verificarRastroDetalleReformaLaboral() {
      if (detalleReformaLSeleccionado != null) {
         int resultado = administrarRastros.obtenerTabla(detalleReformaLSeleccionado.getSecuencia(), "DETALLESREFORMASLABORALES");
         backUp = detalleReformaLSeleccionado.getSecuencia();
         if (resultado == 1) {
            RequestContext.getCurrentInstance().execute("PF('errorObjetosDB').show()");
         } else if (resultado == 2) {
            nombreTablaRastro = "DetallesReformasLaborales";
            msnConfirmarRastro = "La tabla DETALLESREFORMASLABORALES tiene rastros para el registro seleccionado, ¿desea continuar?";
            RequestContext.getCurrentInstance().update("form:msnConfirmarRastro");
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
      } else if (administrarRastros.verificarHistoricosTabla("DETALLESREFORMASLABORALES")) {
         nombreTablaRastro = "DetallesReformasLaborales";
         msnConfirmarRastroHistorico = "La tabla DETALLESREFORMASLABORALES tiene rastros historicos, ¿Desea continuar?";
         RequestContext.getCurrentInstance().update("form:confirmarRastroHistorico");
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public boolean validarCodigoNuevoClonado() {
      boolean retorno = true;
      int conteo = 0;
      for (int i = 0; i < lovReformasLaborales.size(); i++) {
         if (lovReformasLaborales.get(i).getCodigo() == codigoReformaClonar) {
            conteo++;
         }
      }
      if (conteo > 0) {
         retorno = false;
      }
      return retorno;
   }

   public void clonarReformaLaboral() {
      if ((nombreReformaClonar.isEmpty()) || (codigoReformaClonar <= 0) || (reformaLaboralAClonar.getSecuencia() == null)) {
         RequestContext.getCurrentInstance().update("form:errorClonadoReforma");
         RequestContext.getCurrentInstance().execute("PF('errorClonadoReforma').show()");
      } else if (validarCodigoNuevoClonado() == true) {
         RequestContext.getCurrentInstance().update("form:continuarOperacionClonado");
         RequestContext.getCurrentInstance().execute("PF('continuarOperacionClonado').show()");
      } else {
         RequestContext.getCurrentInstance().update("form:errorCodigoClonado");
         RequestContext.getCurrentInstance().execute("PF('errorCodigoClonado').show()");
      }
   }

   public void dispararDialogoClonarReformaLaboral() {
      RequestContext.getCurrentInstance().update("form:ReformaLaboralDialogo");
      RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').show()");
   }

   public void posicionReformaLaboralClonar() {
      if (guardado == true) {
         auxCodigoClonar = reformaLaboralAClonar.getCodigo();
         auxNombreClonar = reformaLaboralAClonar.getNombre();
      } else {
         RequestContext.getCurrentInstance().execute("PF('confirmarGuardar').show()");
      }
   }

   public void seleccionarReformaLaboralClonar() {
      reformaLaboralAClonar = reformaLaboralLOVSeleccionada;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:codigoReformaLaboralClonarBase");
      RequestContext.getCurrentInstance().update("form:nombreReformaLaboralClonarBase");
      reformaLaboralLOVSeleccionada = null;
      filtrarLovReformasLaborales = null;

      context.reset("form:lovReformaLaboral:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReformaLaboral').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ReformaLaboralDialogo");
      RequestContext.getCurrentInstance().update("form:lovReformaLaboral");
      RequestContext.getCurrentInstance().update("form:aceptarRL");
   }

   public void cancelarReformaLaboralClonar() {
      reformaLaboralLOVSeleccionada = null;
      filtrarLovReformasLaborales = null;
      RequestContext context = RequestContext.getCurrentInstance();
      context.reset("form:lovReformaLaboral:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('lovReformaLaboral').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ReformaLaboralDialogo').hide()");
      RequestContext.getCurrentInstance().update("form:ReformaLaboralDialogo");
      RequestContext.getCurrentInstance().update("form:lovReformaLaboral");
      RequestContext.getCurrentInstance().update("form:aceptarRL");
   }

   public void autoCompletarSeleccionarReformaLaboralClonar(String valorConfirmar, int tipoAutoCompletar) {
      int coincidencias = 0;
      int indiceUnicoElemento = 0;
      if (tipoAutoCompletar == 0) {
         short num = Short.parseShort(valorConfirmar);
         for (int i = 0; i < lovReformasLaborales.size(); i++) {
            if (lovReformasLaborales.get(i).getCodigo() == num) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            reformaLaboralAClonar = lovReformasLaborales.get(indiceUnicoElemento);
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            reformaLaboralAClonar.setCodigo(auxCodigoClonar);
            reformaLaboralAClonar.setNombre(auxNombreClonar);
            context.update("form:codigoReformaLaboralClonarBase");
            context.update("form:nombreReformaLaboralClonarBase");
            context.update("form:ReformaLaboralDialogo");
            context.execute("PF('ReformaLaboralDialogo').show()");
         }
      }
      if (tipoAutoCompletar == 1) {
         for (int i = 0; i < lovReformasLaborales.size(); i++) {
            if (lovReformasLaborales.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
               indiceUnicoElemento = i;
               coincidencias++;
            }
         }
         if (coincidencias == 1) {
            reformaLaboralAClonar = lovReformasLaborales.get(indiceUnicoElemento);
         } else {
            RequestContext context = RequestContext.getCurrentInstance();
            reformaLaboralAClonar.setCodigo(auxCodigoClonar);
            reformaLaboralAClonar.setNombre(auxNombreClonar);
            context.update("form:codigoReformaLaboralClonarBase");
            context.update("form:nombreReformaLaboralClonarBase");
            context.update("form:TipoDiaDialogo");
            context.execute("PF('TipoDiaDialogo').show()");
         }
      }
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:informacionRegistro");
   }

   public void contarRegistrosDetalles() {
      RequestContext.getCurrentInstance().update("form:informacionRegistroDetalle");
   }

   public void contarRegistrosLovRef() {
      RequestContext.getCurrentInstance().update("form:infoRegistroLovReforma");
   }

   public void recordarSeleccion() {
      if (reformaLaboralSeleccionada != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaC = (DataTable) c.getViewRoot().findComponent("form:datosReformaLaboral");
         tablaC.setSelection(reformaLaboralSeleccionada);
      }
   }

   public void recordarSeleccionDetalles() {
      if (detalleReformaLSeleccionado != null) {
         FacesContext c = FacesContext.getCurrentInstance();
         tablaD = (DataTable) c.getViewRoot().findComponent("form:datosDetalleReformaLaboral");
         tablaD.setSelection(detalleReformaLSeleccionado);
      }
   }

   //GETTERS AND SETTERS
   public List<ReformasLaborales> getListaReformasLaborales() {
      try {
         if (listaReformasLaborales == null) {
            listaReformasLaborales = administrarReformaLaboral.listaReformasLaborales();
            return listaReformasLaborales;
         } else {
            return listaReformasLaborales;
         }
      } catch (Exception e) {
         System.out.println("Error...!! getListaReformasLaborales " + e.toString());
         return null;
      }
   }

   public void setListaReformasLaborales(List<ReformasLaborales> setListaReformasLaborales) {
      this.listaReformasLaborales = setListaReformasLaborales;
   }

   public List<ReformasLaborales> getFiltrarListaReformasLaborales() {
      return filtrarListaReformasLaborales;
   }

   public void setFiltrarListaReformasLaborales(List<ReformasLaborales> setFiltrarListaReformasLaborales) {
      this.filtrarListaReformasLaborales = setFiltrarListaReformasLaborales;
   }

   public ReformasLaborales getNuevoReformaLaboral() {
      return nuevoReformaLaboral;
   }

   public void setNuevoReformaLaboral(ReformasLaborales setNuevoReformaLaboral) {
      this.nuevoReformaLaboral = setNuevoReformaLaboral;
   }

   public boolean isAceptar() {
      return aceptar;
   }

   public ReformasLaborales getEditarReformaLaboral() {
      return editarReformaLaboral;
   }

   public void setEditarReformaLaboral(ReformasLaborales setEditarReformaLaboral) {
      this.editarReformaLaboral = setEditarReformaLaboral;
   }

   public ReformasLaborales getDuplicarReformaLaboral() {
      return duplicarReformaLaboral;
   }

   public void setDuplicarReformaLaboral(ReformasLaborales setDuplicarReformaLaboral) {
      this.duplicarReformaLaboral = setDuplicarReformaLaboral;
   }

   public List<DetallesReformasLaborales> getListaDetallesReformasLaborales() {
      if (listaDetallesReformasLaborales == null && reformaLaboralSeleccionada != null) {
         listaDetallesReformasLaborales = administrarReformaLaboral.listaDetalleReformasLaborales(reformaLaboralSeleccionada.getSecuencia());
      }
      return listaDetallesReformasLaborales;
   }

   public void setListaDetallesReformasLaborales(List<DetallesReformasLaborales> setListaDetallesReformasLaborales) {
      this.listaDetallesReformasLaborales = setListaDetallesReformasLaborales;
   }

   public List<DetallesReformasLaborales> getFiltrarListaDetallesReformasLaborales() {
      return filtrarListaDetallesReformasLaborales;
   }

   public void setFiltrarListaDetallesReformasLaborales(List<DetallesReformasLaborales> setFiltrarListaDetallesReformasLaborales) {
      this.filtrarListaDetallesReformasLaborales = setFiltrarListaDetallesReformasLaborales;
   }

   public List<ReformasLaborales> getListReformasLaboralesModificar() {
      return listReformasLaboralesModificar;
   }

   public void setListReformasLaboralesModificar(List<ReformasLaborales> setListReformasLaboralesModificar) {
      this.listReformasLaboralesModificar = setListReformasLaboralesModificar;
   }

   public List<ReformasLaborales> getListReformasLaboralesCrear() {
      return listReformasLaboralesCrear;
   }

   public void setListReformasLaboralesCrear(List<ReformasLaborales> setListReformasLaboralesCrear) {
      this.listReformasLaboralesCrear = setListReformasLaboralesCrear;
   }

   public List<ReformasLaborales> getListReformasLaboralesBorrar() {
      return listReformasLaboralesBorrar;
   }

   public void setListReformasLaboralesBorrar(List<ReformasLaborales> setListReformasLaboralesBorrar) {
      this.listReformasLaboralesBorrar = setListReformasLaboralesBorrar;
   }

   public List<DetallesReformasLaborales> getListDetallesReformasLaboralesModificar() {
      return listDetallesReformasLaboralesModificar;
   }

   public void setListDetallesReformasLaboralesModificar(List<DetallesReformasLaborales> setListDetallesReformasLaboralesModificar) {
      this.listDetallesReformasLaboralesModificar = setListDetallesReformasLaboralesModificar;
   }

   public DetallesReformasLaborales getNuevoDetalleReformaLaboral() {
      return nuevoDetalleReformaLaboral;
   }

   public void setNuevoDetalleReformaLaboral(DetallesReformasLaborales setNuevoDetalleReformaLaboral) {
      this.nuevoDetalleReformaLaboral = setNuevoDetalleReformaLaboral;
   }

   public List<DetallesReformasLaborales> getListDetallesReformasLaboralesCrear() {
      return listDetallesReformasLaboralesCrear;
   }

   public void setListDetallesReformasLaboralesCrear(List<DetallesReformasLaborales> setListDetallesReformasLaboralesCrear) {
      this.listDetallesReformasLaboralesCrear = setListDetallesReformasLaboralesCrear;
   }

   public List<DetallesReformasLaborales> getListDetallesReformasLaboralesBorrar() {
      return listDetallesReformasLaboralesBorrar;
   }

   public void setListDetallesReformasLaboralesBorrar(List<DetallesReformasLaborales> setListDetallesReformasLaboralesBorrar) {
      this.listDetallesReformasLaboralesBorrar = setListDetallesReformasLaboralesBorrar;
   }

   public DetallesReformasLaborales getEditarDetalleReformaLaboral() {
      return editarDetalleReformaLaboral;
   }

   public void setEditarDetalleReformaLaboral(DetallesReformasLaborales setEditarDetalleReformaLaboral) {
      this.editarDetalleReformaLaboral = setEditarDetalleReformaLaboral;
   }

   public DetallesReformasLaborales getDuplicarDetalleReformaLaboral() {
      return duplicarDetalleReformaLaboral;
   }

   public void setDuplicarDetalleReformaLaboral(DetallesReformasLaborales setDuplicarDetalleReformaLaboral) {
      this.duplicarDetalleReformaLaboral = setDuplicarDetalleReformaLaboral;
   }

   public String getMsnConfirmarRastro() {
      return msnConfirmarRastro;
   }

   public void setMsnConfirmarRastro(String msnConfirmarRastro) {
      this.msnConfirmarRastro = msnConfirmarRastro;
   }

   public String getMsnConfirmarRastroHistorico() {
      return msnConfirmarRastroHistorico;
   }

   public void setMsnConfirmarRastroHistorico(String msnConfirmarRastroHistorico) {
      this.msnConfirmarRastroHistorico = msnConfirmarRastroHistorico;
   }

   public BigInteger getBackUp() {
      return backUp;
   }

   public void setBackUp(BigInteger backUp) {
      this.backUp = backUp;
   }

   public String getNombreTablaRastro() {
      return nombreTablaRastro;
   }

   public void setNombreTablaRastro(String nombreTablaRastro) {
      this.nombreTablaRastro = nombreTablaRastro;
   }

   public String getNombreXML() {
      return nombreXML;
   }

   public void setNombreXML(String nombreXML) {
      this.nombreXML = nombreXML;
   }

   public String getNombreTabla() {
      return nombreTabla;
   }

   public void setNombreTabla(String nombreTabla) {
      this.nombreTabla = nombreTabla;
   }

   public boolean isCambiosPagina() {
      return cambiosPagina;
   }

   public void setCambiosPagina(boolean cambiosPagina) {
      this.cambiosPagina = cambiosPagina;
   }

   public String getAltoTablaReforma() {
      return altoTablaReforma;
   }

   public void setAltoTablaReforma(String setAltoTablaReforma) {
      this.altoTablaReforma = setAltoTablaReforma;
   }

   public String getAltoTablaDetalles() {
      return altoTablaDetalles;
   }

   public void setAltoTablaDetalles(String setAltoTablaDetalles) {
      this.altoTablaDetalles = setAltoTablaDetalles;
   }

   public short getCodigoReformaClonar() {
      return codigoReformaClonar;
   }

   public void setCodigoReformaClonar(short setCodigoReformaClonar) {
      this.codigoReformaClonar = setCodigoReformaClonar;
   }

   public String getNombreReformaClonar() {
      return nombreReformaClonar;
   }

   public void setNombreReformaClonar(String setNombreReformaClonar) {
      this.nombreReformaClonar = setNombreReformaClonar.toUpperCase();
   }

   public List<ReformasLaborales> getLovReformasLaborales() {
      lovReformasLaborales = administrarReformaLaboral.listaReformasLaborales();
      return lovReformasLaborales;
   }

   public void setLovReformasLaborales(List<ReformasLaborales> setLovReformasLaborales) {
      this.lovReformasLaborales = setLovReformasLaborales;
   }

   public List<ReformasLaborales> getFiltrarLovReformasLaborales() {
      return filtrarLovReformasLaborales;
   }

   public void setFiltrarLovReformasLaborales(List<ReformasLaborales> setFiltrarLovReformasLaborales) {
      this.filtrarLovReformasLaborales = setFiltrarLovReformasLaborales;
   }

   public ReformasLaborales getReformaLaboralLOVSeleccionada() {
      return reformaLaboralLOVSeleccionada;
   }

   public void setReformaLaboralLOVSeleccionada(ReformasLaborales setReformaLaboralSeleccionado) {
      this.reformaLaboralLOVSeleccionada = setReformaLaboralSeleccionado;
   }

   public ReformasLaborales getReformaLaboralAClonar() {
      return reformaLaboralAClonar;
   }

   public void setReformaLaboralAClonar(ReformasLaborales setReformaLaboralAClonar) {
      this.reformaLaboralAClonar = setReformaLaboralAClonar;
   }

   public ReformasLaborales getReformaLaboralSeleccionada() {
      return reformaLaboralSeleccionada;
   }

   public void setReformaLaboralSeleccionada(ReformasLaborales reformaLaboralSeleccionada) {
      this.reformaLaboralSeleccionada = reformaLaboralSeleccionada;
   }

   public DetallesReformasLaborales getDetalleReformaLSeleccionado() {
      return detalleReformaLSeleccionado;
   }

   public void setDetalleReformaLSeleccionado(DetallesReformasLaborales detalleReformaLSeleccionado) {
      this.detalleReformaLSeleccionado = detalleReformaLSeleccionado;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosReformaLaboral");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroDetalle() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDetalleReformaLaboral");
      infoRegistroDetalle = String.valueOf(tabla.getRowCount());
      return infoRegistroDetalle;
   }

   public void setInfoRegistroDetalle(String infoRegistroDetalle) {
      this.infoRegistroDetalle = infoRegistroDetalle;
   }

   public String getInfoRegistroLovReforma() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:lovReformaLaboral");
      infoRegistroLovReforma = String.valueOf(tabla.getRowCount());
      return infoRegistroLovReforma;
   }

   public void setInfoRegistroLovReforma(String infoRegistroLovReforma) {
      this.infoRegistroLovReforma = infoRegistroLovReforma;
   }

}
