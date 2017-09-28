/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Bancos;
import Entidades.Ciudades;
import Entidades.Direcciones;
import Exportar.ExportarPDF;
import Exportar.ExportarXLS;
import InterfaceAdministrar.AdministrarBancosInterface;
import InterfaceAdministrar.AdministrarCiudadesInterface;
import InterfaceAdministrar.AdministrarDireccionesInterface;
import InterfaceAdministrar.AdministrarRastrosInterface;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import ControlNavegacion.ControlListaNavegacion;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.faces.application.FacesMessage;
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
@Named(value = "controlDireccionesBancos")
@SessionScoped
public class ControlDireccionesBancos implements Serializable {

   private static Logger log = Logger.getLogger(ControlDireccionesBancos.class);

   @EJB
   AdministrarDireccionesInterface administrarDirecciones;
   @EJB
   AdministrarCiudadesInterface administrarCiudades;
   @EJB
   AdministrarRastrosInterface administrarRastros;
   @EJB
   AdministrarBancosInterface administrarBancos;

   private List<Direcciones> listaDirecciones;
   private List<Direcciones> listaDireccionesFiltrar;
   private List<Direcciones> listaDireccionesCrear;
   private List<Direcciones> listaDireccionesBorrar;
   private List<Direcciones> listaDireccionesModificar;
   private Direcciones direccionSeleccionada;
   private Direcciones duplicarDireccion;
   private Direcciones editarDireccion;
   private Direcciones nuevaDireccion;
   private Bancos banco;
   private List<Ciudades> lovCiudades;
   private List<Ciudades> listaCiudadesFiltrar;
   private Ciudades ciudadSeleccionada;
   private boolean aceptar;
   private int tipoActualizacion; //Activo/Desactivo Crtl + F11
   private int bandera;
   private boolean permitirIndex;
   private Column dFecha, dUbicacionPrincipal, dDescripcionUbicacionPrincipal, dUbicacionSecundaria;
   private Column dDescripcionUbicacionSecundaria, dInterior, dCiudad;
   private int cualCelda, tipoLista;
   private boolean guardado;
   private BigInteger l;
   private int k;
   private String mensajeValidacion;
   private String altoTabla;
   private String infoRegistro, infoRegistroCiudades;
   private boolean activarLOV;
   private String paginaAnterior = "nominaf";
   private Map<String, Object> mapParametros = new LinkedHashMap<String, Object>();

   public ControlDireccionesBancos() {
      permitirIndex = true;
      aceptar = true;
      tipoLista = 0;
      nuevaDireccion = new Direcciones();
      nuevaDireccion.setCiudad(new Ciudades());
      nuevaDireccion.setFechavigencia(new Date());
      nuevaDireccion.setTipoppal("K");
      nuevaDireccion.setTiposecundario("C");
      duplicarDireccion = new Direcciones();
      duplicarDireccion.setCiudad(new Ciudades());
      duplicarDireccion.setFechavigencia(new Date());
      duplicarDireccion.setTipoppal("K");
      duplicarDireccion.setTiposecundario("C");
      listaDireccionesBorrar = new ArrayList<Direcciones>();
      listaDireccionesCrear = new ArrayList<Direcciones>();
      listaDireccionesModificar = new ArrayList<Direcciones>();
      direccionSeleccionada = null;
      listaDirecciones = null;
      k = 0;
      altoTabla = "270";
      activarLOV = true;
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
      String pagActual = "direccionesbancos";
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
      lovCiudades = null;
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
         administrarDirecciones.obtenerConexion(ses.getId());
         administrarCiudades.obtenerConexion(ses.getId());
         administrarRastros.obtenerConexion(ses.getId());
         administrarBancos.obtenerConexion(ses.getId());
      } catch (Exception e) {
         log.error("Error postconstruct " + this.getClass().getName() + ":  ", e);
         log.error("Causa: " + e.getCause());
      }
   }

   public void recibirPaginaEntrante(String pagina, BigInteger secuenciaBanco) {
      paginaAnterior = pagina;
      banco = administrarBancos.consultarBancosPorSecuencia(secuenciaBanco);
      listaDirecciones = null;
      getListaDirecciones();
      if (listaDirecciones != null) {
         if (!listaDirecciones.isEmpty()) {
            direccionSeleccionada = listaDirecciones.get(0);
         }
      }
   }

   public String retornarPagina() {
      return paginaAnterior;
   }

   public void cambiarIndice(Direcciones direccion, int celda) {
      direccionSeleccionada = direccion;
      cualCelda = celda;
      direccionSeleccionada.getSecuencia();
      if (cualCelda == 2) {
         deshabilitarLov();
         direccionSeleccionada.getPpal();
      } else if (cualCelda == 4) {
         deshabilitarLov();
         direccionSeleccionada.getSecundario();
      } else if (cualCelda == 5) {
         deshabilitarLov();
         direccionSeleccionada.getInterior();
      } else if (cualCelda == 6) {
         habilitarLov();
         direccionSeleccionada.getCiudad().getNombre();
      }
   }

   public void agregarNuevaDireccion() {
      int duplicados = 0;
      int pasa = 0;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      FacesContext c = FacesContext.getCurrentInstance();
      if (nuevaDireccion.getFechavigencia() == null) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         for (int t = 0; t < listaDirecciones.size(); t++) {
            if (listaDirecciones.get(t).getFechavigencia().equals(nuevaDireccion.getFechavigencia()) == true) {
               duplicados++;
            }
         }

         if (duplicados > 0) {
            mensajeValidacion = "Ya existe una dirección con la fecha ingresada";
         } else {
            pasa++;
         }
      }
      if (nuevaDireccion.getPpal() == null) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         pasa++;
      }

      if (nuevaDireccion.getCiudad().getNombre() == null) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         pasa++;
      }

      if (pasa == 3) {
         if (bandera == 1) {
            dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dFecha");
            dFecha.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dUbicacionPrincipal");
            dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dDescripcionUbicacionPrincipal");
            dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dUbicacionSecundaria");
            dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dDescripcionUbicacionSecundaria");
            dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dInterior");
            dInterior.setFilterStyle("display: none; visibility: hidden;");
            dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dCiudad");
            dCiudad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
            bandera = 0;
            listaDireccionesFiltrar = null;
            tipoLista = 0;
         }
         k++;
         l = BigInteger.valueOf(k);
         nuevaDireccion.setSecuencia(l);
         nuevaDireccion.setBanco(banco);
         listaDireccionesCrear.add(nuevaDireccion);
         listaDirecciones.add(nuevaDireccion);
         direccionSeleccionada = nuevaDireccion;
         contarRegistros();
         RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
         nuevaDireccion = new Direcciones();
         nuevaDireccion.setCiudad(new Ciudades());
         nuevaDireccion.setFechavigencia(new Date());
         nuevaDireccion.setTipoppal("K");
         nuevaDireccion.setTiposecundario("C");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('NuevoRegistroDireccion').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaDireccion");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaDireccion').show()");
      }
   }

   public void modificarDirecciones(Direcciones direccion) {
      direccionSeleccionada = direccion;
      if (!listaDireccionesCrear.contains(direccionSeleccionada)) {
         if (listaDireccionesModificar.isEmpty()) {
            listaDireccionesModificar.add(direccionSeleccionada);
         } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
            listaDireccionesModificar.add(direccionSeleccionada);
         }
         guardado = false;
      }
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
   }

   public void actualizarCiudades() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoActualizacion == 0) {
         direccionSeleccionada.setCiudad(ciudadSeleccionada);
         if (!listaDireccionesCrear.contains(direccionSeleccionada)) {
            if (listaDireccionesModificar.isEmpty()) {
               listaDireccionesModificar.add(direccionSeleccionada);
            } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
               listaDireccionesModificar.add(direccionSeleccionada);
            }
         }
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         permitirIndex = true;
         RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
      } else if (tipoActualizacion == 1) {
         nuevaDireccion.setCiudad(ciudadSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDireccion");
      } else if (tipoActualizacion == 2) {
         log.info(ciudadSeleccionada.getNombre());
         duplicarDireccion.setCiudad(ciudadSeleccionada);
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDireccion");
      }
      listaCiudadesFiltrar = null;
      ciudadSeleccionada = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVCiudades");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
      context.reset("formularioDialogos:LOVCiudades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVCiudades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').hide()");
   }

   public void cancelarCambioCiudades() {
      listaCiudadesFiltrar = null;
      aceptar = true;
      tipoActualizacion = -1;
      cualCelda = -1;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
      RequestContext.getCurrentInstance().update("formularioDialogos:LOVCiudades");
      RequestContext.getCurrentInstance().update("formularioDialogos:aceptarC");
      context.reset("formularioDialogos:LOVCiudades:globalFilter");
      RequestContext.getCurrentInstance().execute("PF('LOVCiudades').clearFilters()");
      RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').hide()");

   }

   public void activarCtrlF11() {
      FacesContext c = FacesContext.getCurrentInstance();
      if (bandera == 0) {
         log.info("Activar");
         log.info("TipoLista= " + tipoLista);
         dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dFecha");
         dFecha.setFilterStyle("width: 85% !important");
         dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dUbicacionPrincipal");
         dUbicacionPrincipal.setFilterStyle("width: 85% !important");
         dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dDescripcionUbicacionPrincipal");
         dDescripcionUbicacionPrincipal.setFilterStyle("width: 85% !important");
         dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dUbicacionSecundaria");
         dUbicacionSecundaria.setFilterStyle("width: 85% !important");
         dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dDescripcionUbicacionSecundaria");
         dDescripcionUbicacionSecundaria.setFilterStyle("width: 85% !important");
         dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dInterior");
         dInterior.setFilterStyle("width: 85% !important");
         dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dCiudad");
         dCiudad.setFilterStyle("width: 85% !important");
         altoTabla = "250";
         RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
         bandera = 1;
         tipoLista = 1;
      } else if (bandera == 1) {
         log.info("Desactivar");
         log.info("TipoLista= " + tipoLista);
         dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dFecha");
         dFecha.setFilterStyle("display: none; visibility: hidden;");
         dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dUbicacionPrincipal");
         dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
         dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dDescripcionUbicacionPrincipal");
         dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
         dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dUbicacionSecundaria");
         dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
         dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dDescripcionUbicacionSecundaria");
         dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
         dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dInterior");
         dInterior.setFilterStyle("display: none; visibility: hidden;");
         dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dCiudad");
         dCiudad.setFilterStyle("display: none; visibility: hidden;");
         altoTabla = "270";
         RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
         bandera = 0;
         listaDireccionesFiltrar = null;
         tipoLista = 0;
      }
   }

   public void limpiarNuevaDireccion() {
      nuevaDireccion = new Direcciones();
      nuevaDireccion.setCiudad(new Ciudades());
      nuevaDireccion.setFechavigencia(new Date());
      nuevaDireccion.setTipoppal("K");
      nuevaDireccion.setTiposecundario("C");
   }

   public void listaValoresBoton() {
      if (direccionSeleccionada != null) {
         RequestContext context = RequestContext.getCurrentInstance();
         if (cualCelda == 6) {
            habilitarLov();
            contarRegistrosCiudades();
            RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
            RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
            tipoActualizacion = 0;
         }
      }
   }

   public void activarAceptar() {
      aceptar = false;
   }

   public void asignarIndex(Direcciones direccion, int dlg, int LND) {
      direccionSeleccionada = direccion;
      tipoActualizacion = LND;
      if (dlg == 1) {
         contarRegistrosCiudades();
         RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
      }
   }

   public void editarCelda() {
      if (direccionSeleccionada != null) {
         editarDireccion = direccionSeleccionada;
         if (cualCelda == 0) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarFecha");
            RequestContext.getCurrentInstance().execute("PF('editarFecha').show()");
            cualCelda = -1;
         } else if (cualCelda == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarUbicacionesPrincipales");
            RequestContext.getCurrentInstance().execute("PF('editarUbicacionesPrincipales').show()");
            cualCelda = -1;
         } else if (cualCelda == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionesUbicacionesPrincipales");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionesUbicacionesPrincipales').show()");
            cualCelda = -1;
         } else if (cualCelda == 3) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarUbicacionesSecundarias");
            RequestContext.getCurrentInstance().execute("PF('editarUbicacionesSecundarias').show()");
            cualCelda = -1;
         } else if (cualCelda == 4) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarDescripcionesUbicacionesSecundarias");
            RequestContext.getCurrentInstance().execute("PF('editarDescripcionesUbicacionesSecundarias').show()");
            cualCelda = -1;
         } else if (cualCelda == 5) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarBarrios");
            RequestContext.getCurrentInstance().execute("PF('editarBarrios').show()");
            cualCelda = -1;
         } else if (cualCelda == 6) {
            RequestContext.getCurrentInstance().update("formularioDialogos:editarCiudades");
            RequestContext.getCurrentInstance().execute("PF('editarCiudades').show()");
            cualCelda = -1;
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void duplicarD() {
      if (direccionSeleccionada != null) {
         duplicarDireccion = new Direcciones();
         k++;
         l = BigInteger.valueOf(k);
         duplicarDireccion.setSecuencia(l);
         duplicarDireccion.setFechavigencia(direccionSeleccionada.getFechavigencia());
         duplicarDireccion.setTipoppal(direccionSeleccionada.getTipoppal());
         duplicarDireccion.setPpal(direccionSeleccionada.getPpal());
         duplicarDireccion.setTiposecundario(direccionSeleccionada.getTiposecundario());
         duplicarDireccion.setSecundario(direccionSeleccionada.getSecundario());
         duplicarDireccion.setInterior(direccionSeleccionada.getInterior());
         duplicarDireccion.setCiudad(direccionSeleccionada.getCiudad());
         altoTabla = "270";
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDireccion");
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDireccion').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void confirmarDuplicar() {
      int duplicados = 0;
      int pasa = 0;
      mensajeValidacion = " ";
      RequestContext context = RequestContext.getCurrentInstance();
      FacesContext c = FacesContext.getCurrentInstance();
      if (duplicarDireccion.getFechavigencia() == null) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         for (int t = 0; t < listaDirecciones.size(); t++) {
            if (listaDirecciones.get(t).getFechavigencia().equals(duplicarDireccion.getFechavigencia()) == true) {
               duplicados++;
            }
         }
         if (duplicados > 0) {
            mensajeValidacion = "Ya existe una dirección con la fecha ingresada";
         } else {
            pasa++;
         }
      }
      if (duplicarDireccion.getPpal() == null) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         pasa++;
      }

      if (duplicarDireccion.getCiudad().getNombre() == null) {
         mensajeValidacion = " Los campos marcados con asterisco son obligatorios";
      } else {
         pasa++;
      }

      if (pasa == 3) {
         if (bandera == 1) {
            dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dFecha");
            dFecha.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dUbicacionPrincipal");
            dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dDescripcionUbicacionPrincipal");
            dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
            dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dUbicacionSecundaria");
            dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dDescripcionUbicacionSecundaria");
            dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
            dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dInterior");
            dInterior.setFilterStyle("display: none; visibility: hidden;");
            dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dCiudad");
            dCiudad.setFilterStyle("display: none; visibility: hidden;");
            RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
            bandera = 0;
            listaDireccionesFiltrar = null;
            tipoLista = 0;
         }
         listaDirecciones.add(duplicarDireccion);
         listaDireccionesCrear.add(duplicarDireccion);
         direccionSeleccionada = duplicarDireccion;
         contarRegistros();
         duplicarDireccion = new Direcciones();
         RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
         RequestContext.getCurrentInstance().execute("PF('DuplicarRegistroDireccion').hide()");
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:validacionNuevaDireccion");
         RequestContext.getCurrentInstance().execute("PF('validacionNuevaDireccion').show()");
      }
   }

   public void guardarCambiosDireccion() {
      try {
         if (guardado == false) {
            if (!listaDireccionesBorrar.isEmpty()) {
               administrarDirecciones.borrarDirecciones(listaDireccionesBorrar);
               listaDireccionesBorrar.clear();
            }
            if (!listaDireccionesCrear.isEmpty()) {
               administrarDirecciones.crearDirecciones(listaDireccionesCrear);
               listaDireccionesCrear.clear();
            }
            if (!listaDireccionesModificar.isEmpty()) {
               administrarDirecciones.modificarDirecciones(listaDireccionesModificar);
               listaDireccionesModificar.clear();
            }
            log.info("Se guardaron los datos con exito");
            listaDirecciones = null;
            getListaDirecciones();
            contarRegistros();
            RequestContext context = RequestContext.getCurrentInstance();
            RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
            guardado = true;
            permitirIndex = true;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
            FacesMessage msg = new FacesMessage("Información", "Se guardaron los datos con éxito");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            RequestContext.getCurrentInstance().update("form:growl");
            k = 0;
         }
         direccionSeleccionada = null;
      } catch (Exception e) {
         log.info("entró al catch... error en el guardado de direcciones " + e.toString());
         FacesMessage msg = new FacesMessage("Información", "Error en el guardado, por favor intente nuevamente");
         FacesContext.getCurrentInstance().addMessage(null, msg);
         RequestContext.getCurrentInstance().update("form:growl");
      }

   }

   public void salir() {
      limpiarListasValor();
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dFecha");
         dFecha.setFilterStyle("display: none; visibility: hidden;");
         dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dUbicacionPrincipal");
         dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
         dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dDescripcionUbicacionPrincipal");
         dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
         dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dUbicacionSecundaria");
         dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
         dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dDescripcionUbicacionSecundaria");
         dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
         dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dInterior");
         dInterior.setFilterStyle("display: none; visibility: hidden;");
         dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dCiudad");
         dCiudad.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
         bandera = 0;
         listaDireccionesFiltrar = null;
         tipoLista = 0;
      }
      listaDireccionesBorrar.clear();
      listaDireccionesCrear.clear();
      listaDireccionesModificar.clear();
      direccionSeleccionada = null;
      listaDirecciones = null;
      guardado = true;
      contarRegistros();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      permitirIndex = true;
   }

   public void cancelarModificacion() {
      if (bandera == 1) {
         FacesContext c = FacesContext.getCurrentInstance();
         dFecha = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dFecha");
         dFecha.setFilterStyle("display: none; visibility: hidden;");
         dUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dUbicacionPrincipal");
         dUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
         dDescripcionUbicacionPrincipal = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dDescripcionUbicacionPrincipal");
         dDescripcionUbicacionPrincipal.setFilterStyle("display: none; visibility: hidden;");
         dUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dUbicacionSecundaria");
         dUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
         dDescripcionUbicacionSecundaria = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dDescripcionUbicacionSecundaria");
         dDescripcionUbicacionSecundaria.setFilterStyle("display: none; visibility: hidden;");
         dInterior = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dInterior");
         dInterior.setFilterStyle("display: none; visibility: hidden;");
         dCiudad = (Column) c.getViewRoot().findComponent("form:datosDireccionesBanco:dCiudad");
         dCiudad.setFilterStyle("display: none; visibility: hidden;");
         RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
         bandera = 0;
         listaDireccionesFiltrar = null;
         tipoLista = 0;
      }

      listaDireccionesBorrar.clear();
      listaDireccionesCrear.clear();
      listaDireccionesModificar.clear();
      k = 0;
      listaDirecciones = null;
      direccionSeleccionada = null;
      contarRegistros();
      guardado = true;
      permitirIndex = true;
      RequestContext context = RequestContext.getCurrentInstance();
      RequestContext.getCurrentInstance().update("form:ACEPTAR");
      RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
   }

   public void limpiarDuplicarDireccion() {
      duplicarDireccion = new Direcciones();
   }

   public void autocompletarNuevoyDuplicado(String valorConfirmar, int tipoNuevo) {
      int coincidencias = 0;
      RequestContext context = RequestContext.getCurrentInstance();
      if (tipoNuevo == 1) {
         nuevaDireccion.getCiudad().setNombre(nuevaDireccion.getCiudad().getNombre());
      } else if (tipoNuevo == 2) {
         duplicarDireccion.getCiudad().setNombre(duplicarDireccion.getCiudad().getNombre());
      }
      for (int i = 0; i < lovCiudades.size(); i++) {
         if (lovCiudades.get(i).getNombre().startsWith(valorConfirmar.toUpperCase())) {
            //indiceUnicoElemento = i;
            coincidencias++;
         }
      }
      if (coincidencias == 1) {
         if (tipoNuevo == 1) {
            nuevaDireccion.setCiudad(ciudadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudad");
         } else if (tipoNuevo == 2) {
            duplicarDireccion.setCiudad(ciudadSeleccionada);
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudad");
         }
         lovCiudades = null;
         getLovCiudades();
      } else {
         RequestContext.getCurrentInstance().update("formularioDialogos:ciudadesDialogo");
         RequestContext.getCurrentInstance().execute("PF('ciudadesDialogo').show()");
         tipoActualizacion = tipoNuevo;
         if (tipoNuevo == 1) {
            RequestContext.getCurrentInstance().update("formularioDialogos:nuevaCiudad");
         } else if (tipoNuevo == 2) {
            RequestContext.getCurrentInstance().update("formularioDialogos:duplicarCiudad");
         }
      }
   }

   public void borrarDirecciones() {
      if (direccionSeleccionada != null) {
         if (!listaDireccionesModificar.isEmpty() && listaDireccionesModificar.contains(direccionSeleccionada)) {
            int modIndex = listaDireccionesModificar.indexOf(direccionSeleccionada);
            listaDireccionesModificar.remove(modIndex);
            listaDireccionesBorrar.add(direccionSeleccionada);
         } else if (!listaDireccionesCrear.isEmpty() && listaDireccionesCrear.contains(direccionSeleccionada)) {
            int crearIndex = listaDireccionesCrear.indexOf(direccionSeleccionada);
            listaDireccionesCrear.remove(crearIndex);
         } else {
            listaDireccionesBorrar.add(direccionSeleccionada);
         }
         listaDirecciones.remove(direccionSeleccionada);
         if (tipoLista == 1) {
            listaDireccionesFiltrar.remove(direccionSeleccionada);
         }
         RequestContext context = RequestContext.getCurrentInstance();
         RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
         direccionSeleccionada = null;
         if (guardado == true) {
            guardado = false;
            RequestContext.getCurrentInstance().update("form:ACEPTAR");
         }
      } else {
         RequestContext.getCurrentInstance().execute("PF('seleccionarRegistro').show()");
      }
   }

   public void exportPDF() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDireccionesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarPDF();
      exporter.export(context, tabla, "DireccionesBancosPDF", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void exportXLS() throws IOException {
      DataTable tabla = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formExportar:datosDireccionesExportar");
      FacesContext context = FacesContext.getCurrentInstance();
      Exporter exporter = new ExportarXLS();
      exporter.export(context, tabla, "DireccionesBancosXLS", false, false, "UTF-8", null, null);
      context.responseComplete();
   }

   public void verificarRastro() {
      RequestContext context = RequestContext.getCurrentInstance();
      if (direccionSeleccionada != null) {
         int resultado = administrarRastros.obtenerTabla(direccionSeleccionada.getSecuencia(), "DIRECCIONES");
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
      } else if (administrarRastros.verificarHistoricosTabla("DIRECCIONES")) {
         RequestContext.getCurrentInstance().execute("PF('confirmarRastroHistorico').show()");
      } else {
         RequestContext.getCurrentInstance().execute("PF('errorRastroHistorico').show()");
      }
   }

   public void seleccionarTipoPpal(String estadoTipoPpal, Direcciones direccion) {
      direccionSeleccionada = direccion;
      if (estadoTipoPpal.equals("CALLE")) {
         direccionSeleccionada.setTipoppal("C");
         direccionSeleccionada.setTiposecundario("K");
         direccionSeleccionada.setEstadoTipoSecundario("CARRERA");
      } else if (estadoTipoPpal.equals("CARRERA")) {
         direccionSeleccionada.setTipoppal("K");
         direccionSeleccionada.setTiposecundario("C");
         direccionSeleccionada.setEstadoTipoSecundario("CALLE");
      } else if (estadoTipoPpal.equals("AVENIDA CALLE")) {
         direccionSeleccionada.setTipoppal("A");
         direccionSeleccionada.setTiposecundario("K");
         direccionSeleccionada.setEstadoTipoSecundario("CARRERA");
      } else if (estadoTipoPpal.equals("AVENIDA CARRERA")) {
         direccionSeleccionada.setTipoppal("M");
         direccionSeleccionada.setTiposecundario("C");
         direccionSeleccionada.setEstadoTipoSecundario("CALLE");
      } else if (estadoTipoPpal.equals("DIAGONAL")) {
         direccionSeleccionada.setTipoppal("D");
         direccionSeleccionada.setTiposecundario("T");
         direccionSeleccionada.setEstadoTipoSecundario("TRANSVERSAL");
      } else if (estadoTipoPpal.equals("TRANSVERSAL")) {
         direccionSeleccionada.setTipoppal("T");
         direccionSeleccionada.setTiposecundario("D");
         direccionSeleccionada.setEstadoTipoSecundario("DIAGONAL");
      } else if (estadoTipoPpal.equals("OTROS")) {
         direccionSeleccionada.setTipoppal("O");
      }
      if (!listaDireccionesCrear.contains(direccionSeleccionada)) {
         if (listaDireccionesModificar.isEmpty()) {
            listaDireccionesModificar.add(direccionSeleccionada);
         } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
            listaDireccionesModificar.add(direccionSeleccionada);
         }
      }
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
   }

   public void seleccionarTipoPpalNuevaDireccion(String estadoTipoPpal, int tipoNuevo) {
      if (tipoNuevo == 1) {
         if (estadoTipoPpal.equals("CALLE")) {
            nuevaDireccion.setTipoppal("C");
            nuevaDireccion.setTiposecundario("K");
            nuevaDireccion.setEstadoTipoSecundario("CARRERA");
         } else if (estadoTipoPpal.equals("CARRERA")) {
            nuevaDireccion.setTipoppal("K");
            nuevaDireccion.setTiposecundario("C");
            nuevaDireccion.setEstadoTipoSecundario("CALLE");
         } else if (estadoTipoPpal.equals("AVENIDA CALLE")) {
            nuevaDireccion.setTipoppal("A");
            nuevaDireccion.setTiposecundario("K");
            nuevaDireccion.setEstadoTipoSecundario("CARRERA");
         } else if (estadoTipoPpal.equals("AVENIDA CARRERA")) {
            nuevaDireccion.setTipoppal("M");
            nuevaDireccion.setTiposecundario("C");
            nuevaDireccion.setEstadoTipoSecundario("CALLE");
         } else if (estadoTipoPpal.equals("DIAGONAL")) {
            nuevaDireccion.setTipoppal("D");
            nuevaDireccion.setTiposecundario("T");
            nuevaDireccion.setEstadoTipoSecundario("TRANSVERSAL");
         } else if (estadoTipoPpal.equals("TRANSVERSAL")) {
            nuevaDireccion.setTipoppal("T");
            nuevaDireccion.setTiposecundario("D");
            nuevaDireccion.setEstadoTipoSecundario("DIAGONAL");
         } else if (estadoTipoPpal.equals("OTROS")) {
            nuevaDireccion.setTipoppal("O");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDireccion");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUbicacionPrincipal");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUbicacionSecundaria");
      } else {
         if (estadoTipoPpal.equals("CALLE")) {
            duplicarDireccion.setTipoppal("C");
            duplicarDireccion.setTiposecundario("K");
            duplicarDireccion.setEstadoTipoSecundario("CARRERA");
         } else if (estadoTipoPpal.equals("CARRERA")) {
            duplicarDireccion.setTipoppal("K");
            duplicarDireccion.setTiposecundario("C");
            duplicarDireccion.setEstadoTipoSecundario("CALLE");
         } else if (estadoTipoPpal.equals("AVENIDA CALLE")) {
            duplicarDireccion.setTipoppal("A");
            duplicarDireccion.setTiposecundario("K");
            duplicarDireccion.setEstadoTipoSecundario("CARRERA");
         } else if (estadoTipoPpal.equals("AVENIDA CARRERA")) {
            duplicarDireccion.setTipoppal("M");
            duplicarDireccion.setTiposecundario("C");
            duplicarDireccion.setEstadoTipoSecundario("CALLE");
         } else if (estadoTipoPpal.equals("DIAGONAL")) {
            duplicarDireccion.setTipoppal("D");
            duplicarDireccion.setTiposecundario("T");
            duplicarDireccion.setEstadoTipoSecundario("TRANSVERSAL");
         } else if (estadoTipoPpal.equals("TRANSVERSAL")) {
            duplicarDireccion.setTipoppal("T");
            duplicarDireccion.setTiposecundario("D");
            duplicarDireccion.setEstadoTipoSecundario("DIAGONAL");
         } else if (estadoTipoPpal.equals("OTROS")) {
            duplicarDireccion.setTipoppal("O");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDireccion");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUbicacionPrincipal");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUbicacionSecundaria");
      }
   }

   public void seleccionarTipoSecundario(String estadoTipoSecundario, Direcciones direccion) {
      direccionSeleccionada = direccion;
      if (estadoTipoSecundario.equals("CALLE")) {
         direccionSeleccionada.setTiposecundario("C");
         direccionSeleccionada.setTipoppal("K");
         direccionSeleccionada.setEstadoTipoPpal("CARRERA");
      } else if (estadoTipoSecundario.equals("CARRERA")) {
         direccionSeleccionada.setTiposecundario("K");
         direccionSeleccionada.setTipoppal("C");
         direccionSeleccionada.setEstadoTipoPpal("CALLE");
      } else if (estadoTipoSecundario.equals("DIAGONAL")) {
         direccionSeleccionada.setTiposecundario("D");
         direccionSeleccionada.setTipoppal("T");
         direccionSeleccionada.setEstadoTipoPpal("TRANSVERSAL");
      } else if (estadoTipoSecundario.equals("TRANSVERSAL")) {
         direccionSeleccionada.setTiposecundario("T");
         direccionSeleccionada.setTipoppal("D");
         direccionSeleccionada.setEstadoTipoPpal("DIAGONAL");
      } else if (estadoTipoSecundario.equals("OTROS")) {
         direccionSeleccionada.setTiposecundario("O");
      }
      RequestContext.getCurrentInstance().update("form:dUbicacionSecundaria");
      RequestContext.getCurrentInstance().update("form:dUbicacionPrincipal");
      if (!listaDireccionesCrear.contains(direccionSeleccionada)) {
         if (listaDireccionesModificar.isEmpty()) {
            listaDireccionesModificar.add(direccionSeleccionada);
         } else if (!listaDireccionesModificar.contains(direccionSeleccionada)) {
            listaDireccionesModificar.add(direccionSeleccionada);
         }
      }
      if (guardado == true) {
         guardado = false;
         RequestContext.getCurrentInstance().update("form:ACEPTAR");
      }
      RequestContext.getCurrentInstance().update("form:datosDireccionesBanco");
   }

   public void seleccionarTipoSecundarioNuevaDireccion(String estadoTipoSecundario, int tipoNuevo) {
      if (tipoNuevo == 1) {
         if (estadoTipoSecundario.equals("CALLE")) {
            nuevaDireccion.setTiposecundario("C");
            nuevaDireccion.setTipoppal("K");
            nuevaDireccion.setEstadoTipoPpal("CARRERA");
         } else if (estadoTipoSecundario.equals("CARRERA")) {
            nuevaDireccion.setTiposecundario("K");
            nuevaDireccion.setTipoppal("C");
            nuevaDireccion.setEstadoTipoPpal("CALLE");
         } else if (estadoTipoSecundario.equals("DIAGONAL")) {
            nuevaDireccion.setTiposecundario("D");
            nuevaDireccion.setTipoppal("T");
            nuevaDireccion.setEstadoTipoPpal("TRANSVERSAL");
         } else if (estadoTipoSecundario.equals("TRANSVERSAL")) {
            nuevaDireccion.setTiposecundario("T");
            nuevaDireccion.setTipoppal("D");
            nuevaDireccion.setEstadoTipoPpal("DIAGONAL");
         } else if (estadoTipoSecundario.equals("OTROS")) {
            nuevaDireccion.setTiposecundario("O");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaDireccion");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUbicacionPrincipal");
         RequestContext.getCurrentInstance().update("formularioDialogos:nuevaUbicacionSecundaria");
      } else {
         if (estadoTipoSecundario.equals("CALLE")) {
            duplicarDireccion.setTiposecundario("C");
            duplicarDireccion.setTipoppal("K");
            duplicarDireccion.setEstadoTipoPpal("CARRERA");
         } else if (estadoTipoSecundario.equals("CARRERA")) {
            duplicarDireccion.setTiposecundario("K");
            duplicarDireccion.setTipoppal("C");
            duplicarDireccion.setEstadoTipoPpal("CALLE");
         } else if (estadoTipoSecundario.equals("DIAGONAL")) {
            duplicarDireccion.setTiposecundario("D");
            duplicarDireccion.setTipoppal("T");
            duplicarDireccion.setEstadoTipoPpal("TRANSVERSAL");
         } else if (estadoTipoSecundario.equals("TRANSVERSAL")) {
            duplicarDireccion.setTiposecundario("T");
            duplicarDireccion.setTipoppal("D");
            duplicarDireccion.setEstadoTipoPpal("DIAGONAL");
         } else if (estadoTipoSecundario.equals("OTROS")) {
            duplicarDireccion.setTiposecundario("O");
         }
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarDireccion");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUbicacionPrincipal");
         RequestContext.getCurrentInstance().update("formularioDialogos:duplicarUbicacionSecundaria");
      }
   }

   public void eventoFiltrar() {
      if (tipoLista == 0) {
         tipoLista = 1;
      }
      contarRegistros();
   }

   public void habilitarLov() {
      activarLOV = false;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void deshabilitarLov() {
      activarLOV = true;
      RequestContext.getCurrentInstance().update("form:listaValores");
   }

   public void contarRegistros() {
      RequestContext.getCurrentInstance().update("form:infoRegistro");
   }

   public void contarRegistrosCiudades() {
      RequestContext.getCurrentInstance().update("formularioDialogos:infoRegistroCiudades");
   }

   //////GETS Y SETS /////////
   public List<Direcciones> getListaDirecciones() {
      if (listaDirecciones == null) {
         if (banco.getSecuencia() != null) {
            listaDirecciones = administrarDirecciones.consultarDireccionesBanco(banco.getSecuencia());
         }
      }
      return listaDirecciones;
   }

   public void setListaDirecciones(List<Direcciones> listaDirecciones) {
      this.listaDirecciones = listaDirecciones;
   }

   public List<Direcciones> getListaDireccionesFiltrar() {
      return listaDireccionesFiltrar;
   }

   public void setListaDireccionesFiltrar(List<Direcciones> listaDireccionesFiltrar) {
      this.listaDireccionesFiltrar = listaDireccionesFiltrar;
   }

   public Direcciones getDireccionSeleccionada() {
      return direccionSeleccionada;
   }

   public void setDireccionSeleccionada(Direcciones direccionSeleccionada) {
      this.direccionSeleccionada = direccionSeleccionada;
   }

   public Direcciones getDuplicarDireccion() {
      return duplicarDireccion;
   }

   public void setDuplicarDireccion(Direcciones duplicarDireccion) {
      this.duplicarDireccion = duplicarDireccion;
   }

   public Direcciones getEditarDireccion() {
      return editarDireccion;
   }

   public void setEditarDireccion(Direcciones editarDireccion) {
      this.editarDireccion = editarDireccion;
   }

   public Direcciones getNuevaDireccion() {
      return nuevaDireccion;
   }

   public void setNuevaDireccion(Direcciones nuevaDireccion) {
      this.nuevaDireccion = nuevaDireccion;
   }

   public List<Ciudades> getLovCiudades() {
      if (lovCiudades == null) {
         lovCiudades = administrarCiudades.consultarCiudades();
      }
      return lovCiudades;
   }

   public void setLovCiudades(List<Ciudades> lovCiudades) {
      this.lovCiudades = lovCiudades;
   }

   public List<Ciudades> getListaCiudadesFiltrar() {
      return listaCiudadesFiltrar;
   }

   public void setListaCiudadesFiltrar(List<Ciudades> listaCiudadesFiltrar) {
      this.listaCiudadesFiltrar = listaCiudadesFiltrar;
   }

   public Ciudades getCiudadSeleccionada() {
      return ciudadSeleccionada;
   }

   public void setCiudadSeleccionada(Ciudades ciudadSeleccionada) {
      this.ciudadSeleccionada = ciudadSeleccionada;
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

   public String getAltoTabla() {
      return altoTabla;
   }

   public void setAltoTabla(String altoTabla) {
      this.altoTabla = altoTabla;
   }

   public String getInfoRegistro() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("form:datosDireccionesBanco");
      infoRegistro = String.valueOf(tabla.getRowCount());
      return infoRegistro;
   }

   public void setInfoRegistro(String infoRegistro) {
      this.infoRegistro = infoRegistro;
   }

   public String getInfoRegistroCiudades() {
      FacesContext c = FacesContext.getCurrentInstance();
      DataTable tabla = (DataTable) c.getViewRoot().findComponent("formularioDialogos:LOVCiudades");
      infoRegistroCiudades = String.valueOf(tabla.getRowCount());
      return infoRegistroCiudades;
   }

   public void setInfoRegistroCiudades(String infoRegistroCiudades) {
      this.infoRegistroCiudades = infoRegistroCiudades;
   }

   public boolean isActivarLOV() {
      return activarLOV;
   }

   public void setActivarLOV(boolean activarLOV) {
      this.activarLOV = activarLOV;
   }

}
